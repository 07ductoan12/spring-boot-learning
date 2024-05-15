package com.example;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** Person */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id @GeneratedValue private Long id;

    private String name;

    // Many to One Có nhiều người ở 1 địa điểm.
    @ManyToOne
    @JoinTable(name = "address_id") // thông qua khóa ngoại address_id
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Address address;
}
