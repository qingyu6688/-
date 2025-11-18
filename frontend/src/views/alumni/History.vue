<template>
  <div class="club-history-container">
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
        <el-form-item label="社团名称">
          <el-input 
            v-model="searchForm.clubName" 
            placeholder="请输入社团名称" 
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="职务">
          <el-input 
            v-model="searchForm.position" 
            placeholder="请输入职务" 
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="在任状态">
          <el-select 
            v-model="searchForm.isCurrent" 
            placeholder="请选择状态" 
            clearable
            style="width: 150px"
          >
            <el-option label="当前在任" :value="true" />
            <el-option label="已离任" :value="false" />
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
          <span class="card-title">社团经历列表</span>
          <el-button type="primary" @click="handleAdd" :icon="Plus">
            新增社团经历
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
        <el-table-column prop="clubName" label="社团名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="position" label="担任职务" width="140" />
        <el-table-column prop="startDate" label="开始日期" width="120" align="center" />
        <el-table-column prop="endDate" label="结束日期" width="120" align="center">
          <template #default="{ row }">
            {{ row.endDate || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="isCurrent" label="在任状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.isCurrent ? 'success' : 'info'" size="small">
              {{ row.isCurrent ? '当前在任' : '已离任' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="经历描述" min-width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="220" align="center" fixed="right">
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
      :title="isEdit ? '编辑社团经历' : '新增社团经历'"
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
        <el-form-item label="社团名称" prop="clubName">
          <el-input v-model="form.clubName" placeholder="请输入社团名称" />
        </el-form-item>
        <el-form-item label="担任职务" prop="position">
          <el-input v-model="form.position" placeholder="请输入担任职务" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker
                v-model="form.startDate"
                type="date"
                placeholder="选择开始日期"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期" prop="endDate">
              <el-date-picker
                v-model="form.endDate"
                type="date"
                placeholder="选择结束日期"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="当前在任">
          <el-switch v-model="form.isCurrent" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="经历描述" prop="description">
          <el-input 
            v-model="form.description" 
            type="textarea" 
            :rows="4"
            placeholder="请输入经历描述"
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
      title="社团经历详情"
      width="650px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="用户ID">{{ detailData.userId }}</el-descriptions-item>
        <el-descriptions-item label="社团名称">{{ detailData.clubName }}</el-descriptions-item>
        <el-descriptions-item label="担任职务">{{ detailData.position || '-' }}</el-descriptions-item>
        <el-descriptions-item label="在任状态">
          <el-tag :type="detailData.isCurrent ? 'success' : 'info'" size="small">
            {{ detailData.isCurrent ? '当前在任' : '已离任' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="开始日期">{{ detailData.startDate }}</el-descriptions-item>
        <el-descriptions-item label="结束日期">{{ detailData.endDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="经历描述" :span="2">
          {{ detailData.description || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detailData.updateTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Edit, View, Delete } from '@element-plus/icons-vue'
import request from '@/utils/request'

// 搜索表单
const searchForm = reactive({
  userId: null,
  clubName: '',
  position: '',
  isCurrent: null
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
  clubName: '',
  position: '',
  startDate: '',
  endDate: '',
  description: '',
  isCurrent: 0
})

// 详情数据
const detailData = ref({})

// 表单验证规则
const rules = {
  userId: [
    { required: true, message: '请输入用户ID', trigger: 'blur' }
  ],
  clubName: [
    { required: true, message: '请输入社团名称', trigger: 'blur' }
  ],
  startDate: [
    { required: true, message: '请选择开始日期', trigger: 'change' }
  ]
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/alumni/club-history/list', {
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
    clubName: '',
    position: '',
    isCurrent: null
  })
  handleSearch()
}

// 新增
const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, {
    id: null,
    userId: null,
    clubName: '',
    position: '',
    startDate: '',
    endDate: '',
    description: '',
    isCurrent: 0
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

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该社团经历吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/alumni/club-history/${row.id}`)
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
      await request.put('/alumni/club-history', form)
    } else {
      await request.post('/alumni/club-history', form)
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
.club-history-container {
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
