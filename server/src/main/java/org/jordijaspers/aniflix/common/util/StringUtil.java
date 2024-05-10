package org.jordijaspers.aniflix.common.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for string operations.
 */
@SuppressWarnings("PMD.ShortVariable")
public final class StringUtil {

    private static final Map<Character, Integer> ROMAN_MAP = new ConcurrentHashMap<>();

    static {
        ROMAN_MAP.put('I', 1);
        ROMAN_MAP.put('V', 5);
        ROMAN_MAP.put('X', 10);
        ROMAN_MAP.put('L', 50);
        ROMAN_MAP.put('C', 100);
        ROMAN_MAP.put('D', 500);
        ROMAN_MAP.put('M', 1000);
    }

    private StringUtil() {
        // Utility class
    }

    public static String toInteger(final String roman) {
        final int number = roman.chars()
                .map(i -> ROMAN_MAP.get((char) i))
                .reduce(0, (a, b) -> a < b ? b - a : a + b);
        return String.valueOf(number);
    }
}
