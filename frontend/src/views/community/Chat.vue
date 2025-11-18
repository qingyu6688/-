<template>
  <div class="chat-manage-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <el-icon class="header-icon"><ChatDotSquare /></el-icon>
          <div class="header-text">
            <h2>聊天管理</h2>
            <p>管理用户私信和聊天记录</p>
          </div>
        </div>
        <div class="header-actions">
          <el-button type="primary" :icon="Refresh" @click="loadData">刷新</el-button>
        </div>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-icon total">
            <el-icon><ChatDotRound /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.totalMessages }}</div>
            <div class="stat-label">消息总数</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-icon today">
            <el-icon><Clock /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.todayMessages }}</div>
            <div class="stat-label">今日消息</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-icon users">
            <el-icon><User /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.activeUsers }}</div>
            <div class="stat-label">活跃用户</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-icon reported">
            <el-icon><Warning /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.reportedMessages }}</div>
            <div class="stat-label">被举报消息</div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card">
      <div class="search-header">
        <el-icon><Search /></el-icon>
        <span>搜索筛选</span>
      </div>
      <el-form :model="queryParams" :inline="true">
        <el-form-item label="发送者">
          <el-input v-model="queryParams.senderName" placeholder="请输入发送者" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="接收者">
          <el-input v-model="queryParams.receiverName" placeholder="请输入接收者" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="消息类型">
          <el-select v-model="queryParams.messageType" placeholder="请选择消息类型" clearable style="width: 150px">
            <el-option label="文本" value="text" />
            <el-option label="图片" value="image" />
            <el-option label="文件" value="file" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 150px">
            <el-option label="正常" value="0" />
            <el-option label="已撤回" value="1" />
            <el-option label="已删除" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
          <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 消息列表 -->
    <el-card class="table-card">
      <el-table :data="messageList" v-loading="loading" stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="senderName" label="发送者" width="120" align="center" />
        <el-table-column prop="receiverName" label="接收者" width="120" align="center" />
        <el-table-column prop="contentType" label="消息类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.contentType === 'text'" type="primary" size="small">文本</el-tag>
            <el-tag v-else-if="row.contentType === 'image'" type="success" size="small">图片</el-tag>
            <el-tag v-else-if="row.contentType === 'file'" type="warning" size="small">文件</el-tag>
            <el-tag v-else type="info" size="small">其他</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="消息内容" min-width="200" show-overflow-tooltip />
        <el-table-column prop="isRead" label="已读状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.isRead === 1" type="success" size="small">已读</el-tag>
            <el-tag v-else type="info" size="small">未读</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.status === '0'" type="success" size="small">正常</el-tag>
            <el-tag v-else-if="row.status === '1'" type="warning" size="small">已撤回</el-tag>
            <el-tag v-else type="danger" size="small">已删除</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发送时间" width="160" align="center" />
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" :icon="View" @click="handleView(row)">查看</el-button>
            <el-button link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadData"
        @current-change="loadData"
      />
    </el-card>

    <!-- 查看详情对话框 -->
    <el-dialog v-model="dialogVisible" title="消息详情" width="600px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="发送者">{{ currentMessage.senderName }}</el-descriptions-item>
        <el-descriptions-item label="接收者">{{ currentMessage.receiverName }}</el-descriptions-item>
        <el-descriptions-item label="消息类型">
          <el-tag v-if="currentMessage.contentType === 'text'" type="primary" size="small">文本</el-tag>
          <el-tag v-else-if="currentMessage.contentType === 'image'" type="success" size="small">图片</el-tag>
          <el-tag v-else-if="currentMessage.contentType === 'file'" type="warning" size="small">文件</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="消息内容">{{ currentMessage.content }}</el-descriptions-item>
        <el-descriptions-item label="已读状态">
          <el-tag v-if="currentMessage.isRead === 1" type="success" size="small">已读</el-tag>
          <el-tag v-else type="info" size="small">未读</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="currentMessage.status === '0'" type="success" size="small">正常</el-tag>
          <el-tag v-else-if="currentMessage.status === '1'" type="warning" size="small">已撤回</el-tag>
          <el-tag v-else type="danger" size="small">已删除</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="发送时间">{{ currentMessage.createTime }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="dialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ChatDotSquare, ChatDotRound, Clock, User, Warning, Search, Refresh, View, Delete } from '@element-plus/icons-vue'
import { getChatStatistics, getChatMessageList, deleteChatMessage } from '@/api/community'

// 统计数据
const stats = reactive({
  totalMessages: 0,
  todayMessages: 0,
  activeUsers: 0,
  reportedMessages: 0
})

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  senderName: '',
  receiverName: '',
  messageType: '',
  status: ''
})

// 数据
const loading = ref(false)
const total = ref(0)
const messageList = ref([])
const dialogVisible = ref(false)
const currentMessage = ref({})

// 加载统计数据
const loadStatistics = async () => {
  try {
    const res = await getChatStatistics()
    if (res.code === 200) {
      stats.totalMessages = res.data.totalMessages || 0
      stats.todayMessages = res.data.todayMessages || 0
      stats.activeUsers = res.data.activeUsers || 0
      stats.reportedMessages = res.data.reportedMessages || 0
    }
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

// 加载消息列表
const loadData = async () => {
  loading.value = true
  try {
    const res = await getChatMessageList(queryParams)
    if (res.code === 200) {
      messageList.value = res.data.records || []
      total.value = res.data.total || 0
    }
  } catch (error) {
    console.error('加载消息列表失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleQuery = () => {
  queryParams.pageNum = 1
  loadData()
}

// 重置
const resetQuery = () => {
  queryParams.senderName = ''
  queryParams.receiverName = ''
  queryParams.messageType = ''
  queryParams.status = ''
  handleQuery()
}

// 查看详情
const handleView = (row) => {
  currentMessage.value = { ...row }
  dialogVisible.value = true
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除这条消息吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await deleteChatMessage(row.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadData()
      loadStatistics()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  loadStatistics()
  loadData()
})
</script>

<style scoped>
.chat-manage-page {
  padding: 20px;
}

.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 24px;
  color: white;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-icon {
  font-size: 48px;
  opacity: 0.9;
}

.header-text h2 {
  margin: 0 0 8px 0;
  font-size: 24px;
  font-weight: 600;
}

.header-text p {
  margin: 0;
  font-size: 14px;
  opacity: 0.9;
}

.stats-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  border-radius: 12px;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.2);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: white;
}

.stat-icon.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.today {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.users {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.reported {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #333;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.search-card {
  margin-bottom: 24px;
  border-radius: 12px;
}

.search-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #667eea;
  margin-bottom: 16px;
}

.table-card {
  border-radius: 12px;
}

.el-pagination {
  margin-top: 20px;
  justify-content: flex-end;
}

@media (max-width: 1200px) {
  .stats-cards {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .stats-cards {
    grid-template-columns: 1fr;
  }
  
  .header-content {
    flex-direction: column;
    gap: 16px;
  }
}
</style>
