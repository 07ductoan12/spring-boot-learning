package com.example;

import org.springframework.stereotype.Repository;

/** GirlRepositoryImpl */
@Repository
public class GirlRepositoryImpl implements GirlRepository {

    @Override
    public Girl getGirlName(String name) {
        // TODO Auto-generated method stub
        return new Girl(name);
    }
}
