import api from '@/services/api'

// 报名信息分页查询参数

// 获取奖项列表
export const getAwards = async (params = {}) => {
  try {
    const response = await api.get('/awards')

    if (!response || !response.data) {
      console.warn('API响应为空')
      return []
    }

    let rawAwards = []

    if (Array.isArray(response.data.data)) {
      rawAwards = response.data.data
    } else if (response.data.data && Array.isArray(response.data.data.awards)) {
      rawAwards = response.data.data.awards
    } else if (Array.isArray(response.data)) {
      rawAwards = response.data
    } else {
      console.warn('API响应格式不符合预期，返回空数组')
      return []
    }

    let filteredAwards = rawAwards

    if (params.keyword) {
      const keyword = params.keyword.toLowerCase()
      filteredAwards = rawAwards.filter(
        award =>
          award.competitionName?.toLowerCase().includes(keyword) ||
          award.competitionLevel?.toLowerCase().includes(keyword) ||
          award.awardLevel?.toLowerCase().includes(keyword) ||
          (Array.isArray(award.winners)
            ? award.winners.some(winner =>
                winner?.toLowerCase().includes(keyword)
              )
            : false) ||
          award.year?.toString().includes(keyword)
      )
    }

    return filteredAwards
  } catch (error) {
    console.error('获取奖项列表失败:', error)
    return []
  }
}

// 创建奖项
export const createAward = async params => {
  try {
    const response = await api.post('/admin/awards', params)

    if (!response.data) {
      throw new Error(response?.data ? String(response.data) : '创建奖项失败')
    }

    return {
      id: response.data?.data?.id,
      ...params,
    }
  } catch (error) {
    console.error('创建奖项失败:', error)
    throw error
  }
}

// 更新奖项
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

// 删除奖项
export const deleteAward = async id => {
  try {
    await api.delete(`/admin/awards/${id}`)
  } catch (error) {
    console.error('删除奖项失败:', error)
    throw error
  }
}

// 获取所有管理员列表
export const getAdmins = async () => {
  try {
    const response = await api.get('/admin/admins/page')
    return response.data.data
  } catch (error) {
    console.error('获取管理员列表失败:', error)
    throw error
  }
}

// 添加管理员
export const createAdmin = async params => {
  try {
    const response = await api.post('/admin/admins/add', params)
    return response.data.data
  } catch (error) {
    console.error('添加管理员失败:', error)
    throw error
  }
}

// 删除管理员
export const deleteAdmin = async id => {
  try {
    await api.delete(`/admin/admins/${id}`)
  } catch (error) {
    console.error('删除管理员失败:', error)
    throw error
  }
}

/**
 * 更新管理员信息
 * @param {number} id
 * @param {Object} params
 */
export const updateAdmin = async (id, params) => {
  const response = await api.put(`/admin/admins/${id}`, params)
  return response.data.data
}

// 获取当前登录管理员信息
export const getCurrentAdmin = async () => {
  try {
    const response = await api.get('/admin/admins/me')
    return response.data.data
  } catch (error) {
    console.error('获取当前管理员信息失败:', error)
    throw error
  }
}

/**
 * 登录管理员
 * @param {{ username: string, password: string }} params
 */
export const login = async params => {
  try {
    const response = await api.post('/admins/login', params)
    const token = response.data.data?.token
    if (token) {
      localStorage.setItem('adminToken', token)
    }
    return response.data.data
  } catch (error) {
    const message = error instanceof Error ? error.message : '登录失败'
    console.error('登录失败:', message)
    throw new Error(message)
  }
}

/**
 * 登出管理员
 */
export const logout = async () => {
  await api.post('/admins/logout')
}

// 获取报名信息中所有不重复的专业名称
export const getApplicationMajors = async () => {
  try {
    const response = await api.get('/applications/majors')

    if (!response.data || !response.data.data) {
      throw new Error('获取专业名称列表失败')
    }

    return response.data.data
  } catch (error) {
    console.error('获取专业名称列表失败:', error)
    throw error
  }
}

// 获取报名信息列表
export const getApplications = async (params = {}) => {
  try {
    const response = await api.get('/applications', {
      params,
    })

    if (!response.data || !response.data.data) {
      throw new Error('获取报名信息失败')
    }

    return response.data.data
  } catch (error) {
    console.error('获取报名信息失败:', error)
    throw error
  }
}

// 删除报名信息
export const deleteApplication = async id => {
  try {
    await api.delete(`/applications/${id}`)
  } catch (error) {
    console.error('删除报名信息失败:', error)
    throw error
  }
}

