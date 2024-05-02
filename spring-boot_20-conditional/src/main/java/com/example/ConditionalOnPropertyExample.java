package com.example;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** ConditionalOnPropertyExample */
@Configuration
public class ConditionalOnPropertyExample {

    /**
     * @ConditionalOnProperty giúp gắn điều kiện cho @Bean dựa theo property trong config
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(
            value = "toan.bean2.enable",
            havingValue =
                    "true", // Nếu giá trị loda.bean2.enabled  = true thì Bean mới được khởi tạo
            matchIfMissing =
                    false) // matchIFMissing là giá trị mặc định nếu không tìm thấy property
                           // toan.bean2.enabled
    ABeanWithCondition2 aBeanWithCondition2() {
        return new ABeanWithCondition2();
    }
}
