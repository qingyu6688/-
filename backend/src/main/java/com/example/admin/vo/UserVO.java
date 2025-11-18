package com.example.admin.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户VO（视图对象）
 */
@Data
public class UserVO {
    private Long id;
    private String username;
    private String nickname;
    private String realName;
    private String studentNo;
    private String sex;
    private String email;
    private String phone;
    private String wechat;
    private String qq;
    private String avatar;
    private Integer enrollmentYear;
    private Integer graduationYear;
    private String major;
    private String className;
    private String college;
    private String userType;
    private Long roleId;
    private String roleKey;
    private String roleName;
    private Integer status;
    private LocalDateTime lastLoginTime;
    private String lastLoginIp;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
