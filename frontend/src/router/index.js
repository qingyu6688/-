import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/admin',
    component: () => import('@/layout/MainLayout.vue'),
    redirect: '/admin/home',
    meta: { requiresAuth: true, role: 'admin' },
    children: [
      {
        path: 'home',
        name: 'AdminHome',
        component: () => import('@/views/Home.vue'),
        meta: { title: '首页', icon: 'HomeFilled', role: 'admin' }
      },
      {
        path: 'user',
        name: 'UserManage',
        component: () => import('@/views/system/User.vue'),
        meta: { title: '用户管理', icon: 'User', role: 'admin' }
      },
      {
        path: 'role',
        name: 'RoleManage',
        component: () => import('@/views/system/Role.vue'),
        meta: { title: '角色管理', icon: 'UserFilled', role: 'admin' }
      },
      {
        path: 'menu',
        name: 'MenuManage',
        component: () => import('@/views/system/Menu.vue'),
        meta: { title: '菜单管理', icon: 'Menu', role: 'admin' }
      },
      {
        path: 'operation-log',
        name: 'OperationLog',
        component: () => import('@/views/monitor/OperationLog.vue'),
        meta: { title: '操作日志', icon: 'Document', role: 'admin' }
      },
      {
        path: 'login-log',
        name: 'LoginLog',
        component: () => import('@/views/monitor/LoginLog.vue'),
        meta: { title: '登录日志', icon: 'Key', role: 'admin' }
      },
      {
        path: 'dict-type',
        name: 'DictType',
        component: () => import('@/views/system/DictType.vue'),
        meta: { title: '字典管理', icon: 'Collection', role: 'admin' }
      },
      {
        path: 'server',
        name: 'ServerMonitor',
        component: () => import('@/views/monitor/Server.vue'),
        meta: { title: '服务器监控', icon: 'Monitor', role: 'admin' }
      },
      {
        path: 'file-manage',
        name: 'FileManage',
        component: () => import('@/views/system/FileManage.vue'),
        meta: { title: '文件管理', icon: 'FolderOpened', role: 'admin' }
      },
      {
        path: 'profile',
        name: 'AdminProfile',
        component: () => import('@/views/system/Profile.vue'),
        meta: { title: '个人中心', icon: 'User' }
      },
      // 校友管理模块
      {
        path: 'alumni',
        name: 'Alumni',
        component: () => import('@/layout/ParentView.vue'),
        meta: { title: '校友管理', icon: 'UserFilled', role: 'admin' },
        children: [
          {
            path: 'profile',
            name: 'AlumniProfile',
            component: () => import('@/views/alumni/Profile.vue'),
            meta: { title: '校友档案', icon: 'User', role: 'admin' }
          },
          {
            path: 'history',
            name: 'AlumniHistory',
            component: () => import('@/views/alumni/History.vue'),
            meta: { title: '社团经历', icon: 'Tickets', role: 'admin' }
          },
          {
            path: 'honor',
            name: 'AlumniHonor',
            component: () => import('@/views/alumni/Honor.vue'),
            meta: { title: '荣誉记录', icon: 'Medal', role: 'admin' }
          }
        ]
      },
      // 互动交流模块
      {
        path: 'community',
        name: 'CommunityManage',
        component: () => import('@/layout/ParentView.vue'),
        meta: { title: '互动交流', icon: 'ChatDotRound', role: 'admin' },
        children: [
          {
            path: 'post',
            name: 'CommunityPost',
            component: () => import('@/views/community/Post.vue'),
            meta: { title: '帖子管理', icon: 'Document', role: 'admin' }
          },
          {
            path: 'comment',
            name: 'CommunityComment',
            component: () => import('@/views/community/Comment.vue'),
            meta: { title: '评论管理', icon: 'ChatLineRound', role: 'admin' }
          },
          {
            path: 'category',
            name: 'CommunityCategory',
            component: () => import('@/views/community/Category.vue'),
            meta: { title: '板块管理', icon: 'Grid', role: 'admin' }
          },
          {
            path: 'notice',
            name: 'CommunityNotice',
            component: () => import('@/views/community/Notice.vue'),
            meta: { title: '通知管理', icon: 'Bell', role: 'admin' }
          },
          {
            path: 'chat',
            name: 'CommunityChat',
            component: () => import('@/views/community/Chat.vue'),
            meta: { title: '聊天管理', icon: 'ChatDotSquare', role: 'admin' }
          }
        ]
      },
      // 活动管理模块
      {
        path: 'activity',
        name: 'ActivityManage',
        component: () => import('@/layout/ParentView.vue'),
        meta: { title: '活动管理', icon: 'Calendar', role: 'admin' },
        children: [
          {
            path: 'info',
            name: 'ActivityInfo',
            component: () => import('@/views/activity/ActivityInfo.vue'),
            meta: { title: '活动信息', icon: 'Document', role: 'admin' }
          },
          {
            path: 'registration',
            name: 'ActivityRegistration',
            component: () => import('@/views/activity/Registration.vue'),
            meta: { title: '报名管理', icon: 'Tickets', role: 'admin' }
          },
          {
            path: 'signin',
            name: 'ActivitySignIn',
            component: () => import('@/views/activity/SignIn.vue'),
            meta: { title: '签到管理', icon: 'CircleCheck', role: 'admin' }
          },
          {
            path: 'evaluation',
            name: 'ActivityEvaluation',
            component: () => import('@/views/activity/Evaluation.vue'),
            meta: { title: '评价管理', icon: 'Star', role: 'admin' }
          }
        ]
      },
    ]
  },
  {
    path: '/front',
    component: () => import('@/layout/FrontLayout.vue'),
    redirect: '/front/home',
    meta: { requiresAuth: true, role: 'user' },
    children: [
      {
        path: 'home',
        name: 'FrontHome',
        component: () => import('@/views/front/FrontHome.vue'),
        meta: { title: '首页', role: 'user' }
      },
      {
        path: 'community',
        name: 'Community',
        component: () => import('@/views/front/Community.vue'),
        meta: { title: '社区交流', role: 'user' }
      },
      {
        path: 'post/:id',
        name: 'PostDetail',
        component: () => import('@/views/front/PostDetail.vue'),
        meta: { title: '帖子详情', role: 'user' }
      },
      {
        path: 'activities',
        name: 'Activities',
        component: () => import('@/views/front/Activities.vue'),
        meta: { title: '活动中心', role: 'user' }
      },
      {
        path: 'profile',
        name: 'FrontProfile',
        component: () => import('@/views/front/Profile.vue'),
        meta: { title: '个人中心', role: 'user' }
      },
      {
        path: 'chat',
        name: 'Chat',
        component: () => import('@/views/front/Chat.vue'),
        meta: { title: '私信', role: 'user' }
      },
      {
        path: 'message',
        name: 'Message',
        component: () => import('@/views/front/Message.vue'),
        meta: { title: '消息通知', role: 'user' }
      },
      // 关于社团
      {
        path: 'about',
        name: 'About',
        component: () => import('@/views/front/About.vue'),
        meta: { title: '社团简介', role: 'user' }
      },
      {
        path: 'team',
        name: 'Team',
        component: () => import('@/views/front/Team.vue'),
        meta: { title: '组织架构', role: 'user' }
      },
      {
        path: 'history',
        name: 'History',
        component: () => import('@/views/front/History.vue'),
        meta: { title: '发展历程', role: 'user' }
      },
      {
        path: 'join',
        name: 'Join',
        component: () => import('@/views/front/Join.vue'),
        meta: { title: '加入我们', role: 'user' }
      },
      // 帮助中心
      {
        path: 'guide',
        name: 'Guide',
        component: () => import('@/views/front/Guide.vue'),
        meta: { title: '新手指南', role: 'user' }
      },
      {
        path: 'faq',
        name: 'FAQ',
        component: () => import('@/views/front/FAQ.vue'),
        meta: { title: '常见问题', role: 'user' }
      },
      {
        path: 'feedback',
        name: 'Feedback',
        component: () => import('@/views/front/Feedback.vue'),
        meta: { title: '意见反馈', role: 'user' }
      },
      {
        path: 'contact',
        name: 'Contact',
        component: () => import('@/views/front/Contact.vue'),
        meta: { title: '联系我们', role: 'user' }
      },
      // 法律条款
      {
        path: 'privacy',
        name: 'Privacy',
        component: () => import('@/views/front/Privacy.vue'),
        meta: { title: '隐私政策', role: 'user' }
      },
      {
        path: 'terms',
        name: 'Terms',
        component: () => import('@/views/front/Terms.vue'),
        meta: { title: '服务条款', role: 'user' }
      },
      {
        path: 'sitemap',
        name: 'Sitemap',
        component: () => import('@/views/front/Sitemap.vue'),
        meta: { title: '网站地图', role: 'user' }
      }
    ]
  },
  {
    path: '/',
    redirect: '/login'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    // 如果有保存的位置（浏览器前进/后退），使用保存的位置
    if (savedPosition) {
      return savedPosition
    }
    // 如果有锚点，滚动到锚点位置
    if (to.hash) {
      return {
        el: to.hash,
        behavior: 'smooth'
      }
    }
    // 默认滚动到页面顶部
    return { top: 0, behavior: 'smooth' }
  }
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  console.log('路由守卫 - 目标路径:', to.path)
  console.log('路由守卫 - 用户token:', userStore.token ? '已登录' : '未登录')
  console.log('路由守卫 - 用户信息:', userStore.userInfo)
  console.log('路由守卫 - 用户角色:', userStore.userInfo?.roleKey)
  
  // 如果访问登录页且已登录，根据角色跳转
  if (to.path === '/login' && userStore.token) {
    const roleKey = userStore.userInfo.roleKey
    console.log('已登录访问登录页，角色:', roleKey)
    if (roleKey === 'super_admin' || roleKey === 'admin') {
      console.log('重定向到后台')
      next('/admin/home')
    } else {
      console.log('重定向到前台')
      next('/front/home')
    }
    return
  }
  
  // 如果需要认证但未登录，跳转登录页
  if (to.meta.requiresAuth && !userStore.token) {
    console.log('需要认证但未登录，跳转登录页')
    next('/login')
    return
  }
  
  // 如果已登录，检查角色权限
  if (userStore.token && to.meta.role) {
    const userRole = userStore.userInfo.roleKey
    const isAdmin = userRole === 'super_admin' || userRole === 'admin'
    
    console.log('检查角色权限 - 用户角色:', userRole, '是否管理员:', isAdmin)
    console.log('检查角色权限 - 页面要求角色:', to.meta.role)
    
    // 后台页面只允许管理员访问
    if (to.meta.role === 'admin' && !isAdmin) {
      console.log('非管理员访问后台，重定向到前台')
      next('/front/home')
      return
    }
    // 管理员可以访问前台页面（允许管理员查看用户前台）
    // 只有在从登录页自动跳转时才重定向到后台
    if (to.meta.role === 'user' && isAdmin && from.path === '/login') {
      console.log('管理员登录后默认跳转到后台')
      next('/admin/home')
      return
    }
  }
  
  console.log('路由守卫 - 允许访问')
  next()
})

export default router
