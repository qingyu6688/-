package com.example.admin.controller;

import com.example.admin.common.Result;
import com.example.admin.entity.CommunityMessage;
import com.example.admin.entity.User;
import com.example.admin.mapper.UserMapper;
import com.example.admin.service.CommunityMessageService;
import com.example.admin.utils.ContentAuditUtil;
import com.example.admin.vo.MessageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 消息通知 Controller
 */
@Tag(name = "消息通知", description = "消息通知相关接口")
@RestController
@RequestMapping("/api/communityMessage")
public class CommunityMessageController {

    @Autowired
    private CommunityMessageService messageService;
    
    @Autowired
    private UserMapper userMapper;

    /**
     * 获取用户消息列表
     */
    @Operation(summary = "获取消息列表", description = "分页查询用户消息列表")
    @GetMapping("/list")
    public Result<Page<MessageVO>> list(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String messageType) {
        
        Page<CommunityMessage> page = new Page<>(pageNum, pageSize);
        
        LambdaQueryWrapper<CommunityMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityMessage::getReceiverId, userId);
        
        if (messageType != null && !messageType.isEmpty()) {
            wrapper.eq(CommunityMessage::getMessageType, messageType);
        }
        
        wrapper.orderByDesc(CommunityMessage::getCreateTime);
        
        Page<CommunityMessage> result = messageService.page(page, wrapper);
        
        // 转换为MessageVO并填充发送人信息
        List<MessageVO> messageVOList = result.getRecords().stream().map(message -> {
            MessageVO messageVO = new MessageVO();
            BeanUtils.copyProperties(message, messageVO);
            
            // 查询发送人信息
            if (message.getSenderId() != null) {
                User sender = userMapper.selectById(message.getSenderId());
                if (sender != null) {
                    messageVO.setSenderName(sender.getNickname() != null ? sender.getNickname() : sender.getUsername());
                    messageVO.setSenderAvatar(sender.getAvatar());
                }
            }
            
            return messageVO;
        }).collect(Collectors.toList());
        
        Page<MessageVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(messageVOList);
        
