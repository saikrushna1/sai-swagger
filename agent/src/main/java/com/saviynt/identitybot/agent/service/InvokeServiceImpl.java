package com.saviynt.identitybot.agent.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.saviynt.identitybot.agent.constants.Constants;



@Service
public class InvokeServiceImpl implements InvokeService {
	public void invokePostCall(String postUrl, String inputJson) throws Exception {

		URL url = new URL(postUrl);

		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod(Constants.POST);

		con.setRequestProperty(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON + Constants.SEMI_COLON + Constants.UTF_8);
		con.setRequestProperty(Constants.ACCEPT, Constants.APPLICATION_JSON);

		con.setDoOutput(true);

		try (OutputStream os = con.getOutputStream()) {
			byte[] input = inputJson.getBytes(Constants.UTF_8);
			os.write(input, 0, input.length);
		}

		try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), Constants.UTF_8))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}