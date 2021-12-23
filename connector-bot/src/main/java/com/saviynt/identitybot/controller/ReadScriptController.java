package com.saviynt.identitybot.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.saviynt.identitybot.Service.ReadScriptService;
import com.saviynt.identitybot.model.ReadScript;
import com.saviynt.identitybot.model.ScriptKey;

@RestController
@RequestMapping("/readscript")
public class ReadScriptController {
	private static final Logger logger = LoggerFactory.getLogger(ReadScriptController.class);
	@Autowired
	private ReadScriptService service;

	@RequestMapping(value = "/updatekey", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> updateKey(@RequestBody ScriptKey script) {

		logger.info("updateKey method entered");
		logger.info("Recevied Key Values:- " + script.getKeyValue());
		if (script.getKeyValue() != null) {
			String arr[] = script.getKeyValue().split("#");
			if (arr != null && arr.length > 0) {

				for (String KeyVal : arr) {
					if (KeyVal.contains("-")) {
						ReadScript readScript = new ReadScript();
						readScript.setAppName(script.getAppName());
						readScript.setScriptName(script.getScriptName());
						readScript.setSuiteName(script.getSuiteName());
						readScript.setKeyName(KeyVal.split("-")[0]);
						readScript.setKeyValue(KeyVal.split("-")[1]);
						List<String> keyList = new ArrayList<>();
						keyList.add(KeyVal.split("-")[0]);
						List<ReadScript> rspList = service.getKeyValues(script.getAppName(), script.getScriptName(),
								keyList);
						logger.info("ReadScript Object list:- " + rspList);
						if (rspList != null && rspList.size() > 0) {
							readScript = rspList.get(0);
							readScript.setKeyValue(KeyVal.split("-")[1]);
						}
						service.save(readScript);
					}
				}
			}
		}
		logger.info("updateKey method completed");
		return new ResponseEntity<>("sucess", HttpStatus.OK);
	}

	@RequestMapping(value = "/key", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> getKeyVal(@RequestBody ScriptKey script) throws Exception {

		logger.info("getKeyVal method entered");
		logger.info("Recevied Key Values:- " + script.getKeyValue());
		
		String listString = "";
		if (script.getKeyValue() != null) {
			List<String> keyList = Arrays.asList(script.getKeyValue().split("#"));

			List<ReadScript> rspList = service.getKeyValues(script.getAppName(), script.getScriptName(), keyList);
			if(rspList!=null && !rspList.isEmpty()) {
				List<String> respList = rspList.stream().map(rsp -> rsp.getKeyName() + "-" + rsp.getKeyValue())
						.collect(Collectors.toList());
				listString = String.join("#", respList);
			}
		}
		if(listString.isEmpty()) {
			throw new Exception("Please check setvalue script executed or not");
		}
		logger.info("getKeyVal method completed");
		return new ResponseEntity<>(listString, HttpStatus.OK);
	}

}
