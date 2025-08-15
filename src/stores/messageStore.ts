import { defineStore } from 'pinia'
import { getMessages } from '../services/messageService'
import type { MessageItem } from '../services/messageService'

export const useMessageStore = defineStore('message', {
  state: () => ({
    messages: [] as MessageItem[],
    loading: false,
    error: null as string | null
  }),
  actions: {
    async fetchMessages() {
      this.loading = true
      this.error = null
      try {
        const response = await getMessages()
        this.messages = response.records
      } catch (err) {
        this.error = '获取留言列表失败'
        console.error('获取留言列表失败:', err)
      } finally {
        this.loading = false
      }
    }
  }
})