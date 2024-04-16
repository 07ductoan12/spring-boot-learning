package com.example.primaryqualifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/** Girl */
@Component
public class Girl {

    @Autowired Outfit outfit;

    public Girl(@Qualifier("naked") Outfit outfit) {
        this.outfit = outfit;
    }
}
