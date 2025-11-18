<template>
  <div class="tags-view-container">
    <el-scrollbar class="tags-view-wrapper">
      <div class="tags-view-content">
        <router-link
          v-for="tag in visitedViews"
          :key="tag.path"
          :to="{ path: tag.path }"
          class="tags-view-item"
          :class="{ active: isActive(tag) }"
        >
          <span>{{ tag.title }}</span>
          <el-icon
            v-if="!isAffix(tag)"
            class="close-icon"
            :size="12"
            @click.prevent.stop="closeTag(tag)"
          >
            <Close />
          </el-icon>
        </router-link>
      </div>
    </el-scrollbar>
    <div class="tags-view-actions">
      <el-dropdown @command="handleCommand">
        <el-icon :size="18" class="action-icon">
          <ArrowDown />
        </el-icon>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="refresh">
              <el-icon><Refresh /></el-icon>
              刷新
            </el-dropdown-item>
            <el-dropdown-item command="close">
              <el-icon><Close /></el-icon>
              关闭当前
            </el-dropdown-item>
            <el-dropdown-item command="closeOthers">
              <el-icon><CircleClose /></el-icon>
              关闭其他
            </el-dropdown-item>
            <el-dropdown-item command="closeAll">
              <el-icon><Delete /></el-icon>
              关闭所有
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const visitedViews = ref([
  {
    path: '/admin/home',
    title: '首页',
    affix: true
  }
])

const isActive = (tag) => {
  return tag.path === route.path
}

const isAffix = (tag) => {
  return tag.affix
}

const addTag = () => {
  const { path, meta } = route
  if (meta?.title) {
    const existingTag = visitedViews.value.find(v => v.path === path)
    if (!existingTag) {
      visitedViews.value.push({
        path,
        title: meta.title,
        affix: false
      })
    }
  }
}

const closeTag = (tag) => {
  const index = visitedViews.value.findIndex(v => v.path === tag.path)
  if (index > -1) {
    visitedViews.value.splice(index, 1)
    if (isActive(tag)) {
      const latestView = visitedViews.value[visitedViews.value.length - 1]
      if (latestView) {
        router.push(latestView.path)
      }
    }
  }
}

const handleCommand = (command) => {
  switch (command) {
    case 'refresh':
      window.location.reload()
      break
    case 'close':
      const currentTag = visitedViews.value.find(v => v.path === route.path)
      if (currentTag && !currentTag.affix) {
        closeTag(currentTag)
      }
      break
    case 'closeOthers':
      visitedViews.value = visitedViews.value.filter(
        v => v.affix || v.path === route.path
      )
      break
    case 'closeAll':
      visitedViews.value = visitedViews.value.filter(v => v.affix)
      router.push('/admin/home')
      break
  }
}

watch(route, () => {
  addTag()
})

onMounted(() => {
  addTag()
})
</script>

<style scoped>
.tags-view-container {
  display: flex;
  align-items: center;
  height: 40px;
  background-color: #fff;
  border-bottom: 1px solid #e8eaec;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.tags-view-wrapper {
  flex: 1;
  height: 100%;
}

.tags-view-content {
  display: flex;
  align-items: center;
  padding: 0 10px;
  height: 100%;
}

.tags-view-item {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 5px 12px;
  margin-right: 6px;
  font-size: 13px;
  color: #495060;
  background-color: #f5f7fa;
  border: 1px solid #e8eaec;
  border-radius: 3px;
  text-decoration: none;
  transition: all 0.3s;
  cursor: pointer;
  white-space: nowrap;
}

.tags-view-item:hover {
  background-color: #ecf5ff;
  border-color: #b3d8ff;
  color: #42a5f5;
}

.tags-view-item.active {
  background-color: #42a5f5;
  border-color: #42a5f5;
  color: #fff;
}

.tags-view-item.active .close-icon {
  color: #fff;
}

.close-icon {
  color: #909399;
  transition: color 0.3s;
}

.close-icon:hover {
  color: #f56c6c;
  background-color: rgba(0, 0, 0, 0.1);
  border-radius: 50%;
}

.tags-view-actions {
  padding: 0 15px;
  border-left: 1px solid #e8eaec;
}

.action-icon {
  cursor: pointer;
  color: #606266;
  transition: color 0.3s;
}

.action-icon:hover {
  color: #42a5f5;
}
</style>
