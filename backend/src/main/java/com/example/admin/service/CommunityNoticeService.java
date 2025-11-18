package com.example.admin.service;

import com.example.admin.entity.CommunityNotice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

/**
 * 通知公告表 Service接口
 * @author CodeGenerator
 * @date 2025-11-17
 */
public interface CommunityNoticeService extends IService<CommunityNotice> {

    /**
     * 分页查询公告
     */
    Page<CommunityNotice> getNoticePage(Integer pageNum, Integer pageSize, String title, String noticeType, String status);

    /**
     * 获取已发布的公告列表
     */
    Page<CommunityNotice> getPublishedNotices(Integer pageNum, Integer pageSize, Long userId);

    /**
     * 标记为已读
     */
    void markAsRead(Long noticeId, Long userId);

    /**
     * 发布公告
     */
    void publishNotice(Long id);

    /**
     * 撤回公告
     */
    void withdrawNotice(Long id);

    /**
     * 置顶/取消置顶
     */
    void toggleTop(Long id, Integer isTop);

    /**
     * 获取未读公告数
     */
    int getUnreadCount(Long userId);

    /**
     * 获取阅读统计
     */
    Map<String, Object> getReadStatistics(Long id);
}
