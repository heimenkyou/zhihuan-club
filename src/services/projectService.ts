import api, { type Result, type PageData } from '@/services/api'

// 媒体资源类型定义
export interface MediaResource {
  id: number
  type: 'image' | 'video' | 'audio'
  url: string
  title?: string
  description?: string
}

// 团队成员类型定义
export interface TeamDivision {
  name: string
  role: string
}

// 奖项类型定义
export interface Award {
  id: number
  competitionName: string
  competitionTrack: string
  competitionLevel: string
  awardLevel: string
  winners: string[]
  year: number
  awardDate: string
}

// 项目详情类型定义 - 与接口完全匹配
export interface ProjectDetail {
  projectId: number
  title: string
  category: string
  timeRange: string
  mediaResources: MediaResource[]
  techStackTags: string[]
  descriptionMd: string
  teamDivisions: TeamDivision[]
  awards: Award[]
  createTime: string | null
  updateTime: string | null
}

// 获取项目详情
export const getProjectDetail = async (id: string): Promise<ProjectDetail> => {
  try {
    const response = await api.get<any>(`/public/projects/${id}`)

    // 直接返回接口数据，不再进行兼容性处理
    if (response.data.code === '0' && response.data.data) {
      return response.data.data as ProjectDetail
    }

    throw new Error('获取项目详情失败')
  } catch (error) {
    console.error('获取项目详情失败:', error)
    throw error
  }
}

// 获取项目列表
export const getProjects = async (
  params?: ProjectListParams
): Promise<Result<PageData<Project>>> => {
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
export interface Project {
  id: number
  category: string
  coverImage: string
  title: string
  briefIntro: string
  techStackTags: string[]
  teamMembers?: Array<{ name?: string; role?: string }>
  createTime: string
  updateTime: string
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
