package com.saviynt.identitybot.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saviynt.identitybot.model.ReadScript;
import com.saviynt.identitybot.repository.ReadScriptRepository;

@Service
public class ReadScriptService {

	@Autowired
	private ReadScriptRepository repository;

	public List<ReadScript> getKeyValues(String appName, String scriptName,List<String> keys) {
		return repository.findByKeyName(appName,scriptName,keys);
	}

	public void save(ReadScript script) {
		repository.save(script);
	}

}
