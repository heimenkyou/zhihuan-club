<script setup lang="ts">
import { ref, onMounted } from "vue"
import { useRouter } from "vue-router"
import { ArrowLeft } from "@element-plus/icons-vue"

import ElLoadingSpinner from "element-plus"
import {
  getMessages,
  toggleMessageLike,
  createMessage,
} from "../services/messageService"

// 类型定义
interface Message {
  id: number
  user: string
  content: string
  createdAt: string
  likes: number
  liked: boolean
}

// 状态定义
const messages = ref<Message[]>([])
const newMessage = ref("")
const currentPage = ref(1)
const pageSize = ref(10)
const hasMoreData = ref(true)
const isLoadingMore = ref(false)
const router = useRouter()

// 导航到首页
const navigateToHome = () => {
  router.push("/")
}

// 格式化时间
const formatTime = (timeString: string) => {
  const date = new Date(timeString)
  return date.toLocaleString()
}

// 获取留言数据
const fetchMessages = async () => {
  try {
    const response = await getMessages({
      current: currentPage.value,
      size: pageSize.value,
    })
    // 首次加载时替换数据
    messages.value = (response?.records || []).map((record) => ({
      id: record.id || 0,
      user: (record as any).user || "",
      content: record.content || "",
      createdAt: (record as any).createdAt || "",
      likes: (record as any).likes || 0,
      liked: record.liked || false,
    }))
    // 检查是否还有更多数据
    hasMoreData.value = messages.value.length >= pageSize.value
  } catch (error) {
    console.error("获取留言失败:", error)
  }
}

// 加载更多数据
const loadMoreData = async () => {
  // 防止重复请求和无效请求
  if (isLoadingMore.value || !hasMoreData.value) return

  isLoadingMore.value = true
  currentPage.value++

  try {
    const response = await getMessages({
      current: currentPage.value,
      size: pageSize.value,
    })
    const newMessages = response?.records || []

    // 添加新数据到现有列表
    if (newMessages.length > 0) {
      messages.value = [
        ...messages.value,
        ...(newMessages || []).map((record) => ({
          id: record.id || 0,
          user: (record as any).user || "",
          content: record.content || "",
          createdAt: (record as any).createdAt || "",
          likes: (record as any).likes || 0,
          liked: record.liked || false,
        })),
      ]
      // 如果返回的数据少于pageSize，说明没有更多数据了
      hasMoreData.value = newMessages.length === pageSize.value
    } else {
      // 没有更多数据
      hasMoreData.value = false
    }
  } catch (error) {
    console.error("加载更多留言失败:", error)
    currentPage.value-- // 恢复页码
  } finally {
    isLoadingMore.value = false
  }
}

// 监听滚动事件
const handleScroll = (event: Event) => {
  const scrollContainer = event.target as HTMLElement
  const scrollTop = scrollContainer.scrollTop
  const scrollHeight = scrollContainer.scrollHeight
  const clientHeight = scrollContainer.clientHeight

  // 当距离底部200px时加载更多
  if (scrollHeight - scrollTop - clientHeight < 200) {
    loadMoreData()
  }
}

// 发送新留言
const sendMessage = async () => {
  if (!newMessage.value.trim()) return

  try {
    const response = await createMessage({
      content: newMessage.value,
      nickname: "",
    })
    // 添加新留言到列表顶部
    messages.value.unshift({
      id: response.id || 0,
      user: (response as any).user || "",
      content: response.content || "",
      createdAt: (response as any).createdAt || "",
      likes: (response as any).likes || 0,
      liked: response.liked || false,
    })
    newMessage.value = ""
  } catch (error) {
    console.error("发送留言失败:", error)
  }
}

// 点赞/取消点赞
const toggleLike = async (messageId: number) => {
  try {
    await toggleMessageLike(messageId)
    // 更新本地留言的点赞状态
    const message = messages.value.find((m) => m.id === messageId)
    if (message) {
      if (message.liked) {
        message.likes--
      } else {
        message.likes++
      }
      message.liked = !message.liked
    }
  } catch (error) {
    console.error("点赞失败:", error)
  }
}

// 初始加载数据
onMounted(() => {
  fetchMessages()
})
</script>

