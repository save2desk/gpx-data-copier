package example.save2.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {

    private ExceptionUtils() {
        /* This utility class should not be instantiated */
    }

    public static String stackTraceToString(Exception e) {

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();

    }

}
