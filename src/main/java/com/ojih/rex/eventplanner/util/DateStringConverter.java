package com.ojih.rex.eventplanner.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateStringConverter {

    private DateStringConverter() {
    }

    public static Date getDateFomDateString(String dateString) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-dd-MM'T'HH:mm:ss.SSSX");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.parse(dateString);
    }
}
