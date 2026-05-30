import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useCodeSubmissionStore = defineStore('codeSubmission', () => {
  const submissionList = ref([])
  const isLoading = ref(false)
  const error = ref(null)

  /**
   * 批量覆盖记录，保证列表来源单一。
   */
  const setSubmissions = submissions => {
    submissionList.value = submissions
  }

  /**
   * 新提交默认插到顶部，保持列表时序一致。
   */
  const addSubmission = submission => {
    submissionList.value.unshift(submission)
  }

  /**
   * 原地替换记录，避免打断依赖该数组的响应式引用。
   */
  const updateSubmission = updatedSubmission => {
    const index = submissionList.value.findIndex(
      item => item.id === updatedSubmission.id
    )
    if (index !== -1) {
      submissionList.value[index] = updatedSubmission
    }
  }

  const removeSubmission = id => {
    submissionList.value = submissionList.value.filter(item => item.id !== id)
  }

  const setLoading = loading => {
    isLoading.value = loading
  }

  const setError = err => {
    error.value = err
  }

  const clearError = () => {
    error.value = null
  }

  const clearSubmissions = () => {
    submissionList.value = []
  }

  const findSubmissionById = id => {
    return submissionList.value.find(item => item.id === id)
  }

  const findSubmissionByStudentId = studentId => {
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
    findSubmissionByStudentId,
  }
})
