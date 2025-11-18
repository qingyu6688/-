package com.example.admin.service.impl;

import com.example.admin.entity.CommunityNotice;
import com.example.admin.entity.CommunityNoticeRead;
import com.example.admin.mapper.CommunityNoticeMapper;
import com.example.admin.mapper.CommunityNoticeReadMapper;
import com.example.admin.service.CommunityNoticeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通知公告表 Service实现类
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Service
public class CommunityNoticeServiceImpl extends ServiceImpl<CommunityNoticeMapper, CommunityNotice> implements CommunityNoticeService {

    @Autowired
    private CommunityNoticeReadMapper noticeReadMapper;

    @Override
    public Page<CommunityNotice> getNoticePage(Integer pageNum, Integer pageSize, String title, String noticeType, String status) {
        QueryWrapper<CommunityNotice> wrapper = new QueryWrapper<>();
        
        if (title != null && !title.isEmpty()) {
            wrapper.like("title", title);
        }
        if (noticeType != null && !noticeType.isEmpty()) {
            wrapper.eq("notice_type", noticeType);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }
        
        wrapper.orderByDesc("is_top", "create_time");
        
        Page<CommunityNotice> page = new Page<>(pageNum, pageSize);
        return this.page(page, wrapper);
    }

    @Override
    public Page<CommunityNotice> getPublishedNotices(Integer pageNum, Integer pageSize, Long userId) {
        QueryWrapper<CommunityNotice> wrapper = new QueryWrapper<>();
        wrapper.eq("status", "1");
        wrapper.orderByDesc("is_top", "publish_time");
        
        Page<CommunityNotice> page = new Page<>(pageNum, pageSize);
        return this.page(page, wrapper);
    }

    @Override
    public void markAsRead(Long noticeId, Long userId) {
        // 检查是否已读
        QueryWrapper<CommunityNoticeRead> wrapper = new QueryWrapper<>();
        wrapper.eq("notice_id", noticeId);
        wrapper.eq("user_id", userId);
        
        Long count = noticeReadMapper.selectCount(wrapper);
        if (count == 0) {
            // 未读，添加阅读记录
            CommunityNoticeRead read = new CommunityNoticeRead();
            read.setNoticeId(noticeId);
            read.setUserId(userId);
            read.setReadTime(LocalDateTime.now());
            read.setUpdateTime(LocalDateTime.now());
            noticeReadMapper.insert(read);
            
            // 更新阅读数
            CommunityNotice notice = this.getById(noticeId);
            if (notice != null) {
                notice.setReadCount((notice.getReadCount() == null ? 0 : notice.getReadCount()) + 1);
                this.updateById(notice);
            }
        }
    }

    @Override
    public void publishNotice(Long id) {
        CommunityNotice notice = this.getById(id);
        if (notice != null) {
            notice.setStatus("1");
            if (notice.getPublishTime() == null) {
                notice.setPublishTime(LocalDateTime.now());
            }
            this.updateById(notice);
        }
    }

    @Override
    public void withdrawNotice(Long id) {
        CommunityNotice notice = this.getById(id);
        if (notice != null) {
            notice.setStatus("2");
            this.updateById(notice);
        }
    }

    @Override
    public void toggleTop(Long id, Integer isTop) {
        CommunityNotice notice = this.getById(id);
        if (notice != null) {
            notice.setIsTop(isTop);
            this.updateById(notice);
        }
    }

    @Override
    public int getUnreadCount(Long userId) {
        // 获取所有已发布的公告
        QueryWrapper<CommunityNotice> noticeWrapper = new QueryWrapper<>();
        noticeWrapper.eq("status", "1");
        List<CommunityNotice> notices = this.list(noticeWrapper);
        
        int unreadCount = 0;
        for (CommunityNotice notice : notices) {
            // 检查是否已读
            QueryWrapper<CommunityNoticeRead> readWrapper = new QueryWrapper<>();
            readWrapper.eq("notice_id", notice.getId());
            readWrapper.eq("user_id", userId);
            
            Long count = noticeReadMapper.selectCount(readWrapper);
            if (count == 0) {
                unreadCount++;
            }
        }
        
        return unreadCount;
    }

    @Override
    public Map<String, Object> getReadStatistics(Long id) {
        Map<String, Object> result = new HashMap<>();
        
        CommunityNotice notice = this.getById(id);
        if (notice == null) {
            return result;
        }
        
        // 总阅读数
        int totalRead = notice.getReadCount() == null ? 0 : notice.getReadCount();
        result.put("totalRead", totalRead);
        
        // 计算阅读率
        String readRate = "0%";
        if (notice.getTargetUsers() != null && !notice.getTargetUsers().isEmpty()) {
            // 如果指定了目标用户，根据目标用户数计算
            // 这里简化处理，假设目标用户数为100
            int targetUserCount = 100;
            if (totalRead > 0) {
                double rate = (double) totalRead / targetUserCount * 100;
                readRate = String.format("%.1f%%", Math.min(rate, 100.0));
            }
        } else {
            // 全部用户，假设总用户数为200
            int totalUserCount = 200;
            if (totalRead > 0) {
                double rate = (double) totalRead / totalUserCount * 100;
                readRate = String.format("%.1f%%", Math.min(rate, 100.0));
            }
        }
        result.put("readRate", readRate);
        
        // 阅读用户列表
        QueryWrapper<CommunityNoticeRead> wrapper = new QueryWrapper<>();
        wrapper.eq("notice_id", id);
        wrapper.orderByDesc("read_time");
        List<CommunityNoticeRead> readers = noticeReadMapper.selectList(wrapper);
        
        result.put("readers", readers);
        
        return result;
    }
}
