-- 添加活动相册管理菜单
-- 活动管理模块ID从200开始，相册管理使用210

-- 1. 添加活动管理一级菜单（如果不存在）
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `remark`, `create_time`, `update_time`)
VALUES (200, '活动管理', 0, 3, '/activity', NULL, 1, 0, 'M', '0', '0', '', 'Calendar', '活动管理目录', NOW(), NOW())
ON DUPLICATE KEY UPDATE `menu_name` = '活动管理';

-- 2. 添加活动相册管理菜单
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `remark`, `create_time`, `update_time`)
VALUES (210, '活动相册', 200, 5, '/activity/photo', '/admin/activity/photo', 1, 0, 'C', '0', '0', 'activity:photo:list', 'Picture', '活动相册管理菜单', NOW(), NOW())
ON DUPLICATE KEY UPDATE `menu_name` = '活动相册';

-- 3. 添加相册管理的功能权限
-- 查询
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `remark`, `create_time`, `update_time`)
VALUES (2100, '相册查询', 210, 1, '', '', 1, 0, 'F', '0', '0', 'activity:photo:query', '#', '', NOW(), NOW())
ON DUPLICATE KEY UPDATE `menu_name` = '相册查询';

-- 删除
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `remark`, `create_time`, `update_time`)
VALUES (2101, '相册删除', 210, 2, '', '', 1, 0, 'F', '0', '0', 'activity:photo:remove', '#', '', NOW(), NOW())
ON DUPLICATE KEY UPDATE `menu_name` = '相册删除';

-- 批量删除
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `remark`, `create_time`, `update_time`)
VALUES (2102, '相册批量删除', 210, 3, '', '', 1, 0, 'F', '0', '0', 'activity:photo:batchRemove', '#', '', NOW(), NOW())
ON DUPLICATE KEY UPDATE `menu_name` = '相册批量删除';

-- 设置精选
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `remark`, `create_time`, `update_time`)
VALUES (2103, '设置精选', 210, 4, '', '', 1, 0, 'F', '0', '0', 'activity:photo:feature', '#', '', NOW(), NOW())
ON DUPLICATE KEY UPDATE `menu_name` = '设置精选';

-- 4. 为超级管理员角色分配菜单权限
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 200) ON DUPLICATE KEY UPDATE `role_id` = 1;
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 210) ON DUPLICATE KEY UPDATE `role_id` = 1;
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 2100) ON DUPLICATE KEY UPDATE `role_id` = 1;
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 2101) ON DUPLICATE KEY UPDATE `role_id` = 1;
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 2102) ON DUPLICATE KEY UPDATE `role_id` = 1;
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 2103) ON DUPLICATE KEY UPDATE `role_id` = 1;
