package com.example.admin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Jackson配置类
 * 配置日期时间格式的序列化和反序列化
 */
@Configuration
public class JacksonConfig {
    
    /** 日期时间格式 */
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    /** 日期格式 */
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    
    /** 时间格式 */
    private static final String TIME_FORMAT = "HH:mm:ss";
    
    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        
        // 配置JavaTimeModule
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        
        // LocalDateTime格式化
        javaTimeModule.addSerializer(LocalDateTime.class, 
            new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDateTime.class, 
            new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
        
        // LocalDate格式化
        javaTimeModule.addSerializer(LocalDate.class, 
            new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_FORMAT)));
        javaTimeModule.addDeserializer(LocalDate.class, 
            new LocalDateDeserializer(DateTimeFormatter.ofPattern(DATE_FORMAT)));
        
        // LocalTime格式化
        javaTimeModule.addSerializer(LocalTime.class, 
            new LocalTimeSerializer(DateTimeFormatter.ofPattern(TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalTime.class, 
            new LocalTimeDeserializer(DateTimeFormatter.ofPattern(TIME_FORMAT)));
        
        objectMapper.registerModule(javaTimeModule);
        
        // 禁用将日期写为时间戳
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        return objectMapper;
    }
}
