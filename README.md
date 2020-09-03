# Json-Rule-Engine

## Generates a message and an event when trigger is activated
1) Input is Json document.
2) Output Message & Event are Objcts or Json Documents.

## 1. Configuration
> 1) described by Json format.
> 2) parameters.

> > configs : Json array - can register more than one config pair (trigger, result, event).

> > trigger : describe spepcific conditions. read value from json document (reference : https://github.com/json-path/JsonPath).

> > result : Object or Json Object - can define a message which is output when trigger is activated.

> > event : Object or Json Object - can define a event which is output when trigger is activated.

> example)
<pre>
<code>
// this is a sample to trigger result and evnet when AC has a error code which is not equals to 0.
// trigger is to compare equipment's type and error code.
// result is json object.
// event is simple string.
{
	"configs" : [
		{
			"trigger" : "((read($.eqpType) == 'AC') && (read($.data.errorCode) != 0) && (read($.data.values[0]) == 1))",
			"result" : {
				"eqpId" : "read($.eqpId)", 
				"errorCode" : "read($.data.errorCode)"
			},
			"event" : {
				"eventType" : "alarm"
			}
		}
	]
}

// Both available
// (read($.eqpType) == 'AC'
// (read($.eqpType) == \"AC\"
</code>
</pre>

## 2. Examples
###     2.1 Insert configuration
<pre>
<code>
String configs = "{...}";

JsonRuleEngine jsonRuleEngine = new JsonRuleEngineImpl();
jsonRuleEngine.insertConfigs(configs);
</code>
</pre>

###     2.2 Input Data Sample
<pre>
<code>
{
	"data" : {
		"onoff" : "on",
		"setTemp" : 30,
		"roomTemp" : 24,
		"errorCode" : 10,
		"values": [1,2,3,4]
	},
	"eqpId":"{eqpId}",
	"eqpType":"AC"
}
</code>
</pre>

###     2.3 Execute Rule Engine
<pre>
<code>
String inputData = "{...}"

List<JsonRuleEngineResultSet> results = jsonRuleEngine.execute(inputData);
</code>
</pre>

###     2.4 Result Set Class
<pre>
<code>
public class JsonRuleEngineResultSet {
	private Object result;
	private Object event;
	...
}
</code>
</pre>

###     2.5 Result Data Sampale
<pre>
<code>
...
for (JsonRuleEngineResultSet iter : results) {
	logger.info(iter.toString());
}

// print output
[JsonRuleEngineResultSet [result={"errorCode":10,"eqpId":"{eqpId}"}, event={eventType=alarm}]]
</code>
</pre>

	


