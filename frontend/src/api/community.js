import request from '@/utils/request'

// ==================== 聊天管理 ====================

/**
 * 获取聊天统计数据
 */
export const getChatStatistics = () => {
  return request({
    url: '/communityMessage/admin/statistics',
    method: 'get'
  })
}

/**
 * 获取聊天消息列表
 */
export const getChatMessageList = (params) => {
  return request({
    url: '/communityMessage/admin/list',
    method: 'get',
    params
  })
}

/**
 * 删除聊天消息
 */
export const deleteChatMessage = (id) => {
  return request({
    url: `/communityMessage/admin/${id}`,
    method: 'delete'
  })
}

// ==================== 帖子管理 ====================

/**
 * 分页查询帖子列表
 */
export const getPostList = (params) => {
  return request({
    url: '/communityPost/list',
    method: 'get',
    params
  })
}

/**
 * 获取帖子详情
 */
export const getPostById = (id) => {
  return request({
    url: `/community/post/${id}`,
    method: 'get'
  })
}

/**
 * 新增帖子
 */
export const addPost = (data) => {
  return request({
    url: '/communityPost',
    method: 'post',
    data
  })
}

/**
 * 更新帖子
 */
export const updatePost = (data) => {
  return request({
    url: '/community/post',
    method: 'put',
    data
  })
}

/**
 * 删除帖子
 */
