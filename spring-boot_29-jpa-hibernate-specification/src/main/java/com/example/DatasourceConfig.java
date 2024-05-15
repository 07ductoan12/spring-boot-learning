package com.example;

import com.example.User.UserType;

import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/** DatasourceConfig */
@Configuration
@RequiredArgsConstructor
public class DatasourceConfig {
    private final UserRepotitory userRepotitory;

    @PostConstruct
    public void initData() {
        final Random r = new Random();
        userRepotitory.saveAll(
                IntStream.range(0, 100)
                        .mapToObj(
                                i ->
                                        User.builder()
                                                .name("name-" + i)
                                                .type(
                                                        r.nextDouble() >= 0.5
                                                                ? UserType.VIP
                                                                : UserType.NORMAL)
                                                .build())
                        .collect(Collectors.toList()));
    }
}
