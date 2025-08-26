// applicationsService.ts 修复版本
import api from "./api";
// API响应类型
export interface ApiResponse<T> {
  code: string
  message: string
  data: T
  requestId: string
}
export interface joinForm {
  name: string;
  studentId: string;
  major: string;
  phone: string;
  QQNumber: string;
  department: string;
  secondDepartment: string;
  interests: string[];
  reason: string;
  introduction: string;
  createTime?: string;  // 添加createTime属性（可选，因为提交表单时可能不需要）
}

export const submitApplication = async (params: joinForm): Promise<ApiResponse<null>> => {
  try {
    console.log("🚀 发送请求参数:", params);

    // 使用JSON格式发送请求
    const response = await api.post<ApiResponse<null>>(
      '/m1/6949200-6665662-default/applications',
      params // 直接传递参数对象，axios会自动转为JSON
    );
    
    // 打印响应数据到控制台
    console.log("📝 响应数据:", response.data);
    
    // 返回响应数据
    return response.data as unknown as ApiResponse<null>;
  } catch (error) {
    console.error("提交申请失败:", error);
    throw error;
  }
};
