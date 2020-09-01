package jsonRuleEngine.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;


public class Rules {
	//
	private static Logger logger = LoggerFactory.getLogger(Rules.class);
	
	private static ObjectMapper om = new ObjectMapper();
	
	public enum RULE {
		//
		FUNCTION_PAYLOAD_KEY("PAYLOAD_KEY", "PAYLOAD_KEY\\(([a-z-A-Z-\\s0-9.\\\"]*)\\)") {
			//
			@Override
			public String replaceRuleExpressionWithData(String ruleExpression, String data) {
				//
				try {
					Pattern p = Pattern.compile(this.getRegExp());
					Matcher m = p.matcher(ruleExpression);
					
					while (m.find()) {
						//
						String matchedRule = m.group();
						logger.info("[RULE.FUNCTION_PAYLOAD_KEY].replaceRuleExpressionWithData : matchedRule = " + matchedRule);
						
						String keyPath = getKeyPathInRuleExpression(matchedRule, this.getType());
						if (keyPath != null) {
							//
							String key = extractKeyInKeyPath(keyPath);
							if (key != null) {
								ruleExpression = ruleExpression.replace(matchedRule, key);
							}
						}
					}
					
					return ruleExpression;
				} catch (Exception e) {
					logger.info("[RULE.FUNCTION_PAYLOAD_KEY].replaceRuleExpressionWithData : error = " + e.toString());
					return ruleExpression;
				}
			}
		},
		FUNCTION_PAYLOAD_KEY_PATH("PAYLOAD_KEY_PATH", "PAYLOAD_KEY_PATH\\(([a-z-A-Z-\\s0-9.\\\"]*)\\)") {
			//
			@Override
			public String replaceRuleExpressionWithData(String ruleExpression, String data) {
				//
				try {
					Pattern p = Pattern.compile(this.getRegExp());
					Matcher m = p.matcher(ruleExpression);
					
					while (m.find()) {
						//
						String matchedRule = m.group();
						logger.info("[RULE.FUNCTION_PAYLOAD_KEY_PATH].replaceRuleExpressionWithData : matchedRule = " + matchedRule);
						
						String keyPath = getKeyPathInRuleExpression(matchedRule, this.getType());
						if (keyPath != null) {
							//
							ruleExpression = ruleExpression.replace(matchedRule, keyPath);
						}
					}
					
					return ruleExpression;
				} catch (Exception e) {
					logger.info("[RULE.FUNCTION_PAYLOAD_KEY_PATH].replaceRuleExpressionWithData : error = " + e.toString());
					return ruleExpression;
				}
            }
        },
		FUNCTION_PAYLOAD_VALUE("PAYLOAD_VALUE", "PAYLOAD_VALUE\\(([a-z-A-Z-\\s0-9.\\\"]*)\\)") {
        	//
			@Override
			public String replaceRuleExpressionWithData(String ruleExpression, String data) {
				//
				try {
					Pattern p = Pattern.compile(this.getRegExp());
					Matcher m = p.matcher(ruleExpression);
					
					while (m.find()) {
						//
						String matchedRule = m.group();
						logger.info("[RULE.FUNCTION_PAYLOAD_VALUE].replaceRuleExpressionWithData : matchedRule = " + matchedRule);
						
						String keyPath = getKeyPathInRuleExpression(matchedRule, this.getType());
						if (keyPath != null) {
							//
							String valueAsString = extractValueAsStringInDataByKeyPath(data, keyPath);
							if (valueAsString != null) {
								logger.info("[RULE.FUNCTION_PAYLOAD_VALUE].replaceRuleExpressionWithData : (b/f) ruleExpression = " + ruleExpression);
								ruleExpression = exchangeRuleExpression(ruleExpression, matchedRule, valueAsString);
								logger.info("[RULE.FUNCTION_PAYLOAD_VALUE].replaceRuleExpressionWithData : (a/f) ruleExpression = " + ruleExpression);
							}
						}
						else {
							return data;
						}
					}
					
					return ruleExpression;
				} catch (Exception e) {
					logger.info("[RULE.FUNCTION_PAYLOAD_VALUE].replaceRuleExpressionWithData : error = " + e.toString());
					return ruleExpression;
				}
			}
		},
		FUNCTION_PAYLOAD_VALUE_WITH("PAYLOAD_VALUE_WITH", "PAYLOAD_VALUE_WITH\\(([a-z-A-Z-\\s0-9.\\\\\"]*),(.*?)\\)") {
        	//
			@Override
			public String replaceRuleExpressionWithData(String ruleExpression, String data) {
				//
				try {
					Pattern p = Pattern.compile(this.getRegExp());
					Matcher m = p.matcher(ruleExpression);
					
					while (m.find()) {
						//
						String matchedRule = m.group();
						logger.info("[RULE.FUNCTION_PAYLOAD_VALUE_WITH].replaceRuleExpressionWithData : matchedRule = " + matchedRule);
						
						matchedRule = checkMatchedRuleExpression(matchedRule);
						logger.info("[RULE.FUNCTION_PAYLOAD_VALUE_WITH].replaceRuleExpressionWithData : cheked matchedRule = " + matchedRule);
						
						KeyPathJson keyPathJson = getKeyPathWithInRuleExpression(matchedRule, this.getType(), data);
						if (keyPathJson != null) {
							//
							String valueAsString = extractValueAsStringInDataByKeyPath(keyPathJson.getJsonString(), keyPathJson.getKeyPath());
							if (valueAsString != null) {
								logger.info("[RULE.FUNCTION_PAYLOAD_VALUE_WITH].replaceRuleExpressionWithData : (b/f) ruleExpression = " + ruleExpression);
								ruleExpression = exchangeRuleExpression(ruleExpression, matchedRule, valueAsString);
								logger.info("[RULE.FUNCTION_PAYLOAD_VALUE_WITH].replaceRuleExpressionWithData : (a/f) ruleExpression = " + ruleExpression);
							}
						}
					}
					
					return ruleExpression;
				} catch (Exception e) {
					logger.info("[RULE.FUNCTION_PAYLOAD_VALUE].replaceRuleExpressionWithData : error = " + e.toString());
					return ruleExpression;
				}
			}
		},
		FUNCTION_PAYLOAD_ARRAY("PAYLOAD_ARRAY", "PAYLOAD_ARRAY\\(([a-z-A-Z-\\s0-9.\\\"]*)\\,( [0-9]*)\\)") {
			//
			@Override
			public String replaceRuleExpressionWithData(String ruleExpression, String data) {
				//
				try {
					Pattern p = Pattern.compile(this.getRegExp());
					Matcher m = p.matcher(ruleExpression);
					
					while (m.find()) {
						//
						String matchedRule = m.group();
						logger.info("[RULE.FUNCTION_PAYLOAD_ARRAY].replaceRuleExpressionWithData : matchedRule = " + matchedRule);
						
						KeyPathIndex keyPathIndex = getKeyPathAndArrayIndexInRuleExpression(matchedRule, this.getType());
						if (keyPathIndex != null) {
							//
							String valueAsString = extractValueAsStringInArrayDataByKeyPath(data, keyPathIndex);
							if (valueAsString != null) {
								logger.info("[RULE.FUNCTION_PAYLOAD_ARRAY].replaceRuleExpressionWithData : (b/f) ruleExpression = " + ruleExpression);
								ruleExpression = exchangeRuleExpression(ruleExpression, matchedRule, valueAsString);
								logger.info("[RULE.FUNCTION_PAYLOAD_ARRAY].replaceRuleExpressionWithData : (a/f) ruleExpression = " + ruleExpression);
							}
						}
					}
					
					return ruleExpression;
				} catch (Exception e) {
					logger.info("[RULE.FUNCTION_PAYLOAD_ARRAY].replaceRuleExpressionWithData : error = " + e.toString());
					return ruleExpression;
				}
			}
						
						
						
				
				
//
//            @Override
//            public Object doFunction(String function, String matchedFunction, String payload) {
//                //
//                KeyPathIndex keyPathIndex = getKeyPathIndex(matchedFunction, function);
//                
//                if (keyPathIndex != null) {
//                    //
//                    if (keyPathIndex.getKeyPath() != null) {
//                        //
//                        Object object = payloadValueAsString(payload, keyPathIndex.getKeyPath());
//                        if (object != null)
//                            return object;
//                        else
//                            return payload;
//                    }
//                    else {
//                        // payload ?��체�? json object�? ?��?�� json array?���? 봐야 ?��
//                        Object object = payloadArrayValueAsString(payload, keyPathIndex.getIndex());
//                        if (object != null)
//                            return object;
//                        else
//                            return payload;
//                    }
//                }
//                else {
//                    return payload;
//                }
//            }
		},
		
