package org.longmetal.util;

public class Math {
    /**
     * Limits a value between two numbers
     * @param value The value to limit
     * @param min The minimum value. If {@code value} is less than this, this will be returned
     * @param max The maximum value. If {@code value} is greater than this, this will be returned
     * @return The limited value
     */
    public static Number limit(Number value, Number min, Number max) {
        if (value.doubleValue() < min.doubleValue()) {
            value = min;
        } else if (value.doubleValue() > max.doubleValue()) {
            value = max;
        }
        return value;
    }

    /**
     * Excludes a value outside of two numbers
     * @param value The value to exclude
     * @param min If {@code value} is greater than this, {@code max} is checked
     * @param max If {@code value} is less than this (between {@code min} and {@code max}), {@code def} is returned
     * @param def The default value to be returned. If {@code value} is between {@code min} and {@code max}, this value is returned
     * @return The excluded value
     */
    public static Number excludes(Number value, Number min, Number max, Number def) {
        if (value.doubleValue() > min.doubleValue() && value.doubleValue() < min.doubleValue()) {   // If value is within min and max,
            return def; // Return the default value
        } else {    // Otherwise,
            return value;   // Return the actual value
        }
    }
}