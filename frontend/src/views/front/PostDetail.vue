<template>
  <div class="post-detail-page">
    <!-- 返回按钮 -->
    <div class="back-button">
      <el-button @click="goBack" :icon="ArrowLeft" circle size="large" />
      <span class="back-text" @click="goBack">返回社区</span>
    </div>

    <el-card class="post-card" v-loading="loading">
      <!-- 帖子头部 -->
      <div class="post-header">
        <div class="author-info">
          <el-avatar :size="56" :src="getFileUrl(post.authorAvatar)">
            <el-icon><User /></el-icon>
          </el-avatar>
          <div class="author-detail">
            <div class="author-name">{{ post.authorName || '匿名用户' }}</div>
            <div class="post-meta">
              <span>{{ formatTime(post.createTime) }}</span>
              <el-tag v-if="post.categoryName" size="small" effect="plain">
                {{ post.categoryName }}
              </el-tag>
            </div>
          </div>
        </div>
        <el-button 
          v-if="post.userId && post.userId !== currentUserId"
          type="primary" 
          @click="handleSendMessage"
          :icon="ChatDotRound"
        >
          发私信
        </el-button>
      </div>

      <!-- 帖子标题 -->
      <h1 class="post-title">{{ post.title }}</h1>

      <!-- 帖子内容 -->
      <div class="post-content" v-html="post.content"></div>

      <!-- 帖子图片 -->
      <div class="post-images" v-if="postImages.length > 0">
        <el-image
          v-for="(img, index) in postImages"
          :key="index"
          :src="getFileUrl(img)"
          :preview-src-list="postImages.map(i => getFileUrl(i))"
          :initial-index="index"
          fit="cover"
          class="post-image"
        />
      </div>

      <!-- 帖子操作栏 -->
      <div class="post-actions">
        <div class="action-btn" :class="{ 'is-active': post.isLiked }" @click="handleLike">
          <el-icon><Star :class="{ 'is-filled': post.isLiked }" /></el-icon>
          <span>{{ post.isLiked ? '已点赞' : '点赞' }} {{ post.likeCount || 0 }}</span>
        </div>
        <div class="action-btn">
          <el-icon><ChatLineRound /></el-icon>
          <span>评论 {{ post.commentCount || 0 }}</span>
        </div>
        <div class="action-btn" :class="{ 'is-active': post.isCollected }" @click="handleCollect">
          <el-icon><Collection :class="{ 'is-filled': post.isCollected }" /></el-icon>
          <span>{{ post.isCollected ? '已收藏' : '收藏' }} {{ post.collectCount || 0 }}</span>
        </div>
        <div class="action-btn" @click="handleShare">
          <el-icon><Share /></el-icon>
          <span>分享</span>
        </div>
      </div>
    </el-card>

    <!-- 评论区 -->
    <el-card class="comment-section">
      <template #header>
        <div class="section-header">
          <h3>评论 ({{ comments.length }})</h3>
        </div>
      </template>

      <!-- 发表评论 -->
      <div class="comment-input">
        <el-avatar :size="40" :src="getFileUrl(userStore.userInfo.avatar)">
          <el-icon><User /></el-icon>
        </el-avatar>
        <div class="comment-input-wrapper">
          <el-input
            v-model="commentContent"
            type="textarea"
            :rows="3"
            placeholder="发表你的看法..."
            maxlength="500"
            show-word-limit
          />
          <el-upload
            v-if="commentImages.length < 3"
            :action="uploadAction"
            :headers="uploadHeaders"
            list-type="picture-card"
            :on-success="handleCommentUploadSuccess"
            :on-remove="handleRemoveCommentImage"
            :before-upload="beforeUpload"
            :file-list="commentImageList"
            accept="image/jpeg,image/jpg,image/png,image/gif"
            :limit="3"
            class="comment-upload"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
          <div class="upload-tip" v-if="commentImages.length < 3">最多上传3张图片</div>
        </div>
      </div>
      <div class="comment-submit">
        <el-button type="primary" @click="handleSubmitComment" :loading="submitting">
          发表评论
        </el-button>
      </div>

      <!-- 评论列表 -->
      <div class="comment-list" v-loading="loadingComments">
        <div v-for="comment in comments" :key="comment.id" class="comment-item">
          <el-avatar :size="40" :src="getFileUrl(comment.userAvatar)">
            <el-icon><User /></el-icon>
          </el-avatar>
          <div class="comment-content">
            <div class="comment-header">
              <span class="comment-author">{{ comment.userName }}</span>
              <span v-if="comment.replyToUserName" class="reply-to">
                回复 @{{ comment.replyToUserName }}
              </span>
              <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
            </div>
            <div class="comment-text">{{ comment.content }}</div>
            
            <!-- 评论图片 -->
            <div class="comment-images" v-if="getCommentImages(comment).length > 0">
              <el-image
                v-for="(img, index) in getCommentImages(comment)"
                :key="index"
                :src="getFileUrl(img)"
                :preview-src-list="getCommentImages(comment).map(i => getFileUrl(i))"
                :initial-index="index"
                fit="cover"
                class="comment-image"
              />
            </div>
            
            <div class="comment-actions">
              <span class="action-item" :class="{ 'is-active': comment.isLiked }" @click="handleLikeComment(comment)">
                <el-icon><Star :class="{ 'is-filled': comment.isLiked }" /></el-icon>
                {{ comment.isLiked ? '已赞' : '点赞' }} {{ comment.likeCount || 0 }}
              </span>
              <span class="action-item" @click="handleReply(comment)">
                <el-icon><ChatLineRound /></el-icon>
                回复
              </span>
            </div>
          </div>
        </div>

        <el-empty v-if="!comments.length && !loadingComments" description="暂无评论，快来发表第一条评论吧！" />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Star, ChatLineRound, Collection, Plus, Picture, Delete, ChatDotRound } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { getFileUrl } from '@/utils/file'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const currentUserId = computed(() => userStore.userInfo.id || userStore.userInfo.userId)