// 获取未引用的媒体资源
export const getUnreferencedMedia = async () => {
  try {
    const response = await api.get('/admin/media/unreferenced')

    console.log('未引用媒体资源API响应:', response)
    console.log('响应数据结构:', JSON.stringify(response.data, null, 2))

    if (!response || !response.data) {
      console.warn('获取未引用媒体资源：响应为空')
      return []
    }

    if (response.data.success && Array.isArray(response.data.data)) {
      console.log('匹配格式1：success=true, data为数组')
      return response.data.data
    } else if (response.data.data && Array.isArray(response.data.data)) {
      console.log(`匹配格式2：data字段为数组（code=${response.data.code}）`)
      return response.data.data
    } else if (Array.isArray(response.data)) {
      console.log('匹配格式3：response.data为数组')
      return response.data
    } else if (
      response.data.mediaResources &&
      Array.isArray(response.data.mediaResources)
    ) {
      console.log('匹配格式4：response.data.mediaResources为数组')
      return response.data.mediaResources
    } else if (
      typeof response.data === 'object' &&
      response.data.id &&
      response.data.url
    ) {
      console.log('匹配格式5：response.data是单一媒体资源对象')
      return [response.data]
    }

    const possibleMediaFields = ['media', 'medias', 'resources', 'files', 'items']
    for (const field of possibleMediaFields) {
      if (response.data[field] && Array.isArray(response.data[field])) {
        console.log(`匹配格式6：找到${field}字段为数组`)
        return response.data[field]
      }
    }

    console.warn('获取未引用媒体资源失败：响应格式不符合预期')
    console.warn('无法识别的响应格式：', response.data)
    return []
  } catch (error) {
    console.error('获取未引用媒体资源失败:', error)
    return []
  }
}

// 删除媒体资源
export const deleteMedia = async id => {
  try {
    const response = await api.delete(`/admin/media/${id}`)

    if (typeof response.data === 'object') {
      if (response.data.success || response.data.code === '0') {
        return
      }
    }

    throw new Error('删除媒体资源失败')
  } catch (error) {
    console.error('删除媒体资源失败:', error)
    throw error
  }
}

/**
 * 上传媒体资源
 * @param {{ file: File, title: string, description: string }} params
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

    if (typeof response.data === 'object') {
      if (response.data.success && response.data.data) {
        return response.data.data
      } else if (response.data.code === '0' && response.data.data) {
        return response.data.data
      } else if (response.data) {
        return response.data
      }
    }

    throw new Error('上传媒体资源失败')
  } catch (error) {
    console.error('上传媒体资源失败:', error)
    throw error
  }
}

/**
 * 项目编辑回显 - 获取完整的项目数据包括关联的媒体资源和奖项
 * @param {number} id
 */
export const getProjectForEdit = async id => {
  try {
    const response = await api.get(`/projects/${id}/edit`)

    if (typeof response.data === 'object') {
      if (response.data.success && response.data.data) {
        return response.data.data
      } else if (response.data.code === '0' && response.data.data) {
        return response.data.data
      }
    }

    throw new Error('获取项目编辑数据失败')
  } catch (error) {
    console.error('获取项目编辑数据失败:', error)
    throw error
  }
}

/**
 * 获取项目详情 - 用于展示
 * @param {number} id
 */
export const getProject = async id => {
  try {
    const response = await api.get(`/projects/${id}`)

    if (typeof response.data === 'object') {
      return response.data.data
    }

    throw new Error('获取项目详情失败')
  } catch (error) {
    console.error('获取项目详情失败:', error)
    throw error
  }
}

// 创建项目
export const createProject = async params => {
  try {
    const response = await api.post('/admin/projects', params)

    if (typeof response.data === 'object') {
      return response.data
    }

    throw new Error('创建项目失败')
  } catch (error) {
    console.error('创建项目失败:', error)
    throw error
  }
}

// 更新项目
export const updateProject = async (id, params) => {
  try {
    const response = await api.put(`/admin/projects/${id}`, params)

    if (typeof response.data === 'object') {
      return response.data
    }

    throw new Error('更新项目失败')
  } catch (error) {
    console.error('更新项目失败:', error)
    throw error
  }
}

// 删除项目
export const deleteProject = async id => {
  try {
    const response = await api.delete(`/admin/projects/${id}`)

    if (
      typeof response.data === 'object' &&
      (response.data.success || response.data.code === '0')
    ) {
      return
    }

    throw new Error('删除项目失败')
  } catch (error) {
    console.error('删除项目失败:', error)
    throw error
  }
}

/**
 * 获取项目列表
 * @param {{ current?: number, size?: number, keyword?: string }} [params]
 */
export const getProjects = async params => {
  try {
    const response = await api.get('/projects', { params })

    if (typeof response.data === 'object') {
      if (response.data.success && response.data.data) {
        return response.data.data
      } else if (
        (response.data.code === '0' || response.data.code === '3') &&
        response.data.data
      ) {
        return response.data.data
      } else if (response.data.records) {
        return response.data
      }
    }

    throw new Error('获取项目列表失败')
  } catch (error) {
    console.error('获取项目列表失败:', error)
    throw error
  }
}
