<template>
  <div
    ref="playerRef"
    class="music-player-container fixed z-50"
    :class="{ 'is-dragging': isDragging }"
    :style="playerPosition"
    @mousedown="startDrag"
    @touchstart="startDrag"
  >
    <!-- 首次进入"点我"提示 -->
    <div v-if="showClickTip" class="click-tip">点我</div>

    <!-- 主按钮 -->
    <button
      class="main-button relative w-16 h-16 md:w-20 md:h-20 rounded-full shadow-lg flex items-center justify-center hover:scale-105 transition-all duration-300"
      :class="{ 'is-playing': isPlaying }"
      @click="toggleExpand"
      aria-label="音乐控制中心"
    >
      <img
        src="@/assets/images/manbo.webp"
        alt="音乐控制背景"
        class="background-image absolute inset-0 w-full h-full rounded-full object-cover"
        draggable="false"
      />
    </button>

    <!-- 子按钮容器（弧形环绕） -->
    <div class="sub-buttons-container" :class="{ 'is-expanded': isExpanded }">
      <!-- 播放/暂停按钮 -->
      <button
        class="sub-button"
        :style="buttonPositions.playPause"
        @click.stop="togglePlay"
        aria-label="播放/暂停"
      >
        <font-awesome-icon v-if="!isPlaying" :icon="['fas', 'play']" class="text-white text-lg" />
        <font-awesome-icon v-else :icon="['fas', 'pause']" class="text-white text-lg" />
      </button>

      <!-- 音量按钮 -->
      <button
        class="sub-button"
        :style="buttonPositions.volume"
        @click.stop="showVolumeControl = true"
        aria-label="音量控制"
      >
        <font-awesome-icon :icon="['fas', 'volume-up']" class="text-white text-lg" />
      </button>

      <!-- 拖拽按钮 -->
      <button
        class="sub-button"
        :style="buttonPositions.move"
        @mousedown="startDrag"
        @touchstart="startDrag"
        aria-label="移动播放器"
      >
        <font-awesome-icon :icon="['fas', 'arrows']" class="text-white text-lg" />
      </button>
    </div>

    <!-- 音量控制滑块 -->
    <el-popover
      v-model:visible="showVolumeControl"
      trigger="click"
      placement="right"
      :width="200"
      popper-class="volume-popover"
    >
      <template #reference>
        <button
          class="sub-button"
          :style="buttonPositions.volume"
          @click.stop="() => {}"
          aria-label="音量控制"
        >
          <font-awesome-icon :icon="['fas', 'volume-up']" class="text-white text-lg" />
        </button>
      </template>
      <div class="volume-control-content">
        <span class="text-sm text-gray-600 mb-2 block">音量控制</span>
        <el-slider
          v-model="volumePercent"
          :min="0"
          :max="100"
          :step="1"
          @input="changeVolume"
          size="small"
        />
      </div>
    </el-popover>

    <!-- 音频元素 -->
    <audio
      ref="audioRef"
      :src="songs[currentSongIndex].src"
      loop
      :volume="volume"
      @playing="isPlaying = true"
      @pause="isPlaying = false"
      @ended="nextSong"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick, computed, reactive } from 'vue'
import type { CSSProperties } from 'vue'

// 核心状态定义
const audioRef = ref<HTMLAudioElement | null>(null)
const isPlaying = ref(false)
const volume = ref(0.3)
const playerRef = ref<HTMLDivElement | null>(null)
const navbarHeight = ref(0)
const isExpanded = ref(false)
const isDragging = ref(false)
const showVolumeControl = ref(false)
const hasAttemptedAutoPlay = ref(false)
const showClickTip = ref(true) // 首次进入"点我"提示

// 播放器位置状态（使用Vue响应式替代直接DOM操作）
const playerPosition = reactive({
  left: '10px',
  top: '110px',
  right: 'auto',
  bottom: 'auto'
})

// 子按钮位置状态
const buttonPositions = reactive({
  playPause: {} as CSSProperties,
  volume: {} as CSSProperties,
  move: {} as CSSProperties
})

// 音量百分比计算属性
const volumePercent = computed({
  get: () => Math.round(volume.value * 100),
  set: (value: number) => {
    volume.value = value / 100
    if (audioRef.value) audioRef.value.volume = volume.value
    storePlayState({ isPlaying: isPlaying.value, volume: volume.value })
  }
})

