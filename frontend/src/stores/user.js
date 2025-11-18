import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))
  const permissions = ref(JSON.parse(localStorage.getItem('permissions') || '[]'))

  const setToken = (newToken) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  const setUserInfo = (info) => {
    userInfo.value = info
    localStorage.setItem('userInfo', JSON.stringify(info))
  }

  const setPermissions = (perms) => {
    permissions.value = perms
    localStorage.setItem('permissions', JSON.stringify(perms))
  }

  const logout = () => {
    token.value = ''
    userInfo.value = {}
    permissions.value = []
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    localStorage.removeItem('permissions')
  }

  return {
    token,
    userInfo,
    permissions,
    setToken,
    setUserInfo,
    setPermissions,
    logout
  }
})
