package jsonRuleEngine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jsonRuleEngine.data.JsonRuleEngineConfigs;

public class JsonRuleEngineImpl implements JsonRuleEngine {
	//
	private static Logger logger = LoggerFactory.getLogger(JsonRuleEngineImpl.class);
	
	private JsonRuleEngineConfigs jsonRuleEngineConfigs;
	
	private static ObjectMapper om = new ObjectMapper();
	
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

}