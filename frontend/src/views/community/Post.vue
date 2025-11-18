<template>
  <div class="post-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <div class="header-icon">
            <el-icon :size="28"><ChatDotRound /></el-icon>
          </div>
          <div class="header-text">
            <h2>帖子管理</h2>
            <p>管理社区论坛帖子内容</p>
          </div>
        </div>
        <div class="header-right">
          <el-button type="primary" @click="handleAdd" :icon="Plus" size="large">
            发布帖子
          </el-button>
        </div>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stats-row">
      <el-col :span="6">
        <div class="stat-card stat-card-primary">
          <div class="stat-icon">
            <el-icon :size="32"><Document /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ total }}</div>
            <div class="stat-label">帖子总数</div>
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
            <el-icon :size="32"><Star /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ essenceCount }}</div>
            <div class="stat-label">精华帖</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card-info">
          <div class="stat-icon">
            <el-icon :size="32"><View /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ totalViews }}</div>
            <div class="stat-label">总浏览量</div>
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
        <el-form-item label="标题">
          <el-input 
            v-model="searchForm.title" 
            placeholder="请输入标题" 
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="板块">
          <el-select 
            v-model="searchForm.categoryId" 
            placeholder="请选择板块" 
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="category in categoryList"
              :key="category.id"
              :label="category.categoryName"
              :value="category.id"
            />
          </el-select>
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
        <el-form-item label="帖子状态">
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
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column label="图片" width="120" align="center">
          <template #default="{ row }">
            <div v-if="getPostImages(row).length > 0" class="table-images">
              <el-image
                v-for="(img, index) in getPostImages(row).slice(0, 3)"
                :key="index"
                :src="getFileUrl(img)"
                :preview-src-list="getPostImages(row).map(i => getFileUrl(i))"
                :initial-index="index"
                fit="cover"
                class="table-image"
              />
              <span v-if="getPostImages(row).length > 3" class="image-count">+{{ getPostImages(row).length - 3 }}</span>
            </div>
            <span v-else class="no-image">无图片</span>
          </template>
        </el-table-column>
        <el-table-column prop="categoryId" label="板块" width="120" align="center">
          <template #default="{ row }">
            {{ getCategoryName(row.categoryId) }}
          </template>
        </el-table-column>
        <el-table-column prop="authorName" label="发帖人" width="120" align="center" />
        <el-table-column prop="viewCount" label="浏览" width="80" align="center" />
        <el-table-column prop="likeCount" label="点赞" width="80" align="center" />
        <el-table-column prop="commentCount" label="评论" width="80" align="center" />
        <el-table-column prop="auditStatus" label="审核状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag 
              :type="row.auditStatus === '1' ? 'success' : row.auditStatus === '2' ? 'danger' : 'warning'" 
              size="small"
            >
              {{ getAuditStatusText(row.auditStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="标记" width="150" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.isTop === 1" type="danger" size="small" style="margin-right: 5px">置顶</el-tag>
            <el-tag v-if="row.isEssence === 1" type="warning" size="small">精华</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发布时间" width="160" align="center" />
        <el-table-column label="操作" width="350" align="center" fixed="right">
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
              type="primary" 
              link 
              @click="handleSetTop(row)"
              :icon="Top"
            >
              {{ row.isTop === 1 ? '取消置顶' : '置顶' }}
            </el-button>
            <el-button 
              type="warning" 
              link 
              @click="handleSetEssence(row)"
              :icon="Star"
            >
              {{ row.isEssence === 1 ? '取消精华' : '精华' }}
            </el-button>
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
      title="帖子详情"
      width="800px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="标题" :span="2">{{ detailData.title }}</el-descriptions-item>
        <el-descriptions-item label="板块">{{ getCategoryName(detailData.categoryId) }}</el-descriptions-item>
        <el-descriptions-item label="发帖人ID">{{ detailData.userId }}</el-descriptions-item>
        <el-descriptions-item label="浏览次数">{{ detailData.viewCount }}</el-descriptions-item>
        <el-descriptions-item label="点赞数">{{ detailData.likeCount }}</el-descriptions-item>
        <el-descriptions-item label="评论数">{{ detailData.commentCount }}</el-descriptions-item>
        <el-descriptions-item label="收藏数">{{ detailData.collectCount }}</el-descriptions-item>
        <el-descriptions-item label="审核状态">
          <el-tag 
            :type="detailData.auditStatus === '1' ? 'success' : detailData.auditStatus === '2' ? 'danger' : 'warning'" 
            size="small"
          >
            {{ getAuditStatusText(detailData.auditStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="帖子状态">
          <el-tag 
            :type="detailData.status === '0' ? 'success' : 'info'" 
            size="small"
          >
            {{ getStatusText(detailData.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="内容" :span="2">
          <div v-html="detailData.content" style="max-height: 300px; overflow-y: auto;"></div>
        </el-descriptions-item>
        <el-descriptions-item label="发布时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detailData.updateTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Search, Refresh, Plus, Edit, View, Delete, CircleCheck, CircleClose, 
  ChatDotRound, Document, Clock, Star, Top 
} from '@element-plus/icons-vue'
import { getPostList, deletePost, auditPost, setPostTop, setPostEssence } from '@/api/community'
import { getCategoryList } from '@/api/community'
import { getFileUrl } from '@/utils/file'

// 搜索表单
const searchForm = reactive({
  title: '',
  categoryId: null,
  auditStatus: '',
  status: ''
})

// 解析帖子图片
const getPostImages = (post) => {
  if (!post.images) return []
  try {
    return typeof post.images === 'string' ? JSON.parse(post.images) : post.images
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

// 板块列表
const categoryList = ref([])

// 弹窗相关
const detailVisible = ref(false)
const detailData = ref({})

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await getPostList({
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

// 加载板块列表
const loadCategoryList = async () => {
  try {
    const res = await getCategoryList()
    categoryList.value = res.data
  } catch (error) {
    console.error('加载板块列表失败:', error)
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
    title: '',
    categoryId: null,
    auditStatus: '',
    status: ''
  })
  handleSearch()
}

// 新增
const handleAdd = () => {
  ElMessage.info('请前往前台页面发布帖子')
}

// 查看详情
const handleView = (row) => {
  detailData.value = { ...row }
  detailVisible.value = true
}

// 审核
const handleAudit = async (row, status) => {
  try {
    await auditPost(row.id, status)
    ElMessage.success('审核成功')
    loadData()
  } catch (error) {
    console.error(error)
    ElMessage.error('审核失败')
  }
}

// 设置置顶
const handleSetTop = async (row) => {
  const isTop = row.isTop === 1 ? 0 : 1
  try {
    await setPostTop(row.id, isTop)
    ElMessage.success(isTop === 1 ? '置顶成功' : '取消置顶成功')
    loadData()
  } catch (error) {
    console.error(error)
    ElMessage.error('操作失败')
  }
}

// 设置精华
const handleSetEssence = async (row) => {
  const isEssence = row.isEssence === 1 ? 0 : 1
  try {
    await setPostEssence(row.id, isEssence)
    ElMessage.success(isEssence === 1 ? '设置精华成功' : '取消精华成功')
    loadData()
  } catch (error) {
    console.error(error)
    ElMessage.error('操作失败')
  }
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该帖子吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deletePost(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error(error)
      ElMessage.error('删除失败')
    }
  })
}

// 获取板块名称
const getCategoryName = (categoryId) => {
  const category = categoryList.value.find(c => c.id === categoryId)
  return category ? category.categoryName : '-'
}

// 获取审核状态文本
const getAuditStatusText = (status) => {
  const map = {
    '0': '待审核',
    '1': '已通过',
    '2': '未通过'
  }
  return map[status] || '未知'
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
const pendingCount = computed(() => {
  return tableData.value.filter(item => item.auditStatus === '0').length
})

const essenceCount = computed(() => {
  return tableData.value.filter(item => item.isEssence === 1).length
})

const totalViews = computed(() => {
  return tableData.value.reduce((sum, item) => sum + (item.viewCount || 0), 0)
})

onMounted(() => {
  loadData()
  loadCategoryList()
})
</script>

<style scoped lang="scss">
@import '@/styles/common-page.scss';

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
</style>
