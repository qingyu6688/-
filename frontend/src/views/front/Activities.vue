<template>
  <div class="activities-page">
    <!-- 页面头部 -->
    <div class="hero-section">
      <div class="hero-content">
        <div class="hero-text">
          <h1 class="hero-title">
            <span class="title-icon">🎉</span>
            活动中心
          </h1>
          <p class="hero-subtitle">发现精彩 · 参与活动 · 收获成长</p>
          <div class="hero-stats">
            <div class="stat-item">
              <div class="stat-number">{{ total }}</div>
              <div class="stat-label">活动总数</div>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <div class="stat-number">{{ activities.filter(a => a && a.status === '0').length }}</div>
              <div class="stat-label">报名中</div>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <div class="stat-number">{{ registeredActivityIds.length }}</div>
              <div class="stat-label">我的报名</div>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item stat-action" v-if="canCreateActivity">
              <el-button 
                type="primary" 
                :icon="Plus" 
                @click="showCreateDialog = true"
                size="large"
                round
                class="create-activity-btn"
              >
                新增活动
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Tab切换 -->
    <div class="tab-section">
      <div class="tab-buttons">
        <button 
          class="tab-btn" 
          :class="{ active: activeTab === 'all' }"
          @click="activeTab = 'all'; handleTabChange('all')"
        >
          <span class="tab-icon">🌟</span>
          全部活动
        </button>
        <button 
          class="tab-btn" 
          :class="{ active: activeTab === 'myRegistration' }"
          @click="activeTab = 'myRegistration'; handleTabChange('myRegistration')"
        >
          <span class="tab-icon">📝</span>
          我的报名
        </button>
        <button 
          v-if="canCreateActivity"
          class="tab-btn" 
          :class="{ active: activeTab === 'myCreated' }"
          @click="activeTab = 'myCreated'; handleTabChange('myCreated')"
        >
          <span class="tab-icon">✨</span>
          我创建的
        </button>
      </div>
    </div>

    <!-- 搜索和筛选 -->
    <div class="search-section">
      <div class="search-container">
        <el-input 
          v-model="searchForm.keyword" 
          placeholder="🔍 搜索你感兴趣的活动..." 
          clearable
          size="large"
          class="search-input"
        >
          <template #append>
            <el-button :icon="Search" @click="handleSearch">搜索</el-button>
          </template>
        </el-input>
        <div class="filter-tags">
          <el-tag 
            :type="searchForm.activityType === '' ? 'primary' : 'info'"
            :effect="searchForm.activityType === '' ? 'dark' : 'plain'"
            @click="searchForm.activityType = ''; handleSearch()"
            class="filter-tag"
            size="large"
          >
            全部
          </el-tag>
          <el-tag 
            v-for="type in activityTypes" 
            :key="type.value"
            :type="searchForm.activityType === type.value ? 'primary' : 'info'"
            :effect="searchForm.activityType === type.value ? 'dark' : 'plain'"
            @click="searchForm.activityType = type.value; handleSearch()"
            class="filter-tag"
            size="large"
          >
            {{ type.label }}
          </el-tag>
        </div>
      </div>
    </div>

    <!-- 活动列表 -->
    <div class="activity-grid" v-loading="loading">
      <el-card 
        v-for="activity in activities" 
        :key="activity.id" 
        class="activity-card"
        :body-style="{ padding: '0' }"
      >
        <div class="activity-image" :style="getActivityImageStyle(activity)">
          <div class="activity-icon">
            <el-icon :size="60">
              <component :is="getActivityIcon(activity.activityType)" />
            </el-icon>
          </div>
          <div class="activity-status" :class="getStatusClass(activity.status)">
            {{ getStatusText(activity.status) }}
          </div>
        </div>
        <div class="activity-body">
          <h3 class="activity-title">{{ activity.activityName }}</h3>
          <p class="activity-desc">{{ activity.description }}</p>
          
          <div class="activity-info">
            <div class="info-item">
              <el-icon><Calendar /></el-icon>
              <span>{{ formatDate(activity.startTime) }}</span>
            </div>
            <div class="info-item">
              <el-icon><Location /></el-icon>
              <span>{{ activity.location || '待定' }}</span>
            </div>
            <div class="info-item">
              <el-icon><User /></el-icon>
              <span>{{ activity.currentParticipants || 0 }}/{{ activity.maxParticipants || '不限' }}</span>
            </div>
          </div>

          <div class="activity-footer">
            <el-tag :type="getTypeTag(activity.activityType)" size="small">
              {{ getActivityTypeLabel(activity.activityType) }}
            </el-tag>
            <div class="activity-actions">
              <!-- 查看详情按钮 -->
              <el-button 
                type="primary" 
                size="small"
                @click="handleViewDetail(activity)"
              >
                查看详情
              </el-button>
              
              <!-- 相册按钮 -->
              <el-button 
                type="info" 
                size="small"
                @click.stop="handleViewAlbum(activity)"
              >
                📷 相册
              </el-button>
              
              <!-- 创建者：编辑和删除（只在我创建的标签显示，排除管理员） -->
              <template v-if="canManageActivityAsMember(activity) && activeTab === 'myCreated'">
                <el-button 
                  type="warning" 
                  size="small"
                  @click.stop="handleEdit(activity)"
                >
                  编辑
                </el-button>
                <el-button 
                  type="danger" 
                  size="small"
                  @click.stop="handleDelete(activity)"
                >
                  删除
                </el-button>
              </template>
              
              <!-- 报名按钮（全部活动标签，未报名且未满员） -->
              <el-button 
                v-if="activeTab === 'all' && !isRegistered(activity.id) && !isAdmin()"
                type="primary" 
                size="small"
                @click.stop="handleRegister(activity)"
                :disabled="activity.status === '2' || isActivityFull(activity)"
              >
                {{ getRegisterButtonText(activity) }}
              </el-button>
              
              <!-- 已报名：取消报名 -->
              <el-button 
                v-if="activeTab === 'myRegistration'"
                type="danger" 
                size="small"
                @click.stop="handleCancelRegistration(activity)"
                :disabled="activity.status !== '0'"
              >
                取消报名
              </el-button>
            </div>
          </div>
        </div>
      </el-card>

      <el-empty v-if="!activities.length && !loading" description="暂无活动" />
    </div>

    <!-- 分页 -->
    <div class="pagination-wrapper">
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :page-sizes="[9, 18, 27, 36]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadActivities"
        @current-change="loadActivities"
      />
    </div>

    <!-- 报名弹窗 -->
    <el-dialog 
      v-model="showRegisterDialog" 
      title="活动报名" 
      width="500px"
    >
      <el-form :model="registerForm" :rules="registerRules" ref="registerFormRef" label-width="80px">
        <el-form-item label="活动名称">
          <el-input v-model="currentActivity.activityName" disabled />
        </el-form-item>
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="registerForm.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="registerForm.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input 
            v-model="registerForm.remark" 
            type="textarea" 
            :rows="3"
            placeholder="其他需要说明的信息（选填）"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showRegisterDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitRegister" :loading="submitting">
          确认报名
        </el-button>
      </template>
    </el-dialog>

    <!-- 创建活动弹窗 -->
    <el-dialog 
      v-model="showCreateDialog" 
      title="创建活动" 
      width="600px"
    >
      <el-alert
        title="提示"
        type="info"
        description="创建的活动需要等待管理员审核后才会显示在活动列表中"
        :closable="false"
        style="margin-bottom: 20px"
      />
      <el-form :model="createForm" ref="createFormRef" label-width="100px">
        <el-form-item label="活动名称" prop="activityName" required>
          <el-input v-model="createForm.activityName" placeholder="请输入活动名称" />
        </el-form-item>
        <el-form-item label="活动类型" prop="activityType" required>
          <el-select v-model="createForm.activityType" placeholder="请选择活动类型" style="width: 100%">
            <el-option 
              v-for="type in activityTypes" 
              :key="type.value" 
              :label="type.label" 
              :value="type.value" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="活动封面" prop="coverImage">
          <el-upload
            class="avatar-uploader"
            :action="uploadUrl"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleCreateUploadSuccess"
            :on-error="handleUploadError"
            :before-upload="beforeUpload"
            accept="image/*"
          >
            <img v-if="createForm.coverImage" :src="getFileUrl(createForm.coverImage)" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
          <div class="upload-tip">建议尺寸：800x450，支持jpg/png格式，大小不超过5MB</div>
        </el-form-item>
        <el-form-item label="活动地点" prop="location">
          <el-input v-model="createForm.location" placeholder="请输入活动地点" />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime" required>
          <el-date-picker
            v-model="createForm.startTime"
            type="datetime"
            placeholder="选择开始时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime" required>
          <el-date-picker
            v-model="createForm.endTime"
            type="datetime"
            placeholder="选择结束时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="最大人数" prop="maxParticipants">
          <el-input-number v-model="createForm.maxParticipants" :min="1" :max="1000" placeholder="不限" />
        </el-form-item>
        <el-form-item label="活动描述" prop="description">
          <el-input 
            v-model="createForm.description" 
            type="textarea" 
            :rows="4"
            placeholder="请输入活动描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitCreate" :loading="creating">
          提交审核
        </el-button>
      </template>
    </el-dialog>

    <!-- 编辑活动弹窗 -->
    <el-dialog 
      v-model="showEditDialog" 
      title="编辑活动" 
      width="600px"
    >
      <el-form :model="editForm" ref="editFormRef" label-width="100px">
        <el-form-item label="活动名称" prop="activityName" required>
          <el-input v-model="editForm.activityName" placeholder="请输入活动名称" />
        </el-form-item>
        <el-form-item label="活动类型" prop="activityType" required>
          <el-select v-model="editForm.activityType" placeholder="请选择活动类型" style="width: 100%">
            <el-option 
              v-for="type in activityTypes" 
              :key="type.value" 
              :label="type.label" 
              :value="type.value" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="活动封面" prop="coverImage">
          <el-upload
            class="avatar-uploader"
            :action="uploadUrl"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleEditUploadSuccess"
            :on-error="handleUploadError"
            :before-upload="beforeUpload"
            accept="image/*"
          >
            <img v-if="editForm.coverImage" :src="getFileUrl(editForm.coverImage)" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
          <div class="upload-tip">建议尺寸：800x450，支持jpg/png格式，大小不超过5MB</div>
        </el-form-item>
        <el-form-item label="活动地点" prop="location">
          <el-input v-model="editForm.location" placeholder="请输入活动地点" />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime" required>
          <el-date-picker
            v-model="editForm.startTime"
            type="datetime"
            placeholder="选择开始时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime" required>
          <el-date-picker
            v-model="editForm.endTime"
            type="datetime"
            placeholder="选择结束时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="最大人数" prop="maxParticipants">
          <el-input-number v-model="editForm.maxParticipants" :min="1" :max="1000" placeholder="不限" />
        </el-form-item>
        <el-form-item label="活动状态" prop="status">
          <el-select v-model="editForm.status" placeholder="请选择活动状态" style="width: 100%">
            <el-option label="报名中" value="0" />
            <el-option label="进行中" value="1" />
            <el-option label="已结束" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="活动描述" prop="description">
          <el-input 
            v-model="editForm.description" 
            type="textarea" 
            :rows="4"
            placeholder="请输入活动描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitEdit" :loading="editing">
          保存修改
        </el-button>
      </template>
    </el-dialog>

    <!-- 活动相册对话框 -->
    <el-dialog 
      v-model="showAlbumDialog" 
      :title="`${currentAlbum.activityName} - 活动相册`" 
      width="90%"
      top="5vh"
    >
      <ActivityPhotoAlbum 
        v-if="showAlbumDialog && currentAlbum.id"
        :activityId="currentAlbum.id" 
        :canUpload="canUploadPhoto(currentAlbum)"
      />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Calendar, Search, Refresh, Location, User, Plus, Edit, Delete,
  Reading, Basketball, Suitcase, MagicStick
} from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { uploadFile } from '@/api/file'
import { getFileUrl } from '@/utils/file'
import { 
  getActivityList, 
  addRegistration, 
  getRegistrationList,
  cancelRegistration,
  addActivity,
  updateActivity,
  deleteActivity,
  auditActivity
} from '@/api/activity'
import ActivityPhotoAlbum from '@/components/ActivityPhotoAlbum.vue'

