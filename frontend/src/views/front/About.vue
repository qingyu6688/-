<template>
  <div class="about-page">
    <div class="page-header">
      <h1>社团简介</h1>
      <p>了解我们的使命、愿景和价值观</p>
    </div>

    <div class="content-section">
      <el-card class="intro-card">
        <template #header>
          <div class="card-header">
            <el-icon><InfoFilled /></el-icon>
            <span>关于我们</span>
          </div>
        </template>
        <div class="intro-content">
          <p class="lead">
            高校社团活动综合管理系统致力于为广大师生提供一个高效、便捷的社团活动管理平台。
            我们通过信息化手段，实现社团活动的全流程管理、校友信息的系统化维护，以及成员间的高效互动交流。
          </p>
          
          <div class="mission-vision">
            <div class="mission">
              <h3><el-icon><Flag /></el-icon> 我们的使命</h3>
              <p>打造最专业的高校社团活动管理平台，让每一个社团活动都能顺利开展，让每一位成员都能找到归属感。</p>
            </div>
            
            <div class="vision">
              <h3><el-icon><View /></el-icon> 我们的愿景</h3>
              <p>成为全国领先的高校社团管理系统，推动校园文化建设，促进学生全面发展。</p>
            </div>
          </div>
        </div>
      </el-card>

      <el-card class="values-card">
        <template #header>
          <div class="card-header">
            <el-icon><Star /></el-icon>
            <span>核心价值观</span>
          </div>
        </template>
        <div class="values-grid">
          <div class="value-item">
            <el-icon class="value-icon"><User /></el-icon>
            <h4>以人为本</h4>
            <p>关注每一位用户的需求和体验</p>
          </div>
          <div class="value-item">
            <el-icon class="value-icon"><TrendCharts /></el-icon>
            <h4>持续创新</h4>
            <p>不断优化功能，提升服务质量</p>
          </div>
          <div class="value-item">
            <el-icon class="value-icon"><Connection /></el-icon>
            <h4>开放协作</h4>
            <p>促进交流合作，共建和谐社区</p>
          </div>
          <div class="value-item">
            <el-icon class="value-icon"><Trophy /></el-icon>
            <h4>追求卓越</h4>
            <p>精益求精，打造优质平台</p>
          </div>
        </div>
      </el-card>

      <el-card class="features-card">
        <template #header>
          <div class="card-header">
            <el-icon><Grid /></el-icon>
            <span>核心功能</span>
          </div>
        </template>
        <div class="features-list">
          <div class="feature-item">
            <el-icon class="feature-icon"><UserFilled /></el-icon>
            <div class="feature-content">
              <h4>校友信息管理</h4>
              <p>完善的校友档案系统，记录每一位成员的成长轨迹</p>
            </div>
          </div>
          <div class="feature-item">
            <el-icon class="feature-icon"><ChatDotRound /></el-icon>
            <div class="feature-content">
              <h4>互动交流平台</h4>
              <p>论坛、动态、私信等多种交流方式，促进成员互动</p>
            </div>
          </div>
          <div class="feature-item">
            <el-icon class="feature-icon"><Calendar /></el-icon>
            <div class="feature-content">
              <h4>活动管理系统</h4>
              <p>从发布、报名到签到评价，全流程活动管理</p>
            </div>
          </div>
          <div class="feature-item">
            <el-icon class="feature-icon"><Box /></el-icon>
            <div class="feature-content">
              <h4>服务支持模块</h4>
              <p>资源预订、财务管理、志愿服务等多项支持</p>
            </div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { InfoFilled, Flag, View, Star, User, TrendCharts, Connection, Trophy, Grid, UserFilled, ChatDotRound, Calendar, Box } from '@element-plus/icons-vue'
</script>

<style scoped>
.about-page {
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  text-align: center;
  padding: 40px 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  color: white;
  margin-bottom: 32px;
}

.page-header h1 {
  font-size: 36px;
  font-weight: 700;
  margin: 0 0 12px 0;
}

.page-header p {
  font-size: 16px;
  opacity: 0.9;
  margin: 0;
}

.content-section {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: 600;
  color: #667eea;
}

.intro-content .lead {
  font-size: 16px;
  line-height: 1.8;
  color: #555;
  margin-bottom: 32px;
}

.mission-vision {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24px;
}

.mission, .vision {
  padding: 24px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e8eaf6 100%);
  border-radius: 12px;
}

.mission h3, .vision h3 {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  color: #667eea;
  margin: 0 0 12px 0;
}

.mission p, .vision p {
  color: #666;
  line-height: 1.6;
  margin: 0;
}

.values-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.value-item {
  text-align: center;
  padding: 24px;
  background: #f8f9fa;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.value-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.2);
}

.value-icon {
  font-size: 48px;
  color: #667eea;
  margin-bottom: 16px;
}

.value-item h4 {
  font-size: 16px;
  color: #333;
  margin: 0 0 8px 0;
}

.value-item p {
  font-size: 14px;
  color: #666;
  margin: 0;
}

.features-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.feature-item {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.feature-item:hover {
  background: linear-gradient(135deg, #f5f7fa 0%, #e8eaf6 100%);
  transform: translateX(5px);
}

.feature-icon {
  font-size: 32px;
  color: #667eea;
  flex-shrink: 0;
}

.feature-content h4 {
  font-size: 16px;
  color: #333;
  margin: 0 0 8px 0;
}

.feature-content p {
  font-size: 14px;
  color: #666;
  margin: 0;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .mission-vision {
    grid-template-columns: 1fr;
  }
  
  .values-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .page-header h1 {
    font-size: 28px;
  }
}
</style>
