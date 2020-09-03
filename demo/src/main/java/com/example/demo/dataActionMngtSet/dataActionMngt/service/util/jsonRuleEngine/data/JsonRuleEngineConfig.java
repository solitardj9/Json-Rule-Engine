package com.example.demo.dataActionMngtSet.dataActionMngt.service.util.jsonRuleEngine.data;

public class JsonRuleEngineConfig {
	//
	private String trigger;
	
	private Object result;
	
	private Object event;
	
	public JsonRuleEngineConfig() {
		
	}
	
	public JsonRuleEngineConfig(JsonRuleEngineConfig object) {
		setTrigger(new String(object.getTrigger()));
		setResult(object.getResult());
		setEvent(object.getEvent());
	}
	
	public JsonRuleEngineConfig(String trigger, Object result, Object event) {
		this.trigger = trigger;
		this.result = result;
		this.event = event;
	}

	public JsonRuleEngineConfig(String trigger, String result) {
		this.trigger = trigger;
		this.result = result;
	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public Object getEvent() {
		return event;
	}

	public void setEvent(Object event) {
		this.event = event;
	}

	@Override
	public String toString() {
		return "JsonRuleEngineConfig [trigger=" + trigger + ", result=" + result + ", event=" + event + "]";
	}
}