<template>
  <div class="message-board">
    <!-- 头部区域 -->
    <header class="board-header">
      <div class="container">
        <!-- 返回箭头 -->
        <div class="back-arrow" @click="navigateToHome">
          <el-icon :size="24"><ArrowLeft /></el-icon>
        </div>

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
              <textarea
                class="content-input"
                placeholder="请输入留言内容..."
                v-model="newMessage"
                rows="4"
              ></textarea>
            </div>
            <div class="bottom-row">
              <div class="button-group">
                <button class="submit-btn" @click="sendMessage" :disabled="!newMessage.trim()">
                  <span class="submit-text">发布留言</span>
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- 加载状态 -->
        <div v-if="isLoadingMore && messages.length === 0" class="loading-container">
          <div class="loading-spinner"></div>
          <p class="loading-text">正在加载留言...</p>
        </div>

        <!-- 空数据状态 -->
        <div
          v-else-if="messages.length === 0"
          class="empty-container"
        >
          <div class="empty-icon">📝</div>
          <p class="empty-text">暂无留言数据</p>
          <p class="empty-subtext">成为第一个留言的人吧！</p>
        </div>

        <!-- 留言列表 -->
        <div v-else class="messages-container" @scroll="handleScroll">
          <div class="messages-grid">
            <article
              v-for="message in messages"
              :key="message.id"
              class="message-card"
              :class="{ 'message-liked': message.liked }"
            >
              <!-- 留言头部 -->
              <div class="message-header">
                <div class="user-info">
                  <div class="avatar">
                    {{ message.user.charAt(0).toUpperCase() }}
                  </div>
                  <div class="user-details">
                    <h3 class="nickname">{{ message.user }}</h3>
                    <time class="timestamp">{{ formatTime(message.createdAt) }}</time>
                  </div>
                </div>
              </div>

              <!-- 留言内容 -->
              <div class="message-content">
                <p class="content-text">{{ message.content }}</p>
              </div>

              <!-- 留言底部 -->
              <div class="message-footer">
                <div class="interaction-buttons">
                  <button
                    @click="toggleLike(message.id)"
                    class="like-btn"
                    :class="{ liked: message.liked }"
                    :title="message.liked ? '取消点赞' : '点赞'"
                  >
                    <span class="like-icon" :class="{ liked: message.liked }">{{ message.liked ? '❤️' : '🤍' }}</span>
                    <span class="like-count">{{ message.likes }}</span>
                  </button>
                </div>
              </div>
            </article>
          </div>

          <!-- 加载更多指示器 -->
          <div v-if="isLoadingMore" class="loading-more">
            <el-loading-spinner size="20" />
            <span>加载中...</span>
          </div>

          <!-- 无更多数据提示 -->
          <div v-if="!hasMoreData && messages.length > 0" class="no-more-data">
            没有更多留言了
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<style scoped>
.message-list-container {
  height: 100vh;
  overflow-y: auto;
  padding: 20px;
  box-sizing: border-box;
  position: relative;
}

.back-arrow {
  position: absolute;
  top: 20px;
  left: 20px;
  cursor: pointer;
  z-index: 10;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  transition: background-color 0.3s;
}

.back-arrow:hover {
  background-color: white;
}

.board-header {
  text-align: center;
  margin-bottom: 20px;
  padding-top: 10px;
}

.message-list {
  max-width: 800px;
  margin: 0 auto 20px;
}

.message-item {
  background-color: white;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.message-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background-color: #f0f2f5;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  margin-right: 12px;
}

.user-info {
  flex: 1;
}

.username {
  font-weight: 500;
  margin-bottom: 4px;
}

.time {
  font-size: 12px;
  color: #888;
}

.message-content {
  margin-bottom: 12px;
  line-height: 1.6;
}

.message-footer {
  display: flex;
  justify-content: flex-end;
}

.like-button {
  display: flex;
  align-items: center;
  color: #999;
  cursor: pointer;
  transition: color 0.2s;
}

.like-button:hover {
  color: #f56c6c;
}

.like-button span {
  margin-left: 4px;
  font-size: 14px;
}

.message-input {
  max-width: 800px;
  margin: 0 auto;
  position: sticky;
  bottom: 20px;
  background-color: white;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
}

.message-input textarea {
  width: 100%;
  height: 80px;
  padding: 12px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  resize: none;
  margin-bottom: 12px;
  box-sizing: border-box;
}

.message-input button {
  display: block;
  width: 100%;
  padding: 10px;
  background-color: #4096ff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.message-input button:hover {
  background-color: #6aa8ff;
}

.message-input button:disabled {
  background-color: #a3c5ff;
  cursor: not-allowed;
}

.loading-more {
  text-align: center;
  padding: 16px;
  color: #888;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.no-more-data {
  text-align: center;
  padding: 16px;
  color: #888;
}

/* 适配手机端 */
@media (max-width: 768px) {
  .message-list-container {
    padding: 15px 10px;
  }

  .message-item {
    padding: 12px;
  }

  .message-input {
    padding: 12px;
    bottom: 10px;
  }
}
</style>
