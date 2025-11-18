import request from '@/utils/request'

/**
 * 分页查询操作日志
 */
export const getOperationLogList = (params) => {
  return request({
    url: '/log/operation/list',
    method: 'get',
    params
  })
}

/**
 * 删除操作日志
 */
export const deleteOperationLog = (id) => {
  return request({
    url: `/log/operation/${id}`,
    method: 'delete'
  })
}

/**
 * 清空操作日志
 */
export const cleanOperationLog = () => {
  return request({
    url: '/log/operation/clean',
    method: 'delete'
  })
}

/**
 * 分页查询登录日志
 */
export const getLoginLogList = (params) => {
  return request({
    url: '/log/login/list',
    method: 'get',
    params
  })
}

/**
 * 删除登录日志
 */
export const deleteLoginLog = (id) => {
  return request({
    url: `/log/login/${id}`,
    method: 'delete'
  })
}

/**
 * 清空登录日志
 */
export const cleanLoginLog = () => {
  return request({
    url: '/log/login/clean',
    method: 'delete'
  })
}
