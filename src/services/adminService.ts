// 添加api导入
import api from './api';
import type { ApiResponse } from './messageService';

// 报名信息分页查询参数
export interface GetApplicationsParams {
  current?: number;
  size?: number;
  name?: string;
  studentId?: string;
  department?: string; // 添加部门字段
  secondDepartment?: string; // 添加第二志愿部门字段
}

// 报名信息分页响应数据
export interface ApplicationPageData<T> {
  current: number;
  size: number;
  total: number;
  pages: number;
  records: T[];
}


// 奖项类型定义 - 更新为用户提供的新数据结构
interface Award {
  id: number;
  competitionName: string; // 奖项名称
  competitionTrack?:string//赛道
  competitionLevel: string;// 奖项等级
  awardLevel: '一等奖' | '二等奖' | '三等奖' | '优秀奖'; // 获奖等级
  winners: string[]; // 获奖人员数组
  year: number; // 年份
  awardDate: string; // 获奖日期
}

export interface AwardItem extends Award {}



// 获取奖项列表 
export const getAwards = async (
  params: { keyword?: string } = {}
): Promise<AwardItem[]> => {
  try {
    // 从API获取奖项数据
    const response = await api.get<any>(`/public/awards`);
    
    // 检查响应是否存在
    if (!response || !response.data) {
      console.warn('API响应为空');
      return [];
    }
    
    // 根据后端实际返回格式，数据直接在response.data.data中
    let rawAwards: AwardItem[] = [];
    
    // 适配后端返回格式：data直接是奖项数组
    if (Array.isArray(response.data.data)) {
      rawAwards = response.data.data as AwardItem[];
    }
    // 保留向后兼容性：检查是否有awards字段
    else if (response.data.data && Array.isArray(response.data.data.awards)) {
      rawAwards = response.data.data.awards;
    }
    // 备选格式：response.data直接是数组
    else if (Array.isArray(response.data)) {
      rawAwards = response.data as AwardItem[];
    }
    else {
      console.warn('API响应格式不符合预期，返回空数组');
      return [];
    }
    
    let filteredAwards = rawAwards;
    
    // 关键词搜索功能 - 搜索competitionName等字段
    if (params.keyword) {
      const keyword = params.keyword.toLowerCase();
      filteredAwards = rawAwards.filter((award: AwardItem) =>
        award.competitionName?.toLowerCase().includes(keyword) ||
        award.competitionLevel?.toLowerCase().includes(keyword) ||
        award.awardLevel?.toLowerCase().includes(keyword) ||
        award.winners?.some(winner => winner.toLowerCase().includes(keyword)) ||
        award.year?.toString().includes(keyword)
      );
    }
    
    return filteredAwards;
  } catch (error) {
    console.error('获取奖项列表失败:', error);
    // 发生错误时返回空数组，确保组件不会崩溃
    return [];
  }
};

// 创建奖项 - 更新接口参数以匹配新数据结构
export const createAward = async (params: Omit<AwardItem, 'id'>): Promise<AwardItem> => {
  try {
    const response = await api.post<ApiResponse<{id: number}>>(`/admin/awards`, params);
    
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
    await api.put<null>(`/admin/awards/${id}`, params);
    
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
    await api.delete(`/admin/awards/${id}`);
  } catch (error) {
    console.error('删除奖项失败:', error);
    throw error;
  }
};


// 管理员类型定义
export interface Admin {
  id: number;
  username: string;
  password: string; // 应存储加密后的密码
  role: 'super' | 'normal'; // super为超级管理员，normal为普通管理员
  createTime: string;
  updateTime: string;
  token?: string; // 添加可选的token字段
}

// 获取所有管理员列表
export const getAdmins = async (): Promise<Admin[]> => {
  try {
    const response = await api.get<ApiResponse<Admin[]>>(`/admin/admins/page`);
    return response.data.data;
  } catch (error) {
    console.error('获取管理员列表失败:', error);
    throw error;
  }
};

// 添加管理员
export const createAdmin = async (params: Omit<Admin, 'id' | 'createTime' | 'updateTime'>): Promise<Admin> => {
  try {
    const response = await api.post<ApiResponse<Admin>>(`/admin/admins/add`, params);
    return response.data.data;
  } catch (error) {
    console.error('添加管理员失败:', error);
    throw error;
  }
};

// 删除管理员
export const deleteAdmin = async (id: number): Promise<void> => {
  try {
    await api.delete<ApiResponse<null>>(`/admin/admins/${id}`);
  } catch (error) {
    console.error('删除管理员失败:', error);
    throw error;
  }
};

// 更新管理员信息
export const updateAdmin = async (
  id: number,
  params: Partial<Omit<Admin, 'id' | 'createTime' | 'updateTime'>>
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
    const response = await api.get<ApiResponse<Admin>>(`/admin/admins/me`);
    return response.data.data;
  } catch (error) {
    console.error('获取当前管理员信息失败:', error);
    throw error;
  }
};

// 登录
export const login = async (params: { username: string; password: string }): Promise<Admin> => {
  try {
    const response = await api.post<ApiResponse<Admin>>('/public/admins/login', params);
    
    // 验证响应格式
    if (!response.data) {
      throw new Error('登录失败，服务器响应异常');
    }
    
    if (!response.data || !('success' in response.data) || !response.data.success) {
      throw new Error(response.data.message || '登录失败');
    }
    
    // 存储token到本地
    if (response.data.data && response.data.data.token) {
      localStorage.setItem('adminToken', response.data.data.token);
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


// 获取报名信息列表
export const getApplications = async (
  params: GetApplicationsParams = {}
): Promise<ApplicationPageData<any>> => {
  try {
    const response = await api.get<ApiResponse<ApplicationPageData<any>>>(`/public/applications`, {
      params
    });
    
    if (!response.data || !response.data.data) {
      throw new Error('获取报名信息失败');
    }
    
    return response.data.data;
  } catch (error) {
    console.error('获取报名信息失败:', error);
    throw error;
  }
};

// 删除报名信息
export const deleteApplication = async (id: number): Promise<void> => {
  try {
    await api.delete(`/public/applications/${id}`);
  } catch (error) {
    console.error('删除报名信息失败:', error);
    throw error;
  }
};
