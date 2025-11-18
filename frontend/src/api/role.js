import request from '@/utils/request'

/**
 * 分页查询角色列表
 */
export const getRoleList = (params) => {
  return request({
    url: '/role/list',
    method: 'get',
    params
  })
}

/**
 * 查询所有角色
 */
export const getAllRoles = () => {
  return request({
    url: '/role/all',
    method: 'get'
  })
}

/**
 * 新增角色
 */
export const addRole = (data) => {
  return request({
    url: '/role',
    method: 'post',
    data
  })
}

/**
 * 修改角色
 */
export const updateRole = (data) => {
  return request({
    url: '/role',
    method: 'put',
    data
  })
}

/**
 * 删除角色
 */
export const deleteRole = (id) => {
  return request({
    url: `/role/${id}`,
    method: 'delete'
  })
}

/**
 * 为角色分配菜单权限
 */
export const assignMenus = (data) => {
  return request({
    url: '/role/assignMenus',
    method: 'post',
    data
  })
}

/**
 * 查询角色已分配的菜单ID列表
 */
export const getMenuIdsByRoleId = (roleId) => {
  return request({
    url: `/role/menuIds/${roleId}`,
    method: 'get'
  })
}
