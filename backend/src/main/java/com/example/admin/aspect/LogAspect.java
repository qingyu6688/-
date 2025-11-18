package com.example.admin.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.admin.annotation.Log;
import com.example.admin.entity.OperationLog;
import com.example.admin.service.OperationLogService;
import com.example.admin.util.JwtUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 操作日志记录处理
 */
@Aspect
@Component
public class LogAspect {
    
    @Autowired
    private OperationLogService operationLogService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 处理完请求后执行
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }
    
    /**
     * 拦截异常操作
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }
    
    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
        try {
            long startTime = System.currentTimeMillis();
            
            // 获取当前的request
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            
            // 创建操作日志对象
            OperationLog operationLog = new OperationLog();
            operationLog.setStatus(0);
            operationLog.setOperatorTime(LocalDateTime.now());
            
            // 请求的地址
            String ip = getIpAddr(request);
            operationLog.setOperatorIp(ip);
            operationLog.setOperatorUrl(request.getRequestURI());
            
            // 获取用户名
            String token = request.getHeader("Authorization");
            if (StrUtil.isNotBlank(token) && token.startsWith("Bearer ")) {
                token = token.substring(7);
                try {
                    String username = jwtUtil.getUsernameFromToken(token);
                    operationLog.setOperatorName(username);
                } catch (Exception ex) {
                    // Token解析失败，忽略
                }
            }
            
            if (e != null) {
                operationLog.setStatus(1);
                operationLog.setErrorMsg(StrUtil.sub(e.getMessage(), 0, 2000));
            }
            
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operationLog.setMethod(className + "." + methodName + "()");
            
            // 设置请求方式
            operationLog.setRequestMethod(request.getMethod());
            
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, operationLog, jsonResult);
            
            // 计算消耗时间
            long endTime = System.currentTimeMillis();
            operationLog.setCostTime(endTime - startTime);
            
            // 保存数据库
            operationLogService.save(operationLog);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
    
    /**
     * 获取注解中对方法的描述信息
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, OperationLog operationLog, Object jsonResult) {
        // 设置action动作
        operationLog.setBusinessType(log.businessType());
        // 设置标题
        operationLog.setTitle(log.title());
        // 设置操作人类别
        operationLog.setOperatorType(log.operatorType());
        
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            setRequestValue(joinPoint, operationLog);
        }
        
        // 是否需要保存response，参数和值
        if (log.isSaveResponseData() && jsonResult != null) {
            operationLog.setJsonResult(StrUtil.sub(JSONUtil.toJsonStr(jsonResult), 0, 2000));
        }
    }
    
    /**
     * 获取请求的参数，放到log中
     */
    private void setRequestValue(JoinPoint joinPoint, OperationLog operationLog) {
        String params = "";
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            for (Object arg : args) {
                if (arg != null && !(arg instanceof HttpServletRequest)) {
                    try {
                        params = JSONUtil.toJsonStr(arg);
                    } catch (Exception e) {
                        params = arg.toString();
                    }
                }
            }
        }
        operationLog.setOperatorParam(StrUtil.sub(params, 0, 2000));
    }
    
    /**
     * 获取客户端IP
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
}
