package com.example.admin.annotation;

import java.lang.annotation.*;

/**
 * 防重复提交注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {
    
    /**
     * 间隔时间(秒)，小于此时间视为重复提交
     */
    int interval() default 5;
    
    /**
     * 提示消息
     */
    String message() default "操作过于频繁，请稍后再试";
}
