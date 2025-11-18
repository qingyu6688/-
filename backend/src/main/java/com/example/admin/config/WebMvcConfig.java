package com.example.admin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private FileUploadConfig fileUploadConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 确保上传目录存在
        File uploadDir = new File(fileUploadConfig.getPath());
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        // 配置静态资源映射
        registry.addResourceHandler(fileUploadConfig.getPrefix() + "**")
                .addResourceLocations("file:" + uploadDir.getAbsolutePath() + File.separator);
    }
}
