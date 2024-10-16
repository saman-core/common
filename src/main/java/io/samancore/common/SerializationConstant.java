package io.samancore.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static com.fasterxml.jackson.databind.util.StdDateFormat.DATE_FORMAT_STR_ISO8601;

public class SerializationConstant {
    public static final SimpleDateFormat[] DESERIALIZER_DATE_FORMATTERS = new SimpleDateFormat[] {
            new SimpleDateFormat(DATE_FORMAT_STR_ISO8601),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX"),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX"),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ssXXX"),
            new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"),
            new SimpleDateFormat("yyyy-MM-dd"),
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz"),
    };
    public static final String DATE_PATTERN = DATE_FORMAT_STR_ISO8601;
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
