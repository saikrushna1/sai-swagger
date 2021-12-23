package com.saviynt.identitybot.script.client;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.saviynt.identitybot.script.model.ScriptKey;

@Service
public class TestBotClient {

	private static final Logger logger = LoggerFactory.getLogger(TestBotClient.class);

	@Autowired
	RestTemplate restTemplate;

	@Value("${cobot.server.url}")
	String cobotServer;

	@Value("${testbot.userid}")
	String testBotUserId;

	@Value("${testbot.password}")
	String testBotPassword;

	public void updateKeyValue(ScriptKey script) {
		logger.info("updatekeyValue method entered");
		//String authStr = "chette.rajkumar@gmail.com" + ":" + "123456";
		 String authStr = testBotUserId+":"+testBotPassword;
		String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
		logger.info("Basic " + base64Creds);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);
		headers.add("Accept", "application/json");
		headers.add("Content-Type", "application/json");
		org.springframework.http.HttpEntity<Object> requestEntity = new org.springframework.http.HttpEntity<Object>(
				script, headers);
		logger.info("Before Cobot Server calling");
		ResponseEntity<String> responseFromApi = restTemplate.exchange(cobotServer + "readscript/updatekey",
				HttpMethod.POST, requestEntity, String.class);
		logger.info("After Cobot Server response");
		logger.info("updatekeyValue method completed");
	}

	public String readKeyValue(ScriptKey script) {
		logger.info("readKeyValue method entered");
		//String authStr = "chette.rajkumar@gmail.com" + ":" + "123456";
		String authStr = testBotUserId+":"+testBotPassword;
		String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);
		headers.add("Accept", "application/json");
		headers.add("Content-Type", "application/json");
		logger.info("Basic " + base64Creds);
		logger.info("Before Cobot Server calling");
		org.springframework.http.HttpEntity<Object> requestEntity = new org.springframework.http.HttpEntity<Object>(
				script, headers);
		ResponseEntity<String> responseFromApi = restTemplate.exchange(cobotServer + "readscript/key", HttpMethod.POST,
				requestEntity, String.class);
		logger.info("After Cobot Server response");
		logger.info("readKeyValue method completed");
		return responseFromApi.getBody();
	}

}
