package com.example.helloworld;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Girl */
@Component
public class Girl {

    @Autowired Outfit outfit;

    public Outfit getOutfit() {
        return outfit;
    }
}
