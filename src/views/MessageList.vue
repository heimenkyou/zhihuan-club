<script setup lang="ts">
import { onMounted, ref, computed, onUnmounted } from 'vue'
import { useMessageStore } from '../stores/messageStore'

const messageStore = useMessageStore()
const debugInfo = ref('')

// 分页相关数据
const currentPage = ref(1)
const pageSize = ref(6)
const totalCount = ref(0)
const totalPages = ref(1)
const jumpPage = ref('')

// 新增留言数据
const newMessage = ref({
  nickname: '',
  content: ''
})

// 下拉刷新相关
const isRefreshing = ref(false)
const startY = ref(0)
const currentY = ref(0)
const pullDistance = ref(0)
const threshold = 80 // 下拉刷新阈值

// 计算可见的页码
const visiblePages = computed(() => {
  const pages = []
  const maxVisible = 7

  if (totalPages.value <= maxVisible) {
    // 如果总页数不多，显示所有页码
    for (let i = 1; i <= totalPages.value; i++) {
      pages.push(i)
    }
  } else {
    // 否则显示部分页码和省略号
    if (currentPage.value <= 4) {
      // 当前页在前几页
      for (let i = 1; i <= 5; i++) {
        pages.push(i)
      }
      pages.push('...')
      pages.push(totalPages.value)
    } else if (currentPage.value >= totalPages.value - 3) {
      // 当前页在后几页
      pages.push(1)
      pages.push('...')
      for (let i = totalPages.value - 4; i <= totalPages.value; i++) {
        pages.push(i)
      }
    } else {
      // 当前页在中间
      pages.push(1)
      pages.push('...')
      for (let i = currentPage.value - 1; i <= currentPage.value + 1; i++) {
        pages.push(i)
      }
      pages.push('...')
      pages.push(totalPages.value)
    }
  }

  return pages
})

// 下拉刷新处理
const handleTouchStart = (e: TouchEvent) => {
  if (window.scrollY === 0) {
    startY.value = e.touches[0].clientY
  }
}

const handleTouchMove = (e: TouchEvent) => {
  if (window.scrollY === 0 && startY.value > 0) {
    currentY.value = e.touches[0].clientY
    pullDistance.value = Math.max(0, currentY.value - startY.value)

    // 添加下拉指示器样式
    if (pullDistance.value > 0) {
      document.body.style.setProperty('--pull-distance', `${Math.min(pullDistance.value, threshold)}px`)
    }
  }
}

const handleTouchEnd = async () => {
  if (pullDistance.value >= threshold && !isRefreshing.value) {
    isRefreshing.value = true
    await handleRefresh()
    isRefreshing.value = false
  }

  // 重置下拉状态
  startY.value = 0
  currentY.value = 0
  pullDistance.value = 0
  document.body.style.removeProperty('--pull-distance')
}

// 分页处理方法
const handlePrevPage = async () => {
  if (currentPage.value > 1) {
    currentPage.value--
    await fetchMessages()
  }
}

const handleNextPage = async () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++
    await fetchMessages()
  }
}

const handlePageChange = async (page: number | string) => {
  if (typeof page === 'number') {
    currentPage.value = page
    await fetchMessages()
  }
}

const handleJumpPage = async () => {
  const page = parseInt(jumpPage.value)
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
    jumpPage.value = ''
    await fetchMessages()
  }
}

// 获取留言数据的方法
const fetchMessages = async () => {
  try {
    debugInfo.value = `正在获取第 ${currentPage.value} 页数据...`
    const response = await messageStore.fetchMessages({
      current: currentPage.value,
      size: pageSize.value
    })

    // 从API响应中获取分页数据
    if (response && response.current && response.total) {
      totalCount.value = response.total
      totalPages.value = response.pages
      currentPage.value = response.current
      debugInfo.value = `成功获取第 ${currentPage.value} 页，共 ${totalCount.value} 条留言`
    }
  } catch (error) {
    debugInfo.value = `获取留言失败: ${error}`
    console.error('获取留言失败:', error)
  }
}

// 在组件挂载时获取消息数据
onMounted(async () => {
  try {
    debugInfo.value = '开始获取留言数据...'
    await fetchMessages()

    // 添加触摸事件监听器
    document.addEventListener('touchstart', handleTouchStart, { passive: false })
    document.addEventListener('touchmove', handleTouchMove, { passive: false })
    document.addEventListener('touchend', handleTouchEnd, { passive: false })
  } catch (error) {
    debugInfo.value = `获取留言失败: ${error}`
    console.error('组件中获取留言失败:', error)
  }
})

