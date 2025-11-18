<template>
  <div class="simple-page">
    <div class="page-header">
      <h1>意见反馈</h1>
      <p>您的建议是我们进步的动力</p>
    </div>
    <el-card class="content-card">
      <el-form :model="form" label-width="100px">
        <el-form-item label="反馈类型">
          <el-select v-model="form.type" placeholder="请选择反馈类型">
            <el-option label="功能建议" value="suggestion"></el-option>
            <el-option label="Bug反馈" value="bug"></el-option>
            <el-option label="其他" value="other"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="请输入标题"></el-input>
        </el-form-item>
        <el-form-item label="详细描述">
          <el-input type="textarea" v-model="form.content" :rows="6" placeholder="请详细描述您的问题或建议"></el-input>
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input v-model="form.contact" placeholder="方便我们与您联系"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submit">提交反馈</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const form = ref({ type: '', title: '', content: '', contact: '' })
const submit = () => ElMessage.success('感谢您的反馈！')
</script>

<style scoped>
.simple-page { max-width: 1200px; margin: 0 auto; }
.page-header { text-align: center; padding: 40px 0; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 16px; color: white; margin-bottom: 32px; }
.page-header h1 { font-size: 36px; font-weight: 700; margin: 0 0 12px 0; }
.page-header p { font-size: 16px; opacity: 0.9; margin: 0; }
</style>
