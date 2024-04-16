package com.example.helloworld;

import org.springframework.stereotype.Component;

/** Jean */
@Component
public class Jean implements Outfit {

    @Override
    public void wear() {
        // TODO Auto-generated method stub
        System.out.println("Mặc Jean");
    }
}