// 组件卸载时移除事件监听器
onUnmounted(() => {
  document.removeEventListener('touchstart', handleTouchStart)
  document.removeEventListener('touchmove', handleTouchMove)
  document.removeEventListener('touchend', handleTouchEnd)
})

// 模拟点赞功能（仅样式变化）
// 点赞功能
const handleLike = async (messageId: number) => {
  try {
    debugInfo.value = `正在处理点赞...`
    
    // 调用store中的点赞方法
    const result = await messageStore.handleLike(messageId)
    
    // 显示操作结果
    debugInfo.value = result === 'LIKED' ? `点赞成功` : `取消点赞成功`
    console.log(`👍 ${result === 'LIKED' ? '点赞' : '取消点赞'}成功:`, messageId)
  } catch (error) {
    debugInfo.value = `点赞操作失败: ${error}`
    console.error('❌ 点赞操作失败:', error)
    // 可以在这里添加用户提示，如toast提示等
  }
}

// 模拟删除功能（仅样式变化）
const handleDelete = (messageId: number) => {
  console.log('删除功能待实现:', messageId)
}

// 新增留言
const handleAddMessage = () => {
  if (newMessage.value.nickname && newMessage.value.content) {
    console.log('新增留言:', newMessage.value)
    newMessage.value = { nickname: '', content: '' }
    console.log('新增留言成功')
  } else {
    console.warn('昵称或留言内容不能为空')
  }
}

// 刷新留言
const handleRefresh = () => {
  debugInfo.value = '正在刷新留言数据...'
  fetchMessages()
}

