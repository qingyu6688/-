package com.example.admin.exception;

import com.example.admin.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.error("业务异常: URI={}, Message={}", request.getRequestURI(), e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.error("参数校验异常: URI={}, Message={}", request.getRequestURI(), message);
        return Result.error(400, "参数校验失败: " + message);
    }

    /**
     * 参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e, HttpServletRequest request) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.error("参数绑定异常: URI={}, Message={}", request.getRequestURI(), message);
        return Result.error(400, "参数绑定失败: " + message);
    }

    /**
     * 参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> handleTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.error("参数类型不匹配: URI={}, Parameter={}, Message={}", 
                request.getRequestURI(), e.getName(), e.getMessage());
        return Result.error(400, "参数类型错误: " + e.getName());
    }

    /**
     * 权限不足异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result<Void> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        log.error("权限不足: URI={}, Message={}", request.getRequestURI(), e.getMessage());
        return Result.error(403, "权限不足，无法访问");
    }

    /**
     * 空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public Result<Void> handleNullPointerException(NullPointerException e, HttpServletRequest request) {
        log.error("空指针异常: URI={}", request.getRequestURI(), e);
        return Result.error(500, "系统内部错误，请联系管理员");
    }

    /**
     * 数据库异常
     */
    @ExceptionHandler(SQLException.class)
    public Result<Void> handleSQLException(SQLException e, HttpServletRequest request) {
        log.error("数据库异常: URI={}, SQLState={}, ErrorCode={}", 
                request.getRequestURI(), e.getSQLState(), e.getErrorCode(), e);
        return Result.error(500, "数据库操作失败，请稍后重试");
    }

    /**
     * 非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.error("非法参数异常: URI={}, Message={}", request.getRequestURI(), e.getMessage());
        return Result.error(400, e.getMessage());
    }

    /**
     * 运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("运行时异常: URI={}", request.getRequestURI(), e);
        return Result.error(500, "系统运行异常: " + e.getMessage());
    }

    /**
     * 通用异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e, HttpServletRequest request) {
        log.error("未知异常: URI={}", request.getRequestURI(), e);
        return Result.error(500, "系统异常，请联系管理员");
    }
}
