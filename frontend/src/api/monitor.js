import request from '@/utils/request'

/**
 * 获取服务器信息
 */
export const getServerInfo = () => {
  return request({
    url: '/monitor/server',
    method: 'get'
  })
}

/**
 * 获取系统概览
 */
export const getOverview = () => {
  return request({
    url: '/monitor/overview',
    method: 'get'
  })
}
