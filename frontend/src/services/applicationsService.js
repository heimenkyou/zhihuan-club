import api from '@/services/api'

/**
 * @typedef {Object.<string, {
 *   code: string,
 *   shortName: string,
 *   fullName: string
 * }>} MajorMapping
 */

/**
 * 获取专业代号与名称映射表
 * @returns {Promise<MajorMapping>}
 */
export const getMajorMapping = async () => {
  try {
    const response = await api.get('/majors')
    return response.data.data
  } catch (error) {
    console.error('获取专业映射表失败:', error)
    const msg = error instanceof Error ? error.message : String(error)
    throw new Error(`获取专业映射表失败：${msg}`)
  }
}

/**
 * @param {{
 *   name: string,
 *   studentId: string,
 *   major: string,
 *   phone: string,
 *   QQNumber: string,
 *   department: string,
 *   secondDepartment: string,
 *   initialDirections: string[],
 *   introduction: string,
 *   createTime?: string
 * }} params
 */
export const submitApplication = async params => {
  try {
    const response = await api.post('/applications', params)
    return response.data
  } catch (error) {
    console.error('提交申请失败:', error)
    const msg = error instanceof Error ? error.message : String(error)
    throw new Error(`提交申请失败：${msg}`)
  }
}
