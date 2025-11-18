# 前端项目说明

## 技术栈

- Vue 3.3.4
- Vite 4.5.0
- Element Plus 2.4.2
- Vue Router 4.2.5
- Pinia 2.1.7
- Axios 1.6.0

## 项目结构

```
src/
├── api/                    # API 接口
│   └── user.js            # 用户相关接口
├── assets/                # 静态资源
├── components/            # 公共组件
├── layout/                # 布局组件
│   └── MainLayout.vue     # 主布局
├── router/                # 路由配置
│   └── index.js
├── stores/                # 状态管理
│   └── user.js            # 用户状态
├── utils/                 # 工具函数
│   └── request.js         # Axios 封装
├── views/                 # 页面组件
│   ├── Home.vue           # 首页
│   ├── Login.vue          # 登录页
│   └── User.vue           # 用户管理
├── App.vue                # 根组件
└── main.js                # 入口文件
```

## 运行步骤

### 1. 安装依赖

```bash
npm install
```

### 2. 启动开发服务器

```bash
npm run dev
```

访问 `http://localhost:3000`

### 3. 构建生产版本

```bash
npm run build
```

## 功能说明

### 登录页面
- 用户登录
- 表单验证
- 默认账号提示

### 首页
- 数据统计卡片
- 系统功能介绍
- 技术栈展示

### 用户管理
- 用户列表展示
- 分页查询
- 条件搜索
- 新增用户
- 编辑用户
- 删除用户

## 路由说明

- `/login` - 登录页
- `/` - 主布局
  - `/home` - 首页
  - `/user` - 用户管理

## 状态管理

使用 Pinia 管理用户状态:
- `token` - 用户令牌
- `userInfo` - 用户信息
- `setToken` - 设置令牌
- `setUserInfo` - 设置用户信息
- `logout` - 退出登录

## API 请求

### 请求拦截器
- 自动添加 Authorization 头

### 响应拦截器
- 统一错误处理
- 401 自动跳转登录

## 开发建议

1. 组件使用 Composition API
2. 使用 `<script setup>` 语法
3. 合理使用 Element Plus 组件
4. 保持代码风格一致
5. 添加适当的注释

## 环境变量

可以创建 `.env.development` 和 `.env.production` 文件配置不同环境的变量

```
# .env.development
VITE_API_BASE_URL=http://localhost:8080
```

## 注意事项

1. 确保后端服务已启动
2. 检查代理配置是否正确
3. 登录后 token 存储在 localStorage
