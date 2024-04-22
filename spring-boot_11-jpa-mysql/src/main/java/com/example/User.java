package com.example;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

/** User */
@Entity
@Data
@Table(name = "user")
public class User {
    private static long serialVersionUID = -297553281792804396L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hp")
    private int hp;

    @Column(name = "stamina")
    private int stamina;

    private int atk;
    private int def;
    private int agi;
}