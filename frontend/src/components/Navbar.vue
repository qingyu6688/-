<template>
  <div class="navbar">
    <div class="navbar-left">
      <el-icon class="hamburger" :size="24" @click="toggleSidebar">
        <component :is="isCollapse ? 'Expand' : 'Fold'" />
      </el-icon>
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/admin/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item v-if="currentRoute && currentRoute.path !== '/admin/home'">
          {{ currentRoute.meta?.title }}
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="navbar-right">
      <el-tooltip content="刷新" placement="bottom">
        <el-icon class="navbar-icon" :size="20" @click="handleRefresh">
          <Refresh />
        </el-icon>
      </el-tooltip>
      <el-tooltip content="全屏" placement="bottom">
        <el-icon class="navbar-icon" :size="20" @click="handleFullscreen">
          <FullScreen />
        </el-icon>
      </el-tooltip>
      <el-dropdown @command="handleCommand" class="user-dropdown">
        <span class="user-info">
          <el-avatar :size="32" :src="getFileUrl(userStore.userInfo.avatar)">
            <el-icon><User /></el-icon>
          </el-avatar>
          <span class="username">{{ userStore.userInfo.nickname || userStore.userInfo.username }}</span>
          <el-icon class="el-icon--right"><ArrowDown /></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">
              <el-icon><User /></el-icon>
              个人中心
            </el-dropdown-item>
            <el-dropdown-item command="frontend">
              <el-icon><Monitor /></el-icon>
              前往前台
            </el-dropdown-item>
            <el-dropdown-item divided command="logout">
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { getFileUrl } from '@/utils/file'

const emit = defineEmits(['toggle-sidebar'])

defineProps({
  isCollapse: {
    type: Boolean,
    default: false
  }
})

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const currentRoute = computed(() => {
  return route
})

// 监听头像更新事件
const handleAvatarUpdate = (event) => {
  const { avatar } = event.detail
  // 更新userStore中的头像
  if (userStore.userInfo) {
    userStore.userInfo.avatar = avatar
  }
}

onMounted(() => {
  window.addEventListener('userAvatarUpdate', handleAvatarUpdate)
})

onUnmounted(() => {
  window.removeEventListener('userAvatarUpdate', handleAvatarUpdate)
})

const toggleSidebar = () => {
  emit('toggle-sidebar')
}

const handleRefresh = () => {
  window.location.reload()
}

const handleFullscreen = () => {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen()
  } else {
    if (document.exitFullscreen) {
      document.exitFullscreen()
    }
  }
}

const handleCommand = (command) => {
  if (command === 'logout') {
    ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      userStore.logout()
      router.push('/login')
      ElMessage.success('退出成功')
    })
  } else if (command === 'profile') {
    router.push('/admin/profile')
  } else if (command === 'frontend') {
    // 跳转到用户前台页面
    router.push('/front/home')
    ElMessage.success('已切换到用户前台')
  }
}
</script>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 50px;
  padding: 0 20px;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.navbar-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.hamburger {
  cursor: pointer;
  color: #5a5e66;
  transition: color 0.3s;
}

.hamburger:hover {
  color: #42a5f5;
}

.breadcrumb {
  font-size: 14px;
}

.navbar-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.navbar-icon {
  cursor: pointer;
  color: #5a5e66;
  transition: color 0.3s;
}

.navbar-icon:hover {
  color: #42a5f5;
}

.user-dropdown {
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 5px 10px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-info:hover {
  background-color: #f5f7fa;
}

.username {
  font-size: 14px;
  color: #606266;
  font-weight: 500;
}
</style>
