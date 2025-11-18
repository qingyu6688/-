<template>
  <div class="simple-page">
    <div class="page-header">
      <h1>联系我们</h1>
      <p>随时欢迎您的咨询</p>
    </div>
    <el-card class="content-card">
      <div class="contact-info">
        <div class="info-item">
          <el-icon><Phone /></el-icon>
          <div>
            <h4>联系电话</h4>
            <p>010-12345678</p>
          </div>
        </div>
        <div class="info-item">
          <el-icon><Message /></el-icon>
          <div>
            <h4>电子邮箱</h4>
            <p>club@university.edu.cn</p>
          </div>
        </div>
        <div class="info-item">
          <el-icon><Location /></el-icon>
          <div>
            <h4>办公地址</h4>
            <p>学生活动中心3楼</p>
          </div>
        </div>
        <div class="info-item">
          <el-icon><Clock /></el-icon>
          <div>
            <h4>办公时间</h4>
            <p>周一至周五 9:00-17:00</p>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { Phone, Message, Location, Clock } from '@element-plus/icons-vue'
</script>

<style scoped>
.simple-page { max-width: 1200px; margin: 0 auto; }
.page-header { text-align: center; padding: 40px 0; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 16px; color: white; margin-bottom: 32px; }
.page-header h1 { font-size: 36px; font-weight: 700; margin: 0 0 12px 0; }
.page-header p { font-size: 16px; opacity: 0.9; margin: 0; }
.contact-info { display: grid; grid-template-columns: repeat(2, 1fr); gap: 24px; }
.info-item { display: flex; align-items: flex-start; gap: 16px; padding: 20px; background: #f8f9fa; border-radius: 12px; }
.info-item .el-icon { font-size: 32px; color: #667eea; }
.info-item h4 { margin: 0 0 8px 0; color: #333; }
.info-item p { margin: 0; color: #666; }
</style>
