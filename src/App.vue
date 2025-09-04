<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick } from "vue"
import PolicyModal from "./components/PolicyModal.vue"

// 核心状态定义
const audioRef = ref<HTMLAudioElement | null>(null)
const isPlaying = ref(false)
const volume = ref(0.3)
const playerRef = ref<HTMLDivElement | null>(null)
const volumeRef = ref<HTMLDivElement | null>(null)
const isDragging = ref(false)
const elementOffset = ref({ x: 0, y: 0 })
const navbarHeight = ref(0)
const showControls = ref(false)
const hideControlsTimer = ref<ReturnType<typeof setTimeout> | null>(null)

// 播放/暂停控制
const togglePlay = () => {
  if (!audioRef.value) return

  try {
    if (isPlaying.value) {
      audioRef.value.pause()
      isPlaying.value = false
      showControls.value = true
      resetHideTimer(3000)
    } else {
      audioRef.value
        .play()
        .then(() => {
          isPlaying.value = true
          showControls.value = false
          clearHideTimer()
        })
        .catch((err) => {
          console.error("播放失败:", err)
          showControls.value = true
          resetHideTimer(3000)
        })
    }
  } catch (err) {
    console.error("音乐控制失败:", err)
    showControls.value = true
    resetHideTimer(3000)
  }
}

// 音量控制
const changeVolume = (newVolume: number) => {
  volume.value = newVolume
  if (audioRef.value) audioRef.value.volume = newVolume
  resetHideTimer(isPlaying.value ? 2000 : 3000)
  adjustVolumePosition()
}

// 控制显示/自动隐藏
const handlePlayerClick = () => {
  if (!audioRef.value) return

  if (isPlaying.value) {
    showControls.value = true
    resetHideTimer(2000)
    nextTick(() => adjustVolumePosition())
  } else {
    togglePlay()
  }
}

const resetHideTimer = (delay: number = 2000) => {
  clearHideTimer()
  if (showControls.value) {
    hideControlsTimer.value = setTimeout(() => {
      showControls.value = false
    }, delay)
  }
}

const clearHideTimer = () => {
  if (hideControlsTimer.value) {
    clearTimeout(hideControlsTimer.value)
    hideControlsTimer.value = null
  }
}

// 音量条位置自适应
const adjustVolumePosition = () => {
  if (!playerRef.value || !volumeRef.value || !showControls.value) return

  const playerEl = playerRef.value
  const volumeEl = volumeRef.value
  const windowWidth = window.innerWidth
  const windowHeight = window.innerHeight

  const playerRect = playerEl.getBoundingClientRect()
  const playerWidth = playerRect.width || 48
  const leftSpace = playerRect.left
  const rightSpace = windowWidth - playerRect.right
  const volumeWidth = volumeEl.offsetWidth || 128

  // 横向方向判断
  let targetDirection: "left" | "right" = "right"
  const isLeftSpaceEnough = leftSpace >= volumeWidth
  const isRightSpaceEnough = rightSpace >= volumeWidth

  if (isLeftSpaceEnough && !isRightSpaceEnough) {
    targetDirection = "left"
  } else if (!isLeftSpaceEnough && isRightSpaceEnough) {
    targetDirection = "right"
  } else {
    targetDirection = leftSpace >= rightSpace ? "left" : "right"
  }

  // 应用位置
  volumeEl.style.top = "50%"
  volumeEl.style.transform = "translateY(-50%)"
  volumeEl.style.bottom = "auto"

  if (targetDirection === "left") {
    volumeEl.style.left = "auto"
    volumeEl.style.right = `${playerWidth + 8}px`
  } else {
    volumeEl.style.right = "auto"
    volumeEl.style.left = `${playerWidth + 8}px`
  }

  // 兜底防溢出
  const volumeRect = volumeEl.getBoundingClientRect()
  if (volumeRect.left < 0) {
    volumeEl.style.left = "0"
    volumeEl.style.right = "auto"
  }
  if (volumeRect.right > windowWidth) {
    volumeEl.style.right = "0"
    volumeEl.style.left = "auto"
  }
  if (volumeRect.top < navbarHeight.value) {
    volumeEl.style.top = `${navbarHeight.value + 8}px`
    volumeEl.style.transform = "none"
  }
  if (volumeRect.bottom > windowHeight) {
    volumeEl.style.bottom = "8px"
    volumeEl.style.top = "auto"
    volumeEl.style.transform = "none"
  }
}

