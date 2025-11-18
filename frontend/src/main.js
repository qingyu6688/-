import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import { permission, role } from './directives/permission'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'

const app = createApp(App)
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 注册权限指令
app.directive('permission', permission)
app.directive('role', role)

app.use(pinia)
app.use(router)
app.use(ElementPlus, { locale: zhCn })

app.mount('#app')