// 格式化时间显示
const formatTime = (timeString: string) => {
  try {
    const date = new Date(timeString)
    const now = new Date()
    const diff = now.getTime() - date.getTime()

    // 小于1分钟
    if (diff < 60000) {
      return '刚刚'
    }
    // 小于1小时
    if (diff < 3600000) {
      return `${Math.floor(diff / 60000)}分钟前`
    }
    // 小于24小时
    if (diff < 86400000) {
      return `${Math.floor(diff / 3600000)}小时前`
    }
    // 24-48小时显示"昨天"
    if (diff < 172800000) {
      return '昨天'
    }
    // 超过48小时但不到一年，显示月份-日期
    if (diff < 31536000000) {
      return date.toLocaleDateString('zh-CN', {
        month: 'numeric',
        day: 'numeric'
      }).replace('/', '-')
    }
    // 超过一年显示完整日期
    return date.toLocaleDateString('zh-CN', {
      year: 'numeric',
      month: 'numeric',
      day: 'numeric'
    }).replace(/\//g, '-')
  } catch (error) {
    return timeString
  }
}
</script>

<template>
  <div class="message-board">
    <!-- 下拉刷新指示器 -->
    <div class="pull-indicator" :class="{
      'release': pullDistance >= threshold,
      'refreshing': isRefreshing
    }"></div>

    <!-- 头部区域 -->
    <header class="board-header">
      <div class="container">
        <h1 class="board-title">
          <span class="icon">💬</span>
          社团招新留言板
        </h1>
        <p class="board-subtitle">分享你的想法，为社团注入新活力</p>
      </div>
    </header>

    <!-- 主要内容区域 -->
    <main class="board-main">
      <div class="container">
        <!-- 新增留言区域 -->
        <div class="add-message-container">
          <div class="add-message-form">
            <div class="content-input-wrapper">
              <textarea class="content-input" placeholder="请输入留言内容..." v-model="newMessage.content" rows="4"></textarea>
            </div>
            <div class="bottom-row">
              <div class="left-section">
                <input type="text" class="nickname-input" placeholder="请输入您的昵称" v-model="newMessage.nickname" />
              </div>
              <div class="button-group">
                <button class="submit-btn" @click="handleAddMessage">
                  <span class="submit-icon">📝</span>
                  <span class="submit-text">发布留言</span>
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- 悬浮刷新按钮 -->
        <div class="floating-refresh" :class="{ 'refreshing': isRefreshing }">
          <button class="refresh-btn" @click="handleRefresh" :disabled="isRefreshing">
            <span class="refresh-icon">🔄</span>
          </button>
        </div>

        <!-- 加载状态 -->
        <div v-if="messageStore.loading" class="loading-container">
          <div class="loading-spinner"></div>
          <p class="loading-text">正在加载留言...</p>
        </div>

        <!-- 错误状态 -->
        <div v-else-if="messageStore.error" class="error-container">
          <div class="error-icon">⚠️</div>
          <p class="error-text">{{ messageStore.error }}</p>
        </div>

        <!-- 空数据状态 -->
        <div v-else-if="messageStore.messages.length === 0" class="empty-container">
          <div class="empty-icon">📝</div>
          <p class="empty-text">暂无留言数据</p>
          <p class="empty-subtext">成为第一个留言的人吧！</p>
        </div>

        <!-- 留言列表 -->
        <div v-else class="messages-container">
          <div class="messages-grid">
            <article v-for="msg in messageStore.messages" :key="msg.id" class="message-card"
              :class="{ 'message-liked': msg.liked }">
              <!-- 留言头部 -->
              <div class="message-header">
                <div class="user-info">
                  <div class="avatar">
                    {{ msg.nickname.charAt(0).toUpperCase() }}
                  </div>
                  <div class="user-details">
                    <h3 class="nickname">{{ msg.nickname }}</h3>
                    <time class="timestamp">{{ formatTime(msg.createTime) }}</time>
                  </div>
                </div>
                <div class="message-actions">
                  <button v-if="msg.canDelete" @click="handleDelete(msg.id)" class="delete-btn" title="删除留言">
                    <span class="delete-icon">🗑️</span>
                  </button>
                </div>
              </div>

              <!-- 留言内容 -->
              <div class="message-content">
                <p class="content-text">{{ msg.content }}</p>
              </div>

              <!-- 留言底部 -->
              <div class="message-footer">
                <div class="interaction-buttons">
                  <button @click="handleLike(msg.id)" class="like-btn" :class="{ 'liked': msg.liked }"
                    :title="msg.liked ? '取消点赞' : '点赞'">
                    <span class="like-icon" :class="{ 'liked': msg.liked }">
                      {{ msg.liked ? '❤️' : '🤍' }}
                    </span>
                    <span class="like-count">{{ msg.likeCount }}</span>
                  </button>
                </div>
              </div>
            </article>
          </div>

          <!-- 分页组件 -->
          <div class="pagination-container">
            <div class="pagination-info">
              <span class="total-count">共 {{ totalCount }} 条留言</span>
              <span class="page-info">第 {{ currentPage }} 页，共 {{ totalPages }} 页</span>
            </div>

            <div class="pagination-controls">
              <!-- 上一页按钮 -->
              <button class="page-btn prev-btn" :class="{ 'disabled': currentPage <= 1 }" :disabled="currentPage <= 1"
                @click="handlePrevPage">
                <span class="arrow">←</span>
                <span class="btn-text">上一页</span>
              </button>

              <!-- 页码按钮 -->
              <div class="page-numbers">
                <button v-for="page in visiblePages" :key="page" class="page-number"
                  :class="{ 'active': page === currentPage, 'ellipsis': page === '...' }"
                  @click="handlePageChange(page)" :disabled="page === '...'">
                  {{ page }}
                </button>
              </div>

              <!-- 下一页按钮 -->
              <button class="page-btn next-btn" :class="{ 'disabled': currentPage >= totalPages }"
                :disabled="currentPage >= totalPages" @click="handleNextPage">
                <span class="btn-text">下一页</span>
                <span class="arrow">→</span>
              </button>
            </div>

            <!-- 快速跳转 -->
            <div class="page-jump">
              <span class="jump-text">跳转到</span>
              <input type="number" class="jump-input" v-model="jumpPage" :min="1" :max="totalPages" placeholder="页码" />
              <span class="jump-text">页</span>
              <button class="jump-btn" @click="handleJumpPage">确定</button>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<style scoped>
/* 基础样式 */
* {
  box-sizing: border-box;
}

