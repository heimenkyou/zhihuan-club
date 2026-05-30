import { ref } from 'vue'
import { defineStore } from 'pinia'
import type { CodeSubmission } from '@/services/codeSubmissionService'

export const useCodeSubmissionStore = defineStore('codeSubmission', () => {
  // 代码提交记录列表
  const submissionList = ref<CodeSubmission[]>([])
  
  // 加载状态
  const isLoading = ref(false)
  
  // 错误信息
  const error = ref<string | null>(null)
  
  // 设置提交记录列表
  const setSubmissions = (submissions: CodeSubmission[]) => {
    submissionList.value = submissions
  }
  
  // 添加单条提交记录
  const addSubmission = (submission: CodeSubmission) => {
    submissionList.value.unshift(submission)
  }
  
  // 更新提交记录
  const updateSubmission = (updatedSubmission: CodeSubmission) => {
    const index = submissionList.value.findIndex(
      item => item.id === updatedSubmission.id
    )
    if (index !== -1) {
      submissionList.value[index] = updatedSubmission
    }
  }
  
  // 删除提交记录
  const removeSubmission = (id: number) => {
    submissionList.value = submissionList.value.filter(item => item.id !== id)
  }
  
  // 设置加载状态
  const setLoading = (loading: boolean) => {
    isLoading.value = loading
  }
  
  // 设置错误信息
  const setError = (err: string | null) => {
    error.value = err
  }
  
  // 清空错误信息
  const clearError = () => {
    error.value = null
  }
  
  // 清空提交记录
  const clearSubmissions = () => {
    submissionList.value = []
  }
  
  // 查找提交记录
  const findSubmissionById = (id: number): CodeSubmission | undefined => {
    return submissionList.value.find(item => item.id === id)
  }
  
  // 查找提交记录（按学号）
  const findSubmissionByStudentId = (studentId: string): CodeSubmission | undefined => {
    return submissionList.value.find(item => item.studentId === studentId)
  }
  
  return {
    submissionList,
    isLoading,
    error,
    setSubmissions,
    addSubmission,
    updateSubmission,
    removeSubmission,
    setLoading,
    setError,
    clearError,
    clearSubmissions,
    findSubmissionById,
    findSubmissionByStudentId
  }
})