<template>
  <div class="front-layout">
    <FrontNavbar />
    
    <el-main class="front-main">
      <router-view />
    </el-main>
    
    <FrontFooter />
  </div>
</template>

<script setup>
import FrontNavbar from '@/components/FrontNavbar.vue'
import FrontFooter from '@/components/FrontFooter.vue'
</script>

<style scoped>
.front-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #f5f7fa 0%, #e8eaf6 100%);
}

.front-main {
  flex: 1;
  max-width: 1400px;
  width: 100%;
  margin: 0 auto;
  padding: 24px;
}
</style>
