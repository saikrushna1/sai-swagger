package com.saviynt.identitybot.Service;

import static org.junit.Assert.assertFalse;
import java.io.File;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.saviynt.identitybot.Service.Impl.SSMAuthServiceImpl;
import com.saviynt.identitybot.Util.CLIRecorder;
import com.saviynt.identitybot.Util.Constants;
import com.saviynt.identitybot.controller.Status;
import com.saviynt.identitybot.mapper.JobUIMapper;
import com.saviynt.identitybot.model.SSMConnections;
import com.saviynt.identitybot.repository.BotAgentRepository;
import com.saviynt.identitybot.repository.SSMConnectionRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SSMAuthServiceImplTest {
	
	
	@Autowired
	SSMConnectionRepository ssmConnectionRepository;

	@Autowired
	SSMAuthService authService;

	@Value("${scripts.dir}")
	String scriptsRootDir;

	@Value("${cobot.createconnectionjson.url}")
	String cobotCreateconnectionJSONUrl;
	
	@Value("${cobot.cliRecordconnectionJSONUrl.url}")
	String cliRecordconnectionJSONUrl;

	@Value("${ssm.username}")
	String ssmUsername;

	@Value("${ssm.password}")
	String ssmPassword;

	@Value("${cobot.scriptType}")
	String cobotScriptType;

	@Value("${testcases.root}")
	String testCaseRoot;
	
	@Value("${scripts.store}")
	String scriptsStore;

	@Autowired
	BotAgentRepository botAgentRepository;

	@Autowired
	JobUIMapper mapper;

	@Autowired
	CLIRecorder cliRecorder;
	
	@Test
	public void testgetAuthToken() {
		SSMAuthService service = new SSMAuthServiceImpl();
		Status response = service.getAuthToken(ssmUsername, ssmPassword);
		assertFalse(response != null);		
	}
	

	@Test
	public void testsendTaskCompleteResponseBackToSSM() {
		SSMAuthService service = new SSMAuthServiceImpl();
		Status token = service.getAuthToken(ssmUsername, ssmPassword);
		assertFalse(token != null);
		
		String jobId = "1";
		JSONObject response = service.sendTaskUpdateResponseBackToSSM(jobId, token.getStatus(), Constants.SUCCESS,"");
		assertFalse(response != null);
	}
	
	@Test
	public void testCreateConnectionJSON() throws IOException {
		JSONObject finalCliParams = new JSONObject();
		File file = File.createTempFile("temp",".java");
		String fileNameWithExt = file.getAbsolutePath();
		String[] fileNameArry = fileNameWithExt.split("\\.");
		String fileName = fileNameArry[0];
		SSMConnections  cononections = ssmConnectionRepository.findByScriptFileNameAndConnectionNameAndScriptType("","" , "");
		JSONObject connectionJson = new JSONObject();
		JSONObject responseColsToPropsMap = new JSONObject();
		JSONArray call = new JSONArray();

		JSONObject callObj = new JSONObject();
		JSONObject httpParams = new JSONObject();
		JSONObject httpHeaders = new JSONObject();

		connectionJson.put("accountIdPath", "Call1.message.value[0].taskid");
		connectionJson.put("dateFormat", "");
		connectionJson.put("responseColsToPropsMap", responseColsToPropsMap);

		callObj.put("name", cononections.getConnectionName());
		callObj.put("connection", cononections.getConnectionName());
		callObj.put("url", cliRecordconnectionJSONUrl);
		callObj.put("httpMethod", "POST");
		callObj.put("httpContentType", "application/json");

		httpParams.put("File", file.getName());
		httpParams.put("recordVideo", cononections.getRecordVideo());
		httpParams.put("format", cononections.getVideoFormat());
		httpParams.put("cliParams", finalCliParams);
		httpParams.put("taskid", "${arsTasks.id}");

		httpHeaders.put("Authorization", "Basic YWRtaW46cGFzc3dvcmQ=");

		callObj.put("httpParams", httpParams);
		callObj.put("httpHeaders", httpHeaders);
		call.put(callObj);
		connectionJson.put("call", call);
		
		SSMAuthService service = new SSMAuthServiceImpl();
		Status accessToken = service.getAuthToken(ssmUsername, ssmPassword);
		assertFalse(accessToken != null);
		
		String connectionName = "connectionName";
		service.createConnectionJSON(connectionJson, accessToken.getStatus(), connectionName,"");
	}
}
