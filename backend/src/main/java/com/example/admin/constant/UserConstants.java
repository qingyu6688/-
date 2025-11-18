package com.example.admin.constant;

/**
 * 用户常量
 */
public class UserConstants {
    
    /**
     * 用户类型
     */
    public static final String USER_TYPE_STUDENT = "student";      // 在校生
    public static final String USER_TYPE_GRADUATE = "graduate";    // 毕业生
    public static final String USER_TYPE_TEACHER = "teacher";      // 教师
    
    /**
     * 性别
     */
    public static final String SEX_MALE = "0";      // 男
    public static final String SEX_FEMALE = "1";    // 女
    public static final String SEX_UNKNOWN = "2";   // 未知
    
    /**
     * 用户状态
     */
    public static final Integer STATUS_DISABLE = 0;  // 禁用
    public static final Integer STATUS_NORMAL = 1;   // 正常
    
    /**
     * 删除标志
     */
    public static final Integer DELETED_YES = 1;    // 已删除
    public static final Integer DELETED_NO = 0;     // 未删除
    
    /**
     * 默认密码
     */
    public static final String DEFAULT_PASSWORD = "123456";
}
