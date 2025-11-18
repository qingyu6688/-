<template>
  <div class="activity-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <div class="header-icon">
            <el-icon :size="28"><CircleCheck /></el-icon>
          </div>
          <div class="header-text">
            <h2>签到管理</h2>
            <p>管理活动签到记录</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stats-row">
      <el-col :span="6">
        <div class="stat-card stat-card-primary">
          <div class="stat-icon">
            <el-icon :size="32"><CircleCheck /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ total }}</div>
            <div class="stat-label">签到总数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card-warning">
          <div class="stat-icon">
            <el-icon :size="32"><Clock /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ lateCount }}</div>
            <div class="stat-label">迟到人数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card-danger">
          <div class="stat-icon">
            <el-icon :size="32"><Warning /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ earlyLeaveCount }}</div>
            <div class="stat-label">早退人数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card-success">
          <div class="stat-icon">
            <el-icon :size="32"><Select /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ normalCount }}</div>
            <div class="stat-label">正常签到</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-card class="search-card">
      <div class="search-header">
        <el-icon><Search /></el-icon>
        <span>筛选查询</span>
      </div>
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="活动名称">
          <el-input 
            v-model="searchForm.activityName" 
            placeholder="请输入活动名称" 
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="用户姓名">
          <el-input 
            v-model="searchForm.userName" 
            placeholder="请输入用户姓名" 
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch" :icon="Search">
            搜索
          </el-button>
          <el-button @click="handleReset" :icon="Refresh">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格区域 -->
    <el-card class="table-card">
      <el-table 
        :data="tableData" 
        style="width: 100%"
        v-loading="loading"
        :header-cell-style="{ 
          background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)', 
          color: '#fff', 
          fontWeight: '600',
          height: '50px'
        }"
      >
        <el-table-column type="index" label="#" width="70" align="center" />
        <el-table-column prop="activityId" label="活动ID" width="100" align="center" />
        <el-table-column prop="userId" label="用户ID" width="100" align="center" />
        <el-table-column prop="signInTime" label="签到时间" width="160" align="center" />
        <el-table-column prop="signOutTime" label="签退时间" width="160" align="center">
          <template #default="{ row }">
            {{ row.signOutTime || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="状态标记" width="200" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.isLate === 1" type="warning" size="small" style="margin-right: 5px">
              迟到
            </el-tag>
            <el-tag v-if="row.isEarlyLeave === 1" type="danger" size="small">
              早退
            </el-tag>
            <el-tag v-if="row.isLate === 0 && row.isEarlyLeave === 0" type="success" size="small">
              正常
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="location" label="签到地点" width="150" show-overflow-tooltip>
          <template #default="{ row }">
            {{ row.location || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" align="center" />
        <el-table-column label="操作" width="120" align="center" fixed="right">
          <template #default="{ row }">
            <el-button 
              type="info" 
              link 
              @click="handleView(row)"
              :icon="View"
            >
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadData"
        @current-change="loadData"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <!-- 详情弹窗 -->
    <el-dialog 
      v-model="detailVisible" 
      title="签到详情"
      width="700px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="签到ID">{{ detailData.id }}</el-descriptions-item>
        <el-descriptions-item label="活动ID">{{ detailData.activityId }}</el-descriptions-item>
        <el-descriptions-item label="用户ID">{{ detailData.userId }}</el-descriptions-item>
        <el-descriptions-item label="签到时间">{{ detailData.signInTime }}</el-descriptions-item>
        <el-descriptions-item label="签退时间">{{ detailData.signOutTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="签到地点">{{ detailData.location || '-' }}</el-descriptions-item>
        <el-descriptions-item label="是否迟到">
          <el-tag :type="detailData.isLate === 1 ? 'warning' : 'success'" size="small">
            {{ detailData.isLate === 1 ? '是' : '否' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="是否早退">
          <el-tag :type="detailData.isEarlyLeave === 1 ? 'danger' : 'success'" size="small">
            {{ detailData.isEarlyLeave === 1 ? '是' : '否' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2">{{ detailData.createTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { 
  Search, Refresh, View, CircleCheck, Clock, Warning, Select
} from '@element-plus/icons-vue'
import { getSignInList } from '@/api/activity'

// 搜索表单
const searchForm = reactive({
  activityName: '',
  userName: ''
})

// 表格数据
const tableData = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 弹窗相关
const detailVisible = ref(false)
const detailData = ref({})

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await getSignInList({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      ...searchForm
    })
    tableData.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error(error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pageNum.value = 1
  loadData()
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, {
    activityName: '',
    userName: ''
  })
  handleSearch()
}

// 查看详情
const handleView = (row) => {
  detailData.value = { ...row }
  detailVisible.value = true
}

// 统计数据
const lateCount = computed(() => {
  return tableData.value.filter(item => item.isLate === 1).length
})

const earlyLeaveCount = computed(() => {
  return tableData.value.filter(item => item.isEarlyLeave === 1).length
})

const normalCount = computed(() => {
  return tableData.value.filter(item => item.isLate === 0 && item.isEarlyLeave === 0).length
})

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
@import '@/styles/common-page.scss';

.stat-card-danger .stat-icon {
  background: linear-gradient(135deg, #f56c6c 0%, #ff8080 100%);
}
</style>
