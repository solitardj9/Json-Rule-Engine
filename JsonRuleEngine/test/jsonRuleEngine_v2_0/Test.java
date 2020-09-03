package jsonRuleEngine_v2_0;

import java.util.List;

import jsonRuleEngine_v2_0.data.JsonRuleEngineResultSet;

public class Test {
	
	public static void main(String[] args) {
		//
		String status = "{\r\n" + 
				"			\"siteId\": \"1234\",\r\n" + 
				"			\"eqpId\": \"5678\",\r\n" + 
				"			\"eqpType\": \"ESS\",\r\n" + 
				"			\"data\": {\r\n" + 
				"				\"onoff\": \"on\",\r\n" + 
				"				\"setTemp\": 30,\r\n" + 
				"				\"roomTemp\": 24,\r\n" + 
				"				\"errorCode\": 10,\r\n" + 
				"				\"values\": [1,2,3,4]\r\n" +
				"			}\r\n" + 
				"		}";
		
		String status2 = "{\r\n" + 
				"			\"siteId\": \"4567\",\r\n" + 
				"			\"eqpId\": \"7890\",\r\n" + 
				"			\"eqpType\": \"ESS\",\r\n" + 
				"			\"data\": {\r\n" + 
				"				\"onoff\": \"on\",\r\n" + 
				"				\"setTemp\": 30,\r\n" + 
				"				\"roomTemp\": 24,\r\n" + 
				"				\"errorCode\": 10,\r\n" + 
				"				\"values\": [1,2,3,4]\r\n" +
				"			}\r\n" + 
				"		}";
		
		
		String jsonRuleEngineConfigs = "{\r\n" + 
				"	\"configs\" : [\r\n" + 
				"		{\r\n" + 
				"			\"trigger\" : \"((read($.eqpType) == 'ESS') && (read($.data.errorCode) != 0) && (read($.data.values[0]) == 1))\",\r\n" +
				"			\"result\" : {\"siteId\":\"read($.siteId)\", \"eqpId\":\"read($.eqpId)\", \"errorCode\":\"read($.data.errorCode)\"},\r\n" + 
				"			\"event\" : {\"eventType\" : \"alarm\"}\r\n" +
				"		}\r\n" +
				"	]\r\n" + 
				"}";
			
		JsonRuleEngine jsonRuleEngine = new JsonRuleEngineImpl();
		
		jsonRuleEngine.insertConfigs(jsonRuleEngineConfigs);

		System.out.println(jsonRuleEngine.getConfigs().toString());
		
		List<JsonRuleEngineResultSet> results=null;
		
		for (int i = 0 ; i < 10 ; i++) {
			Long startTime = System.nanoTime();
			
			results=jsonRuleEngine.execute(status);
			System.out.println(results.toString());
			
			results= jsonRuleEngine.execute(status2);
			System.out.println(results.toString());
			
			Long endTime = System.nanoTime();
			Long diffTime = endTime - startTime;
			System.out.println("total time = " + diffTime + "(ns)");
			System.out.println("total time = " + diffTime / 1000000 + "(ms)");
			System.out.println("total time(avg) = " + diffTime / 1000000 / 2 + "(ms)");
		}
		
	}
}