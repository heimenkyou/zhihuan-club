import api from '@/services/api'
// API响应类型
import { type ApiResponse } from '@/services/api'

export interface joinForm {
  name: string
  studentId: string
  major: string
  phone: string
  QQNumber: string
  department: string
  secondDepartment: string
  interests: string[]
  reason: string
  introduction: string
  createTime?: string // 添加createTime属性（可选，因为提交表单时可能不需要）
}

// 专业映射数据接口
export interface MajorMapping {
  [key: string]: {
    code: string
    shortName: string
    fullName: string
  }
}

/**
 * 获取专业代号与名称映射表
 * @returns 专业映射表
 */
export const getMajorMapping = async (): Promise<MajorMapping> => {
  try {
    const response = await api.get<ApiResponse<MajorMapping>>('/public/majors')
    return response.data.data
  } catch (error) {
    console.error('获取专业映射表失败:', error)
    const msg = error instanceof Error ? error.message : String(error)
    throw new Error(`获取专业映射表失败：${msg}`)
  }
}

export const submitApplication = async (
  params: joinForm
): Promise<ApiResponse<null>> => {
  try {
    console.log('🚀 发送请求参数:', params)

    // 使用JSON格式发送请求
    const response = await api.post<ApiResponse<null>>(
      '/public/applications',
      params // 直接传递参数对象，axios会自动转为JSON
    )

    // 打印响应数据到控制台
    console.log('📝 响应数据:', response.data)

    // 返回响应数据
    return response.data as unknown as ApiResponse<null>
  } catch (error) {
    console.error('提交申请失败:', error)
    const msg = error instanceof Error ? error.message : String(error)
    throw new Error(`提交申请失败：${msg}`)
  }
}
