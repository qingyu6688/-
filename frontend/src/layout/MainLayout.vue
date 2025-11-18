<template>
  <el-container class="layout-container">
    <el-aside :width="isCollapse ? '64px' : '200px'" class="sidebar-wrapper">
      <Sidebar :is-collapse="isCollapse" />
    </el-aside>
    
    <el-container class="main-container">
      <el-header class="header-wrapper" height="auto">
        <Navbar :is-collapse="isCollapse" @toggle-sidebar="toggleSidebar" />
        <TagsView />
      </el-header>
      
      <el-main class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref } from 'vue'
import Sidebar from '@/components/Sidebar.vue'
import Navbar from '@/components/Navbar.vue'
import TagsView from '@/components/TagsView.vue'

const isCollapse = ref(false)

const toggleSidebar = () => {
  isCollapse.value = !isCollapse.value
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
  width: 100%;
}

.sidebar-wrapper {
  background-color: #2c3e50;
  overflow: hidden;
  transition: width 0.3s;
}

.main-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  overflow: hidden;
}

.header-wrapper {
  padding: 0;
  height: auto !important;
}

.main-content {
  background-color: #f0f2f5;
  padding: 20px;
  overflow-y: auto;
  flex: 1;
}

/* 页面切换动画 */
.fade-transform-leave-active,
.fade-transform-enter-active {
  transition: all 0.3s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>
