package com.hualing.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.util.Date;


public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private static final String[] DEFAULT_PATTERNS = new String[]{"yyyy-MM-dd", "yyyy-MM", "yyyy-MM-dd hh:mm:ss",
            "yyyy-MM-dd hh:mm", "yyyyMMdd"};

    public static Date parseDateSafe(String str) {

        return parseDateSafe(str, DEFAULT_PATTERNS);

    }

    public static Date parseDateSafe(String str, String... parsePatterns) {
        if (StringUtils.isNotBlank(str)) {
            str = str.replaceAll("[\u4e00-\u9fa5]", "");
            try {
                return parseDate(str, parsePatterns);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
