package com.guava.judelaw.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StringUtil {

    private StringUtil() {}

    public static boolean isEmpty(String str) { return str == null || "".equals(str); }

    public static boolean isEqual(String str1, String str2) {
        if (str1 == null) str1 = "";
        if (str2 == null) str2 = "";
        return str1.equals(str2);
    }

    public static String nullSafeToString(Object obj) {
        if (obj == null) return "";
        return obj.toString();
    }

    public boolean isNumeric(String str) {
        if (str == null) return false;
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
