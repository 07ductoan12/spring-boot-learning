package com.example.config;

import com.example.model.TodoValidator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** TodoConfig */
@Configuration
public class TodoConfig {
    /**
     * Tạo ra Bean TodoValidator để sử dụng
     *
     * @return
     */
    @Bean
    public TodoValidator validator() {
        return new TodoValidator();
    }
}
