<template>
  <div class="chat-page">
    <div class="chat-container">
      <!-- 左侧会话列表 -->
      <div class="chat-sidebar">
        <div class="sidebar-header">
          <div class="header-title">
            <el-icon class="title-icon"><ChatDotRound /></el-icon>
            <span>消息</span>
            <el-badge :value="totalUnread" :hidden="totalUnread === 0" class="unread-badge" />
          </div>
          <el-tooltip content="新建会话" placement="bottom">
            <el-button 
              type="primary" 
              circle 
              size="small"
              @click="showUserSelectDialog = true"
              class="new-chat-btn"
            >
              <el-icon><Plus /></el-icon>
            </el-button>
          </el-tooltip>
        </div>

        <div class="search-box">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索会话..."
            :prefix-icon="Search"
            clearable
            class="search-input"
          />
        </div>

        <div v-loading="listLoading" class="chat-list">
          <transition-group name="chat-list">
            <div
              v-for="chat in filteredChatList"
              :key="chat.userId"
              class="chat-item"
              :class="{ active: currentChat?.userId === chat.userId }"
              @click="selectChat(chat)"
            >
              <div class="chat-avatar">
                <el-badge :value="chat.unreadCount" :hidden="chat.unreadCount === 0" :max="99">
                  <el-avatar :size="50" :src="getFileUrl(chat.userAvatar)">
                    <el-icon><User /></el-icon>
                  </el-avatar>
                </el-badge>
                <div class="online-status" :class="{ online: chat.isOnline }"></div>
              </div>
              <div class="chat-info">
                <div class="info-top">
                  <div class="chat-name">{{ chat.userName }}</div>
                  <div class="chat-time">{{ formatTime(chat.lastTime) }}</div>
                </div>
                <div class="info-bottom">
                  <div class="last-message">{{ chat.lastMessage || '开始聊天吧~' }}</div>
                  <span v-if="chat.unreadCount > 0" class="unread-dot"></span>
                </div>
              </div>
            </div>
          </transition-group>
          
          <el-empty v-if="!filteredChatList.length && !listLoading" description="暂无会话" />
        </div>
      </div>

      <!-- 右侧聊天窗口 -->
      <div class="chat-main">
        <div v-if="currentChat" class="chat-window">
          <div class="chat-header">
            <div class="header-user">
              <el-avatar :size="45" :src="getFileUrl(currentChat.userAvatar)">
                <el-icon><User /></el-icon>
              </el-avatar>
              <div class="user-details">
                <div class="user-name">{{ currentChat.userName }}</div>
                <div class="user-status">
                  <span class="status-dot" :class="{ online: currentChat.isOnline }"></span>
                  <span class="status-text">{{ currentChat.isOnline ? '在线' : '离线' }}</span>
                </div>
              </div>
            </div>
            <div class="header-actions">
              <el-tooltip content="更多" placement="bottom">
                <el-button circle size="small" text>
                  <el-icon><MoreFilled /></el-icon>
                </el-button>
              </el-tooltip>
            </div>
          </div>
            <!-- 消息列表 -->
            <div ref="messageListRef" v-loading="messageLoading" class="message-list">
              <div
                v-for="message in messages"
                :key="message.id"
                class="message-item"
                :class="{ 'is-mine': message.senderId === currentUserId }"
              >
                <el-avatar :size="40" :src="getFileUrl(message.senderAvatar)">
                  <el-icon><User /></el-icon>
                </el-avatar>
                
                <div class="message-content">
                  <!-- 已撤回消息 -->
                  <div v-if="message.isWithdraw === 1" class="message-bubble withdrawn">
                    <el-icon><WarningFilled /></el-icon>
                    {{ message.senderId === currentUserId ? '你' : message.senderName }}撤回了一条消息
                  </div>
                  
                  <!-- 文本消息 -->
                  <div v-else-if="message.contentType === 'text'" class="message-bubble">
                    {{ message.content }}
                    <!-- 撤回按钮 -->
                    <el-button
                      v-if="message.senderId === currentUserId && canWithdraw(message)"
                      class="withdraw-btn"
                      type="danger"
                      size="small"
                      link
                      @click="handleWithdraw(message)"
                    >
                      撤回
                    </el-button>
                  </div>
                  
                  <!-- 图片消息 -->
                  <div v-else-if="message.contentType === 'image'" class="message-bubble image-message">
                    <el-image
                      :src="getFileUrl(message.content)"
                      :preview-src-list="[getFileUrl(message.content)]"
                      fit="cover"
                      class="chat-image"
                    />
                    <el-button
                      v-if="message.senderId === currentUserId && canWithdraw(message)"
                      class="withdraw-btn"
                      type="danger"
                      size="small"
                      link
                      @click="handleWithdraw(message)"
                    >
                      撤回
                    </el-button>
                  </div>
                  
                  <!-- 文件消息 -->
                  <div v-else-if="message.contentType === 'file'" class="message-bubble file-message">
                    <div class="file-info">
                      <el-icon :size="24"><Document /></el-icon>
                      <span class="file-name">{{ getFileName(message.content) }}</span>
                    </div>
                    <el-button type="primary" size="small" @click="downloadFile(message.content)">
                      下载
                    </el-button>
                    <el-button
                      v-if="message.senderId === currentUserId && canWithdraw(message)"
                      class="withdraw-btn"
                      type="danger"
                      size="small"
                      link
                      @click="handleWithdraw(message)"
                    >
                      撤回
                    </el-button>
                  </div>
                  
                  <div class="message-time">
                    {{ formatMessageTime(message.createTime) }}
                    <el-icon v-if="message.senderId === currentUserId && message.isRead === 1" class="read-icon">
                      <Check />
                    </el-icon>
                  </div>
                </div>
              </div>
            </div>

            <!-- 输入框 -->
            <div class="message-input">
              <div class="input-toolbar">
                <el-upload
                  :action="uploadUrl"
                  :headers="uploadHeaders"
                  :show-file-list="false"
                  :on-success="handleImageSuccess"
                  accept="image/*"
                >
                  <el-button :icon="Picture" circle title="发送图片" />
                </el-upload>
                
                <el-upload
                  :action="uploadUrl"
                  :headers="uploadHeaders"
                  :show-file-list="false"
                  :on-success="handleFileSuccess"
                >
                  <el-button :icon="Document" circle title="发送文件" />
                </el-upload>
              </div>
              
              <el-input
                v-model="inputMessage"
                type="textarea"
                :rows="3"
                placeholder="输入消息...（Enter发送，Shift+Enter换行）"
                @keydown.enter="handleKeyDown"
              />
              <div class="input-actions">
                <el-button type="primary" @click="handleSend" :icon="Promotion">
                  发送
                </el-button>
              </div>
            </div>
        </div>

        <el-empty v-else description="选择一个会话开始聊天" />
      </div>
    </div>

    <!-- 选择用户对话框 -->
    <el-dialog
      v-model="showUserSelectDialog"
      title="选择聊天对象"
      width="600px"
      :close-on-click-modal="false"
      @open="handleDialogOpen"
    >
      <div class="user-select-dialog">
        <el-input
          v-model="userSearchKeyword"
          placeholder="搜索用户名或昵称..."
          :prefix-icon="Search"
          clearable
          @input="searchUsers"
          class="search-input"
        />
        
        <div v-loading="userLoading" class="user-list">
          <div
            v-for="user in userList"
            :key="user.id"
            class="user-item"
            @click="startChatWithUser(user)"
          >
            <el-avatar :size="40" :src="getFileUrl(user.avatar)">
              <el-icon><User /></el-icon>
            </el-avatar>
            <div class="user-info">
              <div class="user-name">{{ user.nickname || user.username }}</div>
              <div class="user-desc">{{ user.email || '暂无简介' }}</div>
            </div>
            <el-button type="primary" size="small">发消息</el-button>
          </div>
          
          <el-empty v-if="!userList.length && !userLoading" description="暂无用户" />
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, computed } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  ChatDotRound, Bell, User, Promotion, Picture, Document, WarningFilled, Check, Plus, Search, MoreFilled
} from '@element-plus/icons-vue'
import {
  getChatList,
  getChatHistory,
  sendChat,
  sendImage,
  sendFile,
  withdrawMessage,
  getUnreadChatCount
} from '@/api/community'
import { getFileUrl } from '@/utils/file'
import { useUserStore } from '@/stores/user'
import request from '@/utils/request'

