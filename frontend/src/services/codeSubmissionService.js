import api from '@/services/api'

/**
 * @typedef {{
 *   id: number,
 *   studentId: string,
 *   name: string,
 *   description: string,
 *   createTime: string,
 *   updateTime: string
 * }} CodeSubmission
 */

/**
 * 提交代码
 * @param {{
 *   studentId: string,
 *   name: string,
 *   description: string
 * }} params
 */
export const submitCode = async params => {
  try {
    console.log('发送代码提交请求参数:', params)
    const response = await api.post('/admin/code-submissions', params)
    return response.data
  } catch (error) {
    console.error('代码提交失败:', error)
    const msg = error instanceof Error ? error.message : String(error)
    throw new Error(`代码提交失败：${msg}`)
  }
}

/**
 * 查询代码提交记录（管理员）
 * @param {{
 *   current?: number,
 *   size?: number,
 *   studentId?: string,
 *   name?: string
 * }} [params]
 */
export const getCodeSubmissions = async (params = {}) => {
  try {
    const response = await api.get('/admin/code-submissions', { params })
    return response.data
  } catch (error) {
    console.error('查询代码提交记录失败:', error)
    const msg = error instanceof Error ? error.message : String(error)
    throw new Error(`查询代码提交记录失败：${msg}`)
  }
}

/**
 * 查询单个代码提交记录（管理员）
 * @param {number} id
 */
export const getCodeSubmissionById = async id => {
  try {
    const response = await api.get(`/admin/code-submissions/${id}`)
    return response.data
  } catch (error) {
    console.error('查询代码提交记录详情失败:', error)
    const msg = error instanceof Error ? error.message : String(error)
    throw new Error(`查询代码提交记录详情失败：${msg}`)
  }
}

/**
 * 更新代码提交记录（管理员）
 * @param {number} id
 * @param {{ studentId?: string, name?: string, description?: string }} params
 */
export const updateCodeSubmission = async (id, params) => {
  try {
    const response = await api.put(`/admin/code-submissions/${id}`, params)
    return response.data
  } catch (error) {
    console.error('更新代码提交记录失败:', error)
    const msg = error instanceof Error ? error.message : String(error)
    throw new Error(`更新代码提交记录失败：${msg}`)
  }
}

/**
 * 删除代码提交记录（管理员）
 * @param {number} id
 */
export const deleteCodeSubmission = async id => {
  try {
    const response = await api.delete(`/admin/code-submissions/${id}`)
    return response.data
  } catch (error) {
    console.error('删除代码提交记录失败:', error)
    const msg = error instanceof Error ? error.message : String(error)
    throw new Error(`删除代码提交记录失败：${msg}`)
  }
}

/**
 * 普通用户提交代码和视频
 * @param {FormData} formData
 */
export const submitUserCode = async formData => {
  try {
    console.log('用户发送代码和视频提交请求')
    const response = await api.post('/code-submissions', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
    return response.data
  } catch (error) {
    console.error('用户代码提交失败:', error)
    const msg = error instanceof Error ? error.message : String(error)
    throw new Error(`代码提交失败：${msg}`)
  }
}
