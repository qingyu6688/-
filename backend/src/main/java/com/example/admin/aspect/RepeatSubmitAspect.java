package com.example.admin.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.example.admin.annotation.RepeatSubmit;
import com.example.admin.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 防重复提交切面
 */
@Aspect
@Component
public class RepeatSubmitAspect {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    // 使用内存存储，生产环境建议使用Redis
    private static final Map<String, Long> SUBMIT_RECORDS = new ConcurrentHashMap<>();
    
    @Around("@annotation(repeatSubmit)")
    public Object around(ProceedingJoinPoint point, RepeatSubmit repeatSubmit) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return point.proceed();
        }
        
        HttpServletRequest request = attributes.getRequest();
        
        // 获取用户标识
        String userKey = getUserKey(request);
        if (StrUtil.isBlank(userKey)) {
            userKey = request.getRemoteAddr(); // 使用IP作为备用标识
        }
        
        // 获取请求URI和参数
        String uri = request.getRequestURI();
        String params = getRequestParams(point.getArgs());
        
        // 生成唯一key
        String submitKey = SecureUtil.md5(userKey + ":" + uri + ":" + params);
        
        // 检查是否重复提交
        Long lastSubmitTime = SUBMIT_RECORDS.get(submitKey);
        long currentTime = System.currentTimeMillis();
        
        if (lastSubmitTime != null) {
            long interval = (currentTime - lastSubmitTime) / 1000;
            if (interval < repeatSubmit.interval()) {
                throw new RuntimeException(repeatSubmit.message());
            }
        }
        
        // 记录本次提交时间
        SUBMIT_RECORDS.put(submitKey, currentTime);
        
        // 清理过期记录（简单实现，生产环境建议使用Redis的过期机制）
        cleanExpiredRecords(repeatSubmit.interval());
        
        return point.proceed();
    }
    
    /**
     * 获取用户标识
     */
    private String getUserKey(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (StrUtil.isNotBlank(token) && token.startsWith("Bearer ")) {
                token = token.substring(7);
                return jwtUtil.getUsernameFromToken(token);
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }
    
    /**
     * 获取请求参数
     */
    private String getRequestParams(Object[] args) {
        if (args == null || args.length == 0) {
            return "";
        }
        StringBuilder params = new StringBuilder();
        for (Object arg : args) {
            if (arg != null && !(arg instanceof HttpServletRequest)) {
                params.append(arg.toString());
            }
        }
        return params.toString();
    }
    
    /**
     * 清理过期记录
     */
    private void cleanExpiredRecords(int interval) {
        long currentTime = System.currentTimeMillis();
        SUBMIT_RECORDS.entrySet().removeIf(entry -> 
            (currentTime - entry.getValue()) / 1000 > interval * 2
        );
    }
}
