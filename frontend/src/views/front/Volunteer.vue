<template>
  <div class="volunteer-page">
    <!-- 页面头部 -->
    <el-card class="header-card">
      <div class="header-content">
        <div>
          <h2><el-icon><MagicStick /></el-icon> 志愿服务</h2>
          <p>奉献爱心，传递温暖</p>
        </div>
        <div class="header-stats">
          <div class="stat-item">
            <div class="stat-value">{{ totalHours }}</div>
            <div class="stat-label">累计时长(小时)</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ myRecords }}</div>
            <div class="stat-label">我的记录</div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 标签页 -->
    <el-card>
      <el-tabs v-model="activeTab">
        <!-- 志愿活动 -->
        <el-tab-pane label="志愿活动" name="activities">
          <div class="search-bar">
            <el-input 
              v-model="searchKeyword" 
              placeholder="搜索志愿活动..." 
              :prefix-icon="Search"
              clearable
              style="width: 300px"
              @keyup.enter="loadActivities"
            />
            <el-button type="primary" @click="loadActivities" :icon="Search">搜索</el-button>
          </div>

          <div class="volunteer-list" v-loading="loadingActivities">
            <el-card 
              v-for="activity in activities" 
              :key="activity.id" 
              class="volunteer-card"
            >
              <div class="volunteer-header">
                <h3>{{ activity.activityName }}</h3>
                <el-tag :type="getStatusTag(activity.status)">
                  {{ getStatusText(activity.status) }}
                </el-tag>
              </div>
              
              <p class="volunteer-desc">{{ activity.description }}</p>
              
              <div class="volunteer-info">
                <div class="info-row">
                  <span><el-icon><Location /></el-icon> {{ activity.location }}</span>
                  <span><el-icon><Clock /></el-icon> {{ activity.volunteerHours }}小时</span>
                </div>
                <div class="info-row">
                  <span><el-icon><Calendar /></el-icon> {{ formatDate(activity.startTime) }}</span>
                  <span><el-icon><User /></el-icon> {{ activity.currentParticipants || 0 }}/{{ activity.maxParticipants || '不限' }}</span>
                </div>
              </div>

              <div class="volunteer-footer">
                <el-button 
                  type="primary" 
                  @click="handleJoin(activity)"
                  :disabled="activity.status !== '1'"
                >
                  {{ activity.status === '1' ? '我要参加' : '已结束' }}
                </el-button>
              </div>
            </el-card>

            <el-empty v-if="!activities.length && !loadingActivities" description="暂无志愿活动" />
          </div>

          <div class="pagination-wrapper">
            <el-pagination
              v-model:current-page="activityPage"
              v-model:page-size="activityPageSize"
              :total="activityTotal"
              layout="total, prev, pager, next"
              @current-change="loadActivities"
            />
          </div>
        </el-tab-pane>

        <!-- 我的记录 -->
        <el-tab-pane label="我的记录" name="records">
          <div class="record-list" v-loading="loadingRecords">
            <el-table :data="records" stripe>
              <el-table-column prop="activityName" label="活动名称" min-width="200" />
              <el-table-column prop="volunteerHours" label="服务时长" width="100" align="center">
                <template #default="{ row }">
                  {{ row.volunteerHours }}小时
                </template>
              </el-table-column>
              <el-table-column prop="serviceDate" label="服务日期" width="120" />
              <el-table-column prop="location" label="服务地点" min-width="150" />
              <el-table-column prop="confirmStatus" label="确认状态" width="100" align="center">
                <template #default="{ row }">
                  <el-tag :type="row.confirmStatus === '1' ? 'success' : 'warning'">
                    {{ row.confirmStatus === '1' ? '已确认' : '待确认' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="记录时间" width="180" />
            </el-table>

            <el-empty v-if="!records.length && !loadingRecords" description="暂无记录" />
          </div>

          <div class="pagination-wrapper">
            <el-pagination
              v-model:current-page="recordPage"
              v-model:page-size="recordPageSize"
              :total="recordTotal"
              layout="total, prev, pager, next"
              @current-change="loadRecords"
            />
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 参加弹窗 -->
    <el-dialog 
      v-model="showJoinDialog" 
      title="参加志愿活动" 
      width="500px"
    >
      <el-form :model="joinForm" :rules="joinRules" ref="joinFormRef" label-width="100px">
        <el-form-item label="活动名称">
          <el-input v-model="currentActivity.activityName" disabled />
        </el-form-item>
        <el-form-item label="服务日期" prop="serviceDate">
          <el-date-picker 
            v-model="joinForm.serviceDate" 
            type="date"
            placeholder="选择服务日期"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input 
            v-model="joinForm.remark" 
            type="textarea" 
            :rows="3"
            placeholder="其他需要说明的信息（选填）"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showJoinDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitJoin" :loading="submitting">
          确认参加
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { MagicStick, Search, Location, Clock, Calendar, User } from '@element-plus/icons-vue'
import { getVolunteerActivityList, addVolunteerRecord, getVolunteerRecordList } from '@/api/service'

const activeTab = ref('activities')
const searchKeyword = ref('')

// 统计数据
const totalHours = ref(0)
const myRecords = ref(0)

// 志愿活动
const activities = ref([])
const loadingActivities = ref(false)
const activityPage = ref(1)
const activityPageSize = ref(10)
const activityTotal = ref(0)

// 志愿记录
const records = ref([])
const loadingRecords = ref(false)
const recordPage = ref(1)
const recordPageSize = ref(10)
const recordTotal = ref(0)

// 参加弹窗
const showJoinDialog = ref(false)
const submitting = ref(false)
const joinFormRef = ref(null)
const currentActivity = ref({})
const joinForm = reactive({
  activityId: null,
  userId: 1, // 临时写死
  serviceDate: '',
  remark: ''
})

const joinRules = {
  serviceDate: [
    { required: true, message: '请选择服务日期', trigger: 'change' }
  ]
}

// 加载志愿活动
const loadActivities = async () => {
  loadingActivities.value = true
  try {
    const params = {
      pageNum: activityPage.value,
      pageSize: activityPageSize.value,
      keyword: searchKeyword.value,
      status: '1' // 招募中
    }
    const res = await getVolunteerActivityList(params)
    activities.value = res.data.records || []
    activityTotal.value = res.data.total || 0
  } catch (error) {
    console.error('加载志愿活动失败:', error)
  } finally {
    loadingActivities.value = false
  }
}

// 加载志愿记录
const loadRecords = async () => {
  loadingRecords.value = true
  try {
    const params = {
      pageNum: recordPage.value,
      pageSize: recordPageSize.value,
      userId: 1 // 临时写死
    }
    const res = await getVolunteerRecordList(params)
    records.value = res.data.records || []
    recordTotal.value = res.data.total || 0
    myRecords.value = res.data.total || 0
    
    // 计算总时长
    totalHours.value = records.value.reduce((sum, record) => {
      return sum + (record.volunteerHours || 0)
    }, 0)
  } catch (error) {
    console.error('加载志愿记录失败:', error)
  } finally {
    loadingRecords.value = false
  }
}

// 参加活动
const handleJoin = (activity) => {
  currentActivity.value = activity
  joinForm.activityId = activity.id
  showJoinDialog.value = true
}

// 提交参加
const handleSubmitJoin = async () => {
  if (!joinFormRef.value) return
  
  try {
    await joinFormRef.value.validate()
    submitting.value = true
    
    await addVolunteerRecord(joinForm)
    
    ElMessage.success('报名成功')
    showJoinDialog.value = false
    
    // 重置表单
    joinFormRef.value.resetFields()
    
    // 刷新记录
    loadRecords()
  } catch (error) {
    console.error('报名失败:', error)
    if (error !== false) {
      ElMessage.error('报名失败')
    }
  } finally {
    submitting.value = false
  }
}

// 获取状态文本
const getStatusText = (status) => {
  const map = { '0': '筹备中', '1': '招募中', '2': '进行中', '3': '已结束' }
  return map[status] || '未知'
}

// 获取状态标签
const getStatusTag = (status) => {
  const map = { '0': 'info', '1': 'success', '2': 'primary', '3': 'info' }
  return map[status] || 'info'
}

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return `${date.getMonth() + 1}月${date.getDate()}日`
}

onMounted(() => {
  loadActivities()
  loadRecords()
})
</script>

<style scoped lang="scss">
.volunteer-page {
  max-width: 1200px;
  margin: 0 auto;
}

.header-card {
  margin-bottom: 20px;
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
  border: none;
  color: #fff;
}

.header-card :deep(.el-card__body) {
  padding: 30px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
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

.header-stats {
  display: flex;
  gap: 40px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 13px;
  opacity: 0.9;
}

.search-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.volunteer-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  min-height: 300px;
}

.volunteer-card {
  transition: all 0.3s ease;
}

.volunteer-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(250, 112, 154, 0.2);
}

.volunteer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.volunteer-header h3 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.volunteer-desc {
  margin: 0 0 16px 0;
  color: #666;
  font-size: 14px;
  line-height: 1.6;
  height: 42px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.volunteer-info {
  margin-bottom: 16px;
  padding: 12px;
  background: #f8f9fa;
  border-radius: 8px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 13px;
  color: #666;
}

.info-row:last-child {
  margin-bottom: 0;
}

.info-row span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.volunteer-footer {
  display: flex;
  justify-content: flex-end;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
}

.record-list {
  min-height: 300px;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

@media (max-width: 768px) {
  .volunteer-list {
    grid-template-columns: 1fr;
  }
  
  .header-stats {
    gap: 20px;
  }
}
</style>
