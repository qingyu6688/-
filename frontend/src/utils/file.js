/**
 * 获取完整的文件URL
 * @param {string} path - 文件路径
 * @returns {string} 完整的URL
 */
export const getFileUrl = (path) => {
  // 检查参数是否为空或不是字符串
  if (!path || typeof path !== 'string') return ''
  
  // 如果已经是完整URL，直接返回
  if (path.startsWith('http://') || path.startsWith('https://')) {
    return path
  }
  
  // 如果是相对路径，拼接后端地址
  const baseURL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
  
  // 确保路径以 / 开头
  const filePath = path.startsWith('/') ? path : `/${path}`
  
  return `${baseURL}${filePath}`
}

/**
 * 获取默认头像
 * @returns {string} 默认头像URL
 */
export const getDefaultAvatar = () => {
  return ''
}
