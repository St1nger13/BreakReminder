package me.st1nger13.breakreminder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by st1nger13 on 25.07.16.
 * Print Helper Class
 */
public final class Print {

    public static void line(String line) {
        System.out.println(line) ;
    }
    public static void lined(String line) {
        System.out.println(new SimpleDateFormat("hh:mm:ss").format(new Date()) + ": " + line) ;
    }

    public static void linet(String line) {
        System.out.println("\t" + line) ;
    }

    public static void error(String line) {
        System.err.println("Error: " + line) ;
    }
}