const route = useRoute()
const userStore = useUserStore()

const chatList = ref([])
const currentChat = ref(null)
const messages = ref([])
const inputMessage = ref('')
const listLoading = ref(false)
const messageLoading = ref(false)
const messageListRef = ref(null)

// 用户选择对话框
const showUserSelectDialog = ref(false)
const userSearchKeyword = ref('')
const userList = ref([])
const userLoading = ref(false)

// 会话搜索
const searchKeyword = ref('')

// 过滤后的会话列表
const filteredChatList = computed(() => {
  if (!searchKeyword.value) return chatList.value
  return chatList.value.filter(chat => 
    chat.userName.toLowerCase().includes(searchKeyword.value.toLowerCase())
  )
})

const uploadUrl = '/api/file/upload'
const uploadHeaders = {
  'Authorization': 'Bearer ' + localStorage.getItem('token')
}

const currentUserId = computed(() => userStore.userInfo.id || userStore.userInfo.userId)
const totalUnread = computed(() => {
  return chatList.value.reduce((sum, chat) => sum + (chat.unreadCount || 0), 0)
})

// 加载聊天列表
const loadChatList = async () => {
  listLoading.value = true
  try {
    const res = await getChatList(currentUserId.value)
    chatList.value = res.data || []
  } catch (error) {
    console.error('加载聊天列表失败:', error)
    ElMessage.error('加载聊天列表失败')
  } finally {
    listLoading.value = false
  }
}

