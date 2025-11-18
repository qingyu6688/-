<template>
  <div class="comment-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <div class="header-icon">
            <el-icon :size="28"><ChatLineRound /></el-icon>
          </div>
          <div class="header-text">
            <h2>评论管理</h2>
            <p>管理帖子评论和回复</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stats-row">
      <el-col :span="8">
        <div class="stat-card stat-card-primary">
          <div class="stat-icon">
            <el-icon :size="32"><ChatDotRound /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ total }}</div>
            <div class="stat-label">评论总数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card stat-card-success">
          <div class="stat-icon">
            <el-icon :size="32"><User /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ activeUsers }}</div>
            <div class="stat-label">活跃用户</div>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card stat-card-warning">
          <div class="stat-icon">
            <el-icon :size="32"><Clock /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ todayComments }}</div>
            <div class="stat-label">今日评论</div>
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
        <el-form-item label="评论内容">
          <el-input 
            v-model="searchForm.content" 
            placeholder="请输入评论内容" 
            clearable
            style="width: 250px"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select 
            v-model="searchForm.status" 
            placeholder="请选择状态" 
            clearable
            style="width: 120px"
          >
            <el-option label="正常" value="0" />
            <el-option label="隐藏" value="1" />
            <el-option label="删除" value="2" />
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
        <el-table-column prop="userName" label="评论人" width="120" align="center" />
        <el-table-column prop="content" label="评论内容" min-width="300" show-overflow-tooltip />
        <el-table-column label="图片" width="120" align="center">
          <template #default="{ row }">
            <div v-if="getCommentImages(row).length > 0" class="table-images">
              <el-image
                v-for="(img, index) in getCommentImages(row).slice(0, 3)"
                :key="index"
                :src="getFileUrl(img)"
                :preview-src-list="getCommentImages(row).map(i => getFileUrl(i))"
                :initial-index="index"
                fit="cover"
                class="table-image"
              />
              <span v-if="getCommentImages(row).length > 3" class="image-count">+{{ getCommentImages(row).length - 3 }}</span>
            </div>
            <span v-else class="no-image">无图片</span>
          </template>
        </el-table-column>
        <el-table-column prop="likeCount" label="点赞数" width="100" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag 
              :type="row.status === '0' ? 'success' : row.status === '1' ? 'warning' : 'danger'" 
              size="small"
            >
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="评论时间" width="160" align="center" />
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
      title="评论详情"
      width="700px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="评论ID">{{ detailData.id }}</el-descriptions-item>
        <el-descriptions-item label="评论人">{{ detailData.userName }}</el-descriptions-item>
        <el-descriptions-item label="点赞数">{{ detailData.likeCount }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag 
            :type="detailData.status === '0' ? 'success' : detailData.status === '1' ? 'warning' : 'danger'" 
            size="small"
          >
            {{ getStatusText(detailData.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="评论内容" :span="2">
          {{ detailData.content }}
        </el-descriptions-item>
        <el-descriptions-item label="评论时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detailData.updateTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, View, Delete, ChatLineRound, ChatDotRound, User, Clock } from '@element-plus/icons-vue'
import { getCommentList, deleteComment } from '@/api/community'
import { getFileUrl } from '@/utils/file'

// 搜索表单
const searchForm = reactive({
  content: '',
  status: ''
})

// 解析评论图片
const getCommentImages = (comment) => {
  if (!comment.images) return []
  try {
    return typeof comment.images === 'string' ? JSON.parse(comment.images) : comment.images
  } catch (e) {
    return []
  }
}

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
    const res = await getCommentList({
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
    content: '',
    status: ''
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
  ElMessageBox.confirm('确定要删除该评论吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteComment(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error(error)
      ElMessage.error('删除失败')
    }
  })
}

// 获取状态文本
const getStatusText = (status) => {
  const map = {
    '0': '正常',
    '1': '隐藏',
    '2': '删除'
  }
  return map[status] || '未知'
}

// 统计数据
const activeUsers = computed(() => {
  const uniqueUsers = new Set(tableData.value.map(item => item.userId))
  return uniqueUsers.size
})

const todayComments = computed(() => {
  const today = new Date().toISOString().split('T')[0]
  return tableData.value.filter(item => item.createTime?.startsWith(today)).length
})

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.table-images {
  display: flex;
  gap: 4px;
  justify-content: center;
  align-items: center;
}

.table-image {
  width: 30px;
  height: 30px;
  border-radius: 4px;
  cursor: pointer;
}

.image-count {
  font-size: 12px;
  color: #999;
}

.no-image {
  color: #ccc;
  font-size: 12px;
}

@import '@/styles/common-page.scss';
</style>
