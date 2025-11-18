<template>
  <div class="community-page">
    <!-- Hero Banner -->
    <div class="community-hero">
      <div class="hero-content">
        <div class="hero-text">
          <h1>💬 社区交流</h1>
          <p>分享想法，交流经验，让每个声音被听见</p>
        </div>
        <el-button type="primary" size="large" round @click="showPostDialog = true">
          <el-icon><Edit /></el-icon>
          发布动态
        </el-button>
      </div>
      <div class="hero-stats">
        <div class="stat-item">
          <div class="stat-value">{{ total }}</div>
          <div class="stat-label">帖子总数</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ categories.length }}</div>
          <div class="stat-label">话题板块</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ userStore.userInfo.nickname }}</div>
          <div class="stat-label">当前用户</div>
        </div>
      </div>
    </div>

    <!-- 话题分类和搜索 -->
    <div class="filter-section">
      <div class="category-tabs">
        <div 
          class="category-tab" 
          :class="{ active: !searchForm.categoryId }"
          @click="selectCategory(null)"
        >
          <el-icon><Grid /></el-icon>
          <span>全部</span>
        </div>
        <div 
          v-for="category in categories" 
          :key="category.id"
          class="category-tab"
          :class="{ active: searchForm.categoryId === category.id }"
          @click="selectCategory(category.id)"
        >
          <el-icon><component :is="getCategoryIcon(category.categoryName)" /></el-icon>
          <span>{{ category.categoryName }}</span>
        </div>
      </div>
      
      <div class="search-bar">
        <el-input 
          v-model="searchForm.keyword" 
          placeholder="搜索帖子标题、内容..." 
          :prefix-icon="Search"
          clearable
          size="large"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button :icon="Search" @click="handleSearch">搜索</el-button>
          </template>
        </el-input>
      </div>
    </div>

    <!-- 帖子列表 -->
    <div class="posts-grid" v-loading="loading">
      <transition-group name="post-list" tag="div" class="posts-container">
        <el-card 
          v-for="post in posts" 
          :key="post.id" 
          class="post-card" 
          shadow="hover"
          @click="viewPost(post)"
        >
        <div class="post-main">
          <div class="post-author">
            <el-avatar :size="48" :src="getFileUrl(post.authorAvatar)">
              <el-icon><User /></el-icon>
            </el-avatar>
            <div class="author-info">
              <div class="author-name">{{ post.authorName || '匿名用户' }}</div>
              <div class="post-meta">
                <span class="post-time">{{ formatTime(post.createTime) }}</span>
                <el-tag v-if="post.categoryName" size="small" effect="plain">
                  {{ post.categoryName }}
                </el-tag>
              </div>
            </div>
            <el-button 
              v-if="post.userId && post.userId !== currentUserId"
              type="primary" 
              size="small"
              @click.stop="handleSendMessage(post)"
              :icon="ChatLineSquare"
            >
              发私信
            </el-button>
          </div>
          
          <div class="post-content">
            <h3 class="post-title">{{ post.title }}</h3>
            <p class="post-summary">{{ post.content }}</p>
            
            <!-- 帖子图片 -->
            <div class="post-images" v-if="getPostImages(post).length > 0">
              <el-image
                v-for="(img, index) in getPostImages(post).slice(0, 3)"
                :key="index"
                :src="getFileUrl(img)"
                :preview-src-list="getPostImages(post).map(i => getFileUrl(i))"
                :initial-index="index"
                fit="cover"
                class="post-image"
              />
              <div class="more-images" v-if="getPostImages(post).length > 3">
                +{{ getPostImages(post).length - 3 }}
              </div>
            </div>
          </div>
          
          <div class="post-actions">
            <div class="action-btn" :class="{ 'is-active': post.isLiked }" @click.stop="handleLike(post)">
              <el-icon><Star :class="{ 'is-filled': post.isLiked }" /></el-icon>
              <span>{{ post.isLiked ? '已赞' : '点赞' }} {{ post.likeCount || 0 }}</span>
            </div>
            <div class="action-btn" @click.stop="handleComment(post)">
              <el-icon><ChatLineRound /></el-icon>
              <span>{{ post.commentCount || 0 }}</span>
            </div>
            <div class="action-btn">
              <el-icon><View /></el-icon>
              <span>{{ post.viewCount || 0 }}</span>
            </div>
            <div class="action-btn" @click.stop="handleShare(post)">
              <el-icon><Share /></el-icon>
              <span>分享</span>
            </div>
          </div>
        </div>
      </el-card>
      </transition-group>

      <el-empty v-if="!posts.length && !loading" description="暂无帖子，快来发布第一个吧！" :image-size="120" />
    </div>

    <!-- 分页 -->
    <div class="pagination-wrapper">
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 30, 50]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadPosts"
        @current-change="loadPosts"
      />
    </div>

    <!-- 发帖弹窗 -->
    <el-dialog 
      v-model="showPostDialog" 
      title="发布帖子" 
      width="700px"
      :close-on-click-modal="false"
    >
      <el-form :model="postForm" :rules="postRules" ref="postFormRef" label-width="80px">
        <el-form-item label="帖子标题" prop="title">
          <el-input v-model="postForm.title" placeholder="请输入帖子标题" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item label="选择板块" prop="categoryId">
          <el-select v-model="postForm.categoryId" placeholder="请选择板块" style="width: 100%">
            <el-option 
              v-for="category in categories" 
              :key="category.id" 
              :label="category.categoryName" 
              :value="category.id" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="帖子内容" prop="content">
          <el-input 
            v-model="postForm.content" 
            type="textarea" 
            :rows="8"
            placeholder="分享你的想法..."
            maxlength="2000"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="上传图片">
          <el-upload
            :action="uploadAction"
            :headers="uploadHeaders"
            list-type="picture-card"
            :on-success="handleUploadSuccess"
            :on-remove="handleRemoveImage"
            :before-upload="beforeUpload"
            :file-list="imageList"
            accept="image/jpeg,image/jpg,image/png,image/gif"
            :limit="9"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
          <div class="upload-tip">支持jpg、png、gif格式，最多上传9张图片</div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showPostDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitPost" :loading="submitting">
          发布
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { 
  ChatDotRound, Edit, Search, Refresh, User, View, 
  ChatLineRound, Star, Share, Grid, Reading, TrophyBase,
  Briefcase, School, Service, Plus, ChatLineSquare
} from '@element-plus/icons-vue'
import { getPostList, addPost } from '@/api/community'
import { getCategoryList } from '@/api/community'
import { getFileUrl } from '@/utils/file'
import { useUserStore } from '@/stores/user'
import request from '@/utils/request'

