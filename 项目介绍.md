# 高校社团活动综合管理系统

## 📋 项目简介

高校社团活动综合管理系统是一个基于 Spring Boot + Vue 3 的全栈 Web 应用，旨在为高校社团提供一站式的活动管理、成员管理和社区交流平台。系统采用前后端分离架构，提供了完善的权限管理、活动管理、社区交流等核心功能。

## 🎯 核心功能

### 1. 用户管理
- **多角色权限体系**
  - 超级管理员：系统最高权限
  - 管理员：后台管理权限
  - 社团会长/副会长：社团管理权限
  - 部长/副部长：部门管理权限
  - 普通成员：基础功能权限

- **用户认证**
  - JWT Token 认证
  - 密码 MD5 加密
  - 登录日志记录
  - 操作日志记录

### 2. 成员信息管理
- **档案管理**
  - 基本信息（姓名、学号、学院、专业等）
  - 联系方式（手机、邮箱、微信、QQ）
  - 头像上传
  - 个人资料编辑

- **经历记录**
  - 社团经历
  - 职位变更记录
  - 参与活动记录

- **荣誉管理**
  - 获奖记录
  - 荣誉证书
  - 贡献统计

### 3. 活动管理
- **活动发布**
  - 活动信息编辑（名称、类型、时间、地点）
  - 封面图片上传
  - 活动类型（讲座、比赛、聚会、公益、培训、展览）
  - 人数限制设置
  - 报名截止时间

- **报名管理**
  - 在线报名
  - 报名审核
  - 报名信息查看
  - 取消报名

- **签到管理**
  - 二维码签到
  - 手动签到
  - 签到记录查询

- **活动评价**
  - 5星评分系统
  - 评价内容
  - 评价统计

- **活动相册**
  - 批量上传照片（最多50张）
  - 照片预览
  - 照片删除
  - 精选照片设置

### 4. 社区交流
- **论坛板块**
  - 学习交流
  - 生活分享
  - 活动讨论
  - 招聘求职

- **帖子管理**
  - 发布帖子
  - 富文本编辑
  - 图片上传
  - 帖子审核
  - 点赞收藏

- **评论互动**
  - 评论回复
  - 点赞功能
  - 评论管理

- **通知公告**
  - 系统通知
  - 活动通知
  - 紧急通知
  - 未读消息提醒

- **私信聊天**
  - 一对一私信
  - 消息记录
  - 未读消息提醒

### 5. 统计分析
- **数据概览**
  - 成员总数
  - 帖子总数
  - 活动总数
  - 评论总数

- **活动分析**
  - 活动类型分布
  - 活动状态统计
  - 参与率统计
  - 热门活动排行

- **成员分析**
  - 活跃成员统计
  - 成员增长趋势
  - 参与度分析

### 6. 系统管理
- **用户管理**
  - 用户列表
  - 用户编辑
  - 角色分配
  - 状态管理

- **角色管理**
  - 角色列表
  - 权限分配
  - 角色编辑

- **菜单管理**
  - 菜单树结构
  - 菜单编辑
  - 权限配置

- **日志管理**
  - 操作日志
  - 登录日志
  - 日志查询

- **文件管理**
  - 文件上传
  - 文件列表
  - 文件删除

## 🛠️ 技术栈

### 后端技术
- **框架**: Spring Boot 3.1.5
- **数据库**: MySQL 8.0
- **ORM**: MyBatis-Plus 3.5.3
- **安全**: JWT + MD5
- **文档**: Swagger 3.0
- **工具**: Lombok, Hutool

### 前端技术
- **框架**: Vue 3.3
- **UI组件**: Element Plus
- **路由**: Vue Router 4
- **状态管理**: Pinia
- **HTTP客户端**: Axios
- **构建工具**: Vite

## 📦 项目结构

```
SpringBoot+Vue/
├── backend/                    # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/admin/
│   │   │   │       ├── annotation/      # 自定义注解
│   │   │   │       ├── common/          # 公共类
│   │   │   │       ├── config/          # 配置类
│   │   │   │       ├── controller/      # 控制器
│   │   │   │       ├── dto/             # 数据传输对象
│   │   │   │       ├── entity/          # 实体类
│   │   │   │       ├── filter/          # 过滤器
│   │   │   │       ├── mapper/          # Mapper接口
│   │   │   │       ├── service/         # 服务层
│   │   │   │       ├── util/            # 工具类
│   │   │   │       └── vo/              # 视图对象
│   │   │   └── resources/
│   │   │       ├── mapper/              # MyBatis XML
│   │   │       ├── sql/                 # SQL脚本
│   │   │       └── application.yml      # 配置文件
│   │   └── test/                        # 测试代码
│   └── pom.xml                          # Maven配置
│
└── frontend/                   # 前端项目
    ├── public/                 # 静态资源
    ├── src/
    │   ├── api/               # API接口
    │   ├── assets/            # 资源文件
    │   ├── components/        # 公共组件
    │   ├── layout/            # 布局组件
    │   ├── router/            # 路由配置
    │   ├── stores/            # 状态管理
    │   ├── utils/             # 工具函数
    │   ├── views/             # 页面组件
    │   │   ├── admin/         # 后台管理页面
    │   │   └── front/         # 前台用户页面
    │   ├── App.vue            # 根组件
    │   └── main.js            # 入口文件
    ├── package.json           # 依赖配置
    └── vite.config.js         # Vite配置
```

