<template>
  <div class="team-page">
    <div class="page-header">
      <h1>组织架构</h1>
      <p>了解我们的团队结构和成员</p>
    </div>

    <div class="content-section">
      <el-card class="structure-card">
        <template #header>
          <div class="card-header">
            <el-icon><OfficeBuilding /></el-icon>
            <span>组织结构</span>
          </div>
        </template>
        <div class="org-chart">
          <div class="org-level">
            <div class="org-node president">
              <el-icon><User /></el-icon>
              <h4>社团主席</h4>
              <p>负责社团整体运营</p>
            </div>
          </div>
          
          <div class="org-level">
            <div class="org-node">
              <el-icon><UserFilled /></el-icon>
              <h4>副主席</h4>
              <p>协助主席工作</p>
            </div>
            <div class="org-node">
              <el-icon><UserFilled /></el-icon>
              <h4>秘书长</h4>
              <p>文档与会议管理</p>
            </div>
          </div>
          
          <div class="org-level departments">
            <div class="org-node">
              <el-icon><Calendar /></el-icon>
              <h4>活动部</h4>
              <p>策划组织活动</p>
            </div>
            <div class="org-node">
              <el-icon><Promotion /></el-icon>
              <h4>宣传部</h4>
              <p>品牌推广宣传</p>
            </div>
            <div class="org-node">
              <el-icon><Money /></el-icon>
              <h4>财务部</h4>
              <p>财务管理</p>
            </div>
            <div class="org-node">
              <el-icon><Connection /></el-icon>
              <h4>外联部</h4>
              <p>对外合作交流</p>
            </div>
          </div>
        </div>
      </el-card>

      <el-card class="members-card">
        <template #header>
          <div class="card-header">
            <el-icon><Avatar /></el-icon>
            <span>核心成员</span>
          </div>
        </template>
        <div class="members-grid">
          <div class="member-card" v-for="member in members" :key="member.id">
            <div class="member-avatar">
              <el-icon><User /></el-icon>
            </div>
            <h4>{{ member.name }}</h4>
            <p class="position">{{ member.position }}</p>
            <p class="intro">{{ member.intro }}</p>
            <div class="member-contact">
              <el-icon><Message /></el-icon>
              <el-icon><Phone /></el-icon>
            </div>
          </div>
        </div>
      </el-card>

      <el-card class="departments-card">
        <template #header>
          <div class="card-header">
            <el-icon><Grid /></el-icon>
            <span>部门职责</span>
          </div>
        </template>
        <el-collapse>
          <el-collapse-item v-for="dept in departments" :key="dept.id" :name="dept.id">
            <template #title>
              <div class="dept-title">
                <el-icon>
                  <component :is="dept.icon" />
                </el-icon>
                <span>{{ dept.name }}</span>
              </div>
            </template>
            <div class="dept-content">
              <p class="dept-desc">{{ dept.description }}</p>
              <h5>主要职责：</h5>
              <ul>
                <li v-for="(duty, index) in dept.duties" :key="index">{{ duty }}</li>
              </ul>
            </div>
          </el-collapse-item>
        </el-collapse>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { OfficeBuilding, User, UserFilled, Calendar, Promotion, Money, Connection, Avatar, Message, Phone, Grid } from '@element-plus/icons-vue'

const members = ref([
  { id: 1, name: '张三', position: '社团主席', intro: '负责社团整体运营和战略规划' },
  { id: 2, name: '李四', position: '副主席', intro: '协助主席处理日常事务' },
  { id: 3, name: '王五', position: '秘书长', intro: '负责文档管理和会议组织' },
  { id: 4, name: '赵六', position: '活动部部长', intro: '策划和组织各类社团活动' },
  { id: 5, name: '钱七', position: '宣传部部长', intro: '负责社团品牌建设和推广' },
  { id: 6, name: '孙八', position: '财务部部长', intro: '管理社团财务和预算' }
])

