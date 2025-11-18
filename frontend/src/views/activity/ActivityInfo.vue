<template>
  <div class="activity-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <div class="header-icon">
            <el-icon :size="28"><Calendar /></el-icon>
          </div>
          <div class="header-text">
            <h2>活动管理</h2>
            <p>管理社团活动信息</p>
          </div>
        </div>
        <div class="header-right">
          <el-button type="primary" @click="handleAdd" :icon="Plus" size="large">
            创建活动
          </el-button>
        </div>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stats-row">
      <el-col :span="6">
        <div class="stat-card stat-card-primary">
          <div class="stat-icon">
            <el-icon :size="32"><Calendar /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ total }}</div>
            <div class="stat-label">活动总数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card-success">
          <div class="stat-icon">
            <el-icon :size="32"><VideoPlay /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ ongoingCount }}</div>
            <div class="stat-label">进行中</div>
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
        <div class="stat-card stat-card-info">
          <div class="stat-icon">
            <el-icon :size="32"><User /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ totalParticipants }}</div>
            <div class="stat-label">总参与人数</div>
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
        <el-form-item label="活动类型">
          <el-select 
            v-model="searchForm.activityType" 
            placeholder="请选择类型" 
            clearable
            style="width: 150px"
          >
            <el-option label="讲座" value="lecture" />
            <el-option label="比赛" value="competition" />
            <el-option label="聚会" value="party" />
            <el-option label="公益" value="volunteer" />
            <el-option label="培训" value="training" />
            <el-option label="展览" value="exhibition" />
          </el-select>
        </el-form-item>
        <el-form-item label="活动状态">
          <el-select 
            v-model="searchForm.status" 
            placeholder="请选择状态" 
            clearable
            style="width: 120px"
          >
            <el-option label="报名中" value="0" />
            <el-option label="进行中" value="1" />
            <el-option label="已结束" value="2" />
            <el-option label="已取消" value="3" />
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
        <el-table-column prop="activityName" label="活动名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="activityType" label="类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getTypeTag(row.activityType)" size="small">
              {{ getTypeText(row.activityType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="location" label="地点" width="150" show-overflow-tooltip />
        <el-table-column prop="startTime" label="开始时间" width="160" align="center" />
        <el-table-column label="参与人数" width="120" align="center">
          <template #default="{ row }">
            {{ row.currentParticipants }}/{{ row.maxParticipants || '不限' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="活动状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag 
              :type="getStatusTag(row.status)" 
              size="small"
            >
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
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
              v-if="row.status !== '3'"
              type="danger" 
              link 
              @click="handleCancel(row)"
              :icon="Close"
            >
              取消
            </el-button>
            <el-button 
              type="primary" 
              link 
              @click="handleEdit(row)"
              :icon="Edit"
            >
              编辑
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

    <!-- 新增/编辑弹窗 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="isEdit ? '编辑活动' : '创建活动'"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form 
        ref="formRef" 
        :model="form" 
        :rules="rules" 
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="活动名称" prop="activityName">
              <el-input v-model="form.activityName" placeholder="请输入活动名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="活动类型" prop="activityType">
              <el-select v-model="form.activityType" placeholder="请选择类型" style="width: 100%">
                <el-option label="讲座" value="lecture" />
                <el-option label="比赛" value="competition" />
                <el-option label="聚会" value="party" />
                <el-option label="公益" value="volunteer" />
                <el-option label="培训" value="training" />
                <el-option label="展览" value="exhibition" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="封面图片">
          <el-upload
            class="cover-uploader"
            :action="uploadUrl"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleCoverSuccess"
            :before-upload="beforeCoverUpload"
          >
            <img v-if="form.coverImage" :src="getFileUrl(form.coverImage)" class="cover-image" alt="封面预览" />
            <div v-else class="upload-placeholder">
              <el-icon class="upload-icon"><Plus /></el-icon>
              <div class="upload-text">点击上传封面图片</div>
            </div>
          </el-upload>
          <div class="upload-tip">建议尺寸：800x400px，支持jpg/png格式，大小不超过2MB</div>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始时间" prop="startTime">
              <el-date-picker
                v-model="form.startTime"
                type="datetime"
                placeholder="选择开始时间"
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="endTime">
              <el-date-picker
                v-model="form.endTime"
                type="datetime"
                placeholder="选择结束时间"
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="活动地点" prop="location">
          <el-input v-model="form.location" placeholder="请输入活动地点" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="最大人数" prop="maxParticipants">
              <el-input-number v-model="form.maxParticipants" :min="0" :max="9999" style="width: 100%" />
              <span style="font-size: 12px; color: #909399;">0表示不限</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报名截止" prop="registrationDeadline">
              <el-date-picker
                v-model="form.registrationDeadline"
                type="datetime"
                placeholder="选择报名截止时间"
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="活动费用" prop="fee">
              <el-input-number v-model="form.fee" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="需要审核">
              <el-switch v-model="form.needAudit" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="活动描述" prop="description">
          <el-input 
            v-model="form.description" 
            type="textarea" 
            :rows="4"
            placeholder="请输入活动描述"
          />
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
      title="活动详情"
      width="800px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="活动名称" :span="2">{{ detailData.activityName }}</el-descriptions-item>
        <el-descriptions-item label="活动类型">
          <el-tag :type="getTypeTag(detailData.activityType)" size="small">
            {{ getTypeText(detailData.activityType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="活动地点">{{ detailData.location }}</el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ detailData.startTime }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ detailData.endTime }}</el-descriptions-item>
        <el-descriptions-item label="参与人数">
          {{ detailData.currentParticipants }}/{{ detailData.maxParticipants || '不限' }}
        </el-descriptions-item>
        <el-descriptions-item label="报名截止">{{ detailData.registrationDeadline }}</el-descriptions-item>
        <el-descriptions-item label="活动费用">¥{{ detailData.fee || 0 }}</el-descriptions-item>
        <el-descriptions-item label="需要审核">
          {{ detailData.needAudit === 1 ? '是' : '否' }}
        </el-descriptions-item>
        <el-descriptions-item label="活动状态">
          <el-tag :type="getStatusTag(detailData.status)" size="small">
            {{ getStatusText(detailData.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="审核状态">
          <el-tag 
            :type="detailData.auditStatus === '1' ? 'success' : detailData.auditStatus === '2' ? 'danger' : 'warning'" 
            size="small"
          >
            {{ getAuditStatusText(detailData.auditStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="活动描述" :span="2">
          {{ detailData.description || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detailData.updateTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { 
  Search, Refresh, Plus, Edit, View, Delete, CircleCheck, CircleClose, Close,
  Calendar, VideoPlay, Clock, User
} from '@element-plus/icons-vue'
import { 
  getActivityList, addActivity, updateActivity, deleteActivity, 
  auditActivity, cancelActivity 
} from '@/api/activity'

const userStore = useUserStore()

// 上传配置
const uploadUrl = 'http://localhost:8080/api/file/upload'
const uploadHeaders = computed(() => ({
  Authorization: `Bearer ${userStore.token}`
}))

// 获取文件URL
const getFileUrl = (path) => {
  if (!path || typeof path !== 'string') return ''
  if (path.startsWith('http://') || path.startsWith('https://')) return path
  return `http://localhost:8080${path.startsWith('/') ? path : '/' + path}`
}

// 搜索表单
const searchForm = reactive({
  activityName: '',
  activityType: '',
  status: '',
  auditStatus: ''
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
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)

// 表单数据
const form = reactive({
  id: null,
  activityName: '',
  activityType: '',
  coverImage: '',
  startTime: '',
  endTime: '',
  location: '',
  maxParticipants: 0,
  registrationDeadline: '',
  fee: 0,
  organizerId: null,
  needAudit: 0,
  description: ''
})

// 详情数据
const detailData = ref({})

// 表单验证规则
const rules = {
  activityName: [
    { required: true, message: '请输入活动名称', trigger: 'blur' }
  ],
  activityType: [
    { required: true, message: '请选择活动类型', trigger: 'change' }
  ],
  startTime: [
    { required: true, message: '请选择开始时间', trigger: 'change' }
  ],
  endTime: [
    { required: true, message: '请选择结束时间', trigger: 'change' }
  ],
  location: [
    { required: true, message: '请输入活动地点', trigger: 'blur' }
  ]
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await getActivityList({
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
    activityType: '',
    status: '',
    auditStatus: ''
  })
  handleSearch()
}

// 新增
const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, {
    id: null,
    activityName: '',
    activityType: '',
    startTime: '',
    endTime: '',
    location: '',
    maxParticipants: 0,
    registrationDeadline: '',
    fee: 0,
    needAudit: 0,
    description: ''
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  isEdit.value = true
  // 只复制需要的字段，避免复制嵌套对象
  Object.assign(form, {
    id: row.id,
    activityName: row.activityName,
    activityType: row.activityType,
    coverImage: row.coverImage || '',
    startTime: row.startTime,
    endTime: row.endTime,
    location: row.location,
    maxParticipants: row.maxParticipants || 0,
    registrationDeadline: row.registrationDeadline,
    fee: row.fee || 0,
    organizerId: row.organizerId,
    needAudit: row.needAudit || 0,
    description: row.description || ''
  })
  dialogVisible.value = true
}

// 查看详情
const handleView = (row) => {
  detailData.value = { ...row }
  detailVisible.value = true
}

// 审核
const handleAudit = async (row, status) => {
  try {
    await auditActivity(row.id, status)
    ElMessage.success('审核成功')
    loadData()
  } catch (error) {
    console.error(error)
    ElMessage.error('审核失败')
  }
}

// 取消活动
const handleCancel = (row) => {
  ElMessageBox.confirm('确定要取消该活动吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await cancelActivity(row.id)
      ElMessage.success('取消成功')
      loadData()
    } catch (error) {
      console.error(error)
      ElMessage.error('取消失败')
    }
  })
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该活动吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteActivity(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error(error)
      ElMessage.error('删除失败')
    }
  })
}

// 封面图片上传成功
const handleCoverSuccess = (response) => {
  console.log('上传响应:', response)
  if (response.code === 200) {
    // 如果response.data是对象，提取fileUrl或filePath
    if (typeof response.data === 'object' && response.data !== null) {
      form.coverImage = response.data.fileUrl || response.data.filePath || response.data.url || ''
    } else {
      // 如果是字符串，直接使用
      form.coverImage = response.data
    }
    console.log('设置的coverImage:', form.coverImage)
    ElMessage.success('封面上传成功')
  } else {
    ElMessage.error('封面上传失败')
  }
}

// 封面图片上传前验证
const beforeCoverUpload = (file) => {
  const isImage = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('封面图片只能是 JPG/PNG 格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('封面图片大小不能超过 2MB!')
    return false
  }
  return true
}

// 提交
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    if (isEdit.value) {
      await updateActivity(form)
    } else {
      // 创建时设置组织者ID为当前用户ID
      if (!form.organizerId) {
        form.organizerId = userStore.userInfo?.id
      }
      await addActivity(form)
    }
    
    ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
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

// 获取类型文本
const getTypeText = (type) => {
  const map = {
    'lecture': '讲座',
    'competition': '比赛',
    'party': '聚会',
    'volunteer': '公益',
    'training': '培训',
    'exhibition': '展览'
  }
  return map[type] || '未知'
}

// 获取类型标签
const getTypeTag = (type) => {
  const map = {
    'lecture': 'primary',
    'competition': 'danger',
    'party': 'success',
    'volunteer': 'warning',
    'training': 'info',
    'exhibition': ''
  }
  return map[type] || ''
}

// 获取状态文本
const getStatusText = (status) => {
  const map = {
    '0': '报名中',
    '1': '进行中',
    '2': '已结束',
    '3': '已取消'
  }
  return map[status] || '未知'
}

// 获取状态标签
const getStatusTag = (status) => {
  const map = {
    '0': 'success',
    '1': 'primary',
    '2': 'info',
    '3': 'danger'
  }
  return map[status] || ''
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

// 统计数据
const ongoingCount = computed(() => {
  return tableData.value.filter(item => item.status === '1').length
})

const pendingCount = computed(() => {
  return tableData.value.filter(item => item.auditStatus === '0').length
})

const totalParticipants = computed(() => {
  return tableData.value.reduce((sum, item) => sum + (item.currentParticipants || 0), 0)
})

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
@use '@/styles/common-page.scss';

// 封面上传样式
.cover-uploader {
  :deep(.el-upload) {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: all 0.3s;
    width: 400px;
    height: 200px;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #fafafa;
    
    &:hover {
      border-color: #409eff;
    }
  }
}

.cover-image {
  width: 400px;
  height: 200px;
  display: block;
  object-fit: cover;
}

.upload-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
}

.upload-icon {
  font-size: 48px;
  color: #8c939d;
  margin-bottom: 12px;
}

.upload-text {
  font-size: 14px;
  color: #8c939d;
}

.upload-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 8px;
}
</style>