const userStore = useUserStore()

// 活动类型配置
const activityTypes = [
  { value: 'lecture', label: '讲座', icon: 'Reading', gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' },
  { value: 'competition', label: '比赛', icon: 'Trophy', gradient: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)' },
  { value: 'party', label: '聚会', icon: 'Coffee', gradient: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)' },
  { value: 'volunteer', label: '公益', icon: 'MagicStick', gradient: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)' },
  { value: 'training', label: '培训', icon: 'Reading', gradient: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)' },
  { value: 'exhibition', label: '展览', icon: 'Picture', gradient: 'linear-gradient(135deg, #30cfd0 0%, #330867 100%)' }
]

// Tab切换
const activeTab = ref('all')

// 搜索表单
const searchForm = reactive({
  keyword: '',
  activityType: '',
  status: ''
})

// 数据
const activities = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(9)
const total = ref(0)

// 用户已报名的活动ID列表
const registeredActivityIds = ref([])

// 权限判断：是否可以创建活动（只有社团团长和副团长）
const canCreateActivity = computed(() => {
  const roleKey = userStore.userInfo?.roleKey || ''
  return roleKey === 'club_president' || roleKey === 'club_vice_president'
})

// 报名弹窗
const showRegisterDialog = ref(false)
const submitting = ref(false)
const registerFormRef = ref(null)
const currentActivity = ref({})
const registerForm = reactive({
  activityId: null,
  userId: 1, // 临时写死
  realName: '',
  phone: '',
  remark: ''
})

// 创建活动弹窗
const showCreateDialog = ref(false)
const creating = ref(false)
const createFormRef = ref(null)
const createForm = reactive({
  activityName: '',
  activityType: '',
  location: '',
  startTime: null,
  endTime: null,
  maxParticipants: null,
  description: '',
  coverImage: '', // 封面图片
  organizerId: null,
  auditStatus: '0', // 待审核
  status: '0' // 报名中
})

// 上传地址
const uploadUrl = ref('http://localhost:8080/api/file/upload')

// 上传请求头（包含token）
const uploadHeaders = computed(() => ({
  'Authorization': `Bearer ${userStore.token}`
}))

// 图片上传成功（创建表单）
const handleCreateUploadSuccess = (response, file) => {
  console.log('=== 上传成功回调 ===')
  console.log('原始响应:', response)
  console.log('文件信息:', file)
  
  if (response && response.code === 200) {
    // 后端返回的是对象 {url: '...'}, 需要提取url字段
    const imagePath = typeof response.data === 'string' ? response.data : response.data.url
    createForm.coverImage = imagePath
    console.log('封面路径:', imagePath)
    console.log('完整URL:', getFileUrl(imagePath))
    console.log('createForm.coverImage:', createForm.coverImage)
    ElMessage.success('封面上传成功')
  } else {
    console.error('上传失败:', response)
    ElMessage.error('封面上传失败: ' + (response?.message || '未知错误'))
  }
}

// 上传错误处理
const handleUploadError = (error) => {
  console.error('上传错误:', error)
  ElMessage.error('图片上传失败，请重试')
}

// 上传前验证
const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5

  if (!isImage) {
    ElMessage.error('只能上传图片文件！')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过 5MB！')
    return false
  }
  return true
}

