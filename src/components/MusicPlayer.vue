<template>
  <div
    ref="playerRef"
    class="music-player-container fixed z-50"
    :class="{ 'is-dragging': isDragging }"
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
      />
    </button>

    <!-- 子按钮容器（弧形环绕） -->
    <div class="sub-buttons-container" :class="{ 'is-expanded': isExpanded }">
      <!-- 播放/暂停按钮 -->
      <button
        class="sub-button"
        :style="getButtonPosition().playPause"
        @click.stop="togglePlay"
        aria-label="播放/暂停"
      >
        <font-awesome-icon v-if="!isPlaying" :icon="['fas', 'play']" class="text-white text-lg" />
        <font-awesome-icon v-else :icon="['fas', 'pause']" class="text-white text-lg" />
      </button>

      <!-- 音量按钮 -->
      <button
        class="sub-button"
        :style="getButtonPosition().volume"
        @click.stop="toggleVolumeControl"
        aria-label="音量控制"
      >
        <font-awesome-icon :icon="['fas', 'volume-up']" class="text-white text-lg" />
      </button>

      <!-- 拖拽按钮 -->
      <button
        class="sub-button"
        :style="getButtonPosition().move"
        @mousedown="startDrag"
        @touchstart="startDrag"
        aria-label="移动播放器"
      >
        <font-awesome-icon :icon="['fas', 'arrows']" class="text-white text-lg" />
      </button>
    </div>

    <!-- 音量控制滑块 -->
    <div
      ref="volumeRef"
      v-if="showVolumeControl"
      class="volume-control bg-white rounded-lg shadow-lg z-20"
    >
      <input
        type="range"
        min="0"
        max="1"
        step="0.1"
        v-model="volume"
        @input="changeVolume(Number(($event.target as HTMLInputElement).value))"
        class="w-32"
        aria-label="音乐音量"
      />
    </div>

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
import { ref, onMounted, onUnmounted, nextTick } from 'vue'

// 核心状态定义
const audioRef = ref<HTMLAudioElement | null>(null)
const isPlaying = ref(false)
const volume = ref(0.3)
const playerRef = ref<HTMLDivElement | null>(null)
const volumeRef = ref<HTMLDivElement | null>(null)
const navbarHeight = ref(0)
const isExpanded = ref(false)
const isDragging = ref(false)
const showVolumeControl = ref(false)
const hasAttemptedAutoPlay = ref(false)
const showClickTip = ref(true) // 首次进入"点我"提示

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

// 音量控制
const changeVolume = (newVolume: number) => {
  volume.value = newVolume
  if (audioRef.value) audioRef.value.volume = newVolume
  // 存储当前音量状态
  storePlayState({ isPlaying: isPlaying.value, volume: volume.value })
}

// 切换音量控制显示
const toggleVolumeControl = (e?: MouseEvent) => {
  if (e) e.stopPropagation()
  showVolumeControl.value = !showVolumeControl.value
  if (showVolumeControl.value) {
    nextTick(() => adjustVolumePosition())
  }
}

// 切换按钮展开/收起（隐藏"点我"提示）
const toggleExpand = () => {
  isExpanded.value = !isExpanded.value
  showClickTip.value = false // 点击后隐藏提示
}

