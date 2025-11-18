package com.example.admin.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 内容审核工具类
 * 用于检测帖子和评论中的敏感词、违规内容
 */
public class ContentAuditUtil {

    // 敏感词库（实际项目中应该从数据库或配置文件加载）
    private static final Set<String> SENSITIVE_WORDS = new HashSet<>(Arrays.asList(
            // 政治敏感词
            "反动", "颠覆", "暴力", "恐怖", "分裂",
            // 色情低俗词
            "色情", "淫秽", "裸体", "性交", "卖淫",
            // 赌博相关
            "赌博", "博彩", "六合彩", "赌场",
            // 毒品相关
            "毒品", "大麻", "海洛因", "冰毒", "吸毒",
            // 诈骗相关
            "诈骗", "传销", "非法集资", "洗钱",
            // 违法广告
            "代开发票", "办证", "刻章", "假币",
            // 其他违规
            "自杀", "人肉", "暴力", "血腥"
    ));

    // URL正则表达式
    private static final Pattern URL_PATTERN = Pattern.compile(
            "(https?://)?([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?",
            Pattern.CASE_INSENSITIVE
    );

    // 手机号正则表达式
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "1[3-9]\\d{9}"
    );

    // QQ号正则表达式
    private static final Pattern QQ_PATTERN = Pattern.compile(
            "[1-9]\\d{4,10}"
    );

    /**
     * 审核内容是否合规
     * @param content 待审核内容
     * @return true-合规，false-违规
     */
    public static boolean auditContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }

        // 1. 检测敏感词
        if (containsSensitiveWords(content)) {
            return false;
        }

        // 2. 检测是否包含过多URL（防止广告）
        if (containsTooManyUrls(content)) {
            return false;
        }

        // 3. 检测是否包含联系方式（防止私下交易）
        if (containsContactInfo(content)) {
            return false;
        }

        // 4. 检测内容长度是否合理
        if (content.length() > 10000) {
            return false;
        }

        // 5. 检测是否全是重复字符
        if (isRepeatingContent(content)) {
            return false;
        }

        return true;
    }

    /**
     * 检测是否包含敏感词
     */
    private static boolean containsSensitiveWords(String content) {
        String lowerContent = content.toLowerCase();
        for (String word : SENSITIVE_WORDS) {
            if (lowerContent.contains(word.toLowerCase())) {
                System.out.println("检测到敏感词: " + word);
                return true;
            }
        }
        return false;
    }

    /**
     * 检测是否包含过多URL
     */
    private static boolean containsTooManyUrls(String content) {
        java.util.regex.Matcher matcher = URL_PATTERN.matcher(content);
        int count = 0;
        while (matcher.find()) {
            count++;
            if (count > 3) { // 最多允许3个URL
                System.out.println("检测到过多URL");
                return true;
            }
        }
        return false;
    }

    /**
     * 检测是否包含联系方式
     */
    private static boolean containsContactInfo(String content) {
        // 检测手机号
        java.util.regex.Matcher phoneMatcher = PHONE_PATTERN.matcher(content);
        if (phoneMatcher.find()) {
            System.out.println("检测到手机号");
            return true;
        }

        // 检测QQ号（如果内容中明确提到QQ）
        if (content.contains("QQ") || content.contains("qq")) {
            java.util.regex.Matcher qqMatcher = QQ_PATTERN.matcher(content);
            if (qqMatcher.find()) {
                System.out.println("检测到QQ号");
                return true;
            }
        }

        // 检测微信号
        if (content.contains("微信") || content.contains("WeChat") || content.contains("wechat")) {
            System.out.println("检测到微信号");
            return true;
        }

        return false;
    }

    /**
     * 检测是否为重复内容
     */
    private static boolean isRepeatingContent(String content) {
        if (content.length() < 10) {
            return false;
        }

        // 检测是否有超过10个连续重复的字符
        char[] chars = content.toCharArray();
        int repeatCount = 1;
        for (int i = 1; i < chars.length; i++) {
            if (chars[i] == chars[i - 1]) {
                repeatCount++;
                if (repeatCount > 10) {
                    System.out.println("检测到重复字符");
                    return true;
                }
            } else {
                repeatCount = 1;
            }
        }

        return false;
    }

    /**
     * 获取审核失败原因
     */
    public static String getAuditFailReason(String content) {
        if (content == null || content.trim().isEmpty()) {
            return "内容不能为空";
        }

        if (containsSensitiveWords(content)) {
            return "内容包含敏感词，请修改后重试";
        }

        if (containsTooManyUrls(content)) {
            return "内容包含过多链接，请删除部分链接";
        }

        if (containsContactInfo(content)) {
            return "内容包含联系方式，请删除后重试";
        }

        if (content.length() > 10000) {
            return "内容过长，请精简内容";
        }

        if (isRepeatingContent(content)) {
            return "内容存在大量重复字符";
        }

        return "内容审核通过";
    }
}
