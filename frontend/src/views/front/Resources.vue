<template>
  <div class="resources-page">
    <!-- 页面头部 -->
    <el-card class="header-card">
      <div class="header-content">
        <div>
          <h2><el-icon><Box /></el-icon> 资源预订</h2>
          <p>预订场地、设备等资源</p>
        </div>
      </div>
    </el-card>

    <!-- 搜索和筛选 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item>
          <el-input 
            v-model="searchForm.keyword" 
            placeholder="搜索资源名称..." 
            :prefix-icon="Search"
            clearable
            style="width: 300px"
          />
        </el-form-item>
        <el-form-item>
          <el-select v-model="searchForm.resourceType" placeholder="资源类型" clearable style="width: 150px">
            <el-option label="场地" value="场地" />
            <el-option label="设备" value="设备" />
            <el-option label="器材" value="器材" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select v-model="searchForm.status" placeholder="资源状态" clearable style="width: 150px">
            <el-option label="可用" value="1" />
            <el-option label="维护中" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch" :icon="Search">搜索</el-button>
          <el-button @click="handleReset" :icon="Refresh">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 标签页 -->
    <el-card>
      <el-tabs v-model="activeTab">
        <!-- 资源列表 -->
        <el-tab-pane label="资源列表" name="list">
          <div class="resource-grid" v-loading="loading">
            <el-card 
              v-for="resource in resources" 
              :key="resource.id" 
              class="resource-card"
            >
              <div class="resource-header">
                <h3>{{ resource.resourceName }}</h3>
                <el-tag :type="resource.status === '1' ? 'success' : 'info'">
                  {{ resource.status === '1' ? '可用' : '维护中' }}
                </el-tag>
              </div>
              
              <div class="resource-info">
                <div class="info-item">
                  <span class="label">类型：</span>
                  <span>{{ resource.resourceType }}</span>
                </div>
                <div class="info-item">
                  <span class="label">位置：</span>
                  <span>{{ resource.location || '未设置' }}</span>
                </div>
                <div class="info-item">
                  <span class="label">容量：</span>
                  <span>{{ resource.capacity || '不限' }}</span>
                </div>
                <div class="info-item">
                  <span class="label">描述：</span>
                  <span>{{ resource.description || '暂无描述' }}</span>
                </div>
              </div>

              <div class="resource-footer">
                <el-button 
                  type="primary" 
                  @click="handleBook(resource)"
                  :disabled="resource.status !== '1'"
                >
                  立即预订
                </el-button>
              </div>
            </el-card>

            <el-empty v-if="!resources.length && !loading" description="暂无资源" />
          </div>

          <div class="pagination-wrapper">
            <el-pagination
              v-model:current-page="pageNum"
              v-model:page-size="pageSize"
              :total="total"
              layout="total, prev, pager, next"
              @current-change="loadResources"
            />
          </div>
        </el-tab-pane>

        <!-- 我的预订 -->
        <el-tab-pane label="我的预订" name="bookings">
          <div class="booking-list" v-loading="loadingBookings">
            <el-table :data="bookings" stripe>
              <el-table-column prop="resourceName" label="资源名称" min-width="150" />
              <el-table-column prop="bookingDate" label="预订日期" width="120" />
              <el-table-column prop="startTime" label="开始时间" width="100" />
              <el-table-column prop="endTime" label="结束时间" width="100" />
              <el-table-column prop="purpose" label="用途" min-width="150" show-overflow-tooltip />
              <el-table-column prop="auditStatus" label="审核状态" width="100" align="center">
                <template #default="{ row }">
                  <el-tag :type="getAuditStatusTag(row.auditStatus)">
                    {{ getAuditStatusText(row.auditStatus) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="100" align="center">
                <template #default="{ row }">
                  <el-button 
                    v-if="row.auditStatus === '0'" 
                    type="danger" 
                    size="small" 
                    text
                    @click="handleCancelBooking(row)"
                  >
                    取消
                  </el-button>
                </template>
              </el-table-column>
            </el-table>

            <el-empty v-if="!bookings.length && !loadingBookings" description="暂无预订记录" />
          </div>

          <div class="pagination-wrapper">
            <el-pagination
              v-model:current-page="bookingPage"
              v-model:page-size="bookingPageSize"
              :total="bookingTotal"
              layout="total, prev, pager, next"
              @current-change="loadBookings"
            />
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 预订弹窗 -->
    <el-dialog 
      v-model="showBookDialog" 
      title="预订资源" 
      width="500px"
    >
      <el-form :model="bookForm" :rules="bookRules" ref="bookFormRef" label-width="100px">
        <el-form-item label="资源名称">
          <el-input v-model="currentResource.resourceName" disabled />
        </el-form-item>
        <el-form-item label="预订日期" prop="bookingDate">
          <el-date-picker 
            v-model="bookForm.bookingDate" 
            type="date"
            placeholder="选择预订日期"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-time-picker 
            v-model="bookForm.startTime" 
            placeholder="选择开始时间"
            value-format="HH:mm"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-time-picker 
            v-model="bookForm.endTime" 
            placeholder="选择结束时间"
            value-format="HH:mm"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="预订用途" prop="purpose">
          <el-input 
            v-model="bookForm.purpose" 
            type="textarea" 
            :rows="3"
            placeholder="请说明预订用途"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showBookDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitBook" :loading="submitting">
          确认预订
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Box, Search, Refresh } from '@element-plus/icons-vue'
import { getResourceList, getBookingList, addBooking, deleteBooking } from '@/api/service'

const activeTab = ref('list')

// 搜索表单
const searchForm = reactive({
  keyword: '',
  resourceType: '',
  status: ''
})

// 资源列表
const resources = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(12)
const total = ref(0)

// 预订列表
const bookings = ref([])
const loadingBookings = ref(false)
const bookingPage = ref(1)
const bookingPageSize = ref(10)
const bookingTotal = ref(0)

// 预订弹窗
const showBookDialog = ref(false)
const submitting = ref(false)
const bookFormRef = ref(null)
const currentResource = ref({})
const bookForm = reactive({
  resourceId: null,
  userId: 1, // 临时写死
  bookingDate: '',
  startTime: '',
  endTime: '',
  purpose: ''
})

const bookRules = {
  bookingDate: [
    { required: true, message: '请选择预订日期', trigger: 'change' }
  ],
  startTime: [
    { required: true, message: '请选择开始时间', trigger: 'change' }
  ],
  endTime: [
    { required: true, message: '请选择结束时间', trigger: 'change' }
  ],
  purpose: [
    { required: true, message: '请说明预订用途', trigger: 'blur' }
  ]
}

// 加载资源列表
const loadResources = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      ...searchForm
    }
    const res = await getResourceList(params)
    resources.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('加载资源失败:', error)
  } finally {
    loading.value = false
  }
}