// 从localStorage获取存储的播放状态
const getStoredPlayState = () => {
  try {
    const storedState = localStorage.getItem('musicPlayerState')
    if (storedState) {
      const parsedState = JSON.parse(storedState)
      return {
        isPlaying: parsedState.isPlaying || false,
        volume: parsedState.volume || 0.3
      }
    }
  } catch (error) {
    console.warn('读取音乐播放器状态失败:', error)
  }
  return { isPlaying: false, volume: 0.3 }
}

// 存储播放状态到localStorage
const storePlayState = (state: { isPlaying: boolean; volume: number }) => {
  try {
    localStorage.setItem('musicPlayerState', JSON.stringify(state))
  } catch (error) {
    console.warn('存储音乐播放器状态失败:', error)
  }
}

// 歌曲列表
import manboSrc from '@/assets/audios/manbo.mp3'
const songs = ref([{ id: 1, name: '曼波', src: manboSrc }])
const currentSongIndex = ref(0)

// 播放/暂停控制
const togglePlay = () => {
  if (!audioRef.value) return

  try {
    if (isPlaying.value) {
      audioRef.value.pause()
      isPlaying.value = false
    } else {
      audioRef.value
        .play()
        .then(() => {
          isPlaying.value = true
        })
        .catch(err => {
          console.error('播放失败:', err)
        })
    }
    // 存储当前播放状态
    storePlayState({ isPlaying: isPlaying.value, volume: volume.value })
  } catch (err) {
    console.error('音乐控制失败:', err)
  }
}

// 播放指定歌曲
const playSong = (index: number) => {
  if (!audioRef.value) return

  currentSongIndex.value = index
  audioRef.value.src = songs.value[index].src
  audioRef.value.volume = volume.value

  audioRef.value
    .play()
    .then(() => {
      isPlaying.value = true
    })
    .catch(err => {
      console.error('播放失败:', err)
    })
  
  // 存储当前播放状态
  storePlayState({ isPlaying: true, volume: volume.value })
}

// 下一首
const nextSong = () => {
  let newIndex = currentSongIndex.value + 1
  if (newIndex >= songs.value.length) newIndex = 0
  playSong(newIndex)
}

// 音量控制（使用Element Plus滑块）
const changeVolume = (value: number | number[]) => {
  const volumeValue = Array.isArray(value) ? value[0] : value
  volume.value = volumeValue / 100
  if (audioRef.value) audioRef.value.volume = volume.value
  storePlayState({ isPlaying: isPlaying.value, volume: volume.value })
}

// 切换按钮展开/收起（隐藏"点我"提示）
const toggleExpand = () => {
  isExpanded.value = !isExpanded.value
  showClickTip.value = false // 点击后隐藏提示
}

// 开始拖拽（使用Vue响应式系统替代直接DOM操作）
const startDrag = (e: MouseEvent | TouchEvent) => {
  e.stopPropagation()
  isDragging.value = true

  // 获取鼠标/触摸位置
  const clientX = 'touches' in e ? e.touches[0].clientX : e.clientX
  const clientY = 'touches' in e ? e.touches[0].clientY : e.clientY

  // 获取当前位置（从响应式状态读取）
  const currentLeft = parseInt(playerPosition.left) || 10
  const currentTop = parseInt(playerPosition.top) || 110

  // 计算偏移量
  const offsetX = clientX - currentLeft
  const offsetY = clientY - currentTop

  // 处理拖拽移动
  const handleMove = (moveEvent: Event) => {
    const e = moveEvent as MouseEvent | TouchEvent
    if (!isDragging.value) return

    // 阻止移动端屏幕滚动
    if ('touches' in e) e.preventDefault()

    const moveClientX = 'touches' in e ? e.touches[0].clientX : e.clientX
    const moveClientY = 'touches' in e ? e.touches[0].clientY : e.clientY

    const windowWidth = window.innerWidth
    const windowHeight = window.innerHeight
    const playerWidth = 80 // 主按钮宽度
    const playerHeight = 80 // 主按钮高度

    // 计算新位置（边界限制：顶部间距适配导航栏）
    let newX = moveClientX - offsetX
    let newY = moveClientY - offsetY

    newX = Math.max(10, Math.min(newX, windowWidth - playerWidth - 10))
    newY = Math.max(
      navbarHeight.value + 50,
      Math.min(newY, windowHeight - playerHeight - 30)
    )

    // 使用Vue响应式系统更新位置
    playerPosition.left = `${newX}px`
    playerPosition.top = `${newY}px`
    playerPosition.bottom = 'auto'
    playerPosition.right = 'auto'
  }

  // 结束拖拽
  const handleEnd = () => {
    isDragging.value = false
    document.removeEventListener('mousemove', handleMove as EventListener)
    document.removeEventListener('mouseup', handleEnd as EventListener)
    document.removeEventListener('touchmove', handleMove as EventListener)
    document.removeEventListener('touchend', handleEnd as EventListener)
  }

  // 添加事件监听器
  document.addEventListener('mousemove', handleMove as EventListener)
  document.addEventListener('mouseup', handleEnd as EventListener)
  document.addEventListener('touchmove', handleMove as EventListener, {
    passive: false,
  })
  document.addEventListener('touchend', handleEnd as EventListener)
}



