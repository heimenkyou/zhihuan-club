import { defineStore } from 'pinia'
import { getMessages } from '../services/messageService'
import type { MessageItem, GetMessagesParams, PageData } from '../services/messageService'

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
    }
  }
})