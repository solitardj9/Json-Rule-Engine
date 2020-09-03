package com.example.demo.dataActionMngtSet.dataActionMngt.service.util.jsonRuleEngine.util;

public class StringUtil {
	//
	public static boolean isNumeric(String string) {
		//
		if(string == null || string.equals("")) {
			return false;
		}
		
		return string.matches("-?\\d+(\\.\\d+)?");
	}
	
	public static String replaceSingleQuotesToDoubleQuotes(String data) {
		//
		return data.replace("'","\"");
	}
}