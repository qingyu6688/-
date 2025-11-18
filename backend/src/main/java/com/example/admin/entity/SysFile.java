package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_file")
public class SysFile {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String fileName;
    
    private String originalName;
    
    private String filePath;
    
    private String fileUrl;
    
    private String fileType;
    
    private Long fileSize;
    
    private String fileExt;
    
    private String uploadUser;
    
    private LocalDateTime uploadTime;
    
    private String remark;
}
