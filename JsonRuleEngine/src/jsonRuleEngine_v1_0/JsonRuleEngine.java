package jsonRuleEngine_v1_0;

import java.util.List;

import jsonRuleEngine_v1_0.data.JsonRuleEngineConfigs;

public interface JsonRuleEngine {
	
	public void insertConfigs(String jsonRuleEngineConfigs);
	
	public void insertConfigs(JsonRuleEngineConfigs jsonRuleEngineConfigs);
	
	public JsonRuleEngineConfigs getConfigs();
	
	public List<String> execute(String jsonString);
}