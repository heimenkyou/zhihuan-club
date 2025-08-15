import api from './api'

// 分页查询参数类型
export interface GetMessagesParams {
  current?: number
  size?: number
}

// 留言数据类型
export interface MessageItem {
  id: number
  nickname: string
  content: string
  likeCount: number
  createTime: string
  liked: boolean
  canDelete: boolean
}

// 分页响应数据类型
export interface PageData<T> {
  current: number
  size: number
  total: number
  pages: number
  records: T[]
}

// API响应类型
export interface ApiResponse<T> {
  code: string
  message: string
  data: T
  requestId: string
}

// 分页查询留言接口请求函数
export const getMessages = async (
  params: GetMessagesParams = { current: 1, size: 10 }
): Promise<ApiResponse<PageData<MessageItem>>> => {
  try {
    const response = await api.get<ApiResponse<PageData<MessageItem>>>('/messages', {
      params
    })
    return response.data
  } catch (error) {
    console.error('获取留言列表失败:', error)
    throw error
  }
}