const router = useRouter()
const userStore = useUserStore()

const currentUserId = computed(() => userStore.userInfo.id || userStore.userInfo.userId)

// 搜索表单
const searchForm = reactive({
  keyword: '',
  categoryId: null
})

// 数据
const posts = ref([])
const categories = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 发帖弹窗
const showPostDialog = ref(false)
const submitting = ref(false)
const postFormRef = ref(null)
const postForm = reactive({
  title: '',
  categoryId: null,
  content: '',
  userId: null, // 从用户信息获取
  images: [] // 图片URL数组
})

// 图片上传
const imageList = ref([])
const uploadAction = ref('/api/file/upload')
const uploadHeaders = ref({
  'Authorization': 'Bearer ' + localStorage.getItem('token')
})

const postRules = {
  title: [
    { required: true, message: '请输入帖子标题', trigger: 'blur' },
    { min: 5, max: 100, message: '标题长度在 5 到 100 个字符', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择板块', trigger: 'change' }
  ],
  content: [
    { required: true, message: '请输入帖子内容', trigger: 'blur' },
    { min: 10, message: '内容至少10个字符', trigger: 'blur' }
  ]
}

// 加载帖子列表
const loadPosts = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      auditStatus: '1', // 只显示已审核的帖子
      currentUserId: userStore.userInfo.id || userStore.userInfo.userId, // 传递当前用户ID
      ...searchForm
    }
    const res = await getPostList(params)
    posts.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('加载帖子失败:', error)
    ElMessage.error('加载帖子失败')
  } finally {
    loading.value = false
  }
}

// 加载板块列表
const loadCategories = async () => {
  try {
    console.log('开始加载板块列表...')
    const res = await getCategoryList({ pageNum: 1, pageSize: 100 })
    console.log('板块列表响应:', res)
    
    if (res && res.data) {
      // 兼容不同的响应格式
      if (res.data.records) {
        categories.value = res.data.records
      } else if (Array.isArray(res.data)) {
        categories.value = res.data
      } else {
        categories.value = []
      }
      console.log('板块列表加载成功，数量:', categories.value.length)
    } else {
      categories.value = []
      console.warn('板块列表响应格式异常')
    }
  } catch (error) {
    console.error('加载板块失败:', error)
    ElMessage.error('加载板块列表失败，请稍后重试')
    categories.value = []
  }
}

