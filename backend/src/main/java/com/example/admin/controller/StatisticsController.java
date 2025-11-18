package com.example.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.admin.common.Result;
import com.example.admin.entity.*;
import com.example.admin.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 统计分析控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/statistics")
@Tag(name = "统计分析", description = "数据统计和分析功能")
public class StatisticsController {

    @Autowired
    private AlumniContactExtendService alumniContactExtendService;

    @Autowired
    private CommunityPostService communityPostService;

    @Autowired
    private CommunityCommentService communityCommentService;

    @Autowired
    private ActivityInfoService activityInfoService;

    @Autowired
    private ActivityRegistrationService activityRegistrationService;

    @Autowired
    private ActivitySignInService activitySignInService;

    @Autowired
    private ActivityEvaluationService activityEvaluationService;

    /**
     * 获取数据概览
     */
    @Operation(summary = "获取数据概览")
    @GetMapping("/overview")
    public Result<Map<String, Object>> getOverview() {
        Map<String, Object> overview = new HashMap<>();

        // 成员统计
        long totalMembers = alumniContactExtendService.count();
        overview.put("totalMembers", totalMembers);

        // 帖子统计
        long totalPosts = communityPostService.count();
        overview.put("totalPosts", totalPosts);

        // 评论统计
        long totalComments = communityCommentService.count();
        overview.put("totalComments", totalComments);

        // 活动统计
        long totalActivities = activityInfoService.count();
        overview.put("totalActivities", totalActivities);

        // 活动参与人次
        LambdaQueryWrapper<ActivityRegistration> regWrapper = new LambdaQueryWrapper<>();
        regWrapper.eq(ActivityRegistration::getAuditStatus, "1");
        long totalParticipants = activityRegistrationService.count(regWrapper);
        overview.put("totalParticipants", totalParticipants);

        return Result.success(overview);
    }

    /**
     * 获取活动分析数据
     */
    @Operation(summary = "获取活动分析数据")
    @GetMapping("/activity-analysis")
    public Result<Map<String, Object>> getActivityAnalysis() {
        Map<String, Object> analysis = new HashMap<>();

        // 活动类型分布
        List<ActivityInfo> activities = activityInfoService.list();
        Map<String, Long> typeDistribution = new HashMap<>();
        for (ActivityInfo activity : activities) {
            String type = activity.getActivityType();
            typeDistribution.put(type, typeDistribution.getOrDefault(type, 0L) + 1);
        }
        analysis.put("typeDistribution", typeDistribution);

        // 活动状态分布
        Map<String, Long> statusDistribution = new HashMap<>();
        for (ActivityInfo activity : activities) {
            String status = activity.getStatus();
            statusDistribution.put(status, statusDistribution.getOrDefault(status, 0L) + 1);
        }
        analysis.put("statusDistribution", statusDistribution);

        // 平均评分
        List<ActivityEvaluation> evaluations = activityEvaluationService.list();
        double avgRating = evaluations.stream()
                .mapToInt(ActivityEvaluation::getRating)
                .average()
                .orElse(0.0);
        analysis.put("avgRating", Math.round(avgRating * 10) / 10.0);

        // 参与率统计
        long totalRegistrations = activityRegistrationService.count();
        long approvedRegistrations = activityRegistrationService.count(
                new LambdaQueryWrapper<ActivityRegistration>()
                        .eq(ActivityRegistration::getAuditStatus, "1")
        );
        double participationRate = totalRegistrations > 0 
                ? (double) approvedRegistrations / totalRegistrations * 100 
                : 0;
        analysis.put("participationRate", Math.round(participationRate * 10) / 10.0);

        // 热门活动TOP5
        List<Map<String, Object>> topActivities = new ArrayList<>();
        activities.stream()
                .sorted((a, b) -> Integer.compare(
                        b.getCurrentParticipants() != null ? b.getCurrentParticipants() : 0,
                        a.getCurrentParticipants() != null ? a.getCurrentParticipants() : 0
                ))
                .limit(5)
                .forEach(activity -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("name", activity.getActivityName());
                    item.put("participants", activity.getCurrentParticipants());
                    topActivities.add(item);
                });
        analysis.put("topActivities", topActivities);

        return Result.success(analysis);
    }


    /**
     * 获取成员分析数据
     */
    @Operation(summary = "获取成员分析数据")
    @GetMapping("/member-analysis")
    public Result<Map<String, Object>> getMemberAnalysis() {
        Map<String, Object> analysis = new HashMap<>();

        // 成员总数
        long totalMembers = alumniContactExtendService.count();
        analysis.put("totalMembers", totalMembers);

        // 活跃成员（参与过活动的）
        long activeMembers = activityRegistrationService.count(
                new LambdaQueryWrapper<ActivityRegistration>()
                        .eq(ActivityRegistration::getAuditStatus, "1")
                        .groupBy(ActivityRegistration::getUserId)
        );
        analysis.put("activeMembers", activeMembers);

        // 成员增长趋势（最近7天）
        List<Map<String, Object>> growthTrend = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            Map<String, Object> item = new HashMap<>();
            LocalDateTime date = LocalDateTime.now().minusDays(i);
            item.put("date", date.toLocalDate().toString());
            // 这里简化处理，实际应该查询每天的新增数
            item.put("count", totalMembers / 7);
            growthTrend.add(item);
        }
        analysis.put("growthTrend", growthTrend);

        return Result.success(analysis);
    }


    /**
     * 获取综合统计数据（用于首页）
     */
    @Operation(summary = "获取综合统计数据")
    @GetMapping("/dashboard")
    public Result<Map<String, Object>> getDashboard() {
        Map<String, Object> dashboard = new HashMap<>();

        // 核心数据
        dashboard.put("totalMembers", alumniContactExtendService.count());
        dashboard.put("totalPosts", communityPostService.count());
        dashboard.put("totalActivities", activityInfoService.count());
        dashboard.put("totalComments", communityCommentService.count());

        // 待处理事项
        long pendingPosts = communityPostService.count(
                new LambdaQueryWrapper<CommunityPost>()
                        .eq(CommunityPost::getAuditStatus, "0")
        );
        long pendingActivities = activityInfoService.count(
                new LambdaQueryWrapper<ActivityInfo>()
                        .eq(ActivityInfo::getAuditStatus, "0")
        );

        Map<String, Long> pendingTasks = new HashMap<>();
        pendingTasks.put("posts", pendingPosts);
        pendingTasks.put("activities", pendingActivities);
        dashboard.put("pendingTasks", pendingTasks);

        // 最近活动
        List<ActivityInfo> recentActivities = activityInfoService.list(
                new LambdaQueryWrapper<ActivityInfo>()
                        .orderByDesc(ActivityInfo::getCreateTime)
                        .last("LIMIT 5")
        );
        dashboard.put("recentActivities", recentActivities);

        return Result.success(dashboard);
    }
}
