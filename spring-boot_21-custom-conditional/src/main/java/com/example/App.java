package com.example;

import com.example.AppConfiguration.SomeBean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/** App */
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(App.class, args);

        try {
            SomeBean someBean = context.getBean(SomeBean.class);
            System.out.println("SomeBean tồn tại!");
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("SomeBean chỉ được tạo nếu chạy trên Window");
        }
    }
}
