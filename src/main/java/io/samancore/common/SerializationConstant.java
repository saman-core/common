package io.samancore.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class SerializationConstant {
    public static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final TimeZone DEFAULT_TIMEZONE;
    public static final DateFormat DATE_FORMAT_ISO8601;

    static {
        DEFAULT_TIMEZONE = TimeZone.getTimeZone("UTC");
        DATE_FORMAT_ISO8601 = new SimpleDateFormat(DATE_PATTERN);
        DATE_FORMAT_ISO8601.setTimeZone(DEFAULT_TIMEZONE);
    }

    private SerializationConstant() {
    }
}