const registerRules = {
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

// Tab切换处理
const handleTabChange = (tab) => {
  pageNum.value = 1
  searchForm.keyword = ''
  searchForm.activityType = ''
  searchForm.status = ''
  loadActivities()
}

// 加载活动列表
const loadActivities = async () => {
  loading.value = true
  try {
    let params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      ...searchForm
    }
    
    if (activeTab.value === 'all') {
      // 全部活动：只显示已审核的
      params.auditStatus = '1'
    } else if (activeTab.value === 'myRegistration') {
      // 我的报名：获取用户报名的活动
      const regRes = await getRegistrationList({
        userId: userStore.userInfo.id,
        pageNum: pageNum.value,
        pageSize: pageSize.value
      })
      // 过滤掉 activity 为 null 或 undefined 的记录，并应用搜索条件
      let myActivities = regRes.data.records?.map(r => r.activity).filter(a => a != null) || []
      
      // 应用搜索条件
      if (searchForm.keyword) {
        myActivities = myActivities.filter(a => 
          a.activityName?.includes(searchForm.keyword) || 
          a.description?.includes(searchForm.keyword)
        )
      }
      if (searchForm.activityType) {
        myActivities = myActivities.filter(a => a.activityType === searchForm.activityType)
      }
      
      activities.value = myActivities
      total.value = myActivities.length
      loading.value = false
      return
    } else if (activeTab.value === 'myCreated') {
      // 我创建的：只显示当前用户创建的活动
      params.organizerId = userStore.userInfo.id
    }
    
    const res = await getActivityList(params)
    activities.value = res.data.records || []
    total.value = res.data.total || 0
    
    // 如果是全部活动，加载用户已报名的活动ID
    if (activeTab.value === 'all') {
      await loadUserRegistrations()
    }
  } catch (error) {
    console.error('加载活动失败:', error)
    ElMessage.error('加载活动失败')
  } finally {
    loading.value = false
  }
}

