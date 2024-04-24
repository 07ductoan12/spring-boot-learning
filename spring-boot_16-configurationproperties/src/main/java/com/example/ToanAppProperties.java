package com.example;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/** ToanAppProperties */
@Data
@Component // Là 1 spring bean
// @PropertySource("classpath:toan.yml") // Đánh dấu để lấy config từ trong file toan.yml
@ConfigurationProperties(prefix = "toan") // Chỉ lấy các config có tiền tố là "toan"
public class ToanAppProperties {
    private String email;
    private String googleAnalyticsId;
    private List<String> authors;
    private Map<String, String> exampleMap;
}
