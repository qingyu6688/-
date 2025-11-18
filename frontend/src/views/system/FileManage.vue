<template>
  <div class="file-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <div class="header-icon">
            <el-icon :size="28"><FolderOpened /></el-icon>
          </div>
          <div class="header-text">
            <h2>文件管理</h2>
            <p>统一管理系统文件资源</p>
          </div>
        </div>
        <div class="header-right">
          <el-upload
            :action="uploadUrl"
            :headers="getUploadHeaders()"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :show-file-list="false"
            multiple
          >
            <el-button type="primary" :icon="Upload" size="large">
              上传文件
            </el-button>
          </el-upload>
        </div>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%)">
              <el-icon :size="30"><Files /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">文件总数</div>
              <div class="stat-value">{{ statistics.totalCount || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%)">
              <el-icon :size="30"><Picture /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">图片文件</div>
              <div class="stat-value">{{ statistics.imageCount || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)">
              <el-icon :size="30"><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">文档文件</div>
              <div class="stat-value">{{ statistics.documentCount || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)">
              <el-icon :size="30"><Coin /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">总大小</div>
              <div class="stat-value">{{ formatFileSize(statistics.totalSize || 0) }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索和操作区域 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="文件名称">
          <el-input 
            v-model="searchForm.fileName" 
            placeholder="请输入文件名称" 
            clearable 
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="文件类型">
          <el-select 
            v-model="searchForm.fileType" 
            placeholder="请选择" 
            clearable
            style="width: 150px"
          >
            <el-option label="图片" value="image" />
            <el-option label="文档" value="document" />
            <el-option label="视频" value="video" />
            <el-option label="音频" value="audio" />
            <el-option label="压缩包" value="archive" />
            <el-option label="可执行文件" value="executable" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch" :icon="Search">
            搜索
          </el-button>
          <el-button @click="handleReset" :icon="Refresh">
            重置
          </el-button>
          <el-button 
            type="danger" 
            @click="handleBatchDelete" 
            :icon="Delete"
            :disabled="selectedIds.length === 0"
          >
            批量删除
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 文件列表 -->
    <el-card class="table-card">
      <el-table
        :data="tableData"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        :header-cell-style="{ 
          background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)', 
          color: '#fff', 
          fontWeight: '600',
          height: '50px'
        }"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="预览" width="100" align="center">
          <template #default="{ row }">
            <el-image 
              v-if="row.fileType === 'image'"
              :src="getFileUrl(row.fileUrl)" 
              :preview-src-list="[getFileUrl(row.fileUrl)]"
              style="width: 60px; height: 60px; border-radius: 4px"
              fit="cover"
            />
            <el-icon v-else :size="40" :color="getFileTypeColor(row.fileType)">
              <component :is="getFileTypeIcon(row.fileType)" />
            </el-icon>
          </template>
        </el-table-column>
        <el-table-column prop="originalName" label="文件名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="fileType" label="文件类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getFileTypeTagType(row.fileType)">
              {{ getFileTypeLabel(row.fileType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="fileSize" label="文件大小" width="120" align="center">
          <template #default="{ row }">
            {{ formatFileSize(row.fileSize) }}
          </template>
        </el-table-column>
        <el-table-column prop="uploadUser" label="上传用户" width="120" />
        <el-table-column prop="uploadTime" label="上传时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" size="small" :icon="View" @click="handlePreview(row)">
              预览
            </el-button>
            <el-button type="success" size="small" :icon="Download" @click="handleDownload(row)">
              下载
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

    <!-- 预览对话框 -->
    <el-dialog v-model="previewVisible" title="文件预览" width="800px">
      <div class="preview-content">
        <el-image 
          v-if="previewFile.fileType === 'image'"
          :src="getFileUrl(previewFile.fileUrl)" 
          style="width: 100%"
        />
        <div v-else class="no-preview">
          <el-icon :size="80"><Document /></el-icon>
          <p>该文件类型不支持在线预览</p>
          <el-button type="primary" @click="handleDownload(previewFile)">下载文件</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Upload, Delete, View, Download } from '@element-plus/icons-vue'
import { getFileList, getFileStatistics, deleteFile, batchDeleteFiles } from '@/api/sysfile'
import { getFileUrl } from '@/utils/file'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const uploadUrl = 'http://localhost:8080/api/file/upload'

// 动态获取token的函数
const getUploadHeaders = () => {
  return {
    Authorization: `Bearer ${userStore.token}`
  }
}

const tableData = ref([])
const selectedIds = ref([])
const previewVisible = ref(false)
const previewFile = ref({})

const statistics = ref({
  totalCount: 0,
  totalSize: 0,
  imageCount: 0,
  documentCount: 0,
  videoCount: 0,
  otherCount: 0
})

const searchForm = reactive({
  fileName: '',
  fileType: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const loadData = async () => {
  try {
    const res = await getFileList({
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      fileName: searchForm.fileName,
      fileType: searchForm.fileType
    })
    tableData.value = res.data.records
    pagination.total = res.data.total
  } catch (error) {
    console.error(error)
  }
}

const loadStatistics = async () => {
  try {
    const res = await getFileStatistics()
    statistics.value = res.data
  } catch (error) {
    console.error(error)
  }
}

const handleSearch = () => {
  pagination.pageNum = 1
  loadData()
}

const handleReset = () => {
  searchForm.fileName = ''
  searchForm.fileType = ''
  pagination.pageNum = 1
  loadData()
}

const handleSizeChange = () => {
  loadData()
}

const handleCurrentChange = () => {
  loadData()
}

const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

const handleUploadSuccess = () => {
  ElMessage.success('上传成功')
  loadData()
  loadStatistics()
}

const handleUploadError = (error) => {
  console.error('上传失败:', error)
  ElMessage.error('上传失败，请重试')
}

const handlePreview = (row) => {
  previewFile.value = row
  previewVisible.value = true
}

const handleDownload = (row) => {
  window.open(getFileUrl(row.fileUrl), '_blank')
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该文件吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteFile(row.id)
      ElMessage.success('删除成功')
      loadData()
      loadStatistics()
    } catch (error) {
      console.error(error)
    }
  })
}

const handleBatchDelete = () => {
  ElMessageBox.confirm(`确定要删除选中的 ${selectedIds.value.length} 个文件吗？`, '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await batchDeleteFiles(selectedIds.value)
      ElMessage.success('批量删除成功')
      loadData()
      loadStatistics()
    } catch (error) {
      console.error(error)
    }
  })
}

const formatFileSize = (bytes) => {
  if (!bytes || bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return (bytes / Math.pow(k, i)).toFixed(2) + ' ' + sizes[i]
}

const getFileTypeLabel = (type) => {
  const labels = {
    image: '图片',
    document: '文档',
    video: '视频',
    audio: '音频',
    archive: '压缩包',
    executable: '可执行文件',
    other: '其他'
  }
  return labels[type] || '其他'
}

const getFileTypeTagType = (type) => {
  const types = {
    image: 'success',
    document: 'primary',
    video: 'warning',
    audio: 'info',
    archive: 'danger',
    executable: '',
    other: ''
  }
  return types[type] || ''
}

const getFileTypeIcon = (type) => {
  const icons = {
    image: 'Picture',
    document: 'Document',
    video: 'VideoCamera',
    audio: 'Headset',
    archive: 'FolderOpened',
    executable: 'Setting',
    other: 'Files'
  }
  return icons[type] || 'Files'
}

const getFileTypeColor = (type) => {
  const colors = {
    image: '#67c23a',
    document: '#409eff',
    video: '#e6a23c',
    audio: '#909399',
    archive: '#f56c6c',
    executable: '#606266',
    other: '#606266'
  }
  return colors[type] || '#606266'
}

onMounted(() => {
  loadData()
  loadStatistics()
})
</script>

<style scoped>
.file-container {
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

.statistics-row {
  margin-bottom: 20px;
}

.stat-card {
  border-radius: 12px;
  border: none;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.12);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 20px;
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

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
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

.preview-content {
  text-align: center;
}

.no-preview {
  padding: 60px 0;
  color: #909399;
}

.no-preview p {
  margin: 20px 0;
  font-size: 16px;
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
