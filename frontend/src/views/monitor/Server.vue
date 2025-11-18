<template>
  <div class="monitor-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <div class="header-icon">
            <el-icon :size="28"><Monitor /></el-icon>
          </div>
          <div class="header-text">
            <h2>服务器监控</h2>
            <p>实时监控系统运行状态</p>
          </div>
        </div>
        <div class="header-right">
          <div class="last-update" v-if="lastUpdateTime">
            <el-icon><Clock /></el-icon>
            <span>最后更新: {{ lastUpdateTime }}</span>
          </div>
          <el-button type="primary" @click="loadData" :icon="Refresh" :loading="loading">
            刷新数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 概览卡片 -->
    <el-row :gutter="20" class="overview-row">
      <el-col :span="6">
        <el-card class="overview-card cpu-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon :size="40"><Cpu /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-label">CPU使用率</div>
              <div class="card-value">{{ cpuUsage }}%</div>
            </div>
          </div>
          <el-progress :percentage="cpuUsage" :color="getProgressColor(cpuUsage)" :show-text="false" />
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card memory-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon :size="40"><Coin /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-label">内存使用率</div>
              <div class="card-value">{{ memoryUsage }}%</div>
            </div>
          </div>
          <el-progress :percentage="memoryUsage" :color="getProgressColor(memoryUsage)" :show-text="false" />
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card disk-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon :size="40"><Folder /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-label">磁盘使用率</div>
              <div class="card-value">{{ diskUsage }}%</div>
            </div>
          </div>
          <el-progress :percentage="diskUsage" :color="getProgressColor(diskUsage)" :show-text="false" />
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card cpu-count-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon :size="40"><Grid /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-label">CPU核心数</div>
              <div class="card-value">{{ cpuCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 详细信息 -->
    <el-row :gutter="20">
      <!-- CPU信息 -->
      <el-col :span="12">
        <el-card class="detail-card">
          <template #header>
            <div class="card-header">
              <el-icon><Cpu /></el-icon>
              <span>CPU信息</span>
            </div>
          </template>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="核心数">{{ serverInfo.cpu?.cpuCount || 0 }}</el-descriptions-item>
            <el-descriptions-item label="系统负载">{{ serverInfo.cpu?.systemLoad || 0 }}</el-descriptions-item>
            <el-descriptions-item label="使用率">
              <el-tag :type="getUsageType(serverInfo.cpu?.cpuUsage || 0)">
                {{ serverInfo.cpu?.cpuUsage || 0 }}%
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>

      <!-- 内存信息 -->
      <el-col :span="12">
        <el-card class="detail-card">
          <template #header>
            <div class="card-header">
              <el-icon><Coin /></el-icon>
              <span>内存信息</span>
            </div>
          </template>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="总内存">{{ serverInfo.memory?.total || '0 MB' }}</el-descriptions-item>
            <el-descriptions-item label="已用内存">{{ serverInfo.memory?.used || '0 MB' }}</el-descriptions-item>
            <el-descriptions-item label="空闲内存">{{ serverInfo.memory?.free || '0 MB' }}</el-descriptions-item>
            <el-descriptions-item label="使用率">
              <el-tag :type="getUsageType(serverInfo.memory?.usage || 0)">
                {{ serverInfo.memory?.usage || 0 }}%
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <!-- JVM信息 -->
      <el-col :span="12">
        <el-card class="detail-card">
          <template #header>
            <div class="card-header">
              <el-icon><Platform /></el-icon>
              <span>JVM信息</span>
            </div>
          </template>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="名称">{{ serverInfo.jvm?.name || '-' }}</el-descriptions-item>
            <el-descriptions-item label="版本">{{ serverInfo.jvm?.version || '-' }}</el-descriptions-item>
            <el-descriptions-item label="供应商">{{ serverInfo.jvm?.vendor || '-' }}</el-descriptions-item>
            <el-descriptions-item label="运行时长">{{ serverInfo.jvm?.uptime || '-' }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>

      <!-- 磁盘信息 -->
      <el-col :span="12">
        <el-card class="detail-card">
          <template #header>
            <div class="card-header">
              <el-icon><Folder /></el-icon>
              <span>磁盘信息</span>
            </div>
          </template>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="总空间">{{ serverInfo.disk?.total || '0 GB' }}</el-descriptions-item>
            <el-descriptions-item label="已用空间">{{ serverInfo.disk?.used || '0 GB' }}</el-descriptions-item>
            <el-descriptions-item label="空闲空间">{{ serverInfo.disk?.free || '0 GB' }}</el-descriptions-item>
            <el-descriptions-item label="使用率">
              <el-tag :type="getUsageType(serverInfo.disk?.usage || 0)">
                {{ serverInfo.disk?.usage || 0 }}%
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>

    <!-- 系统信息 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card class="detail-card">
          <template #header>
            <div class="card-header">
              <el-icon><Setting /></el-icon>
              <span>系统信息</span>
            </div>
          </template>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="操作系统">{{ serverInfo.system?.osName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="系统架构">{{ serverInfo.system?.osArch || '-' }}</el-descriptions-item>
            <el-descriptions-item label="系统版本">{{ serverInfo.system?.osVersion || '-' }}</el-descriptions-item>
            <el-descriptions-item label="用户名">{{ serverInfo.system?.userName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="用户目录" :span="2">{{ serverInfo.system?.userDir || '-' }}</el-descriptions-item>
            <el-descriptions-item label="Java路径" :span="2">{{ serverInfo.jvm?.home || '-' }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'
import { getServerInfo } from '@/api/monitor'

const loading = ref(false)
const serverInfo = ref({
  cpu: {},
  memory: {},
  jvm: {},
  disk: {},
  system: {}
})

const cpuUsage = ref(0)
const memoryUsage = ref(0)
const diskUsage = ref(0)
const cpuCount = ref(0)
const lastUpdateTime = ref('')

let timer = null

const loadData = async () => {
  loading.value = true
  try {
    const res = await getServerInfo()
    serverInfo.value = res.data
    
    // 使用平滑过渡更新概览数据
    const newCpuUsage = Math.round(res.data.cpu?.cpuUsage || 0)
    const newMemoryUsage = Math.round(res.data.memory?.usage || 0)
    const newDiskUsage = Math.round(res.data.disk?.usage || 0)
    
    // 平滑过渡动画
    animateValue(cpuUsage, newCpuUsage)
    animateValue(memoryUsage, newMemoryUsage)
    animateValue(diskUsage, newDiskUsage)
    
    cpuCount.value = res.data.cpu?.cpuCount || 0
    
    // 更新最后更新时间
    const now = new Date()
    lastUpdateTime.value = `${now.getHours().toString().padStart(2, '0')}:${now.getMinutes().toString().padStart(2, '0')}:${now.getSeconds().toString().padStart(2, '0')}`
  } catch (error) {
    ElMessage.error('获取服务器信息失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 数值平滑过渡动画
const animateValue = (ref, targetValue) => {
  const startValue = ref.value
  const diff = targetValue - startValue
  const duration = 500 // 动画持续时间（毫秒）
  const steps = 20 // 动画步数
  const stepValue = diff / steps
  const stepDuration = duration / steps
  
  let currentStep = 0
  const interval = setInterval(() => {
    currentStep++
    if (currentStep >= steps) {
      ref.value = targetValue
      clearInterval(interval)
    } else {
      ref.value = Math.round(startValue + stepValue * currentStep)
    }
  }, stepDuration)
}

const getProgressColor = (percentage) => {
  if (percentage < 60) {
    return '#67c23a'
  } else if (percentage < 80) {
    return '#e6a23c'
  } else {
    return '#f56c6c'
  }
}

const getUsageType = (usage) => {
  if (usage < 60) {
    return 'success'
  } else if (usage < 80) {
    return 'warning'
  } else {
    return 'danger'
  }
}

onMounted(() => {
  loadData()
  // 每3秒自动刷新，实现动态监控效果
  timer = setInterval(() => {
    loadData()
  }, 3000)
})

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
  }
})
</script>

<style scoped>
.monitor-container {
  padding: 0;
}

.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  padding: 30px;
  margin-bottom: 20px;
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.4);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.header-icon {
  width: 60px;
  height: 60px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  backdrop-filter: blur(10px);
}

.header-text h2 {
  color: #fff;
  font-size: 28px;
  margin: 0 0 5px 0;
  font-weight: 600;
}

.header-text p {
  color: rgba(255, 255, 255, 0.9);
  margin: 0;
  font-size: 14px;
}

.overview-row {
  margin-bottom: 20px;
}

.overview-card {
  border-radius: 12px;
  border: none;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s;
}

.overview-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.12);
}

.card-content {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 15px;
}

.card-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.cpu-card .card-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.memory-card .card-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.disk-card .card-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.cpu-count-card .card-icon {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.card-info {
  flex: 1;
}

.card-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}

.card-value {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
}

.detail-card {
  border-radius: 12px;
  border: none;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.card-header {
  display: flex;
  align-items: center;
  gap: 10px;
  font-weight: 600;
  font-size: 16px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.last-update {
  display: flex;
  align-items: center;
  gap: 8px;
  color: rgba(255, 255, 255, 0.9);
  font-size: 14px;
  background: rgba(255, 255, 255, 0.15);
  padding: 8px 16px;
  border-radius: 8px;
  backdrop-filter: blur(10px);
}

.last-update .el-icon {
  font-size: 16px;
}

:deep(.el-descriptions__label) {
  font-weight: 600;
}

/* 数值变化过渡动画 */
.card-value {
  transition: all 0.5s ease-in-out;
}

/* 进度条过渡动画 */
:deep(.el-progress__bar) {
  transition: all 0.5s ease-in-out;
}

:deep(.el-progress-bar__inner) {
  transition: width 0.5s ease-in-out !important;
}

/* 标签颜色过渡 */
:deep(.el-tag) {
  transition: all 0.3s ease;
}
</style>
