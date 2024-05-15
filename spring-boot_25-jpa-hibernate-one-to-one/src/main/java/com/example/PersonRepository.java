package com.example;

import org.springframework.data.jpa.repository.JpaRepository;

/** PersonRepository */
public interface PersonRepository extends JpaRepository<Person, Long> {}
