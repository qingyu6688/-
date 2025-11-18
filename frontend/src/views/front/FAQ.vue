<template>
  <div class="simple-page">
    <div class="page-header">
      <h1>常见问题</h1>
      <p>解答您的疑问</p>
    </div>
    <el-card class="content-card">
      <el-collapse>
        <el-collapse-item title="如何修改个人信息？" name="1">
          <p>进入个人中心，点击"编辑资料"按钮即可修改个人信息。</p>
        </el-collapse-item>
        <el-collapse-item title="忘记密码怎么办？" name="2">
          <p>点击登录页面的"忘记密码"链接，通过邮箱或手机号找回密码。</p>
        </el-collapse-item>
        <el-collapse-item title="如何取消活动报名？" name="3">
          <p>在"我的活动"页面找到已报名的活动，点击"取消报名"按钮。</p>
        </el-collapse-item>
        <el-collapse-item title="如何预订资源？" name="4">
          <p>进入"资源预订"页面，选择需要的资源和时间段，提交预订申请。</p>
        </el-collapse-item>
      </el-collapse>
    </el-card>
  </div>
</template>

<style scoped>
.simple-page { max-width: 1200px; margin: 0 auto; }
.page-header { text-align: center; padding: 40px 0; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 16px; color: white; margin-bottom: 32px; }
.page-header h1 { font-size: 36px; font-weight: 700; margin: 0 0 12px 0; }
.page-header p { font-size: 16px; opacity: 0.9; margin: 0; }
.content-card p { color: #666; line-height: 1.8; }
</style>