.message-board {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 头部样式 */
.board-header {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  padding: 1rem 0;
  text-align: center;
}

.board-title {
  font-size: 1.8rem;
  font-weight: 700;
  color: white;
  margin: 0 0 0.3rem 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.board-title .icon {
  font-size: 1.5rem;
}

.board-subtitle {
  font-size: 0.9rem;
  color: rgba(255, 255, 255, 0.8);
  margin: 0;
  font-weight: 300;
}

/* 主要内容区域 */
.board-main {
  padding: 2rem 0;
}

/* 调试信息 */
.debug-info {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 1rem;
  margin-bottom: 2rem;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.debug-content {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: white;
}

.debug-icon {
  font-size: 1.2rem;
}

.debug-text {
  font-size: 0.9rem;
  opacity: 0.9;
}

/* 新增留言区域 */
.add-message-container {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  padding: 1.5rem;
  margin-bottom: 2rem;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.add-message-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.content-input-wrapper {
  width: 100%;
}

.nickname-input,
.content-input {
  padding: 0.8rem 1rem;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.9);
  color: #333;
  font-size: 1rem;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  resize: none;
  line-height: 1.5;
}

.content-input {
  width: 100%;
  min-height: 120px;
  padding-top: 0.8rem;
  padding-bottom: 0.8rem;
}

.nickname-input {
  height: 48px;
  display: flex;
  align-items: center;
}

.nickname-input::placeholder,
.content-input::placeholder {
  color: #666;
  text-align: left;
  line-height: 1.5;
}

.nickname-input:focus,
.content-input:focus {
  outline: none;
  border-color: #667eea;
  background: rgba(255, 255, 255, 1);
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.bottom-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 1rem;
}

.left-section {
  flex-shrink: 0;
}

.button-group {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
}

.refresh-btn,
.submit-btn {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  border: none;
  border-radius: 12px;
  padding: 0.8rem 1.5rem;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  font-size: 1rem;
  font-weight: 600;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

.refresh-btn:hover,
.submit-btn:hover {
  background: linear-gradient(135deg, #764ba2, #667eea);
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

.refresh-icon,
.submit-icon {
  font-size: 1.1rem;
}

.refresh-text,
.submit-text {
  font-weight: 500;
}

/* 悬浮刷新按钮 */
.floating-refresh {
  position: fixed;
  bottom: 20px;
  top: 50%;
  right: 12px;
  transform: translateY(-50%);
  z-index: 10;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 60px;
  height: 60px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  backdrop-filter: blur(5px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
  opacity: 0.9;
}

.floating-refresh:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-50%) scale(1.1);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
}

.floating-refresh.refreshing {
  background: rgba(255, 255, 255, 0.3);
  transform: scale(1.1);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
  opacity: 1;
}

.floating-refresh .refresh-icon {
  font-size: 1.8rem;
  color: white;
}

.floating-refresh .refresh-text {
  display: none;
  /* 隐藏文字，只保留图标 */
}

/* 加载状态 */
.loading-container {
  text-align: center;
  padding: 4rem 0;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid rgba(255, 255, 255, 0.3);
  border-top: 3px solid white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1rem;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}

.loading-text {
  color: white;
  font-size: 1.1rem;
  margin: 0;
}

/* 错误状态 */
.error-container {
  text-align: center;
  padding: 4rem 0;
}

.error-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.error-text {
  color: #ff6b6b;
  font-size: 1.1rem;
  margin: 0;
}

/* 空数据状态 */
.empty-container {
  text-align: center;
  padding: 4rem 0;
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
}

.empty-text {
  color: white;
  font-size: 1.3rem;
  margin: 0 0 0.5rem 0;
  font-weight: 500;
}

.empty-subtext {
  color: rgba(255, 255, 255, 0.7);
  font-size: 1rem;
  margin: 0;
}

/* 留言网格 */
.messages-grid {
  display: grid;
  gap: 1.5rem;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
}

/* 留言卡片 */
.message-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 1.5rem;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.message-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
}

.message-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, #667eea, #764ba2);
  opacity: 0;
  transition: opacity 0.3s ease;
  border-radius: 16px 16px 0 0;
}

.message-card:hover::before {
  opacity: 0.8;
}

.message-liked::before {
  background: linear-gradient(90deg, #ff6b6b, #ee5a24);
  opacity: 0.8;
}

/* 留言头部 */
.message-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 1rem;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.avatar {
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 600;
  font-size: 1.2rem;
  flex-shrink: 0;
}

.user-details {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.nickname {
  font-size: 1.1rem;
  font-weight: 600;
  color: #2c3e50;
  margin: 0;
  line-height: 1.2;
}

.timestamp {
  font-size: 0.85rem;
  color: #7f8c8d;
  margin: 0;
}

.message-actions {
  display: flex;
  gap: 0.5rem;
}

.delete-btn {
  background: rgba(231, 76, 60, 0.1);
  border: 1px solid rgba(231, 76, 60, 0.3);
  padding: 0.5rem;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  color: #e74c3c;
}

.delete-btn:hover {
  background: rgba(231, 76, 60, 0.2);
  border-color: rgba(231, 76, 60, 0.6);
  transform: scale(1.1);
  box-shadow: 0 2px 8px rgba(231, 76, 60, 0.3);
}

.delete-icon {
  font-size: 1.1rem;
}

/* 留言内容 */
.message-content {
  margin-bottom: 1.5rem;
}

.content-text {
  font-size: 1rem;
  line-height: 1.6;
  color: #34495e;
  margin: 0;
  word-wrap: break-word;
}

/* 留言底部 */
.message-footer {
  display: flex;
  justify-content: flex-end;
}

.interaction-buttons {
  display: flex;
  gap: 0.5rem;
}

.like-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: rgba(102, 126, 234, 0.1);
  border: 2px solid rgba(102, 126, 234, 0.3);
  border-radius: 25px;
  padding: 0.5rem 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
  color: #667eea;
  font-weight: 500;
  font-size: 0.9rem;
}

.like-btn:hover {
  background: rgba(102, 126, 234, 0.2);
  border-color: rgba(102, 126, 234, 0.5);
  transform: translateY(-1px);
}

.like-btn.liked {
  background: rgba(255, 107, 107, 0.15);
  border-color: rgba(255, 107, 107, 0.4);
  color: #ff6b6b;
}

.like-btn.liked:hover {
  background: rgba(255, 107, 107, 0.25);
  border-color: rgba(255, 107, 107, 0.6);
}

.like-icon {
  font-size: 1.1rem;
  transition: transform 0.2s ease;
}

.like-btn:hover .like-icon {
  transform: scale(1.1);
}

.like-count {
  font-weight: 600;
}

/* 分页组件样式 */
.pagination-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
  margin-top: 2rem;
  padding: 1rem;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.pagination-info {
  display: flex;
  align-items: center;
  gap: 1rem;
  color: white;
  font-size: 0.9rem;
  opacity: 0.9;
}

.total-count {
  font-weight: 500;
}

.page-info {
  font-weight: 300;
}

.pagination-controls {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: white;
  font-size: 0.9rem;
  opacity: 0.9;
}

.page-btn {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 8px;
  padding: 0.5rem 1rem;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  gap: 0.3rem;
  color: white;
  font-weight: 500;
}

.page-btn:hover:not(.disabled) {
  background: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.5);
}

.page-btn.disabled {
  opacity: 0.5;
  cursor: not-allowed;
  color: rgba(255, 255, 255, 0.7);
}

.page-numbers {
  display: flex;
  gap: 0.3rem;
}

.page-number {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 8px;
  padding: 0.5rem 0.8rem;
  cursor: pointer;
  transition: all 0.2s ease;
  color: white;
  font-weight: 500;
  font-size: 0.9rem;
}

.page-number:hover:not(.active):not(.ellipsis) {
  background: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.5);
}

.page-number.active {
  background: rgba(255, 255, 255, 0.3);
  border-color: rgba(255, 255, 255, 0.6);
  color: #667eea;
  font-weight: 600;
}

.page-number.ellipsis {
  cursor: default;
  color: rgba(255, 255, 255, 0.7);
  font-weight: 500;
}

.page-jump {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: white;
  font-size: 0.9rem;
  opacity: 0.9;
}

.jump-text {
  font-weight: 500;
}

.jump-input {
  width: 60px;
  padding: 0.5rem;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.9);
  color: #333;
  text-align: center;
  font-size: 0.9rem;
}

.jump-input:focus {
  outline: none;
  border-color: #667eea;
  background: rgba(255, 255, 255, 1);
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.jump-btn {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 8px;
  padding: 0.5rem 1rem;
  cursor: pointer;
  transition: all 0.2s ease;
  color: white;
  font-weight: 500;
  font-size: 0.9rem;
}

.jump-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.5);
}

