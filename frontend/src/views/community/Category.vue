<template>
  <div class="category-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <div class="header-icon">
            <el-icon :size="28"><Grid /></el-icon>
          </div>
          <div class="header-text">
            <h2>板块管理</h2>
            <p>管理论坛板块分类</p>
          </div>
        </div>
        <div class="header-right">
          <el-button type="primary" @click="handleAdd" :icon="Plus" size="large">
            新增板块
          </el-button>
        </div>
      </div>
    </div>

    <!-- 板块列表 -->
    <el-row :gutter="16">
      <el-col :span="6" v-for="category in categoryList" :key="category.id">
        <el-card class="category-card" shadow="hover">
          <div class="category-icon">
            <el-icon :size="40"><Folder /></el-icon>
          </div>
          <div class="category-name">{{ category.categoryName }}</div>
          <div class="category-desc">{{ category.description || '暂无描述' }}</div>
          <div class="category-stats">
            <span>帖子数: {{ category.postCount || 0 }}</span>
            <span>排序: {{ category.sortOrder }}</span>
          </div>
          <div class="category-actions">
            <el-button type="primary" link @click="handleEdit(category)" :icon="Edit">
              编辑
            </el-button>
            <el-button type="danger" link @click="handleDelete(category)" :icon="Delete">
              删除
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 新增/编辑弹窗 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="isEdit ? '编辑板块' : '新增板块'"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form 
        ref="formRef" 
        :model="form" 
        :rules="rules" 
        label-width="100px"
      >
        <el-form-item label="板块名称" prop="categoryName">
          <el-input v-model="form.categoryName" placeholder="请输入板块名称" />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="板块描述" prop="description">
          <el-input 
            v-model="form.description" 
            type="textarea" 
            :rows="4"
            placeholder="请输入板块描述"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">停用</el-radio>
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Grid, Folder } from '@element-plus/icons-vue'
import { getCategoryList, addCategory, updateCategory, deleteCategory } from '@/api/community'

// 板块列表
const categoryList = ref([])

// 弹窗相关
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)

// 表单数据
const form = reactive({
  id: null,
  categoryName: '',
  sortOrder: 0,
  description: '',
  status: '0'
})

// 表单验证规则
const rules = {
  categoryName: [
    { required: true, message: '请输入板块名称', trigger: 'blur' }
  ],
  sortOrder: [
    { required: true, message: '请输入排序', trigger: 'blur' }
  ]
}

// 加载数据
const loadData = async () => {
  try {
    const res = await getCategoryList()
    categoryList.value = res.data
  } catch (error) {
    console.error(error)
    ElMessage.error('加载数据失败')
  }
}

// 新增
const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, {
    id: null,
    categoryName: '',
    sortOrder: 0,
    description: '',
    status: '0'
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, { ...row })
  dialogVisible.value = true
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该板块吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteCategory(row.id)
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
      await updateCategory(form)
    } else {
      await addCategory(form)
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

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
@import '@/styles/common-page.scss';

.category-card {
  margin-bottom: 20px;
  text-align: center;
  transition: all 0.3s;
  
  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  }
}

.category-icon {
  color: #667eea;
  margin-bottom: 15px;
}

.category-name {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 10px;
}

.category-desc {
  font-size: 14px;
  color: #909399;
  margin-bottom: 15px;
  min-height: 40px;
}

.category-stats {
  display: flex;
  justify-content: space-around;
  padding: 10px 0;
  border-top: 1px solid #f0f2f5;
  border-bottom: 1px solid #f0f2f5;
  margin-bottom: 15px;
  font-size: 13px;
  color: #606266;
}

.category-actions {
  display: flex;
  justify-content: center;
  gap: 10px;
}
</style>
