<template>
  <div class="activity-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <div class="header-icon">
            <el-icon :size="28"><Tickets /></el-icon>
          </div>
          <div class="header-text">
            <h2>报名管理</h2>
            <p>管理活动报名信息</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stats-row">
      <el-col :span="6">
        <div class="stat-card stat-card-primary">
          <div class="stat-icon">
            <el-icon :size="32"><Tickets /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ total }}</div>
            <div class="stat-label">报名总数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card-warning">
          <div class="stat-icon">
            <el-icon :size="32"><Clock /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ pendingCount }}</div>
            <div class="stat-label">待审核</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card-success">
          <div class="stat-icon">
            <el-icon :size="32"><CircleCheck /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ approvedCount }}</div>
            <div class="stat-label">已通过</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card-info">
          <div class="stat-icon">
            <el-icon :size="32"><User /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ uniqueUsers }}</div>
            <div class="stat-label">报名用户</div>
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
        <el-form-item label="审核状态">
          <el-select 
            v-model="searchForm.auditStatus" 
            placeholder="请选择状态" 
            clearable
            style="width: 120px"
          >
            <el-option label="待审核" value="0" />
            <el-option label="已通过" value="1" />
            <el-option label="未通过" value="2" />
          </el-select>
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
        <el-table-column prop="auditStatus" label="审核状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag 
              :type="getStatusTag(row.auditStatus)" 
              size="small"
            >
              {{ getStatusText(row.auditStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="registrationTime" label="报名时间" width="160" align="center" />
        <el-table-column prop="createTime" label="创建时间" width="160" align="center" />
        <el-table-column label="操作" width="250" align="center" fixed="right">
          <template #default="{ row }">
            <el-button 
              v-if="row.auditStatus === '0'"
              type="success" 
              link 
              @click="handleAudit(row, '1')"
              :icon="CircleCheck"
            >
              通过
            </el-button>
            <el-button 
              v-if="row.auditStatus === '0'"
              type="warning" 
              link 
              @click="handleAudit(row, '2')"
              :icon="CircleClose"
            >
              拒绝
            </el-button>
            <el-button 
              type="danger" 
              link 
              @click="handleCancel(row)"
              :icon="Delete"
            >
              取消报名
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Search, Refresh, Delete, CircleCheck, CircleClose,
  Tickets, Clock, User
} from '@element-plus/icons-vue'
import { getRegistrationList, auditRegistration, cancelRegistration } from '@/api/activity'

// 搜索表单
const searchForm = reactive({
  activityName: '',
  userName: '',
  auditStatus: ''
})

// 表格数据
const tableData = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await getRegistrationList({
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
    userName: '',
    auditStatus: ''
  })
  handleSearch()
}

// 审核
const handleAudit = async (row, status) => {
  try {
    await auditRegistration(row.id, status)
    ElMessage.success('审核成功')
    loadData()
  } catch (error) {
    console.error(error)
    ElMessage.error('审核失败')
  }
}

// 取消报名
const handleCancel = (row) => {
  ElMessageBox.confirm('确定要取消该报名吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await cancelRegistration(row.id)
      ElMessage.success('取消成功')
      loadData()
    } catch (error) {
      console.error(error)
      ElMessage.error('取消失败')
    }
  })
}

// 获取状态文本
const getStatusText = (status) => {
  const map = {
    '0': '待审核',
    '1': '已通过',
    '2': '未通过',
    '3': '已取消'
  }
  return map[status] || '未知'
}

// 获取状态标签
const getStatusTag = (status) => {
  const map = {
    '0': 'warning',
    '1': 'success',
    '2': 'danger',
    '3': 'info'
  }
  return map[status] || ''
}

// 统计数据
const pendingCount = computed(() => {
  return tableData.value.filter(item => item.auditStatus === '0').length
})

const approvedCount = computed(() => {
  return tableData.value.filter(item => item.auditStatus === '1').length
})

const uniqueUsers = computed(() => {
  const users = new Set(tableData.value.map(item => item.userId))
  return users.size
})

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
@import '@/styles/common-page.scss';
</style>