## 🚀 快速开始

### 环境要求
- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Maven 3.6+

### 后端启动

1. **创建数据库**
```sql
CREATE DATABASE admin_system CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

2. **导入数据**
```bash
# 导入数据库脚本
mysql -u root -p admin_system < backend/src/main/resources/sql/admin_system.sql
```

3. **修改配置**
编辑 `backend/src/main/resources/application.yml`
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/admin_system
    username: root
    password: your_password
```

4. **启动后端**
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动

### 前端启动

1. **安装依赖**
```bash
cd frontend
npm install
```

2. **启动开发服务器**
```bash
npm run dev
```

前端服务将在 `http://localhost:5173` 启动

3. **构建生产版本**
```bash
npm run build
```

## 👤 默认账号

### 超级管理员
- 账号: `admin`
- 密码: `123456`

### 普通管理员
- 账号: `manager`
- 密码: `123456`

### 普通用户
- 账号: `user`
- 密码: `123456`

## 📱 功能模块访问

### 后台管理系统
访问地址: `http://localhost:5173/admin`

**主要功能:**
- 系统管理（用户、角色、菜单）
- 成员管理（档案、经历、荣誉）
- 互动交流（帖子、评论、板块、通知）
- 活动管理（活动、报名、签到、评价、相册）
- 统计分析（数据概览、活动分析、成员分析）
- 系统监控（操作日志、登录日志）

### 前台用户系统
访问地址: `http://localhost:5173/front`

**主要功能:**
- 首页（活动展示、热门帖子）
- 社区交流（发帖、评论、点赞）
- 活动中心（浏览活动、报名、签到、评价）
- 个人中心（资料编辑、我的活动、我的帖子）
- 私信聊天（一对一交流）
- 消息通知（系统通知、活动通知）

## 🎨 界面特色

### 后台管理
- 简洁的侧边栏导航
- 面包屑导航
- 数据统计卡片
- 表格数据展示
- 表单编辑对话框

### 前台用户
- 渐变色主题设计
- 卡片式布局
- 响应式设计
- 动画过渡效果
- 现代化UI组件

## 📊 数据库设计

### 核心表
- `sys_user` - 用户表
- `sys_role` - 角色表
- `sys_menu` - 菜单表
- `sys_role_menu` - 角色菜单关联表
- `activity_info` - 活动信息表
- `activity_registration` - 活动报名表
- `activity_sign_in` - 活动签到表
- `activity_evaluation` - 活动评价表
- `activity_photo` - 活动相册表
- `community_post` - 社区帖子表
- `community_comment` - 评论表
- `community_notice` - 通知公告表
- `alumni_contact_extend` - 成员档案表
- `sys_operation_log` - 操作日志表
- `sys_login_log` - 登录日志表

## 🔐 安全特性

- JWT Token 认证
- 密码 MD5 加密
- XSS 过滤
- CORS 跨域配置
- 权限拦截器
- 操作日志记录
- 登录日志记录

## 📝 API文档

启动后端后访问 Swagger 文档:
```
http://localhost:8080/swagger-ui/index.html
```

## 🐛 常见问题

### 1. 后端启动失败
- 检查 MySQL 是否启动
- 检查数据库配置是否正确
- 检查端口 8080 是否被占用

### 2. 前端启动失败
- 删除 `node_modules` 重新安装
- 检查 Node.js 版本
- 检查端口 5173 是否被占用

### 3. 登录失败
- 检查用户名密码是否正确
- 检查后端是否正常运行
- 查看浏览器控制台错误信息

### 4. 图片上传失败
- 检查 `uploads` 目录是否有写入权限
- 检查文件大小是否超过限制
- 检查文件格式是否支持

## 📄 开源协议

本项目采用 MIT 协议开源

## 👨‍💻 开发团队

- 项目开发：[Your Name]
- 技术支持：[Support Email]

## 📮 联系方式

- Email: club@university.edu.cn
- QQ群: 123456789
- 地址: 学生活动中心3楼

---

**版本**: v1.0.0  
**最后更新**: 2025-11-19
