import api from '@/services/api'
import type { Result } from '@/services/api'

// 报名信息分页查询参数
export interface GetApplicationsParams {
  current?: number
  size?: number
  name?: string
  studentId?: string
  department?: string // 添加部门字段
  secondDepartment?: string // 添加第二志愿部门字段
}

// 报名信息分页响应数据
export interface ApplicationPageData<T> {
  current: number
  size: number
  total: number
  pages: number
  records: T[]
}


// 奖项定义
export interface AwardItem {
  id: number
  title?: string
  description?: string
  awardDate?: string
  type?: string
  competitionName?: string
  competitionLevel: string
  competitionTrack?: string
  awardLevel: string
  winners?: string[]
  year?: number
}

// 获取奖项列表
export const getAwards = async (
  params: { keyword?: string } = {}
): Promise<AwardItem[]> => {
  try {
    // 从API获取奖项数据
    const response = await api.get<any>(`/public/awards`)

    // 检查响应是否存在
    if (!response || !response.data) {
      console.warn('API响应为空')
      return []
    }

    // 根据后端实际返回格式，数据直接在response.data.data中
    let rawAwards: AwardItem[] = []

    // 适配后端返回格式：data直接是奖项数组
    if (Array.isArray(response.data.data)) {
      rawAwards = response.data.data as AwardItem[]
    }
    // 保留向后兼容性：检查是否有awards字段
    else if (response.data.data && Array.isArray(response.data.data.awards)) {
      rawAwards = response.data.data.awards
    }
    // 备选格式：response.data直接是数组
    else if (Array.isArray(response.data)) {
      rawAwards = response.data as AwardItem[]
    } else {
      console.warn('API响应格式不符合预期，返回空数组')
      return []
    }

    let filteredAwards = rawAwards

    // 关键词搜索功能 - 搜索competitionName等字段
    if (params.keyword) {
      const keyword = params.keyword.toLowerCase()
      filteredAwards = rawAwards.filter(
        (award: AwardItem) =>
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
    // 发生错误时返回空数组，确保组件不会崩溃
    return []
  }
}

// 创建奖项 - 更新接口参数以匹配新数据结构
export const createAward = async (
  params: Omit<AwardItem, 'id'>
): Promise<AwardItem> => {
  try {
    const response = await api.post<Result<{ id: number }>>(
      `/admin/awards`,
      params
    )

    if (!response.data) {
      throw new Error(response?.data ? String(response.data) : '创建奖项失败')
    }

    // 返回完整的奖项对象
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
export const updateAward = async (
  id: number,
  params: Omit<AwardItem, 'id'>
): Promise<AwardItem> => {
  try {
    await api.put<null>(`/admin/awards/${id}`, params)

    // 返回更新后的奖项对象
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
export const deleteAward = async (id: number): Promise<void> => {
  try {
    await api.delete(`/admin/awards/${id}`)
  } catch (error) {
    console.error('删除奖项失败:', error)
    throw error
  }
}

// 管理员类型定义
export interface Admin {
  id: number
  username: string
  password: string // 应存储加密后的密码
  role: 'super' | 'normal' // super为超级管理员，normal为普通管理员
  createTime: string
  updateTime: string
  token?: string // 添加可选的token字段
}

// 获取所有管理员列表
export const getAdmins = async (): Promise<Admin[]> => {
  try {
    const response = await api.get<Result<Admin[]>>(`/admin/admins/page`)
    return response.data.data
  } catch (error) {
    console.error('获取管理员列表失败:', error)
    throw error
  }
}

// 添加管理员
export const createAdmin = async (
  params: Omit<Admin, 'id' | 'createTime' | 'updateTime'>
): Promise<Admin> => {
  try {
    const response = await api.post<Result<Admin>>(
      `/admin/admins/add`,
      params
    )
    return response.data.data
  } catch (error) {
    console.error('添加管理员失败:', error)
    throw error
  }
}

// 删除管理员
export const deleteAdmin = async (id: number): Promise<void> => {
  try {
    await api.delete<Result<null>>(`/admin/admins/${id}`)
  } catch (error) {
    console.error('删除管理员失败:', error)
    throw error
  }
}

// 更新管理员信息
export const updateAdmin = async (
  id: number,
  params: Partial<Omit<Admin, 'id' | 'createTime' | 'updateTime'>>
): Promise<Admin> => {
  try {
    const response = await api.put<Result<Admin>>(`/admins/${id}`, params)
    return response.data.data
  } catch (error) {
    console.error('更新管理员信息失败:', error)
    throw error
  }
}

// 获取当前登录管理员信息
export const getCurrentAdmin = async (): Promise<Admin> => {
  try {
    const response = await api.get<Result<Admin>>(`/admin/admins/me`)
    return response.data.data
  } catch (error) {
    console.error('获取当前管理员信息失败:', error)
    throw error
  }
}

// 登录
export const login = async (params: {
  username: string
  password: string
}): Promise<Admin> => {
  try {
    const response = await api.post<Result<Admin>>(
      '/public/admins/login',
      params
    )

    // 验证响应格式
    if (!response.data) {
      throw new Error('登录失败，服务器响应异常')
    }

    if (
      !response.data ||
      !('success' in response.data) ||
      !response.data.success
    ) {
      throw new Error(response.data.message || '登录失败')
    }

    // 存储token到本地
    if (response.data.data && response.data.data.token) {
      localStorage.setItem('adminToken', response.data.data.token)
    }
    return response.data.data
  } catch (error) {
    console.error('登录失败:', error)
    // 增强错误信息
    if (error instanceof Error) {
      if (error.message.includes('401')) {
        throw new Error('用户名或密码错误')
      } else if (error.message.includes('403')) {
        throw new Error('账号被禁用，请联系管理员')
      }
    }
    throw error
  }
}

// 获取报名信息列表
export const getApplications = async (
  params: GetApplicationsParams = {}
): Promise<ApplicationPageData<any>> => {
  try {
    const response = await api.get<Result<ApplicationPageData<any>>>(
      `/public/applications`,
      {
        params,
      }
    )

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
export const deleteApplication = async (id: number): Promise<void> => {
  try {
    await api.delete(`/public/applications/${id}`)
  } catch (error) {
    console.error('删除报名信息失败:', error)
    throw error
  }
}

// 项目类型定义
// 更新项目类型定义，完全匹配新API的数据结构
// 完善项目接口定义
interface TeamMember {
  id?: number
  name: string
  role: string
}

// 媒体资源类型定义 - 已移除重复定义，保留下面的export版本

// 更新Project接口以适应新的数据格式
export interface Project {
  id: number
  title: string
  timeRange: string
  descriptionMd: string
  briefIntro: string
  coverImage: string
  type: string
  category?: string
  techStackTags?: string[]
  teamDivision?: TeamMember[]
  mediaResourceIds?: number[] // 替换为媒体资源ID数组
  awardIds?: number[] // 奖项ID数组
  createTime: string
  updateTime: string
  techStackDetail?: string[]
}

// 获取未引用的媒体资源
// 增强的getUnreferencedMedia函数，支持更多数据格式并添加详细调试日志
// 修复的getUnreferencedMedia函数，适配实际后端返回格式
export const getUnreferencedMedia = async (): Promise<MediaResource[]> => {
  try {
    const response = await api.get<any>('/admin/media/unreferenced')

    console.log('📡 未引用媒体资源API响应:', response)
    console.log('📊 响应数据结构:', JSON.stringify(response.data, null, 2))

    if (!response || !response.data) {
      console.warn('⚠️ 获取未引用媒体资源：响应为空')
      return []
    }

    // 情况1: 标准成功响应格式 {success: true, data: MediaResource[]}
    if (response.data.success && Array.isArray(response.data.data)) {
      console.log('✅ 匹配格式1：success=true, data为数组')
      return response.data.data
    }

    // 情况2: 无论code是什么值，只要data是数组就返回（关键修复）
    else if (response.data.data && Array.isArray(response.data.data)) {
      console.log(`✅ 匹配格式2：data字段为数组（code=${response.data.code}）`)
      return response.data.data
    }

    // 情况3: response.data直接是MediaResource[]数组
    else if (Array.isArray(response.data)) {
      console.log('✅ 匹配格式3：response.data为数组')
      return response.data
    }

    // 情况4: response.data有一个mediaResources字段（数组）
    else if (
      response.data.mediaResources &&
      Array.isArray(response.data.mediaResources)
    ) {
      console.log('✅ 匹配格式4：response.data.mediaResources为数组')
      return response.data.mediaResources
    }

    // 情况5: response.data是一个单一的MediaResource对象
    else if (
      typeof response.data === 'object' &&
      response.data.id &&
      response.data.url
    ) {
      console.log('✅ 匹配格式5：response.data是单一媒体资源对象')
      return [response.data]
    }

    // 情况6: response.data有其他可能包含媒体资源数组的字段
    // 这是一个更通用的尝试，可以根据实际情况扩展
    const possibleMediaFields = [
      'media',
      'medias',
      'resources',
      'files',
      'items',
    ]
    for (const field of possibleMediaFields) {
      if (response.data[field] && Array.isArray(response.data[field])) {
        console.log(`✅ 匹配格式6：找到${field}字段为数组`)
        return response.data[field]
      }
    }

    console.warn('❌ 获取未引用媒体资源失败：响应格式不符合预期')
    console.warn('❓ 无法识别的响应格式：', response.data)
    return []
  } catch (error) {
    console.error('💥 获取未引用媒体资源失败:', error)
    return []
  }
}

// 在文件末尾添加以下两个函数（在deleteProject函数后面）

// 删除媒体资源
export const deleteMedia = async (id: number): Promise<void> => {
  try {
    const response = await api.delete<any>(`/admin/media/${id}`)

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

// 上传媒体资源
export const uploadMedia = async (params: {
  file: File
  title: string
  description: string
}): Promise<MediaResource> => {
  try {
    const formData = new FormData()
    formData.append('file', params.file)
    formData.append('title', params.title)
    formData.append('description', params.description)

    const response = await api.post<any>('/admin/media/upload', formData, {
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

// 将MediaResource接口从interface改为export interface，使其可以被导入
export interface MediaResource {
  id: number
  type: string
  url: string
  title?: string
  description?: string
}

export const getProject = async (id: number): Promise<Project> => {
  try {
    const response = await api.get<any>(`/public/projects/${id}`)

    // 增强响应处理：支持多种返回格式
    if (typeof response.data === 'object') {
      // 情况1: 标准响应格式 {success: true, data: {项目数据}}
      return response.data.data
    }

    throw new Error('获取项目详情失败')
  } catch (error) {
    console.error('获取项目详情失败:', error)
    throw error
  }
}

// 添加缺失的项目相关API函数
// 创建项目
export const createProject = async (
  params: Omit<Project, 'id'>
): Promise<Project> => {
  try {
    const response = await api.post<any>('/admin/projects', params)

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
export const updateProject = async (
  id: number,
  params: Omit<Project, 'id'>
): Promise<Project> => {
  try {
    const response = await api.put<any>(`/admin/projects/${id}`, params)

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
export const deleteProject = async (id: number): Promise<void> => {
  try {
    const response = await api.delete<any>(`/admin/projects/${id}`)

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

// 项目列表分页响应数据
export interface ProjectPageData<T> {
  current: number
  size: number
  total: number
  pages: number
  records: T[]
}

// 添加getProjects函数定义
export const getProjects = async (params?: {
  current?: number
  size?: number
  keyword?: string
}): Promise<ProjectPageData<Project>> => {
  try {
    const response = await api.get<any>('/public/projects', { params })

    if (typeof response.data === 'object') {
      // 支持标准响应格式 {success: true, data: {项目数据}}
      if (response.data.success && response.data.data) {
        return response.data.data
      }
      // 支持标准响应格式 {code: '0' 或 code: '3', data: {项目数据}}
      else if (
        (response.data.code === '0' || response.data.code === '3') &&
        response.data.data
      ) {
        return response.data.data
      }
      // 支持直接返回分页数据
      else if (response.data.records) {
        return response.data
      }
    }

    throw new Error('获取项目列表失败')
  } catch (error) {
    console.error('获取项目列表失败:', error)
    throw error
  }
}