// 获取导航栏高度
const getNavbarHeight = () => {
  const navbar = document.querySelector('.navbar')
  navbarHeight.value = navbar
    ? navbar.getBoundingClientRect().height || 60
    : 60
}

// 更新子按钮位置（使用Vue响应式系统）
const updateButtonPositions = () => {
  const windowWidth = window.innerWidth
  const mainBtnCenterX = 40 // 主按钮中心X坐标（相对于容器）
  const mainBtnCenterY = 40 // 主按钮中心Y坐标（相对于容器）
  const mainBtnRadius = 40 // 主按钮半径
  const subBtnRadius = 20 // 子按钮半径
  const arcRadius = mainBtnRadius + subBtnRadius + 35 // 弧形半径

  // 判断弹出方向（基于当前播放器位置）
  const currentLeft = parseInt(playerPosition.left) || 10
  const mainBtnScreenX = currentLeft + mainBtnCenterX
  const leftSpace = mainBtnScreenX - 10
  const rightSpace = windowWidth - mainBtnScreenX - 10
  const isRightArc = leftSpace < rightSpace

  // 角度设置
  const startAngle = Math.PI / 2
  const endAngle = isRightArc ? -Math.PI / 6 : (Math.PI * 7) / 6

  // 按钮配置
  const buttons = [
    { key: 'move', order: 0 },
    { key: 'playPause', order: 2 }, // 注意：跳过了prev和next，因为我们只有3个按钮
    { key: 'volume', order: 4 }
  ]
  
  const btnCount = 5 // 总按钮数（包括未显示的）
  const angleStep = (endAngle - startAngle) / (btnCount - 1)

  // 清空位置
  Object.keys(buttonPositions).forEach(key => {
    buttonPositions[key as keyof typeof buttonPositions] = {}
  })

  // 计算每个按钮的位置
  buttons.forEach(btn => {
    const currentAngle = startAngle + angleStep * btn.order
    const targetX = mainBtnCenterX + Math.cos(currentAngle) * arcRadius
    const targetY = mainBtnCenterY + Math.sin(currentAngle) * arcRadius

    // 转换为相对位置
    const relLeft = targetX - subBtnRadius
    const relTop = targetY - subBtnRadius

    buttonPositions[btn.key as keyof typeof buttonPositions] = {
      left: `${relLeft}px`,
      top: `${relTop}px`
    }
  })
}

// 用户首次交互自动播放
const handleUserInteraction = async () => {
  if (hasAttemptedAutoPlay.value || !audioRef.value) return
  hasAttemptedAutoPlay.value = true

  // 只有当存储的状态是播放时才自动播放
  const storedState = getStoredPlayState()
  if (!storedState.isPlaying) return

  await nextTick()

  try {
    await audioRef.value.play()
    isPlaying.value = true
    // 存储播放状态
    storePlayState({ isPlaying: true, volume: volume.value })
  } catch (err) {
    console.warn('自动播放被浏览器限制，请用户手动点击播放按钮')
    // 即使自动播放失败，也要存储当前状态
    storePlayState({ isPlaying: false, volume: volume.value })
  }
}

