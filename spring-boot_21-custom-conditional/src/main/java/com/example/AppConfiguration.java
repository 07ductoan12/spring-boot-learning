package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/** AppConfiguration */
@Configuration
public class AppConfiguration {
    public static class SomeBean {}

    // @Conditional(WindowRequired.class)
    @ConditionalOnWindow
    // @Conditional(WindowOrLinuxRequired.class)
    @Conditional(LinuxRequired.class)
    @Bean
    SomeBean someBean() {
        return new SomeBean();
    }
}