// 开始拖拽（解决移动端滑动问题）
const startDrag = (e: MouseEvent | TouchEvent) => {
  if (!playerRef.value) return
  e.stopPropagation()
  isDragging.value = true

  // 获取鼠标/触摸位置
  const clientX = 'touches' in e ? e.touches[0].clientX : e.clientX
  const clientY = 'touches' in e ? e.touches[0].clientY : e.clientY

  // 获取元素位置
  const rect = playerRef.value.getBoundingClientRect()
  // 计算偏移量
  const offsetX = clientX - rect.left
  const offsetY = clientY - rect.top

  // 处理拖拽移动
  const handleMove = (moveEvent: Event) => {
    const e = moveEvent as MouseEvent | TouchEvent
    if (!isDragging.value || !playerRef.value) return

    // 阻止移动端屏幕滚动
    if ('touches' in e) e.preventDefault()

    const moveClientX = 'touches' in e ? e.touches[0].clientX : e.clientX
    const moveClientY = 'touches' in e ? e.touches[0].clientY : e.clientY

    const windowWidth = window.innerWidth
    const windowHeight = window.innerHeight
    const playerWidth = playerRef.value.offsetWidth || 120
    const playerHeight = playerRef.value.offsetHeight || 120

    // 计算新位置（边界限制：顶部间距适配导航栏）
    let newX = moveClientX - offsetX
    let newY = moveClientY - offsetY

    newX = Math.max(10, Math.min(newX, windowWidth - playerWidth - 10))
    newY = Math.max(
      navbarHeight.value + 50, // 顶部间距，避免遮挡导航栏
      Math.min(newY, windowHeight - playerHeight - 30)
    )

    // 设置新位置
    playerRef.value.style.left = `${newX}px`
    playerRef.value.style.top = `${newY}px`
    playerRef.value.style.bottom = 'auto'
    playerRef.value.style.right = 'auto'
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

// 音量控制条位置自适应
const adjustVolumePosition = () => {
  if (!playerRef.value || !volumeRef.value || !showVolumeControl.value) return

  const playerEl = playerRef.value
  const volumeEl = volumeRef.value
  const windowWidth = window.innerWidth
  const windowHeight = window.innerHeight

  const playerRect = playerEl.getBoundingClientRect()
  const volumeWidth = volumeEl.offsetWidth || 128
  const volumeHeight = volumeEl.offsetHeight || 40

  // 计算可用空间
  const rightSpace = windowWidth - playerRect.right - 10
  const bottomSpace = windowHeight - playerRect.bottom - 10
  const leftSpace = playerRect.left - 10
  const topSpace = playerRect.top - navbarHeight.value - 10

  let newTop = 0
  let newLeft = 0

  // 优先右侧显示
  if (rightSpace >= volumeWidth) {
    newLeft = playerRect.right + 10
    newTop = playerRect.top + playerRect.height / 2 - volumeHeight / 2
  }
  // 右侧不够则下方显示
  else if (bottomSpace >= volumeHeight) {
    newLeft = playerRect.left + playerRect.width / 2 - volumeWidth / 2
    newTop = playerRect.bottom + 10
  }
  // 下方不够则左侧显示
  else if (leftSpace >= volumeWidth) {
    newLeft = playerRect.left - volumeWidth - 10
    newTop = playerRect.top + playerRect.height / 2 - volumeHeight / 2
  }
  // 左侧不够则上方显示（增加顶部间距检查）
  else if (topSpace >= volumeHeight + 10) {
    newLeft = playerRect.left + playerRect.width / 2 - volumeWidth / 2
    newTop = playerRect.top - volumeHeight - 10
  }
  // 兜底：屏幕中心附近
  else {
    newLeft = windowWidth / 2 - volumeWidth / 2
    newTop = windowHeight / 2 - volumeHeight / 2
  }

  // 最终边界检查
  newLeft = Math.max(10, Math.min(newLeft, windowWidth - volumeWidth - 10))
  newTop = Math.max(
    navbarHeight.value + 10,
    Math.min(newTop, windowHeight - volumeHeight - 10)
  )

  // 应用位置
  volumeEl.style.position = 'fixed'
  volumeEl.style.top = `${newTop}px`
  volumeEl.style.left = `${newLeft}px`
}

// 获取导航栏高度
const getNavbarHeight = () => {
  const navbar = document.querySelector('.navbar')
  navbarHeight.value = navbar
    ? navbar.getBoundingClientRect().height || 60
    : 60
}

// 子按钮位置计算：解决"离大按钮远"和"小按钮间距紧"
const getButtonPosition = () => {
  if (!playerRef.value) return {}

  const playerEl = playerRef.value
  const playerRect = playerEl.getBoundingClientRect()
  const windowWidth = window.innerWidth

  // 基础参数调整：
  // 1. subBtnRadius保持20px（按钮尺寸不变）
  // 2. arcRadius增量从60→35（缩小大按钮与小按钮的距离）
  const mainBtnCenterX = playerRect.left + playerRect.width / 2
  const mainBtnCenterY = playerRect.top + playerRect.height / 2
  const mainBtnRadius = playerRect.width / 2
  const subBtnRadius = 20
  const arcRadius = mainBtnRadius + subBtnRadius + 35 // 核心：减小增量，拉近大-小按钮距离

  // 1. 判断弹出方向（左/右）
  const leftSpace = mainBtnCenterX - 10
  const rightSpace = windowWidth - mainBtnCenterX - 10
  const isRightArc = leftSpace < rightSpace

  // 2. 角度跨度调整：从60°→120°（增大跨度，让小按钮分布更开）
  // 向右弹出：90°→-30°（总跨度120°），向左弹出：90°→210°（对称120°）
  const startAngle = isRightArc
    ? Math.PI / 2 // 向右弹出-起始角度（90°，更靠上）
    : Math.PI / 2 // 向左弹出-起始角度（90°，与右侧对称）
  const endAngle = isRightArc
    ? -Math.PI / 6 // 向右弹出-结束角度（-30°，更靠下）
    : (Math.PI * 7) / 6 // 向左弹出-结束角度（210°，与右侧对称）

  // 3. 固定按钮顺序：move→prev→playPause→next→volume
  const buttons = [
    { key: 'move', order: 0 }, // 最上方（拖拽按钮）
    { key: 'prev', order: 1 }, // 上一首（move下方）
    { key: 'playPause', order: 2 }, // 播放/暂停（中间）
    { key: 'next', order: 3 }, // 下一首（playPause下方）
    { key: 'volume', order: 4 }, // 最下方（音量按钮）
  ]
  const btnCount = buttons.length
  const angleStep = (endAngle - startAngle) / (btnCount - 1) // 角度间隔增大，小按钮间距变宽

  const positions: Record<string, { left: string; top: string }> = {}

  buttons.forEach(btn => {
    const currentAngle = startAngle + angleStep * btn.order

    // 计算子按钮坐标（跨度增大后，按钮间距离自动拉开）
    const targetX = mainBtnCenterX + Math.cos(currentAngle) * arcRadius
    const targetY = mainBtnCenterY + Math.sin(currentAngle) * arcRadius

    // 转换为相对位置
    const relLeft = targetX - playerRect.left - subBtnRadius
    const relTop = targetY - playerRect.top - subBtnRadius

    positions[btn.key] = {
      left: `${relLeft}px`,
      top: `${relTop}px`,
    }
  })

  return positions
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

// 设置播放器初始位置（移动端友好：左上偏移，避免遮挡核心内容）
const setInitialPlayerPosition = () => {
  if (!playerRef.value) return

  const playerEl = playerRef.value
  const initialLeft = 10
  const initialTop = navbarHeight.value + 50 // 顶部间距，避免遮挡导航栏

  playerEl.style.left = `${initialLeft}px`
  playerEl.style.top = `${initialTop}px`
}

// 生命周期
onMounted(() => {
  // 初始化播放状态
  const storedState = getStoredPlayState()
  isPlaying.value = storedState.isPlaying
  volume.value = storedState.volume
  
  getNavbarHeight()
  nextTick(() => setInitialPlayerPosition())

  window.addEventListener('resize', () => {
    getNavbarHeight()
    setInitialPlayerPosition()
    if (showVolumeControl.value) {
      nextTick(() => adjustVolumePosition())
    }
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

/* 音量控制滑块样式 */
.volume-control {
  position: fixed;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.25);
  padding: 10px 12px;
  min-width: 128px;
  animation: fadeIn 0.2s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: scale(0.9);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

/* 滑块样式优化 */
.volume-control input[type='range'] {
  -webkit-appearance: none;
  appearance: none;
  height: 6px;
  background: #e2e8f0;
  border-radius: 3px;
  outline: none;
  margin: 8px 0;
}

.volume-control input[type='range']::-webkit-slider-thumb {
  -webkit-appearance: none;
  appearance: none;
  width: 18px;
  height: 18px;
  background: #646cff;
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.2s ease;
}

.volume-control input[type='range']::-webkit-slider-thumb:hover {
  background: #535bf2;
  transform: scale(1.1);
}

.volume-control input[type='range']::-moz-range-thumb {
  width: 18px;
  height: 18px;
  background: #646cff;
  border-radius: 50%;
  cursor: pointer;
  border: none;
  transition: all 0.2s ease;
}

.volume-control input[type='range']::-moz-range-thumb:hover {
  background: #535bf2;
  transform: scale(1.1);
}
</style>