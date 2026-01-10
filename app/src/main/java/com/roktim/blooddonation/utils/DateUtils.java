package com.roktim.blooddonation.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private static final SimpleDateFormat displayFormat =
            new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    public static String getCurrentDate() {
        return dateFormat.format(new Date());
    }

    public static String formatForDisplay(String date) {
        try {
            Date parsed = dateFormat.parse(date);
            return displayFormat.format(parsed);
        } catch (Exception e) {
            return date;
        }
    }

    public static String formatDate(Date date) {
        return dateFormat.format(date);
    }
}