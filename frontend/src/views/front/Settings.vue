<template>
  <div class="settings-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>账户设置</span>
        </div>
      </template>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="安全设置" name="security">
          <el-form :model="securityForm" label-width="120px" style="max-width: 600px;">
            <el-form-item label="当前密码">
              <el-input v-model="securityForm.oldPassword" type="password" />
            </el-form-item>
            <el-form-item label="新密码">
              <el-input v-model="securityForm.newPassword" type="password" />
            </el-form-item>
            <el-form-item label="确认新密码">
              <el-input v-model="securityForm.confirmPassword" type="password" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleChangePassword">修改密码</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="通知设置" name="notification">
          <el-form label-width="120px" style="max-width: 600px;">
            <el-form-item label="邮件通知">
              <el-switch v-model="notificationForm.email" />
            </el-form-item>
            <el-form-item label="短信通知">
              <el-switch v-model="notificationForm.sms" />
            </el-form-item>
            <el-form-item label="系统消息">
              <el-switch v-model="notificationForm.system" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSaveNotification">保存设置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="隐私设置" name="privacy">
          <el-form label-width="120px" style="max-width: 600px;">
            <el-form-item label="公开资料">
              <el-switch v-model="privacyForm.publicProfile" />
            </el-form-item>
            <el-form-item label="显示邮箱">
              <el-switch v-model="privacyForm.showEmail" />
            </el-form-item>
            <el-form-item label="显示手机号">
              <el-switch v-model="privacyForm.showPhone" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSavePrivacy">保存设置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const activeTab = ref('security')

const securityForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const notificationForm = reactive({
  email: true,
  sms: false,
  system: true
})

const privacyForm = reactive({
  publicProfile: true,
  showEmail: false,
  showPhone: false
})

const handleChangePassword = () => {
  if (!securityForm.oldPassword || !securityForm.newPassword) {
    ElMessage.warning('请填写完整信息')
    return
  }
  if (securityForm.newPassword !== securityForm.confirmPassword) {
    ElMessage.error('两次密码输入不一致')
    return
  }
  ElMessage.success('密码修改成功')
}

const handleSaveNotification = () => {
  ElMessage.success('通知设置已保存')
}

const handleSavePrivacy = () => {
  ElMessage.success('隐私设置已保存')
}
</script>

<style scoped>
.settings-container {
  padding: 0;
}

.card-header {
  font-weight: 600;
  font-size: 16px;
}
</style>
