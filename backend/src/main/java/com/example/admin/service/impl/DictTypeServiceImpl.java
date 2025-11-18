package com.example.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.admin.entity.DictType;
import com.example.admin.mapper.DictTypeMapper;
import com.example.admin.service.DictTypeService;
import org.springframework.stereotype.Service;

@Service
public class DictTypeServiceImpl extends ServiceImpl<DictTypeMapper, DictType> implements DictTypeService {
}
