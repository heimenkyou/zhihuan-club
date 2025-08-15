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
): Promise<PageData<MessageItem>> => {
  try {
    console.log('🚀 发送请求参数:', params)

    // 现在api.get返回正确的类型
    const response = await api.get<ApiResponse<PageData<MessageItem>>>('/messages', { params })

    console.log('📡 响应数据:', response)
    console.log('🔍 响应数据结构:', JSON.stringify(response, null, 2))
    console.log('✅ API响应结构:', response)
    console.log('📄 分页数据:', response.data)

    return response.data
  } catch (error) {
    console.error('❌ 获取留言列表失败:', error)
    throw error
  }
}

// 点赞操作的响应类型
export type LikeActionResult = 'LIKED' | 'UNLIKED'

// 点赞/取消点赞接口请求函数
export const toggleLike = async (messageId: number): Promise<LikeActionResult> => {
  try {
    console.log('👍 发送点赞请求:', messageId)
    
    // 调用点赞接口
    const response = await api.post<ApiResponse<LikeActionResult>>(`/messages/${messageId}/like`)
    
    console.log('👍 点赞响应:', response)
    console.log('👍 点赞结果:', response.data)
    
    return response.data
  } catch (error) {
    console.error('❌ 点赞操作失败:', error)
    throw error
  }
}