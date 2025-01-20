package org.zerock.knock.component.util;

import org.springframework.stereotype.Component;
import org.zerock.knock.dto.dictionary.Dictionary;

import java.util.Random;

@Component
public class RandomNickNameMaker {

    public String makeRandomNickName()
    {

        Dictionary dictionary = new Dictionary();

        Random random = new Random();

        String adjective = dictionary.adjective[random.nextInt(dictionary.adjective.length - 1)];
        String animal = dictionary.animal[random.nextInt(dictionary.animal.length - 1)];
        String number = String.valueOf(random.nextInt(999));

        return adjective + " " + animal + number;
    }
}
