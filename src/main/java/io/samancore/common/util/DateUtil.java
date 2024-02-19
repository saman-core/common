package io.samancore.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY");

    private DateUtil() {
    }

    public static String toString(Date date) {
        if (date == null) {
            return "";
        }
        return dateFormat.format(date);
    }
}
