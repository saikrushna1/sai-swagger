package com.saviynt.identitybot.Service.Impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.saviynt.identitybot.Service.InvokeService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.saviynt.identitybot.Util.Constants;
import com.saviynt.identitybot.constants.STATUS;
import com.saviynt.identitybot.controller.Status;

@Service
public class InvokeServiceImpl implements InvokeService {
	final static Logger logger = Logger.getLogger(InvokeServiceImpl.class);
	public Status invokePostCall(String postUrl, String inputJson) throws Exception {
		Status status = new Status();	
		logger.info("postUrl = "+postUrl);
		logger.info("inputJson = "+inputJson);
		URL url = new URL(postUrl);
		
		logger.info("created HttpURLConnection connection object");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod(Constants.POST);
		con.setRequestProperty(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON + Constants.SEMI_COLON + Constants.UTF_8);
		con.setRequestProperty(Constants.ACCEPT, Constants.APPLICATION_JSON);
		con.setDoOutput(true);
		
		logger.info("assigned inputJson to HttpURLConnection connection object");
		try (OutputStream os = con.getOutputStream()) {
			byte[] input = inputJson.getBytes(Constants.UTF_8);
			os.write(input, 0, input.length);
		}
		int code = con.getResponseCode();
		logger.info("HttpURLConnection Object Response Code = "+code);
		// IF RESPONSE CODE IS 200
		if (code == STATUS.Success.getID()) {
			logger.info("reading response reader");
			try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), Constants.UTF_8))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				status.setJobId(String.valueOf(STATUS.Success.getID()));
			} catch (Exception e) {
				logger.error("ERROR >>> while reading the response "+e.getMessage());			
				status.setJobId(String.valueOf(STATUS.Failed.getID()));
				status.setReason(e.getMessage());
			}
		} else if (code == STATUS.Unauthorized.getID()) {
			logger.info("Unauthorized");
			status.setJobId(String.valueOf(code));
			status.setReason(Constants.UNAUTHORIZED_MSG);
		} else if (code == STATUS.Forbidden.getID()) {
			logger.info("Forbidden");
			status.setJobId(String.valueOf(code));
			status.setReason("No permissions for this inputJson = " + inputJson);
		} else if (code == STATUS.UNKNOWN.getID()) {
			logger.info("Service Unavailable");
			status.setJobId(String.valueOf(code));
			status.setReason("Service Unavailable");
		}
		return status;      
	}
}