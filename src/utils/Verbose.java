package utils;

import taxi.Global;

public class Verbose {
    public static boolean verbose = true;
    public static void println(String line) {
        StackTraceElement stack[] = (new Throwable()).getStackTrace();
        if (verbose) {
            System.out.println(stack[1].getClassName() + "::" + stack[1].getMethodName() + ": " + line);
        }
    }
    public static void println(String line, StackTraceElement stack[]) {
        if (verbose) {
            System.out.println(stack[1].getClassName() + "::" + stack[1].getMethodName() + ": " + line);
        }
    }
    public static void printlnAt(String line) {
        StackTraceElement stack[] = (new Throwable()).getStackTrace();
        println(line + " @" + Global.getRelativeTime(), stack);
    }
}
