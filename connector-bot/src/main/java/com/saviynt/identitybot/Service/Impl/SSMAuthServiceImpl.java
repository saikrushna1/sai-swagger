package com.saviynt.identitybot.Service.Impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.saviynt.identitybot.Service.SSMAuthService;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.saviynt.identitybot.Util.Constants;
import com.saviynt.identitybot.constants.STATUS;
import com.saviynt.identitybot.controller.Status;


@Service
public class SSMAuthServiceImpl implements SSMAuthService {
	final static Logger logger = Logger.getLogger(SSMAuthServiceImpl.class);

	@Value("${ssmurl}")
	String ssmServerURL;

	@Value("${ssm.username}")
	String ssmUsername;

	@Value("${ssm.password}")
	String ssmPassword;
	
	@Override
	public Status getAuthToken(String username, String password) {
		String token = null;
		Status status = new Status();
		try {
			logger.info("Enter into getAuthToken");
			//logger.info("user = "+username+" and password = "+password);
			
			String ssmurl = ssmServerURL;
			logger.info("ssmurl = "+ssmurl);
			
			URL url = new URL(ssmurl + Constants.API_LOGIN);
			JSONObject inputJson=new JSONObject("{\"username\":\""+username+"\",\"password\":\""+password+"\"}");
			//logger.info("inputJson = "+inputJson);
			
			logger.info("Creating and Assigning request parameters to HttpURLConnection Object");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod(Constants.POST);
			con.setRequestProperty(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON + Constants.SEMI_COLON + Constants.UTF_8);
			con.setRequestProperty(Constants.ACCEPT, Constants.APPLICATION_JSON);
			con.setDoOutput(true);
			//logger.info("Created and assigned request parameters to HttpURLConnection Object = "+con.toString());
			
			try (OutputStream os = con.getOutputStream()) {
				byte[] input = inputJson.toString().getBytes(Constants.UTF_8);
				os.write(input, 0, input.length);
			}

			int code = con.getResponseCode();
			logger.info("HttpURLConnection Object Response Code = "+code);
			// IF RESPONSE CODE IS 200
			if (code == STATUS.Success.getID()) {
				
				logger.info("Reading HttpURLConnection Response InputStream");
				try (BufferedReader br = new BufferedReader(
						new InputStreamReader(con.getInputStream(), Constants.UTF_8))) {
					StringBuilder response = new StringBuilder();
					String responseLine = null;
					logger.info("Rreading line by line and appending to stringbuffer");
					while ((responseLine = br.readLine()) != null) {
						response.append(responseLine.trim());
					}
					try {
						JSONObject jsonObject = new JSONObject(response.toString());
						token = jsonObject.getString(Constants.ACCESS_TOKEN);
						//logger.info("Token = "+token);
						status.setJobId(String.valueOf(STATUS.Success.getID()));
						status.setStatus(token);
					} catch (JSONException err) {
						logger.error("Unable to parse response to string");
						status.setJobId(String.valueOf(STATUS.Failed.getID()));						
						status.setReason("Unable to parse response to string");
					}
				} catch (Exception e) {
					e.printStackTrace();
					status.setJobId(String.valueOf(STATUS.Failed.getID()));						
					status.setReason("Unable to read response = "+e.getMessage());
				}
			}else if(code == STATUS.Unauthorized.getID()) {
				logger.info("Unauthorized");
				status.setJobId(String.valueOf(code));
				status.setReason(Constants.UNAUTHORIZED_MSG);
			}else if(code == STATUS.Forbidden.getID()) {
				logger.info("Forbidden");
				status.setJobId(String.valueOf(code));
				status.setReason("No permissions for this user = "+username);
			}else if(code == STATUS.UNKNOWN.getID()) {
				logger.info("Service Unavailable");
				status.setJobId(String.valueOf(code));
				status.setReason("Service Unavailable");
			}else{
				logger.info("SSM is not running");
				status.setJobId(String.valueOf(code));
				status.setReason("SSM is not running");
			}
		} catch (Exception e) {
			status.setJobId(String.valueOf(STATUS.Failed.getID()));
			status.setReason("SSM Connection refused");
			logger.error("SSM Connection refused");
		}
		return status;
	}

