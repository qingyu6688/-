import request from '@/utils/request'

// ==================== 联系方式管理 ====================

/**
 * 获取校友联系方式（根据用户ID）
 */
export const getContactByUserId = (userId) => {
  return request({
    url: `/alumni/contact/${userId}`,
    method: 'get'
  })
}

/**
 * 分页查询联系方式列表
 */
export const getContactList = (params) => {
  return request({
    url: '/alumni/contact/list',
    method: 'get',
    params
  })
}

/**
 * 保存或更新联系方式
 */
export const saveContact = (data) => {
  return request({
    url: '/alumni/contact',
    method: 'post',
    data
  })
}

// ==================== 社团经历管理 ====================

/**
 * 获取社团经历列表（根据用户ID）
 */
export const getClubHistoryByUserId = (userId) => {
  return request({
    url: `/alumni/club-history/${userId}`,
    method: 'get'
  })
}

/**
 * 分页查询社团经历列表
 */
export const getClubHistoryList = (params) => {
  return request({
    url: '/alumni/club-history/list',
    method: 'get',
    params
  })
}

/**
 * 新增社团经历
 */
export const addClubHistory = (data) => {
  return request({
    url: '/alumni/club-history',
    method: 'post',
    data
  })
}

/**
 * 更新社团经历
 */
export const updateClubHistory = (data) => {
  return request({
    url: '/alumni/club-history',
    method: 'put',
    data
  })
}

/**
 * 删除社团经历
 */
export const deleteClubHistory = (id) => {
  return request({
    url: `/alumni/club-history/${id}`,
    method: 'delete'
  })
}

// ==================== 荣誉记录管理 ====================

/**
 * 获取荣誉记录列表（根据用户ID）
 */
export const getHonorByUserId = (userId) => {
  return request({
    url: `/alumni/honor/${userId}`,
    method: 'get'
  })
}

/**
 * 分页查询荣誉记录列表
 */
export const getHonorList = (params) => {
  return request({
    url: '/alumni/honor/list',
    method: 'get',
    params
  })
}

/**
 * 新增荣誉记录
 */
export const addHonor = (data) => {
  return request({
    url: '/alumni/honor',
    method: 'post',
    data
  })
}

/**
 * 更新荣誉记录
 */
export const updateHonor = (data) => {
  return request({
    url: '/alumni/honor',
    method: 'put',
    data
  })
}

/**
 * 审核荣誉记录
 */
export const auditHonor = (id, auditStatus, auditor) => {
  return request({
    url: `/alumni/honor/${id}/audit`,
    method: 'put',
    params: {
      auditStatus,
      auditor
    }
  })
}

/**
 * 删除荣誉记录
 */
export const deleteHonor = (id) => {
  return request({
    url: `/alumni/honor/${id}`,
    method: 'delete'
  })
}

// ==================== 贡献度管理 ====================

/**
 * 获取贡献度记录（根据用户ID）
 */
export const getContributionByUserId = (userId) => {
  return request({
    url: `/alumni/contribution/${userId}`,
    method: 'get'
  })
}

/**
 * 获取总贡献值
 */
export const getTotalContribution = (userId) => {
  return request({
    url: `/alumni/contribution/${userId}/total`,
    method: 'get'
  })
}

/**
 * 分页查询贡献度记录
 */
export const getContributionList = (params) => {
  return request({
    url: '/alumni/contribution/list',
    method: 'get',
    params
  })
}

/**
 * 新增贡献度记录
 */
export const addContribution = (data) => {
  return request({
    url: '/alumni/contribution',
    method: 'post',
    data
  })
}

/**
 * 批量删除贡献度记录
 */
export const deleteContributionBatch = (ids) => {
  return request({
    url: '/alumni/contribution/batch',
    method: 'delete',
    data: ids
  })
}

// ==================== 统计分析 ====================

/**
 * 获取校友统计信息
 */
export const getAlumniStatistics = (userId) => {
  return request({
    url: `/alumni/statistics/${userId}`,
    method: 'get'
  })
}
