package com.saviynt.identitybot.script.model;

public class ScriptKey {

	private String scriptName;
	private String appName;
	private String suiteName;
	private String keyValue;

	public ScriptKey() {
		
	}
	public ScriptKey(String scriptName, String appName, String keyValue) {
		
		this.scriptName = scriptName;
		this.appName = appName;
		this.keyValue = keyValue;
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getSuiteName() {
		return suiteName;
	}

	public void setSuiteName(String suiteName) {
		this.suiteName = suiteName;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

}
