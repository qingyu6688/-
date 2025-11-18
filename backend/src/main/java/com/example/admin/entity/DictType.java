package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_dict_type")
public class DictType {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String dictName;
    
    private String dictType;
    
    private String status;
    
    private String remark;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
}
