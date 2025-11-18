package com.example.admin.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * XSS过滤器
 */
@Component
public class XssFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(httpRequest);
        chain.doFilter(xssRequest, response);
    }
}
