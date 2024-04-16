package com.example.primaryqualifier;

import org.springframework.stereotype.Component;

/** Bikini */
@Component("bikini")
public class Bikini implements Outfit {

    @Override
    public void wear() {
        // TODO Auto-generated method stub
        System.out.println("Mặc bikini");
    }
}
