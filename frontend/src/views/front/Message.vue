<template>
  <div class="message-page">
    <el-card class="message-card">
      <template #header>
        <div class="card-header">
          <span class="title">
            <el-icon><Bell /></el-icon>
            通知公告
          </span>
        </div>
      </template>

      <el-table
        v-loading="loading"
        :data="messages"
        style="width: 100%"
      >
        <el-table-column label="标题" width="200" show-overflow-tooltip>
          <template #default="{ row }">
            {{ row.title }}
          </template>
        </el-table-column>
        <el-table-column label="内容" min-width="300" show-overflow-tooltip>
          <template #default="{ row }">
            {{ row.content }}
          </template>
        </el-table-column>
        <el-table-column label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getNoticeTypeTag(row.noticeType)">
              {{ getNoticeTypeText(row.noticeType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="发布时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.publishTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              link
              @click="handleViewDetail(row)"
            >
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadMessages"
          @current-change="loadMessages"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Bell } from '@element-plus/icons-vue'
import { getFrontNoticeList, markNoticeAsRead } from '@/api/community'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const messages = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(20)
const total = ref(0)
const selectedMessages = ref([])

const currentUserId = computed(() => userStore.userInfo.id || userStore.userInfo.userId)

// 加载消息列表
const loadMessages = async () => {
  loading.value = true
  try {
    const res = await getFrontNoticeList({
      userId: currentUserId.value,
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    messages.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('加载通知失败:', error)
    ElMessage.error('加载通知失败')
  } finally {
    loading.value = false
  }
}

// 查看详情
const handleViewDetail = async (row) => {
  try {
    // 标记为已读
    await markNoticeAsRead(row.id, currentUserId.value)
    
    // 显示详情
    ElMessageBox.alert(row.content, row.title, {
      confirmButtonText: '确定',
      dangerouslyUseHTMLString: false
    })
    
    // 触发导航栏更新
    window.dispatchEvent(new CustomEvent('messageRead'))
    
    // 重新加载列表
    loadMessages()
  } catch (error) {
    console.error('查看详情失败:', error)
    ElMessage.error('查看详情失败')
  }
}

// 选择变化
const handleSelectionChange = (selection) => {
  selectedMessages.value = selection
}

// 获取通知类型标签
const getNoticeTypeTag = (type) => {
  const map = {
    urgent: 'danger',
    important: 'warning',
    normal: 'info'
  }
  return map[type] || 'info'
}

// 获取通知类型文本
const getNoticeTypeText = (type) => {
  const map = {
    urgent: '紧急',
    important: '重要',
    normal: '一般'
  }
  return map[type] || '未知'
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return date.toLocaleString('zh-CN')
}

onMounted(() => {
  loadMessages()
})
</script>

<style scoped lang="scss">
.message-page {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}

.message-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .title {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 16px;
      font-weight: 600;
    }
  }
}

.message-content {
  display: flex;
  align-items: center;
  gap: 8px;

  .unread-dot {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background: #f56c6c;
    flex-shrink: 0;
    display: inline-block;
  }
}

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
