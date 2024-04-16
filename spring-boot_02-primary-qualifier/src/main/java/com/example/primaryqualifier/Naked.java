package com.example.primaryqualifier;

import org.springframework.stereotype.Component;

/** Naked */
@Component("naked")
public class Naked implements Outfit {

    @Override
    public void wear() {
        // TODO Auto-generated method stub
        System.out.println("Đang không mặc gì");
    }
}
