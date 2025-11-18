<template>
  <div class="front-footer">
    <div class="footer-container">
      <div class="footer-content">
        <div class="footer-section">
          <h3>关于社团</h3>
          <ul>
            <li><router-link to="/front/about">社团简介</router-link></li>
            <li><router-link to="/front/team">组织架构</router-link></li>
            <li><router-link to="/front/history">发展历程</router-link></li>
            <li><router-link to="/front/join">加入我们</router-link></li>
          </ul>
        </div>
        
        <div class="footer-section">
          <h3>核心功能</h3>
          <ul>
            <li><router-link to="/front/community">社区交流</router-link></li>
            <li><router-link to="/front/activities">活动中心</router-link></li>
            <li><router-link to="/front/profile">个人中心</router-link></li>
            <li><router-link to="/front/chat">私信聊天</router-link></li>
          </ul>
        </div>
        
        <div class="footer-section">
          <h3>帮助中心</h3>
          <ul>
            <li><router-link to="/front/guide">新手指南</router-link></li>
            <li><router-link to="/front/faq">常见问题</router-link></li>
            <li><router-link to="/front/feedback">意见反馈</router-link></li>
            <li><router-link to="/front/contact">联系我们</router-link></li>
          </ul>
        </div>
        
        <div class="footer-section">
          <h3>关注我们</h3>
          <div class="social-links">
            <el-tooltip content="微信公众号" placement="top">
              <div class="social-icon">
                <el-icon :size="24"><ChatDotRound /></el-icon>
              </div>
            </el-tooltip>
            <el-tooltip content="QQ群" placement="top">
              <div class="social-icon">
                <el-icon :size="24"><ChatLineRound /></el-icon>
              </div>
            </el-tooltip>
            <el-tooltip content="官方网站" placement="top">
              <div class="social-icon">
                <el-icon :size="24"><Link /></el-icon>
              </div>
            </el-tooltip>
            <el-tooltip content="邮箱" placement="top">
              <div class="social-icon">
                <el-icon :size="24"><Message /></el-icon>
              </div>
            </el-tooltip>
          </div>
          <div class="contact-info">
            <p><el-icon><Phone /></el-icon> 联系电话：010-12345678</p>
            <p><el-icon><Message /></el-icon> club@university.edu.cn</p>
            <p><el-icon><Location /></el-icon> 学生活动中心3楼</p>
          </div>
        </div>
      </div>
      
      <div class="footer-bottom">
        <div class="copyright">
          <p>© 2025 高校社团活动综合管理系统. All rights reserved.</p>
          <p class="icp">
            <router-link to="/front/privacy">隐私政策</router-link> | 
            <router-link to="/front/terms">服务条款</router-link> | 
            <router-link to="/front/sitemap">网站地图</router-link>
          </p>
        </div>
        <div class="footer-links">
          <span>Powered by Vue 3 + Spring Boot + Element Plus</span>
          <span class="divider">|</span>
          <span>Version 1.0.0</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ChatDotRound, ChatLineRound, Link, Message, Phone, Location } from '@element-plus/icons-vue'
</script>

<style scoped>
.front-footer {
  background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
  color: #ecf0f1;
  margin-top: 60px;
}

.footer-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 60px 24px 30px;
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 40px;
  margin-bottom: 40px;
}

.footer-section h3 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 20px;
  color: #fff;
  position: relative;
  padding-bottom: 12px;
}

.footer-section h3::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 40px;
  height: 3px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 2px;
}

.footer-section ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-section ul li {
  margin-bottom: 12px;
}

.footer-section ul li a {
  color: #bdc3c7;
  text-decoration: none;
  font-size: 14px;
  transition: all 0.3s ease;
  display: inline-block;
}

.footer-section ul li a:hover {
  color: #fff;
  transform: translateX(5px);
}

.social-links {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
}

.social-icon {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.social-icon:hover {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}

.social-icon .el-icon {
  color: #fff;
}

.contact-info {
  margin-top: 20px;
}

.contact-info p {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 8px 0;
  font-size: 14px;
  color: #bdc3c7;
}

.contact-info .el-icon {
  color: #667eea;
}

.footer-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 30px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.copyright p {
  margin: 4px 0;
  font-size: 14px;
  color: #95a5a6;
}

.copyright .icp {
  font-size: 12px;
}

.copyright .icp a {
  color: #95a5a6;
  text-decoration: none;
  margin: 0 8px;
  transition: color 0.3s ease;
}

.copyright .icp a:hover {
  color: #fff;
}

.footer-links {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 13px;
  color: #95a5a6;
}

.divider {
  color: rgba(255, 255, 255, 0.3);
}

/* 响应式 */
@media (max-width: 1024px) {
  .footer-content {
    grid-template-columns: repeat(2, 1fr);
    gap: 30px;
  }
}

@media (max-width: 768px) {
  .footer-container {
    padding: 40px 16px 20px;
  }
  
  .footer-content {
    grid-template-columns: 1fr;
    gap: 30px;
  }
  
  .footer-bottom {
    flex-direction: column;
    gap: 16px;
    text-align: center;
  }
}
</style>
