package com.example.admin.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "统一响应结果")
@Data
public class Result<T> {
    
    @Schema(description = "状态码，200表示成功", example = "200")
    private Integer code;
    
    @Schema(description = "提示消息", example = "操作成功")
    private String message;
    
    @Schema(description = "返回数据")
    private T data;

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
