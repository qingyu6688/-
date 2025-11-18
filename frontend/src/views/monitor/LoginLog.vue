<template>
  <div class="log-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <div class="header-icon">
            <el-icon :size="28"><Key /></el-icon>
          </div>
          <div class="header-text">
            <h2>登录日志</h2>
            <p>系统登录访问记录</p>
          </div>
        </div>
        <div class="header-right">
          <el-button type="danger" @click="handleClean" :icon="Delete">
            清空日志
          </el-button>
        </div>
      </div>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户名">
          <el-input 
            v-model="searchForm.username" 
            placeholder="请输入用户名" 
            clearable 
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="登录状态">
          <el-select 
            v-model="searchForm.status" 
            placeholder="请选择" 
            clearable
            style="width: 150px"
          >
            <el-option label="成功" value="0" />
            <el-option label="失败" value="1" />
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
        :header-cell-style="{ 
          background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)', 
          color: '#fff', 
          fontWeight: '600',
          height: '50px'
        }"
      >
        <el-table-column type="index" label="#" width="60" align="center" />
        <el-table-column prop="username" label="用户名" width="150">
          <template #default="{ row }">
            <div class="user-cell">
              <el-icon><User /></el-icon>
              <span>{{ row.username }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="ipAddress" label="登录IP" width="150" />
        <el-table-column prop="loginLocation" label="登录地点" width="150" />
        <el-table-column prop="browser" label="浏览器" width="150" />
        <el-table-column prop="os" label="操作系统" width="150" />
        <el-table-column prop="status" label="登录状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.status === '0'" type="success">成功</el-tag>
            <el-tag v-else type="danger">失败</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="msg" label="提示消息" min-width="150" show-overflow-tooltip />
        <el-table-column prop="loginTime" label="登录时间" width="180" />
        <el-table-column label="操作" width="100" fixed="right" align="center">
          <template #default="{ row }">
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Delete, Key } from '@element-plus/icons-vue'
import { getLoginLogList, deleteLoginLog, cleanLoginLog } from '@/api/log'

const tableData = ref([])

const searchForm = reactive({
  username: '',
  status: null
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const loadData = async () => {
  try {
    const res = await getLoginLogList({
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      username: searchForm.username,
      status: searchForm.status
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
  searchForm.username = ''
  searchForm.status = null
  pagination.pageNum = 1
  loadData()
}

const handleSizeChange = () => {
  loadData()
}

const handleCurrentChange = () => {
  loadData()
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该日志吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteLoginLog(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error(error)
    }
  })
}

const handleClean = () => {
  ElMessageBox.confirm('确定要清空所有登录日志吗？此操作不可恢复！', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await cleanLoginLog()
      ElMessage.success('清空成功')
      loadData()
    } catch (error) {
      console.error(error)
    }
  })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.log-container {
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

.user-cell {
  display: flex;
  align-items: center;
  gap: 8px;
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
