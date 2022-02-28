package com.happy.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeUtil {

    public static DateTimeFormatter DEF_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static String toString(LocalDateTime time, DateTimeFormatter df ) {
        return df.format(time);
    }

    public static String toString(LocalDateTime time) {
        return toString(time, DEF_FORMATTER);
    }

}