// 加载用户已报名的活动ID列表
const loadUserRegistrations = async () => {
  try {
    const res = await getRegistrationList({
      userId: userStore.userInfo.id,
      pageSize: 1000
    })
    registeredActivityIds.value = res.data.records?.map(r => r.activityId) || []
  } catch (error) {
    console.error('加载报名信息失败:', error)
  }
}

// 判断活动是否已报名
const isRegistered = (activityId) => {
  return registeredActivityIds.value.includes(activityId)
}

// 判断活动是否已满员
const isActivityFull = (activity) => {
  if (!activity.maxParticipants) return false
  return (activity.currentParticipants || 0) >= activity.maxParticipants
}

// 获取报名按钮文本
const getRegisterButtonText = (activity) => {
  if (activity.status === '2') return '已结束'
  if (isActivityFull(activity)) return '已满员'
  return '立即报名'
}

// 搜索
const handleSearch = () => {
  pageNum.value = 1
  loadActivities()
}

// 重置
const handleReset = () => {
  searchForm.keyword = ''
  searchForm.activityType = ''
  searchForm.status = ''
  handleSearch()
}

// 报名活动
const handleRegister = (activity) => {
  currentActivity.value = activity
  registerForm.activityId = activity.id
  showRegisterDialog.value = true
}

// 提交报名
const handleSubmitRegister = async () => {
  if (!registerFormRef.value) return
  
  try {
    await registerFormRef.value.validate()
    submitting.value = true
    
    registerForm.userId = userStore.userInfo.id
    await addRegistration(registerForm)
    
    ElMessage.success('报名成功，等待审核')
    showRegisterDialog.value = false
    
    // 重置表单
    registerFormRef.value.resetFields()
    
    // 重新加载列表
    loadActivities()
  } catch (error) {
    console.error('报名失败:', error)
    if (error !== false) {
      ElMessage.error('报名失败')
    }
  } finally {
    submitting.value = false
  }
}

