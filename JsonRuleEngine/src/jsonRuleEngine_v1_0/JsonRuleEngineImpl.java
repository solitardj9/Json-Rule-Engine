package jsonRuleEngine_v1_0;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.codehaus.janino.ExpressionEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jsonRuleEngine_v1_0.data.JsonRuleEngineConfig;
import jsonRuleEngine_v1_0.data.JsonRuleEngineConfigs;
import jsonRuleEngine_v1_0.rule.RuleUtil;
import jsonRuleEngine_v1_0.rule.Rules;
import jsonRuleEngine_v1_0.rule.Rules.RULE;

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
	public List<String> execute(String jsonString) {
		//
		List<String> retList = new ArrayList<>();
        for (JsonRuleEngineConfig iter : this.jsonRuleEngineConfigs.getConfigs()) {
        	//
        	Boolean ret = isTriggered(iter.getTrigger(), jsonString);
        	if (ret != null && ret.equals(true)) {
        		//
        		String result = makeResult(iter.getResult(), jsonString);
        		retList.add(result);
	        }
	    }
        
        return retList;
	}
	
	
	/**
	 * check Trigger Expression
	 * <p>- Alphabet and Numeric only in String at expression of trigger
	 * <p>- A string literal is bracketed by either single quotes ( ' )
	 * <p>- ex) "trigger" : "PAYLOAD_VALUE(address.city) == 'New York'"
	 * @param trigger
	 * @param data
	 * @return
	 */
	private Boolean isTriggered(String trigger, String data) {
		//
		if (data == null || data.isEmpty()) {
			logger.info("[JsonRuleEngine].isTriggered : data is null");
			return false;
		}
		
		// 1) single quotes are changed double quotes
		String tmpTrigger = RuleUtil.replaceSingleQuotesToDoubleQuotes(new String(trigger));
		
		logger.info("[JsonRuleEngine].isTriggered : tmpTrigger = " + tmpTrigger);
		
		// 2) get Rules
		List<String> ruleTypes = Rules.getRuleTypes();
		
		logger.info("[JsonRuleEngine].isTriggered : ruleTypes = " + ruleTypes.toString());
		
		for (String ruleType : ruleTypes) {
			//
			RULE rule = Rules.getRuleByType(ruleType);
			
			tmpTrigger = rule.replaceRuleExpressionWithData(tmpTrigger, data);
		}
		
		if (tmpTrigger != null)
			return evaluateTrigger(tmpTrigger);
		else
			return false;
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
	
	private String makeResult(String result, String data) {
		//
		// TODO
		
		return null;
	}
	
	
	
	
	
	

//    /**
//     * Method Name : getResult
//     * Method Desc : result �젙蹂� ?��붿텧
//     * @param function
//     * @param data
//     * @return
//     */
//    private Object getResult(String function, String data) {
//        //
//        Object ret = null;
//        List<String> functions = Functions.getFunctions();
//        
//        for (String iter : functions) {
//            //
//            Pattern p = Pattern.compile(Functions.getFunctionRegExp(iter));
//            Matcher m = p.matcher(function);
//            
//            while(m.find()) {
//                String matchedFunction = m.group();
//                ret = Functions.doFunction(iter, matchedFunction, data);
//            }
//        }
//        
//        return ret;
//    }
	
	
	
  
}