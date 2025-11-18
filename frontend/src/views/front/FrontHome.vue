<template>
  <div class="front-home">
    <!-- 动态背景装饰 -->
    <div class="bg-decoration">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
    </div>

    <!-- Hero Banner -->
    <div class="hero-banner">
      <div class="hero-content">
        <div class="hero-text">
          <h1 class="hero-title">
            Hi, {{ userStore.userInfo.nickname || userStore.userInfo.realName }}! 👋
          </h1>
          <p class="hero-subtitle">{{ greeting }}，{{ getMotivationalQuote() }}</p>
          <div class="hero-actions">
            <el-button type="primary" size="large" round @click="$router.push('/front/activities')">
              <el-icon><Calendar /></el-icon>
              <span>探索活动</span>
            </el-button>
            <el-button size="large" round @click="$router.push('/front/community')">
              <el-icon><ChatDotRound /></el-icon>
              <span>社区动态</span>
            </el-button>
          </div>
        </div>
        <div class="hero-image">
          <img src="https://images.unsplash.com/photo-1523240795612-9a054b0db644?w=800" alt="Campus" />
        </div>
      </div>
      <div class="hero-stats">
        <div class="stat-card" v-for="(stat, index) in heroStats" :key="index">
          <div class="stat-icon" :style="{ background: stat.color }">
            <el-icon :size="24"><component :is="stat.icon" /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stat.value }}</div>
            <div class="stat-label">{{ stat.label }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 快捷入口 -->
    <div class="quick-access">
      <h2 class="section-title">快速开始</h2>
      <div class="quick-grid">
        <div class="quick-card" v-for="(item, index) in quickActions" :key="index" @click="$router.push(item.path)">
          <div class="quick-icon" :style="{ background: item.gradient }">
            <el-icon :size="32"><component :is="item.icon" /></el-icon>
          </div>
          <h3>{{ item.title }}</h3>
          <p>{{ item.desc }}</p>
          <div class="quick-arrow">
            <el-icon><ArrowRight /></el-icon>
          </div>
        </div>
      </div>
    </div>

    <!-- 内容展示区 -->
    <div class="content-section">
      <el-row :gutter="24">
        <!-- 最新活动 -->
        <el-col :span="12">
          <div class="section-header">
            <div class="header-left">
              <div class="header-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%)">
                <el-icon><Calendar /></el-icon>
              </div>
              <h3>最新活动</h3>
            </div>
            <el-button text class="more-btn" @click="$router.push('/front/activities')">
              查看更多 <el-icon><ArrowRight /></el-icon>
            </el-button>
          </div>
          
          <div class="modern-card" v-loading="loadingActivities">
            <div v-if="recentActivities.length" class="activity-grid">
              <div v-for="activity in recentActivities.slice(0, 3)" :key="activity.id" 
                   class="modern-activity-item" 
                   @click="viewActivity(activity)">
                <div class="activity-badge" :style="{ background: getActivityColor(activity.activityType) }">
                  {{ activity.activityType }}
                </div>
                <h4>{{ activity.activityName }}</h4>
                <p>{{ activity.description }}</p>
                <div class="activity-footer">
                  <span class="activity-date">
                    <el-icon><Clock /></el-icon>
                    {{ formatDate(activity.startTime) }}
                  </span>
                  <el-icon class="arrow-icon"><ArrowRight /></el-icon>
                </div>
              </div>
            </div>
            <el-empty v-else description="暂无活动" :image-size="80" />
          </div>
        </el-col>
        
        <!-- 热门帖子 -->
        <el-col :span="12">
          <div class="section-header">
            <div class="header-left">
              <div class="header-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%)">
                <el-icon><ChatDotRound /></el-icon>
              </div>
              <h3>热门帖子</h3>
            </div>
            <el-button text class="more-btn" @click="$router.push('/front/community')">
              查看更多 <el-icon><ArrowRight /></el-icon>
            </el-button>
          </div>
          
          <div class="modern-card" v-loading="loadingPosts">
            <div v-if="hotPosts.length" class="post-grid">
              <div v-for="post in hotPosts.slice(0, 5)" :key="post.id" 
                   class="modern-post-item" 
                   @click="viewPost(post)">
                <div class="post-content">
                  <h4>{{ post.title }}</h4>
                  <div class="post-info">
                    <span class="author">
                      <el-avatar :size="20" :src="getFileUrl(post.authorAvatar)">
                        <el-icon><User /></el-icon>
                      </el-avatar>
                      {{ post.authorName || '匿名' }}
                    </span>
                    <div class="post-stats">
                      <span><el-icon><View /></el-icon>{{ post.viewCount || 0 }}</span>
                      <span><el-icon><ChatLineRound /></el-icon>{{ post.commentCount || 0 }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <el-empty v-else description="暂无帖子" :image-size="80" />
          </div>
        </el-col>
      </el-row>

    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { 
  Edit, Calendar, Clock, User, 
  ChatDotRound, ChatLineRound, View, Sunny, Document, Star,
  ArrowRight, TrophyBase, Histogram
} from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { getActivityList } from '@/api/activity'
import { getPostList } from '@/api/community'
import { ElMessage } from 'element-plus'
import { getFileUrl } from '@/utils/file'

const router = useRouter()
const userStore = useUserStore()
const currentTime = ref('')
const greeting = ref('')

// Hero统计数据
const heroStats = ref([
  { icon: Calendar, value: '0', label: '进行中活动', color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' },
  { icon: ChatDotRound, value: '0', label: '社区帖子', color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)' },
  { icon: Star, value: '0', label: '我的报名', color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)' },
  { icon: TrophyBase, value: '0', label: '我的积分', color: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)' }
])

// 快捷操作
const quickActions = ref([
  {
    icon: ChatDotRound,
    title: '发布动态',
    desc: '分享你的想法和生活',
    path: '/front/community',
    gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
  },
  {
    icon: Calendar,
    title: '报名活动',
    desc: '参与精彩校园活动',
    path: '/front/activities',
    gradient: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)'
  },
  {
    icon: User,
    title: '个人中心',
    desc: '查看和管理个人信息',
    path: '/front/profile',
    gradient: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)'
  },
  {
    icon: ChatLineRound,
    title: '私信聊天',
    desc: '与好友即时交流',
    path: '/front/chat',
    gradient: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)'
  }
])

