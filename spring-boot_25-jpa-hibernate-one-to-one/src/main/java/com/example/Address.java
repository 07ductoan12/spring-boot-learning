package com.example;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import lombok.Builder;
import lombok.Data;

/** Address */
@Entity
@Data
@Builder
public class Address {
    @Id @GeneratedValue private Long id;

    private String city;
    private String province;

    @OneToOne
    @JoinColumn(name = "person_id")
    private Person person;
}