// 查看活动详情
const handleViewDetail = (activity) => {
  ElMessageBox.alert(`
    <div style="text-align: left;">
      <h3>${activity.activityName}</h3>
      <p><strong>活动类型：</strong>${activity.activityType}</p>
      <p><strong>活动时间：</strong>${formatDate(activity.startTime)} - ${formatDate(activity.endTime)}</p>
      <p><strong>活动地点：</strong>${activity.location || '待定'}</p>
      <p><strong>活动描述：</strong>${activity.description || '暂无描述'}</p>
      <p><strong>参与人数：</strong>${activity.currentParticipants || 0}/${activity.maxParticipants || '不限'}</p>
    </div>
  `, '活动详情', {
    dangerouslyUseHTMLString: true,
    confirmButtonText: '关闭'
  })
}

// 取消报名
const handleCancelRegistration = async (activity) => {
  try {
    await ElMessageBox.confirm('确定要取消报名吗？', '提示', {
      type: 'warning'
    })
    
    // 找到报名记录ID
    const regRes = await getRegistrationList({
      userId: userStore.userInfo.id,
      activityId: activity.id
    })
    
    if (regRes.data.records && regRes.data.records.length > 0) {
      await cancelRegistration(regRes.data.records[0].id)
      ElMessage.success('取消报名成功')
      loadActivities()
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消报名失败:', error)
      ElMessage.error('取消报名失败')
    }
  }
}

// 编辑活动弹窗
const showEditDialog = ref(false)
const editing = ref(false)
const editFormRef = ref(null)
const editForm = reactive({
  id: null,
  activityName: '',
  activityType: '',
  location: '',
  startTime: null,
  endTime: null,
  maxParticipants: null,
  description: '',
  coverImage: '', // 封面图片
  status: '0',
  organizerId: null
})

// 图片上传成功（编辑表单）
const handleEditUploadSuccess = (response, file) => {
  console.log('=== 编辑上传成功回调 ===')
  console.log('原始响应:', response)
  console.log('文件信息:', file)
  
  if (response && response.code === 200) {
    // 后端返回的是对象 {url: '...'}, 需要提取url字段
    const imagePath = typeof response.data === 'string' ? response.data : response.data.url
    editForm.coverImage = imagePath
    console.log('编辑封面路径:', imagePath)
    console.log('完整URL:', getFileUrl(imagePath))
    console.log('editForm.coverImage:', editForm.coverImage)
    ElMessage.success('封面上传成功')
  } else {
    console.error('编辑上传失败:', response)
    ElMessage.error('封面上传失败: ' + (response?.message || '未知错误'))
  }
}

// 相册对话框
const showAlbumDialog = ref(false)
const currentAlbum = ref({})

// 查看相册
const handleViewAlbum = (activity) => {
  currentAlbum.value = activity
  showAlbumDialog.value = true
}

// 判断是否可以上传照片
const canUploadPhoto = (activity) => {
  const roleKey = userStore.userInfo?.roleKey || ''
  const userId = userStore.userInfo?.id
  
  // 管理员、活动创建者、已报名用户可以上传
  if (roleKey === 'admin' || roleKey === 'super_admin') {
    return true
  }
  
  if (activity.organizerId === userId) {
    return true
  }
  
  // 检查是否已报名
  return registeredActivityIds.value.includes(activity.id)
}

// 编辑活动
const handleEdit = (activity) => {
  // 填充表单数据
  Object.assign(editForm, {
    id: activity.id,
    activityName: activity.activityName,
    activityType: activity.activityType,
    location: activity.location,
    startTime: activity.startTime ? new Date(activity.startTime) : null,
    endTime: activity.endTime ? new Date(activity.endTime) : null,
    maxParticipants: activity.maxParticipants,
    description: activity.description,
    coverImage: activity.coverImage || '',
    status: activity.status,
    organizerId: activity.organizerId
  })
  
  showEditDialog.value = true
}

// 格式化日期时间为后端期望的格式（yyyy-MM-dd HH:mm:ss）
const formatDateTime = (date) => {
  if (!date) return null
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}

// 提交编辑
const handleSubmitEdit = async () => {
  if (!editFormRef.value) return
  
  try {
    editing.value = true
    
    // 验证必填字段
    if (!editForm.activityName || !editForm.activityType || !editForm.startTime || !editForm.endTime) {
      ElMessage.error('请填写完整的活动信息')
      editing.value = false
      return
    }
    
    // 格式化日期时间
    const submitData = {
      ...editForm,
      startTime: formatDateTime(editForm.startTime),
      endTime: formatDateTime(editForm.endTime),
      // 确保coverImage是字符串
      coverImage: typeof editForm.coverImage === 'string' ? editForm.coverImage : ''
    }
    
    console.log('提交的编辑数据:', submitData)
    
    await updateActivity(submitData)
    
    ElMessage.success('活动修改成功')
    showEditDialog.value = false
    
    // 重新加载列表
    loadActivities()
  } catch (error) {
    console.error('编辑活动失败:', error)
    ElMessage.error(error.message || '编辑活动失败')
  } finally {
    editing.value = false
  }
}

