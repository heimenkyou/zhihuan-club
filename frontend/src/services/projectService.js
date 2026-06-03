import api from '@/services/api'

/**
 * 读取接口响应体。
 *
 * @param {import('axios').AxiosResponse<any>=} response
 * @returns {any}
 */
const getBody = response => response?.data

/**
 * 读取标准响应体中的 data 字段。
 *
 * @param {any} body
 * @returns {any}
 */
const getResultData = body => {
  if (!body || typeof body !== 'object') {
    return undefined
  }
  return body.data
}

/**
 * 判断响应体是否表示成功。
 *
 * @param {any} body
 * @returns {boolean}
 */
const isSuccessBody = body => {
  return Boolean(body && (body.success || body.code === '0' || body.code === '3'))
}

/**
 * 获取项目详情。
 *
 * @param {string | number} id
 * @returns {Promise<any>}
 */
export const getProjectDetail = async id => {
  try {
    const response = await api.get(`/projects/${id}`)
    const data = getResultData(getBody(response))

    if (data) {
      return data
    }

    throw new Error('获取项目详情失败')
  } catch (error) {
    console.error('获取项目详情失败:', error)
    throw error
  }
}

/**
 * 获取项目编辑回显数据。
 *
 * @param {number} id
 * @returns {Promise<any>}
 */
export const getProjectForEdit = async id => {
  try {
    const response = await api.get(`/projects/${id}/edit`)
    const body = getBody(response)
    const data = getResultData(body)

    if (isSuccessBody(body) && data) {
      return data
    }

    throw new Error('获取项目编辑数据失败')
  } catch (error) {
    console.error('获取项目编辑数据失败:', error)
    throw error
  }
}

/**
 * 创建项目。
 *
 * @param {Object} params
 * @returns {Promise<any>}
 */
export const createProject = async params => {
  try {
    const response = await api.post('/admin/projects', params)
    const body = getBody(response)

    if (body) {
      return body
    }

    throw new Error('创建项目失败')
  } catch (error) {
    console.error('创建项目失败:', error)
    throw error
  }
}

/**
 * 更新项目。
 *
 * @param {number} id
 * @param {Object} params
 * @returns {Promise<any>}
 */
export const updateProject = async (id, params) => {
  try {
    const response = await api.put(`/admin/projects/${id}`, params)
    const body = getBody(response)

    if (body) {
      return body
    }

    throw new Error('更新项目失败')
  } catch (error) {
    console.error('更新项目失败:', error)
    throw error
  }
}

/**
 * 删除项目。
 *
 * @param {number} id
 * @returns {Promise<void>}
 */
export const deleteProject = async id => {
  try {
    const response = await api.delete(`/admin/projects/${id}`)
    if (isSuccessBody(getBody(response))) {
      return
    }

    throw new Error('删除项目失败')
  } catch (error) {
    console.error('删除项目失败:', error)
    throw error
  }
}

/**
 * 获取项目分页数据。
 *
 * @param {{ current?: number, size?: number, keyword?: string }} [params]
 * @returns {Promise<any>}
 */
export const getProjects = async params => {
  try {
    const response = await api.get('/projects', { params })
    return response.data
  } catch (error) {
    console.error('获取项目列表失败:', error)
    return {
      code: '500',
      message: error instanceof Error ? error.message : '获取项目列表失败',
      data: {
        current: 1,
        size: 10,
        total: 0,
        pages: 0,
        records: [],
      },
      success: false,
      fail: true,
    }
  }
}
