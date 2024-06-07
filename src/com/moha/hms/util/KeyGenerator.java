package com.moha.hms.util;

import java.util.Random;

public class KeyGenerator {
    private static final Random RANDOM = new Random();

    public static String generateId() {
        long maxValue = Long.MAX_VALUE;
        long generatedId = (long) (RANDOM.nextDouble() * maxValue);
        return String.valueOf(generatedId);
    }
}
