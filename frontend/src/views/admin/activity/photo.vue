<template>
  <div class="app-container">
    <el-card class="search-card">
      <el-form :model="queryParams" :inline="true">
        <el-form-item label="活动名称">
          <el-input
            v-model="queryParams.activityName"
            placeholder="请输入活动名称"
            clearable
            style="width: 200px"
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="上传者">
          <el-input
            v-model="queryParams.uploaderName"
            placeholder="请输入上传者姓名"
            clearable
            style="width: 200px"
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="是否精选">
          <el-select
            v-model="queryParams.isFeatured"
            placeholder="请选择"
            clearable
            style="width: 120px"
          >
            <el-option label="是" :value="1" />
            <el-option label="否" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
          <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>活动相册列表</span>
          <el-button
            type="danger"
            :icon="Delete"
            :disabled="selectedIds.length === 0"
            @click="handleBatchDelete"
          >
            批量删除
          </el-button>
        </div>
      </template>

      <el-table
        v-loading="loading"
        :data="photoList"
        @selection-change="handleSelectionChange"
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="照片预览" align="center" width="120">
          <template #default="scope">
            <el-image
              :src="getFileUrl(scope.row.photoUrl)"
              :preview-src-list="[getFileUrl(scope.row.photoUrl)]"
              fit="cover"
              style="width: 80px; height: 80px; border-radius: 4px; cursor: pointer"
            >
              <template #error>
                <div class="image-slot">
                  <el-icon><Picture /></el-icon>
                </div>
              </template>
            </el-image>
          </template>
        </el-table-column>
        <el-table-column label="活动名称" prop="activityName" min-width="150" show-overflow-tooltip />
        <el-table-column label="上传者" prop="uploaderName" width="120" align="center" />
        <el-table-column label="描述" prop="description" min-width="200" show-overflow-tooltip>
          <template #default="scope">
            <span v-if="scope.row.description">{{ scope.row.description }}</span>
            <span v-else style="color: #999">暂无描述</span>
          </template>
        </el-table-column>
        <el-table-column label="点赞数" prop="likeCount" width="100" align="center">
          <template #default="scope">
            <el-tag type="success">{{ scope.row.likeCount || 0 }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="是否精选" width="100" align="center">
          <template #default="scope">
            <el-switch
              v-model="scope.row.isFeatured"
              :active-value="1"
              :inactive-value="0"
              @change="handleFeatureChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="上传时间" prop="createTime" width="180" align="center">
          <template #default="scope">
            {{ formatTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" align="center" fixed="right">
          <template #default="scope">
            <el-button
              type="danger"
              link
              :icon="Delete"
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="getList"
        @current-change="getList"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Delete, Picture } from '@element-plus/icons-vue'
import { getPhotoList, deletePhoto, deletePhotoBatch, setPhotoFeature } from '@/api/activity'
import { getFileUrl } from '@/utils/file'

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  activityName: '',
  uploaderName: '',
  isFeatured: null
})

// 数据
const loading = ref(false)
const photoList = ref([])
const total = ref(0)
const selectedIds = ref([])

// 获取列表
const getList = async () => {
  loading.value = true
  try {
    const res = await getPhotoList(queryParams)
    photoList.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('获取相册列表失败:', error)
    ElMessage.error('获取相册列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

// 重置
const resetQuery = () => {
  queryParams.activityName = ''
  queryParams.uploaderName = ''
  queryParams.isFeatured = null
  handleQuery()
}

// 选择变化
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

// 设置精选
const handleFeatureChange = async (row) => {
  try {
    await setPhotoFeature(row.id, row.isFeatured)
    ElMessage.success(row.isFeatured ? '已设为精选' : '已取消精选')
  } catch (error) {
    console.error('设置精选失败:', error)
    ElMessage.error('设置精选失败')
    // 恢复原状态
    row.isFeatured = row.isFeatured === 1 ? 0 : 1
  }
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除这张照片吗？', '提示', {
      type: 'warning'
    })
    await deletePhoto(row.id)
    ElMessage.success('删除成功')
    getList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 批量删除
const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedIds.value.length} 张照片吗？`, '提示', {
      type: 'warning'
    })
    await deletePhotoBatch(selectedIds.value)
    ElMessage.success('批量删除成功')
    getList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  }
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(() => {
  getList()
})
</script>

<style scoped lang="scss">
.app-container {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.table-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 30px;
}
</style>
