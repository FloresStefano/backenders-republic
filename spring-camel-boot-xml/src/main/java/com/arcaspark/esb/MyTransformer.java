package com.arcaspark.esb;


import org.springframework.stereotype.Component;


/**
 * A sample transform
 */
@Component(value = "myTransformer")
public class MyTransformer {

    public String transform() {
        // let's return a random string
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            int number = (int) (Math.round(Math.random() * 1000) % 10);
            char letter = (char) ('0' + number);
            buffer.append(letter);
        }
        return buffer.toString();
    }

}