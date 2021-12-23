package com.saviynt.identitybot.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.saviynt.identitybot.Util.Constants;
import com.saviynt.identitybot.constants.STATUS;
import com.saviynt.identitybot.controller.Status;

@Service
public class RemoteAgentService {
	
	public static final String SEARCH_EXCEPTION_CLASS="SearchTextNotFoundException.class";
	
//	@Value("${secclient.oauth2.client.clientId}")
//	String clientId;
//	
//	@Value("${secclient.oauth2.client.clientSecret}")
//	String clientSecret;
//	
//	@Value("${secclient.oauth2.client.accessTokenUri}")
//	String accessTokenUri;
//	
//	@Value("${secclient.oauth2.client.scope}")
//	String scope;
	
	@Value("${scripts.dir}")
	String scriptsRootDir;

	final static Logger logger = Logger.getLogger(RemoteAgentService.class);

	//	public static void executeRemoteJob(String scriptsRootDir, String agentSystemPort, String agentScriptUploadUrl,
	//			String cobotStatusCallbackURL, String appJobID, String content, String fileName, String remoteIP)
	//			throws Exception {
	//
	//		CloseableHttpClient httpclient = HttpClients.createDefault();
	//		String scriptsDir = scriptsRootDir;
	//		String agentPort = agentSystemPort;
	//		String scriptUpload = agentScriptUploadUrl;
	//		String statusCallBackUrl = cobotStatusCallbackURL;
	//
	//		File file = new File(scriptsDir + File.separatorChar + fileName);
	//
	//		String remoteHostUrl = "http://" + remoteIP + ":" + agentPort + scriptUpload;
	//		logger.info("Remote Host URL " + remoteHostUrl);
	//		FileInputStream input = new FileInputStream(file);
	//		byte[] byteArray = IOUtils.toByteArray(input);
	//
	//		HttpEntity entity = MultipartEntityBuilder.create().addTextBody("JobId", appJobID)
	//				.addTextBody("CallBackURL", statusCallBackUrl).addTextBody("Body", content)
	//				.addTextBody("FileName", fileName)
	//				.addBinaryBody("File", byteArray, ContentType.create("application/octet-stream"), "filename").build();
	//
	//		HttpPost httpPost = new HttpPost(remoteHostUrl);
	//		httpPost.setEntity(entity);
	//		HttpResponse response = httpclient.execute(httpPost);
	//		HttpEntity result = response.getEntity();
	//		logger.info("job status " + EntityUtils.toString(result));
	//		logger.info("job " + appJobID + " is successfully completed");
	//	}


//
//	@Bean
//	@ConfigurationProperties("example.oauth2.client")
//	protected ClientCredentialsResourceDetails oAuthDetails() {
//		return new ClientCredentialsResourceDetails();
//	}

	@Bean
	protected RestTemplate restTemplate() {
		return new RestTemplate();
//		ClientCredentialsResourceDetails clientCredentialsResourceDetails = new ClientCredentialsResourceDetails();
//		clientCredentialsResourceDetails.setClientId(clientId);
//		clientCredentialsResourceDetails.setClientSecret(clientSecret);
//		clientCredentialsResourceDetails.setAccessTokenUri(accessTokenUri);
//		List<String> scopeList = new ArrayList<String>();
//		scopeList.add(scope);
//		clientCredentialsResourceDetails.setScope(scopeList);
//		
//		return new OAuth2RestTemplate(clientCredentialsResourceDetails);
	}


