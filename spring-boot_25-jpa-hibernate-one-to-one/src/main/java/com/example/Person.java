package com.example;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import lombok.Builder;
import lombok.Data;

/** Person */
@Entity // Hibernate Entity
@Data // Lombok
@Builder // Lombok
public class Person {
    @Id @GeneratedValue private Long id;
    private String name;
}
