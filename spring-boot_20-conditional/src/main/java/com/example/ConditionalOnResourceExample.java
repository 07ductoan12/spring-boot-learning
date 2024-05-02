package com.example;

import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Configuration;

/**
 * ConditionalOnResourceExample Nếu Spring Boot không tìm thấy file application.properties thì class
 * này không được tạo
 */
@Configuration
@ConditionalOnResource(resources = "/application.properties")
public class ConditionalOnResourceExample {}
