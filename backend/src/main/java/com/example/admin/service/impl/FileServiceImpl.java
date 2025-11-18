package com.example.admin.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.example.admin.config.FileUploadConfig;
import com.example.admin.entity.SysFile;
import com.example.admin.mapper.SysFileMapper;
import com.example.admin.service.FileService;
import com.example.admin.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileUploadConfig fileUploadConfig;
    
    @Autowired
    private SysFileMapper sysFileMapper;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }

        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        if (StrUtil.isBlank(originalFilename)) {
            throw new RuntimeException("文件名不能为空");
        }

        // 获取文件扩展名
        String extension = FileUtil.extName(originalFilename).toLowerCase();
        
        // 验证文件类型
        boolean isAllowed = false;
        for (String allowedType : fileUploadConfig.getAllowedTypes()) {
            if (allowedType.equalsIgnoreCase(extension)) {
                isAllowed = true;
                break;
            }
        }
        if (!isAllowed) {
            throw new RuntimeException("不支持的文件类型: " + extension);
        }

        // 验证文件大小
        if (file.getSize() > fileUploadConfig.getMaxSize()) {
            throw new RuntimeException("文件大小超过限制: " + (fileUploadConfig.getMaxSize() / 1024 / 1024) + "MB");
        }

        try {
            // 按日期创建子目录
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String uploadPath = fileUploadConfig.getPath() + datePath;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 生成唯一文件名
            String newFilename = IdUtil.simpleUUID() + "." + extension;
            String filePath = uploadPath + File.separator + newFilename;

            // 保存文件
            file.transferTo(new File(filePath));

            // 返回访问URL
            String fileUrl = fileUploadConfig.getPrefix() + datePath + "/" + newFilename;
            
            // 保存文件记录到数据库
            saveFileRecord(originalFilename, newFilename, filePath, fileUrl, extension, file.getSize());
            
            return fileUrl;
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteFile(String fileUrl) {
        if (StrUtil.isBlank(fileUrl)) {
            return false;
        }

        try {
            // 从URL中提取文件路径
            String filePath = fileUrl.replace(fileUploadConfig.getPrefix(), fileUploadConfig.getPath());
            File file = new File(filePath);
            
            if (file.exists() && file.isFile()) {
                return file.delete();
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 保存文件记录
     */
    private void saveFileRecord(String originalName, String fileName, String filePath, 
                                String fileUrl, String extension, long fileSize) {
        try {
            SysFile sysFile = new SysFile();
            sysFile.setFileName(fileName);
            sysFile.setOriginalName(originalName);
            sysFile.setFilePath(filePath);
            sysFile.setFileUrl(fileUrl);
            sysFile.setFileExt(extension);
            sysFile.setFileSize(fileSize);
            sysFile.setFileType(getFileType(extension));
            sysFile.setUploadTime(LocalDateTime.now());
            
            // 获取当前用户
            String username = getCurrentUsername();
            sysFile.setUploadUser(username);
            
            sysFileMapper.insert(sysFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 根据扩展名判断文件类型
     */
    private String getFileType(String extension) {
        if (extension == null) return "other";
        
        String ext = extension.toLowerCase();
        if (ext.matches("jpg|jpeg|png|gif|bmp|webp|svg")) {
            return "image";
        } else if (ext.matches("doc|docx|xls|xlsx|ppt|pptx|pdf|txt|csv")) {
            return "document";
        } else if (ext.matches("mp4|avi|mov|wmv|flv|mkv")) {
            return "video";
        } else if (ext.matches("mp3|wav|flac|aac")) {
            return "audio";
        } else if (ext.matches("zip|rar|7z|tar|gz")) {
            return "archive";
        } else if (ext.matches("exe|msi|apk|dmg")) {
            return "executable";
        } else {
            return "other";
        }
    }
    
    /**
     * 获取当前用户名
     */
    private String getCurrentUsername() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String token = request.getHeader("Authorization");
                System.out.println("Token from header: " + token);
                
                if (StrUtil.isNotBlank(token)) {
                    if (token.startsWith("Bearer ")) {
                        token = token.substring(7);
                    }
                    String username = jwtUtil.getUsernameFromToken(token);
                    System.out.println("Parsed username: " + username);
                    if (StrUtil.isNotBlank(username)) {
                        return username;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting username: " + e.getMessage());
            e.printStackTrace();
        }
        return "system";
    }
}
