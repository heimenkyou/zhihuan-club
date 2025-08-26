import api from './api';
import type { ApiResponse } from './messageService';
import type { joinForm } from './applicationsService';

// 报名信息分页查询参数
export interface GetApplicationsParams {
  current?: number;
  size?: number;
  name?: string;
  studentId?: string;
}

// 报名信息分页响应数据
export interface ApplicationPageData<T> {
  current: number;
  size: number;
  total: number;
  pages: number;
  records: T[];
}

// 奖项类型定义 - 与用户提供的Award接口保持一致
interface Award {
  id: number;
  title: string; // 奖项名称
  workName: string; // 作品名称
  level: "national" | "provincial" | "school"; // 奖项级别
  year: "2023" | "2022" | "2021"; // 年份
  date: string; // 具体日期
}

export interface AwardItem extends Award {}

// 获取报名信息列表
export const getApplications = async (
  params: GetApplicationsParams = { current: 1, size: 10 }
): Promise<ApplicationPageData<joinForm>> => {
  try {
    const response = await api.get<ApiResponse<ApplicationPageData<joinForm>>>(
      '/applications', // 修复路径，添加/admin前缀
      { params }
    );
    return response.data.data;
  } catch (error) {
    console.error('获取报名信息失败:', error);
    throw error;
  }
};

// 删除报名信息
export const deleteApplication = async (id: number): Promise<void> => {
  try {
    await api.delete<ApiResponse<null>>(`/applications/${id}`);
  } catch (error) {
    console.error('删除报名信息失败:', error);
    throw error;
  }
};

// 级别映射函数 - 将英文级别转换为中文显示
export const mapLevelToDisplay = (level: string): string => {
  const levelMap: Record<string, string> = {
    'national': '国家级',
    'provincial': '省级',
    'school': '校级'
  };
  return levelMap[level.toLowerCase()] || '其他';
};

// 获取奖项列表
export const getAwards = async (
  params: { current?: number; size?: number; keyword?: string } = { current: 1, size: 10 }
): Promise<ApplicationPageData<AwardItem> | undefined> => {
  try {
    // 从API获取奖项数据
    const response = await api.get<ApiResponse<{ awards: AwardItem[] }>>(`/awards`, {
      params
    });
    
    // 检查响应格式
    if (!response.data || !response.data.data || !response.data.data.awards || !Array.isArray(response.data.data.awards)) {
      throw new Error('API响应格式不正确，缺少awards数组');
    }
    
    const rawAwards = response.data.data?.awards || [];
    
    // 处理id类型转换（字符串转数字）
    const parsedAwards = rawAwards.map((award: AwardItem) => ({
      ...award,
      id: parseInt(award.id as unknown as string, 10) || 0
    }));

    // 关键词搜索过滤
    let filteredAwards = parsedAwards;
    if (params.keyword) {
      const keyword = params.keyword.toLowerCase();
      filteredAwards = parsedAwards.filter((award: AwardItem) =>
        award.title?.toLowerCase().includes(keyword) ||
        award.workName?.toLowerCase().includes(keyword) ||
        award.level?.toLowerCase().includes(keyword)
      );
    }
    
    // 分页处理
    const current = params.current || 1;
    const size = params.size || 10;
    const startIndex = (current - 1) * size;
    const endIndex = startIndex + size;
    const paginatedAwards = filteredAwards.slice(startIndex, endIndex);
    
    // 返回格式化后的分页数据
    return {
      current,
      size,
      total: filteredAwards.length,
      pages: Math.ceil(filteredAwards.length / size),
      records: paginatedAwards
    };
  } catch (error) {
    console.error('获取奖项列表失败:', error);
    throw error;
  }
};

// 创建奖项
export const createAward = async (params: Omit<AwardItem, 'id'>): Promise<AwardItem> => {
  try {
    const response = await api.post<ApiResponse<{id: number}>>(`/awards`, params);
    
    if (!response.data) {
      throw new Error(response?.data? String(response.data) : '创建奖项失败');
    }
    
    // 返回完整的奖项对象
    return {
      id: response.data.data.id,
      ...params
    };
  } catch (error) {
    console.error('创建奖项失败:', error);
    throw error;
  }
};

