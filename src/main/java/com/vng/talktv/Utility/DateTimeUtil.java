package com.vng.talktv.Utility;

import java.time.LocalDateTime;
import java.util.Calendar;

public class DateTimeUtil {
    public static String getCurrentMonthAndYear() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        return (month + 1) + "/" + year;
    }

    public static LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }
}
