package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类（扩展）
 * 对应表：sys_user
 */
@Data
@TableName("sys_user")
public class User {
    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户名（唯一）
     */
    private String username;
    
    /**
     * 密码（MD5加密）
     */
    @JsonIgnore
    private String password;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 真实姓名
     */
    private String realName;
    
    /**
     * 学号（唯一）
     */
    private String studentNo;
    
    /**
     * 身份证号
     */
    @JsonIgnore
    private String idCard;
    
    /**
     * 性别（0男 1女 2未知）
     */
    private String sex;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 微信号
     */
    private String wechat;
    
    /**
     * QQ号
     */
    private String qq;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 入学年份
     */
    private Integer enrollmentYear;
    
    /**
     * 毕业年份
     */
    private Integer graduationYear;
    
    /**
     * 专业
     */
    private String major;
    
    /**
     * 班级
     */
    private String className;
    
    /**
     * 学院
     */
    private String college;
    
    /**
     * 用户类型（student在校生/graduate毕业生/teacher教师）
     */
    private String userType;
    
    /**
     * 角色ID
     */
    private Long roleId;
    
    /**
     * 状态（0禁用 1启用）
     */
    private Integer status;
    
    /**
     * 逻辑删除（0未删除 1已删除）
     */
    @TableLogic
    private Integer deleted;
    
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
    
    /**
     * 最后登录IP
     */
    private String lastLoginIp;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    // ========== 非数据库字段 ==========
    
    /**
     * 角色名称（关联查询）
     */
    @TableField(exist = false)
    private String roleName;
    
    /**
     * 角色标识（关联查询）
     */
    @TableField(exist = false)
    private String roleKey;
}