	@SuppressWarnings("unused")
	@Override
	public JSONObject sendTaskCompleteResponseBackToSSM(String taskId, String accessToken, String jobStatus) {
		JSONObject jsonObject = null;
		try {
			String ssmurl = ssmServerURL;
			String urlParameters = Constants.TASKID+"=" + taskId + Constants.AND_OPERATOR+Constants.PROVISIONING+"="+ jobStatus == Constants.SUCCESS ? Constants.TRUE : Constants.FALSE;
			byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
			int postDataLength = postData.length;
			URL url = new URL(ssmurl + Constants.API_V5_COMPLETETASK);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setInstanceFollowRedirects(false);
			conn.setRequestMethod(Constants.POST);
			conn.setRequestProperty(Constants.CONTENT_TYPE, Constants.APPLICATION_X_WWW_FORM_URLENCODED);
			conn.setRequestProperty(Constants.AUTHORIZATION, Constants.BEARER + accessToken + "");
			conn.setUseCaches(false);
			try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
				wr.write(postData);
			}
			int code = conn.getResponseCode();
			logger.info(code);

			try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), Constants.UTF_8))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				try {
					jsonObject = new JSONObject(response.toString());

				} catch (JSONException err) {
					logger.error("Unable to parse response to string");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.info("Successfully status sent back to SSM");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Unable to sent status to SSM");
		}
		return jsonObject;
	}
	
	@SuppressWarnings("unused")
	@Override
	public JSONObject sendTaskUpdateResponseBackToSSM(String taskId, String accessToken, String jobStatus,String comment) {
		JSONObject jsonObject = null;
		try {
			String ssmurl = ssmServerURL;
			JSONObject bodyJsonObj = new JSONObject();
			JSONArray taskKeyToUpdate = new JSONArray();
			JSONObject taskJsonObj = new JSONObject();
			//String urlParameters = Constants.TASKID+"=" + taskId + Constants.AND_OPERATOR+Constants.PROVISIONING+"="+ jobStatus == Constants.SUCCESS ? Constants.TRUE : Constants.FALSE;
			//String urlParameters = "{\""+Constants.TASKKEYTOUPDATE+"\":{\""+Constants.TASKID+"\":\""+taskId+"\",\""+Constants.UPDATETASK+"\":\""+Constants.COMPLETETASK+"\" }}";
			taskJsonObj.put(Constants.TASKID, taskId);
			if(jobStatus.equalsIgnoreCase(Constants.SUCCESS)) {
				taskJsonObj.put(Constants.UPDATETYPE, Constants.COMPLETETASK);
				taskJsonObj.put(Constants.PROVISIONING_METADATA, "ConnectorBOT video link");
				taskJsonObj.put(Constants.COMMENTS, jobStatus);
			}else {
				taskJsonObj.put(Constants.PROVISIONING_METADATA, "ConnectorBOT video link");
				taskJsonObj.put(Constants.COMMENTS, comment);
			}
			taskKeyToUpdate.put(taskJsonObj);
			bodyJsonObj.put(Constants.TASKKEYTOUPDATE, taskKeyToUpdate);
			byte[] postData = bodyJsonObj.toString().getBytes(StandardCharsets.UTF_8);
			int postDataLength = postData.length;
			URL url = new URL(ssmurl + Constants.API_V5_UPDATETASK);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setInstanceFollowRedirects(false);
			conn.setRequestMethod(Constants.POST);
			conn.setRequestProperty(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
			conn.setRequestProperty(Constants.AUTHORIZATION, Constants.BEARER + accessToken + "");
			conn.setUseCaches(false);
			try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
				wr.write(postData);
			}
			int code = conn.getResponseCode();
			logger.info(code);

			try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), Constants.UTF_8))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				try {
					jsonObject = new JSONObject(response.toString());

				} catch (JSONException err) {
					logger.error("Unable to parse response to string");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.info("Successfully status sent back to SSM");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Unable to sent status to SSM");
		}
		return jsonObject;
	}

	@SuppressWarnings("unused")
	@Override
	public JSONObject createConnectionJSON(JSONObject accountJSON, String accessToken, String connectionName,String connectionType) {
		
		JSONObject jsonObject = null;
		try {
			String ssmurl = ssmServerURL;
			String urlParameters = "connectiontype=REST&saveconnection=Y&systemname=testSecuritySystem&connectionName="
					+ connectionName + "&ConnectionJSON={\"authentications\":{\"" + connectionName
					+ "\":{\"authType\":\"basic\",\"errorPath\":\"401\",\"maxRefreshTryCount\":5,\"properties\":{\"userName\":\""+ssmUsername+"\",\"password\":\""+ssmPassword+"\"},\"authError\":[\"404\"],\"retryFailureStatusCode\":[202]}}}&"+connectionType+"="
					+ accountJSON;
			byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
			int postDataLength = postData.length;
			URL url = new URL(ssmurl + Constants.API_V5_TESTCONNECTION);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setInstanceFollowRedirects(false);
			conn.setRequestMethod(Constants.POST);
			conn.setRequestProperty(Constants.CONTENT_TYPE, Constants.APPLICATION_X_WWW_FORM_URLENCODED);
			conn.setRequestProperty(Constants.AUTHORIZATION, Constants.BEARER + accessToken + "");
			conn.setUseCaches(false);
			try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
				wr.write(postData);
			}
			int code = conn.getResponseCode();
			logger.info(code);

			try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), Constants.UTF_8))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				try {
					jsonObject = new JSONObject(response.toString());

				} catch (JSONException err) {
					logger.error("Unable to parse response to string");
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Unable to sent status to SSM");
		}
		return jsonObject;
	}

	@Override
	public JSONObject createSecuritySystem(JSONObject jsonObjecte, String accessToken) {
		// TODO Auto-generated method stub
		return null;
	}

}
