import axios from 'axios'
import { ElMessage, ElNotification } from 'element-plus'
import { useUserStore } from '@/stores/user'
import router from '@/router'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers['Authorization'] = `Bearer ${userStore.token}`
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    ElMessage.error('请求发送失败')
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    
    // 成功响应
    if (res.code === 200) {
      return res
    }
    
    // 业务错误处理
    handleBusinessError(res.code, res.message)
    return Promise.reject(new Error(res.message || '请求失败'))
  },
  error => {
    console.error('响应错误:', error)
    
    // 网络错误或服务器错误
    if (error.response) {
      handleHttpError(error.response)
    } else if (error.request) {
      // 请求已发送但没有收到响应
      ElNotification({
        title: '网络错误',
        message: '服务器无响应，请检查网络连接',
        type: 'error',
        duration: 3000
      })
    } else {
      // 请求配置出错
      ElMessage.error(error.message || '请求配置错误')
    }
    
    return Promise.reject(error)
  }
)

/**
 * 处理业务错误
 */
function handleBusinessError(code, message) {
  const errorMap = {
    400: { title: '请求错误', type: 'warning' },
    401: { title: '未授权', type: 'error' },
    403: { title: '权限不足', type: 'error' },
    404: { title: '资源不存在', type: 'warning' },
    500: { title: '服务器错误', type: 'error' }
  }
  
  const errorInfo = errorMap[code] || { title: '错误', type: 'error' }
  
  // 401 特殊处理：跳转登录
  if (code === 401) {
    const userStore = useUserStore()
    userStore.logout()
    router.push('/login')
    ElNotification({
      title: errorInfo.title,
      message: message || '登录已过期，请重新登录',
      type: errorInfo.type,
      duration: 3000
    })
    return
  }
  
  // 其他错误使用通知
  ElNotification({
    title: errorInfo.title,
    message: message || '操作失败',
    type: errorInfo.type,
    duration: 3000
  })
}

/**
 * 处理HTTP错误
 */
function handleHttpError(response) {
  const status = response.status
  const data = response.data
  
  switch (status) {
    case 400:
      ElNotification({
        title: '请求错误',
        message: data.message || '请求参数错误',
        type: 'warning',
        duration: 3000
      })
      break
    case 401:
      const userStore = useUserStore()
      userStore.logout()
      router.push('/login')
      ElNotification({
        title: '未授权',
        message: '登录已过期，请重新登录',
        type: 'error',
        duration: 3000
      })
      break
    case 403:
      ElNotification({
        title: '权限不足',
        message: data.message || '您没有权限执行此操作',
        type: 'error',
        duration: 3000
      })
      break
    case 404:
      ElNotification({
        title: '资源不存在',
        message: data.message || '请求的资源不存在',
        type: 'warning',
        duration: 3000
      })
      break
    case 500:
      ElNotification({
        title: '服务器错误',
        message: data.message || '服务器内部错误，请稍后重试',
        type: 'error',
        duration: 4000
      })
      break
    case 502:
      ElNotification({
        title: '网关错误',
        message: '网关错误，请稍后重试',
        type: 'error',
        duration: 3000
      })
      break
    case 503:
      ElNotification({
        title: '服务不可用',
        message: '服务暂时不可用，请稍后重试',
        type: 'error',
        duration: 3000
      })
      break
    case 504:
      ElNotification({
        title: '请求超时',
        message: '请求超时，请稍后重试',
        type: 'error',
        duration: 3000
      })
      break
    default:
      ElNotification({
        title: '错误',
        message: data.message || `请求失败 (${status})`,
        type: 'error',
        duration: 3000
      })
  }
}

export default request
