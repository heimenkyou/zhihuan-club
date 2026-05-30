import { defineStore } from 'pinia'
import { getMessages, toggleLike, deleteMessage, createMessage } from '@/services/messageService'
import type { MessageItem, GetMessagesParams, PageData, LikeActionResult, CreateMessageParams } from '@/services/messageService'

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
      } finally {
        this.loading = false
      }
    },
    
    // 处理点赞操作
    async handleLike(messageId: number): Promise<LikeActionResult> {
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
    },
    
    // 处理删除留言操作
    async handleDeleteMessage(messageId: number): Promise<void> {
      try {
        this.loading = true
        this.error = null
        
        // 找到对应的消息
        const messageIndex = this.messages.findIndex(msg => msg.id === messageId)
        if (messageIndex === -1) {
          throw new Error('未找到指定的留言')
        }
        
        // 检查是否有权限删除
        const message = this.messages[messageIndex]
        if (!message.canDelete) {
          throw new Error('您无权删除此留言')
        }
        
        // 先调用后端接口，等待响应
        await deleteMessage(messageId)
        
        // 接口调用成功后，刷新整个页面的数据
        // 获取当前的分页参数
        const currentPage = this.pagination?.current || 1
        const pageSize = this.pagination?.size || 10
        
        // 重新获取留言列表数据
        const response = await getMessages({ current: currentPage, size: pageSize })
        this.messages = response.records
        this.pagination = response
      } finally {
        this.loading = false
      }
    },
    
    // 处理创建留言操作
    async handleCreateMessage(params: CreateMessageParams): Promise<MessageItem> {
      this.loading = true
      this.error = null
      
      try {
        // 调用创建留言接口
        const newMessage = await createMessage(params)
        
        // 创建成功后，刷新列表
        const currentPage = this.pagination?.current || 1
        const pageSize = this.pagination?.size || 10
        
        // 重新获取留言列表数据
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
    }
  }
})