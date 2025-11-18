package com.example.admin.dto;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 用户DTO（数据传输对象）
 */
@Data
public class UserDTO {
    
    private Long id;
    
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    private String password;
    
    private String nickname;
    
    @NotBlank(message = "真实姓名不能为空")
    private String realName;
    
    private String studentNo;
    
    private String idCard;
    
    private String sex;
    
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
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
    
    private Integer status;
    
    private String remark;
}
