package com.hualing.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;


public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {

    public static BigDecimal createBigDecimalSafe(String str, BigDecimal defaultValue) {
        if (StringUtils.isNotBlank(str)) {
            String temp = StringUtils.trim(str.replaceAll("[\u4e00-\u9fa5]", ""));
            temp = StringUtils.remove(temp, (char) 160);
            temp = StringUtils.remove(temp, "——");
            try {
                if (StringUtils.isNotBlank(temp)) {
                    return new BigDecimal(temp);
                }
            } catch (NumberFormatException e) {

            }
        }
        return defaultValue;
    }

    public static BigDecimal createBigDecimalSafe(String str) {
        return createBigDecimalSafe(str, null);
    }
}
