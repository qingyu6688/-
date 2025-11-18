import request from '@/utils/request'

// ==================== 资源管理 ====================

/**
 * 分页查询资源列表
 */
export const getResourceList = (params) => {
  return request({
    url: '/service/resource/list',
    method: 'get',
    params
  })
}

/**
 * 获取所有可用资源
 */
export const getAvailableResources = () => {
  return request({
    url: '/service/resource/available',
    method: 'get'
  })
}

/**
 * 获取资源详情
 */
export const getResourceById = (id) => {
  return request({
    url: `/service/resource/${id}`,
    method: 'get'
  })
}

/**
 * 新增资源
 */
export const addResource = (data) => {
  return request({
    url: '/service/resource',
    method: 'post',
    data
  })
}

/**
 * 更新资源
 */
export const updateResource = (data) => {
  return request({
    url: '/service/resource',
    method: 'put',
    data
  })
}

/**
 * 删除资源
 */
export const deleteResource = (id) => {
  return request({
    url: `/service/resource/${id}`,
    method: 'delete'
  })
}

// ==================== 资源预订管理 ====================

/**
 * 分页查询预订列表
 */
export const getBookingList = (params) => {
  return request({
    url: '/service/booking/list',
    method: 'get',
    params
  })
}

/**
 * 获取预订详情
 */
export const getBookingById = (id) => {
  return request({
    url: `/service/booking/${id}`,
    method: 'get'
  })
}

/**
 * 新增预订
 */
export const addBooking = (data) => {
  return request({
    url: '/service/booking',
    method: 'post',
    data
  })
}

/**
 * 更新预订
 */
export const updateBooking = (data) => {
  return request({
    url: '/service/booking',
    method: 'put',
    data
  })
}

/**
 * 审核预订
 */
export const auditBooking = (id, auditStatus, auditorId, auditRemark) => {
  return request({
    url: `/service/booking/${id}/audit`,
    method: 'put',
    params: { auditStatus, auditorId, auditRemark }
  })
}

/**
 * 取消预订
 */
export const cancelBooking = (id, cancelReason) => {
  return request({
    url: `/service/booking/${id}/cancel`,
    method: 'put',
    params: { cancelReason }
  })
}

/**
 * 删除预订
 */
export const deleteBooking = (id) => {
  return request({
    url: `/service/booking/${id}`,
    method: 'delete'
  })
}

/**
 * 检查时间冲突
 */
export const checkConflict = (params) => {
  return request({
    url: '/service/booking/check-conflict',
    method: 'get',
    params
  })
}

// ==================== 统计信息 ====================

/**
 * 获取服务统计信息
 */
export const getStatistics = () => {
  return request({
    url: '/service/statistics',
    method: 'get'
  })
}

// ==================== 财务管理 ====================

/**
 * 分页查询财务记录列表
 */
export const getFinanceList = (params) => {
  return request({
    url: '/service/finance/list',
    method: 'get',
    params
  })
}

/**
 * 新增财务记录
 */
export const addFinanceRecord = (data) => {
  return request({
    url: '/service/finance',
    method: 'post',
    data
  })
}

/**
 * 更新财务记录
 */
export const updateFinanceRecord = (data) => {
  return request({
    url: '/service/finance',
    method: 'put',
    data
  })
}

/**
 * 删除财务记录
 */
export const deleteFinanceRecord = (id) => {
  return request({
    url: `/service/finance/${id}`,
    method: 'delete'
  })
}

/**
 * 审核财务记录
 */
export const auditFinanceRecord = (id, auditStatus, auditorId, auditRemark) => {
  return request({
    url: `/service/finance/${id}/audit`,
    method: 'put',
    params: { auditStatus, auditorId, auditRemark }
  })
}

// ==================== 报销管理 ====================

/**
 * 分页查询报销列表
 */
export const getReimbursementList = (params) => {
  return request({
    url: '/service/reimbursement/list',
    method: 'get',
    params
  })
}

/**
 * 新增报销申请
 */
export const addReimbursement = (data) => {
  return request({
    url: '/service/reimbursement',
    method: 'post',
    data
  })
}

/**
 * 更新报销申请
 */
export const updateReimbursement = (data) => {
  return request({
    url: '/service/reimbursement',
    method: 'put',
    data
  })
}

/**
 * 删除报销申请
 */
export const deleteReimbursement = (id) => {
  return request({
    url: `/service/reimbursement/${id}`,
    method: 'delete'
  })
}

/**
 * 审核报销申请
 */
export const auditReimbursement = (id, auditStatus, auditorId, auditRemark) => {
  return request({
    url: `/service/reimbursement/${id}/audit`,
    method: 'put',
    params: { auditStatus, auditorId, auditRemark }
  })
}

/**
 * 支付报销
 */
export const payReimbursement = (id) => {
  return request({
    url: `/service/reimbursement/${id}/pay`,
    method: 'put'
  })
}

// ==================== 志愿活动管理 ====================

/**
 * 分页查询志愿活动列表
 */
export const getVolunteerActivityList = (params) => {
  return request({
    url: '/service/volunteer/activity/list',
    method: 'get',
    params
  })
}

/**
 * 新增志愿活动
 */
export const addVolunteerActivity = (data) => {
  return request({
    url: '/service/volunteer/activity',
    method: 'post',
    data
  })
}

/**
 * 更新志愿活动
 */
export const updateVolunteerActivity = (data) => {
  return request({
    url: '/service/volunteer/activity',
    method: 'put',
    data
  })
}

/**
 * 删除志愿活动
 */
export const deleteVolunteerActivity = (id) => {
  return request({
    url: `/service/volunteer/activity/${id}`,
    method: 'delete'
  })
}

// ==================== 志愿记录管理 ====================

/**
 * 分页查询志愿记录列表
 */
export const getVolunteerRecordList = (params) => {
  return request({
    url: '/service/volunteer/record/list',
    method: 'get',
    params
  })
}

/**
 * 新增志愿记录
 */
export const addVolunteerRecord = (data) => {
  return request({
    url: '/service/volunteer/record',
    method: 'post',
    data
  })
}

/**
 * 确认志愿记录
 */
export const confirmVolunteerRecord = (id, confirmerId) => {
  return request({
    url: `/service/volunteer/record/${id}/confirm`,
    method: 'put',
    params: { confirmerId }
  })
}

/**
 * 删除志愿记录
 */
export const deleteVolunteerRecord = (id) => {
  return request({
    url: `/service/volunteer/record/${id}`,
    method: 'delete'
  })
}
