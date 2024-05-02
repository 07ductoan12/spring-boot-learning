package com.example;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** ConditionalOnBeanExample */
@Configuration
public class ConditionalOnBeanExample {

    @Bean
    RandomBean randomBean() {
        return new RandomBean();
    }

    /**
     * ABeanWithCondition chỉ được tạo ra, khi RandomBean tồn tại trong Context.
     *
     * @return
     */
    @Bean
    @ConditionalOnBean(RandomBean.class)
    ABeanWithCondition aBeanWithCondition() {
        return new ABeanWithCondition();
    }
}