const departments = ref([
  {
    id: 1,
    name: '活动部',
    icon: 'Calendar',
    description: '负责社团各类活动的策划、组织和执行',
    duties: [
      '策划年度活动计划',
      '组织实施各类社团活动',
      '活动现场管理和协调',
      '活动效果评估和总结'
    ]
  },
  {
    id: 2,
    name: '宣传部',
    icon: 'Promotion',
    description: '负责社团品牌建设和对外宣传工作',
    duties: [
      '设计制作宣传物料',
      '管理社团新媒体平台',
      '撰写新闻稿和活动报道',
      '维护社团形象和声誉'
    ]
  },
  {
    id: 3,
    name: '财务部',
    icon: 'Money',
    description: '负责社团财务管理和预算控制',
    duties: [
      '编制年度财务预算',
      '管理社团收支账目',
      '审核报销申请',
      '定期财务报告'
    ]
  },
  {
    id: 4,
    name: '外联部',
    icon: 'Connection',
    description: '负责对外合作和资源拓展',
    duties: [
      '寻找合作伙伴和赞助商',
      '维护校友关系',
      '组织对外交流活动',
      '拓展社团资源'
    ]
  }
])
</script>

<style scoped>
.team-page {
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

.org-chart {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 32px;
  padding: 24px;
}

.org-level {
  display: flex;
  gap: 24px;
  justify-content: center;
  width: 100%;
}

.org-level.departments {
  flex-wrap: wrap;
}

.org-node {
  background: linear-gradient(135deg, #f5f7fa 0%, #e8eaf6 100%);
  padding: 24px;
  border-radius: 12px;
  text-align: center;
  min-width: 150px;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.org-node:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
  border-color: #667eea;
}

.org-node.president {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.org-node .el-icon {
  font-size: 32px;
  margin-bottom: 12px;
  color: #667eea;
}

.org-node.president .el-icon {
  color: white;
}

.org-node h4 {
  font-size: 16px;
  margin: 0 0 8px 0;
}

.org-node p {
  font-size: 14px;
  opacity: 0.8;
  margin: 0;
}

.members-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.member-card {
  background: #f8f9fa;
  padding: 24px;
  border-radius: 12px;
  text-align: center;
  transition: all 0.3s ease;
}

.member-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.2);
}

.member-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
}

.member-avatar .el-icon {
  font-size: 40px;
  color: white;
}

.member-card h4 {
  font-size: 18px;
  color: #333;
  margin: 0 0 8px 0;
}

.member-card .position {
  font-size: 14px;
  color: #667eea;
  font-weight: 600;
  margin: 0 0 12px 0;
}

.member-card .intro {
  font-size: 13px;
  color: #666;
  margin: 0 0 16px 0;
  line-height: 1.6;
}

.member-contact {
  display: flex;
  gap: 12px;
  justify-content: center;
}

.member-contact .el-icon {
  font-size: 20px;
  color: #667eea;
  cursor: pointer;
  transition: all 0.3s ease;
}

.member-contact .el-icon:hover {
  color: #764ba2;
  transform: scale(1.2);
}

.dept-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
}

.dept-title .el-icon {
  color: #667eea;
}

.dept-content {
  padding: 16px 0;
}

.dept-desc {
  color: #666;
  line-height: 1.6;
  margin-bottom: 16px;
}

.dept-content h5 {
  color: #333;
  margin: 0 0 12px 0;
}

.dept-content ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.dept-content li {
  padding: 8px 0;
  padding-left: 24px;
  position: relative;
  color: #666;
}

.dept-content li::before {
  content: '•';
  position: absolute;
  left: 8px;
  color: #667eea;
  font-weight: bold;
}

@media (max-width: 768px) {
  .members-grid {
    grid-template-columns: 1fr;
  }
  
  .org-level {
    flex-direction: column;
    align-items: center;
  }
  
  .page-header h1 {
    font-size: 28px;
  }
}
</style>
