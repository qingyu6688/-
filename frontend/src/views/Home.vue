<template>
  <div class="home-container">
    <!-- 核心数据统计 -->
    <el-row :gutter="20" v-loading="loading">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
              <el-icon :size="30"><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-title">成员总数</div>
              <div class="stat-value">{{ dashboardData.totalMembers || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
              <el-icon :size="30"><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-title">帖子总数</div>
              <div class="stat-value">{{ dashboardData.totalPosts || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
              <el-icon :size="30"><Calendar /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-title">活动总数</div>
              <div class="stat-value">{{ dashboardData.totalActivities || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);">
              <el-icon :size="30"><ChatDotRound /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-title">评论总数</div>
              <div class="stat-value">{{ dashboardData.totalComments || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>


    <!-- 待处理事项 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <el-icon><Bell /></el-icon>
              <span style="margin-left: 8px;">待处理事项</span>
            </div>
          </template>
          <div class="pending-tasks">
            <div class="task-item">
              <el-icon color="#e6a23c"><Document /></el-icon>
              <span class="task-label">待审核帖子</span>
              <el-tag type="warning">{{ dashboardData.pendingTasks?.posts || 0 }}</el-tag>
            </div>
            <div class="task-item">
              <el-icon color="#409eff"><Calendar /></el-icon>
              <span class="task-label">待审核活动</span>
              <el-tag type="primary">{{ dashboardData.pendingTasks?.activities || 0 }}</el-tag>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 最近活动 -->
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <el-icon><TrendCharts /></el-icon>
              <span style="margin-left: 8px;">最近活动</span>
            </div>
          </template>
          <div class="recent-activities">
            <div 
              v-for="(activity, index) in dashboardData.recentActivities" 
              :key="index"
              class="activity-item"
            >
              <div class="activity-name">{{ activity.activityName }}</div>
              <div class="activity-time">{{ formatDate(activity.startTime) }}</div>
            </div>
            <el-empty v-if="!dashboardData.recentActivities?.length" description="暂无活动" :image-size="80" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 系统信息 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <el-icon><InfoFilled /></el-icon>
              <span style="margin-left: 8px;">系统信息</span>
            </div>
          </template>
          <div class="welcome-content">
            <h2>🎉 高校社团活动综合管理系统</h2>
            <el-row :gutter="20">
              <el-col :span="12">
                <h3>核心功能</h3>
                <ul>
                  <li>✅ 成员信息管理：档案、经历、荣誉记录</li>
                  <li>✅ 互动交流：帖子、评论、板块、通知、私信</li>
                  <li>✅ 活动管理：活动发布、报名、签到、评价、相册</li>
                  <li>✅ 统计分析：数据概览、活动分析、成员分析</li>
                  <li>✅ 系统管理：用户、角色、权限、日志</li>
                </ul>
              </el-col>
              <el-col :span="12">
                <h3>技术栈</h3>
                <ul>
                  <li>前端：Vue 3 + Element Plus + Axios + Vue Router</li>
                  <li>后端：Spring Boot 3 + MyBatis-Plus + Swagger</li>
                  <li>数据库：MySQL 8.0</li>
                  <li>工具：Lombok + Hutool</li>
                </ul>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { 
  User, Document, Calendar, ChatDotRound, Bell, 
  TrendCharts, InfoFilled 
} from '@element-plus/icons-vue'
import { getDashboard } from '@/api/statistics'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const dashboardData = ref({
  totalMembers: 0,
  totalPosts: 0,
  totalActivities: 0,
  totalComments: 0,
  pendingTasks: {
    posts: 0,
    activities: 0
  },
  recentActivities: []
})

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await getDashboard()
    dashboardData.value = res.data
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.home-container {
  padding: 0;
}

.stat-card {
  cursor: pointer;
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.stat-info {
  flex: 1;
}

.stat-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.card-header {
  font-weight: bold;
  font-size: 16px;
}

.welcome-content {
  line-height: 1.8;
}

.welcome-content h2 {
  margin-top: 20px;
  margin-bottom: 10px;
  color: #409eff;
}

.welcome-content ul {
  padding-left: 20px;
}

.welcome-content li {
  margin-bottom: 8px;
  color: #606266;
}

.finance-card {
  cursor: pointer;
  transition: all 0.3s;
}

.finance-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.finance-item {
  text-align: center;
  padding: 10px 0;
}

.finance-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}

.finance-value {
  font-size: 28px;
  font-weight: bold;
}

.finance-value.income {
  color: #67c23a;
}

.finance-value.expense {
  color: #f56c6c;
}

.finance-value.balance {
  color: #409eff;
}

.pending-tasks {
  padding: 10px 0;
}

.task-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.task-item:last-child {
  border-bottom: none;
}

.task-label {
  flex: 1;
  margin-left: 10px;
  color: #606266;
}

.recent-activities {
  max-height: 200px;
  overflow-y: auto;
}

.activity-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-name {
  flex: 1;
  color: #303133;
  font-weight: 500;
}

.activity-time {
  color: #909399;
  font-size: 12px;
}

.welcome-content h3 {
  color: #667eea;
  margin-top: 15px;
  margin-bottom: 10px;
}
</style>
