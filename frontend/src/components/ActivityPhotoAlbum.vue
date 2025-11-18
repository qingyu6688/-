<template>
  <div class="photo-album">
    <div class="album-header">
      <h3>活动相册</h3>
      <el-button 
        v-if="canUpload" 
        type="primary" 
        :icon="Plus" 
        @click="showUploadDialog = true"
      >
        上传照片
      </el-button>
    </div>

    <!-- 照片网格 -->
    <div v-if="photos.length > 0" class="photo-grid">
      <div 
        v-for="photo in photos" 
        :key="photo.id" 
        class="photo-item"
      >
        <el-image
          :src="getFileUrl(photo.photoUrl)"
          :preview-src-list="photoUrls"
          fit="cover"
          class="photo-image"
        >
          <template #error>
            <div class="image-slot">
              <el-icon><Picture /></el-icon>
            </div>
          </template>
        </el-image>
        <div class="photo-info">
          <span class="photo-time">{{ formatTime(photo.createTime) }}</span>
          <el-button 
            v-if="canDelete(photo)" 
            type="danger" 
            size="small" 
            :icon="Delete"
            @click="handleDelete(photo.id)"
            circle
          />
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <el-empty v-else description="还没有照片，快来上传吧！" />

    <!-- 上传对话框 -->
    <el-dialog 
      v-model="showUploadDialog" 
      title="上传活动照片" 
      width="600px"
    >
      <el-upload
        ref="uploadRef"
        :auto-upload="false"
        :on-change="handleFileChange"
        :on-remove="handleFileRemove"
        :limit="50"
        accept="image/*"
        list-type="picture-card"
        multiple
      >
        <el-icon><Plus /></el-icon>
      </el-upload>
      <div class="upload-tip">
        <el-icon><InfoFilled /></el-icon>
        最多上传50张照片，支持jpg/png格式
      </div>
      <template #footer>
        <el-button @click="showUploadDialog = false">取消</el-button>
        <el-button 
          type="primary" 
          @click="handleUpload" 
          :loading="uploading"
        >
          开始上传
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete, Picture, InfoFilled } from '@element-plus/icons-vue'
import { getPhotoList, uploadPhotos, deletePhoto } from '@/api/activity'
import { getFileUrl } from '@/utils/file'
import { useUserStore } from '@/stores/user'

const props = defineProps({
  activityId: {
    type: Number,
    required: true
  },
  canUpload: {
    type: Boolean,
    default: false
  }
})

const userStore = useUserStore()

// 数据
const photos = ref([])
const showUploadDialog = ref(false)
const uploading = ref(false)
const fileList = ref([])

// 计算属性
const photoUrls = computed(() => {
  return photos.value.map(p => getFileUrl(p.photoUrl))
})

// 加载照片列表
const loadPhotos = async () => {
  try {
    const res = await getPhotoList({
      activityId: props.activityId,
      pageNum: 1,
      pageSize: 100
    })
    photos.value = res.data.records || []
  } catch (error) {
    console.error('加载照片失败:', error)
  }
}

// 文件选择
const handleFileChange = (file) => {
  fileList.value.push(file.raw)
}

// 文件移除
const handleFileRemove = (file) => {
  const index = fileList.value.findIndex(f => f.uid === file.uid)
  if (index > -1) {
    fileList.value.splice(index, 1)
  }
}

// 上传照片
const handleUpload = async () => {
  if (fileList.value.length === 0) {
    ElMessage.warning('请选择要上传的照片')
    return
  }

  try {
    uploading.value = true
    await uploadPhotos(props.activityId, userStore.userInfo.id, fileList.value)
    ElMessage.success('照片上传成功')
    showUploadDialog.value = false
    fileList.value = []
    loadPhotos()
  } catch (error) {
    console.error('上传失败:', error)
    ElMessage.error('上传失败: ' + (error.message || '未知错误'))
  } finally {
    uploading.value = false
  }
}

// 删除照片
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这张照片吗？', '提示', {
      type: 'warning'
    })
    await deletePhoto(id)
    ElMessage.success('删除成功')
    loadPhotos()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 判断是否可以删除
const canDelete = (photo) => {
  const roleKey = userStore.userInfo?.roleKey
  const userId = userStore.userInfo?.id
  
  // 管理员或上传者可以删除
  return roleKey === 'admin' || roleKey === 'super_admin' || photo.uploaderId === userId
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return date.toLocaleDateString()
}

onMounted(() => {
  loadPhotos()
})
</script>

<style scoped lang="scss">
.photo-album {
  padding: 24px;
}

.album-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  
  h3 {
    margin: 0;
    font-size: 20px;
    font-weight: 600;
    color: #333;
  }
}

.photo-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
}

.photo-item {
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  background: #f5f7fa;
  aspect-ratio: 1;
  
  &:hover {
    .photo-info {
      opacity: 1;
    }
  }
}

.photo-image {
  width: 100%;
  height: 100%;
  cursor: pointer;
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

.photo-info {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 8px 12px;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.7), transparent);
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.photo-time {
  font-size: 12px;
}

.upload-tip {
  margin-top: 12px;
  padding: 8px 12px;
  background: #f0f9ff;
  border-radius: 4px;
  color: #409eff;
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>
