package com.example.admin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "file.upload")
public class FileUploadConfig {
    
    /**
     * 文件上传路径
     */
    private String path = "uploads/";
    
    /**
     * 文件访问路径前缀
     */
    private String prefix = "/uploads/";
    
    /**
     * 允许的文件类型
     */
    private String[] allowedTypes = {"jpg", "jpeg", "png", "gif"};
    
    /**
     * 最大文件大小（字节）
     */
    private long maxSize = 2 * 1024 * 1024; // 2MB
}
