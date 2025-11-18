package com.example.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.admin.entity.DictData;
import com.example.admin.mapper.DictDataMapper;
import com.example.admin.service.DictDataService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements DictDataService {
    
    @Override
    public List<DictData> selectDictDataByType(String dictType) {
        QueryWrapper<DictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_type", dictType);
        queryWrapper.eq("status", "0");
        queryWrapper.orderByAsc("dict_sort");
        return this.list(queryWrapper);
    }
}
