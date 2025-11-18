package com.example.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.admin.entity.SysFile;
import com.example.admin.mapper.SysFileMapper;
import com.example.admin.service.SysFileService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements SysFileService {
    
    @Override
    public Map<String, Object> getFileStatistics() {
        return baseMapper.getFileStatistics();
    }
}