// 选择会话
const selectChat = async (chat) => {
  currentChat.value = chat
  await loadChatHistory()
  // 清除未读数
  chat.unreadCount = 0
  
  // 触发导航栏更新未读数
  window.dispatchEvent(new CustomEvent('chatRead'))
}

// 加载聊天记录
const loadChatHistory = async () => {
  if (!currentChat.value) return
  
  messageLoading.value = true
  try {
    const res = await getChatHistory({
      userId: currentUserId.value,
      otherUserId: currentChat.value.userId,
      pageNum: 1,
      pageSize: 100
    })
    messages.value = res.data || []
    
    // 滚动到底部
    await nextTick()
    scrollToBottom()
  } catch (error) {
    console.error('加载聊天记录失败:', error)
    ElMessage.error('加载聊天记录失败')
  } finally {
    messageLoading.value = false
  }
}

// 键盘事件处理
const handleKeyDown = (e) => {
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault()
    handleSend()
  }
}

// 发送消息
const handleSend = async () => {
  if (!inputMessage.value.trim()) {
    ElMessage.warning('请输入消息内容')
    return
  }
  
  if (!currentChat.value) {
    ElMessage.warning('请先选择聊天对象')
    return
  }
  
  try {
    const res = await sendChat({
      senderId: currentUserId.value,
      receiverId: currentChat.value.userId,
      content: inputMessage.value.trim()
    })
    
    // 检查是否审核失败
    if (res.code !== 200) {
      ElMessage.error({
        message: res.message || '发送失败',
        duration: 5000,
        showClose: true
      })
      return
    }
    
    // 添加到消息列表
    messages.value.push({
      ...res.data,
      senderAvatar: userStore.userInfo.avatar,
      senderName: userStore.userInfo.nickname || userStore.userInfo.username
    })
    
    // 更新会话列表
    updateChatList(inputMessage.value.trim())
    
    inputMessage.value = ''
    
    // 滚动到底部
    await nextTick()
    scrollToBottom()
  } catch (error) {
    console.error('发送失败:', error)
    const errorMsg = error.response?.data?.message || error.message || '发送失败'
    ElMessage.error({
      message: errorMsg,
      duration: 5000,
      showClose: true
    })
  }
}

// 图片上传成功
const handleImageSuccess = async (response) => {
  if (!currentChat.value) {
    ElMessage.warning('请先选择聊天对象')
    return
  }
  
  try {
    // 确保content是字符串
    const imageUrl = typeof response.data === 'string' ? response.data : response.data?.url || response.data?.path || ''
    
    const res = await sendImage({
      senderId: currentUserId.value,
      receiverId: currentChat.value.userId,
      content: imageUrl
    })
    
    messages.value.push({
      ...res.data,
      senderAvatar: userStore.userInfo.avatar,
      senderName: userStore.userInfo.nickname || userStore.userInfo.username
    })
    
    updateChatList('[图片]')
    
    await nextTick()
    scrollToBottom()
    
    ElMessage.success('图片发送成功')
  } catch (error) {
    console.error('发送失败:', error)
    ElMessage.error('图片发送失败')
  }
}