	public Status executeRemoteJob(String scriptsRootDir, 
			String agentSystemPort, 
			String agentScriptUploadUrl,
			String cobotStatusCallbackURL, 
			String appJobID, 
			String content, 
			String fileName, 
			String remoteIP,
			String excludingPattern,
			String suiteName,boolean readParentScriptOutputFlag,JSONObject parentScriptOutputdataList,String browserType)
					throws Exception {
		Status status = new Status();
		try {
		String scriptsDir = scriptsRootDir;
		String agentPort = agentSystemPort;
		String scriptUpload = agentScriptUploadUrl;
		String statusCallBackUrl = cobotStatusCallbackURL;
		logger.info("scriptsDir = "+scriptsDir);
		logger.info("agentPort = "+agentPort);
		logger.info("scriptUpload = "+scriptUpload);
		logger.info("statusCallBackUrl = "+statusCallBackUrl);
		logger.info("appJobID = "+appJobID);
		logger.info("readParentScriptOutputFlag = "+readParentScriptOutputFlag);
		logger.info("parentScriptOutputdataList = "+parentScriptOutputdataList);
		logger.info("browserType = "+browserType);
		
		File file = new File(scriptsDir + File.separatorChar + fileName);
		File searchException = new File(scriptsDir + File.separatorChar + SEARCH_EXCEPTION_CLASS);
		String remoteHostUrl = "http://" + remoteIP + ":" + agentPort + scriptUpload;
		logger.info("Remote Host URL " + remoteHostUrl);

		logger.info("assigning key, values to bodyMap object");
		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<String, Object>();
		bodyMap.add("File", new FileSystemResource(file));
		bodyMap.add("SearchExceptionFile", new FileSystemResource(searchException));
		bodyMap.add("JobId", appJobID);
		bodyMap.add("CallBackURL",statusCallBackUrl);
		bodyMap.add("Body", content);
		bodyMap.add("FileName", fileName);
		bodyMap.add("excludingPattern", excludingPattern);
		bodyMap.add("suiteName", suiteName);
		bodyMap.add("readParentScriptOutputFlag", readParentScriptOutputFlag);
		bodyMap.add("parentScriptOutputdataList", parentScriptOutputdataList.toString());
		bodyMap.add("browserType", browserType);
		logger.info("bodyMap = "+bodyMap);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		org.springframework.http.HttpEntity<MultiValueMap<String, Object>> requestEntity = new org.springframework.http.HttpEntity<>(bodyMap, headers);
		logger.info("creating hesders "+headers);

		RestTemplate restTemplate = restTemplate();
		
		logger.info("remoteHostUrl = "+remoteHostUrl);
		ResponseEntity<String> response = restTemplate.exchange(remoteHostUrl,
				HttpMethod.POST, requestEntity, String.class);
		
		logger.info("response status: " + response.getStatusCode());
		logger.info("response body: " + response.getBody());
		
		
		logger.info("job " + appJobID + " is  completed");
		status.setJobId(String.valueOf(STATUS.Success.getID()));
		}catch (Exception e) {
			status.setJobId(String.valueOf(STATUS.Failed.getID()));
			status.setReason(e.getMessage());
		}
		return status;
	}
	public void recordCliJob(String agentSystemPort, String recordRemoteCli,String recordCallbackUpdateStatus,
			 String appJobID, String data, String scriptFileName, String remoteIP, String appName) {
		
		String agentPort = agentSystemPort;
		
		String remoteHostUrl = "http://" + remoteIP + ":" + agentPort + recordRemoteCli;
		
		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<String, Object>();
		bodyMap.add("JobId", appJobID);
		bodyMap.add("CallBackURL",recordCallbackUpdateStatus);
		bodyMap.add("Body", data);
		bodyMap.add("FileName", scriptFileName);
		bodyMap.add("AppName", appName);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		org.springframework.http.HttpEntity<MultiValueMap<String, Object>> requestEntity = new org.springframework.http.HttpEntity<>(bodyMap, headers);
		
		RestTemplate restTemplate = restTemplate();
		
		ResponseEntity<String> response = restTemplate.exchange(remoteHostUrl,
				HttpMethod.POST, requestEntity, String.class);
		
		System.out.println("response status: " + response.getStatusCode());
		System.out.println("response body: " + response.getBody());
		
		logger.info("job " + appJobID + " is  completed");
	}
	public void playCliJob(String agentSystemPort, String playRemoteCli,String recordCallbackUpdateStatus,
			 String appJobID, String data, String scriptFileName, String remoteIP,
			 String format, String recordVideo, String appName) {
		
		String agentPort = agentSystemPort;
		
		String remoteHostUrl = "http://" + remoteIP + ":" + agentPort + playRemoteCli;
		
		String scriptPath = scriptsRootDir + appName + File.separator + Constants.RECORDED_SCRIPTS_PATH;
		scriptPath = scriptPath.replace("\\", "/");
//		File scriptsFile = new File(scriptPath);
		String scriptFileName1=null;
//		if(scriptsFile.exists()) {
//			scriptFileName1 = scriptsFile.list()[0];			
//		}
		scriptFileName1 = scriptPath + File.separator + scriptFileName+Constants.DOTTXT;
		scriptFileName1 = scriptFileName1.replace("\\", "/");
		File file = new File(scriptsRootDir + appName + File.separator + Constants.RECORDED_SCRIPTS_PATH + File.separator + scriptFileName + ".txt");
		
		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<String, Object>();
		bodyMap.add("JobId", appJobID);
		bodyMap.add("CallBackURL",recordCallbackUpdateStatus);
		bodyMap.add("Body", data);
		bodyMap.add("FileName", scriptFileName);
		bodyMap.add("Format", format);
		bodyMap.add("RecordVideo", recordVideo);
		bodyMap.add("File", new FileSystemResource(scriptFileName1));
		bodyMap.add("AppName", appName);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		org.springframework.http.HttpEntity<MultiValueMap<String, Object>> requestEntity = new org.springframework.http.HttpEntity<>(bodyMap, headers);
		
		RestTemplate restTemplate = restTemplate();
		
		ResponseEntity<String> response = restTemplate.exchange(remoteHostUrl,
				HttpMethod.POST, requestEntity, String.class);
		
		System.out.println("response status: " + response.getStatusCode());
		System.out.println("response body: " + response.getBody());
		
		logger.info("job " + appJobID + " is  completed");
	}
	public void playDesktopJob(String agentSystemPort, String playRemoteCli,String recordCallbackUpdateStatus,
			 String appJobID, String data, String scriptFileName, String remoteIP,
			 String format, String recordVideo, String appName,String appPath) {
		
		String agentPort = agentSystemPort;
		
		String remoteHostUrl = "http://" + remoteIP + ":" + agentPort + playRemoteCli;
		
		String scriptPath = scriptsRootDir + appName;
		scriptPath = scriptPath.replace("\\", "/");
//		File scriptsFile = new File(scriptPath);
		String scriptFileName1=null;
//		if(scriptsFile.exists()) {
//			scriptFileName1 = scriptsFile.list()[0];			
//		}
		scriptFileName1 = scriptPath + File.separator + scriptFileName+Constants.DOTSAH;
		scriptFileName1 = scriptFileName1.replace("\\", "/");
		File file = new File(scriptsRootDir + appName + File.separator + scriptFileName + Constants.DOTSAH);
		
		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<String, Object>();
		bodyMap.add("JobId", appJobID);
		bodyMap.add("CallBackURL",recordCallbackUpdateStatus);
		bodyMap.add("Body", data);
		bodyMap.add("FileName", scriptFileName);
		bodyMap.add("Format", format);
		bodyMap.add("RecordVideo", recordVideo);
		bodyMap.add("File", new FileSystemResource(scriptFileName1));
		bodyMap.add("AppName", appName);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		org.springframework.http.HttpEntity<MultiValueMap<String, Object>> requestEntity = new org.springframework.http.HttpEntity<>(bodyMap, headers);
		
		RestTemplate restTemplate = restTemplate();
		
		ResponseEntity<String> response = restTemplate.exchange(remoteHostUrl,
				HttpMethod.POST, requestEntity, String.class);
		
		System.out.println("response status: " + response.getStatusCode());
		System.out.println("response body: " + response.getBody());
		
		logger.info("job " + appJobID + " is  completed");
	}
	public void playDesktopPywinJob(String agentSystemPort, String playRemoteCli,String recordCallbackUpdateStatus,
			 String appJobID, String data, String scriptFileName, String remoteIP,
			 String format, String recordVideo, String appName,String appPath) {
		
		String agentPort = agentSystemPort;
		
		String remoteHostUrl = "http://" + remoteIP + ":" + agentPort + playRemoteCli;
		
		String scriptPath = scriptsRootDir + appName;
		scriptPath = scriptPath.replace("\\", "/");
//		File scriptsFile = new File(scriptPath);
		String scriptFileName1=null;
//		if(scriptsFile.exists()) {
//			scriptFileName1 = scriptsFile.list()[0];			
//		}
		scriptFileName1 = scriptPath + File.separator + scriptFileName;
		scriptFileName1 = scriptFileName1.replace("\\", "/");
		File file = new File(scriptsRootDir + appName + File.separator + scriptFileName);
		
		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<String, Object>();
		bodyMap.add("JobId", appJobID);
		bodyMap.add("CallBackURL",recordCallbackUpdateStatus);
		bodyMap.add("Body", data);
		bodyMap.add("FileName", scriptFileName);
		bodyMap.add("Format", format);
		bodyMap.add("RecordVideo", recordVideo);
		bodyMap.add("File", new FileSystemResource(scriptFileName1));
		bodyMap.add("AppName", appName);
		bodyMap.add("appPath", "");
		logger.info(bodyMap);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		org.springframework.http.HttpEntity<MultiValueMap<String, Object>> requestEntity = new org.springframework.http.HttpEntity<>(bodyMap, headers);
		
		RestTemplate restTemplate = restTemplate();
		
		ResponseEntity<String> response = restTemplate.exchange(remoteHostUrl,
				HttpMethod.POST, requestEntity, String.class);
		
		System.out.println("response status: " + response.getStatusCode());
		System.out.println("response body: " + response.getBody());
		
		logger.info("job " + appJobID + " is  completed");
	}

}
