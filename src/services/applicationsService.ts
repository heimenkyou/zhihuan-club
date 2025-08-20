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
  className: string;
  major: string;
  phone: string;
  QQNumber: string;
  department: string;
  introduction: string;
  reason: string;
}

export const submitApplication = async (params: joinForm): Promise<void> => {
  try {
    console.log("🚀 发送请求参数:", params);

    // 使用查询字符串格式发送请求
    const queryParams = new URLSearchParams();
    queryParams.append("name", params.name);
    queryParams.append("studentId", params.studentId);
    queryParams.append("className", params.className);
    queryParams.append("major", params.major);
    queryParams.append("phone", params.phone);
    queryParams.append("QQNumber", params.QQNumber);
    queryParams.append("department", params.department);
    queryParams.append("introduction", params.introduction);
    queryParams.append("reason", params.reason);

    await api.post<ApiResponse<null>>(
      `/applications?${queryParams.toString()}`
    );
  } catch (error) {
    console.error("提交申请失败:", error);
    throw error;
  }
};