const post = ref({})
const comments = ref([])
const commentContent = ref('')
const loading = ref(false)
const loadingComments = ref(false)
const submitting = ref(false)

// 帖子图片
const postImages = ref([])

// 评论图片上传
const commentImages = ref([])
const commentImageList = ref([])
const uploadAction = ref('/api/file/upload')
const uploadHeaders = ref({
  'Authorization': 'Bearer ' + localStorage.getItem('token')
})

// 解析帖子图片
const parsePostImages = (images) => {
  if (!images) return []
  try {
    return typeof images === 'string' ? JSON.parse(images) : images
  } catch (e) {
    return []
  }
}

// 获取评论图片
const getCommentImages = (comment) => {
  if (!comment.images) return []
  try {
    return typeof comment.images === 'string' ? JSON.parse(comment.images) : comment.images
  } catch (e) {
    return []
  }
}

// 加载帖子详情
const loadPost = async () => {
  loading.value = true
  try {
    const userId = userStore.userInfo.id || userStore.userInfo.userId
    const res = await request.get(`/communityPost/${route.params.id}?currentUserId=${userId}`)
    post.value = res.data
    // 解析图片
    postImages.value = parsePostImages(res.data.images)
  } catch (error) {
    console.error('加载帖子失败:', error)
    ElMessage.error('加载帖子失败')
  } finally {
    loading.value = false
  }
}

// 加载评论列表
const loadComments = async () => {
  loadingComments.value = true
  try {
    const userId = userStore.userInfo.id || userStore.userInfo.userId
    const res = await request.get(`/communityComment/post/${route.params.id}?currentUserId=${userId}`)
    comments.value = res.data
  } catch (error) {
    console.error('加载评论失败:', error)
  } finally {
    loadingComments.value = false
  }
}

