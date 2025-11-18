<template>
  <div class="front-navbar">
    <div class="navbar-container">
      <div class="logo">
        <el-icon :size="32" class="logo-icon"><Compass /></el-icon>
        <span class="logo-text">用户中心</span>
      </div>
      
      <el-menu 
        mode="horizontal" 
        :default-active="activeMenu" 
        router 
        class="navbar-menu"
        :ellipsis="false"
      >
        <el-menu-item index="/front/home">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
        </el-menu-item>
        <el-menu-item index="/front/community">
          <el-icon><ChatDotRound /></el-icon>
          <span>社区交流</span>
        </el-menu-item>
        <el-menu-item index="/front/activities">
          <el-icon><Calendar /></el-icon>
          <span>活动中心</span>
        </el-menu-item>
      </el-menu>
      
      <div class="navbar-right">
        <el-tooltip content="私信" placement="bottom">
          <el-badge :value="unreadChatCount" :hidden="unreadChatCount === 0" class="notification-badge">
            <el-icon :size="20" class="navbar-icon" @click="goToChat"><ChatLineSquare /></el-icon>
          </el-badge>
        </el-tooltip>
        
        <el-dropdown trigger="hover" @command="handleNoticeCommand">
          <el-badge :value="unreadNoticeCount" :hidden="unreadNoticeCount === 0" class="notification-badge">
            <el-icon :size="20" class="navbar-icon" @click="goToMessage"><Bell /></el-icon>
          </el-badge>
          <template #dropdown>
            <el-dropdown-menu class="notice-dropdown">
              <div class="notice-header">
                <span>通知公告</span>
                <el-link type="primary" @click="goToMessage" :underline="false">查看全部</el-link>
              </div>
              <el-scrollbar max-height="400px">
                <div v-if="recentNotices.length === 0" class="empty-notice">
                  <el-empty description="暂无通知" :image-size="60" />
                </div>
                <el-dropdown-item 
                  v-for="notice in recentNotices" 
                  :key="notice.id"
                  :command="notice.id"
                  class="notice-item"
                >
                  <div class="notice-content">
                    <div class="notice-title">
                      <el-tag 
                        :type="getNoticeTypeTag(notice.noticeType)" 
                        size="small"
                        style="margin-right: 8px;"
                      >
                        {{ getNoticeTypeText(notice.noticeType) }}
                      </el-tag>
                      <span class="title-text">{{ notice.title }}</span>
                    </div>
                    <div class="notice-desc">{{ truncate(notice.content, 50) }}</div>
                    <div class="notice-time">{{ formatTime(notice.publishTime) }}</div>
                  </div>
                </el-dropdown-item>
              </el-scrollbar>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        
        <el-dropdown @command="handleCommand" class="user-dropdown">
          <span class="user-info">
            <el-avatar :size="36" :src="getFileUrl(userStore.userInfo.avatar)">
              <el-icon><User /></el-icon>
            </el-avatar>
            <span class="username">{{ userStore.userInfo.nickname || userStore.userInfo.username }}</span>
            <el-icon class="arrow-icon"><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">
                <el-icon><User /></el-icon>
                <span>个人资料</span>
              </el-dropdown-item>
              <el-dropdown-item command="settings">
                <el-icon><Setting /></el-icon>
                <span>账户设置</span>
              </el-dropdown-item>
              <el-dropdown-item command="admin" v-if="isAdmin">
                <el-icon><Monitor /></el-icon>
                <span>后台管理</span>
              </el-dropdown-item>
              <el-dropdown-item divided command="logout">
                <el-icon><SwitchButton /></el-icon>
                <span>退出登录</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { HomeFilled, User, Setting, Compass, Bell, ArrowDown, SwitchButton, ChatDotRound, Calendar, Monitor, ChatLineSquare } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { getFileUrl } from '@/utils/file'
import { getUnreadChatCount, getUnreadNoticeCount, getFrontNoticeList, markNoticeAsRead } from '@/api/community'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)

// 判断是否有后台管理权限（仅系统管理员和超级管理员）
const isAdmin = computed(() => {
  const roleKey = userStore.userInfo?.roleKey
  console.log('当前用户角色:', roleKey)
  console.log('用户信息:', userStore.userInfo)
  const hasPermission = roleKey === 'admin' || roleKey === 'super_admin'
  console.log('是否有后台管理权限:', hasPermission)
  return hasPermission
})

// 未读消息数
const unreadChatCount = ref(0)
const unreadNoticeCount = ref(0)

// 最新通知列表
const recentNotices = ref([])

// 加载未读消息数
const loadUnreadCount = async () => {
  try {
    const userId = userStore.userInfo.id || userStore.userInfo.userId
    if (userId) {
      // 加载未读私信数
      const chatRes = await getUnreadChatCount(userId)
      unreadChatCount.value = chatRes.data || 0
      
      // 加载未读通知数
      const noticeRes = await getUnreadNoticeCount(userId)
      unreadNoticeCount.value = noticeRes.data || 0
      
      // 加载最新通知列表（最多5条）
      loadRecentNotices(userId)
    }
  } catch (error) {
    console.error('加载未读消息数失败:', error)
  }
}

// 加载最新通知
const loadRecentNotices = async (userId) => {
  try {
    const res = await getFrontNoticeList({
      userId,
      pageNum: 1,
      pageSize: 5
    })
    recentNotices.value = res.data.records || []
  } catch (error) {
    console.error('加载最新通知失败:', error)
  }
}

// 跳转到私信
const goToChat = () => {
  router.push('/front/chat')
}

