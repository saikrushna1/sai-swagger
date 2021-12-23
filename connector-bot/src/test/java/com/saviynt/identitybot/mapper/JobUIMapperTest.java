package com.saviynt.identitybot.mapper;

import static org.junit.Assert.assertTrue;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.JsonArray;
import com.saviynt.identitybot.mapper.JobUIMapper;
import com.saviynt.identitybot.model.BotAgent;
import com.saviynt.identitybot.repository.BotAgentRepository;
import com.saviynt.identitybot.response.BotAgentsResponse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JobUIMapperTest {

	@Autowired
	BotAgentRepository botAgentRepository;
	
	@Autowired
	JobUIMapper mapper;
	
	@Test
	public void testUpdateStatus() {
		List<BotAgent> botAgentsList = botAgentRepository.findAll();
		List<BotAgentsResponse> response = mapper.updateStatus(botAgentsList);
		assertTrue(!response.isEmpty());
	}
	
	@Test
	public void testUpdateStatusJson() {
		
		List<BotAgent> botAgentsList = botAgentRepository.findAll();
		JsonArray jsonArray = mapper.updateStatusJson(botAgentsList);
		assertTrue(!jsonArray.isJsonNull());
	}
	
	@Test
	public void testPrepareScriptTypes() {
		LinkedHashMap<String, LinkedHashMap<String, List<String>>> filesList = mapper.prepareScriptTypes();
		assertTrue(filesList.containsKey("WEB"));
		assertTrue(filesList.containsKey("CLI"));
	}
}