        return Result.success(voPage);
    }

    /**
     * 获取未读消息数量
     */
    @Operation(summary = "获取未读数量", description = "获取用户未读消息数量")
    @GetMapping("/unreadCount")
    public Result<Integer> getUnreadCount(@RequestParam Long userId) {
        int count = messageService.getUnreadCount(userId);
        return Result.success(count);
    }

    /**
     * 标记消息为已读
     */
    @Operation(summary = "标记已读", description = "标记单条消息为已读")
    @PutMapping("/{id}/read")
    public Result<Void> markAsRead(@PathVariable Long id) {
        CommunityMessage message = messageService.getById(id);
        if (message != null) {
            message.setIsRead(1);
            message.setReadTime(LocalDateTime.now());
            messageService.updateById(message);
            return Result.success();
        }
        return Result.error("消息不存在");
    }

    /**
     * 标记所有消息为已读
     */
    @Operation(summary = "全部已读", description = "标记用户所有消息为已读")
    @PutMapping("/readAll")
    public Result<Void> markAllAsRead(@RequestParam Long userId) {
        LambdaQueryWrapper<CommunityMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityMessage::getReceiverId, userId)
               .eq(CommunityMessage::getIsRead, 0);
        
        List<CommunityMessage> messages = messageService.list(wrapper);
        messages.forEach(message -> {
            message.setIsRead(1);
            message.setReadTime(LocalDateTime.now());
        });
        
        messageService.updateBatchById(messages);
        return Result.success();
    }

    /**
     * 删除消息
     */
    @Operation(summary = "删除消息", description = "删除单条消息")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        messageService.removeById(id);
        return Result.success();
    }

    /**
     * 获取消息统计
     */
    @Operation(summary = "消息统计", description = "获取各类消息数量统计")
    @GetMapping("/statistics")
    public Result<Map<String, Integer>> getStatistics(@RequestParam Long userId) {
        Map<String, Integer> stats = new HashMap<>();
        
        LambdaQueryWrapper<CommunityMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityMessage::getReceiverId, userId);
        
        // 总消息数
        stats.put("total", (int) messageService.count(wrapper));
        
        // 未读消息数
        wrapper.clear();
        wrapper.eq(CommunityMessage::getReceiverId, userId)
               .eq(CommunityMessage::getIsRead, 0);
        stats.put("unread", (int) messageService.count(wrapper));
        
        // 点赞消息数（通过内容匹配）
        wrapper.clear();
        wrapper.eq(CommunityMessage::getReceiverId, userId)
               .eq(CommunityMessage::getMessageType, "notice")
               .like(CommunityMessage::getContent, "赞了你的");
        stats.put("like", (int) messageService.count(wrapper));
        
        // 收藏消息数（通过内容匹配）
        wrapper.clear();
        wrapper.eq(CommunityMessage::getReceiverId, userId)
               .eq(CommunityMessage::getMessageType, "notice")
               .like(CommunityMessage::getContent, "收藏了你的");
        stats.put("collect", (int) messageService.count(wrapper));
        
        // 评论消息数（通过内容匹配）
        wrapper.clear();
        wrapper.eq(CommunityMessage::getReceiverId, userId)
               .eq(CommunityMessage::getMessageType, "notice")
               .like(CommunityMessage::getContent, "评论了你的");
        stats.put("comment", (int) messageService.count(wrapper));
        
        return Result.success(stats);
    }

    /**
     * 发送私信
     */
    @Operation(summary = "发送私信", description = "用户之间发送私信")
    @PostMapping("/sendChat")
    public Result<CommunityMessage> sendChat(@RequestBody CommunityMessage message) {
        // 审核私信内容
        String content = message.getContent() != null ? message.getContent() : "";
        boolean isPass = ContentAuditUtil.auditContent(content);
        
        if (!isPass) {
            String reason = ContentAuditUtil.getAuditFailReason(content);
            System.out.println("❌ 私信内容审核未通过");
            System.out.println("   原因: " + reason);
            System.out.println("   内容: " + (content.length() > 50 ? content.substring(0, 50) + "..." : content));
            
            return Result.error("您的消息审核未通过！\n原因：" + reason + "\n\n请修改内容后重新发送。");
        }
        
        message.setMessageType("chat");
        message.setContentType("text");
        message.setIsRead(0);
        message.setIsWithdraw(0);
        message.setCreateTime(LocalDateTime.now());
        message.setUpdateTime(LocalDateTime.now());
        messageService.save(message);
        
        System.out.println("✅ 私信内容审核通过，发送成功");
        return Result.success(message);
    }

    /**
     * 获取聊天列表（会话列表）
     */
    @Operation(summary = "获取聊天列表", description = "获取用户的所有聊天会话")
    @GetMapping("/chatList")
    public Result<List<Map<String, Object>>> getChatList(@RequestParam Long userId) {
        // 查询所有与该用户相关的消息
        LambdaQueryWrapper<CommunityMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityMessage::getMessageType, "chat")
               .and(w -> w.eq(CommunityMessage::getSenderId, userId)
                         .or()
                         .eq(CommunityMessage::getReceiverId, userId))
               .orderByDesc(CommunityMessage::getCreateTime);
        
        List<CommunityMessage> messages = messageService.list(wrapper);
        
        // 按对话人分组
        Map<Long, Map<String, Object>> chatMap = new HashMap<>();
        for (CommunityMessage msg : messages) {
            Long otherUserId = msg.getSenderId().equals(userId) ? msg.getReceiverId() : msg.getSenderId();
            
            if (!chatMap.containsKey(otherUserId)) {
                User otherUser = userMapper.selectById(otherUserId);
                if (otherUser != null) {
                    Map<String, Object> chat = new HashMap<>();
                    chat.put("userId", otherUserId);
                    chat.put("userName", otherUser.getNickname() != null ? otherUser.getNickname() : otherUser.getUsername());
                    chat.put("userAvatar", otherUser.getAvatar());
                    chat.put("lastMessage", msg.getContent());
                    chat.put("lastTime", msg.getCreateTime());
                    
                    // 计算未读数
                    LambdaQueryWrapper<CommunityMessage> unreadWrapper = new LambdaQueryWrapper<>();
                    unreadWrapper.eq(CommunityMessage::getMessageType, "chat")
                                .eq(CommunityMessage::getSenderId, otherUserId)
                                .eq(CommunityMessage::getReceiverId, userId)
                                .eq(CommunityMessage::getIsRead, 0);
                    int unreadCount = (int) messageService.count(unreadWrapper);
                    chat.put("unreadCount", unreadCount);
                    
                    chatMap.put(otherUserId, chat);
                }
            }
        }
        
        List<Map<String, Object>> chatList = new ArrayList<>(chatMap.values());
        // 按最后消息时间排序
        chatList.sort((a, b) -> {
            LocalDateTime timeA = (LocalDateTime) a.get("lastTime");
            LocalDateTime timeB = (LocalDateTime) b.get("lastTime");
            return timeB.compareTo(timeA);
        });
        
        return Result.success(chatList);
    }

    /**
     * 获取与某人的聊天记录
     */
    @Operation(summary = "获取聊天记录", description = "获取与指定用户的聊天记录")
    @GetMapping("/chatHistory")
    public Result<List<MessageVO>> getChatHistory(
            @RequestParam Long userId,
            @RequestParam Long otherUserId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "50") Integer pageSize) {
        
        Page<CommunityMessage> page = new Page<>(pageNum, pageSize);
        
        LambdaQueryWrapper<CommunityMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityMessage::getMessageType, "chat")
               .and(w -> w.and(w1 -> w1.eq(CommunityMessage::getSenderId, userId)
                                       .eq(CommunityMessage::getReceiverId, otherUserId))
                         .or(w2 -> w2.eq(CommunityMessage::getSenderId, otherUserId)
                                     .eq(CommunityMessage::getReceiverId, userId)))
               .orderByAsc(CommunityMessage::getCreateTime);
        
        Page<CommunityMessage> result = messageService.page(page, wrapper);
        
        // 转换为MessageVO
        List<MessageVO> messageVOList = result.getRecords().stream().map(message -> {
            MessageVO messageVO = new MessageVO();
            BeanUtils.copyProperties(message, messageVO);
            
            // 查询发送人信息
            if (message.getSenderId() != null) {
                User sender = userMapper.selectById(message.getSenderId());
                if (sender != null) {
                    messageVO.setSenderName(sender.getNickname() != null ? sender.getNickname() : sender.getUsername());
                    messageVO.setSenderAvatar(sender.getAvatar());
                }
            }
            
            return messageVO;
        }).collect(Collectors.toList());
        
        // 标记对方发来的消息为已读
        LambdaQueryWrapper<CommunityMessage> unreadWrapper = new LambdaQueryWrapper<>();
        unreadWrapper.eq(CommunityMessage::getMessageType, "chat")
                    .eq(CommunityMessage::getSenderId, otherUserId)
                    .eq(CommunityMessage::getReceiverId, userId)
                    .eq(CommunityMessage::getIsRead, 0);
        
        List<CommunityMessage> unreadMessages = messageService.list(unreadWrapper);
        if (!unreadMessages.isEmpty()) {
            unreadMessages.forEach(msg -> {
                msg.setIsRead(1);
                msg.setReadTime(LocalDateTime.now());
            });
            messageService.updateBatchById(unreadMessages);
        }
        
        return Result.success(messageVOList);
    }

    /**
     * 获取未读私信数量
     */
    @Operation(summary = "未读私信数", description = "获取用户未读私信数量")
    @GetMapping("/unreadChatCount")
    public Result<Integer> getUnreadChatCount(@RequestParam Long userId) {
        LambdaQueryWrapper<CommunityMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityMessage::getMessageType, "chat")
               .eq(CommunityMessage::getReceiverId, userId)
               .eq(CommunityMessage::getIsRead, 0);
        int count = (int) messageService.count(wrapper);
        return Result.success(count);
    }

    /**
     * 撤回消息
     */
    @Operation(summary = "撤回消息", description = "撤回2分钟内的消息")
    @PutMapping("/{id}/withdraw")
    public Result<Void> withdrawMessage(@PathVariable Long id, @RequestParam Long userId) {
        CommunityMessage message = messageService.getById(id);
        if (message == null) {
            return Result.error("消息不存在");
        }
        
        // 检查是否是发送人
        if (!message.getSenderId().equals(userId)) {
            return Result.error("只能撤回自己发送的消息");
        }
        
        // 检查是否在2分钟内
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createTime = message.getCreateTime();
        long minutes = java.time.Duration.between(createTime, now).toMinutes();
        
        if (minutes > 2) {
            return Result.error("只能撤回2分钟内的消息");
        }
        
        // 标记为已撤回
        message.setIsWithdraw(1);
        message.setUpdateTime(now);
        messageService.updateById(message);
        
        return Result.success();
    }

    /**
     * 发送图片消息
     */
    @Operation(summary = "发送图片", description = "发送图片消息")
    @PostMapping("/sendImage")
    public Result<CommunityMessage> sendImage(@RequestBody CommunityMessage message) {
        message.setMessageType("chat");
        message.setContentType("image");
        message.setIsRead(0);
        message.setIsWithdraw(0);
        message.setCreateTime(LocalDateTime.now());
        message.setUpdateTime(LocalDateTime.now());
        messageService.save(message);
        return Result.success(message);
    }

    /**
     * 发送文件消息
     */
    @Operation(summary = "发送文件", description = "发送文件消息")
    @PostMapping("/sendFile")
    public Result<CommunityMessage> sendFile(@RequestBody CommunityMessage message) {
        message.setMessageType("chat");
        message.setContentType("file");
        message.setIsRead(0);
        message.setIsWithdraw(0);
        message.setCreateTime(LocalDateTime.now());
        message.setUpdateTime(LocalDateTime.now());
        messageService.save(message);
        return Result.success(message);
    }

    /**
     * 管理端：获取聊天统计数据
     */
    @Operation(summary = "聊天统计", description = "获取聊天管理统计数据")
    @GetMapping("/admin/statistics")
    public Result<Map<String, Object>> getAdminStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 消息总数
        LambdaQueryWrapper<CommunityMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityMessage::getMessageType, "chat");
        long totalMessages = messageService.count(wrapper);
        stats.put("totalMessages", totalMessages);
        
        // 今日消息数
        wrapper.clear();
        wrapper.eq(CommunityMessage::getMessageType, "chat")
               .ge(CommunityMessage::getCreateTime, LocalDateTime.now().toLocalDate().atStartOfDay());
        long todayMessages = messageService.count(wrapper);
        stats.put("todayMessages", todayMessages);
        
        // 活跃用户数（今日发送过消息的用户）
        wrapper.clear();
        wrapper.eq(CommunityMessage::getMessageType, "chat")
               .ge(CommunityMessage::getCreateTime, LocalDateTime.now().toLocalDate().atStartOfDay())
               .select(CommunityMessage::getSenderId)
               .groupBy(CommunityMessage::getSenderId);
        List<CommunityMessage> activeUserMessages = messageService.list(wrapper);
        stats.put("activeUsers", activeUserMessages.size());
        
        // 被举报消息数（这里简化处理，实际应该有举报表）
        // 暂时返回撤回消息数作为参考
        wrapper.clear();
        wrapper.eq(CommunityMessage::getMessageType, "chat")
               .eq(CommunityMessage::getIsWithdraw, 1);
        long reportedMessages = messageService.count(wrapper);
        stats.put("reportedMessages", reportedMessages);
        
        return Result.success(stats);
    }

    /**
     * 管理端：分页查询所有聊天消息
     */
    @Operation(summary = "消息列表", description = "管理端分页查询所有聊天消息")
    @GetMapping("/admin/list")
    public Result<Page<MessageVO>> getAdminMessageList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String senderName,
            @RequestParam(required = false) String receiverName,
            @RequestParam(required = false) String messageType,
            @RequestParam(required = false) String status) {
        
        Page<CommunityMessage> page = new Page<>(pageNum, pageSize);
        
        LambdaQueryWrapper<CommunityMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityMessage::getMessageType, "chat");
        
        // 根据消息类型筛选
        if (messageType != null && !messageType.isEmpty()) {
            wrapper.eq(CommunityMessage::getContentType, messageType);
        }
        
        // 根据状态筛选
        if (status != null && !status.isEmpty()) {
            if ("0".equals(status)) {
                // 正常消息
                wrapper.eq(CommunityMessage::getIsWithdraw, 0);
            } else if ("1".equals(status)) {
                // 已撤回
                wrapper.eq(CommunityMessage::getIsWithdraw, 1);
            }
        }
        
        wrapper.orderByDesc(CommunityMessage::getCreateTime);
        
        Page<CommunityMessage> result = messageService.page(page, wrapper);
        
        // 转换为MessageVO并填充用户信息
        List<MessageVO> messageVOList = result.getRecords().stream().map(message -> {
            MessageVO messageVO = new MessageVO();
            BeanUtils.copyProperties(message, messageVO);
            
            // 查询发送人信息
            if (message.getSenderId() != null) {
                User sender = userMapper.selectById(message.getSenderId());
                if (sender != null) {
                    String senderNameStr = sender.getNickname() != null ? sender.getNickname() : sender.getUsername();
                    messageVO.setSenderName(senderNameStr);
                    messageVO.setSenderAvatar(sender.getAvatar());
                    
                    // 如果有发送者名称筛选，进行过滤
                    if (senderName != null && !senderName.isEmpty() && !senderNameStr.contains(senderName)) {
                        return null;
                    }
                }
            }
            
            // 查询接收人信息
            if (message.getReceiverId() != null) {
                User receiver = userMapper.selectById(message.getReceiverId());
                if (receiver != null) {
                    String receiverNameStr = receiver.getNickname() != null ? receiver.getNickname() : receiver.getUsername();
                    messageVO.setReceiverName(receiverNameStr);
                    messageVO.setReceiverAvatar(receiver.getAvatar());
                    
                    // 如果有接收者名称筛选，进行过滤
                    if (receiverName != null && !receiverName.isEmpty() && !receiverNameStr.contains(receiverName)) {
                        return null;
                    }
                }
            }
            
            // 设置状态
            if (message.getIsWithdraw() == 1) {
                messageVO.setStatus("1"); // 已撤回
            } else {
                messageVO.setStatus("0"); // 正常
            }
            
            return messageVO;
        }).filter(vo -> vo != null).collect(Collectors.toList());
        
        Page<MessageVO> voPage = new Page<>(result.getCurrent(), result.getSize());
        voPage.setRecords(messageVOList);
        voPage.setTotal(messageVOList.size());
        
        return Result.success(voPage);
    }

    /**
     * 管理端：删除消息
     */
    @Operation(summary = "删除消息", description = "管理端删除聊天消息")
    @DeleteMapping("/admin/{id}")
    public Result<Void> adminDeleteMessage(@PathVariable Long id) {
        messageService.removeById(id);
        return Result.success();
    }
}