// 获取导航栏高度
const getNavbarHeight = () => {
  const navbar = document.querySelector(".navbar")
  navbarHeight.value = navbar ? navbar.getBoundingClientRect().height || 60 : 60
}

// 拖拽功能 - 完全重写以确保PC和移动端都能正常工作
const handleDragStart = (e: MouseEvent | TouchEvent) => {
  if (!playerRef.value) return

  // 阻止默认行为以避免冲突
  if (e.preventDefault) {
    e.preventDefault()
  }

  // 开始拖拽
  isDragging.value = true

  // 获取鼠标/触摸位置
  const clientX = "touches" in e ? e.touches[0].clientX : e.clientX
  const clientY = "touches" in e ? e.touches[0].clientY : e.clientY

  // 计算偏移量
  const rect = playerRef.value.getBoundingClientRect()
  elementOffset.value = {
    x: clientX - rect.left,
    y: clientY - rect.top,
  }

  // 添加全局事件监听器 - 关键修复：不使用capture模式
  document.addEventListener("mousemove", handleDragMove)
  document.addEventListener("mouseup", handleDragEnd)
  document.addEventListener("touchmove", handleDragMove, { passive: false })
  document.addEventListener("touchend", handleDragEnd)
}

const handleDragMove = (e: MouseEvent | TouchEvent) => {
  if (!isDragging.value || !playerRef.value) return

  // 阻止默认行为以确保流畅拖拽
  if (e.preventDefault) {
    e.preventDefault()
  }

  const playerEl = playerRef.value
  const windowWidth = window.innerWidth
  const windowHeight = window.innerHeight
  const playerWidth = playerEl.offsetWidth || 48
  const playerHeight = playerEl.offsetHeight || 48

  // 获取当前坐标
  const clientX = "touches" in e ? e.touches[0].clientX : e.clientX
  const clientY = "touches" in e ? e.touches[0].clientY : e.clientY

  // 计算新位置
  let newX = clientX - elementOffset.value.x
  let newY = clientY - elementOffset.value.y

  // 限制在窗口内
  newX = Math.max(0, Math.min(newX, windowWidth - playerWidth))
  newY = Math.max(
    navbarHeight.value + 10,
    Math.min(newY, windowHeight - playerHeight)
  )

  // 应用新位置
  playerEl.style.position = "fixed"
  playerEl.style.left = `${newX}px`
  playerEl.style.top = `${newY}px`
  playerEl.style.bottom = "auto"
  playerEl.style.right = "auto"

  // 调整音量条位置
  if (showControls.value) adjustVolumePosition()
}

const handleDragEnd = () => {
  isDragging.value = false

  // 移除事件监听器 - 确保与添加时完全一致
  document.removeEventListener("mousemove", handleDragMove)
  document.removeEventListener("mouseup", handleDragEnd)
  document.removeEventListener("touchmove", handleDragMove)
  document.removeEventListener("touchend", handleDragEnd)
}

// 协议检测自动播放
const checkPolicyAndAutoPlay = async () => {
  await nextTick()
  if (!audioRef.value) return

  const agreed = localStorage.getItem("hasAgreedToPolicies")
  if (agreed === "true") {
    audioRef.value
      .play()
      .then(() => {
        isPlaying.value = true
        showControls.value = false
        console.log("协议已同意，自动播放音乐")
      })
      .catch((err) => {
        console.warn("自动播放被限制，点击按钮可播放:", err)
        showControls.value = true
        resetHideTimer(3000)
      })
  } else {
    console.log("未同意协议，不自动播放")
    showControls.value = true
    resetHideTimer(3000)
  }
}

// 生命周期
onMounted(() => {
  getNavbarHeight()
  checkPolicyAndAutoPlay()
  window.addEventListener("resize", () => {
    getNavbarHeight()
    if (showControls.value) adjustVolumePosition()
  })

  // 为body添加点击事件，点击其他地方时隐藏控制
  const handleBodyClick = () => {
    if (showControls.value) {
      showControls.value = false
    }
  }
  document.body.addEventListener("click", handleBodyClick)

  onUnmounted(() => {
    document.body.removeEventListener("click", handleBodyClick)
  })
})

