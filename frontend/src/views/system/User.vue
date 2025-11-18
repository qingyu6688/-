<template>
  <div class="user-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <div class="header-icon">
            <el-icon :size="28"><UserFilled /></el-icon>
          </div>
          <div class="header-text">
            <h2>用户管理</h2>
            <p>管理系统用户信息和权限</p>
          </div>
        </div>
        <div class="header-right">
          <el-button type="primary" @click="handleAdd" :icon="Plus" size="large">
            新增用户
          </el-button>
        </div>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stats-row">
      <el-col :span="6">
        <div class="stat-card stat-card-primary">
          <div class="stat-icon">
            <el-icon :size="32"><User /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ pagination.total }}</div>
            <div class="stat-label">总用户数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card-success">
          <div class="stat-icon">
            <el-icon :size="32"><UserFilled /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ adminCount }}</div>
            <div class="stat-label">管理员</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card-warning">
          <div class="stat-icon">
            <el-icon :size="32"><Avatar /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ normalCount }}</div>
            <div class="stat-label">普通用户</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card-info">
          <div class="stat-icon">
            <el-icon :size="32"><CircleCheck /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ activeCount }}</div>
            <div class="stat-label">活跃用户</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-card class="search-card">
      <div class="search-header">
        <el-icon><Search /></el-icon>
        <span>筛选查询</span>
      </div>
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户名">
          <el-input 
            v-model="searchForm.username" 
            placeholder="请输入用户名" 
            clearable 
            prefix-icon="User"
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input 
            v-model="searchForm.realName" 
            placeholder="请输入真实姓名" 
            clearable 
            prefix-icon="UserFilled"
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="学号">
          <el-input 
            v-model="searchForm.studentNo" 
            placeholder="请输入学号" 
            clearable 
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="用户类型">
          <el-select 
            v-model="searchForm.userType" 
            placeholder="请选择用户类型" 
            clearable
            style="width: 140px"
          >
            <el-option label="在校生" value="student" />
            <el-option label="毕业生" value="graduate" />
            <el-option label="教师" value="teacher" />
          </el-select>
        </el-form-item>
        <el-form-item label="角色">
          <el-select 
            v-model="searchForm.roleId" 
            placeholder="请选择角色" 
            clearable
            style="width: 150px"
          >
            <el-option 
              v-for="role in roleList" 
              :key="role.id" 
              :label="role.roleName" 
              :value="role.id" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select 
            v-model="searchForm.status" 
            placeholder="请选择状态" 
            clearable
            style="width: 120px"
          >
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch" :icon="Search">
            搜索
          </el-button>
          <el-button @click="handleReset" :icon="Refresh">
            重置
          </el-button>
          <el-button type="success" @click="handleAdd" :icon="Plus" v-permission="['system:user:add']">
            新增用户
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
        class="modern-table"
      >
        <el-table-column type="index" label="#" width="70" align="center">
          <template #default="{ $index }">
            <div class="index-cell">{{ $index + 1 }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户信息" min-width="220" fixed="left">
          <template #default="{ row }">
            <div class="user-info-cell">
              <el-avatar :size="40" :src="getFileUrl(row.avatar)" class="user-avatar">
                <el-icon :size="20"><User /></el-icon>
              </el-avatar>
              <div class="user-details">
                <div class="user-name">{{ row.username }}</div>
                <div class="user-nickname">{{ row.realName || row.nickname }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="realName" label="真实姓名" width="120" show-overflow-tooltip />
        <el-table-column prop="studentNo" label="学号" width="130" show-overflow-tooltip />
        <el-table-column prop="sex" label="性别" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.sex === '0' ? 'primary' : row.sex === '1' ? 'danger' : 'info'" size="small">
              {{ row.sex === '0' ? '男' : row.sex === '1' ? '女' : '未知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="userType" label="用户类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getUserTypeTag(row.userType)" size="small">
              {{ getUserTypeText(row.userType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="college" label="学院" width="120" show-overflow-tooltip />
        <el-table-column prop="major" label="专业" width="130" show-overflow-tooltip />
        <el-table-column prop="className" label="班级" width="100" show-overflow-tooltip />
        <el-table-column prop="enrollmentYear" label="入学年份" width="100" align="center">
          <template #default="{ row }">
            <span v-if="row.enrollmentYear">{{ row.enrollmentYear }}</span>
            <span v-else style="color: #909399;">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="graduationYear" label="毕业年份" width="100" align="center">
          <template #default="{ row }">
            <span v-if="row.graduationYear">{{ row.graduationYear }}</span>
            <span v-else style="color: #909399;">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="roleName" label="角色" width="130" align="center">
          <template #default="{ row }">
            <el-tag :type="getRoleTagType(row.roleKey)" size="small">
              {{ row.roleName || '未分配' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="130">
          <template #default="{ row }">
            <div class="contact-cell" v-if="row.phone">
              <el-icon color="#67c23a"><Phone /></el-icon>
              <span>{{ row.phone }}</span>
            </div>
            <span v-else style="color: #909399;">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱" width="180" show-overflow-tooltip>
          <template #default="{ row }">
            <div class="contact-cell" v-if="row.email">
              <el-icon color="#42a5f5"><Message /></el-icon>
              <span>{{ row.email }}</span>
            </div>
            <span v-else style="color: #909399;">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(row)"
              size="large"
              inline-prompt
              active-text="ON"
              inactive-text="OFF"
              style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            <div class="time-cell">
              <el-icon><Clock /></el-icon>
              <span>{{ row.createTime }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right" align="center">
          <template #default="{ row }">
            <el-button-group class="action-buttons">
              <el-button type="primary" size="small" :icon="View" @click="handleView(row)" v-permission="['system:user:query']" />
              <el-button type="success" size="small" :icon="Edit" @click="handleEdit(row)" v-permission="['system:user:edit']" />
              <el-button type="danger" size="small" :icon="Delete" @click="handleDelete(row)" v-permission="['system:user:remove']" />
            </el-button-group>
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

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="900px"
      @close="handleDialogClose"
      :close-on-click-modal="false"
      class="user-dialog"
    >
      <div class="dialog-content">
        <!-- 头像上传区域 -->
        <div class="avatar-section">
          <div class="avatar-upload">
            <el-upload
              class="avatar-uploader"
              :show-file-list="false"
              :before-upload="beforeAvatarUpload"
              :http-request="handleAvatarUpload"
              :disabled="uploadLoading"
            >
              <el-avatar :size="120" :src="getFileUrl(form.avatar)" class="avatar-preview" v-loading="uploadLoading">
                <el-icon :size="50"><User /></el-icon>
              </el-avatar>
              <div class="avatar-overlay" v-if="!uploadLoading">
                <el-icon :size="30"><Camera /></el-icon>
                <span>点击上传</span>
              </div>
            </el-upload>
            <div class="avatar-tips">
              <p>支持 JPG、PNG、GIF 格式</p>
              <p>文件大小不超过 2MB</p>
              <p v-if="uploadLoading" style="color: #42a5f5">上传中...</p>
            </div>
          </div>
        </div>

        <!-- 使用扩展表单组件 -->
        <UserFormExtended 
          ref="userFormRef" 
          v-model="form" 
          :is-edit="isEdit"
        />
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false" :icon="Close" size="large">
            取消
          </el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading" :icon="Check" size="large">
            {{ isEdit ? '保存修改' : '立即创建' }}
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Edit, Delete, Check, Close, View, Clock, Camera } from '@element-plus/icons-vue'
import UserFormExtended from '@/components/UserFormExtended.vue'
import { getUserList, addUser, updateUser, deleteUser } from '@/api/user'
import { getRoleList } from '@/api/role'
import { uploadFile } from '@/api/file'
import { getFileUrl } from '@/utils/file'

const tableData = ref([])
const roleList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const userFormRef = ref(null)

// 统计数据
const adminCount = computed(() => tableData.value.filter(u => u.roleId === 1).length)
const normalCount = computed(() => tableData.value.filter(u => u.roleId === 2).length)
const activeCount = computed(() => tableData.value.filter(u => u.status === 1).length)

const searchForm = reactive({
  username: '',
  realName: '',
  studentNo: '',
  userType: null,
  roleId: null,
  status: null
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const form = reactive({
  id: null,
  username: '',
  password: '',
  nickname: '',
  realName: '',
  sex: '2',
  email: '',
  phone: '',
  wechat: '',
  qq: '',
  avatar: '',
  studentNo: '',
  userType: 'student',
  enrollmentYear: null,
  graduationYear: null,
  major: '',
  className: '',
  college: '',
  roleId: 3,
  status: 1,
  remark: ''
})

// rules 已移至 UserFormExtended 组件中

const loadData = async () => {
  try {
    const res = await getUserList({
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...searchForm
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
  searchForm.username = ''
  searchForm.realName = ''
  searchForm.studentNo = ''
  searchForm.userType = null
  searchForm.roleId = null
  searchForm.status = null
  pagination.pageNum = 1
  loadData()
}

const handleSizeChange = () => {
  loadData()
}

const handleCurrentChange = () => {
  loadData()
}

const handleAdd = () => {
  dialogTitle.value = '新增用户'
  isEdit.value = false
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑用户'
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleView = (row) => {
  const sexText = row.sex === '0' ? '男' : row.sex === '1' ? '女' : '未知'
  const statusText = row.status === 1 ? '<span style="color: #67c23a;">●</span> 启用' : '<span style="color: #f56c6c;">●</span> 禁用'
  
  ElMessageBox.alert(
    `<div style="max-height: 600px; overflow-y: auto; padding: 10px;">
      <!-- 用户头像和基本信息 -->
      <div style="text-align: center; margin-bottom: 24px; padding: 20px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 12px; color: white;">
        <div style="width: 80px; height: 80px; margin: 0 auto 12px; border-radius: 50%; overflow: hidden; border: 3px solid white; background: white;">
          ${row.avatar ? `<img src="${getFileUrl(row.avatar)}" style="width: 100%; height: 100%; object-fit: cover;" />` : '<div style="width: 100%; height: 100%; display: flex; align-items: center; justify-content: center; font-size: 32px; color: #909399;">👤</div>'}
        </div>
        <h3 style="margin: 0 0 8px 0; font-size: 20px;">${row.realName || row.nickname}</h3>
        <p style="margin: 0; opacity: 0.9; font-size: 14px;">@${row.username}</p>
      </div>
      
      <!-- 信息卡片 -->
      <div style="display: grid; gap: 16px;">
        <!-- 基本信息 -->
        <div style="background: #f5f7fa; padding: 16px; border-radius: 8px; border-left: 4px solid #409eff;">
          <h4 style="margin: 0 0 12px 0; color: #409eff; font-size: 16px;">
            <span style="margin-right: 6px;">👤</span>基本信息
          </h4>
          <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 12px; font-size: 14px;">
            <div><span style="color: #909399;">用户名：</span><strong>${row.username}</strong></div>
            <div><span style="color: #909399;">真实姓名：</span><strong>${row.realName || '未填写'}</strong></div>
            <div><span style="color: #909399;">昵称：</span><strong>${row.nickname}</strong></div>
            <div><span style="color: #909399;">学号：</span><strong>${row.studentNo || '未填写'}</strong></div>
            <div><span style="color: #909399;">性别：</span><strong>${sexText}</strong></div>
            <div><span style="color: #909399;">用户类型：</span><strong>${getUserTypeText(row.userType)}</strong></div>
          </div>
        </div>
        
        <!-- 联系方式 -->
        <div style="background: #f5f7fa; padding: 16px; border-radius: 8px; border-left: 4px solid #67c23a;">
          <h4 style="margin: 0 0 12px 0; color: #67c23a; font-size: 16px;">
            <span style="margin-right: 6px;">📞</span>联系方式
          </h4>
          <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 12px; font-size: 14px;">
            <div><span style="color: #909399;">邮箱：</span><strong>${row.email || '未设置'}</strong></div>
            <div><span style="color: #909399;">手机：</span><strong>${row.phone || '未设置'}</strong></div>
            <div><span style="color: #909399;">微信：</span><strong>${row.wechat || '未填写'}</strong></div>
            <div><span style="color: #909399;">QQ：</span><strong>${row.qq || '未填写'}</strong></div>
          </div>
        </div>
        
        <!-- 学籍信息 -->
        <div style="background: #f5f7fa; padding: 16px; border-radius: 8px; border-left: 4px solid #e6a23c;">
          <h4 style="margin: 0 0 12px 0; color: #e6a23c; font-size: 16px;">
            <span style="margin-right: 6px;">🎓</span>学籍信息
          </h4>
          <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 12px; font-size: 14px;">
            <div><span style="color: #909399;">学院：</span><strong>${row.college || '未填写'}</strong></div>
            <div><span style="color: #909399;">专业：</span><strong>${row.major || '未填写'}</strong></div>
            <div><span style="color: #909399;">班级：</span><strong>${row.className || '未填写'}</strong></div>
            <div><span style="color: #909399;">入学年份：</span><strong>${row.enrollmentYear || '未填写'}</strong></div>
            <div><span style="color: #909399;">毕业年份：</span><strong>${row.graduationYear || '未填写'}</strong></div>
          </div>
        </div>
        
        <!-- 系统信息 -->
        <div style="background: #f5f7fa; padding: 16px; border-radius: 8px; border-left: 4px solid #909399;">
          <h4 style="margin: 0 0 12px 0; color: #909399; font-size: 16px;">
            <span style="margin-right: 6px;">⚙️</span>系统信息
          </h4>
          <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 12px; font-size: 14px;">
            <div><span style="color: #909399;">角色：</span><strong>${row.roleName || '未分配'}</strong></div>
            <div><span style="color: #909399;">状态：</span><strong>${statusText}</strong></div>
            <div><span style="color: #909399;">最后登录：</span><strong>${row.lastLoginTime || '未登录'}</strong></div>
            <div><span style="color: #909399;">创建时间：</span><strong>${row.createTime || '-'}</strong></div>
          </div>
        </div>
        
        ${row.remark ? `
        <!-- 备注信息 -->
        <div style="background: #fef0f0; padding: 16px; border-radius: 8px; border-left: 4px solid #f56c6c;">
          <h4 style="margin: 0 0 8px 0; color: #f56c6c; font-size: 16px;">
            <span style="margin-right: 6px;">📝</span>备注
          </h4>
          <p style="margin: 0; font-size: 14px; color: #606266;">${row.remark}</p>
        </div>
        ` : ''}
      </div>
    </div>`,
    '用户详情',
    {
      dangerouslyUseHTMLString: true,
      confirmButtonText: '关闭',
      width: '700px',
      customClass: 'user-detail-dialog'
    }
  )
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该用户吗？此操作不可恢复！', '警告', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning',
    center: true
  }).then(async () => {
    try {
      await deleteUser(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error(error)
    }
  })
}

const handleSubmit = async () => {
  if (!userFormRef.value) return
  
  try {
    const valid = await userFormRef.value.validate()
    if (!valid) return
    
    submitLoading.value = true
    try {
      if (isEdit.value) {
        await updateUser(form)
        ElMessage.success('更新成功')
      } else {
        await addUser(form)
        ElMessage.success('添加成功')
      }
      dialogVisible.value = false
      loadData()
    } catch (error) {
      console.error(error)
    } finally {
      submitLoading.value = false
    }
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

const uploadLoading = ref(false)

const beforeAvatarUpload = (file) => {
  const isImage = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('头像只能是 JPG、PNG 或 GIF 格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('头像大小不能超过 2MB!')
    return false
  }
  return true
}

const handleAvatarUpload = async (options) => {
  const { file } = options
  uploadLoading.value = true
  
  try {
    // 1. 上传文件到服务器
    const res = await uploadFile(file)
    const avatarUrl = res.data.url
    form.avatar = avatarUrl
    
    // 2. 如果是编辑模式，立即保存到数据库
    if (isEdit.value && form.id) {
      await updateUser({
        id: form.id,
        avatar: avatarUrl
      })
      
      // 3. 更新表格数据
      const userIndex = tableData.value.findIndex(u => u.id === form.id)
      if (userIndex !== -1) {
        tableData.value[userIndex].avatar = avatarUrl
      }
      
      // 4. 如果是当前登录用户，更新localStorage和触发Header更新
      const currentUser = JSON.parse(localStorage.getItem('user') || '{}')
      if (currentUser.id === form.id) {
        currentUser.avatar = avatarUrl
        localStorage.setItem('user', JSON.stringify(currentUser))
        // 触发自定义事件通知Header更新
        window.dispatchEvent(new CustomEvent('userAvatarUpdate', { 
          detail: { avatar: avatarUrl } 
        }))
      }
      
      ElMessage.success('头像上传并保存成功')
    } else {
      ElMessage.success('头像上传成功，请点击保存完成创建')
    }
  } catch (error) {
    ElMessage.error('头像上传失败')
    console.error(error)
  } finally {
    uploadLoading.value = false
  }
}

const handleDialogClose = () => {
  userFormRef.value?.resetFields()
  Object.assign(form, {
    id: null,
    username: '',
    password: '',
    nickname: '',
    realName: '',
    sex: '2',
    email: '',
    phone: '',
    wechat: '',
    qq: '',
    avatar: '',
    studentNo: '',
    userType: 'student',
    enrollmentYear: null,
    graduationYear: null,
    major: '',
    className: '',
    college: '',
    roleId: 3,
    status: 1,
    remark: ''
  })
}

// 状态切换
const handleStatusChange = async (row) => {
  try {
    await updateUser({
      id: row.id,
      status: row.status
    })
    ElMessage.success(`已${row.status === 1 ? '启用' : '禁用'}用户`)
  } catch (error) {
    row.status = row.status === 1 ? 0 : 1
    console.error(error)
  }
}

// 用户类型文本
const getUserTypeText = (type) => {
  const typeMap = {
    student: '在校生',
    graduate: '毕业生',
    teacher: '教师'
  }
  return typeMap[type] || '未知'
}

// 用户类型标签
const getUserTypeTag = (type) => {
  const tagMap = {
    student: 'primary',
    graduate: 'success',
    teacher: 'warning'
  }
  return tagMap[type] || 'info'
}

// 角色标签类型
const getRoleTagType = (roleKey) => {
  if (!roleKey) return 'info'
  if (roleKey.includes('super_admin')) return 'danger'
  if (roleKey.includes('admin')) return 'warning'
  if (roleKey.includes('president') || roleKey.includes('vice')) return 'success'
  return 'primary'
}

// 加载角色列表
const loadRoles = async () => {
  try {
    const res = await getRoleList({ pageNum: 1, pageSize: 100 })
    roleList.value = res.data.records || []
  } catch (error) {
    console.error('加载角色列表失败:', error)
  }
}

onMounted(() => {
  loadData()
  loadRoles()
})
</script>

<style scoped>
.user-container {
  padding: 0;
}

/* 页面头部 */
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

/* 统计卡片 */
.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 20px;
  transition: all 0.3s;
  cursor: pointer;
  border: 2px solid transparent;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.stat-card-primary:hover {
  border-color: #42a5f5;
}

.stat-card-success:hover {
  border-color: #67c23a;
}

.stat-card-warning:hover {
  border-color: #e6a23c;
}

.stat-card-info:hover {
  border-color: #909399;
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

.stat-card-primary .stat-icon {
  background: linear-gradient(135deg, #42a5f5 0%, #2196f3 100%);
}

.stat-card-success .stat-icon {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
}

.stat-card-warning .stat-icon {
  background: linear-gradient(135deg, #e6a23c 0%, #f56c6c 100%);
}

.stat-card-info .stat-icon {
  background: linear-gradient(135deg, #909399 0%, #b3b6bb 100%);
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

/* 搜索卡片 */
.search-card {
  margin-bottom: 16px;
  border-radius: 12px;
  border: none;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.search-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 2px solid #f0f2f5;
}

.search-header .el-icon {
  color: #42a5f5;
  font-size: 20px;
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

/* 表格样式 */
.modern-table :deep(.el-table__body tr) {
  transition: all 0.3s;
}

.modern-table :deep(.el-table__body tr:hover) {
  background: #f5f7fa !important;
}

.modern-table :deep(.el-table__cell) {
  padding: 16px 0;
  border-bottom: 1px solid #f0f2f5;
}

.index-cell {
  width: 32px;
  height: 32px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-weight: 600;
  margin: 0 auto;
}

.user-info-cell {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-avatar {
  border: 2px solid #f0f2f5;
  transition: all 0.3s;
}

.user-avatar:hover {
  border-color: #42a5f5;
  transform: scale(1.1);
}

.user-details {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.user-name {
  font-weight: 600;
  color: #303133;
  font-size: 15px;
}

.user-nickname {
  font-size: 13px;
  color: #909399;
}

.role-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 16px;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 600;
}

.role-admin {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: #fff;
}

.role-user {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: #fff;
}

.contact-cell {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #606266;
}

.time-cell {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #909399;
  font-size: 13px;
}

.action-buttons {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border-radius: 6px;
  overflow: hidden;
}

.action-buttons :deep(.el-button) {
  border: none;
  padding: 8px 12px;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

/* 对话框样式 */
.user-dialog :deep(.el-dialog) {
  border-radius: 16px;
  overflow: hidden;
}

.user-dialog :deep(.el-dialog__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 24px 30px;
  margin: 0;
}

.user-dialog :deep(.el-dialog__title) {
  color: #fff;
  font-size: 20px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-dialog :deep(.el-dialog__headerbtn) {
  top: 24px;
  right: 30px;
}

.user-dialog :deep(.el-dialog__headerbtn .el-dialog__close) {
  color: #fff;
  font-size: 22px;
  transition: all 0.3s;
}

.user-dialog :deep(.el-dialog__headerbtn .el-dialog__close:hover) {
  transform: rotate(90deg);
}

.user-dialog :deep(.el-dialog__body) {
  padding: 0;
}

.dialog-content {
  display: flex;
  gap: 30px;
  padding: 30px;
}

/* 头像上传区域 */
.avatar-section {
  flex-shrink: 0;
}

.avatar-upload {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
}

.avatar-uploader {
  position: relative;
  cursor: pointer;
}

.avatar-preview {
  border: 3px solid #f0f2f5;
  transition: all 0.3s;
}

.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  opacity: 0;
  transition: all 0.3s;
  color: #fff;
}

.avatar-uploader:hover .avatar-overlay {
  opacity: 1;
}

.avatar-uploader:hover .avatar-preview {
  border-color: #667eea;
  transform: scale(1.05);
}

.avatar-overlay span {
  font-size: 14px;
}

.avatar-tips {
  text-align: center;
  color: #909399;
  font-size: 12px;
  line-height: 1.8;
}

.avatar-tips p {
  margin: 0;
}

/* 表单区域 */
.user-form {
  flex: 1;
}

.user-form :deep(.el-divider) {
  margin: 20px 0;
}

.user-form :deep(.el-divider__text) {
  background: transparent;
  color: #606266;
  font-weight: 600;
  display: flex;
  align-items: center;
}

.user-form :deep(.el-form-item) {
  margin-bottom: 20px;
}

.user-form :deep(.el-input__wrapper) {
  border-radius: 8px;
  transition: all 0.3s;
}

.user-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #667eea inset;
}

.user-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #667eea inset;
}

.user-form :deep(.el-select .el-input__wrapper) {
  border-radius: 8px;
}

/* 对话框底部 */
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 20px 30px;
  background: #f5f7fa;
  border-top: 1px solid #e8eaec;
}

.dialog-footer .el-button {
  min-width: 100px;
  border-radius: 8px;
  font-weight: 500;
}

/* 用户详情弹窗样式 */
:deep(.user-detail-dialog) {
  border-radius: 16px;
  overflow: hidden;
}

:deep(.user-detail-dialog .el-message-box__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px 24px;
}

:deep(.user-detail-dialog .el-message-box__title) {
  color: #fff;
  font-size: 18px;
  font-weight: 600;
}

:deep(.user-detail-dialog .el-message-box__headerbtn .el-message-box__close) {
  color: #fff;
  font-size: 20px;
}

:deep(.user-detail-dialog .el-message-box__content) {
  padding: 0;
}

:deep(.user-detail-dialog .el-message-box__message) {
  padding: 0;
}

:deep(.user-detail-dialog .el-button--primary) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  padding: 10px 24px;
  border-radius: 8px;
  font-weight: 500;
}
</style>
