import { defineStore } from 'pinia'
import { getMessages, toggleLike } from '../services/messageService'
import type { MessageItem, GetMessagesParams, PageData, LikeActionResult } from '../services/messageService'

export const useMessageStore = defineStore('message', {
  state: () => ({
    messages: [] as MessageItem[],
    loading: false,
    error: null as string | null,
    pagination: null as PageData<MessageItem> | null
  }),
  actions: {
    async fetchMessages(params?: GetMessagesParams) {
      this.loading = true
      this.error = null
      try {
        const response = await getMessages(params)
        this.messages = response.records
        this.pagination = response
        return response
      } catch (err) {
        this.error = '获取留言列表失败'
        console.error('获取留言列表失败:', err)
        throw err
      } finally {
        this.loading = false
      }
    },
    
    // 处理点赞操作
    async handleLike(messageId: number): Promise<LikeActionResult> {
      try {
        // 找到对应的消息
        const message = this.messages.find(msg => msg.id === messageId)
        if (!message) {
          throw new Error('未找到指定的留言')
        }
        
        // 记录当前状态用于回滚
        const originalLiked = message.liked
        const originalLikeCount = message.likeCount
        
        // 乐观更新：立即更新本地状态
        if (message.liked) {
          // 取消点赞
          message.liked = false
          message.likeCount = Math.max(0, message.likeCount - 1)
        } else {
          // 点赞
          message.liked = true
          message.likeCount += 1
        }
        
        try {
          // 调用点赞接口
          const result: LikeActionResult = await toggleLike(messageId)
          
          // 验证结果是否符合预期
          const expectedResult = originalLiked ? 'UNLIKED' : 'LIKED'
          if (result !== expectedResult) {
            // 结果不符合预期，回滚状态
            message.liked = originalLiked
            message.likeCount = originalLikeCount
            throw new Error('点赞操作结果与预期不符')
          }
          
          return result
        } catch (error) {
          // 接口调用失败，回滚状态
          message.liked = originalLiked
          message.likeCount = originalLikeCount
          throw error
        }
      } catch (err) {
        this.error = '点赞操作失败'
        console.error('点赞操作失败:', err)
        throw err
      }
    }
  }
})