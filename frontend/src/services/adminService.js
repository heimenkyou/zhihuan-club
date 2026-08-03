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
 * 从响应体中提取数组数据。
 *
 * @param {any} body
 * @returns {any[]}
 */
const getArrayData = body => {
  const data = getResultData(body)

  if (Array.isArray(data)) {
    return data
  }

  if (data && Array.isArray(data.awards)) {
    return data.awards
  }

  if (Array.isArray(body)) {
    return body
  }

  return []
}

/**
 * 从媒体相关响应体中提取媒体数组。
 *
 * @param {any} body
 * @returns {any[]}
 */
const getMediaList = body => {
  const data = getResultData(body)

  if (Array.isArray(data)) {
    return data
  }

  if (Array.isArray(body)) {
    return body
  }

  if (data && Array.isArray(data.mediaResources)) {
    return data.mediaResources
  }

  if (data && typeof data === 'object' && data.id && data.url) {
    return [data]
  }

  const possibleMediaFields = ['media', 'medias', 'resources', 'files', 'items']
  for (const field of possibleMediaFields) {
    if (Array.isArray(body?.[field])) {
      return body[field]
    }
  }

  return []
}

/**
 * 获取奖项列表。
 *
 * @param {{ keyword?: string }} [params]
 * @returns {Promise<any[]>}
 */
export const getAwards = async (params = {}) => {
  try {
    const response = await api.get('/awards')
    const rawAwards = getArrayData(getBody(response))

    if (!params.keyword) {
      return rawAwards
    }

    const keyword = params.keyword.toLowerCase()
    return rawAwards.filter(
      award =>
        award.competitionName?.toLowerCase().includes(keyword) ||
        award.competitionLevel?.toLowerCase().includes(keyword) ||
        award.awardLevel?.toLowerCase().includes(keyword) ||
        (Array.isArray(award.winners)
          ? award.winners.some(winner => winner?.toLowerCase().includes(keyword))
          : false) ||
        award.year?.toString().includes(keyword)
    )
  } catch (error) {
    console.error('获取奖项列表失败:', error)
    return []
  }
}

/**
 * 获取后台奖项列表。
 *
 * @param {{ keyword?: string }} [params]
 * @returns {Promise<any[]>}
 */
export const getAdminAwards = async (params = {}) => {
  const response = await api.get('/admin/awards')
  const awards = getArrayData(getBody(response))
  if (!params.keyword) return awards

  const keyword = params.keyword.toLowerCase()
  return awards.filter(
    award =>
      award.competitionName?.toLowerCase().includes(keyword) ||
      award.competitionLevel?.toLowerCase().includes(keyword) ||
      award.awardLevel?.toLowerCase().includes(keyword) ||
      award.winners?.some?.(winner => winner?.toLowerCase().includes(keyword)) ||
      award.year?.toString().includes(keyword)
  )
}

/**
 * 创建奖项。
 *
 * @param {Object} params
 * @returns {Promise<Object>}
 */
export const createAward = async params => {
  try {
    const response = await api.post('/admin/awards', params)
    const body = getBody(response)

    if (!body) {
      throw new Error('创建奖项失败')
    }

    return {
      id: getResultData(body)?.id,
      ...params,
    }
  } catch (error) {
    console.error('创建奖项失败:', error)
    throw error
  }
}

/**
 * 更新奖项。
 *
 * @param {number} id
 * @param {Object} params
 * @returns {Promise<Object>}
 */
export const updateAward = async (id, params) => {
  try {
    await api.put(`/admin/awards/${id}`, params)
    return {
      id,
      ...params,
    }
  } catch (error) {
    console.error('更新奖项失败:', error)
    throw error
  }
}

/**
 * 删除奖项。
 *
 * @param {number} id
 * @returns {Promise<void>}
 */
export const deleteAward = async id => {
  try {
    await api.delete(`/admin/awards/${id}`)
  } catch (error) {
    console.error('删除奖项失败:', error)
    throw error
  }
}

/**
 * 获取管理员分页数据。
 *
 * @returns {Promise<any>}
 */
export const getAdmins = async (params = {}) => {
  try {
    const response = await api.get('/admin/admins', { params })
    return getResultData(getBody(response))
  } catch (error) {
    console.error('获取管理员列表失败:', error)
    throw error
  }
}

/**
 * 创建管理员。
 *
 * @param {Object} params
 * @returns {Promise<any>}
 */
export const createAdmin = async params => {
  try {
    const response = await api.post('/admin/admins', params)
    return getResultData(getBody(response))
  } catch (error) {
    console.error('添加管理员失败:', error)
    throw error
  }
}

/**
 * 删除管理员。
 *
 * @param {number} id
 * @returns {Promise<void>}
 */