// 点赞帖子
const handleLike = async () => {
  try {
    const userId = userStore.userInfo.id || userStore.userInfo.userId
    const res = await request.post(`/communityPost/${post.value.id}/like?userId=${userId}`)
    
    // 更新点赞状态和数量
    post.value.isLiked = res.data.isLiked
    post.value.likeCount = res.data.likeCount
    
    if (res.data.isLiked) {
      ElMessage.success('点赞成功')
    } else {
      ElMessage.info('已取消点赞')
    }
  } catch (error) {
    console.error('点赞失败:', error)
    ElMessage.error('操作失败')
  }
}

// 收藏帖子
const handleCollect = async () => {
  try {
    const userId = userStore.userInfo.id || userStore.userInfo.userId
    const res = await request.post(`/communityPost/${post.value.id}/collect?userId=${userId}`)
    
    // 更新收藏状态和数量
    post.value.isCollected = res.data.isCollected
    post.value.collectCount = res.data.collectCount
    
    if (res.data.isCollected) {
      ElMessage.success('收藏成功')
    } else {
      ElMessage.info('已取消收藏')
    }
  } catch (error) {
    console.error('收藏失败:', error)
    ElMessage.error('操作失败')
  }
}

// 分享帖子
const handleShare = () => {
  ElMessage.success('分享链接已复制到剪贴板')
}

// 上传前检查
const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过 5MB!')
    return false
  }
  return true
}

// 评论图片上传成功
const handleCommentUploadSuccess = (response, file, fileList) => {
  if (response.code === 200) {
    commentImages.value.push(response.data.url)
    ElMessage.success('图片上传成功')
  } else {
    ElMessage.error('图片上传失败')
  }
}

// 移除评论图片
const handleRemoveCommentImage = (file, fileList) => {
  const index = commentImages.value.indexOf(file.response?.data?.url || file.url)
  if (index > -1) {
    commentImages.value.splice(index, 1)
  }
}

// 提交评论
const handleSubmitComment = async () => {
  if (!commentContent.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }

  submitting.value = true
  try {
    await request.post('/communityComment', {
      postId: post.value.id,
      userId: userStore.userInfo.id || userStore.userInfo.userId,
      content: commentContent.value,
      parentId: 0,
      images: JSON.stringify(commentImages.value) // 将图片数组转为JSON字符串
    })
    
    ElMessage.success('评论成功')
    commentContent.value = ''
    commentImages.value = []
    commentImageList.value = []
    loadComments()
    post.value.commentCount = (post.value.commentCount || 0) + 1
  } catch (error) {
    console.error('评论失败:', error)
    ElMessage.error(error.response?.data?.message || '评论失败')
  } finally {
    submitting.value = false
  }
}

// 发私信
const handleSendMessage = () => {
  // 跳转到聊天页面，并传递用户信息
  router.push({
    path: '/front/chat',
    query: {
      userId: post.value.userId,
      userName: post.value.authorName,
      userAvatar: post.value.authorAvatar
    }
  })
}

// 点赞评论
const handleLikeComment = async (comment) => {
  try {
    const userId = userStore.userInfo.id || userStore.userInfo.userId
    const res = await request.post(`/communityComment/${comment.id}/like?userId=${userId}`)
    
    // 更新点赞状态和数量
    comment.isLiked = res.data.isLiked
    comment.likeCount = res.data.likeCount
    
    if (res.data.isLiked) {
      ElMessage.success('点赞成功')
    } else {
      ElMessage.info('已取消点赞')
    }
  } catch (error) {
    console.error('点赞失败:', error)
    ElMessage.error('操作失败')
  }
}

// 回复评论
const handleReply = (comment) => {
  ElMessage.info('回复功能开发中')
}

// 返回社区
const goBack = () => {
  router.push('/front/community')
}

// 格式化时间
const formatTime = (timeStr) => {
  if (!timeStr) return '-'
  const time = new Date(timeStr)
  const now = new Date()
  const diff = now - time
  
  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return `${Math.floor(diff / 60000)}分钟前`
  if (diff < 86400000) return `${Math.floor(diff / 3600000)}小时前`
  if (diff < 604800000) return `${Math.floor(diff / 86400000)}天前`
  
  return `${time.getMonth() + 1}-${time.getDate()}`
}

