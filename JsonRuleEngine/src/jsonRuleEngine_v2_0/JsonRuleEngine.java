package jsonRuleEngine_v2_0;

import java.util.List;

import jsonRuleEngine_v2_0.data.JsonRuleEngineConfigs;
import jsonRuleEngine_v2_0.data.JsonRuleEngineResultSet;

public interface JsonRuleEngine {
	
	public void insertConfigs(String jsonRuleEngineConfigs);
	
	public void insertConfigs(JsonRuleEngineConfigs jsonRuleEngineConfigs);
	
	public JsonRuleEngineConfigs getConfigs();
	
	public List<JsonRuleEngineResultSet> execute(String jsonString);
}