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

  return {
    submissionList,
    isLoading,
    error,
    setSubmissions,
    removeSubmission,
    setLoading,
    setError,
    clearError,
  }
})
