import request from '@/utils/request'

/**
 * 获取个人信息
 */
export const getProfile = () => {
  return request({
    url: '/profile/info',
    method: 'get'
  })
}

/**
 * 修改密码
 */
export const updatePassword = (data) => {
  return request({
    url: '/profile/password',
    method: 'post',
    data
  })
}

/**
 * 更新个人资料
 */
export const updateProfile = (data) => {
  return request({
    url: '/profile/info',
    method: 'put',
    data
  })
}

/**
 * 获取个人操作记录
 */
export const getOperations = (params) => {
  return request({
    url: '/profile/operations',
    method: 'get',
    params
  })
}

/**
 * 获取用户统计数据
 */
export const getUserStats = (userId) => {
  return request({
    url: `/profile/stats/${userId}`,
    method: 'get'
  })
}
