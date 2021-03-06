package com.example.demo.dataActionMngtSet.dataActionMngt.service.util.jsonRuleEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.janino.ExpressionEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.dataActionMngtSet.dataActionMngt.service.util.jsonRuleEngine.data.JsonRuleEngineConfig;
import com.example.demo.dataActionMngtSet.dataActionMngt.service.util.jsonRuleEngine.data.JsonRuleEngineConfigs;
import com.example.demo.dataActionMngtSet.dataActionMngt.service.util.jsonRuleEngine.data.JsonRuleEngineResultSet;
import com.example.demo.dataActionMngtSet.dataActionMngt.service.util.jsonRuleEngine.util.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

public class JsonRuleEngineImpl implements JsonRuleEngine {
	//
	private static Logger logger = LoggerFactory.getLogger(JsonRuleEngineImpl.class);
	
	private JsonRuleEngineConfigs jsonRuleEngineConfigs;
	
	private static ObjectMapper om = new ObjectMapper();
	
	public JsonRuleEngineImpl() {
		//
	}
	
	@Override
	public void insertConfigs(String jsonRuleEngineConfigs) {
		//
		try {
			this.jsonRuleEngineConfigs = om.readValue(jsonRuleEngineConfigs, JsonRuleEngineConfigs.class);
		} catch (JsonProcessingException e) {
			logger.error(e.toString());
		}
	}

	@Override
	public void insertConfigs(JsonRuleEngineConfigs jsonRuleEngineConfigs) {
		//
		this.jsonRuleEngineConfigs = new JsonRuleEngineConfigs(jsonRuleEngineConfigs);
	}

	@Override
	public JsonRuleEngineConfigs getConfigs() {
		//
		JsonRuleEngineConfigs retConfigs = new JsonRuleEngineConfigs(jsonRuleEngineConfigs);
		return retConfigs;
	}

	@Override
	public List<JsonRuleEngineResultSet> execute(String jsonString) {
		//
		List<JsonRuleEngineResultSet> retList = new ArrayList<>();
		DocumentContext context = JsonPath.parse(jsonString);
		
        for (JsonRuleEngineConfig iter : this.jsonRuleEngineConfigs.getConfigs()) {
        	//
        	Boolean ret = isTriggered(iter.getTrigger(), context);
        	if (ret != null && ret.equals(true)) {
        		//
        		Object result = makeResult(iter.getResult(), context);

        		JsonRuleEngineResultSet jsonRuleEngineResultSet = new JsonRuleEngineResultSet(result, iter.getEvent());
        		
        		retList.add(jsonRuleEngineResultSet);
	        }
	    }
        
        return retList;
	}

	private Boolean isTriggered(String trigger, DocumentContext context) {
		//
		if (context == null) {
			logger.info("[JsonRuleEngine].isTriggered : context is null");
			return false;
		}
		
		// 1) single quotes are changed double quotes
		String tmpTrigger = StringUtil.replaceSingleQuotesToDoubleQuotes(new String(trigger));
		
		logger.info("[JsonRuleEngine].isTriggered : tmpTrigger = " + tmpTrigger);
		
		String regExp = "read\\((.*?)\\)";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(tmpTrigger);
		
		while (m.find()) {
			//
			String matchedRule = m.group();
			logger.info("[JsonRuleEngine].isTriggered : matchedRule = " + matchedRule);
			
			Object exchageObject = readDataFromJson(context, matchedRule);
			
			logger.info("[JsonRuleEngine].isTriggered : exchageObject = " + exchageObject);
			
			String exchangeString = null;
			try {
				exchangeString = om.writeValueAsString(exchageObject);
				tmpTrigger = tmpTrigger.replace(matchedRule, exchangeString);
			} catch (JsonProcessingException e) {
				logger.info("[JsonRuleEngine].isTriggered : error = " + e.toString());
			}
		}
		logger.info("[JsonRuleEngine].isTriggered : tmpTrigger = " + tmpTrigger);
		
		if (tmpTrigger != null)
			return evaluateTrigger(tmpTrigger);
		else
			return false;
	}
	
	private Object readDataFromJson(DocumentContext context, String function) {
		//
		try {
			String keyPath = function.replace("read", "").replace("(", "").replace(")", "");
			
			Object object = context.read(keyPath);
			
			return object;
		} catch (PathNotFoundException e) {
			return null;
		}
	}
	
    private Boolean evaluateTrigger(String trigger) {
    	//
    	String regexp = "[!@#$%^&*(),?:{}|<=>]";
    	
    	try {
    		Boolean result = null;

    		Pattern pattern = Pattern.compile(regexp);
    		String[] operands = pattern.split(trigger);
    		
    		List<String> params = new ArrayList<>();
    		for (String iter : operands) {
    			if (iter != null && !iter.equals("")  && !iter.equals(" ")) {
    				params.add(iter);
    			}
    		}
    		
    		// Now here's where the story begins...
    		ExpressionEvaluator ee = new ExpressionEvaluator();
    		
    		// The expression will have two "int" parameters: "a" and "b".
    		String[] arrParams = new String[params.size()];
    		Class[] arrClasses = new Class[params.size()];
    		Object[] arrObjects = new Object[params.size()];
    		
    		int count = 0;
    		for (String iter : params) {
    			String tmpParam = ("p" + count);
    			arrParams[count] = tmpParam;
    			arrClasses[count] = String.class;
    			
    			String value = iter.trim();
    			arrObjects[count] = value;
    			count++;
            }
    		
    		// The expression will have two "int" parameters: "a" and "b".
    		ee.setParameters(arrParams, arrClasses);
    		
    		// And the expression (i.e. "result") type is also "int".
    		ee.setExpressionType(Boolean.class);
    		
    		// And now we "cook" (scan, parse, compile and load) the fabulous expression.
    		//System.out.println("[DataActionWorker].evaluateTrigger :" + tmpTrigger);
    		ee.cook(trigger);
    		
    		// Eventually we evaluate the expression - and that goes super-fast.
    		result = (Boolean) ee.evaluate(arrObjects);
    		logger.info("[JsonRuleEngine].evaluateTrigger : result = " + result.toString());
    		
    		return result;
    	}
    	catch (Exception e) {
    		logger.info("[JsonRuleEngine].evaluateTrigger : error = " + e.toString());
    		return false;
    	}
    }
	
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private Object makeResult(Object result, DocumentContext context) {
    	//
    	Object ret = null;
    	Object tmpResult = result;
 
    	logger.info("[JsonRuleEngine].makeResult : result = " + result);
		try {
			if ((tmpResult instanceof String) && ((String)tmpResult).startsWith("read")) {
				//
				ret = readDataFromJson(context, (String)tmpResult);
			}
			else if (tmpResult instanceof Map) {
				//
				Map<String, Object> map = new HashMap((Map)tmpResult);
				for (Entry<String, Object> entry : map.entrySet()) {
					//
					Object tmpObject = entry.getValue();
					if ((tmpObject instanceof String) && ((String)tmpObject).startsWith("read")) {
						tmpObject = readDataFromJson(context, (String)tmpObject);
						map.put(entry.getKey(), tmpObject);
					}
				}
				
				return om.writeValueAsString(map);
			}
			else{
				// nothing to do
				ret = tmpResult;
			}
			
		} catch (Exception e) {
			logger.info("[JsonRuleEngine].makeResult : error = " + e.toString());
			return null;
		}
		
    	return ret;
    }
}