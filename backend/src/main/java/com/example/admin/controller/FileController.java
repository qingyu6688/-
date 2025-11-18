package com.example.admin.controller;

import com.example.admin.common.Result;
import com.example.admin.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public Result<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = fileService.uploadFile(file);
            Map<String, String> data = new HashMap<>();
            data.put("url", fileUrl);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/delete")
    public Result<Void> delete(@RequestParam("url") String url) {
        try {
            boolean success = fileService.deleteFile(url);
            if (success) {
                return Result.success();
            } else {
                return Result.error("文件删除失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