		;
		
		private String type;
		private String regExp;
		
		private RULE(String type, String regExp) {
			this.type = type;
			this.regExp = regExp;
		}
		
		@Override
		public String toString() {
			return type;
		}
		
		public String getType() {
			return type;
		}

		public String getRegExp() {
			return regExp;
		}

		public abstract String replaceRuleExpressionWithData(String ruleExpression, String data);
		
//		public abstract String executeRuleExpressionWithData(String ruleExpression, String data);
	}
	
	public static List<String> getRuleTypes() {
		//
		List<String> list = new ArrayList<>();
		for (RULE iter : RULE.values()) {
			list.add(iter.getType());
		}
		return list;
	}
	
	public static String getRegExpOfRuleByType(String type) {
		//
		for (RULE iter : RULE.values()) {
			if (iter.getType().equals(type)) {
				return iter.getRegExp();
			}
		}
		return null;
	}
	
	public static RULE getRuleByType(String type) {
		//
		for (RULE iter : RULE.values()) {
			if (iter.getType().equals(type)) {
				return iter;
			}
		}
		return null;
	}
	
	/**
	 * extract key path string in rule expression
	 * @param payloadFunction
	 * @param function
	 * @return
	 */
	private static String getKeyPathInRuleExpression(String ruleExpression, String ruleType) {
		//
		String keyPath = ruleExpression.replace(ruleType, "").replace("(", "").replace(")", "");
		
		if (keyPath != null && !keyPath.isEmpty() )
			return keyPath;
		else
			return null;
	}
	
