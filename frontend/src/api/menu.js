import request from '@/utils/request'

/**
 * 查询菜单树
 */
export const getMenuTree = () => {
  return request({
    url: '/menu/tree',
    method: 'get'
  })
}

/**
 * 获取当前用户的权限标识
 */
export const getUserPermissions = () => {
  return request({
    url: '/menu/permissions',
    method: 'get'
  })
}

/**
 * 根据角色ID查询菜单树
 */
export const getMenuTreeByRoleId = (roleId) => {
  return request({
    url: `/menu/tree/${roleId}`,
    method: 'get'
  })
}

/**
 * 新增菜单
 */
export const addMenu = (data) => {
  return request({
    url: '/menu',
    method: 'post',
    data
  })
}

/**
 * 修改菜单
 */
export const updateMenu = (data) => {
  return request({
    url: '/menu',
    method: 'put',
    data
  })
}

/**
 * 删除菜单
 */
export const deleteMenu = (id) => {
  return request({
    url: `/menu/${id}`,
    method: 'delete'
  })
}
