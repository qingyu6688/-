<template>
  <div class="dict-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <div class="header-icon">
            <el-icon :size="28"><Collection /></el-icon>
          </div>
          <div class="header-text">
            <h2>字典类型</h2>
            <p>系统数据字典类型管理</p>
          </div>
        </div>
        <div class="header-right">
          <el-button type="primary" @click="handleAdd" :icon="Plus" size="large">
            新增类型
          </el-button>
        </div>
      </div>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="字典名称">
          <el-input 
            v-model="searchForm.dictName" 
            placeholder="请输入字典名称" 
            clearable 
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="字典类型">
          <el-input 
            v-model="searchForm.dictType" 
            placeholder="请输入字典类型" 
            clearable 
            style="width: 200px"
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
        :header-cell-style="{ 
          background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)', 
          color: '#fff', 
          fontWeight: '600',
          height: '50px'
        }"
      >
        <el-table-column type="index" label="#" width="60" align="center" />
        <el-table-column prop="dictName" label="字典名称" min-width="150" />
        <el-table-column prop="dictType" label="字典类型" min-width="180" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              active-value="0"
              inactive-value="1"
              @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="200" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="280" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" size="small" :icon="Edit" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="success" size="small" :icon="List" @click="handleData(row)">
              字典数据
            </el-button>
            <el-button type="danger" size="small" :icon="Delete" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.pageNum"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          background
        />
      </div>
    </el-card>

    <!-- 编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
      :close-on-click-modal="false"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="字典名称" prop="dictName">
          <el-input v-model="form.dictName" placeholder="请输入字典名称" />
        </el-form-item>
        <el-form-item label="字典类型" prop="dictType">
          <el-input v-model="form.dictType" placeholder="请输入字典类型" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false" :icon="Close">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading" :icon="Check">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Edit, Delete, Check, Close, List } from '@element-plus/icons-vue'
import { getDictTypeList, addDictType, updateDictType, deleteDictType } from '@/api/dict'

const router = useRouter()
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)

const searchForm = reactive({
  dictName: '',
  dictType: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const form = reactive({
  id: null,
  dictName: '',
  dictType: '',
  status: '0',
  remark: ''
})

const rules = {
  dictName: [{ required: true, message: '请输入字典名称', trigger: 'blur' }],
  dictType: [{ required: true, message: '请输入字典类型', trigger: 'blur' }]
}

const loadData = async () => {
  try {
    const res = await getDictTypeList({
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      dictName: searchForm.dictName,
      dictType: searchForm.dictType
    })
    tableData.value = res.data.records
    pagination.total = res.data.total
  } catch (error) {
    console.error(error)
  }
}

const handleSearch = () => {
  pagination.pageNum = 1
  loadData()
}

const handleReset = () => {
  searchForm.dictName = ''
  searchForm.dictType = ''
  pagination.pageNum = 1
  loadData()
}

const handleSizeChange = () => {
  loadData()
}

const handleCurrentChange = () => {
  loadData()
}

const handleAdd = () => {
  dialogTitle.value = '新增字典类型'
  isEdit.value = false
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑字典类型'
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该字典类型吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteDictType(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error(error)
    }
  })
}

const handleStatusChange = async (row) => {
  try {
    await updateDictType(row)
    ElMessage.success('状态更新成功')
  } catch (error) {
    console.error(error)
    row.status = row.status === '0' ? '1' : '0'
  }
}

const handleData = (row) => {
  router.push({
    path: '/admin/dict-data',
    query: { dictType: row.dictType }
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (isEdit.value) {
          await updateDictType(form)
          ElMessage.success('修改成功')
        } else {
          await addDictType(form)
          ElMessage.success('新增成功')
        }
        dialogVisible.value = false
        loadData()
      } catch (error) {
        console.error(error)
      } finally {
        submitLoading.value = false
      }
    }
  })
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
  Object.assign(form, {
    id: null,
    dictName: '',
    dictType: '',
    status: '0',
    remark: ''
  })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.dict-container {
  padding: 0;
}

.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  padding: 30px;
  margin-bottom: 20px;
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.4);
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
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  backdrop-filter: blur(10px);
}

.header-text h2 {
  color: #fff;
  font-size: 28px;
  margin: 0 0 5px 0;
  font-weight: 600;
}

.header-text p {
  color: rgba(255, 255, 255, 0.9);
  margin: 0;
  font-size: 14px;
}

.search-card {
  margin-bottom: 16px;
  border-radius: 12px;
  border: none;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.search-form {
  margin: 0;
}

.search-form :deep(.el-form-item) {
  margin-bottom: 0;
}

.table-card {
  border-radius: 12px;
  border: none;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

:deep(.el-table) {
  font-size: 14px;
}

:deep(.el-table__row) {
  transition: all 0.3s;
}

:deep(.el-table__row:hover) {
  background: #f5f7fa !important;
}
</style>
