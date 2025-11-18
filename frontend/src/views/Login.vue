<template>
  <div class="login-container">
    <!-- 动态背景粒子 -->
    <div class="particles">
      <div class="particle" v-for="i in 50" :key="i" :style="getParticleStyle(i)"></div>
    </div>
    
    <div class="login-box" :class="{ 'flip': !isLogin }">
      <div class="login-left">
        <div class="welcome-content">
          <h1 class="animate-text">欢迎使用</h1>
          <h2 class="animate-text delay-1">管理系统</h2>
          <p class="animate-text delay-2">一个现代化的管理系统解决方案</p>
          <div class="features">
            <div class="feature-item animate-slide delay-3">
              <el-icon :size="24"><Check /></el-icon>
              <span>安全可靠</span>
            </div>
            <div class="feature-item animate-slide delay-4">
              <el-icon :size="24"><Check /></el-icon>
              <span>简单易用</span>
            </div>
            <div class="feature-item animate-slide delay-5">
              <el-icon :size="24"><Check /></el-icon>
              <span>功能强大</span>
            </div>
          </div>
        </div>
      </div>
      <div class="login-right">
        <el-card class="login-card">
          <div class="card-header">
            <h3 class="title">{{ isLogin ? '登录' : '注册' }}</h3>
            <p class="subtitle">{{ isLogin ? '欢迎回来' : '创建新账户' }}</p>
          </div>
          
          <!-- 登录表单 -->
          <el-form v-if="isLogin" :model="loginForm" :rules="loginRules" ref="loginFormRef" label-width="0">
            <el-form-item prop="username">
              <el-input v-model="loginForm.username" placeholder="请输入用户名" prefix-icon="User" size="large" />
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" prefix-icon="Lock"
                size="large" show-password @keyup.enter="handleLogin" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="large" style="width: 100%" :loading="loading" @click="handleLogin">
                登录
              </el-button>
            </el-form-item>
          </el-form>

          <!-- 注册表单 -->
          <el-form v-else :model="registerForm" :rules="registerRules" ref="registerFormRef" label-width="0">
            <el-form-item prop="username">
              <el-input v-model="registerForm.username" placeholder="请输入用户名" prefix-icon="User" size="large" />
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" prefix-icon="Lock"
                size="large" show-password />
            </el-form-item>
            <el-form-item prop="confirmPassword">
              <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请确认密码"
                prefix-icon="Lock" size="large" show-password />
            </el-form-item>
            <el-form-item prop="nickname">
              <el-input v-model="registerForm.nickname" placeholder="请输入昵称" prefix-icon="UserFilled" size="large" />
            </el-form-item>
            <el-form-item prop="email">
              <el-input v-model="registerForm.email" placeholder="请输入邮箱" prefix-icon="Message" size="large" />
            </el-form-item>
            <el-form-item prop="phone">
              <el-input v-model="registerForm.phone" placeholder="请输入手机号" prefix-icon="Phone" size="large" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="large" style="width: 100%" :loading="loading" @click="handleRegister">
                注册
              </el-button>
            </el-form-item>
          </el-form>

          <div class="switch-mode">
            <el-button type="text" @click="toggleMode">
              {{ isLogin ? '还没有账号？立即注册' : '已有账号？立即登录' }}
            </el-button>
          </div>

          <div class="tips" v-if="isLogin">
            <p>测试账号：admin / 123456 (系统管理员-后台)</p>
            <p>测试账号：user / 123456 (普通用户-前台)</p>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login, register } from '@/api/user'
import { getUserPermissions } from '@/api/menu'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const loginFormRef = ref(null)
const registerFormRef = ref(null)
const loading = ref(false)
const isLogin = ref(true)

// 生成粒子样式
const getParticleStyle = (index) => {
  const size = Math.random() * 6 + 2
  const left = Math.random() * 100
  const animationDuration = Math.random() * 20 + 10
  const animationDelay = Math.random() * 5
  
  return {
    width: `${size}px`,
    height: `${size}px`,
    left: `${left}%`,
    animationDuration: `${animationDuration}s`,
    animationDelay: `${animationDelay}s`
  }
}

const loginForm = reactive({
  username: '',
  password: ''
})

const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  nickname: '',
  email: '',
  phone: ''
})

const loginRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== registerForm.password) {
    callback(new Error('两次密码输入不一致'))
  } else {
    callback()
  }
}

const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

const toggleMode = () => {
  isLogin.value = !isLogin.value
}

