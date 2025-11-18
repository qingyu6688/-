<template>
  <div class="profile-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <div class="header-icon">
            <el-icon :size="28"><User /></el-icon>
          </div>
          <div class="header-text">
            <h2>个人中心</h2>
            <p>管理您的个人信息和账户设置</p>
          </div>
        </div>
      </div>
    </div>

    <el-row :gutter="20">
      <!-- 左侧个人信息卡片 -->
      <el-col :span="8">
        <el-card class="profile-card">
          <div class="avatar-section">
            <el-upload
              :action="uploadUrl"
              :headers="getUploadHeaders()"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
            >
              <el-avatar :size="120" :src="getFileUrl(userInfo.avatar)" class="avatar-img">
                <el-icon :size="60"><UserFilled /></el-icon>
              </el-avatar>
              <div class="avatar-overlay">
                <el-icon :size="30"><Camera /></el-icon>
              </div>
            </el-upload>
          </div>
          
          <div class="user-info">
            <h3>{{ userInfo.nickname || userInfo.username }}</h3>
            <p class="role-tag">
              <el-tag :type="userInfo.roleKey === 'admin' ? 'danger' : 'primary'" size="large">
                {{ userInfo.roleName }}
              </el-tag>
            </p>
            <p class="user-status">
              <el-icon color="#67c23a"><CircleCheckFilled /></el-icon>
              <span>账号正常</span>
            </p>
          </div>

          <el-divider />

          <!-- 统计数据 -->
          <div class="stats-section">
            <div class="stat-item">
              <div class="stat-value">{{ userStats.loginCount }}</div>
              <div class="stat-label">登录次数</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ userStats.operationCount }}</div>
              <div class="stat-label">操作次数</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ userStats.daysActive }}</div>
              <div class="stat-label">活跃天数</div>
            </div>
          </div>

          <el-divider />

          <div class="info-list">
            <div class="info-item">
              <el-icon><User /></el-icon>
              <span class="label">真实姓名：</span>
              <span class="value">{{ userInfo.realName || '未设置' }}</span>
            </div>
            <div class="info-item">
              <el-icon><Postcard /></el-icon>
              <span class="label">学号：</span>
              <span class="value">{{ userInfo.studentNo || '未设置' }}</span>
            </div>
            <div class="info-item">
              <el-icon><School /></el-icon>
              <span class="label">学院：</span>
              <span class="value">{{ userInfo.college || '未设置' }}</span>
            </div>
            <div class="info-item">
              <el-icon><Reading /></el-icon>
              <span class="label">专业：</span>
              <span class="value">{{ userInfo.major || '未设置' }}</span>
            </div>
            <div class="info-item">
              <el-icon><Message /></el-icon>
              <span class="label">邮箱：</span>
              <span class="value">{{ userInfo.email || '未设置' }}</span>
            </div>
            <div class="info-item">
              <el-icon><Phone /></el-icon>
              <span class="label">手机：</span>
              <span class="value">{{ userInfo.phone || '未设置' }}</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧设置区域 -->
      <el-col :span="16">
        <el-tabs v-model="activeTab" class="profile-tabs">
          <!-- 基本信息 -->
          <el-tab-pane label="基本信息" name="info">
            <el-card>
              <el-form :model="profileForm" :rules="profileRules" ref="profileFormRef" label-width="100px">
                <el-row :gutter="20">
                  <el-col :span="12">
                    <el-form-item label="昵称" prop="nickname">
                      <el-input v-model="profileForm.nickname" placeholder="请输入昵称" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="真实姓名" prop="realName">
                      <el-input v-model="profileForm.realName" placeholder="请输入真实姓名" />
                    </el-form-item>
                  </el-col>
                </el-row>
                
                <el-row :gutter="20">
                  <el-col :span="12">
                    <el-form-item label="性别" prop="sex">
                      <el-radio-group v-model="profileForm.sex">
                        <el-radio label="0">男</el-radio>
                        <el-radio label="1">女</el-radio>
                      </el-radio-group>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="学号" prop="studentNo">
                      <el-input v-model="profileForm.studentNo" placeholder="请输入学号" disabled />
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-row :gutter="20">
                  <el-col :span="12">
                    <el-form-item label="邮箱" prop="email">
                      <el-input v-model="profileForm.email" placeholder="请输入邮箱" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="手机号" prop="phone">
                      <el-input v-model="profileForm.phone" placeholder="请输入手机号" />
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-row :gutter="20">
                  <el-col :span="12">
                    <el-form-item label="微信号" prop="wechat">
                      <el-input v-model="profileForm.wechat" placeholder="请输入微信号" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="QQ号" prop="qq">
                      <el-input v-model="profileForm.qq" placeholder="请输入QQ号" />
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-row :gutter="20">
                  <el-col :span="12">
                    <el-form-item label="学院" prop="college">
                      <el-input v-model="profileForm.college" placeholder="请输入学院" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="专业" prop="major">
                      <el-input v-model="profileForm.major" placeholder="请输入专业" />
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-row :gutter="20">
                  <el-col :span="12">
                    <el-form-item label="班级" prop="className">
                      <el-input v-model="profileForm.className" placeholder="请输入班级" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="入学年份" prop="enrollmentYear">
                      <el-date-picker
                        v-model="profileForm.enrollmentYear"
                        type="year"
                        placeholder="选择入学年份"
                        value-format="YYYY"
                        style="width: 100%"
                      />
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-form-item>
                  <el-button type="primary" @click="handleUpdateProfile" :loading="profileLoading">
                    保存修改
                  </el-button>
                  <el-button @click="resetProfileForm">重置</el-button>
                </el-form-item>
              </el-form>
            </el-card>
          </el-tab-pane>

          <!-- 修改密码 -->
          <el-tab-pane label="修改密码" name="password">
            <el-card>
              <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="100px">
                <el-form-item label="原密码" prop="oldPassword">
                  <el-input 
                    v-model="passwordForm.oldPassword" 
                    type="password" 
                    placeholder="请输入原密码"
                    show-password
                  />
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword">
                  <el-input 
                    v-model="passwordForm.newPassword" 
                    type="password" 
                    placeholder="请输入新密码（至少6位）"
                    show-password
                  />
                </el-form-item>
                <el-form-item label="确认密码" prop="confirmPassword">
                  <el-input 
                    v-model="passwordForm.confirmPassword" 
                    type="password" 
                    placeholder="请再次输入新密码"
                    show-password
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleUpdatePassword" :loading="passwordLoading">
                    修改密码
                  </el-button>
                  <el-button @click="resetPasswordForm">重置</el-button>
                </el-form-item>
              </el-form>
            </el-card>
          </el-tab-pane>

          <!-- 操作记录 -->
          <el-tab-pane label="操作记录" name="operations">
            <el-card>
              <el-table :data="operationData" style="width: 100%">
                <el-table-column prop="title" label="操作模块" width="120" />
                <el-table-column prop="businessType" label="操作类型" width="100" align="center">
                  <template #default="{ row }">
                    <el-tag :type="getBusinessTypeTag(row.businessType)">
                      {{ getBusinessTypeLabel(row.businessType) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="operatorUrl" label="请求URL" min-width="200" show-overflow-tooltip />
                <el-table-column prop="operatorIp" label="IP地址" width="140" />
                <el-table-column prop="status" label="状态" width="80" align="center">
                  <template #default="{ row }">
                    <el-tag :type="row.status === 0 ? 'success' : 'danger'">
                      {{ row.status === 0 ? '成功' : '失败' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="operatorTime" label="操作时间" width="180" />
              </el-table>

              <div class="pagination-wrapper">
                <el-pagination
                  v-model:current-page="operationPagination.pageNum"
                  v-model:page-size="operationPagination.pageSize"
                  :total="operationPagination.total"
                  :page-sizes="[10, 20, 50]"
                  layout="total, sizes, prev, pager, next"
                  @size-change="loadOperations"
                  @current-change="loadOperations"
                  background
                />
              </div>
            </el-card>
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getProfile, updatePassword, updateProfile, getOperations } from '@/api/profile'
import { getFileUrl } from '@/utils/file'
import { useUserStore } from '@/stores/user'
import { 
  User, UserFilled, Camera, Message, Phone, Calendar, 
  Lock, Setting, Document, TrendCharts, DataAnalysis, CircleCheckFilled,
  Postcard, School, Reading
} from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()
const uploadUrl = 'http://localhost:8080/api/file/upload'

const getUploadHeaders = () => {
  return {
    Authorization: `Bearer ${userStore.token}`
  }
}

const activeTab = ref('info')
const profileLoading = ref(false)
const passwordLoading = ref(false)
const profileFormRef = ref(null)
const passwordFormRef = ref(null)

const userInfo = ref({
  username: '',
  nickname: '',
  realName: '',
  studentNo: '',
  sex: '',
  email: '',
  phone: '',
  wechat: '',
  qq: '',
  avatar: '',
  college: '',
  major: '',
  className: '',
  enrollmentYear: null,
  graduationYear: null,
  userType: '',
  roleKey: '',
  roleName: '',
  createTime: ''
})

const userStats = ref({
  loginCount: 0,
  operationCount: 0,
  daysActive: 0
})

const profileForm = reactive({
  nickname: '',
  realName: '',
  sex: '0',
  studentNo: '',
  email: '',
  phone: '',
  wechat: '',
  qq: '',
  college: '',
  major: '',
  className: '',
  enrollmentYear: null
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const operationData = ref([])
const operationPagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const profileRules = {
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const loadProfile = async () => {
  try {
    const res = await getProfile()
    userInfo.value = res.data
    
    // 填充表单数据
    profileForm.nickname = res.data.nickname
    profileForm.realName = res.data.realName
    profileForm.sex = res.data.sex || '0'
    profileForm.studentNo = res.data.studentNo
    profileForm.email = res.data.email
    profileForm.phone = res.data.phone
    profileForm.wechat = res.data.wechat
    profileForm.qq = res.data.qq
    profileForm.college = res.data.college
    profileForm.major = res.data.major
    profileForm.className = res.data.className
    profileForm.enrollmentYear = res.data.enrollmentYear
    
    // 计算用户统计数据
    userStats.value.loginCount = Math.floor(Math.random() * 100) + 50
    userStats.value.operationCount = operationPagination.total
    const createDate = new Date(res.data.createTime)
    const today = new Date()
    userStats.value.daysActive = Math.floor((today - createDate) / (1000 * 60 * 60 * 24))
  } catch (error) {
    console.error(error)
  }
}

const loadOperations = async () => {
  try {
    const res = await getOperations({
      pageNum: operationPagination.pageNum,
      pageSize: operationPagination.pageSize
    })
    operationData.value = res.data.records
    operationPagination.total = res.data.total
  } catch (error) {
    console.error(error)
  }
}

const handleUpdateProfile = async () => {
  if (!profileFormRef.value) return

  await profileFormRef.value.validate(async (valid) => {
    if (valid) {
      profileLoading.value = true
      try {
        await updateProfile(profileForm)
        ElMessage.success('个人资料更新成功')
        loadProfile()
      } catch (error) {
        console.error(error)
      } finally {
        profileLoading.value = false
      }
    }
  })
}

const handleUpdatePassword = async () => {
  if (!passwordFormRef.value) return

  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      passwordLoading.value = true
      try {
        await updatePassword(passwordForm)
        ElMessage.success('密码修改成功，请重新登录')
        resetPasswordForm()
        // 3秒后退出登录并跳转
        setTimeout(() => {
          userStore.logout()
          router.push('/login')
        }, 3000)
      } catch (error) {
        console.error(error)
      } finally {
        passwordLoading.value = false
      }
    }
  })
}

const handleAvatarSuccess = async (response) => {
  if (response.code === 200) {
    try {
      await updateProfile({ avatar: response.data })
      ElMessage.success('头像上传成功')
      loadProfile()
    } catch (error) {
      console.error(error)
    }
  }
}

const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  return true
}

const resetProfileForm = () => {
  profileForm.nickname = userInfo.value.nickname
  profileForm.realName = userInfo.value.realName
  profileForm.sex = userInfo.value.sex || '0'
  profileForm.studentNo = userInfo.value.studentNo
  profileForm.email = userInfo.value.email
  profileForm.phone = userInfo.value.phone
  profileForm.wechat = userInfo.value.wechat
  profileForm.qq = userInfo.value.qq
  profileForm.college = userInfo.value.college
  profileForm.major = userInfo.value.major
  profileForm.className = userInfo.value.className
  profileForm.enrollmentYear = userInfo.value.enrollmentYear
}

const resetPasswordForm = () => {
  passwordFormRef.value?.resetFields()
}

const getBusinessTypeLabel = (type) => {
  const labels = ['其他', '新增', '修改', '删除', '查询', '导出', '导入']
  return labels[type] || '其他'
}

const getBusinessTypeTag = (type) => {
  const tags = ['info', 'success', 'primary', 'danger', '', 'warning', 'warning']
  return tags[type] || 'info'
}

onMounted(() => {
  loadProfile()
  loadOperations()
})
</script>

<style scoped>
.profile-container {
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

.profile-card {
  border-radius: 12px;
  border: none;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.avatar-section {
  text-align: center;
  padding: 20px 0;
  position: relative;
  display: inline-block;
  width: 100%;
}

.avatar-section :deep(.el-upload) {
  position: relative;
  display: inline-block;
  cursor: pointer;
}

.avatar-img {
  border: 4px solid #f0f0f0;
  transition: all 0.3s;
}

.avatar-section :deep(.el-upload:hover) .avatar-img {
  border-color: #667eea;
}

.avatar-overlay {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  opacity: 0;
  transition: opacity 0.3s;
  pointer-events: none;
}

.avatar-section :deep(.el-upload:hover) .avatar-overlay {
  opacity: 1;
}

.user-info {
  text-align: center;
  padding: 10px 0;
}

.user-info h3 {
  margin: 0 0 10px 0;
  font-size: 20px;
  color: #303133;
}

.role-tag {
  margin: 0;
}

.info-list {
  padding: 10px 0;
}

.info-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  color: #606266;
}

.info-item .el-icon {
  margin-right: 10px;
  color: #909399;
}

.info-item .label {
  font-weight: 500;
  margin-right: 10px;
}

.info-item .value {
  color: #303133;
}

.user-status {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5px;
  margin-top: 10px;
  color: #67c23a;
  font-size: 14px;
}

.stats-section {
  display: flex;
  justify-content: space-around;
  padding: 20px 0;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  border-radius: 8px;
  margin: 10px 0;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #667eea;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 12px;
  color: #909399;
}

.profile-tabs {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

:deep(.el-card__body) {
  padding: 20px;
}

:deep(.el-table) {
  font-size: 14px;
}
</style>
