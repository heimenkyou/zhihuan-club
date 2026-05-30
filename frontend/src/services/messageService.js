import api from '@/services/api'

/**
 * @typedef {{
 *   id: number,
 *   nickname: string,
 *   content: string,
 *   likeCount: number,
 *   createTime: string,
 *   liked: boolean,
 *   canDelete: boolean
 * }} MessageItem
 */

/**
 * @typedef {{
 *   current: number,
 *   size: number,
 *   total: number,
 *   pages: number,
 *   records: MessageItem[]
 * }} MessagePageData
 */

/**
 * 分页查询留言接口请求函数
 * @param {{ current?: number, size?: number }} [params]
 * @returns {Promise<MessagePageData>}
 */
export const getMessages = async (params = { current: 1, size: 10 }) => {
  try {
    const response = await api.get('/messages', { params })
    return response.data.data
  } catch (error) {
    console.error('获取留言列表失败:', error)
    throw error
  }
}

/**
 * 点赞/取消点赞接口请求函数
 * @param {number} messageId
 * @returns {Promise<'LIKED' | 'UNLIKED'>}
 */
export const toggleLike = async messageId => {
  try {
    const response = await api.post(`/messages/${messageId}/like`)
    return response.data.data
  } catch (error) {
    console.error('点赞操作失败:', error)
    throw error
  }
}

export { toggleLike as toggleMessageLike }

/**
 * 删除留言接口请求函数
 * @param {number} messageId
 */
export const deleteMessage = async messageId => {
  try {
    await api.delete(`/messages/${messageId}`)
  } catch (error) {
    console.error('删除留言失败:', error)
    throw error
  }
}

/**
 * 创建留言接口请求函数
 * @param {{ nickname: string, content: string }} params
 * @returns {Promise<MessageItem>}
 */
export const createMessage = async params => {
  try {
    const response = await api.post('/messages', params)
    return response.data.data
  } catch (error) {
    console.error('创建留言失败:', error)
    throw error
  }
}