// 更新奖项
export const updateAward = async (
  id: number,
  params: Omit<AwardItem, 'id'>
): Promise<AwardItem> => {
  try {
    await api.put<null>(`/awards/${id}`, params);
    
    // 返回更新后的奖项对象
    return {
      id,
      ...params
    };
  } catch (error) {
    console.error('更新奖项失败:', error);
    throw error;
  }
};

// 删除奖项
export const deleteAward = async (id: number): Promise<void> => {
  try {
    await api.delete(`/awards/${id}`);
  } catch (error) {
    console.error('删除奖项失败:', error);
    throw error;
  }
};

// 获取所有奖项
export const getAllAwards = async (): Promise<AwardItem[]> => {
  try {
    // 从json-server获取所有奖项数据
    const response = await api.get<ApiResponse<AwardItem[]>>(`/awards`);
    
    if (!response.data) {
      throw new Error(response?.data ? String(response.data) : '获取奖项失败');
    }
    
    return response.data.data;
  } catch (error) {
    console.error('获取所有奖项失败:', error);
    throw error;
  }
};

// 管理员类型定义
export interface Admin {
  id: number;
  username: string;
  password: string; // 实际项目中应存储加密后的密码
  role: 'super' | 'normal'; // super为超级管理员，normal为普通管理员
  createdAt: string;
  updatedAt: string;
}

// 获取所有管理员列表
export const getAdmins = async (): Promise<Admin[]> => {
  try {
    const response = await api.get<ApiResponse<Admin[]>>(`/admins`);
    return response.data.data;
  } catch (error) {
    console.error('获取管理员列表失败:', error);
    throw error;
  }
};

// 添加管理员
export const createAdmin = async (params: Omit<Admin, 'id' | 'createdAt' | 'updatedAt'>): Promise<Admin> => {
  try {
    const response = await api.post<ApiResponse<Admin>>(`/admins`, params);
    return response.data.data;
  } catch (error) {
    console.error('添加管理员失败:', error);
    throw error;
  }
};

// 删除管理员
export const deleteAdmin = async (id: number): Promise<void> => {
  try {
    await api.delete<ApiResponse<null>>(`/admins/${id}`);
  } catch (error) {
    console.error('删除管理员失败:', error);
    throw error;
  }
};

// 更新管理员信息
export const updateAdmin = async (
  id: number,
  params: Partial<Omit<Admin, 'id' | 'createdAt' | 'updatedAt'>>
): Promise<Admin> => {
  try {
    const response = await api.put<ApiResponse<Admin>>(`/admins/${id}`, params);
    return response.data.data;
  } catch (error) {
    console.error('更新管理员信息失败:', error);
    throw error;
  }
};

// 获取当前登录管理员信息
export const getCurrentAdmin = async (): Promise<Admin> => {
  try {
    const response = await api.get<ApiResponse<Admin>>(`/admins/current`);
    return response.data.data;
  } catch (error) {
    console.error('获取当前管理员信息失败:', error);
    throw error;
  }
};

// 登录
export const login = async (params: { username: string; password: string }): Promise<Admin> => {
  try {
    const response = await api.post<ApiResponse<Admin>>('/admin/login', params);
    
    // 验证响应格式
    if (!response.data) {
      throw new Error('登录失败，服务器响应异常');
    }
    
    if (!response.data || !('success' in response.data) || !response.data.success) {
      throw new Error(response.data.message || '登录失败');
    }
    
    // 存储token到本地
    if (response.headers && response.headers.authorization) {
      localStorage.setItem('adminToken', response.headers.authorization);
    }
    
    return response.data.data;
  } catch (error) {
    console.error('登录失败:', error);
    // 增强错误信息
    if (error instanceof Error) {
      if (error.message.includes('401')) {
        throw new Error('用户名或密码错误');
      } else if (error.message.includes('403')) {
        throw new Error('账号被禁用，请联系管理员');
      }
    }
    throw error;
  }
};
