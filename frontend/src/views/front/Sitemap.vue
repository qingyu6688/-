<template>
  <div class="simple-page">
    <div class="page-header">
      <h1>网站地图</h1>
      <p>快速导航到您需要的页面</p>
    </div>
    <el-card class="content-card">
      <div class="sitemap-grid">
        <div class="sitemap-section">
          <h3>核心功能</h3>
          <ul>
            <li><router-link to="/front/home">首页</router-link></li>
            <li><router-link to="/front/community">社区交流</router-link></li>
            <li><router-link to="/front/activities">活动中心</router-link></li>
            <li><router-link to="/front/volunteer">志愿服务</router-link></li>
            <li><router-link to="/front/resources">资源预订</router-link></li>
          </ul>
        </div>
        <div class="sitemap-section">
          <h3>关于社团</h3>
          <ul>
            <li><router-link to="/front/about">社团简介</router-link></li>
            <li><router-link to="/front/team">组织架构</router-link></li>
            <li><router-link to="/front/history">发展历程</router-link></li>
            <li><router-link to="/front/join">加入我们</router-link></li>
          </ul>
        </div>
        <div class="sitemap-section">
          <h3>帮助中心</h3>
          <ul>
            <li><router-link to="/front/guide">新手指南</router-link></li>
            <li><router-link to="/front/faq">常见问题</router-link></li>
            <li><router-link to="/front/feedback">意见反馈</router-link></li>
            <li><router-link to="/front/contact">联系我们</router-link></li>
          </ul>
        </div>
        <div class="sitemap-section">
          <h3>法律条款</h3>
          <ul>
            <li><router-link to="/front/privacy">隐私政策</router-link></li>
            <li><router-link to="/front/terms">服务条款</router-link></li>
          </ul>
        </div>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.simple-page { max-width: 1200px; margin: 0 auto; }
.page-header { text-align: center; padding: 40px 0; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 16px; color: white; margin-bottom: 32px; }
.page-header h1 { font-size: 36px; font-weight: 700; margin: 0 0 12px 0; }
.page-header p { font-size: 16px; opacity: 0.9; margin: 0; }
.sitemap-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 32px; }
.sitemap-section h3 { color: #667eea; margin-bottom: 16px; }
.sitemap-section ul { list-style: none; padding: 0; }
.sitemap-section li { margin: 12px 0; }
.sitemap-section a { color: #666; text-decoration: none; transition: color 0.3s; }
.sitemap-section a:hover { color: #667eea; }
</style>
