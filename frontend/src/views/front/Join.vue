<template>
  <div class="join-page">
    <div class="page-header">
      <h1>加入我们</h1>
      <p>成为社团的一员，开启精彩的大学生活</p>
    </div>

    <div class="content-section">
      <el-card class="join-card">
        <template #header>
          <div class="card-header">
            <el-icon><UserFilled /></el-icon>
            <span>招新信息</span>
          </div>
        </template>
        <div class="join-content">
          <h3>招新时间</h3>
          <p>每年9月开学季和3月春季学期开始时</p>
          
          <h3>招新对象</h3>
          <p>全校在校学生，不限年级、专业</p>
          
          <h3>加入条件</h3>
          <ul>
            <li>热爱社团活动，积极向上</li>
            <li>有团队协作精神</li>
            <li>能够保证参与社团活动的时间</li>
            <li>遵守社团章程和规定</li>
          </ul>
        </div>
      </el-card>

      <el-card class="process-card">
        <template #header>
          <div class="card-header">
            <el-icon><List /></el-icon>
            <span>加入流程</span>
          </div>
        </template>
        <el-steps direction="vertical" :active="4">
          <el-step title="填写报名表" description="在线填写个人信息和意向部门"></el-step>
          <el-step title="参加面试" description="与部门负责人进行简单交流"></el-step>
          <el-step title="试用期" description="参与社团活动，了解社团文化"></el-step>
          <el-step title="正式成员" description="通过考核，成为正式成员"></el-step>
        </el-steps>
      </el-card>

      <el-card class="form-card">
        <template #header>
          <div class="card-header">
            <el-icon><Edit /></el-icon>
            <span>在线报名</span>
          </div>
        </template>
        <el-form :model="form" label-width="100px">
          <el-form-item label="姓名">
            <el-input v-model="form.name" placeholder="请输入您的姓名"></el-input>
          </el-form-item>
          <el-form-item label="学号">
            <el-input v-model="form.studentNo" placeholder="请输入学号"></el-input>
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input v-model="form.phone" placeholder="请输入联系电话"></el-input>
          </el-form-item>
          <el-form-item label="意向部门">
            <el-select v-model="form.department" placeholder="请选择意向部门">
              <el-option label="活动部" value="activity"></el-option>
              <el-option label="宣传部" value="promotion"></el-option>
              <el-option label="财务部" value="finance"></el-option>
              <el-option label="外联部" value="external"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="个人简介">
            <el-input type="textarea" v-model="form.intro" :rows="4" placeholder="请简单介绍一下自己"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm">提交报名</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { UserFilled, List, Edit } from '@element-plus/icons-vue'

const form = ref({
  name: '',
  studentNo: '',
  phone: '',
  department: '',
  intro: ''
})

const submitForm = () => {
  ElMessage.success('报名成功！我们会尽快与您联系')
}

const resetForm = () => {
  form.value = {
    name: '',
    studentNo: '',
    phone: '',
    department: '',
    intro: ''
  }
}
</script>

<style scoped>
.join-page {
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

.join-content h3 {
  color: #333;
  margin: 20px 0 12px 0;
}

.join-content p {
  color: #666;
  line-height: 1.6;
}

.join-content ul {
  list-style: none;
  padding: 0;
}

.join-content li {
  padding: 8px 0;
  padding-left: 24px;
  position: relative;
  color: #666;
}

.join-content li::before {
  content: '•';
  position: absolute;
  left: 8px;
  color: #667eea;
  font-weight: bold;
}

@media (max-width: 768px) {
  .page-header h1 {
    font-size: 28px;
  }
}
</style>
