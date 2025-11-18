<template>
  <div v-if="!item.meta?.hidden">
    <!-- 单个菜单项 -->
    <el-menu-item v-if="!item.children || item.children.length === 0" :index="resolvePath(item.path)">
      <el-icon v-if="item.meta?.icon">
        <component :is="item.meta.icon" />
      </el-icon>
      <template #title>
        <span>{{ item.meta?.title }}</span>
      </template>
    </el-menu-item>

    <!-- 有子菜单 -->
    <el-sub-menu v-else :index="resolvePath(item.path)">
      <template #title>
        <el-icon v-if="item.meta?.icon">
          <component :is="item.meta.icon" />
        </el-icon>
        <span>{{ item.meta?.title }}</span>
      </template>
      <sidebar-item
        v-for="child in item.children"
        :key="child.path"
        :item="child"
        :base-path="resolvePath(item.path)"
      />
    </el-sub-menu>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  item: {
    type: Object,
    required: true
  },
  basePath: {
    type: String,
    default: ''
  }
})

const resolvePath = (routePath) => {
  if (routePath.startsWith('/')) {
    return routePath
  }
  return `${props.basePath}/${routePath}`.replace(/\/+/g, '/')
}
</script>
