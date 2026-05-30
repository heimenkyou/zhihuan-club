import api from '@/services/api'
import type { Result } from '@/services/api'

// 代码提交数据类型
export interface CodeSubmission {
  id: number
  studentId: string
  name: string
  description: string
  createTime: string
  updateTime: string
}

// 代码提交请求数据类型
export interface CodeSubmissionReq {
  studentId: string
  name: string
  description: string
}

/**
 * 提交代码
 * @param params 代码提交参数
 * @returns 提交结果
 */
export const submitCode = async (params: CodeSubmissionReq): Promise<Result<null>> => {
  try {
    console.log('🚀 发送代码提交请求参数:', params)
    
    const response = await api.post<Result<null>>('/admin/code-submissions', params)
    return response.data
  } catch (error) {
    console.error('代码提交失败:', error)
    const msg = error instanceof Error ? error.message : String(error)
    throw new Error(`代码提交失败：${msg}`)
  }
}

/**
 * 查询代码提交记录（管理员）
 * @param params 查询参数
 * @returns 代码提交记录列表
 */
export const getCodeSubmissions = async (params: {
  current?: number
  size?: number
  studentId?: string
  name?: string
} = {}): Promise<Result<any>> => {
  try {
    const response = await api.get<Result<any>>('/admin/code-submissions', {
      params
    })
    return response.data
  } catch (error) {
    console.error('查询代码提交记录失败:', error)
    const msg = error instanceof Error ? error.message : String(error)
    throw new Error(`查询代码提交记录失败：${msg}`)
  }
}

/**
 * 查询单个代码提交记录（管理员）
 * @param id 提交ID
 * @returns 代码提交记录详情
 */
export const getCodeSubmissionById = async (id: number): Promise<Result<CodeSubmission>> => {
  try {
    const response = await api.get<Result<CodeSubmission>>(`/admin/code-submissions/${id}`)
    return response.data
  } catch (error) {
    console.error('查询代码提交记录详情失败:', error)
    const msg = error instanceof Error ? error.message : String(error)
    throw new Error(`查询代码提交记录详情失败：${msg}`)
  }
}

/**
 * 更新代码提交记录（管理员）
 * @param id 提交ID
 * @param params 更新参数
 * @returns 更新结果
 */
export const updateCodeSubmission = async (
  id: number,
  params: Partial<CodeSubmissionReq>
): Promise<Result<null>> => {
  try {
    const response = await api.put<Result<null>>(`/admin/code-submissions/${id}`, params)
    return response.data
  } catch (error) {
    console.error('更新代码提交记录失败:', error)
    const msg = error instanceof Error ? error.message : String(error)
    throw new Error(`更新代码提交记录失败：${msg}`)
  }
}

/**
 * 删除代码提交记录（管理员）
 * @param id 提交ID
 * @returns 删除结果
 */
export const deleteCodeSubmission = async (id: number): Promise<Result<null>> => {
  try {
    const response = await api.delete<Result<null>>(`/admin/code-submissions/${id}`)
    return response.data
  } catch (error) {
    console.error('删除代码提交记录失败:', error)
    const msg = error instanceof Error ? error.message : String(error)
    throw new Error(`删除代码提交记录失败：${msg}`)
  }
}

/**
 * 普通用户提交代码和视频
 * @param formData 包含文件的表单数据
 * @returns 提交结果
 */
export const submitUserCode = async (formData: FormData): Promise<Result<null>> => {
  try {
    console.log('🚀 用户发送代码和视频提交请求')
    
    const response = await api.post<Result<null>>('/code-submissions', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    return response.data
  } catch (error) {
    console.error('用户代码提交失败:', error)
    const msg = error instanceof Error ? error.message : String(error)
    throw new Error(`代码提交失败：${msg}`)
  }
}