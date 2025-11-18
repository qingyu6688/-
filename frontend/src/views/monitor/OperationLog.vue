<template>
  <div class="log-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <div class="header-icon">
            <el-icon :size="28"><Document /></el-icon>
          </div>
          <div class="header-text">
            <h2>操作日志</h2>
            <p>系统操作行为记录</p>
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
        <el-form-item label="模块标题">
          <el-input 
            v-model="searchForm.title" 
            placeholder="请输入模块标题" 
            clearable 
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="操作人员">
          <el-input 
            v-model="searchForm.operatorName" 
            placeholder="请输入操作人员" 
            clearable 
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="业务类型">
          <el-select 
            v-model="searchForm.businessType" 
            placeholder="请选择" 
            clearable
            style="width: 150px"
          >
            <el-option label="其它" :value="0" />
            <el-option label="新增" :value="1" />
            <el-option label="修改" :value="2" />
            <el-option label="删除" :value="3" />
            <el-option label="查询" :value="4" />
            <el-option label="导出" :value="5" />
            <el-option label="导入" :value="6" />
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
        <el-table-column prop="title" label="模块标题" width="120" />
        <el-table-column prop="businessType" label="业务类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.businessType === 1" type="success">新增</el-tag>
            <el-tag v-else-if="row.businessType === 2" type="primary">修改</el-tag>
            <el-tag v-else-if="row.businessType === 3" type="danger">删除</el-tag>
            <el-tag v-else-if="row.businessType === 4" type="info">查询</el-tag>
            <el-tag v-else>其它</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operatorName" label="操作人员" width="120" />
        <el-table-column prop="operatorIp" label="操作IP" width="140" />
        <el-table-column prop="operatorUrl" label="请求地址" min-width="200" show-overflow-tooltip />
        <el-table-column prop="requestMethod" label="请求方式" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.requestMethod === 'GET'" type="success" size="small">GET</el-tag>
            <el-tag v-else-if="row.requestMethod === 'POST'" type="primary" size="small">POST</el-tag>
            <el-tag v-else-if="row.requestMethod === 'PUT'" type="warning" size="small">PUT</el-tag>
            <el-tag v-else-if="row.requestMethod === 'DELETE'" type="danger" size="small">DELETE</el-tag>
            <el-tag v-else size="small">{{ row.requestMethod }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.status === 0" type="success">成功</el-tag>
            <el-tag v-else type="danger">失败</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="costTime" label="耗时(ms)" width="100" align="center" />
        <el-table-column prop="operatorTime" label="操作时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" size="small" :icon="View" @click="handleView(row)">
              详情
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

    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="操作日志详情" width="800px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="模块标题">{{ detailData.title }}</el-descriptions-item>
        <el-descriptions-item label="业务类型">
          <el-tag v-if="detailData.businessType === 1" type="success">新增</el-tag>
          <el-tag v-else-if="detailData.businessType === 2" type="primary">修改</el-tag>
          <el-tag v-else-if="detailData.businessType === 3" type="danger">删除</el-tag>
          <el-tag v-else>其它</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="操作人员">{{ detailData.operatorName }}</el-descriptions-item>
        <el-descriptions-item label="操作IP">{{ detailData.operatorIp }}</el-descriptions-item>
        <el-descriptions-item label="请求方式">{{ detailData.requestMethod }}</el-descriptions-item>
        <el-descriptions-item label="操作状态">
          <el-tag v-if="detailData.status === 0" type="success">成功</el-tag>
          <el-tag v-else type="danger">失败</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="请求地址" :span="2">{{ detailData.operatorUrl }}</el-descriptions-item>
        <el-descriptions-item label="方法名称" :span="2">{{ detailData.method }}</el-descriptions-item>
        <el-descriptions-item label="请求参数" :span="2">
          <el-input type="textarea" :rows="3" v-model="detailData.operatorParam" readonly />
        </el-descriptions-item>
        <el-descriptions-item label="返回参数" :span="2">
          <el-input type="textarea" :rows="3" v-model="detailData.jsonResult" readonly />
        </el-descriptions-item>
        <el-descriptions-item label="错误消息" :span="2" v-if="detailData.errorMsg">
          <el-input type="textarea" :rows="3" v-model="detailData.errorMsg" readonly />
        </el-descriptions-item>
        <el-descriptions-item label="操作时间">{{ detailData.operatorTime }}</el-descriptions-item>
        <el-descriptions-item label="消耗时间">{{ detailData.costTime }}ms</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Delete, View } from '@element-plus/icons-vue'
import { getOperationLogList, deleteOperationLog, cleanOperationLog } from '@/api/log'

const tableData = ref([])
const detailVisible = ref(false)
const detailData = ref({})

const searchForm = reactive({
  title: '',
  operatorName: '',
  businessType: null
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const loadData = async () => {
  try {
    const res = await getOperationLogList({
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      title: searchForm.title,
      operatorName: searchForm.operatorName,
      businessType: searchForm.businessType
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
  searchForm.title = ''
  searchForm.operatorName = ''
  searchForm.businessType = null
  pagination.pageNum = 1
  loadData()
}

const handleSizeChange = () => {
  loadData()
}

const handleCurrentChange = () => {
  loadData()
}

const handleView = (row) => {
  detailData.value = row
  detailVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该日志吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteOperationLog(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error(error)
    }
  })
}

const handleClean = () => {
  ElMessageBox.confirm('确定要清空所有操作日志吗？此操作不可恢复！', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await cleanOperationLog()
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
