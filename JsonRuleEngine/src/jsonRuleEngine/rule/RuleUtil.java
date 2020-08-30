package jsonRuleEngine.rule;

public class RuleUtil {
	//
	public static boolean isNumeric(String string) {
		//
		if(string == null || string.equals("")) {
			return false;
		}
		
		return string.matches("-?\\d+(\\.\\d+)?");
	}
}