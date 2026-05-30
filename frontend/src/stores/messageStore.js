import { defineStore } from 'pinia'
import {
  getMessages,
  toggleLike,
  deleteMessage,
  createMessage,
} from '@/services/messageService'

export const useMessageStore = defineStore('message', {
  state: () => ({
    messages: [],
    loading: false,
    error: null,
    pagination: null,
  }),
  actions: {
    async fetchMessages(params) {
      this.loading = true
      this.error = null
      try {
        const response = await getMessages(params)
        this.messages = response.records
        this.pagination = response
        return response
      } finally {
        this.loading = false
      }
    },

    /**
     * 先做乐观更新，再用服务端结果校验状态，避免界面卡顿。
     */
    async handleLike(messageId) {
      const message = this.messages.find(msg => msg.id === messageId)
      if (!message) {
        throw new Error('未找到指定的留言')
      }

      const originalLiked = message.liked
      const originalLikeCount = message.likeCount

      if (message.liked) {
        message.liked = false
        message.likeCount = Math.max(0, message.likeCount - 1)
      } else {
        message.liked = true
        message.likeCount += 1
      }

      try {
        const result = await toggleLike(messageId)
        const expectedResult = originalLiked ? 'UNLIKED' : 'LIKED'
        if (result !== expectedResult) {
          message.liked = originalLiked
          message.likeCount = originalLikeCount
          throw new Error('点赞操作结果与预期不符')
        }

        return result
      } catch (error) {
        message.liked = originalLiked
        message.likeCount = originalLikeCount
        throw error
      }
    },

    /**
     * 删除成功后整页刷新，避免本地分页总数与后端脱节。
     */
    async handleDeleteMessage(messageId) {
      try {
        this.loading = true
        this.error = null

        const messageIndex = this.messages.findIndex(msg => msg.id === messageId)
        if (messageIndex === -1) {
          throw new Error('未找到指定的留言')
        }

        const message = this.messages[messageIndex]
        if (!message.canDelete) {
          throw new Error('您无权删除此留言')
        }

        await deleteMessage(messageId)

        const currentPage = this.pagination?.current || 1
        const pageSize = this.pagination?.size || 10
        const response = await getMessages({ current: currentPage, size: pageSize })
        this.messages = response.records
        this.pagination = response
      } finally {
        this.loading = false
      }
    },

    /**
     * 创建后重新拉取当前页，保持留言顺序和分页信息一致。
     */
    async handleCreateMessage(params) {
      this.loading = true
      this.error = null

      try {
        const newMessage = await createMessage(params)
        const currentPage = this.pagination?.current || 1
        const pageSize = this.pagination?.size || 10
        const response = await getMessages({ current: currentPage, size: pageSize })
        this.messages = response.records
        this.pagination = response
        return newMessage
      } catch (error) {
        this.error = error instanceof Error ? error.message : '创建留言失败'
        throw error
      } finally {
        this.loading = false
      }
    },
  },
})
