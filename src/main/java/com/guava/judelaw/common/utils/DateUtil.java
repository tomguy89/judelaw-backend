package com.guava.judelaw.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private DateUtil() {}

    public static String convertDateFormat(Date date, String formatString) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatString);
        return sdf.format(date);
    }


}
