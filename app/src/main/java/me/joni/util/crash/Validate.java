package me.joni.util.crash;

/**
 * Created by niqiang on 16/3/24.
 */
public class Validate {
    static void isTrue(final boolean expression, final String message, final Object... values) {
        if (expression == false) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }
}
