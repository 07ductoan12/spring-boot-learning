package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {
    @Autowired MyHouse myHouse;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    CommandLineRunner run() {
        return args -> {
            System.out.println(Thread.currentThread().getName() + ": Toan đi tới cửa nhà !!!");
            System.out.println(
                    Thread.currentThread().getName() + ": => Toan bấm chuông và khai báo họ tên!");
            // gõ cửa
            myHouse.rangDoorbellBy("Toan");
            System.out.println(Thread.currentThread().getName() + ": Toan quay lưng bỏ đi");
        };
    }
}