// 跳转到通知
const goToMessage = () => {
  router.push('/front/message')
}

// 处理通知点击
const handleNoticeCommand = async (noticeId) => {
  try {
    const userId = userStore.userInfo.id || userStore.userInfo.userId
    // 标记为已读
    await markNoticeAsRead(noticeId, userId)
    
    // 跳转到通知页面
    router.push('/front/message')
    
    // 刷新未读数
    loadUnreadCount()
  } catch (error) {
    console.error('处理通知失败:', error)
  }
}

// 获取通知类型标签
const getNoticeTypeTag = (type) => {
  const map = {
    urgent: 'danger',
    important: 'warning',
    normal: 'info'
  }
  return map[type] || 'info'
}

// 获取通知类型文本
const getNoticeTypeText = (type) => {
  const map = {
    urgent: '紧急',
    important: '重要',
    normal: '一般'
  }
  return map[type] || '未知'
}

// 截断文本
const truncate = (text, length) => {
  if (!text) return ''
  return text.length > length ? text.substring(0, length) + '...' : text
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date
  
  // 1分钟内
  if (diff < 60000) {
    return '刚刚'
  }
  // 1小时内
  if (diff < 3600000) {
    return Math.floor(diff / 60000) + '分钟前'
  }
  // 24小时内
  if (diff < 86400000) {
    return Math.floor(diff / 3600000) + '小时前'
  }
  // 超过24小时
  return date.toLocaleDateString('zh-CN')
}

// 监听头像更新事件
const handleAvatarUpdate = (event) => {
  const { avatar } = event.detail
  // 更新userStore中的头像
  if (userStore.userInfo) {
    userStore.userInfo.avatar = avatar
  }
}

// 监听聊天已读事件
const handleChatRead = () => {
  loadUnreadCount()
}

// 监听消息已读事件
const handleMessageRead = () => {
  loadUnreadCount()
}

onMounted(() => {
  window.addEventListener('userAvatarUpdate', handleAvatarUpdate)
  window.addEventListener('chatRead', handleChatRead)
  window.addEventListener('messageRead', handleMessageRead)
  loadUnreadCount()
  
  // 定时刷新未读数（每30秒）
  const timer = setInterval(loadUnreadCount, 30000)
  
  // 清理定时器
  onUnmounted(() => {
    clearInterval(timer)
  })
})

onUnmounted(() => {
  window.removeEventListener('userAvatarUpdate', handleAvatarUpdate)
  window.removeEventListener('chatRead', handleChatRead)
  window.removeEventListener('messageRead', handleMessageRead)
})

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
    router.push('/front/profile')
  } else if (command === 'settings') {
    router.push('/front/settings')
  } else if (command === 'admin') {
    router.push('/admin/home')
    ElMessage.success('已切换到后台管理')
  }
}
</script>

<style scoped>
.front-navbar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  box-shadow: 0 2px 12px rgba(102, 126, 234, 0.3);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.navbar-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 64px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.logo:hover {
  transform: scale(1.05);
}

.logo-icon {
  color: #fff;
  animation: rotate 20s linear infinite;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.logo-text {
  font-size: 20px;
  font-weight: 700;
  color: #fff;
  letter-spacing: 1px;
}

.navbar-menu {
  flex: 1;
  margin: 0 40px;
  background: transparent;
  border: none;
}

.navbar-menu :deep(.el-menu-item) {
  color: rgba(255, 255, 255, 0.9);
  border-bottom: 3px solid transparent;
  font-weight: 500;
  transition: all 0.3s ease;
}

.navbar-menu :deep(.el-menu-item:hover) {
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
}

.navbar-menu :deep(.el-menu-item.is-active) {
  background: rgba(255, 255, 255, 0.15);
  color: #fff;
  border-bottom-color: #fff;
}

.navbar-right {
  display: flex;
  align-items: center;
  gap: 24px;
}

.notification-badge {
  cursor: pointer;
}

.navbar-icon {
  color: #fff;
  cursor: pointer;
  transition: all 0.3s ease;
}

.navbar-icon:hover {
  transform: scale(1.2);
}

.user-dropdown {
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 16px;
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.1);
  transition: all 0.3s ease;
}

.user-info:hover {
  background: rgba(255, 255, 255, 0.2);
}

.username {
  color: #fff;
  font-size: 14px;
  font-weight: 500;
}

.arrow-icon {
  color: #fff;
  font-size: 12px;
}

/* 通知下拉框样式 */
:deep(.notice-dropdown) {
  width: 360px;
  max-height: 500px;
}

.notice-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #f0f0f0;
  font-weight: 600;
  color: #333;
}

.empty-notice {
  padding: 20px;
  text-align: center;
}

:deep(.notice-item) {
  padding: 0 !important;
  border-bottom: 1px solid #f5f5f5;
}

:deep(.notice-item:hover) {
  background: #f8f9fa;
}

.notice-content {
  padding: 12px 16px;
  cursor: pointer;
}

.notice-title {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.notice-title .title-text {
  font-weight: 500;
  color: #333;
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  flex: 1;
}

.notice-desc {
  color: #666;
  font-size: 13px;
  line-height: 1.5;
  margin-bottom: 6px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
}

.notice-time {
  color: #999;
  font-size: 12px;
}

/* 响应式 */
@media (max-width: 768px) {
  .navbar-container {
    padding: 0 16px;
  }
  
  .navbar-menu {
    margin: 0 20px;
  }
  
  .username {
    display: none;
  }
}
</style>