	/**
	 * extract value as string in data by key path
	 * @param payload
	 * @param keyPath
	 * @return
	 */
	private static String extractValueAsStringInDataByKeyPath(String data, String keyPath) {
		//
		try {
			Object object = extractValueInDataByKeyPath(data, keyPath);
			if (object != null) {
				String ret = om.writeValueAsString(object);
				return ret;
			}
			else {
				return null;
			}
		}
		catch (Exception e) {
			logger.info("[RULE].extractValueAsStringInDataByKeyPath : error = " + e.toString());
			return null;
		}
	}
	
	/**
	 * extract value as object in data by key path
	 * @param payload
	 * @param keyPath
	 * @return
	 */
	private static Object extractValueInDataByKeyPath(String data, String keyPath) {
		//
		try {
			DocumentContext context = JsonPath.parse(data);
			
			if (context.json() instanceof JSONArray) {
				return null;
			}
			else {
				Object object = context.read("$." + keyPath);
				return object;
			}
		}
		catch (Exception e) {
			logger.info("[RULE].extractValueInDataByKeyPath : error = " + e.toString());
			return null;
		}
	}
	
	private static String extractValueAsStringInArrayDataByKeyPath(String payload, KeyPathIndex keyPathIndex) {
		//
		try {
			Object object = extractValueInArrayDataByKeyPath(payload, keyPathIndex);
			if (object != null) {
				String ret = om.writeValueAsString(object);
				return ret;
			}
			else {
				return null;
			}
		}
		catch (Exception e) {
			logger.info("[RULE].extractValueAsStringInArrayDataByKeyPath : error = " + e.toString());
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	private static Object extractValueInArrayDataByKeyPath(String payload, KeyPathIndex keyPathIndex) {
		//
		try {
			DocumentContext context = JsonPath.parse(payload);
			
			if (keyPathIndex.getKeyPath() != null) {
				Object object = context.read("$." + keyPathIndex.getKeyPath());
				if (object instanceof JSONArray) {
					List<Object> list = (List<Object>)object;
					try {
						return list.get(keyPathIndex.getIndex());
					}
					catch (IndexOutOfBoundsException e) {
						logger.info("[RULE].extractValueInArrayDataByKeyPath : error = " + e.toString());
						return null;
					}
				}
				else {
					return null;
				}
			}
			else {	// root is json array
				if (context.json() instanceof JSONArray) {
					Object object = context.read("$[" + keyPathIndex.getIndex() + "]");
					return object;
				}
				else {
					return null;
				}
			}
		}
		catch (Exception e) {
			logger.info("[RULE].extractValueInArrayDataByKeyPath : error = " + e.toString());
			return null;
		}
	}

	private static String extractKeyInKeyPath(String keyPath) {
		//
		return seperateStrings(keyPath, null, ".");
	}
	
	private static KeyPathIndex getKeyPathAndArrayIndexInRuleExpression(String ruleExpression, String ruleType) {
		//
		String keyPathAndArrayIndex = ruleExpression.replace(ruleType, "").replace("(", "").replace(")", "").replace(" ", "");
	
		if (keyPathAndArrayIndex != null && !keyPathAndArrayIndex.isEmpty() ) {
			//
			List<String> params = seperateStrings(keyPathAndArrayIndex, ",");
			
			if (params != null && !params.isEmpty()) {
				//
				String keyPath = null;
				Integer arrayIndex = null;
				
				if (keyPathAndArrayIndex.startsWith(",")) {
					arrayIndex = getIndex(params.get(0));
				}
				else {
					if (params.size() == 2) {
						keyPath = params.get(0);
						arrayIndex = getIndex(params.get(1));
					}
				}
				
				if (arrayIndex != null) {
					KeyPathIndex result = new KeyPathIndex(keyPath, arrayIndex);
					return result;
				}
				else {
					return null;
				}
			}
			else {
				return null;
			}
		}
		else
			return null;
	}
	
	private static KeyPathJson getKeyPathWithInRuleExpression(String ruleExpression, String ruleType, String data) {
		//
		String keyPathAndJsonString = ruleExpression.replaceFirst(ruleType, "");
		keyPathAndJsonString = keyPathAndJsonString.replaceFirst("\\(", "");
		
		Integer lastIndexOf = keyPathAndJsonString.lastIndexOf(")");
		keyPathAndJsonString = keyPathAndJsonString.substring(0, lastIndexOf);
		
		while (true) {
			if (keyPathAndJsonString.startsWith(" ") || keyPathAndJsonString.startsWith("\t")) {
				keyPathAndJsonString.replaceFirst(" ", "");
			}
			else if (keyPathAndJsonString.startsWith("\t")) {
				keyPathAndJsonString.replaceFirst("\t", "");
			}
			else {
				break;
			}
		}
	
		if (keyPathAndJsonString != null && !keyPathAndJsonString.isEmpty() ) {
			//
			Integer index = keyPathAndJsonString.indexOf(",");
			
			String keyPath = keyPathAndJsonString.substring(0, index);
			String jsonString = keyPathAndJsonString.substring(index + 1);
			
			if (jsonString.startsWith("{") || jsonString.startsWith("[")) {
				keyPathAndJsonString.substring(index + 1);
			}
			else {
				List<String> ruleTypes = Rules.getRuleTypes();
				for (String iterType : ruleTypes) {
					RULE rule = Rules.getRuleByType(iterType);
					jsonString = rule.replaceRuleExpressionWithData(jsonString, data);
				}
			}
			
			KeyPathJson result = new KeyPathJson(keyPath, jsonString);
			logger.info(result.toString());
			return result;
		}
		else
			return null;
	}
	
	/**
	 * exchange matched rule string with value string in rule expression
	 * @param ruleExpression
	 * @param matchedRule
	 * @param valueAsString
	 * @return
	 */
	private static String exchangeRuleExpression(String ruleExpression, String matchedRule, String valueAsString) {
		//
		String exchangedRuleExpression = ruleExpression.replace(matchedRule, valueAsString);
		exchangedRuleExpression = exchangedRuleExpression.replace("\"\"", "\"").replace("\"{", "{").replace("}\"", "}");
		return exchangedRuleExpression;
	}
	
	/**
	 * extract word at specific index of sentence with seperator
	 * @param string
	 * @param index
	 * @param seperator
	 * @return
	 */
	private static String seperateStrings(String string, Integer index, String seperator) {
		//
		StringTokenizer st = new StringTokenizer(string, seperator);
		List<String> listTopicUnits = new ArrayList<>();
		
		while(st.hasMoreTokens()) {
			listTopicUnits.add(st.nextToken());
		}
		
		if (index != null)
			return listTopicUnits.get(index);
		else
			return listTopicUnits.get(listTopicUnits.size()-1);
	}
	
	/**
	 * make word list of sentence with seperator
	 * @param string
	 * @param seperator
	 * @return
	 */
	private static List<String> seperateStrings(String string, String seperator) {
		//
		StringTokenizer st = new StringTokenizer(string, seperator);
		List<String> listTopicUnits = new ArrayList<>();
		
		while(st.hasMoreTokens()) {
			listTopicUnits.add(st.nextToken());
		}
		
		if (!listTopicUnits.isEmpty())
			return listTopicUnits;
		else
			return null;
	}
	
	private static Integer getIndex(String index) {
		try {
			return Integer.valueOf(index);
		}
		catch (NumberFormatException e) {
			return null;
		}
	}
	
	private static String checkMatchedRuleExpression(String mactchedExpression) {
		//
		Integer countOfOpen = 0;
		Integer countOfclose = 0;
		Integer fromIndex = 0;
		
		while (true) {
			Integer tmpIndex = mactchedExpression.indexOf("(", fromIndex);
			if (tmpIndex < 0) {
				break;
			}
			fromIndex = tmpIndex + 1;
			countOfOpen++;
		}
		
		fromIndex = 0;
		while (true) {
			Integer tmpIndex = mactchedExpression.indexOf(")", fromIndex);
			if (tmpIndex < 0) {
				break;
			}
			fromIndex = tmpIndex + 1;
			countOfclose++;
		}
		
		Integer diff = countOfOpen - countOfclose;
		if (diff > 0) {
			for (int i = 0 ; i < diff ; i++) {
				mactchedExpression += ")";
			}
		}
		
		return mactchedExpression;
	}
}

class KeyPathIndex {
	//
	private String keyPath;
	private Integer index;
	
	public KeyPathIndex(String keyPath, Integer index) {
		this.keyPath = keyPath;
		this.index = index;
	}

	public String getKeyPath() {
		return keyPath;
	}
	
	public void setKeyPath(String keyPath) {
		this.keyPath = keyPath;
	}
	
	public Integer getIndex() {
		return index;
	}
	
	public void setIndex(Integer index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "KeyPathIndex [keyPath=" + keyPath + ", index=" + index + "]";
	}
}

class KeyPathJson {
	//
	private String keyPath;
	private String jsonString;
	
	public KeyPathJson(String keyPath, String jsonString) {
		this.keyPath = keyPath;
		this.jsonString = jsonString;
	}

	public String getKeyPath() {
		return keyPath;
	}
	
	public void setKeyPath(String keyPath) {
		this.keyPath = keyPath;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	@Override
	public String toString() {
		return "KeyPathJson [keyPath=" + keyPath + ", jsonString=" + jsonString + "]";
	}
}