/* 下拉刷新指示器 */
.pull-indicator {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: var(--pull-distance, 0px);
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.8), rgba(118, 75, 162, 0.8));
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 0.9rem;
  font-weight: 500;
  z-index: 1000;
  transition: height 0.2s ease;
  pointer-events: none;
}

/* 下拉刷新指示器 - 达到阈值时 */
.pull-indicator.release {
  background: linear-gradient(135deg, rgba(255, 107, 107, 0.8), rgba(238, 90, 36, 0.8));
}

.pull-indicator.release::before {
  content: '释放刷新';
  opacity: 1;
}

/* 下拉刷新指示器 - 刷新中 */
.pull-indicator.refreshing {
  background: linear-gradient(135deg, rgba(66, 185, 131, 0.8), rgba(46, 204, 113, 0.8));
}

.pull-indicator.refreshing::before {
  content: '正在刷新...';
  opacity: 1;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .container {
    padding: 0 16px;
  }

  .board-title {
    font-size: 1.5rem;
  }

  .board-title .icon {
    font-size: 1.2rem;
  }

  .board-subtitle {
    font-size: 0.8rem;
  }

  .messages-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }

  .message-card {
    padding: 1.25rem;
  }

  .avatar {
    width: 40px;
    height: 40px;
    font-size: 1rem;
  }

  .nickname {
    font-size: 1rem;
  }

  .like-btn {
    padding: 0.4rem 0.8rem;
    font-size: 0.85rem;
  }

  .add-message-container {
    padding: 1.25rem;
  }

  .bottom-row {
    flex-direction: column;
    gap: 1rem;
    align-items: stretch;
  }

  .left-section {
    width: 100%;
  }

  .nickname-input {
    width: 100%;
  }

  .button-group {
    flex-direction: column;
    gap: 0.8rem;
  }

  .refresh-btn,
  .submit-btn {
    width: 100%;
    padding: 0.7rem 1.2rem;
    font-size: 0.9rem;
  }

  .pagination-container {
    padding: 0.8rem;
  }

  .pagination-info {
    font-size: 0.8rem;
  }

  .page-btn {
    padding: 0.6rem 1rem;
    font-size: 0.9rem;
    min-height: 48px;
  }

  .page-numbers {
    gap: 0.2rem;
  }

  .page-number {
    padding: 0.4rem 0.6rem;
    font-size: 0.85rem;
    min-height: 40px;
    min-width: 40px;
  }

  .page-jump {
    font-size: 0.8rem;
  }

  .jump-input {
    width: 40px;
    font-size: 0.8rem;
  }

  .jump-btn {
    padding: 0.4rem 0.8rem;
    font-size: 0.85rem;
  }

  .floating-refresh {
    top: 50%;
    right: 16px;
    transform: translateY(-50%);
    width: 56px;
    height: 56px;
  }

  .floating-refresh .refresh-icon {
    font-size: 1.6rem;
  }
}

