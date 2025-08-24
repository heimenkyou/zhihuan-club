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
      '/admin/applications',
      { params }
    );
    return response.data;
  } catch (error) {
    console.error('获取报名信息失败:', error);
    throw error;
  }
};

// 删除报名信息
export const deleteApplication = async (id: number): Promise<void> => {
  try {
    await api.delete<ApiResponse<null>>(`/admin/applications/${id}`);
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
): Promise<ApplicationPageData<AwardItem>> => {
  try {
    // 从json-server获取原始奖项数据
    const response = await api.get<AwardItem[]>(`/awards`);
    const rawAwards = response;
    
    // 关键词搜索过滤
    let filteredAwards = rawAwards;
    if (params.keyword) {
      const keyword = params.keyword.toLowerCase();
      filteredAwards = rawAwards.filter(award => 
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
    const response = await api.post<{id: number}>(`/awards`, params);
    
    // 返回完整的奖项对象
    return {
      id: response.id,
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
