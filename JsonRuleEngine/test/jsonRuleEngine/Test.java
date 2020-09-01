package jsonRuleEngine;

import java.util.List;

public class Test {
	
	public static void main(String[] args) {
		//
		String sampleJsonString1 = "{\r\n" + 
				"	\"firstName\" : \"John\",\r\n" + 
				"	\"lastName\" : \"Smith\",\r\n" + 
				"	\"age\" : 20,\r\n" + 
				"	\"address\" : {\r\n" + 
				"		\"streetAddress\" : \"21 2nd Street\",\r\n" + 
				"		\"city\" : \"New York\",\r\n" + 
				"		\"state\" : \"NY\",\r\n" + 
				"		\"postalCode\" : \"10021\"\r\n" + 
				"	},\r\n" + 
				"	\"phoneNumber\" : [\r\n" + 
				"		{\r\n" + 
				"			\"type\" : \"home\",\r\n" + 
				"			\"number\" : \"212 555-1234\"\r\n" + 
				"		},\r\n" + 
				"		{\r\n" + 
				"			\"type\" : \"fax\",\r\n" + 
				"			\"number\" : \"646 555-4567\"\r\n" + 
				"		}\r\n" + 
				"	]\r\n" + 
				"}";
		
		String sampleJsonString2 = "{\r\n" + 
				"	\"firstName\" : \"John\",\r\n" + 
				"	\"lastName\" : \"Smith\",\r\n" + 
				"	\"age\" : 30,\r\n" + 
				"	\"address\" : {\r\n" + 
				"		\"streetAddress\" : \"21 2nd Street\",\r\n" + 
				"		\"city\" : \"Illinois\",\r\n" + 
				"		\"state\" : \"NY\",\r\n" + 
				"		\"postalCode\" : \"10021\"\r\n" + 
				"	},\r\n" + 
				"	\"phoneNumber\" : [\r\n" + 
				"		{\r\n" + 
				"			\"type\" : \"home\",\r\n" + 
				"			\"number\" : \"212 555-1234\"\r\n" + 
				"		},\r\n" + 
				"		{\r\n" + 
				"			\"type\" : \"fax\",\r\n" + 
				"			\"number\" : \"646 555-4567\"\r\n" + 
				"		}\r\n" + 
				"	]\r\n" + 
				"}";
		
		String sampleJsonString3 = "[1,2,3,4]";
		
		// 1) Alphabet and Numeric only in String at expression of trigger
		// 
		String jsonRuleEngineConfigs = "{\r\n" + 
				"	\"configs\" : [\r\n" + 
				"		{\r\n" + 
				"			\"trigger\" : \"((PAYLOAD_VALUE(address.city) == 'New York') && (PAYLOAD_VALUE(age) == 20))\",\r\n" + 
				"			\"result\" : \"PAYLOAD_VALUE()\"\r\n" + 
				"		},\r\n" +
				"		{\r\n" + 
				"			\"trigger\" : \"((PAYLOAD_VALUE_WITH(type, PAYLOAD_ARRAY(phoneNumber, 0)) == 'home') && (PAYLOAD_VALUE(address.city) == 'Illinois'))\",\r\n" +
				"			\"result\" : \"{'type':'alarm', 'info':PAYLOAD_VALUE(address.city)}\"\r\n" + 
				"		},\r\n" +
				"		{\r\n" + 
				"			\"trigger\" : \"PAYLOAD_ARRAY(, 0) == 1\",\r\n" +
				"			\"result\" : \"PAYLOAD_ARRAY(, 2)\"\r\n" + 
				"		},\r\n" +
				"		{\r\n" + 
				"			\"trigger\" : \"((PAYLOAD_VALUE(address.city) == 'Illinois') && (PAYLOAD_VALUE(age) == 30))\",\r\n" + 
//				"			\"result\" : \"PAYLOAD_VALUE()\"\r\n" + 
				"			\"result\" : \"PAYLOAD_VALUE(phoneNumber)\"\r\n" +
				"		}\r\n" + 
				"	]\r\n" + 
				"}";
			
		JsonRuleEngine jsonRuleEngine = new JsonRuleEngineImpl();
		
		jsonRuleEngine.insertConfigs(jsonRuleEngineConfigs);

		System.out.println(jsonRuleEngine.getConfigs().toString());
		
		List<String> jsonRetStringList = null;
		
		for (int i = 0 ; i < 10 ; i++) {
			Long startTime = System.nanoTime();
			
			jsonRetStringList = jsonRuleEngine.execute(sampleJsonString1);
			System.out.println(jsonRetStringList.toString());
			
			jsonRetStringList = jsonRuleEngine.execute(sampleJsonString2);
			System.out.println(jsonRetStringList.toString());
			
			jsonRetStringList = jsonRuleEngine.execute(sampleJsonString3);
			System.out.println(jsonRetStringList.toString());
			
			Long endTime = System.nanoTime();
			Long diffTime = endTime - startTime;
			System.out.println(diffTime + "(ns)");
			System.out.println(diffTime / 1000000 + "(ms)");
		}
	}
}