// 文件上传成功
const handleFileSuccess = async (response) => {
  if (!currentChat.value) {
    ElMessage.warning('请先选择聊天对象')
    return
  }
  
  try {
    // 确保content是字符串
    const fileUrl = typeof response.data === 'string' ? response.data : response.data?.url || response.data?.path || ''
    
    const res = await sendFile({
      senderId: currentUserId.value,
      receiverId: currentChat.value.userId,
      content: fileUrl
    })
    
    messages.value.push({
      ...res.data,
      senderAvatar: userStore.userInfo.avatar,
      senderName: userStore.userInfo.nickname || userStore.userInfo.username
    })
    
    updateChatList('[文件]')
    
    await nextTick()
    scrollToBottom()
    
    ElMessage.success('文件发送成功')
  } catch (error) {
    console.error('发送失败:', error)
    ElMessage.error('文件发送失败')
  }
}

// 更新会话列表
const updateChatList = (lastMessage) => {
  const chatIndex = chatList.value.findIndex(c => c.userId === currentChat.value.userId)
  if (chatIndex !== -1) {
    chatList.value[chatIndex].lastMessage = lastMessage
    chatList.value[chatIndex].lastTime = new Date()
    // 移到第一位
    const chat = chatList.value.splice(chatIndex, 1)[0]
    chatList.value.unshift(chat)
  }
}

// 判断是否可以撤回（2分钟内）
const canWithdraw = (message) => {
  const now = new Date()
  const createTime = new Date(message.createTime)
  const diff = (now - createTime) / 1000 / 60 // 分钟
  return diff <= 2
}

// 撤回消息
const handleWithdraw = async (message) => {
  try {
    await withdrawMessage(message.id, currentUserId.value)
    message.isWithdraw = 1
    ElMessage.success('消息已撤回')
  } catch (error) {
    console.error('撤回失败:', error)
    ElMessage.error(error.response?.data?.message || '撤回失败')
  }
}

// 获取文件名
const getFileName = (url) => {
  if (!url) return '未知文件'
  const parts = url.split('/')
  return parts[parts.length - 1]
}

// 下载文件
const downloadFile = (url) => {
  window.open(getFileUrl(url), '_blank')
}

// 对话框打开时加载用户列表
const handleDialogOpen = () => {
  userSearchKeyword.value = ''
  searchUsers()
}

// 搜索用户
const searchUsers = async () => {
  userLoading.value = true
  try {
    // 构建查询参数
    const params = {
      pageNum: 1,
      pageSize: 50
    }
    
    // 如果有搜索关键词，使用username参数搜索
    if (userSearchKeyword.value && userSearchKeyword.value.trim()) {
      params.username = userSearchKeyword.value.trim()
    }
    
    const res = await request.get('/user/list', { params })
    
    console.log('用户列表响应:', res)
    
    // 处理分页数据
    let users = []
    if (res.data && res.data.records) {
      users = res.data.records
    } else if (res.data && Array.isArray(res.data)) {
      users = res.data
    } else if (Array.isArray(res)) {
      users = res
    }
    
    // 过滤掉当前用户
    userList.value = users.filter(
      user => (user.id || user.userId) !== currentUserId.value
    )
    
    console.log('过滤后的用户列表:', userList.value)
  } catch (error) {
    console.error('搜索用户失败:', error)
    ElMessage.error(error.message || '搜索用户失败')
  } finally {
    userLoading.value = false
  }
}

// 开始与用户聊天
const startChatWithUser = (user) => {
  // 检查是否已存在会话
  const existingChat = chatList.value.find(
    chat => chat.userId === user.id || chat.userId === user.userId
  )
  
  if (existingChat) {
    // 已存在会话，直接选中
    selectChat(existingChat)
  } else {
    // 创建新会话
    const newChat = {
      userId: user.id || user.userId,
      userName: user.nickname || user.username,
      userAvatar: user.avatar,
      lastMessage: '',
      lastTime: new Date(),
      unreadCount: 0
    }
    
    // 添加到会话列表
    chatList.value.unshift(newChat)
    selectChat(newChat)
  }
  
  // 关闭对话框
  showUserSelectDialog.value = false
  userSearchKeyword.value = ''
  userList.value = []
}

// 滚动到底部
const scrollToBottom = () => {
  if (messageListRef.value) {
    messageListRef.value.scrollTop = messageListRef.value.scrollHeight
  }
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date
  
  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return Math.floor(diff / 60000) + '分钟前'
  if (diff < 86400000) return Math.floor(diff / 3600000) + '小时前'
  if (diff < 604800000) return Math.floor(diff / 86400000) + '天前'
  
  return date.toLocaleDateString()
}

const formatMessageTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

onMounted(async () => {
  await loadChatList()
  
  // 如果URL中有用户信息，自动打开聊天
  if (route.query.userId) {
    const targetUser = {
      userId: parseInt(route.query.userId),
      userName: route.query.userName,
      userAvatar: route.query.userAvatar,
      lastMessage: '',
      lastTime: new Date(),
      unreadCount: 0
    }
    
    // 检查是否已存在会话
    const existingChat = chatList.value.find(chat => chat.userId === targetUser.userId)
    if (existingChat) {
      selectChat(existingChat)
    } else {
      // 添加新会话并选中
      chatList.value.unshift(targetUser)
      selectChat(targetUser)
    }
  }
})
</script>

<style scoped lang="scss">
.chat-page {
  height: calc(100vh - 80px);
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.chat-container {
  width: 100%;
  max-width: 1400px;
  height: 90%;
  display: flex;
  gap: 0;
  background: #fff;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

/* 左侧边栏 */
.chat-sidebar {
  width: 350px;
  background: #f8f9fa;
  border-right: 1px solid #e9ecef;
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  padding: 20px;
  background: #fff;
  border-bottom: 1px solid #e9ecef;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .header-title {
    display: flex;
    align-items: center;
    gap: 10px;
    font-size: 20px;
    font-weight: 700;
    color: #1f2937;

    .title-icon {
      font-size: 24px;
      color: #667eea;
    }

    .unread-badge {
      margin-left: 8px;
    }
  }

  .new-chat-btn {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
    transition: all 0.3s;

    &:hover {
      transform: scale(1.1);
      box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
    }
  }
}

.search-box {
  padding: 15px 20px;
  background: #fff;
  border-bottom: 1px solid #e9ecef;

  .search-input {
    :deep(.el-input__wrapper) {
      border-radius: 20px;
      background: #f8f9fa;
      box-shadow: none;
      border: 1px solid #e9ecef;
      transition: all 0.3s;

      &:hover, &.is-focus {
        border-color: #667eea;
        background: #fff;
      }
    }
  }
}

.chat-list {
  flex: 1;
  overflow-y: auto;
  padding: 10px 0;

  &::-webkit-scrollbar {
    width: 6px;
  }

  &::-webkit-scrollbar-thumb {
    background: #d1d5db;
    border-radius: 3px;

    &:hover {
      background: #9ca3af;
    }
  }
}

/* 列表动画 */
.chat-list-enter-active,
.chat-list-leave-active {
  transition: all 0.3s ease;
}

.chat-list-enter-from {
  opacity: 0;
  transform: translateX(-20px);
}

.chat-list-leave-to {
  opacity: 0;
  transform: translateX(20px);
}

.chat-item {
  display: flex;
  gap: 15px;
  padding: 15px 20px;
  cursor: pointer;
  transition: all 0.3s;
  position: relative;
  background: #fff;
  margin: 0 10px 8px;
  border-radius: 12px;

  &:hover {
    background: #f3f4f6;
    transform: translateX(5px);
  }

  &.active {
    background: linear-gradient(135deg, #667eea15 0%, #764ba215 100%);
    border-left: 4px solid #667eea;
    box-shadow: 0 2px 8px rgba(102, 126, 234, 0.2);
  }

  .chat-avatar {
    position: relative;

    .online-status {
      position: absolute;
      bottom: 2px;
      right: 2px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: #9ca3af;
      border: 2px solid #fff;
      transition: all 0.3s;

      &.online {
        background: #10b981;
        box-shadow: 0 0 8px rgba(16, 185, 129, 0.6);
      }
    }
  }

  .chat-info {
    flex: 1;
    min-width: 0;
    display: flex;
    flex-direction: column;
    gap: 6px;

    .info-top {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .chat-name {
        font-weight: 600;
        font-size: 15px;
        color: #1f2937;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .chat-time {
        font-size: 12px;
        color: #9ca3af;
        flex-shrink: 0;
      }
    }

    .info-bottom {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .last-message {
        flex: 1;
        font-size: 13px;
        color: #6b7280;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .unread-dot {
        width: 8px;
        height: 8px;
        border-radius: 50%;
        background: #ef4444;
        flex-shrink: 0;
        margin-left: 8px;
        display: inline-block;
      }
    }
  }
}

/* 聊天主窗口 */
.chat-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: #fff;
}

.chat-window {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.chat-header {
  padding: 20px 30px;
  background: #fff;
  border-bottom: 1px solid #e9ecef;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

  .header-user {
    display: flex;
    align-items: center;
    gap: 15px;

    .user-details {
      .user-name {
        font-size: 18px;
        font-weight: 700;
        color: #1f2937;
        margin-bottom: 4px;
      }

      .user-status {
        display: flex;
        align-items: center;
        gap: 6px;
        font-size: 13px;
        color: #6b7280;

        .status-dot {
          width: 8px;
          height: 8px;
          border-radius: 50%;
          background: #9ca3af;
          transition: all 0.3s;

          &.online {
            background: #10b981;
            box-shadow: 0 0 6px rgba(16, 185, 129, 0.6);
          }
        }
      }
    }
  }

  .header-actions {
    display: flex;
    gap: 8px;
  }
}

.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 30px;
  background: linear-gradient(to bottom, #f8f9fa 0%, #ffffff 100%);

  &::-webkit-scrollbar {
    width: 8px;
  }

  &::-webkit-scrollbar-thumb {
    background: #d1d5db;
    border-radius: 4px;

    &:hover {
      background: #9ca3af;
    }
  }
}

.message-item {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
  animation: messageSlideIn 0.3s ease;

  &.is-mine {
    flex-direction: row-reverse;

    .message-bubble {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: #fff;
      box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);

      &.withdrawn {
        background: #f3f4f6;
        color: #9ca3af;
      }
    }

    .message-time {
      text-align: right;
    }
  }

  &:not(.is-mine) {
    .message-bubble {
      background: #fff;
      color: #1f2937;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);

      &.withdrawn {
        background: #f3f4f6;
        color: #9ca3af;
      }
    }
  }
}

@keyframes messageSlideIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.message-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
  max-width: 60%;
}

