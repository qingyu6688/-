package com.example.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户登录DTO")
@Data
public class LoginDTO {
    
    @Schema(description = "用户名", required = true, example = "admin")
    private String username;
    
    @Schema(description = "密码", required = true, example = "123456")
    private String password;
}
