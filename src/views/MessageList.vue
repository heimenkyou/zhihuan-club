<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useMessageStore } from '../stores/messageStore'

const messageStore = useMessageStore()
const debugInfo = ref('')

// 在组件挂载时获取消息数据
onMounted(async () => {
  try {
    debugInfo.value = '开始获取留言数据...'
    await messageStore.fetchMessages()
    debugInfo.value = `成功获取 ${messageStore.messages.length} 条留言`
  } catch (error) {
    debugInfo.value = `获取留言失败: ${error}`
    console.error('组件中获取留言失败:', error)
  }
})

// 模拟点赞功能（仅样式变化）
const handleLike = (messageId: number) => {
  console.log('点赞功能待实现:', messageId)
}

// 模拟删除功能（仅样式变化）
const handleDelete = (messageId: number) => {
  console.log('删除功能待实现:', messageId)
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
    // 小于7天
    if (diff < 604800000) {
      return `${Math.floor(diff / 86400000)}天前`
    }
    // 超过7天显示具体日期
    return date.toLocaleDateString('zh-CN', {
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    })
  } catch (error) {
    return timeString
  }
}
</script>

<template>
  <div class="message-board">
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
        <!-- 调试信息 -->
        <div class="debug-info" v-if="debugInfo">
          <div class="debug-content">
            <span class="debug-icon">🔍</span>
            <span class="debug-text">{{ debugInfo }}</span>
          </div>
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
  padding: 2rem 0;
  text-align: center;
}

.board-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: white;
  margin: 0 0 0.5rem 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.board-title .icon {
  font-size: 2rem;
}

.board-subtitle {
  font-size: 1.1rem;
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
  background: none;
  border: none;
  padding: 0.5rem;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  color: #e74c3c;
}

.delete-btn:hover {
  background: rgba(231, 76, 60, 0.1);
  transform: scale(1.1);
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

/* 响应式设计 */
@media (max-width: 768px) {
  .container {
    padding: 0 16px;
  }

  .board-title {
    font-size: 2rem;
  }

  .board-title .icon {
    font-size: 1.5rem;
  }

  .board-subtitle {
    font-size: 1rem;
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
}

@media (max-width: 480px) {
  .board-header {
    padding: 1.5rem 0;
  }

  .board-title {
    font-size: 1.75rem;
  }

  .board-subtitle {
    font-size: 0.9rem;
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
}
</style>