// 搜索
const handleSearch = () => {
  pageNum.value = 1
  loadPosts()
}

// 重置
const handleReset = () => {
  searchForm.keyword = ''
  searchForm.categoryId = null
  handleSearch()
}

// 选择分类
const selectCategory = (categoryId) => {
  searchForm.categoryId = categoryId
  pageNum.value = 1
  loadPosts()
}

// 获取分类图标
const getCategoryIcon = (categoryName) => {
  const iconMap = {
    '学习': Reading,
    '生活': Service,
    '活动': TrophyBase,
    '招聘': Briefcase,
    '校园': School
  }
  return iconMap[categoryName] || ChatDotRound
}

// 解析帖子图片
const getPostImages = (post) => {
  if (!post.images) return []
  try {
    return typeof post.images === 'string' ? JSON.parse(post.images) : post.images
  } catch (e) {
    return []
  }
}

// 点赞
const handleLike = async (post) => {
  try {
    const userId = userStore.userInfo.id || userStore.userInfo.userId
    const res = await request.post(`/communityPost/${post.id}/like?userId=${userId}`)
    
    // 更新点赞状态和数量
    post.isLiked = res.data.isLiked
    post.likeCount = res.data.likeCount
    
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

// 评论
const handleComment = (post) => {
  viewPost(post)
}

// 分享
const handleShare = (post) => {
  ElMessage.success('分享链接已复制到剪贴板')
}

// 查看帖子详情
const viewPost = (post) => {
  router.push(`/front/post/${post.id}`)
}

// 发私信
const handleSendMessage = (post) => {
  router.push({
    path: '/front/chat',
    query: {
      userId: post.userId,
      userName: post.authorName,
      userAvatar: post.authorAvatar
    }
  })
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

// 上传成功
const handleUploadSuccess = (response, file, fileList) => {
  if (response.code === 200) {
    postForm.images.push(response.data.url)
    ElMessage.success('图片上传成功')
  } else {
    ElMessage.error('图片上传失败')
  }
}

// 移除图片
const handleRemoveImage = (file, fileList) => {
  const index = postForm.images.indexOf(file.response?.data?.url || file.url)
  if (index > -1) {
    postForm.images.splice(index, 1)
  }
}

// 提交帖子
const handleSubmitPost = async () => {
  if (!postFormRef.value) return
  
  try {
    await postFormRef.value.validate()
    submitting.value = true
    
    // 从用户信息中获取userId
    const submitData = {
      ...postForm,
      userId: userStore.userInfo.id || userStore.userInfo.userId,
      images: JSON.stringify(postForm.images) // 将图片数组转为JSON字符串
    }
    
    console.log('提交帖子数据:', submitData)
    const res = await addPost(submitData)
    
    // 检查返回结果
    if (res.code === 200) {
      ElMessage.success('发布成功！')
      showPostDialog.value = false
      postFormRef.value.resetFields()
      postForm.images = []
      imageList.value = []
      loadPosts()
    } else {
      // 显示详细的违规信息
      ElMessage({
        type: 'warning',
        message: res.message || '发布失败',
        duration: 5000,
        dangerouslyUseHTMLString: true,
        customClass: 'audit-fail-message'
      })
    }
  } catch (error) {
    console.error('发布失败:', error)
    if (error !== false) {
      // 显示详细错误信息
      const errorMsg = error.response?.data?.message || error.message || '发布失败，请稍后重试'
      ElMessage({
        type: 'error',
        message: errorMsg,
        duration: 5000,
        dangerouslyUseHTMLString: true
      })
    }
  } finally {
    submitting.value = false
  }
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

onMounted(async () => {
  // 先加载板块，再加载帖子
  await loadCategories()
  
  // 如果没有板块数据，添加默认板块
  if (categories.value.length === 0) {
    console.warn('后端没有板块数据，使用默认板块')
    categories.value = [
      { id: 1, categoryName: '学习', description: '学习交流' },
      { id: 2, categoryName: '生活', description: '生活分享' },
      { id: 3, categoryName: '活动', description: '活动讨论' },
      { id: 4, categoryName: '招聘', description: '招聘信息' },
      { id: 5, categoryName: '校园', description: '校园话题' }
    ]
  }
  
  loadPosts()
})
</script>

<style scoped lang="scss">
.community-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0;
}

/* Hero Banner */
.community-hero {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 24px;
  padding: 48px;
  margin-bottom: 32px;
  color: #fff;
  position: relative;
  overflow: hidden;
}

.community-hero::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 70%);
  animation: rotate 20s linear infinite;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.hero-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
  position: relative;
  z-index: 1;
}

.hero-text h1 {
  font-size: 36px;
  font-weight: 800;
  margin: 0 0 12px 0;
}

.hero-text p {
  font-size: 16px;
  opacity: 0.95;
  margin: 0;
}

.hero-stats {
  display: flex;
  gap: 48px;
  position: relative;
  z-index: 1;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
}

/* 筛选区域 */
.filter-section {
  margin-bottom: 24px;
}

.category-tabs {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
  overflow-x: auto;
  padding-bottom: 8px;
}

.category-tab {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 24px;
  background: #fff;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  white-space: nowrap;
  border: 2px solid transparent;
}

.category-tab:hover {
  background: #f8f9fa;
  transform: translateY(-2px);
}

.category-tab.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-color: transparent;
}

