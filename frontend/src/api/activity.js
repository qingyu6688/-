import request from '@/utils/request'

// ==================== 活动信息管理 ====================

/**
 * 分页查询活动列表
 */
export const getActivityList = (params) => {
  return request({
    url: '/activity/info/list',
    method: 'get',
    params
  })
}

/**
 * 获取活动详情
 */
export const getActivityById = (id) => {
  return request({
    url: `/activity/info/${id}`,
    method: 'get'
  })
}

/**
 * 新增活动
 */
export const addActivity = (data) => {
  return request({
    url: '/activity/info',
    method: 'post',
    data
  })
}

/**
 * 更新活动
 */
export const updateActivity = (data) => {
  return request({
    url: '/activity/info',
    method: 'put',
    data
  })
}

/**
 * 删除活动
 */
export const deleteActivity = (id) => {
  return request({
    url: `/activity/info/${id}`,
    method: 'delete'
  })
}

/**
 * 审核活动
 */
export const auditActivity = (id, auditStatus) => {
  return request({
    url: `/activity/info/${id}/audit`,
    method: 'put',
    params: { auditStatus }
  })
}

/**
 * 取消活动
 */
export const cancelActivity = (id) => {
  return request({
    url: `/activity/info/${id}/cancel`,
    method: 'put'
  })
}

// ==================== 报名管理 ====================

/**
 * 分页查询报名列表
 */
export const getRegistrationList = (params) => {
  return request({
    url: '/activity/registration/list',
    method: 'get',
    params
  })
}

/**
 * 新增报名
 */
export const addRegistration = (data) => {
  return request({
    url: '/activity/registration',
    method: 'post',
    data
  })
}

/**
 * 审核报名
 */
export const auditRegistration = (id, auditStatus) => {
  return request({
    url: `/activity/registration/${id}/audit`,
    method: 'put',
    params: { auditStatus }
  })
}

/**
 * 取消报名
 */
export const cancelRegistration = (id) => {
  return request({
    url: `/activity/registration/${id}`,
    method: 'delete'
  })
}

// ==================== 签到管理 ====================

/**
 * 分页查询签到列表
 */
export const getSignInList = (params) => {
  return request({
    url: '/activity/signin/list',
    method: 'get',
    params
  })
}

/**
 * 获取活动签到统计
 */
export const getSignInStatistics = (activityId) => {
  return request({
    url: `/activity/signin/statistics/${activityId}`,
    method: 'get'
  })
}

// ==================== 评价管理 ====================

/**
 * 分页查询评价列表
 */
export const getEvaluationList = (params) => {
  return request({
    url: '/activity/evaluation/list',
    method: 'get',
    params
  })
}

/**
 * 删除评价
 */
export const deleteEvaluation = (id) => {
  return request({
    url: `/activity/evaluation/${id}`,
    method: 'delete'
  })
}

/**
 * 获取活动评价统计
 */
export const getEvaluationStatistics = (activityId) => {
  return request({
    url: `/activity/evaluation/statistics/${activityId}`,
    method: 'get'
  })
}

// ==================== 相册管理 ====================

/**
 * 分页查询相册列表
 */
export const getPhotoList = (params) => {
  return request({
    url: '/activity/photo/list',
    method: 'get',
    params
  })
}

/**
 * 批量上传活动照片
 */
export const uploadPhotos = (activityId, uploaderId, files) => {
  const formData = new FormData()
  formData.append('activityId', activityId)
  formData.append('uploaderId', uploaderId)
  files.forEach(file => {
    formData.append('files', file)
  })
  
  return request({
    url: '/activity/photo/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 删除照片
 */
export const deletePhoto = (id) => {
  return request({
    url: `/activity/photo/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除照片
 */
export const deletePhotoBatch = (ids) => {
  return request({
    url: '/activity/photo/batch',
    method: 'delete',
    data: ids
  })
}

/**
 * 设置照片精选状态
 */
export const setPhotoFeature = (id, isFeatured) => {
  return request({
    url: `/activity/photo/feature/${id}`,
    method: 'put',
    params: { isFeatured }
  })
}

// ==================== 统计信息 ====================

/**
 * 获取活动统计信息
 */
export const getStatistics = () => {
  return request({
    url: '/activity/statistics',
    method: 'get'
  })
}
