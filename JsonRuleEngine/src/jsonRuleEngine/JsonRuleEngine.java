package jsonRuleEngine;

import jsonRuleEngine.data.JsonRuleEngineConfigs;

public interface JsonRuleEngine {
	
	public void insertConfigs(String jsonRuleEngineConfigs);
	
	public void insertConfigs(JsonRuleEngineConfigs jsonRuleEngineConfigs);
	
	public JsonRuleEngineConfigs getConfigs();
}