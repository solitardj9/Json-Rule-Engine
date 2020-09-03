package jsonRuleEngine_v1_0.rule;

public class RuleUtil {
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