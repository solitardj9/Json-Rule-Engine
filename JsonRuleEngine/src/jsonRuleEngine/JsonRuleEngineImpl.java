package jsonRuleEngine;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jsonRuleEngine.data.JsonRuleEngineConfig;
import jsonRuleEngine.data.JsonRuleEngineConfigs;
import jsonRuleEngine.rule.Rules;

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
	
	

	private Boolean isTriggered(String trigger, String data) {
		//
		if (data == null || data.isEmpty()) {
			logger.info("[JsonRuleEngine].isTriggered : data is null");
			return false;
		}
		
		String tmpTrigger = replaceSingleQuotesToDoubleQuotes(new String(trigger));
		
		logger.info("[JsonRuleEngine].isTriggered : tmpTrigger = " + tmpTrigger);
		
		List<String> rules = Rules.getRules();
		
		logger.info("[JsonRuleEngine].isTriggered : rules = " + rules.toString());
		 
		
		// TODO
		
		
		
        
        
//        for (String iter : functions) {
//            //
////            System.out.println("[DataActionWorker].checkTrigger : trigger=" + trigger);
//            
//            Pattern p = Pattern.compile(Functions.getFunctionRegExp(iter));
//            Matcher m = p.matcher(trigger);
//            
//            while(m.find()) {
//                String matchedFunction = m.group();
//                replacedTrigger = Functions.replaceFunction(iter, replacedTrigger, matchedFunction, data);
//            }
//            
////            System.out.println("[DataActionWorker].checkTrigger : replacedTrigger=" + replacedTrigger);
//        }
//        
//        if(replacedTrigger != null)
//            return evaluateTrigger(replacedTrigger);
//        else
//            return false;
		
		return null;		
	}
	
	private String makeResult(String result, String data) {
		//
		
		// TODO
		
		return null;
	}
	
	
	
	
	
	
//	
//    
//    /**
//     * Method Name : evaluateTrigger
//     * Method Desc : Trigger 寃��궗 (�끉由� �뿰�궛 臾몄옄�뿴 �솗�씤)
//     * @param trigger
//     * @return
//     */
//    private Boolean evaluateTrigger(String trigger) {
//        //
//        String regexp = "[!@#$%^&*(),?:{}|<=>]";
//        
//        try {
//            Boolean result = null;
//            
//            //System.out.println("[DataActionWorker].evaluateTrigger : trigger=" + trigger);
//            String tmpTrigger = trigger.replace("\"", "");
//            //System.out.println("[DataActionWorker].evaluateTrigger : tmpTrigger=" + tmpTrigger);
//            
//            Pattern pattern = Pattern.compile(regexp);
//            String[] operands = pattern.split(tmpTrigger);
//            
//            List<String> params = new ArrayList<>();
//            for (String iter : operands) {
//                if (iter != null && !iter.equals("")  && !iter.equals(" ")) {
//                    params.add(iter);
//                }
//            }
//            
//            // Now here's where the story begins...
//            ExpressionEvaluator ee = new ExpressionEvaluator();
//            
//            // The expression will have two "int" parameters: "a" and "b".
//            Map<String, String> strMap = new HashMap<>();
//            String[] arrParams = new String[params.size()];
//            Class[] arrClasses = new Class[params.size()];
//            Object[] arrObjects = new Object[params.size()];
//            
//            int count = 0;
//            for (String iter : params) {
//                String tmpParam = ("p" + count);
//                arrParams[count] = tmpParam;
//                arrClasses[count] = String.class;
//                
//                String value = iter.trim();
//                if (!isNumeric(value)) {
//                    String strValue = "\"" + value + "\"";
//                    strMap.put(value, strValue);
//                }
//                
//                arrObjects[count] = value;
//                count++;
//            }
//            
////            System.out.println("[DataActionWorker].evaluateTrigger : tmpTrigger=" + tmpTrigger);
////            System.out.println("[DataActionWorker].evaluateTrigger : strMap=" + strMap.toString());
//            for (Entry<String, String> entry :strMap.entrySet()) {
//                tmpTrigger = tmpTrigger.replace(entry.getKey(), entry.getValue());
//            }
//            
//            //System.out.println("[DataActionWorker] checkTrigger : arrParams : ");
//            //for (String iter : arrParams) System.out.println(iter);
//            //System.out.println("[DataActionWorker] checkTrigger : arrObjects : ");
//            //for (Object iter : arrObjects) System.out.println(iter.toString());
//            
//            // The expression will have two "int" parameters: "a" and "b".
//            ee.setParameters(arrParams, arrClasses);
//            
//            // And the expression (i.e. "result") type is also "int".
//            ee.setExpressionType(Boolean.class);
//            
//            // And now we "cook" (scan, parse, compile and load) the fabulous expression.
//            //System.out.println("[DataActionWorker].evaluateTrigger :" + tmpTrigger);
//            ee.cook(tmpTrigger);
//            
//            // Eventually we evaluate the expression - and that goes super-fast.
//            result = (Boolean) ee.evaluate(arrObjects);
//            
////            System.out.println("[DataActionWorker].evaluateTrigger : trigger=" + trigger);
////            System.out.println("[DataActionWorker].evaluateTrigger : tmpTrigger=" + tmpTrigger);
////            System.out.println("[DataActionWorker].evaluateTrigger : result=" + result);
//            
//            return result;
//        }
//        catch (Exception e) {
//            System.out.println("[DataActionWorker].evaluateTrigger : " + e.toString());
//            return null;
//        }
//    }
//    
//    /**
//     * Method Name : isNumeric
//     * Method Desc : 臾몄옄�뿴 �닽�옄 �솗�씤
//     * @param string
//     * @return
//     */

//    
//    /**
//     * Method Name : getResult
//     * Method Desc : result �젙蹂� 異붿텧
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
	
	private String replaceSingleQuotesToDoubleQuotes(String data) {
		//
		return data.replace("'","\"");
	}
	
  
}