package com.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.admin.entity.DictData;

import java.util.List;

public interface DictDataService extends IService<DictData> {
    
    /**
     * 根据字典类型查询字典数据
     */
    List<DictData> selectDictDataByType(String dictType);
}