// 统计数据
const stats = reactive({
  totalActivities: 0,
  totalPosts: 0,
  totalMembers: 0
})

// 数据列表
const recentActivities = ref([])
const hotPosts = ref([])

// 加载状态
const loadingActivities = ref(false)
const loadingPosts = ref(false)

// 获取励志语录
const getMotivationalQuote = () => {
  const quotes = [
    '让我们一起创造美好回忆',
    '每一次参与都是成长的机会',
    '在这里遇见更好的自己',
    '青春不散场，梦想在路上',
    '用热情点亮每一天',
    '让社团生活更精彩'
  ]
  return quotes[Math.floor(Math.random() * quotes.length)]
}

// 加载最新活动
const loadActivities = async () => {
  loadingActivities.value = true
  try {
    const res = await getActivityList({ pageNum: 1, pageSize: 5, status: '1' })
    recentActivities.value = res.data.records || []
    stats.totalActivities = res.data.total || 0
    heroStats.value[0].value = res.data.total || 0
  } catch (error) {
    console.error('加载活动失败:', error)
  } finally {
    loadingActivities.value = false
  }
}

// 加载热门帖子
const loadPosts = async () => {
  loadingPosts.value = true
  try {
    const res = await getPostList({ pageNum: 1, pageSize: 5, auditStatus: '1' })
    hotPosts.value = res.data.records || []
    stats.totalPosts = res.data.total || 0
    heroStats.value[1].value = res.data.total || 0
  } catch (error) {
    console.error('加载帖子失败:', error)
  } finally {
    loadingPosts.value = false
  }
}


// 查看活动详情
const viewActivity = (activity) => {
  ElMessage.info('活动详情功能开发中')
}

