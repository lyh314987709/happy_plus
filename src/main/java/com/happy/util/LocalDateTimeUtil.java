package com.happy.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeUtil {

    public static DateTimeFormatter DEF_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static DateTimeFormatter DEF_FORMATTER_TIME = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");

    public static String toString(LocalDateTime time, DateTimeFormatter df ) {
        return df.format(time);
    }

    public static String toString(LocalDateTime time) {
        return toString(time, DEF_FORMATTER);
    }

    public static LocalDateTime parseToLocalDateToLocalDateTime(String time, DateTimeFormatter df) {
        return LocalDate.parse(time, df).atStartOfDay();
    }


    public static LocalDateTime parseToLocalDateToLocalDateTime(String time) {
        return parseToLocalDateToLocalDateTime(time, DEF_FORMATTER);
    }

    public static LocalDate parseToLocalDate(String time) {
        return LocalDate.parse(time, DEF_FORMATTER);
    }
}
