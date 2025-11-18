package com.example.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.admin.entity.SysFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface SysFileMapper extends BaseMapper<SysFile> {
    
    /**
     * 统计文件信息
     */
    @Select("SELECT " +
            "COUNT(*) as totalCount, " +
            "COALESCE(SUM(file_size), 0) as totalSize, " +
            "COUNT(CASE WHEN file_type = 'image' THEN 1 END) as imageCount, " +
            "COUNT(CASE WHEN file_type = 'document' THEN 1 END) as documentCount, " +
            "COUNT(CASE WHEN file_type = 'video' THEN 1 END) as videoCount, " +
            "COUNT(CASE WHEN file_type = 'other' THEN 1 END) as otherCount " +
            "FROM sys_file")
    Map<String, Object> getFileStatistics();
}
