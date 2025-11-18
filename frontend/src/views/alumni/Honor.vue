<template>
  <div class="honor-container">
    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户ID">
          <el-input 
            v-model.number="searchForm.userId" 
            placeholder="请输入用户ID" 
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="荣誉名称">
          <el-input 
            v-model="searchForm.honorName" 
            placeholder="请输入荣誉名称" 
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="荣誉级别">
          <el-select 
            v-model="searchForm.honorLevel" 
            placeholder="请选择荣誉级别" 
            clearable
            style="width: 150px"
          >
            <el-option label="校级" value="校级" />
            <el-option label="市级" value="市级" />
            <el-option label="省级" value="省级" />
            <el-option label="国家级" value="国家级" />
          </el-select>
        </el-form-item>
        <el-form-item label="审核状态">
          <el-select 
            v-model="searchForm.auditStatus" 
            placeholder="请选择审核状态" 
            clearable
            style="width: 150px"
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
    <el-card class="table-card" shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">荣誉记录列表</span>
          <el-button type="primary" @click="handleAdd" :icon="Plus">
            新增荣誉记录
          </el-button>
        </div>
      </template>

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
        <el-table-column prop="honorName" label="荣誉名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="honorLevel" label="荣誉级别" width="100" align="center">
          <template #default="{ row }">
            <el-tag 
              :type="getLevelTagType(row.honorLevel)" 
              size="small"
            >
              {{ row.honorLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="awardDate" label="获奖日期" width="120" align="center" />
        <el-table-column prop="issuer" label="颁发单位" min-width="150" show-overflow-tooltip />
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
        <el-table-column prop="auditor" label="审核人" width="100" align="center">
          <template #default="{ row }">
            {{ row.auditor || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="auditTime" label="审核时间" width="160" align="center">
          <template #default="{ row }">
            {{ row.auditTime || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" align="center" fixed="right">
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
      :title="isEdit ? '编辑荣誉记录' : '新增荣誉记录'"
      width="700px"
      :close-on-click-modal="false"
    >
      <el-form 
        ref="formRef" 
        :model="form" 
        :rules="rules" 
        label-width="100px"
      >
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
        <el-form-item label="荣誉名称" prop="honorName">
          <el-input v-model="form.honorName" placeholder="请输入荣誉名称" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="荣誉级别" prop="honorLevel">
              <el-select v-model="form.honorLevel" placeholder="请选择荣誉级别" style="width: 100%">
                <el-option label="校级" value="校级" />
                <el-option label="市级" value="市级" />
                <el-option label="省级" value="省级" />
                <el-option label="国家级" value="国家级" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="获奖日期" prop="awardDate">
              <el-date-picker
                v-model="form.awardDate"
                type="date"
                placeholder="选择获奖日期"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="颁发单位" prop="issuer">
          <el-input v-model="form.issuer" placeholder="请输入颁发单位" />
        </el-form-item>
        <el-form-item label="证书图片" prop="certificateUrl">
          <el-input v-model="form.certificateUrl" placeholder="请输入证书图片URL" />
        </el-form-item>
        <el-form-item label="荣誉描述" prop="description">
          <el-input 
            v-model="form.description" 
            type="textarea" 
            :rows="4"
            placeholder="请输入荣誉描述"
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
      title="荣誉记录详情"
      width="700px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="用户ID">{{ detailData.userId }}</el-descriptions-item>
        <el-descriptions-item label="荣誉名称">{{ detailData.honorName }}</el-descriptions-item>
        <el-descriptions-item label="荣誉级别">
          <el-tag :type="getLevelTagType(detailData.honorLevel)" size="small">
            {{ detailData.honorLevel }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="获奖日期">{{ detailData.awardDate }}</el-descriptions-item>
        <el-descriptions-item label="颁发单位" :span="2">{{ detailData.issuer || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审核状态">
          <el-tag 
            :type="detailData.auditStatus === '1' ? 'success' : detailData.auditStatus === '2' ? 'danger' : 'warning'" 
            size="small"
          >
            {{ getAuditStatusText(detailData.auditStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="审核人">{{ detailData.auditor || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审核时间" :span="2">{{ detailData.auditTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="证书图片" :span="2">
          <el-image 
            v-if="detailData.certificateUrl" 
            :src="detailData.certificateUrl" 
            style="width: 200px; height: 150px"
            fit="contain"
            :preview-src-list="[detailData.certificateUrl]"
          />
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="荣誉描述" :span="2">
          {{ detailData.description || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detailData.updateTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 审核弹窗 -->
    <el-dialog 
      v-model="auditVisible" 
      title="审核荣誉记录"
      width="500px"
    >
      <el-form label-width="100px">
        <el-form-item label="审核人">
          <el-input v-model="auditForm.auditor" placeholder="请输入审核人姓名" />
        </el-form-item>
        <el-form-item label="审核结果">
          <el-tag :type="auditForm.auditStatus === '1' ? 'success' : 'danger'" size="large">
            {{ auditForm.auditStatus === '1' ? '通过' : '拒绝' }}
          </el-tag>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="auditVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmAudit" :loading="auditLoading">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Edit, View, Delete, CircleCheck, CircleClose } from '@element-plus/icons-vue'
import request from '@/utils/request'

// 搜索表单
const searchForm = reactive({
  userId: null,
  honorName: '',
  honorLevel: '',
  auditStatus: ''
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
const auditVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const auditLoading = ref(false)
const formRef = ref(null)

// 表单数据
const form = reactive({
  id: null,
  userId: null,
  honorName: '',
  honorLevel: '',
  awardDate: '',
  issuer: '',
  certificateUrl: '',
  description: ''
})

// 审核表单
const auditForm = reactive({
  id: null,
  auditStatus: '',
  auditor: ''
})

// 详情数据
const detailData = ref({})

// 表单验证规则
const rules = {
  userId: [
    { required: true, message: '请输入用户ID', trigger: 'blur' }
  ],
  honorName: [
    { required: true, message: '请输入荣誉名称', trigger: 'blur' }
  ],
  awardDate: [
    { required: true, message: '请选择获奖日期', trigger: 'change' }
  ]
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/alumni/honor/list', {
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
    userId: null,
    honorName: '',
    honorLevel: '',
    auditStatus: ''
  })
  handleSearch()
}

// 新增
const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, {
    id: null,
    userId: null,
    honorName: '',
    honorLevel: '',
    awardDate: '',
    issuer: '',
    certificateUrl: '',
    description: ''
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

// 审核
const handleAudit = (row, status) => {
  auditForm.id = row.id
  auditForm.auditStatus = status
  auditForm.auditor = ''
  auditVisible.value = true
}

// 确认审核
const confirmAudit = async () => {
  if (!auditForm.auditor) {
    ElMessage.warning('请输入审核人姓名')
    return
  }
  
  auditLoading.value = true
  try {
    await request.put(`/alumni/honor/${auditForm.id}/audit`, null, {
      params: {
        auditStatus: auditForm.auditStatus,
        auditor: auditForm.auditor
      }
    })
    ElMessage.success('审核成功')
    auditVisible.value = false
    loadData()
  } catch (error) {
    console.error(error)
    ElMessage.error('审核失败')
  } finally {
    auditLoading.value = false
  }
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该荣誉记录吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/alumni/honor/${row.id}`)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error(error)
      ElMessage.error('删除失败')
    }
  })
}

// 提交
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    if (isEdit.value) {
      await request.put('/alumni/honor', form)
    } else {
      await request.post('/alumni/honor', form)
    }
    
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

// 获取审核状态文本
const getAuditStatusText = (status) => {
  const map = {
    '0': '待审核',
    '1': '已通过',
    '2': '未通过'
  }
  return map[status] || '未知'
}

// 获取级别标签类型
const getLevelTagType = (level) => {
  const map = {
    '校级': 'info',
    '市级': 'success',
    '省级': 'warning',
    '国家级': 'danger'
  }
  return map[level] || ''
}

// 加载用户列表
const loadUserList = async () => {
  try {
    const res = await request.get('/user/list', {
      params: {
        pageNum: 1,
        pageSize: 1000
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
.honor-container {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
  
  .search-form {
    :deep(.el-form-item) {
      margin-bottom: 0;
    }
  }
}

.table-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .card-title {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
    }
  }
}

:deep(.el-table) {
  .el-table__header {
    th {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: #fff;
    }
  }
}
</style>
