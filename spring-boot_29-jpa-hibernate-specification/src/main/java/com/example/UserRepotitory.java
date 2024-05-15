package com.example;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/** UserRepotitory */
public interface UserRepotitory extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {}
