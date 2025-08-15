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
</script>

<template>
  <div class="message-list">
    <h2>留言列表</h2>

    <!-- 调试信息 -->
    <div class="debug-info" v-if="debugInfo">
      <strong>调试信息:</strong> {{ debugInfo }}
    </div>

    <div v-if="messageStore.loading" class="loading">加载中...</div>
    <div v-else-if="messageStore.error" class="error">{{ messageStore.error }}</div>
    <div v-else-if="messageStore.messages.length === 0" class="no-data">
      暂无留言数据
    </div>
    <div v-else class="messages">
      <div v-for="msg in messageStore.messages" :key="msg.id" class="message-item">
        <div class="message-header">
          <span class="nickname">{{ msg.nickname }}</span>
          <span class="time">{{ msg.createTime }}</span>
        </div>
        <div class="message-content">{{ msg.content }}</div>
        <div class="message-footer">
          <span class="like-count">👍 {{ msg.likeCount }}</span>
          <span class="liked" :class="{ active: msg.liked }">
            {{ msg.liked ? '已点赞' : '未点赞' }}
          </span>
          <span v-if="msg.canDelete" class="can-delete">可删除</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.message-list {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
}

h2 {
  font-size: 1.8rem;
  margin-bottom: 1.5rem;
}

.debug-info {
  background-color: #f0f8ff;
  border: 1px solid #b0d4f1;
  border-radius: 4px;
  padding: 0.5rem;
  margin-bottom: 1rem;
  font-size: 0.9rem;
}

.loading,
.error,
.no-data {
  text-align: center;
  padding: 2rem;
  color: #666;
}

.error {
  color: #d32f2f;
  background-color: #ffebee;
  border-radius: 4px;
}

.messages {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.message-item {
  padding: 1rem;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.nickname {
  font-weight: bold;
  color: #1976d2;
}

.time {
  font-size: 0.8rem;
  color: #666;
}

.message-content {
  margin-bottom: 1rem;
  line-height: 1.5;
}

.message-footer {
  display: flex;
  gap: 1rem;
  align-items: center;
  font-size: 0.9rem;
}

.like-count {
  color: #666;
}

.liked {
  color: #999;
}

.liked.active {
  color: #e91e63;
  font-weight: bold;
}

.can-delete {
  color: #f44336;
  font-size: 0.8rem;
}
</style>