// 加载预订列表
const loadBookings = async () => {
  loadingBookings.value = true
  try {
    const params = {
      pageNum: bookingPage.value,
      pageSize: bookingPageSize.value,
      userId: 1 // 临时写死
    }
    const res = await getBookingList(params)
    bookings.value = res.data.records || []
    bookingTotal.value = res.data.total || 0
  } catch (error) {
    console.error('加载预订记录失败:', error)
  } finally {
    loadingBookings.value = false
  }
}

// 搜索
const handleSearch = () => {
  pageNum.value = 1
  loadResources()
}

// 重置
const handleReset = () => {
  searchForm.keyword = ''
  searchForm.resourceType = ''
  searchForm.status = ''
  handleSearch()
}

// 预订资源
const handleBook = (resource) => {
  currentResource.value = resource
  bookForm.resourceId = resource.id
  showBookDialog.value = true
}

// 提交预订
const handleSubmitBook = async () => {
  if (!bookFormRef.value) return
  
  try {
    await bookFormRef.value.validate()
    submitting.value = true
    
    await addBooking(bookForm)
    
    ElMessage.success('预订成功，等待审核')
    showBookDialog.value = false
    
    // 重置表单
    bookFormRef.value.resetFields()
    
    // 刷新预订列表
    loadBookings()
  } catch (error) {
    console.error('预订失败:', error)
    if (error !== false) {
      ElMessage.error('预订失败')
    }
  } finally {
    submitting.value = false
  }
}

// 取消预订
const handleCancelBooking = async (booking) => {
  try {
    await ElMessageBox.confirm('确定要取消此预订吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteBooking(booking.id)
    ElMessage.success('取消成功')
    loadBookings()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消预订失败:', error)
    }
  }
}

// 获取审核状态文本
const getAuditStatusText = (status) => {
  const map = { '0': '待审核', '1': '已通过', '2': '已拒绝' }
  return map[status] || '未知'
}

// 获取审核状态标签
const getAuditStatusTag = (status) => {
  const map = { '0': 'warning', '1': 'success', '2': 'danger' }
  return map[status] || 'info'
}

onMounted(() => {
  loadResources()
  loadBookings()
})
</script>

<style scoped lang="scss">
.resources-page {
  max-width: 1200px;
  margin: 0 auto;
}

.header-card {
  margin-bottom: 20px;
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
  border: none;
  color: #fff;
}

.header-card :deep(.el-card__body) {
  padding: 30px;
}

.header-content h2 {
  display: flex;
  align-items: center;
  gap: 10px;
  margin: 0 0 8px 0;
  font-size: 28px;
}

.header-content p {
  margin: 0;
  opacity: 0.9;
}

.search-card {
  margin-bottom: 20px;
}

.resource-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  min-height: 300px;
}

.resource-card {
  transition: all 0.3s ease;
}

.resource-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(17, 153, 142, 0.2);
}

.resource-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.resource-header h3 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.resource-info {
  margin-bottom: 16px;
}

.info-item {
  display: flex;
  margin-bottom: 8px;
  font-size: 14px;
  color: #666;
}

.info-item .label {
  font-weight: 600;
  min-width: 60px;
}

.resource-footer {
  display: flex;
  justify-content: flex-end;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
}

.booking-list {
  min-height: 300px;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

@media (max-width: 1024px) {
  .resource-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .resource-grid {
    grid-template-columns: 1fr;
  }
}
</style>
