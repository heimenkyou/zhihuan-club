import api from '@/services/api'

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

// 创建留言参数类型
export interface CreateMessageParams {
  nickname: string
  content: string
}

// 分页查询留言接口请求函数
export const getMessages = async (
  params: GetMessagesParams = { current: 1, size: 10 }
): Promise<PageData<MessageItem>> => {
  try {
    // 现在api.get返回正确的类型
    const response = await api.get<ApiResponse<PageData<MessageItem>>>(
      '/messages',
      { params }
    )
    return response.data.data
  } catch (error) {
    console.error('❌ 获取留言列表失败:', error)
    throw error
  }
}

// 点赞操作的响应类型
export type LikeActionResult = 'LIKED' | 'UNLIKED'

// 点赞/取消点赞接口请求函数
export const toggleLike = async (
  messageId: number
): Promise<LikeActionResult> => {
  try {
    // 调用点赞接口
    const response = await api.post<ApiResponse<LikeActionResult>>(
      `/messages/${messageId}/like`
    )

    return response.data.data
  } catch (error) {
    console.error('❌ 点赞操作失败:', error)
    throw error
  }
}

// 在文件末尾添加
// 为 toggleLike 添加别名 toggleMessageLike
export { toggleLike as toggleMessageLike }

// 删除留言接口请求函数
export const deleteMessage = async (messageId: number): Promise<void> => {
  try {
    // 调用删除接口
    await api.delete<ApiResponse<null>>(`/messages/${messageId}`)
    // 不需要返回值
  } catch (error) {
    console.error('❌ 删除留言失败:', error)
    throw error
  }
}

// 创建留言接口请求函数
export const createMessage = async (
  params: CreateMessageParams
): Promise<MessageItem> => {
  try {
    // 调用创建留言接口
    const response = await api.post<ApiResponse<MessageItem>>(
      '/messages',
      params
    )
    return response.data.data
  } catch (error) {
    console.error('❌ 创建留言失败:', error)
    throw error
  }
}
