package com.linlazy.mmorpg.utils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author linlazy
 */
public class RandomUtils {


    public static <E> E randomElement(List<E> elements){
        ThreadLocalRandom current = ThreadLocalRandom.current();
        int i = current.nextInt(0, elements.size());
        return elements.get(i);
    }

    public static <E> E randomElement(Collection<E> elements){
        List<E> collect = elements.stream().collect(Collectors.toList());
        ThreadLocalRandom current = ThreadLocalRandom.current();
        int i = current.nextInt(0, elements.size());
        return collect.get(i);
    }
}
