package com.linlazy.mmorpglintingyang.utils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {


    public static <E> E randomElement(List<E> elements){
        ThreadLocalRandom current = ThreadLocalRandom.current();
        int i = current.nextInt(0, elements.size());
        return elements.get(i);
    }
}
