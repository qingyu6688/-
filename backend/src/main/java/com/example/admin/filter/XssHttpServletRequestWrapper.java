package com.example.admin.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.web.util.HtmlUtils;

/**
 * XSS请求包装器
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }
    
    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        return cleanXSS(value);
    }
    
    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }
        String[] cleanValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            cleanValues[i] = cleanXSS(values[i]);
        }
        return cleanValues;
    }
    
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return cleanXSS(value);
    }
    
    /**
     * 清理XSS
     */
    private String cleanXSS(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        
        // 使用Spring的HtmlUtils进行HTML转义
        value = HtmlUtils.htmlEscape(value);
        
        // 额外的XSS防护规则
        value = value.replaceAll("<script", "&lt;script");
        value = value.replaceAll("</script>", "&lt;/script&gt;");
        value = value.replaceAll("<iframe", "&lt;iframe");
        value = value.replaceAll("</iframe>", "&lt;/iframe&gt;");
        value = value.replaceAll("javascript:", "");
        value = value.replaceAll("onerror=", "");
        value = value.replaceAll("onclick=", "");
        value = value.replaceAll("onload=", "");
        
        return value;
    }
}
