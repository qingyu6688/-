<template>
  <div class="sidebar-container">
    <div class="logo-wrapper">
      <div class="logo">
        <div class="logo-icon">
          <el-icon :size="32"><TrendCharts /></el-icon>
        </div>
        <transition name="fade">
          <div v-if="!isCollapse" class="logo-content">
            <span class="logo-title">Admin Pro</span>
            <span class="logo-subtitle">管理系统</span>
          </div>
        </transition>
      </div>
    </div>
    <el-scrollbar class="menu-scrollbar">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :unique-opened="true"
        router
        :collapse-transition="false"
        class="sidebar-menu"
      >
        <sidebar-item
          v-for="route in menuRoutes"
          :key="route.path"
          :item="route"
          :base-path="'/admin'"
        />
      </el-menu>
    </el-scrollbar>
    <div class="sidebar-footer" v-if="!isCollapse">
      <div class="footer-content">
        <el-icon><InfoFilled /></el-icon>
        <span>v1.0.0</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import SidebarItem from './SidebarItem.vue'

defineProps({
  isCollapse: {
    type: Boolean,
    default: false
  }
})

const route = useRoute()
const router = useRouter()

const menuRoutes = computed(() => {
  return router.options.routes.find(r => r.path === '/admin')?.children || []
})

const activeMenu = computed(() => {
  const { path } = route
  return path
})
</script>

<style scoped>
.sidebar-container {
  height: 100%;
  background: linear-gradient(180deg, #1a2332 0%, #2c3e50 100%);
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.15);
  position: relative;
}

.sidebar-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(66, 165, 245, 0.3), transparent);
}

.logo-wrapper {
  padding: 20px 0;
  background: linear-gradient(135deg, #1e3a5f 0%, #2c3e50 100%);
  border-bottom: 1px solid rgba(66, 165, 245, 0.1);
  position: relative;
}

.logo-wrapper::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 60%;
  height: 2px;
  background: linear-gradient(90deg, transparent, #42a5f5, transparent);
  opacity: 0.5;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 15px;
  padding: 0 20px;
}

.logo-icon {
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, #42a5f5 0%, #2196f3 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  box-shadow: 0 4px 12px rgba(66, 165, 245, 0.4);
  transition: all 0.3s;
}

.logo-icon:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(66, 165, 245, 0.6);
}

.logo-content {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.logo-title {
  font-size: 20px;
  font-weight: 700;
  color: #fff;
  letter-spacing: 1px;
  background: linear-gradient(135deg, #fff 0%, #42a5f5 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.logo-subtitle {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.6);
  letter-spacing: 2px;
}

.menu-scrollbar {
  flex: 1;
  padding: 10px 0;
}

.sidebar-menu {
  border-right: none;
  background: transparent;
}

:deep(.el-menu) {
  background: transparent;
}

:deep(.el-menu-item) {
  margin: 4px 12px;
  border-radius: 8px;
  color: rgba(255, 255, 255, 0.7);
  transition: all 0.3s;
  position: relative;
  overflow: hidden;
}

:deep(.el-menu-item::before) {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  background: #42a5f5;
  transform: scaleY(0);
  transition: transform 0.3s;
}

:deep(.el-menu-item:hover) {
  background: rgba(66, 165, 245, 0.1) !important;
  color: #fff;
}

:deep(.el-menu-item.is-active) {
  background: linear-gradient(90deg, rgba(66, 165, 245, 0.2) 0%, rgba(66, 165, 245, 0.05) 100%) !important;
  color: #42a5f5 !important;
  font-weight: 600;
  box-shadow: 0 2px 8px rgba(66, 165, 245, 0.2);
}

:deep(.el-menu-item.is-active::before) {
  transform: scaleY(1);
}

:deep(.el-menu-item .el-icon) {
  font-size: 18px;
  margin-right: 10px;
}

:deep(.el-sub-menu__title) {
  margin: 4px 12px;
  border-radius: 8px;
  color: rgba(255, 255, 255, 0.7);
  transition: all 0.3s;
}

:deep(.el-sub-menu__title:hover) {
  background: rgba(66, 165, 245, 0.1) !important;
  color: #fff;
}

:deep(.el-sub-menu.is-active > .el-sub-menu__title) {
  color: #42a5f5 !important;
}

:deep(.el-sub-menu .el-menu) {
  background: rgba(0, 0, 0, 0.2);
}

:deep(.el-sub-menu .el-menu-item) {
  margin: 2px 8px 2px 20px;
  min-width: auto;
  padding-left: 40px !important;
}

:deep(.el-sub-menu .el-menu-item .el-icon) {
  font-size: 16px;
}

.sidebar-footer {
  padding: 15px 20px;
  border-top: 1px solid rgba(66, 165, 245, 0.1);
  background: rgba(0, 0, 0, 0.1);
}

.footer-content {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: rgba(255, 255, 255, 0.5);
  font-size: 12px;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
