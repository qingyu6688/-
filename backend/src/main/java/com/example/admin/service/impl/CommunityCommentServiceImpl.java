package com.example.admin.service.impl;

import com.example.admin.entity.CommunityComment;
import com.example.admin.mapper.CommunityCommentMapper;
import com.example.admin.service.CommunityCommentService;
import com.example.admin.utils.ContentAuditUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 评论表 Service实现类
 * @author CodeGenerator
 * @date 2025-11-17
 */
@Service
public class CommunityCommentServiceImpl extends ServiceImpl<CommunityCommentMapper, CommunityComment> implements CommunityCommentService {

    /**
     * 保存评论（带自动审核）
     */
    @Override
    public boolean save(CommunityComment entity) {
        // 自动审核评论内容
        String content = entity.getContent() != null ? entity.getContent() : "";
        
        boolean isPass = ContentAuditUtil.auditContent(content);
        
        if (!isPass) {
            // 审核不通过，直接拒绝保存，不允许发布违规评论
            String reason = ContentAuditUtil.getAuditFailReason(content);
            System.out.println("❌ 评论自动审核未通过，拒绝保存");
            System.out.println("   原因: " + reason);
            System.out.println("   内容: " + (content.length() > 50 ? content.substring(0, 50) + "..." : content));
            
            // 抛出异常，阻止保存，并提供详细的违规信息
            String errorMessage = "您的评论审核未通过！\n" +
                                "原因：" + reason + "\n\n" +
                                "请修改评论内容后重新发布。";
            throw new RuntimeException(errorMessage);
        }
        
        // 审核通过，设置为正常状态
        entity.setStatus("0"); // 0-正常显示
        System.out.println("✅ 评论自动审核通过，允许发布");
        
        // 设置默认值
        if (entity.getLikeCount() == null) {
            entity.setLikeCount(0);
        }
        
        return super.save(entity);
    }
}
