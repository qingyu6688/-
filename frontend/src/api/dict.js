import request from '@/utils/request'

// ==================== 字典类型 ====================

/**
 * 分页查询字典类型
 */
export const getDictTypeList = (params) => {
  return request({
    url: '/dict/type/list',
    method: 'get',
    params
  })
}

/**
 * 查询所有字典类型
 */
export const getAllDictTypes = () => {
  return request({
    url: '/dict/type/all',
    method: 'get'
  })
}

/**
 * 新增字典类型
 */
export const addDictType = (data) => {
  return request({
    url: '/dict/type',
    method: 'post',
    data
  })
}

/**
 * 修改字典类型
 */
export const updateDictType = (data) => {
  return request({
    url: '/dict/type',
    method: 'put',
    data
  })
}

/**
 * 删除字典类型
 */
export const deleteDictType = (id) => {
  return request({
    url: `/dict/type/${id}`,
    method: 'delete'
  })
}

// ==================== 字典数据 ====================

/**
 * 分页查询字典数据
 */
export const getDictDataList = (params) => {
  return request({
    url: '/dict/data/list',
    method: 'get',
    params
  })
}

/**
 * 根据字典类型查询字典数据
 */
export const getDictDataByType = (dictType) => {
  return request({
    url: `/dict/data/type/${dictType}`,
    method: 'get'
  })
}

/**
 * 新增字典数据
 */
export const addDictData = (data) => {
  return request({
    url: '/dict/data',
    method: 'post',
    data
  })
}

/**
 * 修改字典数据
 */
export const updateDictData = (data) => {
  return request({
    url: '/dict/data',
    method: 'put',
    data
  })
}

/**
 * 删除字典数据
 */
export const deleteDictData = (id) => {
  return request({
    url: `/dict/data/${id}`,
    method: 'delete'
  })
}
