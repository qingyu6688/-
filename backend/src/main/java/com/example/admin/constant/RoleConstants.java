package com.example.admin.constant;

/**
 * 角色常量
 */
public class RoleConstants {
    
    /**
     * 角色类型
     */
    public static final String ROLE_TYPE_SYSTEM = "system";    // 系统角色
    public static final String ROLE_TYPE_CLUB = "club";        // 社团角色
    public static final String ROLE_TYPE_CUSTOM = "custom";    // 自定义角色
    
    /**
     * 角色标识
     */
    public static final String ROLE_SUPER_ADMIN = "super_admin";           // 超级管理员
    public static final String ROLE_ADMIN = "admin";                       // 系统管理员
    public static final String ROLE_USER = "user";                         // 普通用户
    public static final String ROLE_CLUB_PRESIDENT = "club_president";    // 社团会长
    public static final String ROLE_CLUB_VICE_PRESIDENT = "club_vice_president";  // 社团副会长
    public static final String ROLE_DEPARTMENT_HEAD = "department_head";  // 部长
    public static final String ROLE_OFFICER = "officer";                  // 干事
    public static final String ROLE_CLUB_MEMBER = "club_member";          // 社团成员
    public static final String ROLE_FINANCE_ADMIN = "finance_admin";      // 财务管理员
    
    /**
     * 数据范围
     */
    public static final String DATA_SCOPE_ALL = "1";           // 全部数据权限
    public static final String DATA_SCOPE_CUSTOM = "2";        // 自定义数据权限
    public static final String DATA_SCOPE_DEPT = "3";          // 本部门数据权限
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4"; // 本部门及以下数据权限
    public static final String DATA_SCOPE_SELF = "5";          // 仅本人数据权限
    
    /**
     * 角色状态
     */
    public static final Integer STATUS_DISABLE = 0;  // 禁用
    public static final Integer STATUS_NORMAL = 1;   // 正常
    
    /**
     * 是否默认角色
     */
    public static final Integer IS_DEFAULT_YES = 1;  // 是
    public static final Integer IS_DEFAULT_NO = 0;   // 否
    
    /**
     * 权限标识
     */
    // 社团权限
    public static final String PERM_CLUB_ALL = "club:all";
    public static final String PERM_CLUB_VIEW = "club:view";
    public static final String PERM_CLUB_MANAGE = "club:manage";
    
    // 活动权限
    public static final String PERM_ACTIVITY_ALL = "activity:all";
    public static final String PERM_ACTIVITY_VIEW = "activity:view";
    public static final String PERM_ACTIVITY_MANAGE = "activity:manage";
    public static final String PERM_ACTIVITY_CREATE = "activity:create";
    public static final String PERM_ACTIVITY_JOIN = "activity:join";
    public static final String PERM_ACTIVITY_AUDIT = "activity:audit";
    
    // 成员权限
    public static final String PERM_MEMBER_ALL = "member:all";
    public static final String PERM_MEMBER_VIEW = "member:view";
    public static final String PERM_MEMBER_MANAGE = "member:manage";
    
    // 财务权限
    public static final String PERM_FINANCE_ALL = "finance:all";
    public static final String PERM_FINANCE_VIEW = "finance:view";
    public static final String PERM_FINANCE_MANAGE = "finance:manage";
    public static final String PERM_FINANCE_AUDIT = "finance:audit";
    
    // 帖子权限
    public static final String PERM_POST_CREATE = "post:create";
    public static final String PERM_POST_MANAGE = "post:manage";
    public static final String PERM_POST_AUDIT = "post:audit";
    
    // 资源权限
    public static final String PERM_RESOURCE_VIEW = "resource:view";
    public static final String PERM_RESOURCE_BOOKING = "resource:booking";
    public static final String PERM_RESOURCE_MANAGE = "resource:manage";
    
    // 报销权限
    public static final String PERM_REIMBURSEMENT_CREATE = "reimbursement:create";
    public static final String PERM_REIMBURSEMENT_AUDIT = "reimbursement:audit";
}
