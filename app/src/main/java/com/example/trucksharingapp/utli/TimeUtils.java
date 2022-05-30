package com.example.trucksharingapp.utli;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeUtils {


    public static String timeFormat(long time, String format) {
        SimpleDateFormat sdr = new SimpleDateFormat(format, Locale.ENGLISH);
        return sdr.format(new Date(time * 1000L));
    }
    public static int getCurrentTime() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static long timeToStamp(String time, String format) {
        Date d = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sf = new SimpleDateFormat(format, Locale.US);
        try {
            d = sf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d != null ? d.getTime() : 0;
    }


}
