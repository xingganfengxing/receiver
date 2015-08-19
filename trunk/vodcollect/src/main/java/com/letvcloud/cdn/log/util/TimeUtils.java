package com.letvcloud.cdn.log.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

public class TimeUtils {

    public static final String SOURCETIME_FORMAT = "dd/MMM/yyyy:HH:mm:ss Z";
    public static final String MILLI_TIME_FORMAT = "yyyyMMddHHmmssSSS";

    public static String format(String time, String from, String to) {
    
        DateTimeFormatter timeformat = DateTimeFormat.forPattern(from)
                .withLocale(Locale.US);
        DateTime datetime = DateTime.parse(time, timeformat);
        return datetime.toString(to);
    }
}