export const deleteAdmin = async id => {
  try {
    await api.delete(`/admin/admins/${id}`)
  } catch (error) {
    console.error('删除管理员失败:', error)
    throw error
  }
}

/**
 * 更新管理员信息。
 *
 * @param {number} id
 * @param {Object} params
 * @returns {Promise<any>}
 */
export const updateAdmin = async (id, params) => {
  const response = await api.put(`/admin/admins/${id}`, params)
  return getResultData(getBody(response))
}

/**
 * 获取当前登录管理员信息。
 *
 * @returns {Promise<any>}
 */
export const getCurrentAdmin = async () => {
  try {
    const response = await api.get('/admin/auth/me')
    return getResultData(getBody(response))
  } catch (error) {
    console.error('获取当前管理员信息失败:', error)
    throw error
  }
}

/**
 * 登录管理员。
 *
 * @param {{ username: string, password: string }} params
 * @returns {Promise<any>}
 */
export const login = async params => {
  try {
    const response = await api.post('/admin/auth/login', params)
    const data = getResultData(getBody(response))
    const token = data?.token

    return data
  } catch (error) {
    const message = error instanceof Error ? error.message : '登录失败'
    console.error('登录失败:', message)
    throw new Error(message)
  }
}

/**
 * 登出管理员。
 *
 * @returns {Promise<void>}
 */
export const logout = async () => {
  await api.post('/admin/auth/logout')
}

/**
 * 获取报名专业列表。
 *
 * @returns {Promise<any>}
 */
export const getApplicationMajors = async () => {
  try {
    const response = await api.get('/admin/applications/majors')
    const data = getResultData(getBody(response))

    if (!data) {
      throw new Error('获取专业名称列表失败')
    }

    return data
  } catch (error) {
    console.error('获取专业名称列表失败:', error)
    throw error
  }
}

/**
 * 获取报名分页数据。
 *
 * @param {Object} [params={}]
 * @returns {Promise<any>}
 */
export const getApplications = async (params = {}) => {
  try {
    const response = await api.get('/admin/applications', { params })
    const data = getResultData(getBody(response))

    if (!data) {
      throw new Error('获取报名信息失败')
    }

    return data
  } catch (error) {
    console.error('获取报名信息失败:', error)
    throw error
  }
}

/**
 * 删除报名信息。
 *
 * @param {number} id
 * @returns {Promise<void>}
 */
export const deleteApplication = async id => {
  try {
    await api.delete(`/admin/applications/${id}`)
  } catch (error) {
    console.error('删除报名信息失败:', error)
    throw error
  }
}

/**
 * 获取未被业务引用的媒体资源。
 *
 * @returns {Promise<any[]>}
 */
export const getUnreferencedMedia = async () => {
  try {
    const response = await api.get('/admin/media/unreferenced')
    return getMediaList(getBody(response))
  } catch (error) {
    console.error('获取未引用媒体资源失败:', error)
    return []
  }
}

/**
 * 删除媒体资源。
 *
 * @param {number} id
 * @returns {Promise<void>}
 */
export const deleteMedia = async id => {
  try {
    const response = await api.delete(`/admin/media/${id}`)
    if (isSuccessBody(getBody(response))) {
      return
    }
    throw new Error('删除媒体资源失败')
  } catch (error) {
    console.error('删除媒体资源失败:', error)
    throw error
  }
}

/**
 * 上传媒体资源。
 *
 * @param {{ file: File, title: string, description: string }} params
 * @returns {Promise<any>}
 */
export const uploadMedia = async params => {
  try {
    const formData = new FormData()
    formData.append('file', params.file)
    formData.append('title', params.title)
    formData.append('description', params.description)

    const response = await api.post('/admin/media/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })

    const body = getBody(response)
    const data = getResultData(body)

    if (isSuccessBody(body) && data) {
      return data
    }

    if (body) {
      return body
    }

    throw new Error('上传媒体资源失败')
  } catch (error) {
    console.error('上传媒体资源失败:', error)
    throw error
  }
}

/**
 * 更新当前管理员资料或密码。
 *
 * @param {Object} params
 * @returns {Promise<void>}
 */
export const updateCurrentAdmin = async params => {
  await api.put('/admin/auth/me', params)
}

/**
 * 获取后台留言分页数据。
 *
 * @param {Object} params
 * @returns {Promise<any>}
 */
export const getAdminMessages = async params => {
  const response = await api.get('/admin/messages', { params })
  return getResultData(getBody(response))
}

/**
 * 删除后台留言。
 *
 * @param {number} id
 * @returns {Promise<void>}
 */
export const deleteAdminMessage = async id => {
  await api.delete(`/admin/messages/${id}`)
}
