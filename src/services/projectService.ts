import api from '@/services/api'
import type { AwardItem } from '@/services/adminService'

// 项目详情类型定义
export interface ProjectDetail {
  id: number
  title: string
  timeRange: string
  descriptionMd: string
  briefIntro: string
  coverImage: string
  type: string
  category?: string
  techStackTags?: string[]
  // 更新 techStackDetail 类型为对象数组
  techStackDetail?: Array<{ name: string }>
  // 添加 teamMembers 字段（后端使用的字段名）
  teamMembers?: Array<{ id?: number; name?: string; role?: string }>
  // 添加 teamDivision 字段作为兼容性处理
  teamDivision?: Array<{ name?: string; role?: string }>
  mediaResources?: Array<{
    id: number
    url: string
    type: string
    title?: string
    description?: string
    caption?: string
  }>
  // 添加 awards 字段（后端使用的字段名）
  awards?: Array<{
    date?: string
    title?: string
    description?: string
    type?: string
  }>
  // 添加 awardList 字段作为兼容性处理
  awardList?: AwardItem[]
  createTime: string
  updateTime: string
}

// 获取项目详情
export const getProjectDetail = async (id: string): Promise<ProjectDetail> => {
  try {
    const response = await api.get<any>(`/public/projects/${id}`)

    let projectData = null

    // 增强响应处理：支持多种返回格式
    if (typeof response.data === 'object') {
      // 情况1: 标准响应格式 {success: true, data: {项目数据}}
      if (response.data.success && response.data.data) {
        projectData = processProjectData(response.data.data)
      }
      // 情况2: 标准响应格式 {code: '0', data: {项目数据}}
      else if (response.data.code === '0' && response.data.data) {
        projectData = processProjectData(response.data.data)
      }
      // 情况3: 直接返回项目对象（如您提供的数据格式）
      else {
        projectData = processProjectData(response.data)
      }
    }

    if (projectData) {
      return projectData
    }

    throw new Error('获取项目详情失败')
  } catch (error) {
    console.error('获取项目详情失败:', error)
    throw error
  }
}

// 抽取数据处理逻辑到独立函数
function processProjectData(data: any): ProjectDetail | null {
  if (!data) return null

  let projectData = data

  // 数据兼容处理：确保字段名一致
  // 处理团队成员字段兼容
  if (projectData.teamMembers && !projectData.teamDivision) {
    projectData.teamDivision = projectData.teamMembers
  }
  // 处理奖项字段兼容
  if (projectData.awards && !projectData.awardList) {
    // 转换awards数组为awardList数组
    projectData.awardList = projectData.awards.map((award: any) => ({
      id: award.id || Date.now(), // 提供默认ID
      title: award.title,
      description: award.description,
      awardDate: award.date,
      awardLevel: award.type,
      competitionName: award.title,
      winners: [],
    }))
  }
  // 处理技术栈字段兼容
  if (
    projectData.techStackDetail &&
    Array.isArray(projectData.techStackDetail)
  ) {
    // 如果techStackDetail是对象数组，提取name字段
    if (
      projectData.techStackDetail.length > 0 &&
      typeof projectData.techStackDetail[0] === 'object'
    ) {
      projectData.techStackDetail = projectData.techStackDetail.map(
        (tech: any) => tech.name
      )
    }
  }
  // 处理图片URL中的空格和反引号
  if (projectData.coverImage) {
    projectData.coverImage = projectData.coverImage.replace(/[`\s]/g, '')
  }
  if (projectData.mediaResources) {
    projectData.mediaResources.forEach((media: any) => {
      if (media.url) {
        media.url = media.url.replace(/[`\s]/g, '')
      }
    })
  }

  // 确保所有必要字段都有默认值
  projectData.type = projectData.type || 'project'

  return projectData as ProjectDetail
}

// 获取项目列表
export const getProjects = async (
  params?: ProjectListParams
): Promise<ProjectListResponse> => {
  try {
    const response = await api.get<any>('/public/projects', { params })
    return response.data
  } catch (error) {
    console.error('获取项目列表失败:', error)
    // 返回默认值以避免应用崩溃
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

// 项目列表项类型定义
export interface Project extends ProjectDetail {
  // 添加后端返回的特有字段
  teamMembers?: Array<{ name?: string; role?: string }>
}

// 分页响应类型定义
export interface ProjectListResponse {
  code: string
  message: string
  data: {
    current: number
    size: number
    total: number
    pages: number
    records: Project[]
  }
  requestId?: string
  success: boolean
  fail: boolean
}

// 项目列表请求参数类型定义
export interface ProjectListParams {
  current?: number
  page?: number
  size?: number
  limit?: number
  keyword?: string
  competitionType?: string
  techStack?: string
}
