import api from '@/services/api'

/**
 * @typedef {Object} MediaResource
 * @property {number} id
 * @property {'image' | 'video' | 'audio'} type
 * @property {string} url
 * @property {string=} title
 * @property {string=} description
 */

/**
 * @typedef {Object} TeamDivision
 * @property {string} name
 * @property {string} role
 */

/**
 * @typedef {Object} Award
 * @property {number} id
 * @property {string} competitionName
 * @property {string} competitionTrack
 * @property {string} competitionLevel
 * @property {string} awardLevel
 * @property {string[]} winners
 * @property {number} year
 * @property {string} awardDate
 */

/**
 * @typedef {Object} ProjectDetail
 * @property {number} projectId
 * @property {string} title
 * @property {string} category
 * @property {string} timeRange
 * @property {MediaResource[]} mediaResources
 * @property {string[]} techStackTags
 * @property {string} descriptionMd
 * @property {TeamDivision[]} teamDivisions
 * @property {Award[]} awards
 * @property {string | null} createTime
 * @property {string | null} updateTime
 */

/**
 * @typedef {Object} Project
 * @property {number} id
 * @property {string} category
 * @property {string} coverImage
 * @property {string} title
 * @property {string} briefIntro
 * @property {string[]} techStackTags
 * @property {{ name?: string, role?: string }[]=} teamMembers
 * @property {string} createTime
 * @property {string} updateTime
 */

/**
 * @typedef {Object} ProjectListParams
 * @property {number=} current
 * @property {number=} page
 * @property {number=} size
 * @property {number=} limit
 * @property {string=} keyword
 * @property {string=} competitionType
 * @property {string=} techStack
 */

/**
 * 获取项目详情，并保持与后端返回结构一致。
 *
 * @param {string} id 项目 ID
 * @returns {Promise<ProjectDetail>}
 */
export const getProjectDetail = async id => {
  try {
    const response = await api.get(`/projects/${id}`)

    if (response.data.code === '0' && response.data.data) {
      return response.data.data
    }

    throw new Error('获取项目详情失败')
  } catch (error) {
    console.error('获取项目详情失败:', error)
    throw error
  }
}

/**
 * 获取项目列表；失败时返回空分页结果，避免列表页直接崩溃。
 *
 * @param {ProjectListParams=} params 查询参数
 * @returns {Promise<{
 *   code: string,
 *   message: string,
 *   data: {
 *     current: number,
 *     size: number,
 *     total: number,
 *     pages: number,
 *     records: Project[],
 *   },
 *   success: boolean,
 *   fail: boolean,
 * }>}
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
