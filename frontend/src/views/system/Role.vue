<template>
  <div class="role-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <div class="header-icon">
            <el-icon :size="28"><UserFilled /></el-icon>
          </div>
          <div class="header-text">
            <h2>角色管理</h2>
            <p>系统角色权限配置</p>
          </div>
        </div>
        <div class="header-right">
          <el-button type="primary" @click="handleAdd" :icon="Plus" size="large">
            新增角色
          </el-button>
        </div>
      </div>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="角色名称">
          <el-input 
            v-model="searchForm.roleName" 
            placeholder="请输入角色名称" 
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
        <el-table-column prop="roleName" label="角色名称" min-width="150" />
        <el-table-column prop="roleKey" label="角色标识" min-width="150" />
        <el-table-column prop="roleType" label="角色类型" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="getRoleTypeTag(row.roleType)" size="small">
              {{ getRoleTypeText(row.roleType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="roleLevel" label="角色级别" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.roleLevel >= 80 ? 'danger' : row.roleLevel >= 50 ? 'warning' : 'info'" size="small">
              Lv.{{ row.roleLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="dataScope" label="数据范围" width="120" align="center">
          <template #default="{ row }">
            {{ getDataScopeText(row.dataScope) }}
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(row)"
              inline-prompt
              active-text="启用"
              inactive-text="禁用"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="280" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" size="small" :icon="Key" @click="handleAssignPerms(row)">
              分配权限
            </el-button>
            <el-button type="success" size="small" :icon="Edit" @click="handleEdit(row)">
              编辑
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
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="角色名称" prop="roleName">
              <el-input v-model="form.roleName" placeholder="请输入角色名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="角色标识" prop="roleKey">
              <el-input v-model="form.roleKey" placeholder="请输入角色标识" :disabled="isEdit" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="角色类型" prop="roleType">
              <el-select v-model="form.roleType" placeholder="请选择角色类型" style="width: 100%">
                <el-option label="系统角色" value="system" />
                <el-option label="社团角色" value="club" />
                <el-option label="自定义角色" value="custom" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="角色级别" prop="roleLevel">
              <el-input-number v-model="form.roleLevel" :min="0" :max="100" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据范围" prop="dataScope">
              <el-select v-model="form.dataScope" placeholder="请选择数据范围" style="width: 100%">
                <el-option label="全部数据" value="1" />
                <el-option label="自定义数据" value="2" />
                <el-option label="本部门数据" value="3" />
                <el-option label="本部门及以下" value="4" />
                <el-option label="仅本人数据" value="5" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示顺序" prop="sortOrder">
              <el-input-number v-model="form.sortOrder" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="form.status"
            :active-value="1"
            :inactive-value="0"
            active-text="启用"
            inactive-text="禁用"
            inline-prompt
          />
        </el-form-item>
        
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入描述" />
        </el-form-item>
        
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false" :icon="Close">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading" :icon="Check">
          确定
        </el-button>
      </template>
    </el-dialog>

    <!-- 分配权限对话框 -->
    <el-dialog
      v-model="permsDialogVisible"
      title="分配权限"
      width="500px"
      @close="handlePermsDialogClose"
      :close-on-click-modal="false"
    >
      <el-tree
        ref="menuTreeRef"
        :data="menuTreeData"
        show-checkbox
        node-key="id"
        :props="{ label: 'menuName', children: 'children' }"
        default-expand-all
      />

      <template #footer>
        <el-button @click="permsDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSavePerms" :loading="permsLoading">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Edit, Delete, Check, Close, Key } from '@element-plus/icons-vue'
import { getRoleList, addRole, updateRole, deleteRole, assignMenus, getMenuIdsByRoleId } from '@/api/role'
import { getMenuTree } from '@/api/menu'

const tableData = ref([])
const menuTreeData = ref([])
const dialogVisible = ref(false)
const permsDialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const submitLoading = ref(false)
const permsLoading = ref(false)
const formRef = ref(null)
const menuTreeRef = ref(null)
const currentRoleId = ref(null)

const searchForm = reactive({
  roleName: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const form = reactive({
  id: null,
  roleName: '',
  roleKey: '',
  roleType: 'custom',
  roleLevel: 10,
  description: '',
  dataScope: '5',
  clubId: null,
  maxMembers: 0,
  status: 1,
  isDefault: 0,
  sortOrder: 0,
  remark: ''
})

const rules = {
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  roleKey: [
    { required: true, message: '请输入角色标识', trigger: 'blur' },
    { pattern: /^[a-z_]+$/, message: '角色标识只能包含小写字母和下划线', trigger: 'blur' }
  ],
  roleType: [{ required: true, message: '请选择角色类型', trigger: 'change' }],
  roleLevel: [{ required: true, message: '请输入角色级别', trigger: 'blur' }],
  dataScope: [{ required: true, message: '请选择数据范围', trigger: 'change' }]
}

const loadData = async () => {
  try {
    const res = await getRoleList({
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      roleName: searchForm.roleName
    })
    tableData.value = res.data.records
    pagination.total = res.data.total
  } catch (error) {
    console.error(error)
  }
}

const loadMenuTree = async () => {
  try {
    const res = await getMenuTree()
    menuTreeData.value = res.data
  } catch (error) {
    console.error(error)
  }
}

const handleSearch = () => {
  pagination.pageNum = 1
  loadData()
}

const handleReset = () => {
  searchForm.roleName = ''
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
  dialogTitle.value = '新增角色'
  isEdit.value = false
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑角色'
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该角色吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteRole(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error(error)
    }
  })
}

const handleAssignPerms = async (row) => {
  currentRoleId.value = row.id
  permsDialogVisible.value = true
  
  // 加载菜单树
  await loadMenuTree()
  
  // 加载已分配的菜单ID
  try {
    const res = await getMenuIdsByRoleId(row.id)
    // 设置选中的节点
    menuTreeRef.value.setCheckedKeys(res.data, false)
  } catch (error) {
    console.error(error)
  }
}

const handleSavePerms = async () => {
  permsLoading.value = true
  try {
    // 获取选中的节点（包括半选中的父节点）
    const checkedKeys = menuTreeRef.value.getCheckedKeys()
    const halfCheckedKeys = menuTreeRef.value.getHalfCheckedKeys()
    const menuIds = [...checkedKeys, ...halfCheckedKeys]
    
    await assignMenus({
      roleId: currentRoleId.value,
      menuIds: menuIds
    })
    
    ElMessage.success('权限分配成功')
    permsDialogVisible.value = false
  } catch (error) {
    console.error(error)
  } finally {
    permsLoading.value = false
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (isEdit.value) {
          await updateRole(form)
          ElMessage.success('修改成功')
        } else {
          await addRole(form)
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
    roleName: '',
    roleKey: '',
    roleType: 'custom',
    roleLevel: 10,
    description: '',
    dataScope: '5',
    clubId: null,
    maxMembers: 0,
    status: 1,
    isDefault: 0,
    sortOrder: 0,
    remark: ''
  })
}

const handleStatusChange = async (row) => {
  try {
    await updateRole(row)
    ElMessage.success('状态更新成功')
    loadData()
  } catch (error) {
    console.error(error)
    row.status = row.status === 1 ? 0 : 1
  }
}

const getRoleTypeText = (type) => {
  const map = {
    system: '系统',
    club: '社团',
    custom: '自定义'
  }
  return map[type] || '未知'
}

const getRoleTypeTag = (type) => {
  const map = {
    system: 'danger',
    club: 'primary',
    custom: 'warning'
  }
  return map[type] || 'info'
}

const getDataScopeText = (scope) => {
  const map = {
    '1': '全部',
    '2': '自定义',
    '3': '本部门',
    '4': '部门及以下',
    '5': '仅本人'
  }
  return map[scope] || '未知'
}

const handlePermsDialogClose = () => {
  menuTreeRef.value?.setCheckedKeys([], false)
  currentRoleId.value = null
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.role-container {
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
