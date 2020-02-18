package org.longmetal.util;

public class Console {
    public static void log(Object output) {
        System.out.println(output.toString());
    }

    public static void ok(Object output) {
        System.out.println(
                "[ OK ]\t" + output.toString());
    }

    public static void warn(Object output) {
        System.out.println(
                "[WARN]\t" + output.toString());
    }

    public static void error(Object output) {
        System.out.println(
                "[ERROR]\t" + output.toString());
    }
}