.message-bubble {
  padding: 14px 18px;
  border-radius: 18px;
  word-wrap: break-word;
  position: relative;
  max-width: 100%;
  transition: all 0.3s;

  &.image-message {
    padding: 6px;
    background: transparent !important;
    box-shadow: none !important;
  }

  &.file-message {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px 16px;
  }

  .withdraw-btn {
    position: absolute;
    top: 6px;
    right: 6px;
    opacity: 0;
    transition: all 0.3s;
    background: rgba(0, 0, 0, 0.1);
    border-radius: 50%;

    &:hover {
      background: rgba(0, 0, 0, 0.2);
      transform: scale(1.1);
    }
  }

  &:hover .withdraw-btn {
    opacity: 1;
  }
}

.chat-image {
  max-width: 200px;
  max-height: 200px;
  border-radius: 8px;
  cursor: pointer;
}

.file-info {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
}

.file-name {
  font-size: 14px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.message-time {
  font-size: 12px;
  color: #999;
  padding: 0 8px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.read-icon {
  color: #67c23a;
  font-size: 14px;
}

.message-input {
  padding: 20px 30px;
  background: #fff;
  border-top: 1px solid #e9ecef;
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.05);

  :deep(.el-textarea__inner) {
    border-radius: 12px;
    border: 1px solid #e9ecef;
    padding: 12px 16px;
    font-size: 14px;
    transition: all 0.3s;

    &:focus {
      border-color: #667eea;
      box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
    }
  }
}

.input-toolbar {
  display: flex;
  gap: 12px;
  margin-bottom: 12px;

  :deep(.el-upload) {
    display: inline-block;
  }

  .el-button {
    border-radius: 8px;
    transition: all 0.3s;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    }
  }
}

.input-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;

  .el-button {
    border-radius: 8px;
    padding: 10px 24px;
    font-weight: 600;
    transition: all 0.3s;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
    }
  }
}

/* 用户选择对话框 */
.user-select-dialog {
  .search-input {
    margin-bottom: 16px;
  }

  .user-list {
    max-height: 400px;
    overflow-y: auto;
  }

  .user-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px;
    border-radius: 8px;
    cursor: pointer;
    transition: background 0.3s;

    &:hover {
      background: #f5f5f5;
    }

    .user-info {
      flex: 1;
      min-width: 0;
    }

    .user-name {
      font-weight: 600;
      color: #333;
      margin-bottom: 4px;
    }

    .user-desc {
      font-size: 12px;
      color: #999;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
}
</style>
