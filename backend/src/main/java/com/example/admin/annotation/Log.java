package com.example.admin.annotation;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    
    /**
     * 模块标题
     */
    String title() default "";
    
    /**
     * 业务类型（0其它 1新增 2修改 3删除 4查询 5导出 6导入）
     */
    int businessType() default 0;
    
    /**
     * 操作人类别
     */
    int operatorType() default 1;
    
    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default true;
    
    /**
     * 是否保存响应的参数
     */
    boolean isSaveResponseData() default true;
}
