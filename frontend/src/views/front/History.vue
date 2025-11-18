<template>
  <div class="history-page">
    <div class="page-header">
      <h1>发展历程</h1>
      <p>回顾我们的成长足迹</p>
    </div>

    <div class="content-section">
      <el-timeline>
        <el-timeline-item
          v-for="event in timeline"
          :key="event.id"
          :timestamp="event.date"
          :type="event.type"
          :size="event.size"
          placement="top"
        >
          <el-card>
            <div class="timeline-content">
              <h3>{{ event.title }}</h3>
              <p>{{ event.description }}</p>
              <div class="event-tags" v-if="event.tags">
                <el-tag v-for="tag in event.tags" :key="tag" size="small">{{ tag }}</el-tag>
              </div>
            </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>

      <el-card class="stats-card">
        <template #header>
          <div class="card-header">
            <el-icon><TrendCharts /></el-icon>
            <span>发展数据</span>
          </div>
        </template>
        <div class="stats-grid">
          <div class="stat-item">
            <div class="stat-value">{{ stats.years }}</div>
            <div class="stat-label">发展年限</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ stats.members }}</div>
            <div class="stat-label">累计成员</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ stats.activities }}</div>
            <div class="stat-label">举办活动</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ stats.awards }}</div>
            <div class="stat-label">获得荣誉</div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { TrendCharts } from '@element-plus/icons-vue'

const timeline = ref([
  {
    id: 1,
    date: '2020-09',
    title: '社团成立',
    description: '在学校的支持下，社团正式成立，开始招募第一批成员',
    type: 'primary',
    size: 'large',
    tags: ['成立', '招新']
  },
  {
    id: 2,
    date: '2020-12',
    title: '首次大型活动',
    description: '成功举办首届校园文化节，吸引了500余名师生参与',
    type: 'success',
    tags: ['活动', '文化节']
  },
  {
    id: 3,
    date: '2021-03',
    title: '获得优秀社团称号',
    description: '凭借出色的表现，荣获学校"优秀社团"荣誉称号',
    type: 'warning',
    tags: ['荣誉', '优秀']
  },
  {
    id: 4,
    date: '2021-09',
    title: '成员突破100人',
    description: '社团规模不断扩大，正式成员数量突破100人',
    type: 'primary',
    tags: ['里程碑']
  },
  {
    id: 5,
    date: '2022-05',
    title: '启动管理系统',
    description: '自主开发的社团管理系统正式上线，实现信息化管理',
    type: 'success',
    size: 'large',
    tags: ['创新', '系统']
  },
  {
    id: 6,
    date: '2023-01',
    title: '对外交流合作',
    description: '与多所高校社团建立合作关系，开展联合活动',
    type: 'info',
    tags: ['合作', '交流']
  },
  {
    id: 7,
    date: '2024-06',
    title: '获得省级荣誉',
    description: '在省级社团评比中脱颖而出，获得"省级优秀社团"称号',
    type: 'warning',
    size: 'large',
    tags: ['荣誉', '省级']
  },
  {
    id: 8,
    date: '2025-01',
    title: '持续发展中',
    description: '社团继续秉承初心，为广大师生提供更优质的服务',
    type: 'primary',
    tags: ['未来']
  }
])

const stats = ref({
  years: '5+',
  members: '500+',
  activities: '200+',
  awards: '15+'
})
</script>

<style scoped>
.history-page {
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  text-align: center;
  padding: 40px 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  color: white;
  margin-bottom: 32px;
}

.page-header h1 {
  font-size: 36px;
  font-weight: 700;
  margin: 0 0 12px 0;
}

.page-header p {
  font-size: 16px;
  opacity: 0.9;
  margin: 0;
}

.content-section {
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.timeline-content h3 {
  font-size: 18px;
  color: #333;
  margin: 0 0 12px 0;
}

.timeline-content p {
  color: #666;
  line-height: 1.6;
  margin: 0 0 12px 0;
}

.event-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: 600;
  color: #667eea;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.stat-item {
  text-align: center;
  padding: 24px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e8eaf6 100%);
  border-radius: 12px;
  transition: all 0.3s ease;
}

.stat-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.2);
}

.stat-value {
  font-size: 36px;
  font-weight: 700;
  color: #667eea;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .page-header h1 {
    font-size: 28px;
  }
}
</style>
