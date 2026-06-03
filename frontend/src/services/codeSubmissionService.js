import api from '@/services/api'

/**
 * 提交代码和演示视频。
 *
 * @param {FormData} formData
 * @returns {Promise<any>}
 */
export const submitUserCode = async formData => {
  try {
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