onUnmounted(() => {
  if (audioRef.value) audioRef.value.pause()
  clearHideTimer()
  window.removeEventListener("resize", getNavbarHeight)

  // 确保移除所有拖拽事件监听器
  document.removeEventListener("mousemove", handleDragMove)
  document.removeEventListener("mouseup", handleDragEnd)
  document.removeEventListener("touchmove", handleDragMove)
  document.removeEventListener("touchend", handleDragEnd)
})
</script>

<template>
  <!-- 背景音乐播放器（拖拽修复） -->
  <div
    ref="playerRef"
    class="background-music-player fixed bottom-6 right-6 z-50"
    @mousedown="handleDragStart"
    @touchstart="handleDragStart"
    @click="handlePlayerClick"
  >
    <audio
      ref="audioRef"
      src="/music.mp3"
      loop
      :volume="volume"
      @playing="isPlaying = true"
      @pause="isPlaying = false"
    />

    <!-- 播放控制按钮 -->
    <button
      class="music-control-btn relative w-12 h-12 rounded-full shadow-lg flex items-center justify-center hover:scale-105 transition-all duration-300"
      :class="{ playing: isPlaying && !showControls }"
      aria-label="{{ isPlaying ? '显示音乐控制' : '播放音乐' }}"
      @click.stop="handlePlayerClick"
    >
      <img
        src="/image.png"
        alt="音乐控制背景"
        class="music-background-image absolute inset-0 w-full h-full rounded-full object-cover"
      />
      <!-- 图标层 -->
      <div
        v-if="showControls"
        class="music-icon-overlay relative z-10 text-white p-2"
      >
        <i
          v-if="!isPlaying"
          class="fa fa-play text-xl"
          @click.stop="togglePlay"
        ></i>
        <i v-else class="fa fa-pause text-xl" @click.stop="togglePlay"></i>
      </div>
    </button>

    <!-- 音量控制滑块 -->
    <div
      ref="volumeRef"
      v-if="showControls"
      class="volume-control absolute p-2 bg-white rounded-lg shadow-md z-10"
      @mousedown.stop
      @touchstart.stop
      @touchmove="resetHideTimer(isPlaying ? 2000 : 3000)"
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
  </div>

  <PolicyModal />
  <router-view />
</template>

<style scoped>
/* 背景音乐播放器样式 */
.background-music-player {
  position: fixed;
  z-index: 50;
  display: flex;
  align-items: center;
  gap: 8px;
  user-select: none;
  padding: 4px;
  cursor: move; /* 默认设置为可移动光标 */
}

/* 音乐控制按钮 */
.music-control-btn {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  border: none;
  cursor: inherit; /* 继承父元素的光标样式 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  transition: all 0.2s ease;
  overflow: hidden;
  background: transparent;
  outline: none;
}

/* 播放状态下的旋转动画 */
.music-control-btn.playing {
  animation: rotate 15s linear infinite;
}

/* 旋转动画定义 */
@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

/* 背景图片样式 */
.music-background-image {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
  filter: brightness(0.9);
  transition: filter 0.2s ease;
}

/* 悬停效果 */
.music-control-btn:hover .music-background-image {
  filter: brightness(0.8);
}

/* 图标叠加层 */
.music-icon-overlay {
  display: flex;
  align-items: center;
  justify-content: center;
  transition: none;
}

/* 音量控制滑块样式 */
.volume-control {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 3px 12px rgba(0, 0, 0, 0.2);
  padding: 8px;
  min-width: 128px;
  transition: all 0.2s ease;
}

/* 美化音量滑块 */
.volume-control input {
  -webkit-appearance: none;
  appearance: none;
  height: 6px;
  border-radius: 3px;
  background: #e5e7eb;
  outline: none;
  width: 100%;
}

.volume-control input::-webkit-slider-thumb {
  -webkit-appearance: none;
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: #646cff;
  cursor: pointer;
  box-shadow: none;
}

.volume-control input::-moz-range-thumb {
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: #646cff;
  border: none;
  cursor: pointer;
  box-shadow: none;
}

/* 原有logo样式 */
.logo {
  height: 6em;
  padding: 1.5em;
  will-change: filter;
  transition: filter 300ms;
}

.logo:hover {
  filter: drop-shadow(0 0 2em #646cffaa);
}

.logo.vue:hover {
  filter: drop-shadow(0 0 2em #42b883aa);
}
</style>
