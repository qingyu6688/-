import { useUserStore } from '@/stores/user'

/**
 * 按钮权限指令
 * 使用方式: v-permission="['system:user:add']"
 */
export const permission = {
  mounted(el, binding) {
    const { value } = binding
    const userStore = useUserStore()
    const permissions = userStore.permissions || []

    // 如果没有配置权限数据，则不进行权限控制（开发阶段）
    if (permissions.length === 0) {
      return
    }

    if (value && value instanceof Array && value.length > 0) {
      const requiredPermissions = value
      const hasPermission = permissions.some(permission => {
        return requiredPermissions.includes(permission)
      })

      if (!hasPermission) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    } else {
      throw new Error('需要权限标识! 如: v-permission="[\'system:user:add\']"')
    }
  }
}

/**
 * 角色权限指令
 * 使用方式: v-role="['admin']"
 */
export const role = {
  mounted(el, binding) {
    const { value } = binding
    const userStore = useUserStore()
    const userRole = userStore.userInfo.roleKey

    if (value && value instanceof Array && value.length > 0) {
      const requiredRoles = value
      const hasRole = requiredRoles.includes(userRole)

      if (!hasRole) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    } else {
      throw new Error('需要角色标识! 如: v-role="[\'admin\']"')
    }
  }
}

export default {
  permission,
  role
}
