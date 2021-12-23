package com.saviynt.identitybot.Service;

import org.json.JSONObject;

import com.saviynt.identitybot.controller.Status;

public interface SSMAuthService {

	Status getAuthToken(String username, String password);

	JSONObject sendTaskCompleteResponseBackToSSM(String jobId, String accessToken, String jobStatus);
	
	JSONObject sendTaskUpdateResponseBackToSSM(String jobId, String accessToken, String jobStatus,String comment);

	JSONObject createConnectionJSON(JSONObject jsonObjecte, String accessToken, String connectionName,String connectionType);

	JSONObject createSecuritySystem(JSONObject jsonObjecte, String accessToken);
}