// 点击页面其他地方关闭音量控制和展开的按钮
const handleDocumentClick = (e: MouseEvent) => {
  const target = e.target as HTMLElement
  if (playerRef.value && !playerRef.value.contains(target)) {
    showVolumeControl.value = false
    isExpanded.value = false
  }
}

// 设置播放器初始位置（使用Vue响应式系统）
const setInitialPlayerPosition = () => {
  playerPosition.left = '10px'
  playerPosition.top = `${navbarHeight.value + 50}px`
  playerPosition.bottom = 'auto'
  playerPosition.right = 'auto'
}

// 生命周期
onMounted(() => {
  // 初始化播放状态
  const storedState = getStoredPlayState()
  isPlaying.value = storedState.isPlaying
  volume.value = storedState.volume
  
  getNavbarHeight()
  nextTick(() => {
    setInitialPlayerPosition()
    updateButtonPositions() // 初始化按钮位置
  })

  window.addEventListener('resize', () => {
    getNavbarHeight()
    setInitialPlayerPosition()
    updateButtonPositions() // 窗口大小改变时重新计算按钮位置
  })

  document.addEventListener('click', handleUserInteraction, {
    once: true,
    passive: true,
  })
  document.addEventListener('touchstart', handleUserInteraction, {
    once: true,
    passive: true,
  })

  document.addEventListener('click', handleDocumentClick)
})

onUnmounted(() => {
  if (audioRef.value) audioRef.value.pause()
  window.removeEventListener('resize', getNavbarHeight)
  document.removeEventListener('click', handleUserInteraction)
  document.removeEventListener('touchstart', handleUserInteraction)
  document.removeEventListener('click', handleDocumentClick)
})
</script>

<style scoped>
/* 音乐播放器容器 */
.music-player-container {
  position: fixed;
  z-index: 50;
  user-select: none;
  cursor: default;
}

.music-player-container.is-dragging {
  cursor: move;
}

/* "点我"提示样式 */
.click-tip {
  position: absolute;
  left: calc(100% + 12px);
  top: 50%;
  transform: translateY(-50%);
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 4px 10px;
  border-radius: 20px;
  font-size: 14px;
  white-space: nowrap;
  animation: tipFade 1.5s ease-in-out infinite alternate;
  z-index: 3;
}

@keyframes tipFade {
  from {
    opacity: 0.8;
    transform: translateY(-50%) scale(1);
  }
  to {
    opacity: 1;
    transform: translateY(-50%) scale(1.05);
  }
}

/* 主按钮样式 */
.main-button {
  border: none;
  cursor: pointer;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.3);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
  background: transparent;
  outline: none;
  position: relative;
  z-index: 2;
}

.main-button.is-playing {
  animation: rotate 20s linear infinite;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.background-image {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
  filter: brightness(0.9);
  transition: filter 0.2s ease;
}

.main-button:hover .background-image {
  filter: brightness(0.8);
}

/* 子按钮容器 */
.sub-buttons-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

/* 子按钮样式：保持尺寸，间距通过角度调整 */
.sub-button {
  position: absolute;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: rgba(100, 108, 255, 0.9);
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
  opacity: 0;
  transform: translate(calc(50% - 20px), calc(50% - 20px)) scale(0);
  pointer-events: none;
  z-index: 1;
}

/*  hover时轻微放大，提升交互感 */
.sub-button:hover {
  background: rgba(100, 108, 255, 1);
  transform: translate(0, 0) scale(1.15); /* 放大比例从1.1→1.15，更明显 */
}

.sub-buttons-container.is-expanded .sub-button {
  opacity: 1;
  transform: translate(0, 0) scale(1);
  pointer-events: all;
}

/* 移动端适配：确保按钮触控区域足够大 */
@media (max-width: 768px) {
  .main-button {
    width: 64px;
    height: 64px; /* 主按钮触控区域不变 */
  }

  .sub-button {
    width: 42px; /* 子按钮宽度从38→42，增大触控区域 */
    height: 42px;
    transform: translate(calc(50% - 21px), calc(50% - 21px)) scale(0); /* 对应宽度调整偏移 */
  }

  .click-tip {
    font-size: 12px;
    padding: 3px 8px;
    left: calc(100% + 8px);
  }
}
</style>