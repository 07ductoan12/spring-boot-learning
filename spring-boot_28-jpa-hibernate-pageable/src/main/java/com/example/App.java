package com.example;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    private final UserRepository userRepository;

    @Bean
    CommandLineRunner run() {
        return args -> {
            // Lấy ra 5 user đầu tiên
            // PageRequest.of(0,5) tương đương với lấy ra page đầu tiên, và mỗi page sẽ có 5 phần tử
            Page<User> page = userRepository.findAll(PageRequest.of(0, 5));
            // In ra 5 user đầu tiên
            System.out.println("In ra 5 user đầu tiên: ");
            page.forEach(System.out::println);

            System.out.println("In ra số lượng user ở page hiện tại: " + page.getSize());
            System.out.println("In ra tổng số lượng user: " + page.getTotalElements());
            System.out.println("In ra tổng số page: " + page.getTotalPages());

            // Lấy ra 5 user ở page 1, sort theo tên
            page = userRepository.findAll(PageRequest.of(1, 5, Sort.by("name").descending()));
            System.out.println("In ra 5 user page 1, sắp xếp theo name descending:");
            page.forEach(System.out::println);

            // Custom method
            List<User> list = userRepository.findAllByNameLike("name-%", PageRequest.of(0, 5));
            System.out.println(list);
        };
    }
}
