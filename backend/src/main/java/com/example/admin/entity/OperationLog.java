package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_operation_log")
public class OperationLog {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String title;
    
    private Integer businessType;
    
    private String method;
    
    private String requestMethod;
    
    private Integer operatorType;
    
    private String operatorName;
    
    private String operatorUrl;
    
    private String operatorIp;
    
    private String operatorLocation;
    
    private String operatorParam;
    
    private String jsonResult;
    
    private Integer status;
    
    private String errorMsg;
    
    private LocalDateTime operatorTime;
    
    private Long costTime;
}