.category-tab span {
  font-weight: 500;
}

.search-bar {
  max-width: 600px;
}

.post-list {
  min-height: 400px;
}

.posts-grid {
  margin-top: 30px;
}

.posts-container {
  display: contents;
}

/* 列表过渡动画 */
.post-list-enter-active {
  animation: fadeInUp 0.6s ease-out;
}

.post-list-leave-active {
  animation: fadeOut 0.3s ease-in;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeOut {
  from {
    opacity: 1;
  }
  to {
    opacity: 0;
  }
}

.post-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
  border-radius: 16px;
  overflow: hidden;
}

.post-card:hover {
  transform: translateY(-8px) scale(1.02);
  box-shadow: 0 20px 40px rgba(102, 126, 234, 0.2);
}

.post-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
  transform: scaleX(0);
  transition: transform 0.3s ease;
}

.post-card:hover::before {
  transform: scaleX(1);
}

.post-main {
  padding: 24px;
}

.post-author {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
}

.author-info {
  flex: 1;
}

.author-name {
  font-weight: 600;
  font-size: 16px;
  color: #1a1a1a;
  margin-bottom: 4px;
}

.post-meta {
  display: flex;
  align-items: center;
  gap: 12px;
}

.post-time {
  font-size: 13px;
  color: #999;
}

.post-content {
  margin-bottom: 20px;
}

.post-title {
  margin: 0 0 12px 0;
  font-size: 20px;
  color: #1a1a1a;
  font-weight: 700;
  line-height: 1.4;
}

.post-summary {
  margin: 0;
  color: #666;
  font-size: 15px;
  line-height: 1.8;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  line-clamp: 3;
  -webkit-box-orient: vertical;
  margin-bottom: 12px;
}

.post-images {
  display: flex;
  gap: 8px;
  margin-top: 12px;
  position: relative;
}

.post-image {
  width: 120px;
  height: 120px;
  border-radius: 8px;
  cursor: pointer;
  transition: transform 0.3s;
}

.post-image:hover {
  transform: scale(1.05);
}

.more-images {
  width: 120px;
  height: 120px;
  border-radius: 8px;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 24px;
  font-weight: bold;
  cursor: pointer;
}

.post-actions {
  display: flex;
  gap: 32px;
  padding-top: 16px;
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
  transition: all 0.3s ease;
  background: #f8f9fa;
}

.action-btn:hover {
  background: #667eea;
  color: #fff;
  transform: translateY(-2px);
}

.action-btn.is-active {
  background: #667eea;
  color: #fff;
}

.action-btn .el-icon {
  font-size: 16px;
}

.action-btn .el-icon.is-filled {
  color: #ffd700;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 30px;
  padding-bottom: 30px;
}

/* 违规提示样式 */
:deep(.audit-fail-message) {
  white-space: pre-line;
  line-height: 1.6;
  max-width: 400px;
}

/* 图片上传提示 */
.upload-tip {
  font-size: 12px;
  color: #999;
  margin-top: 8px;
}
</style>
