import request from '@/utils/request'

/**
 * 获取数据概览
 */
export const getOverview = () => {
  return request({
    url: '/statistics/overview',
    method: 'get'
  })
}

/**
 * 获取活动分析数据
 */
export const getActivityAnalysis = () => {
  return request({
    url: '/statistics/activity-analysis',
    method: 'get'
  })
}

/**
 * 获取财务分析数据
 */
export const getFinanceAnalysis = () => {
  return request({
    url: '/statistics/finance-analysis',
    method: 'get'
  })
}

/**
 * 获取成员分析数据
 */
export const getMemberAnalysis = () => {
  return request({
    url: '/statistics/member-analysis',
    method: 'get'
  })
}

/**
 * 获取资源使用分析
 */
export const getResourceAnalysis = () => {
  return request({
    url: '/statistics/resource-analysis',
    method: 'get'
  })
}

/**
 * 获取志愿服务分析
 */
export const getVolunteerAnalysis = () => {
  return request({
    url: '/statistics/volunteer-analysis',
    method: 'get'
  })
}

/**
 * 获取综合统计数据（用于首页）
 */
export const getDashboard = () => {
  return request({
    url: '/statistics/dashboard',
    method: 'get'
  })
}
