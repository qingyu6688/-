<template>
  <div class="alumni-profile-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <div class="header-icon">
            <el-icon :size="28"><UserFilled /></el-icon>
          </div>
          <div class="header-text">
            <h2>校友档案管理</h2>
            <p>管理校友联系方式和职业信息</p>
          </div>
        </div>
        <div class="header-right">
          <el-button type="primary" @click="handleAdd" :icon="Plus" size="large">
            新增联系方式
          </el-button>
        </div>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stats-row">
      <el-col :span="6">
        <div class="stat-card stat-card-primary">
          <div class="stat-icon">
            <el-icon :size="32"><User /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ total }}</div>
            <div class="stat-label">校友总数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card-success">
          <div class="stat-icon">
            <el-icon :size="32"><OfficeBuilding /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ publicCount }}</div>
            <div class="stat-label">公开信息</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card-warning">
          <div class="stat-icon">
            <el-icon :size="32"><Lock /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ privateCount }}</div>
            <div class="stat-label">私密信息</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card-info">
          <div class="stat-icon">
            <el-icon :size="32"><Briefcase /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ workingCount }}</div>
            <div class="stat-label">在职校友</div>
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
        <el-form-item label="用户名">
          <el-input 
            v-model="searchForm.username" 
            placeholder="请输入用户名" 
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input 
            v-model="searchForm.realName" 
            placeholder="请输入真实姓名" 
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="公司">
          <el-input 
            v-model="searchForm.company" 
            placeholder="请输入公司名称" 
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="行业">
          <el-input 
            v-model="searchForm.industry" 
            placeholder="请输入行业" 
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="隐私级别">
          <el-select 
            v-model="searchForm.privacyLevel" 
            placeholder="请选择隐私级别" 
            clearable
            style="width: 150px"
          >
            <el-option label="公开" :value="0" />
            <el-option label="仅好友" :value="1" />
            <el-option label="私密" :value="2" />
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
        <el-table-column prop="userId" label="用户ID" width="100" align="center" />
        <el-table-column prop="company" label="工作单位" min-width="180" show-overflow-tooltip />
        <el-table-column prop="position" label="职位" width="140" show-overflow-tooltip />
        <el-table-column prop="industry" label="所属行业" width="120" show-overflow-tooltip />
        <el-table-column prop="address" label="现居住地" min-width="200" show-overflow-tooltip />
        <el-table-column prop="emergencyContact" label="紧急联系人" width="120" />
        <el-table-column prop="emergencyPhone" label="紧急电话" width="130" />
        <el-table-column prop="privacyLevel" label="隐私级别" width="100" align="center">
          <template #default="{ row }">
            <el-tag 
              :type="row.privacyLevel === 0 ? 'success' : row.privacyLevel === 1 ? 'warning' : 'danger'" 
              size="small"
            >
              {{ getPrivacyLevelText(row.privacyLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template #default="{ row }">
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
      :title="isEdit ? '编辑联系方式' : '新增联系方式'"
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
            <el-form-item label="选择用户" prop="userId">
              <el-select 
                v-model="form.userId" 
                placeholder="请选择用户" 
                filterable
                style="width: 100%"
              >
                <el-option
                  v-for="user in userList"
                  :key="user.id"
                  :label="`${user.realName} (${user.username})`"
                  :value="user.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工作单位" prop="company">
              <el-input v-model="form.company" placeholder="请输入工作单位" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="职位" prop="position">
              <el-input v-model="form.position" placeholder="请输入职位" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属行业" prop="industry">
              <el-input v-model="form.industry" placeholder="请输入所属行业" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="现居住地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入现居住地址" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="紧急联系人" prop="emergencyContact">
              <el-input v-model="form.emergencyContact" placeholder="请输入紧急联系人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="紧急电话" prop="emergencyPhone">
              <el-input v-model="form.emergencyPhone" placeholder="请输入紧急联系电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="隐私级别" prop="privacyLevel">
          <el-radio-group v-model="form.privacyLevel">
            <el-radio :label="0">公开</el-radio>
            <el-radio :label="1">仅好友</el-radio>
            <el-radio :label="2">私密</el-radio>
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
      title="联系方式详情"
      width="700px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="用户ID">{{ detailData.userId }}</el-descriptions-item>
        <el-descriptions-item label="工作单位">{{ detailData.company || '-' }}</el-descriptions-item>
        <el-descriptions-item label="职位">{{ detailData.position || '-' }}</el-descriptions-item>
        <el-descriptions-item label="所属行业">{{ detailData.industry || '-' }}</el-descriptions-item>
        <el-descriptions-item label="现居住地址" :span="2">{{ detailData.address || '-' }}</el-descriptions-item>
        <el-descriptions-item label="紧急联系人">{{ detailData.emergencyContact || '-' }}</el-descriptions-item>
        <el-descriptions-item label="紧急电话">{{ detailData.emergencyPhone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="隐私级别">
          <el-tag 
            :type="detailData.privacyLevel === 0 ? 'success' : detailData.privacyLevel === 1 ? 'warning' : 'danger'" 
            size="small"
          >
            {{ getPrivacyLevelText(detailData.privacyLevel) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detailData.updateTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh, Plus, Edit, View, User, UserFilled, OfficeBuilding, Lock, Briefcase } from '@element-plus/icons-vue'
import request from '@/utils/request'

// 搜索表单
const searchForm = reactive({
  username: '',
  realName: '',
  company: '',
  industry: '',
  privacyLevel: null
})

// 表格数据
const tableData = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 用户列表
const userList = ref([])

// 弹窗相关
const dialogVisible = ref(false)
const detailVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)

// 表单数据
const form = reactive({
  id: null,
  userId: null,
  company: '',
  position: '',
  industry: '',
  address: '',
  emergencyContact: '',
  emergencyPhone: '',
  privacyLevel: 1
})

// 详情数据
const detailData = ref({})

// 表单验证规则
const rules = {
  userId: [
    { required: true, message: '请输入用户ID', trigger: 'blur' }
  ],
  privacyLevel: [
    { required: true, message: '请选择隐私级别', trigger: 'change' }
  ]
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/alumni/contact/list', {
      params: {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        ...searchForm
      }
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
    username: '',
    realName: '',
    company: '',
    industry: '',
    privacyLevel: null
  })
  handleSearch()
}

// 新增
const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, {
    id: null,
    userId: null,
    company: '',
    position: '',
    industry: '',
    address: '',
    emergencyContact: '',
    emergencyPhone: '',
    privacyLevel: 1
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, { ...row })
  dialogVisible.value = true
}

// 查看详情
const handleView = (row) => {
  detailData.value = { ...row }
  detailVisible.value = true
}

// 提交
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    await request.post('/alumni/contact', form)
    ElMessage.success(isEdit.value ? '更新成功' : '新增成功')
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

// 获取隐私级别文本
const getPrivacyLevelText = (level) => {
  const map = {
    0: '公开',
    1: '仅好友',
    2: '私密'
  }
  return map[level] || '未知'
}

// 统计数据
const publicCount = computed(() => {
  return tableData.value.filter(item => item.privacyLevel === 0).length
})

const privateCount = computed(() => {
  return tableData.value.filter(item => item.privacyLevel === 2).length
})

const workingCount = computed(() => {
  return tableData.value.filter(item => item.company && item.company.trim() !== '').length
})

// 加载用户列表
const loadUserList = async () => {
  try {
    const res = await request.get('/user/list', {
      params: {
        pageNum: 1,
        pageSize: 1000 // 获取所有用户
      }
    })
    userList.value = res.data.records
  } catch (error) {
    console.error('加载用户列表失败:', error)
  }
}

onMounted(() => {
  loadData()
  loadUserList()
})
</script>

<style scoped lang="scss">
.alumni-profile-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
}

/* 页面头部 */
.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 30px;
  margin-bottom: 20px;
  box-shadow: 0 8px 32px rgba(102, 126, 234, 0.3);
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
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  backdrop-filter: blur(10px);
}

