package org.longmetal.util;

import org.longmetal.Constants;

public class Console {
    public static void log(Object output) {
        System.out.println(output.toString());
    }

    public static void ok(Object output) {
        System.out.println(
                Constants.ANSI_GREEN + "[ OK ]\t" + output.toString() + Constants.ANSI_RESET);
    }

    public static void warn(Object output) {
        System.out.println(
                Constants.ANSI_YELLOW + "[WARN]\t" + output.toString() + Constants.ANSI_RESET);
    }

    public static void error(Object output) {
        System.out.println(
                Constants.ANSI_RED + "[ERROR]\t" + output.toString() + Constants.ANSI_RESET);
    }
}
