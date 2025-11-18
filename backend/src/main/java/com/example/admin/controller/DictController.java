package com.example.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.annotation.Log;
import com.example.admin.common.Result;
import com.example.admin.entity.DictData;
import com.example.admin.entity.DictType;
import com.example.admin.service.DictDataService;
import com.example.admin.service.DictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dict")
public class DictController {
    
    @Autowired
    private DictTypeService dictTypeService;
    
    @Autowired
    private DictDataService dictDataService;
    
    // ==================== 字典类型管理 ====================
    
    /**
     * 分页查询字典类型列表
     */
    @GetMapping("/type/list")
    public Result<Page<DictType>> getDictTypeList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String dictName,
            @RequestParam(required = false) String dictType) {
        
        QueryWrapper<DictType> queryWrapper = new QueryWrapper<>();
        if (dictName != null && !dictName.isEmpty()) {
            queryWrapper.like("dict_name", dictName);
        }
        if (dictType != null && !dictType.isEmpty()) {
            queryWrapper.like("dict_type", dictType);
        }
        queryWrapper.orderByDesc("create_time");
        
        Page<DictType> page = new Page<>(pageNum, pageSize);
        Page<DictType> result = dictTypeService.page(page, queryWrapper);
        return Result.success(result);
    }
    
    /**
     * 查询所有字典类型
     */
    @GetMapping("/type/all")
    public Result<List<DictType>> getAllDictTypes() {
        List<DictType> list = dictTypeService.list();
        return Result.success(list);
    }
    
    /**
     * 新增字典类型
     */
    @Log(title = "字典类型", businessType = 1)
    @PostMapping("/type")
    public Result<Void> addDictType(@RequestBody DictType dictType) {
        try {
            dictTypeService.save(dictType);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 修改字典类型
     */
    @Log(title = "字典类型", businessType = 2)
    @PutMapping("/type")
    public Result<Void> updateDictType(@RequestBody DictType dictType) {
        try {
            dictTypeService.updateById(dictType);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除字典类型
     */
    @Log(title = "字典类型", businessType = 3)
    @DeleteMapping("/type/{id}")
    public Result<Void> deleteDictType(@PathVariable Long id) {
        try {
            dictTypeService.removeById(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    // ==================== 字典数据管理 ====================
    
    /**
     * 分页查询字典数据列表
     */
    @GetMapping("/data/list")
    public Result<Page<DictData>> getDictDataList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String dictType,
            @RequestParam(required = false) String dictLabel) {
        
        QueryWrapper<DictData> queryWrapper = new QueryWrapper<>();
        if (dictType != null && !dictType.isEmpty()) {
            queryWrapper.eq("dict_type", dictType);
        }
        if (dictLabel != null && !dictLabel.isEmpty()) {
            queryWrapper.like("dict_label", dictLabel);
        }
        queryWrapper.orderByAsc("dict_sort");
        
        Page<DictData> page = new Page<>(pageNum, pageSize);
        Page<DictData> result = dictDataService.page(page, queryWrapper);
        return Result.success(result);
    }
    
    /**
     * 根据字典类型查询字典数据
     */
    @GetMapping("/data/type/{dictType}")
    public Result<List<DictData>> getDictDataByType(@PathVariable String dictType) {
        try {
            List<DictData> list = dictDataService.selectDictDataByType(dictType);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 新增字典数据
     */
    @Log(title = "字典数据", businessType = 1)
    @PostMapping("/data")
    public Result<Void> addDictData(@RequestBody DictData dictData) {
        try {
            dictDataService.save(dictData);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 修改字典数据
     */
    @Log(title = "字典数据", businessType = 2)
    @PutMapping("/data")
    public Result<Void> updateDictData(@RequestBody DictData dictData) {
        try {
            dictDataService.updateById(dictData);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除字典数据
     */
    @Log(title = "字典数据", businessType = 3)
    @DeleteMapping("/data/{id}")
    public Result<Void> deleteDictData(@PathVariable Long id) {
        try {
            dictDataService.removeById(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