// 查看帖子详情
const viewPost = (post) => {
  ElMessage.info('帖子详情功能开发中')
}

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return `${date.getMonth() + 1}月${date.getDate()}日 ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
}

// 获取活动类型标签
const getActivityTypeTag = (type) => {
  const map = {
    '学术讲座': 'primary',
    '文体活动': 'success',
    '社会实践': 'warning',
    '志愿服务': 'danger'
  }
  return map[type] || 'info'
}

// 获取活动颜色
const getActivityColor = (type) => {
  const map = {
    '学术讲座': 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    '文体活动': 'linear-gradient(135deg, #11998e 0%, #38ef7d 100%)',
    '社会实践': 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)',
    '志愿服务': 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)'
  }
  return map[type] || 'linear-gradient(135deg, #999 0%, #666 100%)'
}

// 更新时间
const updateTime = () => {
  const now = new Date()
  const hours = now.getHours()
  const minutes = now.getMinutes().toString().padStart(2, '0')
  currentTime.value = `${hours}:${minutes}`
  
  // 根据时间设置问候语
  if (hours < 6) {
    greeting.value = '凌晨好'
  } else if (hours < 9) {
    greeting.value = '早上好'
  } else if (hours < 12) {
    greeting.value = '上午好'
  } else if (hours < 14) {
    greeting.value = '中午好'
  } else if (hours < 18) {
    greeting.value = '下午好'
  } else if (hours < 22) {
    greeting.value = '晚上好'
  } else {
    greeting.value = '夜深了'
  }
}

let timer = null

onMounted(() => {
  updateTime()
  timer = setInterval(updateTime, 60000)
  
  // 加载数据
  loadActivities()
  loadPosts()
  
  // 模拟成员数据
  stats.totalMembers = 156
})

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
  }
})
</script>

<style scoped>
.front-home {
  padding: 0;
  position: relative;
  min-height: 100vh;
  background: #f8f9fa;
}

/* Hero Banner */
.hero-banner {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 24px;
  padding: 60px;
  margin-bottom: 40px;
  position: relative;
  overflow: hidden;
}

.hero-banner::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 70%);
  animation: rotate 20s linear infinite;
}

.hero-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  z-index: 1;
  margin-bottom: 40px;
}

.hero-text {
  flex: 1;
  color: #fff;
}

.hero-title {
  font-size: 48px;
  font-weight: 800;
  margin: 0 0 16px 0;
  line-height: 1.2;
}

.hero-subtitle {
  font-size: 20px;
  opacity: 0.95;
  margin: 0 0 32px 0;
}

.hero-actions {
  display: flex;
  gap: 16px;
}

.hero-actions .el-button {
  padding: 16px 32px;
  font-size: 16px;
  font-weight: 600;
}

.hero-image {
  width: 400px;
  height: 300px;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0,0,0,0.3);
}

.hero-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.hero-stats {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  position: relative;
  z-index: 1;
}

.hero-stats .stat-card {
  background: rgba(255,255,255,0.15);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  transition: all 0.3s ease;
  border: 1px solid rgba(255,255,255,0.2);
}

.hero-stats .stat-card:hover {
  background: rgba(255,255,255,0.25);
  transform: translateY(-4px);
}

.hero-stats .stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.hero-stats .stat-content {
  flex: 1;
  color: #fff;
}

.hero-stats .stat-value {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 4px;
}

.hero-stats .stat-label {
  font-size: 13px;
  opacity: 0.9;
}

/* 背景装饰 */
.bg-decoration {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 0;
  overflow: hidden;
}

.circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.1;
}

.circle-1 {
  width: 300px;
  height: 300px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  top: -100px;
  right: -100px;
  animation: float 20s ease-in-out infinite;
}

.circle-2 {
  width: 200px;
  height: 200px;
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  bottom: 100px;
  left: -50px;
  animation: float 15s ease-in-out infinite reverse;
}

.circle-3 {
  width: 150px;
  height: 150px;
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  top: 50%;
  right: 10%;
  animation: float 18s ease-in-out infinite;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-30px) rotate(180deg);
  }
}

/* 欢迎卡片 */
.welcome-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: #fff;
  position: relative;
  z-index: 1;
  overflow: hidden;
}

.welcome-card::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 0%, transparent 70%);
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

.welcome-card :deep(.el-card__body) {
  padding: 50px;
  position: relative;
  z-index: 1;
}

.welcome-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-text h1 {
  font-size: 36px;
  margin-bottom: 16px;
  font-weight: 700;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.welcome-text p {
  font-size: 18px;
  opacity: 0.95;
  margin-bottom: 30px;
}

/* 统计信息 */
.welcome-stats {
  display: flex;
  gap: 40px;
  margin-top: 20px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.stat-number {
  font-size: 28px;
  font-weight: 700;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
}

/* 动画 */
.animate-fade-in {
  animation: fadeIn 0.8s ease-out;
}

.animate-slide-right {
  animation: slideRight 0.8s ease-out;
  animation-fill-mode: both;
}

.animate-slide-right.delay-1 {
  animation-delay: 0.2s;
}

.animate-slide-right.delay-2 {
  animation-delay: 0.4s;
}

.animate-float {
  animation: floatIcon 3s ease-in-out infinite;
}

.animate-scale {
  animation: scaleIn 0.6s ease-out;
  animation-fill-mode: both;
}

.animate-scale.delay-3 {
  animation-delay: 0.6s;
}

.animate-scale.delay-4 {
  animation-delay: 0.8s;
}

.animate-scale.delay-5 {
  animation-delay: 1s;
}

.animate-slide-up {
  animation: slideUp 0.8s ease-out;
  animation-fill-mode: both;
}

.animate-slide-up.delay-6 {
  animation-delay: 1.2s;
}

.animate-slide-up.delay-7 {
  animation-delay: 1.4s;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideRight {
  from {
    opacity: 0;
    transform: translateX(-30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes floatIcon {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-20px);
  }
}

@keyframes scaleIn {
  from {
    opacity: 0;
    transform: scale(0.8);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 快捷入口 */
.quick-access {
  margin-bottom: 40px;
}

.section-title {
  font-size: 28px;
  font-weight: 700;
  color: #1a1a1a;
  margin: 0 0 24px 0;
}

.quick-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.quick-card {
  background: #fff;
  border-radius: 20px;
  padding: 32px 24px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
  border: 2px solid transparent;
}

.quick-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: var(--gradient);
  opacity: 0;
  transition: opacity 0.3s;
}

.quick-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 16px 48px rgba(0,0,0,0.12);
  border-color: rgba(102, 126, 234, 0.2);
}

.quick-card:hover::before {
  opacity: 1;
}

.quick-icon {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  margin-bottom: 20px;
}

.quick-card h3 {
  font-size: 18px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0 0 8px 0;
}

.quick-card p {
  font-size: 14px;
  color: #666;
  margin: 0 0 16px 0;
  line-height: 1.6;
}

.quick-arrow {
  color: #999;
  transition: all 0.3s;
}

.quick-card:hover .quick-arrow {
  color: #667eea;
  transform: translateX(4px);
}

.feature-content {
  text-align: center;
  padding: 30px 20px;
}

/* 图标包装器 */
.icon-wrapper {
  width: 80px;
  height: 80px;
  margin: 0 auto 20px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.icon-wrapper.purple {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.icon-wrapper.green {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
  color: #fff;
}

.icon-wrapper.orange {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
  color: #fff;
}

.feature-card:hover .icon-wrapper {
  transform: scale(1.1) rotate(5deg);
}

.feature-content h3 {
  margin: 0 0 12px;
  font-size: 20px;
  color: #333;
  font-weight: 600;
}

.feature-content p {
  color: #666;
  margin-bottom: 20px;
  font-size: 14px;
  line-height: 1.6;
}

.feature-btn {
  color: #667eea;
  font-weight: 500;
  transition: all 0.3s ease;
}

.feature-btn:hover {
  transform: translateX(5px);
  color: #764ba2;
}

/* 卡片头部 */
.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  font-size: 16px;
  color: #333;
}

.card-header .el-icon {
  color: #667eea;
}

/* 活动卡片 */
.activity-card {
  border: none;
}

.activity-card :deep(.el-timeline-item__timestamp) {
  color: #999;
  font-size: 13px;
}

.activity-card :deep(.el-timeline-item__node) {
  background: #667eea;
}

.activity-card :deep(.el-timeline-item__wrapper h4) {
  color: #333;
  font-size: 15px;
  margin-bottom: 8px;
}

.activity-card :deep(.el-timeline-item__wrapper p) {
  color: #666;
  font-size: 14px;
}

/* 快速操作 */
.quick-card {
  border: none;
}

.quick-actions {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 8px 0;
}

.quick-btn {
  width: 100%;
  height: 50px;
  border: none;
  border-radius: 12px;
  font-size: 15px;
  font-weight: 500;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.purple-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.purple-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}

.green-btn {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
  color: #fff;
}

.green-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(17, 153, 142, 0.4);
}

.blue-btn {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: #fff;
}

.blue-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(79, 172, 254, 0.4);
}

/* 统计卡片 */
.stat-card {
  border: none;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.2);
}

.stat-card-content {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 8px 0;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.stat-icon.purple {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.green {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
}

.stat-icon.orange {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}

.stat-icon.blue {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #333;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 13px;
  color: #999;
}

/* 内容展示区 */
.content-section {
  margin-top: 40px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-icon {
  width: 40px;
  height: 40px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.section-header h3 {
  margin: 0;
  font-size: 20px;
  font-weight: 700;
  color: #1a1a1a;
}

.more-btn {
  color: #667eea;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 4px;
}

.more-btn:hover {
  color: #764ba2;
}

/* 现代化卡片 */
.modern-card {
  background: #fff;
  border-radius: 20px;
  padding: 24px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.06);
  transition: all 0.3s ease;
  min-height: 400px;
}

.modern-card:hover {
  box-shadow: 0 8px 32px rgba(0,0,0,0.12);
}

/* 活动网格 */
.activity-grid {
  display: grid;
  gap: 16px;
}

.modern-activity-item {
  padding: 20px;
  border-radius: 16px;
  background: #f8f9fa;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.modern-activity-item::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 4px;
  background: var(--activity-color);
  opacity: 0;
  transition: opacity 0.3s;
}

.modern-activity-item:hover {
  background: #fff;
  box-shadow: 0 4px 16px rgba(0,0,0,0.08);
  transform: translateX(4px);
}

.modern-activity-item:hover::before {
  opacity: 1;
}

.activity-badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
  color: #fff;
  margin-bottom: 12px;
}

.modern-activity-item h4 {
  margin: 0 0 8px 0;
  font-size: 16px;
  font-weight: 600;
  color: #1a1a1a;
}

.modern-activity-item p {
  margin: 0 0 12px 0;
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
}

.activity-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.activity-date {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: #999;
}

.arrow-icon {
  color: #999;
  transition: all 0.3s;
}

.modern-activity-item:hover .arrow-icon {
  color: #667eea;
  transform: translateX(4px);
}

.activity-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  margin-bottom: 12px;
  border-radius: 8px;
  background: #f8f9fa;
  cursor: pointer;
  transition: all 0.3s ease;
}

.activity-item:hover {
  background: #e8eaf6;
  transform: translateX(5px);
}

.activity-info {
  flex: 1;
}

.activity-info h4 {
  margin: 0 0 8px 0;
  font-size: 16px;
  color: #333;
  font-weight: 600;
}

.activity-desc {
  margin: 0 0 8px 0;
  font-size: 14px;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.activity-meta {
  display: flex;
  align-items: center;
  gap: 12px;
}

.activity-time {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: #999;
}

/* 帖子网格 */
.post-grid {
  display: grid;
  gap: 12px;
}

.modern-post-item {
  padding: 16px;
  border-radius: 12px;
  background: #f8f9fa;
  cursor: pointer;
  transition: all 0.3s ease;
  border-left: 3px solid transparent;
}

.modern-post-item:hover {
  background: #fff;
  border-left-color: #f093fb;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  transform: translateX(4px);
}

.post-content h4 {
  margin: 0 0 12px 0;
  font-size: 15px;
  font-weight: 600;
  color: #1a1a1a;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.post-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.post-info .author {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: #666;
}

.post-stats {
  display: flex;
  gap: 16px;
  font-size: 13px;
  color: #999;
}

.post-stats span {
  display: flex;
  align-items: center;
  gap: 4px;
}

/* 志愿服务网格 */
.volunteer-grid {
  display: grid;
  gap: 16px;
}

.modern-volunteer-item {
  padding: 20px;
  border-radius: 16px;
  background: #f8f9fa;
  transition: all 0.3s ease;
}

.modern-volunteer-item:hover {
  background: #fff;
  box-shadow: 0 4px 16px rgba(0,0,0,0.08);
}

.volunteer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.volunteer-header h4 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #1a1a1a;
}

.modern-volunteer-item p {
  margin: 0 0 12px 0;
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
}

.volunteer-info {
  display: flex;
  gap: 16px;
  font-size: 13px;
  color: #999;
}

.volunteer-info span {
  display: flex;
  align-items: center;
  gap: 4px;
}

/* 快速操作 */
.quick-operations {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  padding: 20px;
}

.operation-btn {
  padding: 32px 24px;
  border-radius: 16px;
  color: #fff;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  text-align: center;
}

.operation-btn:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(0,0,0,0.2);
}

.operation-btn span {
  font-size: 16px;
  font-weight: 600;
}

/* 快速操作按钮 */
.orange-btn {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
  color: #fff;
}

.orange-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(250, 112, 154, 0.4);
}

/* 响应式 */
@media (max-width: 1024px) {
  .hero-banner {
    padding: 40px;
  }
  
  .hero-title {
    font-size: 36px;
  }
  
  .hero-stats {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .quick-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .hero-banner {
    padding: 30px 20px;
  }
  
  .hero-content {
    flex-direction: column;
    text-align: center;
  }
  
  .hero-title {
    font-size: 28px;
  }
  
  .hero-subtitle {
    font-size: 16px;
  }
  
  .hero-image {
    width: 100%;
    height: 200px;
    margin-top: 24px;
  }
  
  .hero-stats {
    grid-template-columns: 1fr;
  }
  
  .quick-grid {
    grid-template-columns: 1fr;
  }
  
  .section-title {
    font-size: 24px;
  }
}
</style>
