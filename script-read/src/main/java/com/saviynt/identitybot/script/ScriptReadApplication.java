package com.saviynt.identitybot.script;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.saviynt.identitybot.script.client.TestBotClient;
import com.saviynt.identitybot.script.config.AppConfig;
import com.saviynt.identitybot.script.model.ScriptKey;

public class ScriptReadApplication {

	private static final Logger logger = LoggerFactory.getLogger(ScriptReadApplication.class);

	public static void setValue(String scriptName, String appName, String key) {
		
		logger.info("setValue method entered");
		
		TestBotClient client = getInstance();
		ScriptKey script = new ScriptKey(scriptName, appName, key);
		client.updateKeyValue(script);
		
		logger.info("setValue method completed");
	}

	public static Map<String, String> getValue(String scriptName, String appName, String key)  {
		logger.info("getValue method entered");
		Map<String, String> map = new HashMap<String, String>();
		TestBotClient client = getInstance();
		ScriptKey script = new ScriptKey(scriptName, appName, key);
		String keyValue = client.readKeyValue(script);
		logger.info("Response recevied from COBOT:-" + keyValue);
		if (keyValue != null) {
			String arr[] = keyValue.split("#");
			if (arr != null && arr.length > 0) {
				for (String KeyVal : arr) {
					map.put(KeyVal.split("-")[0], KeyVal.split("-")[1]);
				}
			}
		}

		logger.info("getValue method Completed");
		return map;

	}

	private static TestBotClient getInstance() {

		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
		TestBotClient client = applicationContext.getBean(TestBotClient.class);
		return client;
	}

	public static void main(String arg[]) {
		ScriptReadApplication ap = new ScriptReadApplication();
		//ap.setValue("Search", "Global", "name3-test3#name4-test5#name6-test6#name7-test7");
		try {
			Map<String, String> map = ap.getValue("Search", "Global", "name8");
			System.out.println(map.get("name8"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		//ap.getValue("script1", "test",  "OrderID1#OrderID2#OrderID3");
	}

}