.header-text h2 {
  margin: 0 0 8px 0;
  color: #fff;
  font-size: 28px;
  font-weight: 700;
}

.header-text p {
  margin: 0;
  color: rgba(255, 255, 255, 0.9);
  font-size: 14px;
}

/* 统计卡片 */
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
  transition: all 0.3s;
  cursor: pointer;
  border: 2px solid transparent;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.stat-card-primary:hover {
  border-color: #42a5f5;
}

.stat-card-success:hover {
  border-color: #67c23a;
}

.stat-card-warning:hover {
  border-color: #e6a23c;
}

.stat-card-info:hover {
  border-color: #909399;
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

.stat-card-primary .stat-icon {
  background: linear-gradient(135deg, #42a5f5 0%, #2196f3 100%);
}

.stat-card-success .stat-icon {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
}

.stat-card-warning .stat-icon {
  background: linear-gradient(135deg, #e6a23c 0%, #f56c6c 100%);
}

.stat-card-info .stat-icon {
  background: linear-gradient(135deg, #909399 0%, #b3b6bb 100%);
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

/* 搜索卡片 */
.search-card {
  margin-bottom: 16px;
  border-radius: 12px;
  border: none;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.search-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 2px solid #f0f2f5;
}

.search-header .el-icon {
  color: #42a5f5;
  font-size: 20px;
}

.search-form {
  margin: 0;
}

.search-form :deep(.el-form-item) {
  margin-bottom: 0;
}

/* 表格卡片 */
.table-card {
  border-radius: 12px;
  border: none;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

:deep(.el-table) {
  .el-table__header {
    th {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: #fff;
    }
  }
  
  .el-table__body tr {
    transition: all 0.3s;
  }
  
  .el-table__body tr:hover {
    background: #f5f7fa !important;
  }
}

/* 对话框样式 */
:deep(.el-dialog) {
  border-radius: 16px;
  overflow: hidden;
}

:deep(.el-dialog__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 24px 30px;
  margin: 0;
}

:deep(.el-dialog__title) {
  color: #fff;
  font-size: 20px;
  font-weight: 600;
}

:deep(.el-dialog__headerbtn .el-dialog__close) {
  color: #fff;
  font-size: 22px;
}

:deep(.el-dialog__body) {
  padding: 30px;
}
</style>
