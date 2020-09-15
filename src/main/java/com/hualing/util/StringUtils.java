package com.hualing.util;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	public static String generateQuestionMark(int count){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i< count; i++){
			sb.append(",?");
		}
		
		return sb.substring(1);
	}

	public static boolean nonNull(Object o){
		if(o == null)
			return false;
		else if(o instanceof String && "".equals((String)o))
			return false;
		else if(o instanceof Long && ((Long)o) == 0)
			return false;
		else if(o instanceof Double && ((Double)o) == 0)
			return false;
		else if("".equals(o.toString()))
			return false;
		else return true;
	}

	public static boolean checkPhoneNumber(String str){
		Pattern p = Pattern.compile("^(\\d)*(-)*(\\d)*$");
		Matcher m = p.matcher(str);
		return m.matches();
	}
}
