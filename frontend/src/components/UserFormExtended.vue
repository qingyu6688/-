<template>
  <el-form :model="formData" :rules="rules" ref="formRef" label-width="100px" class="user-form-extended">
    <!-- 基本信息 -->
    <el-divider content-position="left">
      <el-icon><User /></el-icon>
      <span style="margin-left: 8px">基本信息</span>
    </el-divider>
    
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="用户名" prop="username">
          <el-input 
            v-model="formData.username" 
            :disabled="isEdit" 
            placeholder="请输入用户名"
            prefix-icon="User"
          />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="真实姓名" prop="realName">
          <el-input 
            v-model="formData.realName" 
            placeholder="请输入真实姓名"
            prefix-icon="UserFilled"
          />
        </el-form-item>
      </el-col>
    </el-row>
    
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="昵称" prop="nickname">
          <el-input 
            v-model="formData.nickname" 
            placeholder="请输入昵称"
            prefix-icon="UserFilled"
          />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="formData.sex">
            <el-radio label="0">男</el-radio>
            <el-radio label="1">女</el-radio>
            <el-radio label="2">未知</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>
    </el-row>
    
    <el-form-item label="密码" prop="password" v-if="!isEdit">
      <el-input 
        v-model="formData.password" 
        type="password" 
        placeholder="请输入密码（6-20位）"
        prefix-icon="Lock"
        show-password
      />
    </el-form-item>

    <!-- 联系方式 -->
    <el-divider content-position="left">
      <el-icon><Message /></el-icon>
      <span style="margin-left: 8px">联系方式</span>
    </el-divider>
    
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="邮箱" prop="email">
          <el-input 
            v-model="formData.email" 
            placeholder="请输入邮箱"
            prefix-icon="Message"
          />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="手机号" prop="phone">
          <el-input 
            v-model="formData.phone" 
            placeholder="请输入手机号"
            prefix-icon="Phone"
          />
        </el-form-item>
      </el-col>
    </el-row>
    
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="微信号" prop="wechat">
          <el-input 
            v-model="formData.wechat" 
            placeholder="请输入微信号"
            prefix-icon="ChatDotRound"
          />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="QQ号" prop="qq">
          <el-input 
            v-model="formData.qq" 
            placeholder="请输入QQ号"
            prefix-icon="ChatLineRound"
          />
        </el-form-item>
      </el-col>
    </el-row>

    <!-- 学籍信息 -->
    <el-divider content-position="left">
      <el-icon><School /></el-icon>
      <span style="margin-left: 8px">学籍信息</span>
    </el-divider>
    
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="学号" prop="studentNo">
          <el-input 
            v-model="formData.studentNo" 
            placeholder="请输入学号"
            prefix-icon="Postcard"
          />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="用户类型" prop="userType">
          <el-select v-model="formData.userType" placeholder="请选择用户类型" style="width: 100%">
            <el-option label="在校生" value="student">
              <div style="display: flex; align-items: center; gap: 8px">
                <el-icon color="#409eff"><Reading /></el-icon>
                <span>在校生</span>
              </div>
            </el-option>
            <el-option label="毕业生" value="graduate">
              <div style="display: flex; align-items: center; gap: 8px">
                <el-icon color="#67c23a"><Medal /></el-icon>
                <span>毕业生</span>
              </div>
            </el-option>
            <el-option label="教师" value="teacher">
              <div style="display: flex; align-items: center; gap: 8px">
                <el-icon color="#e6a23c"><Avatar /></el-icon>
                <span>教师</span>
              </div>
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>
    
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="入学年份" prop="enrollmentYear">
          <el-date-picker
            v-model="formData.enrollmentYear"
            type="year"
            placeholder="选择入学年份"
            style="width: 100%"
            value-format="YYYY"
          />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="毕业年份" prop="graduationYear">
          <el-date-picker
            v-model="formData.graduationYear"
            type="year"
            placeholder="选择毕业年份"
            style="width: 100%"
            value-format="YYYY"
          />
        </el-form-item>
      </el-col>
    </el-row>
    
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="学院" prop="college">
          <el-input 
            v-model="formData.college" 
            placeholder="请输入学院"
            prefix-icon="OfficeBuilding"
          />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="专业" prop="major">
          <el-input 
            v-model="formData.major" 
            placeholder="请输入专业"
            prefix-icon="Reading"
          />
        </el-form-item>
      </el-col>
    </el-row>
    
    <el-form-item label="班级" prop="className">
      <el-input 
        v-model="formData.className" 
        placeholder="请输入班级"
        prefix-icon="Grid"
      />
    </el-form-item>

    <!-- 权限设置 -->
    <el-divider content-position="left">
      <el-icon><Setting /></el-icon>
      <span style="margin-left: 8px">权限设置</span>
    </el-divider>
    
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="角色" prop="roleId">
          <el-select v-model="formData.roleId" placeholder="请选择角色" style="width: 100%">
            <el-option
              v-for="role in roleList"
              :key="role.id"
              :label="role.roleName"
              :value="role.id"
            >
              <div style="display: flex; align-items: center; gap: 8px; justify-content: space-between">
                <span>{{ role.roleName }}</span>
                <el-tag :type="getRoleTagType(role.roleType)" size="small">
                  {{ getRoleTypeText(role.roleType) }}
                </el-tag>
              </div>
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="formData.status"
            :active-value="1"
            :inactive-value="0"
            active-text="启用"
            inactive-text="禁用"
            inline-prompt
            style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
          />
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script setup>
import { ref, computed } from 'vue'
import { getRoleList } from '@/api/role'

const props = defineProps({
  modelValue: {
    type: Object,
    default: () => ({})
  },
  isEdit: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue'])

const formRef = ref(null)
const roleList = ref([])

// 使用computed来直接操作父组件的数据
const formData = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  roleId: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ],
  userType: [
    { required: true, message: '请选择用户类型', trigger: 'change' }
  ]
}

// 加载角色列表
const loadRoles = async () => {
  try {
    const res = await getRoleList({ pageNum: 1, pageSize: 100 })
    roleList.value = res.data.records || res.data || []
  } catch (error) {
    console.error('加载角色列表失败:', error)
  }
}

// 获取角色类型标签
const getRoleTagType = (type) => {
  const map = {
    system: 'danger',
    club: 'primary',
    custom: 'warning'
  }
  return map[type] || 'info'
}

// 获取角色类型文本
const getRoleTypeText = (type) => {
  const map = {
    system: '系统',
    club: '社团',
    custom: '自定义'
  }
  return map[type] || '未知'
}

// 验证表单
const validate = () => {
  return formRef.value.validate()
}

// 重置表单
const resetFields = () => {
  formRef.value.resetFields()
}

// 暴露方法给父组件
defineExpose({
  validate,
  resetFields
})

// 初始化
loadRoles()
</script>

<style scoped>
.user-form-extended {
  padding: 20px 0;
}

.user-form-extended :deep(.el-divider) {
  margin: 30px 0 20px 0;
}

.user-form-extended :deep(.el-divider__text) {
  display: flex;
  align-items: center;
  font-weight: 600;
  color: #409eff;
}
</style>
