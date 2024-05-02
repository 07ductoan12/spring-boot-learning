package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/** App */
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(App.class, args);

        try {
            ABeanWithCondition aBeanWithCondition = context.getBean(ABeanWithCondition.class);
            System.out.println("ABeanWithCondition tồn tại!");
        } catch (Exception e) {
            System.out.println(
                    "Bean ABeanWithCondition.class chỉ tồn tại khi RandomBean.class tồn tại");
        }

        try {
            ABeanWithCondition2 aBeanWithCondition2 = context.getBean(ABeanWithCondition2.class);
            System.out.println("ABeanWithCondition2 tồn tại!");

        } catch (Exception e) {
            System.out.println(
                    "Bean ABeanWithCondition2.class chỉ tồn tại khi loda.bean2.enabled=true tồn"
                            + " tại");
        }

        try {
            ConditionalOnResourceExample conditionalOnResourceExample =
                    context.getBean(ConditionalOnResourceExample.class);
            System.out.println("ConditionalOnResourceExample tồn tại!");

        } catch (Exception e) {
            System.out.println(
                    "Bean ConditionalOnResourceExample.class chỉ tồn tại khi application.properties"
                        + " tồn tại");
        }
    }
}
