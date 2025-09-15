import { ElNotification } from 'element-plus'

// 提示类型
type NotificationType = 'success' | 'warning' | 'error' | 'info'

// 通用提示函数
export const showNotification = (
  type: NotificationType,
  message: string,
  title?: string,
  duration: number = type === 'error' ? 3000 : 2000
) => {
  ElNotification({
    title:
      title ||
      (type === 'success'
        ? '操作成功'
        : type === 'error'
        ? '错误'
        : type === 'warning'
        ? '提示'
        : '信息'),
    message,
    type,
    duration,
    position: 'top-right',
    // 可选：添加自定义类名做样式控制
    customClass: 'app-notification',
  })
}

// 快捷方法
// 成功提示
export const showSuccess = (message: string) => {
  showNotification('success', message, '操作成功', 2000)
}

// 错误提示
export const showError = (message: string) => {
  showNotification('error', message, '错误', 3000)
}

// 警告提示
export const showWarning = (message: string) => {
  showNotification('warning', message, '提示', 2000)
}

// 信息提示
export const showInfo = (message: string) => {
  showNotification('info', message, '信息', 2000)
}
