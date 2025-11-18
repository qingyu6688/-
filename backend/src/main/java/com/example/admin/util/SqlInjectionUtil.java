package com.example.admin.util;

import java.util.regex.Pattern;

/**
 * SQL注入防护工具类
 */
public class SqlInjectionUtil {
    
    // SQL注入关键字正则
    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile(
        "(?i)(\\b(select|insert|update|delete|drop|create|alter|exec|execute|script|javascript|eval)\\b)|" +
        "(--|;|/\\*|\\*/|xp_|sp_|0x|\\bor\\b|\\band\\b)",
        Pattern.CASE_INSENSITIVE
    );
    
    /**
     * 检查字符串是否包含SQL注入风险
     */
    public static boolean isSqlInjection(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        return SQL_INJECTION_PATTERN.matcher(value).find();
    }
    
    /**
     * 清理SQL注入风险字符
     */
    public static String cleanSqlInjection(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        
        // 移除危险字符
        value = value.replaceAll("'", "''");  // 单引号转义
        value = value.replaceAll("--", "");   // 移除注释
        value = value.replaceAll("/\\*", ""); // 移除多行注释开始
        value = value.replaceAll("\\*/", ""); // 移除多行注释结束
        value = value.replaceAll(";", "");    // 移除分号
        
        return value;
    }
    
    /**
     * 验证排序字段，防止SQL注入
     */
    public static boolean isValidOrderBy(String orderBy) {
        if (orderBy == null || orderBy.isEmpty()) {
            return true;
        }
        
        // 只允许字母、数字、下划线、逗号、空格和ASC/DESC
        Pattern validPattern = Pattern.compile("^[a-zA-Z0-9_,\\s]+(\\s+(ASC|DESC))?$", Pattern.CASE_INSENSITIVE);
        return validPattern.matcher(orderBy.trim()).matches();
    }
}
