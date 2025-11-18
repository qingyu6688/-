<template>
  <div class="notice-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <div class="header-icon">
            <el-icon :size="28"><Bell /></el-icon>
          </div>
          <div class="header-text">
            <h2>通知管理</h2>
            <p>管理系统通知公告</p>
          </div>
        </div>
        <div class="header-right">
          <el-button type="primary" @click="handleAdd" :icon="Plus" size="large">
            发布通知
          </el-button>
        </div>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stats-row">
      <el-col :span="8">
        <div class="stat-card stat-card-primary">
          <div class="stat-icon">
            <el-icon :size="32"><Bell /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ total }}</div>
            <div class="stat-label">通知总数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card stat-card-warning">
          <div class="stat-icon">
            <el-icon :size="32"><Warning /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ urgentCount }}</div>
            <div class="stat-label">紧急通知</div>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card stat-card-success">
          <div class="stat-icon">
            <el-icon :size="32"><CircleCheck /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ publishedCount }}</div>
            <div class="stat-label">已发布</div>
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
        <el-form-item label="通知类型">
          <el-select 
            v-model="searchForm.noticeType" 
            placeholder="请选择类型" 
            clearable
            style="width: 150px"
          >
            <el-option label="紧急" value="urgent" />
            <el-option label="重要" value="important" />
            <el-option label="一般" value="normal" />
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
        <el-table-column prop="title" label="标题" min-width="250" show-overflow-tooltip />
        <el-table-column prop="noticeType" label="类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag 
              :type="getNoticeTypeTag(row.noticeType)" 
              size="small"
            >
              {{ getNoticeTypeText(row.noticeType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isTop" label="置顶" width="80" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.isTop === 1" type="danger" size="small">置顶</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="readCount" label="阅读数" width="100" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag 
              :type="row.status === '1' ? 'success' : row.status === '2' ? 'info' : 'warning'" 
              size="small"
            >
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="publishTime" label="发布时间" width="160" align="center" />
        <el-table-column label="操作" width="320" align="center" fixed="right">
          <template #default="{ row }">
            <el-button 
              type="primary" 
              link 
              @click="handleEdit(row)"
              :icon="Edit"
              size="small"
            >
              编辑
            </el-button>
            <el-button 
              type="info" 
              link 
              @click="handleView(row)"
              :icon="View"
              size="small"
            >
              详情
            </el-button>
            <el-button 
              v-if="row.isTop === 0"
              type="warning" 
              link 
              @click="handleToggleTop(row, 1)"
              size="small"
            >
              置顶
            </el-button>
            <el-button 
              v-else
              type="warning" 
              link 
              @click="handleToggleTop(row, 0)"
              size="small"
            >
              取消置顶
            </el-button>
            <el-button 
              v-if="row.status === '1'"
              type="danger" 
              link 
              @click="handleWithdraw(row)"
              size="small"
            >
              撤回
            </el-button>
            <el-button 
              type="success" 
              link 
              @click="handleStatistics(row)"
              size="small"
            >
              统计
            </el-button>
            <el-button 
              type="danger" 
              link 
              @click="handleDelete(row)"
              :icon="Delete"
              size="small"
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

    <!-- 新增/编辑弹窗 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="isEdit ? '编辑通知' : '发布通知'"
      width="700px"
      :close-on-click-modal="false"
    >
      <el-form 
        ref="formRef" 
        :model="form" 
        :rules="rules" 
        label-width="100px"
      >
        <el-form-item label="通知标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入通知标题" />
        </el-form-item>
        <el-form-item label="通知类型" prop="noticeType">
          <el-select v-model="form.noticeType" placeholder="请选择通知类型" style="width: 100%">
            <el-option label="紧急" value="urgent" />
            <el-option label="重要" value="important" />
            <el-option label="一般" value="normal" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否置顶">
          <el-switch v-model="form.isTop" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="定时发布">
          <el-date-picker
            v-model="form.publishTime"
            type="datetime"
            placeholder="选择发布时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DDTHH:mm:ss"
            style="width: 100%"
          />
          <div style="color: #999; font-size: 12px; margin-top: 4px;">
            不选择则立即发布
          </div>
        </el-form-item>
        <el-form-item label="目标群体">
          <el-select 
            v-model="form.targetRoles" 
            multiple 
            placeholder="不选择则推送给所有用户"
            style="width: 100%"
          >
            <el-option label="全部用户" value="all" />
            <el-option label="管理员" value="admin" />
            <el-option label="超级管理员" value="super_admin" />
            <el-option label="普通用户" value="user" />
            <el-option label="社团负责人" value="club_leader" />
            <el-option label="社团成员" value="club_member" />
            <el-option label="志愿者" value="volunteer" />
          </el-select>
          <div style="color: #999; font-size: 12px; margin-top: 4px;">
            可选择特定角色群体推送，不选择默认推送给所有用户
          </div>
        </el-form-item>
        <el-form-item label="通知内容" prop="content">
          <el-input 
            v-model="form.content" 
            type="textarea" 
            :rows="10"
            placeholder="请输入通知内容（支持换行）"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">保存为草稿</el-radio>
            <el-radio label="1">立即发布</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
          确定
        </el-button>
      </template>
    </el-dialog>

    <!-- 详情弹窗 -->
    <el-dialog 
      v-model="detailVisible" 
      title="通知详情"
      width="700px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="标题" :span="2">{{ detailData.title }}</el-descriptions-item>
        <el-descriptions-item label="类型">
          <el-tag :type="getNoticeTypeTag(detailData.noticeType)" size="small">
            {{ getNoticeTypeText(detailData.noticeType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="置顶">
          <el-tag v-if="detailData.isTop === 1" type="danger" size="small">置顶</el-tag>
          <span v-else>否</span>
        </el-descriptions-item>
        <el-descriptions-item label="阅读数">{{ detailData.readCount }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag 
            :type="detailData.status === '1' ? 'success' : detailData.status === '2' ? 'info' : 'warning'" 
            size="small"
          >
            {{ getStatusText(detailData.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="目标群体" :span="2">
          <el-tag 
            v-for="role in getTargetRoles(detailData.targetUsers)" 
            :key="role" 
            style="margin-right: 8px;"
            size="small"
          >
            {{ getRoleText(role) }}
          </el-tag>
          <span v-if="!detailData.targetUsers">全部用户</span>
        </el-descriptions-item>
        <el-descriptions-item label="内容" :span="2">
          <div style="white-space: pre-wrap;">{{ detailData.content }}</div>
        </el-descriptions-item>
        <el-descriptions-item label="发布时间">{{ detailData.publishTime }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 统计弹窗 -->
    <el-dialog 
      v-model="statisticsVisible" 
      title="阅读统计"
      width="600px"
    >
      <div class="statistics-content">
        <el-row :gutter="20" style="margin-bottom: 20px;">
          <el-col :span="12">
            <div class="stat-box">
              <div class="stat-label">总阅读数</div>
              <div class="stat-value">{{ statistics.totalRead || 0 }}</div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="stat-box">
              <div class="stat-label">阅读率</div>
              <div class="stat-value">{{ statistics.readRate || '0%' }}</div>
            </div>
          </el-col>
        </el-row>
        <el-divider />
        <div class="reader-list">
          <h4>阅读用户列表</h4>
          <el-table :data="statistics.readers || []" max-height="300">
            <el-table-column prop="userName" label="用户" />
            <el-table-column prop="readTime" label="阅读时间" width="180" />
          </el-table>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Search, Refresh, Plus, Edit, View, Delete, Bell, Warning, CircleCheck 
} from '@element-plus/icons-vue'
import { getNoticeList, addNotice, updateNotice, deleteNotice, toggleNoticeTop, withdrawNotice, getNoticeStatistics } from '@/api/community'
import request from '@/utils/request'

// 搜索表单
const searchForm = reactive({
  title: '',
  noticeType: ''
})

// 表格数据
const tableData = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 弹窗相关
const dialogVisible = ref(false)
const detailVisible = ref(false)
const statisticsVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)

// 表单数据
const form = reactive({
  id: null,
  title: '',
  content: '',
  noticeType: 'normal',
  isTop: 0,
  status: '1',
  publishTime: null,
  targetRoles: []
})

// 详情数据
const detailData = ref({})
const statistics = ref({})

// 表单验证规则
const rules = {
  title: [
    { required: true, message: '请输入通知标题', trigger: 'blur' }
  ],
  noticeType: [
    { required: true, message: '请选择通知类型', trigger: 'change' }
  ],
  content: [
    { required: true, message: '请输入通知内容', trigger: 'blur' }
  ]
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await getNoticeList({
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
    title: '',
    noticeType: ''
  })
  handleSearch()
}

// 新增
const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, {
    id: null,
    title: '',
    content: '',
    noticeType: 'normal',
    isTop: 0,
    status: '1',
    publishTime: null,
    targetRoles: []
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, { 
    ...row,
    targetRoles: getTargetRoles(row.targetUsers)
  })
  dialogVisible.value = true
}

// 查看详情
const handleView = (row) => {
  detailData.value = { ...row }
  detailVisible.value = true
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该通知吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteNotice(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error(error)
      ElMessage.error('删除失败')
    }
  })
}

// 置顶/取消置顶
const handleToggleTop = async (row, isTop) => {
  try {
    await toggleNoticeTop(row.id, isTop)
    ElMessage.success(isTop === 1 ? '置顶成功' : '取消置顶成功')
    loadData()
  } catch (error) {
    console.error(error)
    ElMessage.error('操作失败')
  }
}

// 撤回
const handleWithdraw = (row) => {
  ElMessageBox.confirm('确定要撤回该通知吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await withdrawNotice(row.id)
      ElMessage.success('撤回成功')
      loadData()
    } catch (error) {
      console.error(error)
      ElMessage.error('撤回失败')
    }
  })
}

// 查看统计
const handleStatistics = async (row) => {
  try {
    const res = await getNoticeStatistics(row.id)
    statistics.value = res.data || {}
    statisticsVisible.value = true
  } catch (error) {
    console.error(error)
    ElMessage.error('加载统计数据失败')
  }
}

// 提交
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    // 准备提交数据
    const submitData = {
      ...form,
      targetUsers: form.targetRoles && form.targetRoles.length > 0 
        ? JSON.stringify(form.targetRoles) 
        : null
    }
    
    // 移除targetRoles字段
    delete submitData.targetRoles
    
    if (isEdit.value) {
      await updateNotice(submitData)
    } else {
      await addNotice(submitData)
    }
    
    ElMessage.success(isEdit.value ? '更新成功' : '发布成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error(error)
    if (error !== false) {
      ElMessage.error('操作失败')
    }
  } finally {
    submitLoading.value = false
  }
}

// 获取通知类型文本
const getNoticeTypeText = (type) => {
  const map = {
    'urgent': '紧急',
    'important': '重要',
    'normal': '一般'
  }
  return map[type] || '未知'
}

// 获取通知类型标签
const getNoticeTypeTag = (type) => {
  const map = {
    'urgent': 'danger',
    'important': 'warning',
    'normal': 'info'
  }
  return map[type] || ''
}

// 获取状态文本
const getStatusText = (status) => {
  const map = {
    '0': '草稿',
    '1': '已发布',
    '2': '已撤回'
  }
  return map[status] || '未知'
}

// 获取目标角色列表
const getTargetRoles = (targetUsers) => {
  if (!targetUsers) return []
  try {
    return JSON.parse(targetUsers)
  } catch {
    return []
  }
}

// 获取角色文本
const getRoleText = (role) => {
  const map = {
    'all': '全部用户',
    'admin': '管理员',
    'super_admin': '超级管理员',
    'user': '普通用户',
    'club_leader': '社团负责人',
    'club_member': '社团成员',
    'volunteer': '志愿者'
  }
  return map[role] || role
}

// 统计数据
const urgentCount = computed(() => {
  return tableData.value.filter(item => item.noticeType === 'urgent').length
})

const publishedCount = computed(() => {
  return tableData.value.filter(item => item.status === '1').length
})

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
/* 通用页面样式 */
.notice-container {
  padding: 20px;
}

.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 30px;
  margin-bottom: 20px;
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.3);

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
  }

  .header-text {
    h2 {
      color: #fff;
      margin: 0 0 8px 0;
      font-size: 24px;
      font-weight: 600;
    }

    p {
      color: rgba(255, 255, 255, 0.9);
      margin: 0;
      font-size: 14px;
    }
  }
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.12);
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

  .stat-content {
    flex: 1;
  }

  .stat-value {
    font-size: 28px;
    font-weight: 700;
    margin-bottom: 4px;
  }

  .stat-label {
    font-size: 14px;
    color: #666;
  }

  &.stat-card-primary {
    .stat-icon {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }
    .stat-value {
      color: #667eea;
    }
  }

  &.stat-card-warning {
    .stat-icon {
      background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    }
    .stat-value {
      color: #f5576c;
    }
  }

  &.stat-card-success {
    .stat-icon {
      background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    }
    .stat-value {
      color: #4facfe;
    }
  }
}

.search-card,
.table-card {
  margin-bottom: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.search-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 16px;
}

.search-form {
  .el-form-item {
    margin-bottom: 0;
  }
}

.statistics-content {
  .stat-box {
    text-align: center;
    padding: 20px;
    background: linear-gradient(135deg, #667eea15 0%, #764ba215 100%);
    border-radius: 12px;
    border: 1px solid #e9ecef;

    .stat-label {
      font-size: 14px;
      color: #6b7280;
      margin-bottom: 8px;
    }

    .stat-value {
      font-size: 32px;
      font-weight: 700;
      color: #667eea;
    }
  }

  .reader-list {
    h4 {
      margin-bottom: 16px;
      color: #1f2937;
      font-weight: 600;
    }
  }
}
</style>
