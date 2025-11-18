package com.example.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.admin.entity.OperationLog;
import com.example.admin.mapper.OperationLogMapper;
import com.example.admin.service.OperationLogService;
import org.springframework.stereotype.Service;

@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {
}