const handleLogin = async () => {
  if (!loginFormRef.value) return

  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const res = await login(loginForm)
        
        // 保存token和用户信息
        userStore.setToken(res.data.token)
        userStore.setUserInfo(res.data.user)
        
        // 打印用户信息用于调试
        console.log('登录成功，用户信息:', res.data.user)
        console.log('用户角色:', res.data.user.roleKey)
        
        // 加载用户权限
        try {
          const permsRes = await getUserPermissions()
          userStore.setPermissions(permsRes.data || [])
        } catch (error) {
          console.error('加载权限失败:', error)
        }
        
        ElMessage.success('登录成功')
        
        // 根据角色跳转不同页面
        // 只有超级管理员(super_admin)和系统管理员(admin)进入后台，其他角色进入前台
        const roleKey = res.data.user.roleKey
        console.log('准备跳转，角色标识:', roleKey)
        
        if (roleKey === 'super_admin' || roleKey === 'admin') {
          console.log('跳转到后台管理页面')
          await router.push('/admin/home')
        } else {
          console.log('跳转到前台页面')
          await router.push('/front/home')
        }
      } catch (error) {
        console.error('登录失败:', error)
        ElMessage.error(error.message || '登录失败')
      } finally {
        loading.value = false
      }
    }
  })
}

const handleRegister = async () => {
  if (!registerFormRef.value) return

  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await register(registerForm)
        ElMessage.success('注册成功，请登录')
        isLogin.value = true
        // 清空注册表单
        Object.keys(registerForm).forEach(key => {
          registerForm[key] = ''
        })
      } catch (error) {
        console.error(error)
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
  position: relative;
  overflow: hidden;
}

/* 动态背景粒子 */
.particles {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  pointer-events: none;
}

.particle {
  position: absolute;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 50%;
  animation: float linear infinite;
}

@keyframes float {
  0% {
    transform: translateY(100vh) rotate(0deg);
    opacity: 0;
  }
  10% {
    opacity: 1;
  }
  90% {
    opacity: 1;
  }
  100% {
    transform: translateY(-100px) rotate(360deg);
    opacity: 0;
  }
}

.login-box {
  display: flex;
  width: 1000px;
  min-height: 600px;
  background: #fff;
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  overflow: hidden;
  position: relative;
  z-index: 1;
  animation: slideUp 0.8s ease-out;
  transition: transform 0.6s ease;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(50px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.login-box.flip {
  transform: rotateY(0deg);
}

.login-left {
  flex: 1;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  position: relative;
  overflow: hidden;
}

.login-left::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 0%, transparent 70%);
  animation: rotate 20s linear infinite;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.welcome-content {
  position: relative;
  z-index: 1;
}

.welcome-content h1 {
  font-size: 48px;
  font-weight: bold;
  margin-bottom: 10px;
  letter-spacing: 2px;
}

.welcome-content h2 {
  font-size: 36px;
  font-weight: 300;
  margin-bottom: 30px;
}

.welcome-content p {
  font-size: 18px;
  opacity: 0.9;
  margin-bottom: 40px;
}

/* 文字动画 */
.animate-text {
  animation: fadeInUp 0.8s ease-out;
  animation-fill-mode: both;
}

.animate-text.delay-1 {
  animation-delay: 0.2s;
}

.animate-text.delay-2 {
  animation-delay: 0.4s;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.features {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 16px;
}

/* 特性项滑入动画 */
.animate-slide {
  animation: slideInLeft 0.6s ease-out;
  animation-fill-mode: both;
}

.animate-slide.delay-3 {
  animation-delay: 0.6s;
}

.animate-slide.delay-4 {
  animation-delay: 0.8s;
}

.animate-slide.delay-5 {
  animation-delay: 1s;
}

@keyframes slideInLeft {
  from {
    opacity: 0;
    transform: translateX(-30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.login-right {
  flex: 1;
  padding: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-card {
  width: 100%;
  border: none;
  box-shadow: none;
  animation: fadeIn 1s ease-out 0.3s;
  animation-fill-mode: both;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

/* 表单输入框悬停效果 */
.login-card :deep(.el-input__wrapper) {
  transition: all 0.3s ease;
}

.login-card :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #667eea inset;
}

.login-card :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #667eea inset;
}

/* 按钮悬停效果 */
.login-card :deep(.el-button--primary) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.3s ease;
}

.login-card :deep(.el-button--primary:hover) {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}

.card-header {
  text-align: center;
  margin-bottom: 30px;
}

.title {
  font-size: 32px;
  font-weight: bold;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 8px;
  animation: titlePulse 2s ease-in-out infinite;
}

@keyframes titlePulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.8;
  }
}

.subtitle {
  font-size: 16px;
  color: #999;
}

.switch-mode {
  text-align: center;
  margin-top: 20px;
}

.switch-mode .el-button {
  color: #667eea;
  font-size: 14px;
  transition: all 0.3s ease;
}

.switch-mode .el-button:hover {
  color: #764ba2;
  transform: scale(1.05);
}

.tips {
  text-align: center;
  color: #999;
  font-size: 13px;
  margin-top: 20px;
  line-height: 1.8;
}

.tips p {
  margin: 4px 0;
}

@media (max-width: 768px) {
  .login-box {
    flex-direction: column;
    width: 100%;
    max-width: 500px;
  }

  .login-left {
    padding: 40px;
  }

  .welcome-content h1 {
    font-size: 32px;
  }

  .welcome-content h2 {
    font-size: 24px;
  }

  .login-right {
    padding: 40px;
  }
}
</style>
