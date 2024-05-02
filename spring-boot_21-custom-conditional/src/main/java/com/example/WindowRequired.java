package com.example;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/** WindowRequired Một điều kiện, phải kế thừa lớp Condition của Spring Boot cung cấp */
public class WindowRequired implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // Nếu OS ra window trả ra true.
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
