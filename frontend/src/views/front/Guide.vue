<template>
  <div class="simple-page">
    <div class="page-header">
      <h1>新手指南</h1>
      <p>快速了解系统使用方法</p>
    </div>
    <el-card class="content-card">
      <h3>欢迎使用高校社团活动综合管理系统</h3>
      <p>本指南将帮助您快速上手使用系统的各项功能。</p>
      <el-divider />
      <h4>如何注册和登录</h4>
      <p>1. 点击首页的"注册"按钮<br>2. 填写个人信息完成注册<br>3. 使用用户名和密码登录系统</p>
      <h4>如何参与活动</h4>
      <p>1. 进入"活动中心"页面<br>2. 浏览感兴趣的活动<br>3. 点击"立即报名"参与活动</p>
      <h4>如何发帖交流</h4>
      <p>1. 进入"社区交流"页面<br>2. 点击"发布帖子"按钮<br>3. 填写标题和内容，选择板块<br>4. 点击"发布"即可</p>
    </el-card>
  </div>
</template>

<style scoped>
.simple-page { max-width: 1200px; margin: 0 auto; }
.page-header { text-align: center; padding: 40px 0; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 16px; color: white; margin-bottom: 32px; }
.page-header h1 { font-size: 36px; font-weight: 700; margin: 0 0 12px 0; }
.page-header p { font-size: 16px; opacity: 0.9; margin: 0; }
.content-card { padding: 24px; }
.content-card h3 { color: #333; margin-bottom: 16px; }
.content-card h4 { color: #667eea; margin: 20px 0 12px 0; }
.content-card p { color: #666; line-height: 1.8; }
</style>
