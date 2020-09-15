package com.hualing.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {

        String a = "123-123";
        Pattern p = Pattern.compile("^(\\d)*(-)*(\\d)*$");
        Matcher m = p.matcher(a);
        System.out.println(m.matches());
    }
}
