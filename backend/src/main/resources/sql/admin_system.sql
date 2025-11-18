/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50711 (5.7.11)
 Source Host           : localhost:3306
 Source Schema         : admin_system

 Target Server Type    : MySQL
 Target Server Version : 50711 (5.7.11)
 File Encoding         : 65001

 Date: 17/11/2025 18:45:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for activity_evaluation
-- ----------------------------
DROP TABLE IF EXISTS `activity_evaluation`;
CREATE TABLE `activity_evaluation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `activity_id` bigint(20) NOT NULL COMMENT '活动ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `rating` tinyint(1) NOT NULL COMMENT '评分(1-5星)',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '评价内容',
  `images` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '评价图片(JSON数组)',
  `tags` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评价标签(JSON数组)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_activity_user`(`activity_id`, `user_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_rating`(`rating`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '活动评价表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of activity_evaluation
-- ----------------------------

-- ----------------------------
-- Table structure for activity_info
-- ----------------------------
DROP TABLE IF EXISTS `activity_info`;
CREATE TABLE `activity_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  `activity_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '活动名称',
  `activity_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '活动类型(lecture讲座/competition比赛/party聚会/volunteer公益/training培训/exhibition展览)',
  `cover_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面图片',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '活动描述',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动地点',
  `max_participants` int(11) NULL DEFAULT 0 COMMENT '最大参与人数(0表示不限)',
  `current_participants` int(11) NULL DEFAULT 0 COMMENT '当前参与人数',
  `registration_deadline` datetime NULL DEFAULT NULL COMMENT '报名截止时间',
  `fee` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '活动费用',
  `organizer_id` bigint(20) NOT NULL COMMENT '组织者ID',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '活动状态(0报名中 1进行中 2已结束 3已取消)',
  `audit_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '审核状态(0待审核 1已通过 2未通过)',
  `need_audit` tinyint(1) NULL DEFAULT 0 COMMENT '是否需要审核报名(0否 1是)',
  `tags` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动标签(JSON数组)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_activity_type`(`activity_type`) USING BTREE,
  INDEX `idx_organizer_id`(`organizer_id`) USING BTREE,
  INDEX `idx_start_time`(`start_time`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '活动信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of activity_info
-- ----------------------------

-- ----------------------------
-- Table structure for activity_photo
-- ----------------------------
DROP TABLE IF EXISTS `activity_photo`;
CREATE TABLE `activity_photo`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '照片ID',
  `activity_id` bigint(20) NOT NULL COMMENT '活动ID',
  `uploader_id` bigint(20) NOT NULL COMMENT '上传者ID',
  `photo_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '照片地址',
  `photo_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'image' COMMENT '类型(image图片/video视频)',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `like_count` int(11) NULL DEFAULT 0 COMMENT '点赞数',
  `is_featured` tinyint(1) NULL DEFAULT 0 COMMENT '是否精选(0否 1是)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_activity_id`(`activity_id`) USING BTREE,
  INDEX `idx_uploader_id`(`uploader_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '活动相册表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of activity_photo
-- ----------------------------

-- ----------------------------
-- Table structure for activity_registration
-- ----------------------------
DROP TABLE IF EXISTS `activity_registration`;
CREATE TABLE `activity_registration`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '报名ID',
  `activity_id` bigint(20) NOT NULL COMMENT '活动ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '真实姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '联系电话',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `registration_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '报名类型(0正式 1候补)',
  `audit_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '审核状态(0待审核 1已通过 2未通过)',
  `payment_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '支付状态(0未支付 1已支付 2已退款)',
  `cancel_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '取消原因',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_activity_user`(`activity_id`, `user_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_audit_status`(`audit_status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '活动报名表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of activity_registration
-- ----------------------------

-- ----------------------------
-- Table structure for activity_sign_in
-- ----------------------------
DROP TABLE IF EXISTS `activity_sign_in`;
CREATE TABLE `activity_sign_in`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '签到ID',
  `activity_id` bigint(20) NOT NULL COMMENT '活动ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `sign_in_time` datetime NOT NULL COMMENT '签到时间',
  `sign_in_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'qrcode' COMMENT '签到方式(qrcode二维码/manual手动)',
  `is_late` tinyint(1) NULL DEFAULT 0 COMMENT '是否迟到(0否 1是)',
  `is_early_leave` tinyint(1) NULL DEFAULT 0 COMMENT '是否早退(0否 1是)',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_activity_user`(`activity_id`, `user_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '活动签到表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of activity_sign_in
-- ----------------------------

-- ----------------------------
-- Table structure for alumni_club_history
-- ----------------------------
DROP TABLE IF EXISTS `alumni_club_history`;
CREATE TABLE `alumni_club_history`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '经历ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `club_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '社团名称',
  `position` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '担任职务',
  `start_date` date NOT NULL COMMENT '开始日期',
  `end_date` date NULL DEFAULT NULL COMMENT '结束日期',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '经历描述',
  `is_current` tinyint(1) NULL DEFAULT 0 COMMENT '是否当前职务(0否 1是)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_club_name`(`club_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '社团经历表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of alumni_club_history
-- ----------------------------

-- ----------------------------
-- Table structure for alumni_contact_extend
-- ----------------------------
DROP TABLE IF EXISTS `alumni_contact_extend`;
CREATE TABLE `alumni_contact_extend`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '联系方式扩展ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `company` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工作单位',
  `position` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '职位',
  `industry` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属行业',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '现居住地址',
  `emergency_contact` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '紧急联系人',
  `emergency_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '紧急联系电话',
  `privacy_level` tinyint(1) NULL DEFAULT 1 COMMENT '隐私级别(0公开 1仅好友 2私密)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '校友联系方式扩展表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of alumni_contact_extend
-- ----------------------------

-- ----------------------------
-- Table structure for alumni_contribution
-- ----------------------------
DROP TABLE IF EXISTS `alumni_contribution`;
CREATE TABLE `alumni_contribution`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `contribution_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '贡献类型(activity活动/post发帖/volunteer志愿/comment评论)',
  `contribution_value` int(11) NULL DEFAULT 0 COMMENT '贡献值',
  `related_id` bigint(20) NULL DEFAULT NULL COMMENT '关联ID',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_contribution_type`(`contribution_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '贡献度记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of alumni_contribution
-- ----------------------------

-- ----------------------------
-- Table structure for alumni_honor
-- ----------------------------
DROP TABLE IF EXISTS `alumni_honor`;
CREATE TABLE `alumni_honor`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '荣誉ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `honor_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '荣誉名称',
  `honor_level` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '荣誉级别(校级/市级/省级/国家级)',
  `award_date` date NOT NULL COMMENT '获奖日期',
  `issuer` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '颁发单位',
  `certificate_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '证书图片',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '荣誉描述',
  `audit_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '审核状态(0待审核 1已通过 2未通过)',
  `auditor` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核人',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_audit_status`(`audit_status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '荣誉记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of alumni_honor
-- ----------------------------

-- ----------------------------
-- Table structure for community_collect
-- ----------------------------
DROP TABLE IF EXISTS `community_collect`;
CREATE TABLE `community_collect`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `target_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '目标类型(post帖子/activity活动)',
  `target_id` bigint(20) NOT NULL COMMENT '目标ID',
  `user_id` bigint(20) NOT NULL COMMENT '收藏人ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_target_user`(`target_type`, `target_id`, `user_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '收藏记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of community_collect
-- ----------------------------

-- ----------------------------
-- Table structure for community_comment
-- ----------------------------
DROP TABLE IF EXISTS `community_comment`;
CREATE TABLE `community_comment`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `post_id` bigint(20) NOT NULL COMMENT '帖子ID',
  `user_id` bigint(20) NOT NULL COMMENT '评论人ID',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父评论ID(0为一级评论)',
  `reply_to_id` bigint(20) NULL DEFAULT NULL COMMENT '回复的评论ID',
  `reply_to_user_id` bigint(20) NULL DEFAULT NULL COMMENT '回复的用户ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
  `like_count` int(11) NULL DEFAULT 0 COMMENT '点赞数',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态(0正常 1隐藏 2删除)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_post_id`(`post_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of community_comment
-- ----------------------------

-- ----------------------------
-- Table structure for community_forum_category
-- ----------------------------
DROP TABLE IF EXISTS `community_forum_category`;
CREATE TABLE `community_forum_category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '板块ID',
  `category_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '板块名称',
  `category_icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '板块图标',
  `sort_order` int(4) NULL DEFAULT 0 COMMENT '排序',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '板块描述',
  `post_count` int(11) NULL DEFAULT 0 COMMENT '帖子数',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态(0正常 1停用)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '论坛板块表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of community_forum_category
-- ----------------------------
INSERT INTO `community_forum_category` VALUES (1, '学习交流', 'Reading', 1, '学习经验、资料分享', 0, '0', '2025-11-17 18:00:00', '2025-11-17 18:00:00');
INSERT INTO `community_forum_category` VALUES (2, '生活分享', 'Coffee', 2, '校园生活、美食旅游', 0, '0', '2025-11-17 18:00:00', '2025-11-17 18:00:00');
INSERT INTO `community_forum_category` VALUES (3, '活动讨论', 'ChatDotRound', 3, '活动预告、活动回顾', 0, '0', '2025-11-17 18:00:00', '2025-11-17 18:00:00');
INSERT INTO `community_forum_category` VALUES (4, '招聘求职', 'Briefcase', 4, '实习招聘、求职经验', 0, '0', '2025-11-17 18:00:00', '2025-11-17 18:00:00');

-- ----------------------------
-- Table structure for community_like
-- ----------------------------
DROP TABLE IF EXISTS `community_like`;
CREATE TABLE `community_like`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '点赞ID',
  `target_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '目标类型(post帖子/comment评论/moment动态)',
  `target_id` bigint(20) NOT NULL COMMENT '目标ID',
  `user_id` bigint(20) NOT NULL COMMENT '点赞人ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_target_user`(`target_type`, `target_id`, `user_id`) USING BTREE,
  INDEX `idx_target`(`target_type`, `target_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '点赞记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of community_like
-- ----------------------------

-- ----------------------------
-- Table structure for community_message
-- ----------------------------
DROP TABLE IF EXISTS `community_message`;
CREATE TABLE `community_message`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `message_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息类型(system系统/chat聊天/notice通知)',
  `sender_id` bigint(20) NULL DEFAULT NULL COMMENT '发送人ID',
  `receiver_id` bigint(20) NOT NULL COMMENT '接收人ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息内容',
  `content_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'text' COMMENT '内容类型(text文字/image图片/file文件)',
  `related_id` bigint(20) NULL DEFAULT NULL COMMENT '关联ID',
  `is_read` tinyint(1) NULL DEFAULT 0 COMMENT '是否已读(0未读 1已读)',
  `read_time` datetime NULL DEFAULT NULL COMMENT '阅读时间',
  `is_withdraw` tinyint(1) NULL DEFAULT 0 COMMENT '是否撤回(0否 1是)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sender_id`(`sender_id`) USING BTREE,
  INDEX `idx_receiver_id`(`receiver_id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of community_message
-- ----------------------------

-- ----------------------------
-- Table structure for community_moment
-- ----------------------------
DROP TABLE IF EXISTS `community_moment`;
CREATE TABLE `community_moment`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '动态ID',
  `user_id` bigint(20) NOT NULL COMMENT '发布人ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '动态内容',
  `images` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '图片(JSON数组)',
  `video_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '视频地址',
  `location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '位置',
  `visible_range` tinyint(1) NULL DEFAULT 0 COMMENT '可见范围(0公开 1仅好友 2私密)',
  `like_count` int(11) NULL DEFAULT 0 COMMENT '点赞数',
  `comment_count` int(11) NULL DEFAULT 0 COMMENT '评论数',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态(0正常 1隐藏 2删除)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '动态表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of community_moment
-- ----------------------------

-- ----------------------------
-- Table structure for community_notice
-- ----------------------------
DROP TABLE IF EXISTS `community_notice`;
CREATE TABLE `community_notice`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告内容',
  `notice_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'normal' COMMENT '公告类型(urgent紧急/important重要/normal一般)',
  `is_top` tinyint(1) NULL DEFAULT 0 COMMENT '是否置顶(0否 1是)',
  `publish_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `publisher_id` bigint(20) NOT NULL COMMENT '发布人ID',
  `target_users` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '目标用户(JSON数组,为空表示全部)',
  `read_count` int(11) NULL DEFAULT 0 COMMENT '已读人数',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态(0草稿 1已发布 2已撤回)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_publisher_id`(`publisher_id`) USING BTREE,
  INDEX `idx_publish_time`(`publish_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of community_notice
-- ----------------------------

-- ----------------------------
-- Table structure for community_notice_read
-- ----------------------------
DROP TABLE IF EXISTS `community_notice_read`;
CREATE TABLE `community_notice_read`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `notice_id` bigint(20) NOT NULL COMMENT '公告ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `read_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '阅读时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_notice_user`(`notice_id`, `user_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公告阅读记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of community_notice_read
-- ----------------------------

-- ----------------------------
-- Table structure for community_post
-- ----------------------------
DROP TABLE IF EXISTS `community_post`;
CREATE TABLE `community_post`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
  `category_id` bigint(20) NOT NULL COMMENT '板块ID',
  `user_id` bigint(20) NOT NULL COMMENT '发帖人ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `images` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '图片(JSON数组)',
  `attachments` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '附件(JSON数组)',
  `is_top` tinyint(1) NULL DEFAULT 0 COMMENT '是否置顶(0否 1是)',
  `is_hot` tinyint(1) NULL DEFAULT 0 COMMENT '是否热门(0否 1是)',
  `is_essence` tinyint(1) NULL DEFAULT 0 COMMENT '是否精华(0否 1是)',
  `view_count` int(11) NULL DEFAULT 0 COMMENT '浏览次数',
  `like_count` int(11) NULL DEFAULT 0 COMMENT '点赞数',
  `comment_count` int(11) NULL DEFAULT 0 COMMENT '评论数',
  `collect_count` int(11) NULL DEFAULT 0 COMMENT '收藏数',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态(0正常 1隐藏 2删除)',
  `audit_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '审核状态(0待审核 1已通过 2未通过)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category_id`(`category_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE,
  INDEX `idx_is_hot`(`is_hot`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '帖子表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of community_post
-- ----------------------------

-- ----------------------------
-- Table structure for service_finance_record
-- ----------------------------
DROP TABLE IF EXISTS `service_finance_record`;
CREATE TABLE `service_finance_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `record_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '记录类型(0收入 1支出)',
  `amount` decimal(10, 2) NOT NULL COMMENT '金额',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类别(会费/赞助/活动收入/活动支出/物资采购/场地费用/其他)',
  `related_id` bigint(20) NULL DEFAULT NULL COMMENT '关联ID(活动ID/报销ID等)',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `voucher_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '凭证图片',
  `operator_id` bigint(20) NOT NULL COMMENT '操作人ID',
  `audit_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '审核状态(0待审核 1已通过 2未通过)',
  `auditor_id` bigint(20) NULL DEFAULT NULL COMMENT '审核人ID',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `audit_remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_record_type`(`record_type`) USING BTREE,
  INDEX `idx_category`(`category`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE,
  INDEX `idx_audit_status`(`audit_status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '财务记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of service_finance_record
-- ----------------------------

-- ----------------------------
-- Table structure for service_material
-- ----------------------------
DROP TABLE IF EXISTS `service_material`;
CREATE TABLE `service_material`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '物资ID',
  `material_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '物资名称',
  `material_category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物资类别',
  `quantity` int(11) NULL DEFAULT 0 COMMENT '数量',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存放位置',
  `warning_quantity` int(11) NULL DEFAULT 0 COMMENT '预警数量',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '描述',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态(0正常 1报废)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_material_category`(`material_category`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '物资表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of service_material
-- ----------------------------

-- ----------------------------
-- Table structure for service_material_record
-- ----------------------------
DROP TABLE IF EXISTS `service_material_record`;
CREATE TABLE `service_material_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `material_id` bigint(20) NOT NULL COMMENT '物资ID',
  `record_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '记录类型(0入库 1出库 2归还 3报废)',
  `quantity` int(11) NOT NULL COMMENT '数量',
  `operator_id` bigint(20) NOT NULL COMMENT '操作人ID',
  `borrower_id` bigint(20) NULL DEFAULT NULL COMMENT '借用人ID',
  `expected_return_time` datetime NULL DEFAULT NULL COMMENT '预计归还时间',
  `actual_return_time` datetime NULL DEFAULT NULL COMMENT '实际归还时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_material_id`(`material_id`) USING BTREE,
  INDEX `idx_operator_id`(`operator_id`) USING BTREE,
  INDEX `idx_borrower_id`(`borrower_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '物资出入库记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of service_material_record
-- ----------------------------

-- ----------------------------
-- Table structure for service_reimbursement
-- ----------------------------
DROP TABLE IF EXISTS `service_reimbursement`;
CREATE TABLE `service_reimbursement`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '报销ID',
  `applicant_id` bigint(20) NOT NULL COMMENT '申请人ID',
  `amount` decimal(10, 2) NOT NULL COMMENT '报销金额',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '报销类别',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报销说明',
  `voucher_urls` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '凭证图片(JSON数组)',
  `related_activity_id` bigint(20) NULL DEFAULT NULL COMMENT '关联活动ID',
  `audit_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '审核状态(0待审核 1已通过 2未通过)',
  `auditor_id` bigint(20) NULL DEFAULT NULL COMMENT '审核人ID',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `audit_remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核备注',
  `payment_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '支付状态(0未支付 1已支付)',
  `payment_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_applicant_id`(`applicant_id`) USING BTREE,
  INDEX `idx_audit_status`(`audit_status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报销申请表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of service_reimbursement
-- ----------------------------

-- ----------------------------
-- Table structure for service_resource
-- ----------------------------
DROP TABLE IF EXISTS `service_resource`;
CREATE TABLE `service_resource`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `resource_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源名称',
  `resource_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源类型(room会议室/equipment设备/venue场地)',
  `capacity` int(11) NULL DEFAULT 0 COMMENT '容量',
  `location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '位置',
  `facilities` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '设施(JSON数组)',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '描述',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态(0正常 1维护 2停用)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_resource_type`(`resource_type`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '资源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of service_resource
-- ----------------------------

-- ----------------------------
-- Table structure for service_resource_booking
-- ----------------------------
DROP TABLE IF EXISTS `service_resource_booking`;
CREATE TABLE `service_resource_booking`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '预订ID',
  `resource_id` bigint(20) NOT NULL COMMENT '资源ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `purpose` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '使用目的',
  `audit_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '审核状态(0待审核 1已通过 2未通过)',
  `auditor_id` bigint(20) NULL DEFAULT NULL COMMENT '审核人ID',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `audit_remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核备注',
  `cancel_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '取消原因',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态(0待使用 1使用中 2已完成 3已取消)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_resource_id`(`resource_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_start_time`(`start_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '资源预订表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of service_resource_booking
-- ----------------------------

-- ----------------------------
-- Table structure for service_volunteer_activity
-- ----------------------------
DROP TABLE IF EXISTS `service_volunteer_activity`;
CREATE TABLE `service_volunteer_activity`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '志愿活动ID',
  `activity_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '活动名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '活动描述',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动地点',
  `max_volunteers` int(11) NULL DEFAULT 0 COMMENT '最大志愿者人数',
  `current_volunteers` int(11) NULL DEFAULT 0 COMMENT '当前志愿者人数',
  `volunteer_hours` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '志愿时长(小时)',
  `organizer_id` bigint(20) NOT NULL COMMENT '组织者ID',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态(0招募中 1进行中 2已结束 3已取消)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_organizer_id`(`organizer_id`) USING BTREE,
  INDEX `idx_start_time`(`start_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '志愿活动表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of service_volunteer_activity
-- ----------------------------

-- ----------------------------
-- Table structure for service_volunteer_record
-- ----------------------------
DROP TABLE IF EXISTS `service_volunteer_record`;
CREATE TABLE `service_volunteer_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `volunteer_activity_id` bigint(20) NOT NULL COMMENT '志愿活动ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `volunteer_hours` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '志愿时长(小时)',
  `is_confirmed` tinyint(1) NULL DEFAULT 0 COMMENT '是否确认(0否 1是)',
  `confirmer_id` bigint(20) NULL DEFAULT NULL COMMENT '确认人ID',
  `confirm_time` datetime NULL DEFAULT NULL COMMENT '确认时间',
  `certificate_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '证书URL',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_activity_user`(`volunteer_activity_id`, `user_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '志愿记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of service_volunteer_record
-- ----------------------------

-- ----------------------------
-- Table structure for statistics_activity
-- ----------------------------
DROP TABLE IF EXISTS `statistics_activity`;
CREATE TABLE `statistics_activity`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '统计ID',
  `activity_id` bigint(20) NOT NULL COMMENT '活动ID',
  `registration_count` int(11) NULL DEFAULT 0 COMMENT '报名人数',
  `actual_participants` int(11) NULL DEFAULT 0 COMMENT '实际参与人数',
  `sign_in_count` int(11) NULL DEFAULT 0 COMMENT '签到人数',
  `late_count` int(11) NULL DEFAULT 0 COMMENT '迟到人数',
  `early_leave_count` int(11) NULL DEFAULT 0 COMMENT '早退人数',
  `evaluation_count` int(11) NULL DEFAULT 0 COMMENT '评价人数',
  `average_rating` decimal(3, 2) NULL DEFAULT 0.00 COMMENT '平均评分',
  `total_cost` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '总成本',
  `total_income` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '总收入',
  `participation_rate` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '参与率(%)',
  `satisfaction_rate` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '满意度(%)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_activity_id`(`activity_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '活动统计表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of statistics_activity
-- ----------------------------

-- ----------------------------
-- Table structure for statistics_daily
-- ----------------------------
DROP TABLE IF EXISTS `statistics_daily`;
CREATE TABLE `statistics_daily`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '统计ID',
  `stat_date` date NOT NULL COMMENT '统计日期',
  `total_members` int(11) NULL DEFAULT 0 COMMENT '总成员数',
  `active_members` int(11) NULL DEFAULT 0 COMMENT '活跃成员数',
  `new_members` int(11) NULL DEFAULT 0 COMMENT '新增成员数',
  `total_activities` int(11) NULL DEFAULT 0 COMMENT '总活动数',
  `ongoing_activities` int(11) NULL DEFAULT 0 COMMENT '进行中活动数',
  `total_posts` int(11) NULL DEFAULT 0 COMMENT '总帖子数',
  `new_posts` int(11) NULL DEFAULT 0 COMMENT '新增帖子数',
  `total_income` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '总收入',
  `total_expense` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '总支出',
  `balance` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '余额',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_stat_date`(`stat_date`) USING BTREE,
  INDEX `idx_stat_date`(`stat_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '每日统计表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of statistics_daily
-- ----------------------------

-- ----------------------------
-- Table structure for statistics_finance
-- ----------------------------
DROP TABLE IF EXISTS `statistics_finance`;
CREATE TABLE `statistics_finance`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '统计ID',
  `stat_month` varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '统计月份(YYYY-MM)',
  `total_income` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '总收入',
  `total_expense` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '总支出',
  `membership_fee` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '会费收入',
  `sponsorship` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '赞助收入',
  `activity_income` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '活动收入',
  `activity_expense` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '活动支出',
  `material_expense` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '物资支出',
  `venue_expense` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '场地支出',
  `other_income` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '其他收入',
  `other_expense` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '其他支出',
  `balance` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '余额',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_stat_month`(`stat_month`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '财务统计表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of statistics_finance
-- ----------------------------

-- ----------------------------
-- Table structure for statistics_member
-- ----------------------------
DROP TABLE IF EXISTS `statistics_member`;
CREATE TABLE `statistics_member`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '统计ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `activity_count` int(11) NULL DEFAULT 0 COMMENT '参与活动数',
  `post_count` int(11) NULL DEFAULT 0 COMMENT '发帖数',
  `comment_count` int(11) NULL DEFAULT 0 COMMENT '评论数',
  `volunteer_hours` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '志愿时长',
  `contribution_value` int(11) NULL DEFAULT 0 COMMENT '贡献值',
  `active_days` int(11) NULL DEFAULT 0 COMMENT '活跃天数',
  `last_active_time` datetime NULL DEFAULT NULL COMMENT '最后活跃时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '成员统计表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of statistics_member
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int(4) NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_dict_type`(`dict_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 65 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', '性别男', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', '性别女', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', '性别未知', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', '显示菜单', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', '隐藏菜单', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', '正常状态', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', '停用状态', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (8, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', '系统默认是', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (9, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', '系统默认否', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (10, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', '通知', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (11, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', '公告', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (12, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', '正常状态', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (13, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', '关闭状态', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (14, 1, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', '其他操作', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (15, 2, '新增', '1', 'sys_oper_type', '', 'success', 'N', '0', '新增操作', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (16, 3, '修改', '2', 'sys_oper_type', '', 'primary', 'N', '0', '修改操作', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (17, 4, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', '删除操作', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (18, 5, '查询', '4', 'sys_oper_type', '', '', 'N', '0', '查询操作', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (19, 6, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', '导出操作', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (20, 7, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', '导入操作', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (21, 1, '在校生', 'student', 'sys_user_type', '', 'primary', 'Y', '0', '在校学生', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (22, 2, '毕业生', 'graduate', 'sys_user_type', '', 'success', 'N', '0', '已毕业校友', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (23, 3, '教师', 'teacher', 'sys_user_type', '', 'warning', 'N', '0', '教师用户', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (24, 1, '讲座', 'lecture', 'activity_type', '', 'primary', 'Y', '0', '学术讲座', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (25, 2, '比赛', 'competition', 'activity_type', '', 'danger', 'N', '0', '竞赛活动', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (26, 3, '聚会', 'party', 'activity_type', '', 'success', 'N', '0', '联谊聚会', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (27, 4, '公益', 'volunteer', 'activity_type', '', 'warning', 'N', '0', '志愿服务', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (28, 5, '培训', 'training', 'activity_type', '', 'info', 'N', '0', '技能培训', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (29, 6, '展览', 'exhibition', 'activity_type', '', '', 'N', '0', '作品展览', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (30, 1, '报名中', '0', 'activity_status', '', 'primary', 'Y', '0', '活动报名中', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (31, 2, '进行中', '1', 'activity_status', '', 'warning', 'N', '0', '活动进行中', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (32, 3, '已结束', '2', 'activity_status', '', 'info', 'N', '0', '活动已结束', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (33, 4, '已取消', '3', 'activity_status', '', 'danger', 'N', '0', '活动已取消', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (34, 1, '待审核', '0', 'audit_status', '', 'warning', 'Y', '0', '等待审核', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (35, 2, '已通过', '1', 'audit_status', '', 'success', 'N', '0', '审核通过', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (36, 3, '未通过', '2', 'audit_status', '', 'danger', 'N', '0', '审核未通过', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (37, 1, '未支付', '0', 'payment_status', '', 'warning', 'Y', '0', '未支付', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (38, 2, '已支付', '1', 'payment_status', '', 'success', 'N', '0', '已支付', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (39, 3, '已退款', '2', 'payment_status', '', 'info', 'N', '0', '已退款', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (40, 1, '会费', 'membership_fee', 'finance_category', '', 'primary', 'Y', '0', '会费收入', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (41, 2, '赞助', 'sponsorship', 'finance_category', '', 'success', 'N', '0', '赞助收入', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (42, 3, '活动收入', 'activity_income', 'finance_category', '', 'success', 'N', '0', '活动收入', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (43, 4, '活动支出', 'activity_expense', 'finance_category', '', 'danger', 'N', '0', '活动支出', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (44, 5, '物资采购', 'material_purchase', 'finance_category', '', 'danger', 'N', '0', '物资采购', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (45, 6, '场地费用', 'venue_fee', 'finance_category', '', 'danger', 'N', '0', '场地费用', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (46, 7, '其他收入', 'other_income', 'finance_category', '', 'info', 'N', '0', '其他收入', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (47, 8, '其他支出', 'other_expense', 'finance_category', '', 'info', 'N', '0', '其他支出', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (48, 1, '校级', 'school', 'honor_level', '', 'info', 'Y', '0', '校级荣誉', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (49, 2, '市级', 'city', 'honor_level', '', 'primary', 'N', '0', '市级荣誉', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (50, 3, '省级', 'province', 'honor_level', '', 'warning', 'N', '0', '省级荣誉', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (51, 4, '国家级', 'national', 'honor_level', '', 'danger', 'N', '0', '国家级荣誉', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (52, 1, '会议室', 'room', 'resource_type', '', 'primary', 'Y', '0', '会议室', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (53, 2, '设备', 'equipment', 'resource_type', '', 'success', 'N', '0', '设备器材', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (54, 3, '场地', 'venue', 'resource_type', '', 'warning', 'N', '0', '活动场地', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (55, 1, '办公用品', 'office', 'material_category', '', 'primary', 'Y', '0', '办公用品', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (56, 2, '活动物资', 'activity', 'material_category', '', 'success', 'N', '0', '活动物资', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (57, 3, '宣传物料', 'promotion', 'material_category', '', 'warning', 'N', '0', '宣传物料', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (58, 4, '电子设备', 'electronic', 'material_category', '', 'danger', 'N', '0', '电子设备', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (59, 1, '紧急', 'urgent', 'notice_type_new', '', 'danger', 'Y', '0', '紧急公告', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (60, 2, '重要', 'important', 'notice_type_new', '', 'warning', 'N', '0', '重要公告', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (61, 3, '一般', 'normal', 'notice_type_new', '', 'info', 'N', '0', '一般公告', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (62, 1, '系统消息', 'system', 'message_type', '', 'primary', 'Y', '0', '系统消息', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (63, 2, '聊天消息', 'chat', 'message_type', '', 'success', 'N', '0', '聊天消息', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_data` VALUES (64, 3, '通知消息', 'notice', 'message_type', '', 'warning', 'N', '0', '通知消息', '2025-11-17 18:44:19', '2025-11-17 18:44:19');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_dict_type`(`dict_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', '用户性别列表', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', '菜单状态列表', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', '系统开关列表', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', '任务状态列表', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_type` VALUES (5, '系统是否', 'sys_yes_no', '0', '系统是否列表', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_type` VALUES (6, '通知类型', 'sys_notice_type', '0', '通知类型列表', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_type` VALUES (7, '通知状态', 'sys_notice_status', '0', '通知状态列表', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_type` VALUES (8, '操作类型', 'sys_oper_type', '0', '操作类型列表', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_type` VALUES (9, '用户类型', 'sys_user_type', '0', '用户类型列表', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_type` VALUES (10, '校友类型', 'alumni_type', '0', '校友类型列表', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_type` VALUES (11, '活动类型', 'activity_type', '0', '活动类型列表', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_type` VALUES (12, '活动状态', 'activity_status', '0', '活动状态列表', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_type` VALUES (13, '审核状态', 'audit_status', '0', '审核状态列表', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_type` VALUES (14, '支付状态', 'payment_status', '0', '支付状态列表', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_type` VALUES (15, '财务类别', 'finance_category', '0', '财务类别列表', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_type` VALUES (16, '荣誉级别', 'honor_level', '0', '荣誉级别列表', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_type` VALUES (17, '资源类型', 'resource_type', '0', '资源类型列表', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_type` VALUES (18, '物资类别', 'material_category', '0', '物资类别列表', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_type` VALUES (19, '公告类型', 'notice_type_new', '0', '公告类型列表', '2025-11-17 18:44:19', '2025-11-17 18:44:19');
INSERT INTO `sys_dict_type` VALUES (20, '消息类型', 'message_type', '0', '消息类型列表', '2025-11-17 18:44:19', '2025-11-17 18:44:19');

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名称',
  `original_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '原始文件名',
  `file_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件路径',
  `file_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件URL',
  `file_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件类型',
  `file_size` bigint(20) NULL DEFAULT 0 COMMENT '文件大小（字节）',
  `file_ext` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件扩展名',
  `upload_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上传用户',
  `upload_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_file_type`(`file_type`) USING BTREE,
  INDEX `idx_upload_user`(`upload_user`) USING BTREE,
  INDEX `idx_upload_time`(`upload_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_file
-- ----------------------------
INSERT INTO `sys_file` VALUES (1, '1e5be0bf3ba74341be31167a3ffec9a2.png', 'Snipaste_2024-10-17_22-21-19.png', 'F:/桌面/SpringBoot+Vue/uploads/2025/10/26\\1e5be0bf3ba74341be31167a3ffec9a2.png', '/uploads/2025/10/26/1e5be0bf3ba74341be31167a3ffec9a2.png', 'image', 90498, 'png', 'admin', '2025-10-26 14:01:37', NULL);
INSERT INTO `sys_file` VALUES (2, '55d24a1117dd4b6dba72178eeb77133a.png', 'Snipaste_2024-10-28_22-24-11.png', 'F:/桌面/SpringBoot+Vue/uploads/2025/10/26\\55d24a1117dd4b6dba72178eeb77133a.png', '/uploads/2025/10/26/55d24a1117dd4b6dba72178eeb77133a.png', 'image', 2565786, 'png', 'admin', '2025-10-26 14:01:41', NULL);
INSERT INTO `sys_file` VALUES (3, 'b96d049c34cc497caf6acc1021c5569a.png', 'Snipaste_2024-10-18_00-17-37.png', 'F:/桌面/SpringBoot+Vue/uploads/2025/10/26\\b96d049c34cc497caf6acc1021c5569a.png', '/uploads/2025/10/26/b96d049c34cc497caf6acc1021c5569a.png', 'image', 9430, 'png', 'admin', '2025-10-26 14:02:09', NULL);
INSERT INTO `sys_file` VALUES (4, '843622b1ea2942258d75f6a65877e66d.jpg', '6161F95FFC6BA3DD8077BE1C84D089D4.jpg', 'F:/桌面/SpringBoot+Vue/uploads/2025/10/26\\843622b1ea2942258d75f6a65877e66d.jpg', '/uploads/2025/10/26/843622b1ea2942258d75f6a65877e66d.jpg', 'image', 876087, 'jpg', 'admin', '2025-10-26 14:02:17', NULL);
INSERT INTO `sys_file` VALUES (5, 'cbba7850708643ea9708974ff0fb8c2e.png', 'logo.PNG', 'F:\\桌面\\2026年毕设\\SpringBoot+Vue\\uploads\\2025/11/17\\cbba7850708643ea9708974ff0fb8c2e.png', '/uploads/2025/11/17/cbba7850708643ea9708974ff0fb8c2e.png', 'image', 471007, 'png', 'admin', '2025-11-17 18:20:56', NULL);

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户账号',
  `ip_address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作系统',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '提示消息',
  `login_time` datetime NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_username`(`username`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_login_time`(`login_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统访问记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------
INSERT INTO `sys_login_log` VALUES (1, 'admin', '127.0.0.1', '', 'Chrome', 'Windows 10 or Windows Server 2016', '0', '登录成功', '2025-10-26 14:00:34');
INSERT INTO `sys_login_log` VALUES (2, 'admin', '127.0.0.1', '', 'Chrome', 'Windows 10 or Windows Server 2016', '0', '登录成功', '2025-10-26 14:02:27');
INSERT INTO `sys_login_log` VALUES (3, 'admin', '127.0.0.1', '', 'Chrome', 'Windows 10 or Windows Server 2016', '1', '密码错误', '2025-10-29 14:28:03');
INSERT INTO `sys_login_log` VALUES (4, 'admin', '127.0.0.1', '', 'Chrome', 'Windows 10 or Windows Server 2016', '1', '密码错误', '2025-10-29 14:28:25');
INSERT INTO `sys_login_log` VALUES (5, 'admin', '127.0.0.1', '', 'Chrome', 'Windows 10 or Windows Server 2016', '0', '登录成功', '2025-10-29 14:28:28');
INSERT INTO `sys_login_log` VALUES (6, 'admin', '127.0.0.1', '', 'Chrome', 'Windows 10 or Windows Server 2016', '0', '登录成功', '2025-11-17 18:13:08');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int(4) NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径',
  `is_frame` int(1) NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int(1) NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1016 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 1, '/system', NULL, 1, 0, 'M', '0', '0', '', 'Setting', '系统管理目录', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (2, '系统监控', 0, 2, '/monitor', NULL, 1, 0, 'M', '0', '0', '', 'Monitor', '系统监控目录', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, '/system/user', '/admin/user', 1, 0, 'C', '0', '0', 'system:user:list', 'User', '用户管理菜单', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, '/system/role', '/admin/role', 1, 0, 'C', '0', '0', 'system:role:list', 'UserFilled', '角色管理菜单', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, '/system/menu', '/admin/menu', 1, 0, 'C', '0', '0', 'system:menu:list', 'Menu', '菜单管理菜单', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1000, '用户查询', 100, 1, '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1001, '用户新增', 100, 2, '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1002, '用户修改', 100, 3, '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1003, '用户删除', 100, 4, '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1004, '用户导出', 100, 5, '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1005, '重置密码', 100, 6, '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1006, '角色查询', 101, 1, '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1007, '角色新增', 101, 2, '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1008, '角色修改', 101, 3, '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1009, '角色删除', 101, 4, '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1010, '角色导出', 101, 5, '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1011, '分配权限', 101, 6, '', '', 1, 0, 'F', '0', '0', 'system:role:auth', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1012, '菜单查询', 102, 1, '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1013, '菜单新增', 102, 2, '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1014, '菜单修改', 102, 3, '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1015, '菜单删除', 102, 4, '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '模块标题',
  `business_type` int(2) NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除 4查询 5导出 6导入）',
  `method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求方式',
  `operator_type` int(1) NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `operator_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作人员',
  `operator_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求URL',
  `operator_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '主机地址',
  `operator_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作地点',
  `operator_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '返回参数',
  `status` int(1) NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '错误消息',
  `operator_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint(20) NULL DEFAULT 0 COMMENT '消耗时间(毫秒)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_operator_name`(`operator_name`) USING BTREE,
  INDEX `idx_business_type`(`business_type`) USING BTREE,
  INDEX `idx_operator_time`(`operator_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_operation_log
-- ----------------------------
INSERT INTO `sys_operation_log` VALUES (1, '用户管理', 2, 'com.example.admin.controller.UserController.update()', 'PUT', 1, 'admin', '/api/user', '127.0.0.1', '', '{\"id\":1,\"username\":\"admin\",\"nickname\":\"管理员\",\"email\":\"admin@example.com\",\"phone\":\"13800138000\",\"avatar\":\"/uploads/2025/10/26/b96d049c34cc497caf6acc1021c5569a.png\",\"roleId\":1,\"status\":1,\"deleted\":0,\"createTime\":1761458163000,\"updateTime\":1761458529710}', '{\"code\":200,\"message\":\"success\",\"data\":{\"id\":1,\"username\":\"admin\",\"nickname\":\"管理员\",\"email\":\"admin@example.com\",\"phone\":\"13800138000\",\"avatar\":\"/uploads/2025/10/26/b96d049c34cc497caf6acc1021c5569a.png\",\"roleId\":1,\"status\":1,\"deleted\":0,\"createTime\":1761458163000,\"updateTime\":1761458529710}}', 0, '', '2025-10-26 14:02:10', 17);
INSERT INTO `sys_operation_log` VALUES (2, '用户管理', 2, 'com.example.admin.controller.UserController.update()', 'PUT', 1, 'admin', '/api/user', '127.0.0.1', '', '{\"id\":1,\"username\":\"admin\",\"nickname\":\"管理员\",\"email\":\"admin@example.com\",\"phone\":\"13800138000\",\"avatar\":\"/uploads/2025/10/26/843622b1ea2942258d75f6a65877e66d.jpg\",\"roleId\":1,\"status\":1,\"deleted\":0,\"createTime\":1761458163000,\"updateTime\":1761458537547}', '{\"code\":200,\"message\":\"success\",\"data\":{\"id\":1,\"username\":\"admin\",\"nickname\":\"管理员\",\"email\":\"admin@example.com\",\"phone\":\"13800138000\",\"avatar\":\"/uploads/2025/10/26/843622b1ea2942258d75f6a65877e66d.jpg\",\"roleId\":1,\"status\":1,\"deleted\":0,\"createTime\":1761458163000,\"updateTime\":1761458537547}}', 0, '', '2025-10-26 14:02:18', 4);
INSERT INTO `sys_operation_log` VALUES (3, '用户管理', 2, 'com.example.admin.controller.UserController.update()', 'PUT', 1, 'admin', '/api/user', '127.0.0.1', '', '{\"id\":2,\"username\":\"user1\",\"nickname\":\"用户1\",\"email\":\"user1@example.com\",\"phone\":\"13800138001\",\"avatar\":\"/uploads/2025/11/17/cbba7850708643ea9708974ff0fb8c2e.png\",\"roleId\":2,\"status\":1,\"deleted\":0,\"createTime\":1761458163000,\"updateTime\":1763374857390}', '{\"code\":200,\"message\":\"success\",\"data\":{\"id\":2,\"username\":\"user1\",\"nickname\":\"用户1\",\"email\":\"user1@example.com\",\"phone\":\"13800138001\",\"avatar\":\"/uploads/2025/11/17/cbba7850708643ea9708974ff0fb8c2e.png\",\"roleId\":2,\"status\":1,\"deleted\":0,\"createTime\":1761458163000,\"updateTime\":1763374857390}}', 0, '', '2025-11-17 18:20:57', 40);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色标识',
  `role_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'system' COMMENT '角色类型(system系统角色/club社团角色/custom自定义角色)',
  `role_level` int(4) NULL DEFAULT 0 COMMENT '角色级别(数字越大权限越高)',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '数据范围(1全部 2自定义 3本部门 4本部门及以下 5仅本人)',
  `club_id` bigint(20) NULL DEFAULT NULL COMMENT '所属社团ID(社团角色专用)',
  `max_members` int(11) NULL DEFAULT 0 COMMENT '最大成员数(0表示不限)',
  `permissions` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '权限列表(JSON数组)',
  `status` int(1) NULL DEFAULT 1 COMMENT '状态(0禁用 1启用)',
  `is_default` tinyint(1) NULL DEFAULT 0 COMMENT '是否默认角色(0否 1是)',
  `sort_order` int(4) NULL DEFAULT 0 COMMENT '显示顺序',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_key`(`role_key`) USING BTREE,
  INDEX `idx_role_type`(`role_type`) USING BTREE,
  INDEX `idx_club_id`(`club_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表(扩展)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'super_admin', 'system', 100, '系统最高权限，拥有所有功能权限', '1', NULL, 0, NULL, 1, 0, 1, '系统内置角色，不可删除', 'system', '2025-11-17 18:00:00', NULL, '2025-11-17 18:00:00');
INSERT INTO `sys_role` VALUES (2, '系统管理员', 'admin', 'system', 90, '系统管理员，管理系统配置和用户', '1', NULL, 0, NULL, 1, 0, 2, '系统内置角色', 'system', '2025-11-17 18:00:00', NULL, '2025-11-17 18:00:00');
INSERT INTO `sys_role` VALUES (3, '普通用户', 'user', 'system', 10, '普通用户，基础权限', '5', NULL, 0, NULL, 1, 1, 10, '默认角色', 'system', '2025-11-17 18:00:00', NULL, '2025-11-17 18:00:00');
INSERT INTO `sys_role` VALUES (4, '社团会长', 'club_president', 'club', 80, '社团最高负责人，拥有社团所有权限', '3', NULL, 0, '[\"club:all\",\"activity:all\",\"finance:all\",\"member:all\"]', 1, 0, 3, '社团核心角色', 'system', '2025-11-17 18:00:00', NULL, '2025-11-17 18:00:00');
INSERT INTO `sys_role` VALUES (5, '社团副会长', 'club_vice_president', 'club', 70, '协助会长管理社团事务', '3', NULL, 0, '[\"club:view\",\"activity:manage\",\"finance:view\",\"member:manage\"]', 1, 0, 4, '社团核心角色', 'system', '2025-11-17 18:00:00', NULL, '2025-11-17 18:00:00');
INSERT INTO `sys_role` VALUES (6, '部长', 'department_head', 'club', 60, '部门负责人，管理本部门事务', '4', NULL, 0, '[\"activity:manage\",\"member:view\"]', 1, 0, 5, '部门管理角色', 'system', '2025-11-17 18:00:00', NULL, '2025-11-17 18:00:00');
INSERT INTO `sys_role` VALUES (7, '干事', 'officer', 'club', 40, '社团干事，协助部长工作', '4', NULL, 0, '[\"activity:view\",\"activity:join\"]', 1, 0, 6, '社团成员角色', 'system', '2025-11-17 18:00:00', NULL, '2025-11-17 18:00:00');
INSERT INTO `sys_role` VALUES (8, '社团成员', 'club_member', 'club', 20, '普通社团成员', '5', NULL, 0, '[\"activity:view\",\"activity:join\",\"post:create\"]', 1, 0, 7, '基础成员角色', 'system', '2025-11-17 18:00:00', NULL, '2025-11-17 18:00:00');
INSERT INTO `sys_role` VALUES (9, '财务管理员', 'finance_admin', 'custom', 50, '负责财务管理和审核', '2', NULL, 0, '[\"finance:all\",\"reimbursement:audit\"]', 1, 0, 8, '财务专员角色', 'system', '2025-11-17 18:00:00', NULL, '2025-11-17 18:00:00');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1);
INSERT INTO `sys_role_menu` VALUES (1, 2);
INSERT INTO `sys_role_menu` VALUES (1, 100);
INSERT INTO `sys_role_menu` VALUES (1, 101);
INSERT INTO `sys_role_menu` VALUES (1, 102);
INSERT INTO `sys_role_menu` VALUES (1, 1000);
INSERT INTO `sys_role_menu` VALUES (1, 1001);
INSERT INTO `sys_role_menu` VALUES (1, 1002);
INSERT INTO `sys_role_menu` VALUES (1, 1003);
INSERT INTO `sys_role_menu` VALUES (1, 1004);
INSERT INTO `sys_role_menu` VALUES (1, 1005);
INSERT INTO `sys_role_menu` VALUES (1, 1006);
INSERT INTO `sys_role_menu` VALUES (1, 1007);
INSERT INTO `sys_role_menu` VALUES (1, 1008);
INSERT INTO `sys_role_menu` VALUES (1, 1009);
INSERT INTO `sys_role_menu` VALUES (1, 1010);
INSERT INTO `sys_role_menu` VALUES (1, 1011);
INSERT INTO `sys_role_menu` VALUES (1, 1012);
INSERT INTO `sys_role_menu` VALUES (1, 1013);
INSERT INTO `sys_role_menu` VALUES (1, 1014);
INSERT INTO `sys_role_menu` VALUES (1, 1015);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `student_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学号',
  `id_card` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '性别(0男 1女 2未知)',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `wechat` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信号',
  `qq` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'QQ号',
  `avatar` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '头像URL',
  `enrollment_year` int(4) NULL DEFAULT NULL COMMENT '入学年份',
  `graduation_year` int(4) NULL DEFAULT NULL COMMENT '毕业年份',
  `major` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '专业',
  `class_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '班级',
  `college` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学院',
  `user_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'student' COMMENT '用户类型(student在校生/graduate毕业生/teacher教师)',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  `status` int(1) NULL DEFAULT 1 COMMENT '状态(0禁用 1启用)',
  `deleted` int(1) NULL DEFAULT 0 COMMENT '逻辑删除(0未删除 1已删除)',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后登录IP',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username`) USING BTREE,
  UNIQUE INDEX `uk_student_no`(`student_no`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE,
  INDEX `idx_user_type`(`user_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表(扩展)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
