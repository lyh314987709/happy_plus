package com.happy.util;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class Kit {

    public static String contain(Set<String> set, String key) {
        return set.stream().filter(item -> contain(item, key)).findFirst().orElse(key);
    }

    public static Boolean contain(String message, String key) {
        return ImmutableSet.copyOf(message.split(" ")).stream().anyMatch(item -> key.contains(item));
    }


}