export const deletePost = (id) => {
  return request({
    url: `/community/post/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除帖子
 */
export const deletePostBatch = (ids) => {
  return request({
    url: '/community/post/batch',
    method: 'delete',
    data: ids
  })
}

/**
 * 审核帖子
 */
export const auditPost = (id, auditStatus) => {
  return request({
    url: `/community/post/${id}/audit`,
    method: 'put',
    params: { auditStatus }
  })
}

/**
 * 设置帖子置顶
 */
export const setPostTop = (id, isTop) => {
  return request({
    url: `/community/post/${id}/top`,
    method: 'put',
    params: { isTop }
  })
}

/**
 * 设置帖子精华
 */
export const setPostEssence = (id, isEssence) => {
  return request({
    url: `/community/post/${id}/essence`,
    method: 'put',
    params: { isEssence }
  })
}

// ==================== 评论管理 ====================

/**
 * 分页查询评论列表
 */
export const getCommentList = (params) => {
  return request({
    url: '/communityComment/list',
    method: 'get',
    params
  })
}

/**
 * 获取帖子的评论列表
 */
export const getCommentsByPostId = (postId) => {
  return request({
    url: `/community/comment/post/${postId}`,
    method: 'get'
  })
}

/**
 * 新增评论
 */
export const addComment = (data) => {
  return request({
    url: '/community/comment',
    method: 'post',
    data
  })
}

/**
 * 删除评论
 */
export const deleteComment = (id) => {
  return request({
    url: `/community/comment/${id}`,
    method: 'delete'
  })
}

// ==================== 板块管理 ====================

/**
 * 获取所有板块
 */
export const getCategoryList = (params) => {
  return request({
    url: '/community/category/list',
    method: 'get',
    params
  })
}

/**
 * 新增板块
 */
export const addCategory = (data) => {
  return request({
    url: '/community/category',
    method: 'post',
    data
  })
}

/**
 * 更新板块
 */
export const updateCategory = (data) => {
  return request({
    url: '/community/category',
    method: 'put',
    data
  })
}

/**
 * 删除板块
 */
export const deleteCategory = (id) => {
  return request({
    url: `/community/category/${id}`,
    method: 'delete'
  })
}

// ==================== 通知管理 ====================

/**
 * 分页查询通知列表
 */
export const getNoticeList = (params) => {
  return request({
    url: '/communityNotice/page',
    method: 'get',
    params
  })
}

/**
 * 新增通知
 */
export const addNotice = (data) => {
  return request({
    url: '/communityNotice',
    method: 'post',
    data
  })
}

/**
 * 更新通知
 */
export const updateNotice = (data) => {
  return request({
    url: '/communityNotice',
    method: 'put',
    data
  })
}

/**
 * 删除通知
 */
export const deleteNotice = (id) => {
  return request({
    url: `/communityNotice/${id}`,
    method: 'delete'
  })
}

/**
 * 置顶/取消置顶通知
 */
export const toggleNoticeTop = (id, isTop) => {
  return request({
    url: `/communityNotice/${id}/top`,
    method: 'put',
    params: { isTop }
  })
}

/**
 * 撤回通知
 */
export const withdrawNotice = (id) => {
  return request({
    url: `/communityNotice/${id}/withdraw`,
    method: 'put'
  })
}

/**
 * 获取通知阅读统计
 */
export const getNoticeStatistics = (id) => {
  return request({
    url: `/communityNotice/${id}/statistics`,
    method: 'get'
  })
}

/**
 * 获取未读通知数
 */
export const getUnreadNoticeCount = (userId) => {
  return request({
    url: '/communityNotice/unread/count',
    method: 'get',
    params: { userId }
  })
}

/**
 * 获取前台通知列表
 */
export const getFrontNoticeList = (params) => {
  return request({
    url: '/communityNotice/list',
    method: 'get',
    params
  })
}

/**
 * 标记通知为已读
 */
export const markNoticeAsRead = (noticeId, userId) => {
  return request({
    url: `/communityNotice/${noticeId}/read`,
    method: 'post',
    params: { userId }
  })
}

// ==================== 统计信息 ====================

/**
 * 获取社区统计信息
 */
export const getStatistics = () => {
  return request({
    url: '/community/statistics',
    method: 'get'
  })
}

// ==================== 消息通知 ====================

/**
 * 获取消息列表
 */
export const getMessageList = (params) => {
  return request({
    url: '/communityMessage/list',
    method: 'get',
    params
  })
}

/**
 * 获取未读消息数量
 */
export const getUnreadCount = (userId) => {
  return request({
    url: '/communityMessage/unreadCount',
    method: 'get',
    params: { userId }
  })
}

/**
 * 标记消息为已读
 */
export const markAsRead = (id) => {
  return request({
    url: `/communityMessage/${id}/read`,
    method: 'put'
  })
}

/**
 * 标记所有消息为已读
 */
export const markAllAsRead = (userId) => {
  return request({
    url: '/communityMessage/readAll',
    method: 'put',
    params: { userId }
  })
}

/**
 * 删除消息
 */
export const deleteMessage = (id) => {
  return request({
    url: `/communityMessage/${id}`,
    method: 'delete'
  })
}

/**
 * 获取消息统计
 */
export const getMessageStatistics = (userId) => {
  return request({
    url: '/communityMessage/statistics',
    method: 'get',
    params: { userId }
  })
}

/**
 * 发送私信
 */
export const sendChat = (data) => {
  return request({
    url: '/communityMessage/sendChat',
    method: 'post',
    data
  })
}

/**
 * 获取聊天列表
 */
export const getChatList = (userId) => {
  return request({
    url: '/communityMessage/chatList',
    method: 'get',
    params: { userId }
  })
}

/**
 * 获取聊天记录
 */
export const getChatHistory = (params) => {
  return request({
    url: '/communityMessage/chatHistory',
    method: 'get',
    params
  })
}

/**
 * 获取未读私信数
 */
export const getUnreadChatCount = (userId) => {
  return request({
    url: '/communityMessage/unreadChatCount',
    method: 'get',
    params: { userId }
  })
}

/**
 * 撤回消息
 */
export const withdrawMessage = (id, userId) => {
  return request({
    url: `/communityMessage/${id}/withdraw`,
    method: 'put',
    params: { userId }
  })
}

/**
 * 发送图片消息
 */
export const sendImage = (data) => {
  return request({
    url: '/communityMessage/sendImage',
    method: 'post',
    data
  })
}

/**
 * 发送文件消息
 */
export const sendFile = (data) => {
  return request({
    url: '/communityMessage/sendFile',
    method: 'post',
    data
  })
}
