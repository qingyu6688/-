<template>
  <div class="activity-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <div class="header-icon">
            <el-icon :size="28"><Star /></el-icon>
          </div>
          <div class="header-text">
            <h2>评价管理</h2>
            <p>管理活动评价反馈</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stats-row">
      <el-col :span="6">
        <div class="stat-card stat-card-primary">
          <div class="stat-icon">
            <el-icon :size="32"><Star /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ total }}</div>
            <div class="stat-label">评价总数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card-success">
          <div class="stat-icon">
            <el-icon :size="32"><StarFilled /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ averageRating }}</div>
            <div class="stat-label">平均评分</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card-warning">
          <div class="stat-icon">
            <el-icon :size="32"><Medal /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ fiveStarCount }}</div>
            <div class="stat-label">五星好评</div>
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
            <div class="stat-label">评价用户</div>
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
        <el-table-column prop="rating" label="评分" width="150" align="center">
          <template #default="{ row }">
            <el-rate 
              v-model="row.rating" 
              disabled 
              show-score 
              text-color="#ff9900"
            />
          </template>
        </el-table-column>
        <el-table-column prop="content" label="评价内容" min-width="300" show-overflow-tooltip />
        <el-table-column prop="tags" label="评价标签" width="200" show-overflow-tooltip>
          <template #default="{ row }">
            <span v-if="row.tags">
              <el-tag 
                v-for="(tag, index) in parseTagsArray(row.tags)" 
                :key="index" 
                size="small" 
                style="margin-right: 5px"
              >
                {{ tag }}
              </el-tag>
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="评价时间" width="160" align="center" />
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template #default="{ row }">
            <el-button 
              type="info" 
              link 
              @click="handleView(row)"
              :icon="View"
            >
              详情
            </el-button>
            <el-button 
              type="danger" 
              link 
              @click="handleDelete(row)"
              :icon="Delete"
            >
              删除
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
      title="评价详情"
      width="700px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="评价ID">{{ detailData.id }}</el-descriptions-item>
        <el-descriptions-item label="活动ID">{{ detailData.activityId }}</el-descriptions-item>
        <el-descriptions-item label="用户ID">{{ detailData.userId }}</el-descriptions-item>
        <el-descriptions-item label="评分">
          <el-rate 
            v-model="detailData.rating" 
            disabled 
            show-score 
            text-color="#ff9900"
          />
        </el-descriptions-item>
        <el-descriptions-item label="评价标签" :span="2">
          <span v-if="detailData.tags">
            <el-tag 
              v-for="(tag, index) in parseTagsArray(detailData.tags)" 
              :key="index" 
              size="small" 
              style="margin-right: 5px"
            >
              {{ tag }}
            </el-tag>
          </span>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="评价内容" :span="2">
          <div style="white-space: pre-wrap;">{{ detailData.content || '-' }}</div>
        </el-descriptions-item>
        <el-descriptions-item label="评价时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detailData.updateTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Search, Refresh, View, Delete, Star, StarFilled, Medal, User
} from '@element-plus/icons-vue'
import { getEvaluationList, deleteEvaluation } from '@/api/activity'

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
    const res = await getEvaluationList({
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

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该评价吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteEvaluation(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error(error)
      ElMessage.error('删除失败')
    }
  })
}

// 解析标签数组
const parseTagsArray = (tags) => {
  try {
    if (typeof tags === 'string') {
      return JSON.parse(tags)
    }
    return tags || []
  } catch (error) {
    return []
  }
}

// 统计数据
const averageRating = computed(() => {
  if (tableData.value.length === 0) return 0
  const totalRating = tableData.value.reduce((sum, item) => sum + (item.rating || 0), 0)
  return (totalRating / tableData.value.length).toFixed(1)
})

const fiveStarCount = computed(() => {
  return tableData.value.filter(item => item.rating === 5).length
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
