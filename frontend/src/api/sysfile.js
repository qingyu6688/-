import request from '@/utils/request'

/**
 * 分页查询文件列表
 */
export const getFileList = (params) => {
  return request({
    url: '/sysfile/list',
    method: 'get',
    params
  })
}

/**
 * 获取文件统计信息
 */
export const getFileStatistics = () => {
  return request({
    url: '/sysfile/statistics',
    method: 'get'
  })
}

/**
 * 删除文件
 */
export const deleteFile = (id) => {
  return request({
    url: `/sysfile/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除文件
 */
export const batchDeleteFiles = (ids) => {
  return request({
    url: '/sysfile/batch',
    method: 'delete',
    data: ids
  })
}