onMounted(() => {
  loadPost()
  loadComments()
})
</script>

<style scoped lang="scss">
.post-detail-page {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
}

/* 过渡动画 */
.slide-down-enter-active {
  animation: slideDown 0.5s ease-out;
}

.fade-up-enter-active {
  animation: fadeUp 0.6s ease-out;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.back-button {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  cursor: pointer;
}

.back-button .el-button {
  background: #fff;
  border: 1px solid #e0e0e0;
  transition: all 0.3s ease;
}

.back-button .el-button:hover {
  background: #667eea;
  border-color: #667eea;
  color: #fff;
  transform: translateX(-4px);
}

.back-text {
  font-size: 16px;
  color: #666;
  font-weight: 500;
  transition: color 0.3s;
}

.back-text:hover {
  color: #667eea;
}

.post-card {
  margin-bottom: 20px;
  border-radius: 16px;
}

.post-header {
  margin-bottom: 24px;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.author-detail {
  flex: 1;
}

.author-name {
  font-size: 16px;
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 4px;
}

.post-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 13px;
  color: #999;
}

.post-title {
  font-size: 28px;
  font-weight: 700;
  color: #1a1a1a;
  margin: 0 0 24px 0;
  line-height: 1.4;
}

.post-content {
  font-size: 16px;
  line-height: 1.8;
  color: #333;
  margin-bottom: 20px;
  white-space: pre-wrap;
  word-wrap: break-word;
}

.post-images {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  margin-bottom: 32px;
}

.post-image {
  width: 100%;
  height: 200px;
  border-radius: 8px;
  cursor: pointer;
  transition: transform 0.3s;
}

.post-image:hover {
  transform: scale(1.05);
}

.post-actions {
  display: flex;
  gap: 32px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background: #f8f9fa;
  position: relative;
  overflow: hidden;
}

.action-btn::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  border-radius: 50%;
  background: rgba(102, 126, 234, 0.3);
  transform: translate(-50%, -50%);
  transition: width 0.6s, height 0.6s;
}

.action-btn:active::before {
  width: 200px;
  height: 200px;
}

.action-btn:hover {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(102, 126, 234, 0.3);
}

.action-btn.is-active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  box-shadow: 0 4px 10px rgba(102, 126, 234, 0.4);
}

.action-btn .el-icon {
  font-size: 16px;
}

.action-btn .el-icon.is-filled {
  color: #ffd700;
}

.comment-section {
  border-radius: 16px;
}

.section-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
}

.comment-input {
  display: flex;
  gap: 12px;
  margin-bottom: 12px;
}

.comment-input-wrapper {
  flex: 1;
}

.comment-upload {
  margin-top: 12px;
}

.upload-tip {
  font-size: 12px;
  color: #999;
  margin-top: 8px;
}

.comment-submit {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 24px;
}

.comment-list {
  min-height: 200px;
}

.comment-item {
  display: flex;
  gap: 12px;
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-content {
  flex: 1;
}

.comment-header {
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.comment-author {
  font-weight: 600;
  color: #1a1a1a;
}

.reply-to {
  color: #667eea;
  font-size: 13px;
}

.comment-time {
  font-size: 13px;
  color: #999;
  margin-left: auto;
}

.comment-text {
  color: #333;
  line-height: 1.6;
  margin-bottom: 8px;
}

.comment-images {
  display: flex;
  gap: 8px;
  margin-top: 12px;
  margin-bottom: 8px;
  flex-wrap: wrap;
}

.comment-image {
  width: 120px;
  height: 120px;
  border-radius: 6px;
  cursor: pointer;
  transition: transform 0.3s;
}

.comment-image:hover {
  transform: scale(1.05);
}

.comment-actions {
  display: flex;
  gap: 24px;
}

.action-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: #999;
  cursor: pointer;
  transition: color 0.3s;
}

.action-item:hover {
  color: #667eea;
}

.action-item.is-active {
  color: #667eea;
  font-weight: 600;
}

.action-item .el-icon.is-filled {
  color: #ffd700;
}
</style>