// 判断是否是管理员
const isAdmin = () => {
  const roleKey = userStore.userInfo?.roleKey || ''
  return roleKey === 'admin' || roleKey === 'super_admin'
}

// 判断是否可以作为普通成员管理活动（排除管理员）
const canManageActivityAsMember = (activity) => {
  const roleKey = userStore.userInfo?.roleKey || ''
  const userId = userStore.userInfo?.id
  
  // 排除管理员
  if (roleKey === 'admin' || roleKey === 'super_admin') {
    return false
  }
  
  // 社团会长、副会长、部长可以管理所有活动
  if (roleKey === 'club_president' || roleKey === 'club_vice_president' || roleKey === 'department_head') {
    return true
  }
  
  // 活动创建者可以管理自己的活动
  return activity.organizerId === userId
}

// 删除活动
const handleDelete = async (activity) => {
  try {
    await ElMessageBox.confirm('确定要删除这个活动吗？删除后无法恢复！', '警告', {
      type: 'warning',
      confirmButtonText: '确定删除',
      cancelButtonText: '取消'
    })
    
    await deleteActivity(activity.id)
    ElMessage.success('删除成功')
    loadActivities()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 提交创建活动
const handleSubmitCreate = async () => {
  if (!createFormRef.value) return
  
  try {
    creating.value = true
    
    // 验证用户信息
    const userId = userStore.userInfo?.id
    console.log('当前用户信息:', userStore.userInfo)
    console.log('用户ID:', userId)
    
    if (!userId) {
      ElMessage.error('无法获取用户信息，请重新登录')
      creating.value = false
      return
    }
    
    // 设置组织者ID
    createForm.organizerId = userId
    
    // 验证必填字段
    if (!createForm.activityName || !createForm.activityType || !createForm.startTime || !createForm.endTime) {
      ElMessage.error('请填写完整的活动信息')
      creating.value = false
      return
    }
    
    // 格式化日期时间
    const submitData = {
      ...createForm,
      startTime: formatDateTime(createForm.startTime),
      endTime: formatDateTime(createForm.endTime),
      // 确保coverImage是字符串
      coverImage: typeof createForm.coverImage === 'string' ? createForm.coverImage : ''
    }
    
    console.log('提交的活动数据:', submitData)
    
    await addActivity(submitData)
    
    ElMessage.success('活动创建成功，等待管理员审核')
    showCreateDialog.value = false
    
    // 重置表单
    Object.assign(createForm, {
      activityName: '',
      activityType: '',
      location: '',
      startTime: null,
      endTime: null,
      maxParticipants: null,
      description: '',
      coverImage: '',
      organizerId: null,
      auditStatus: '0',
      status: '0'
    })
    
    // 如果在"我创建的"标签页，重新加载
    if (activeTab.value === 'myCreated') {
      loadActivities()
    }
  } catch (error) {
    console.error('创建活动失败:', error)
    ElMessage.error(error.message || '创建活动失败')
  } finally {
    creating.value = false
  }
}

// 获取活动图片样式（优先使用封面图片，否则使用渐变背景）
const getActivityImageStyle = (activity) => {
  // 如果有封面图片，使用封面图片
  if (activity.coverImage) {
    return {
      backgroundImage: `url(${getFileUrl(activity.coverImage)})`,
      backgroundSize: 'cover',
      backgroundPosition: 'center'
    }
  }
  
  // 否则使用渐变背景
  const typeConfig = activityTypes.find(t => t.value === activity.activityType)
  return {
    background: typeConfig ? typeConfig.gradient : 'linear-gradient(135deg, #a8edea 0%, #fed6e3 100%)'
  }
}

// 获取活动类型图标
const getActivityIcon = (type) => {
  const typeConfig = activityTypes.find(t => t.value === type)
  return typeConfig ? typeConfig.icon : 'Calendar'
}

// 获取活动类型标签文本
const getActivityTypeLabel = (type) => {
  const typeConfig = activityTypes.find(t => t.value === type)
  return typeConfig ? typeConfig.label : type
}

// 获取状态文本
const getStatusText = (status) => {
  const map = { '0': '报名中', '1': '进行中', '2': '已结束' }
  return map[status] || '未知'
}

// 获取状态样式
const getStatusClass = (status) => {
  const map = { '0': 'status-open', '1': 'status-ongoing', '2': 'status-closed' }
  return map[status] || ''
}

// 获取类型标签
const getTypeTag = (type) => {
  const map = {
    '学术讲座': 'primary',
    '文体活动': 'success',
    '社会实践': 'warning',
    '志愿服务': 'danger'
  }
  return map[type] || 'info'
}

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return `${date.getMonth() + 1}月${date.getDate()}日 ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
}

// 判断用户是否可以管理活动（编辑、删除）
const canManageActivity = (activity) => {
  const roleKey = userStore.userInfo?.roleKey
  const userId = userStore.userInfo?.id
  
  console.log('=== 权限判断 ===')
  console.log('活动:', activity.activityName)
  console.log('当前用户角色:', roleKey)
  console.log('当前用户ID:', userId)
  console.log('活动创建者ID:', activity.organizerId)
  
  // 系统管理员和超级管理员可以管理所有活动
  if (roleKey === 'admin' || roleKey === 'super_admin') {
    console.log('✅ 系统管理员权限')
    return true
  }
  
  // 社团会长、副会长可以管理所有活动
  if (roleKey === 'club_president' || roleKey === 'club_vice_president') {
    console.log('✅ 社团会长/副会长权限')
    return true
  }
  
  // 部长可以管理所有活动
  if (roleKey === 'department_head') {
    console.log('✅ 部长权限')
    return true
  }
  
  // 活动创建者可以管理自己的活动
  if (activity.organizerId === userId) {
    console.log('✅ 创建者权限')
    return true
  }
  
  console.log('❌ 无管理权限')
  return false
}

// 判断用户是否可以审核活动
const canAuditActivity = (activity) => {
  const roleKey = userStore.userInfo?.roleKey
  
  // 系统管理员和超级管理员可以审核所有活动
  if (roleKey === 'admin' || roleKey === 'super_admin') {
    return true
  }
  
  // 社团会长、副会长可以审核所有活动
  if (roleKey === 'club_president' || roleKey === 'club_vice_president') {
    return true
  }
  
  return false
}

// 审核活动
const handleAudit = async (activity) => {
  try {
    const { value } = await ElMessageBox.prompt('请选择审核结果', '审核活动', {
      confirmButtonText: '通过',
      cancelButtonText: '拒绝',
      inputType: 'textarea',
      inputPlaceholder: '请输入审核意见（可选）',
      distinguishCancelAndClose: true
    })
    
    // 通过审核
    await auditActivity(activity.id, '1')
    ElMessage.success('审核通过')
    loadActivities()
  } catch (action) {
    if (action === 'cancel') {
      // 拒绝审核
      try {
        const { value } = await ElMessageBox.prompt('请输入拒绝原因', '拒绝审核', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputType: 'textarea',
          inputPlaceholder: '请输入拒绝原因',
          inputValidator: (value) => {
            if (!value || value.trim() === '') {
              return '请输入拒绝原因'
            }
            return true
          }
        })
        
        await auditActivity(activity.id, '2')
        ElMessage.success('已拒绝该活动')
        loadActivities()
      } catch (error) {
        // 用户取消操作
      }
    }
  }
}

onMounted(() => {
  loadActivities()
})
</script>

<style scoped lang="scss">
.activities-page {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px 40px;
  background: linear-gradient(180deg, #f8f9ff 0%, #ffffff 100%);
  min-height: 100vh;
}

// Hero Section
.hero-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 24px;
  padding: 60px 40px;
  margin-bottom: 40px;
  box-shadow: 0 20px 60px rgba(102, 126, 234, 0.3);
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: -50%;
    right: -10%;
    width: 500px;
    height: 500px;
    background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 70%);
    border-radius: 50%;
  }
}

.hero-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  z-index: 1;
}

.hero-text {
  color: white;
}

.hero-title {
  font-size: 48px;
  font-weight: 800;
  margin: 0 0 16px 0;
  display: flex;
  align-items: center;
  gap: 16px;
  
  .title-icon {
    font-size: 56px;
    animation: bounce 2s infinite;
  }
}

@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.hero-subtitle {
  font-size: 20px;
  margin: 0 0 32px 0;
  opacity: 0.95;
  font-weight: 300;
  letter-spacing: 2px;
}

.hero-stats {
  display: flex;
  gap: 32px;
  align-items: center;
}

.stat-item {
  text-align: center;
}

.stat-number {
  font-size: 36px;
  font-weight: 700;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
}

.stat-divider {
  width: 1px;
  height: 40px;
  background: rgba(255, 255, 255, 0.3);
}

.stat-action {
  text-align: center;
  
  .create-activity-btn {
    padding: 12px 28px;
    font-size: 16px;
    font-weight: 600;
    background: rgba(255, 255, 255, 0.95);
    color: #667eea;
    border: none;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
    transition: all 0.3s ease;
    
    &:hover {
      background: white;
      transform: translateY(-2px);
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
    }
    
    &:active {
      transform: translateY(0);
    }
  }
}

// Tab Section
.tab-section {
  margin-bottom: 32px;
}

.tab-buttons {
  display: flex;
  gap: 16px;
  background: white;
  padding: 8px;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.tab-btn {
  flex: 1;
  padding: 16px 24px;
  border: none;
  background: transparent;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  color: #666;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  
  .tab-icon {
    font-size: 20px;
  }
  
  &:hover {
    background: #f5f7fa;
    color: #409eff;
  }
  
  &.active {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
  }
}

// Search Section - 紫色主题
.search-section {
  margin-bottom: 32px;
}

.search-container {
  background: transparent;
  padding: 0;
}

.search-input {
  margin-bottom: 16px;
  max-width: 800px;
  margin-left: auto;
  margin-right: auto;
  
  :deep(.el-input__wrapper) {
    border-radius: 4px 0 0 4px;
    box-shadow: none;
    border: 2px solid #667eea;
    border-right: none;
    padding: 0 16px;
    transition: all 0.3s ease;
    background: white;
    
    &:hover {
      border-color: #667eea;
    }
    
    &.is-focus {
      border-color: #667eea;
      box-shadow: none;
    }
  }
  
  :deep(.el-input__inner) {
    font-size: 14px;
    height: 40px;
    color: #333;
    
    &::placeholder {
      color: #bbb;
    }
  }
  
  :deep(.el-input-group__append) {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
    border-radius: 0 4px 4px 0;
    padding: 0;
    box-shadow: none;
    
    .el-button {
      background: transparent;
      border: none;
      color: white;
      font-weight: 600;
      font-size: 16px;
      padding: 0 32px;
      height: 44px;
      transition: all 0.3s ease;
      
      &:hover {
        background: rgba(255, 255, 255, 0.15);
      }
      
      &:active {
        background: rgba(255, 255, 255, 0.25);
      }
    }
  }
}

.filter-tags {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  padding-top: 0;
  max-width: 800px;
  margin-left: auto;
  margin-right: auto;
  justify-content: center;
}

.filter-tag {
  cursor: pointer;
  transition: all 0.2s ease;
  border-radius: 2px;
  padding: 6px 16px;
  font-size: 13px;
  font-weight: 400;
  user-select: none;
  border: 1px solid #e0e0e0;
  
  &:hover {
    border-color: #667eea;
    color: #667eea;
  }
  
  &:active {
    opacity: 0.8;
  }
  
  // 激活状态
  &.el-tag--primary.el-tag--dark {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-color: transparent;
    color: white;
    box-shadow: none;
  }
  
  // 未激活状态
  &.el-tag--info.el-tag--plain {
    background: white;
    border-color: #e0e0e0;
    color: #666;
    
    &:hover {
      background: #f5f7ff;
      border-color: #667eea;
      color: #667eea;
    }
  }
}

.activity-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  min-height: 400px;
}

.activity-card {
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
  border-radius: 20px;
  border: none;
  background: white;
  
  &:hover {
    transform: translateY(-12px) scale(1.02);
    box-shadow: 0 20px 60px rgba(102, 126, 234, 0.25);
    
    .activity-icon {
      transform: scale(1.3) rotate(10deg);
    }
    
    .activity-title {
      color: #667eea;
    }
  }
}

.activity-image {
  position: relative;
  width: 100%;
  height: 200px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.activity-icon {
  color: rgba(255, 255, 255, 0.9);
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.activity-card:hover .activity-icon {
  transform: scale(1.2) rotate(5deg);
}

.activity-status {
  position: absolute;
  top: 12px;
  right: 12px;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
  color: #fff;
}

.status-open {
  background: #67c23a;
}

.status-ongoing {
  background: #409eff;
}

.status-closed {
  background: #909399;
}

.activity-body {
  padding: 24px;
}

.activity-title {
  font-size: 20px;
  font-weight: 700;
  margin: 0 0 12px 0;
  color: #2c3e50;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  transition: color 0.3s ease;
  letter-spacing: 0.5px;
}

.activity-desc {
  margin: 0 0 16px 0;
  color: #666;
  font-size: 14px;
  line-height: 1.6;
  height: 42px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
}

.activity-info {
  margin-bottom: 16px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 8px;
  font-size: 13px;
  color: #999;
}

.activity-footer {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding-top: 20px;
  margin-top: 16px;
  border-top: 2px solid #f5f7fa;
  gap: 12px;
}

.activity-actions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  flex: 1;
  justify-content: flex-end;
  
  .el-button {
    border-radius: 20px;
    font-weight: 600;
    transition: all 0.3s ease;
    padding: 8px 16px;
    font-size: 13px;
    
    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    }
  }
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 30px;
  padding-bottom: 30px;
}

@media (max-width: 1024px) {
  .activity-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .activity-grid {
    grid-template-columns: 1fr;
  }
}

// 图片上传样式
.avatar-uploader {
  border: 2px dashed #d9d9d9;
  border-radius: 8px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
  width: 200px;
  height: 112px;
  display: flex;
  align-items: center;
  justify-content: center;
  
  &:hover {
    border-color: #667eea;
  }
  
  .avatar {
    width: 100%;
    height: 100%;
    object-fit: cover;
    display: block;
  }
  
  .avatar-uploader-icon {
    font-size: 32px;
    color: #8c939d;
    text-align: center;
  }
}

.upload-tip {
  font-size: 12px;
  color: #999;
  margin-top: 8px;
  line-height: 1.5;
}
</style>