@media (max-width: 480px) {
  .board-header {
    padding: 0.8rem 0;
  }

  .board-title {
    font-size: 1.4rem;
  }

  .board-icon {
    font-size: 1.1rem;
  }

  .board-subtitle {
    font-size: 0.75rem;
  }

  .board-main {
    padding: 1.5rem 0;
  }

  .message-card {
    padding: 1rem;
  }

  .user-info {
    gap: 0.5rem;
  }

  .avatar {
    width: 36px;
    height: 36px;
    font-size: 0.9rem;
  }

  .nickname {
    font-size: 0.95rem;
  }

  .timestamp {
    font-size: 0.8rem;
  }

  .content-text {
    font-size: 0.95rem;
  }

  .add-message-container {
    padding: 1rem;
  }

  .nickname-input,
  .content-input {
    font-size: 0.85rem;
    padding: 0.7rem 0.8rem;
  }

  .refresh-btn,
  .submit-btn {
    padding: 0.6rem 1rem;
    font-size: 0.85rem;
  }

  .pagination-container {
    padding: 0.6rem;
  }

  .pagination-info {
    font-size: 0.7rem;
  }

  .page-btn {
    padding: 0.5rem 0.8rem;
    font-size: 0.8rem;
    min-height: 52px;
  }

  .page-numbers {
    gap: 0.1rem;
  }

  .page-number {
    padding: 0.3rem 0.5rem;
    font-size: 0.75rem;
    min-height: 44px;
    min-width: 44px;
  }

  .page-jump {
    font-size: 0.7rem;
  }

  .jump-input {
    width: 30px;
    font-size: 0.7rem;
  }

  .jump-btn {
    padding: 0.3rem 0.6rem;
    font-size: 0.75rem;
  }

  .floating-refresh {
    top: 50%;
    right: 12px;
    transform: translateY(-50%);
    width: 52px;
    height: 52px;
  }

  .floating-refresh .refresh-icon {
    font-size: 1.4rem;
  }
}
</style>