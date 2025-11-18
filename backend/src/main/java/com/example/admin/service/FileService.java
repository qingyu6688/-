package com.example.admin.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    
    /**
     * 上传文件
     * @param file 文件
     * @return 文件访问URL
     */
    String uploadFile(MultipartFile file);
    
    /**
     * 删除文件
     * @param fileUrl 文件URL
     * @return 是否删除成功
     */
    boolean deleteFile(String fileUrl);
}
