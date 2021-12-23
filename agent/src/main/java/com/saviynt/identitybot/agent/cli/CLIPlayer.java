package com.saviynt.identitybot.agent.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.json.Json;
import javax.json.JsonObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saviynt.identitybot.agent.constants.Constants;
import com.saviynt.identitybot.agent.constants.STATUS;
import com.saviynt.identitybot.agent.response.Status;
import com.saviynt.identitybot.agent.util.FileExplorer;
import com.saviynt.identitybot.agent.util.MobaXterm;

@Service
public class CLIPlayer {

	@Value("${scripts.dir}")
	String scriptsRootDir;
	
	@Value("${sendSignal.root.dir}")
	String sendSignalRootDir;

	@Value("${cli.errors}")
	String cliErrors;
	
	@Value("${cobot.server.url}")
	String cobotServerUrl;
	
	@Value("${sahi.root.dir}")
	String sahiRootDir;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private MobaXterm mobaXterm;
	
	@Autowired
	com.saviynt.identitybot.agent.service.InvokeServiceImpl invokeServiceImpl;

	final static Logger logger = LoggerFactory.getLogger(CLIPlayer.class);
	
	@Autowired
	FileExplorer fileExplorer;
	
	@PostConstruct
	public void postConstruct() {
		scriptsRootDir=scriptsRootDir.replaceAll("/$", "");
		scriptsRootDir=scriptsRootDir.replaceAll("\\$", "");
		
		scriptsRootDir=scriptsRootDir+File.separator;
		
	}

	public Status runTest(String scriptFileName,String videoFormat, String callbackUrl, String jobid,String content, boolean recordVideo, String appName,boolean mobaX) throws Exception {
		Status status = new Status();
		try {
			Status startVideoStatus = null;
			if (recordVideo) {
				startVideoStatus = startVideo(scriptFileName, videoFormat,appName);
			}
			Status parseFileStatus = new Status();
			parseFileStatus.setJobId(String.valueOf(STATUS.Success.getID()));
			if(!mobaX) {
				parseFileStatus = parseFile(scriptFileName,content, appName);
			}
			if(parseFileStatus.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
				Status executeBatFileStatus = null;
				if(mobaX) {
					String rootPath = env.getProperty("scripts.dir");
					String recorderFilePath = rootPath + File.separator + appName + File.separator
							+ Constants.RECORDED_SCRIPTS_PATH + File.separator + scriptFileName + Constants.DOTTXT;
					executeBatFileStatus = mobaXterm.executeMobaxScript(recorderFilePath,content);
				}else {
					executeBatFileStatus = executeBatFile(scriptFileName,appName);
				}
				if(executeBatFileStatus.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
					if(recordVideo) {
						if(startVideoStatus != null && startVideoStatus.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
							status.setReason("Script file execution success. ");
							status.setJobId(String.valueOf(STATUS.Success.getID()));
						}else {
							status.setReason("Script file execution success. "+startVideoStatus.getReason());
							status.setJobId(String.valueOf(STATUS.Success.getID()));
						}           
					}else {
						status.setReason("Script file execution success. ");
						status.setJobId(String.valueOf(STATUS.Success.getID()));
					}
				}else {
					if (recordVideo) {
						if (startVideoStatus != null && startVideoStatus.getJobId()
								.equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
							status.setReason("Script error. "
									+ executeBatFileStatus.getReason());
							status.setJobId(String.valueOf(STATUS.Failed.getID()));
						} else {
							status.setReason("Script error.. "
									+ startVideoStatus.getReason() + " : " + executeBatFileStatus.getReason());
							status.setJobId(String.valueOf(STATUS.Failed.getID()));
						}
					} else {
						status.setReason("Script error. " + executeBatFileStatus.getReason());
						status.setJobId(String.valueOf(STATUS.Failed.getID()));
					}
				}
			}else {
				if(recordVideo) {
					if(startVideoStatus != null && startVideoStatus.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
						status.setReason("Parse error. "+parseFileStatus.getReason());
						status.setJobId(String.valueOf(STATUS.Failed.getID()));
					}else {
						status.setReason("Both Video and Parse error.. "+startVideoStatus.getReason() +" : "+parseFileStatus.getReason());
						status.setJobId(String.valueOf(STATUS.Failed.getID()));
					}           
				}else {
					status.setReason("Parse error. "+parseFileStatus.getReason());
					status.setJobId(String.valueOf(STATUS.Failed.getID()));
				}
			}
			if(recordVideo) {
				Thread.sleep(30000);
				stopVideo();
			}
			if (status.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
				
				try {
					String videoFileName = startVideoStatus == null ? null : startVideoStatus.getVideoFileName();
					sendExecutionStatus(callbackUrl,jobid,"Success","",
						scriptFileName,videoFileName,videoFormat, appName, recordVideo,mobaX);
				}catch (Exception e) {
					logger.info("ERROR >>>"+e.getLocalizedMessage());
					JsonObject response = Json.createObjectBuilder().add("jobId", jobid).add("status", "Failed")
							.add("reason", e.getMessage()).build();
					invokeServiceImpl.invokePostCall(cobotServerUrl+Constants.CobotServerUrls.CLI_UPDATE_STATUS, response.toString());
				}
				
			}else if (status.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Inprocess.getID()))) {
				
				try {
					String videoFileName = startVideoStatus == null ? null : startVideoStatus.getVideoFileName();
					sendExecutionStatus(callbackUrl,jobid,"Success","",
							scriptFileName,videoFileName,videoFormat, appName, recordVideo,mobaX);
					}catch (Exception e) {
						logger.info("ERROR >>>"+e.getMessage());
						JsonObject response = Json.createObjectBuilder().add("jobId", jobid).add("status", "Failed")
								.add("reason", e.getMessage()).build();
						invokeServiceImpl.invokePostCall(cobotServerUrl+Constants.CobotServerUrls.CLI_UPDATE_STATUS, response.toString());
					}
			}
			else {      
				try {
					logger.info("Reason : "+status.getReason());
					String videoFileName = startVideoStatus == null ? null : startVideoStatus.getVideoFileName();
					sendExecutionStatus(callbackUrl,jobid,"Failed",status.getReason(),
							scriptFileName,videoFileName,videoFormat, appName, recordVideo,mobaX);
					}catch (Exception e) {
						logger.info("ERROR >>>" +e.getMessage());
						JsonObject response = Json.createObjectBuilder().add("jobId", jobid).add("status", "Failed")
								.add("reason", e.getMessage()).build();
						invokeServiceImpl.invokePostCall(cobotServerUrl+Constants.CobotServerUrls.CLI_UPDATE_STATUS, response.toString());
					}
			}
		} catch (Exception e) {
			if(recordVideo) {
				Thread.sleep(30000);
				stopVideo();
			}
			JsonObject response = Json.createObjectBuilder().add("jobId", jobid).add("status", "Failed")
					.add("reason", e.getMessage()).build();
			e.printStackTrace();
			invokeServiceImpl.invokePostCall(cobotServerUrl+Constants.CobotServerUrls.CLI_UPDATE_STATUS, response.toString());
			status.setJobId(String.valueOf(STATUS.Failed.getID()));
			status.setReason(e.getMessage());
		}
		return status;
	}

	@SuppressWarnings("unchecked")
	public Status parseFile(String scriptFileName,String content, String appName) throws Exception {
		Status status = new Status();
		try {
			logger.info("Parsing the file");
			String line;
			StringBuffer psBuffer = new StringBuffer();
			StringBuffer sqlBuffer = new StringBuffer();
			ArrayList<String> finalConditions = new ArrayList<>();
			finalConditions.add("PS C.*>");
			logger.info("appName = "+appName);
			logger.info("checking converted script path exists or not");
			if (!new File(scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH).exists())
				new File(scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH).mkdirs();

			logger.info("deleting .bat file if exists.");
			Files.deleteIfExists(Paths.get(scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH + File.separator
					+ scriptFileName + ".bat"));
			logger.info("creating .bat file in " + scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH + File.separator
					+ scriptFileName + ".bat");
			Files.createFile(Paths.get(scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH + File.separator
					+ scriptFileName + ".bat"));

			logger.info("reading .txt file in " + scriptsRootDir + appName + File.separator + Constants.RECORDED_SCRIPTS_PATH + File.separator
					+ scriptFileName + ".txt");
			BufferedReader br = new BufferedReader(new FileReader(new File(scriptsRootDir
					+ appName + File.separator + Constants.RECORDED_SCRIPTS_PATH + File.separator + scriptFileName + ".txt")));

			String oldContent = "";
			while ((line = br.readLine()) != null) {
				oldContent = oldContent + line + System.lineSeparator();
			}
			logger.info("appending content to list = " + oldContent);
			logger.info("preparing the cli params from job content");
			Map<String, Object> cliParams = new ObjectMapper().readValue(content, HashMap.class);
			logger.info("prepared cliparams = " + cliParams);

			logger.info("adding cli params to olcContent");
			for (String key : cliParams.keySet()) {
				oldContent = StringUtils.replace(oldContent, "{{" + key + "}}", cliParams.get(key).toString());
			}
			logger.info("added cli params to olcContent = " + oldContent);

			logger.info("appending above oldcontent to hidden.txt file");
			FileWriter writer = new FileWriter(scriptsRootDir + appName + File.separator + Constants.RECORDED_SCRIPTS_PATH + File.separator
					+ scriptFileName + "_hidden.txt");
			writer.write(oldContent);
			writer.close();
			br.close();

			br = new BufferedReader(new FileReader(new File(scriptsRootDir + appName + File.separator
					+ Constants.RECORDED_SCRIPTS_PATH + File.separator + scriptFileName + ".txt")));
			String navigatePath = "";
			logger.info("reading line by line");
			while ((line = br.readLine()) != null) {
				navigatePath = line;
			}
			br.close();
			

			
			logger.info("reading the hidden.txt file");
			br = new BufferedReader(new FileReader(new File(scriptsRootDir + appName + File.separator + Constants.RECORDED_SCRIPTS_PATH
					+ File.separator + scriptFileName + "_hidden.txt")));
			
			psBuffer.append(navigatePath + "\n");
			while ((line = br.readLine()) != null) {
				if (StringUtils.startsWith(line, "mysql> ")) {
					sqlBuffer.append(line.split("> ")[1] + "\n");
				} else if (StringUtils.contains(line, "mysql")) {
					psBuffer.append("for /F \"tokens=*\" %%A in (" + scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH
							+ File.separator + scriptFileName + ".txt) do ");
					Pattern pattern = Pattern.compile(finalConditions.get(0));
					psBuffer.append(pattern.split(line)[pattern.split(line).length - 1].trim() + " -e \"%%A\" \n");
				} else {
					for (String condition : finalConditions) {
						Pattern pattern = Pattern.compile(condition);
						if (condition.contains(".*") && Pattern.matches(condition + ".*", line)) {
							psBuffer.append(pattern.split(line)[pattern.split(line).length - 1].trim() + "\n");
						} else if (line.contains(condition)) {
							psBuffer.append(line.replace(condition, "").trim() + "\n");
						}
					}
				}
			}
			br.close();
			psBuffer.append("exit");
			logger.info("psBuffer = " + psBuffer);
			FileOutputStream powershell = new FileOutputStream(scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH
					+ File.separator + scriptFileName + ".bat");
			powershell.write(psBuffer.toString().getBytes());
			powershell.flush();
			powershell.close();
			logger.info("checking and saving script file");
			logger.info("sqlBuffer = "+sqlBuffer);
			if (!StringUtils.isEmpty(sqlBuffer.toString())) {
				Files.deleteIfExists(Paths.get(scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH + File.separator
						+ scriptFileName + ".txt"));
				Files.createFile(Paths.get(scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH + File.separator
						+ scriptFileName + ".txt"));
				FileOutputStream sqlFile = new FileOutputStream(scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH
						+ File.separator + scriptFileName + ".txt");
				sqlFile.write(sqlBuffer.toString().getBytes());
				sqlFile.flush();
				sqlFile.close();
			}
			logger.info("deleting the hidden.txt file if exists");
			Files.deleteIfExists(Paths.get(scriptsRootDir + appName + File.separator + Constants.RECORDED_SCRIPTS_PATH + File.separator
					+ scriptFileName + "_hidden.txt"));
			logger.info("deleted the hidden.txt file if exists");
			
			status.setJobId(String.valueOf(STATUS.Success.getID()));
		} catch (Exception e) {
			status.setJobId(String.valueOf(STATUS.Failed.getID()));
			status.setReason(e.getMessage());
		}
		return status;
	}

	@SuppressWarnings("resource")
	public Status executeBatFile(String fileName, String appName) throws Exception {
		logger.info("executing the bat file");
		Status status = new Status();
		try {
			logger.info("checking playback script path exists or not");
			if (!new File(scriptsRootDir + appName + File.separator + Constants.PLAYBACK_SCRIPTS_PATH).exists()) {
				new File(scriptsRootDir + appName + File.separator + Constants.PLAYBACK_SCRIPTS_PATH).mkdir();
			}

			logger.info("executing the play back script");
			Process process = Runtime.getRuntime()
					.exec("cmd.exe /k powershell " + scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH + File.separator
							+ fileName + ".bat > " + scriptsRootDir + appName + File.separator + Constants.PLAYBACK_SCRIPTS_PATH + File.separator
							+ fileName + ".txt 2>&1");
			process.waitFor(10000, TimeUnit.MILLISECONDS);
			String content = "";
			String line = "";
			
			BufferedReader br = new BufferedReader(new FileReader(new File(scriptsRootDir + appName + File.separator
					+ Constants.RECORDED_SCRIPTS_PATH + File.separator + fileName + ".txt")));
			String navigatePath = "";
			logger.info("reading line by line");
			while ((line = br.readLine()) != null) {
				navigatePath = line;
			}
			br.close();

			
			logger.info("saving the play script to .txt file");
			process = Runtime.getRuntime().exec("cmd /c start /wait cmd.exe /k \"" + navigatePath + " && powershell type " + scriptsRootDir
					+ appName + File.separator + Constants.PLAYBACK_SCRIPTS_PATH + File.separator + fileName + ".txt");
			process.waitFor(5000, TimeUnit.MILLISECONDS);
			content = "";

			BufferedReader stdInput = new BufferedReader(new FileReader(
					new File(scriptsRootDir + appName + File.separator + Constants.PLAYBACK_SCRIPTS_PATH + File.separator + fileName + ".txt")));
			logger.info("reading the above saved script file.");
			while ((line = stdInput.readLine()) != null) {
				content = content + line + System.lineSeparator();
			}
			logger.info("content = "+content);
			logger.info("killing the process");
			
			Map<String, String> errorMap = new HashMap<String, String>();
			for (String error : cliErrors.split(";")) {
				errorMap.put(error.split(":")[0], error.split(":")[1]);
			}
			logger.info("errorMap = "+errorMap);
			status.setJobId(String.valueOf(STATUS.Success.getID()));
			for (String error : errorMap.keySet()) {
				if (StringUtils.contains(content, errorMap.get(error))) {
					//throw new Exception(errorMap.get(error));
					logger.error("CLI Error "+errorMap.get(error));
					status.setJobId(String.valueOf(STATUS.Failed.getID()));
					status.setReason(getErrorMessage(error));
				}
			}
		} catch (Exception e) {
			logger.error("ERROR >>> while executing the bat file = "+e.getMessage());
			status.setJobId(String.valueOf(STATUS.Failed.getID()));
			status.setReason(e.getMessage());
		} finally {
			Runtime.getRuntime().exec("cmd /c taskkill /IM cmd.exe /F");
		}
		return status;
	}

	public Status startVideo(String fileName, String format,String appName) throws Exception {
		logger.info("Starting video capturing.");
		Status status = new Status();
		try {
			stopVideo();
			logger.info("checking playbackVideos path exists or not");
			String recordFileName = appName + "_" + fileName + "_" + new Date().getTime() + format;
			if (!new File(scriptsRootDir + File.separator + appName).exists()) {
				new File(scriptsRootDir + File.separator + appName).mkdirs();
			}
			String recordFilePath = scriptsRootDir + File.separator + appName + File.separator + Constants.PLAYBACK_VIDEOS_PATH;
			if (!new File(recordFilePath).exists()) {
				new File(recordFilePath).mkdirs();
			}
			
			recordFilePath = recordFilePath.replace("\\", "/").replace("//", "/");
			logger.info("executing the powershell.exe to open the ffmpeg for recording the video");
			Runtime.getRuntime().exec(Constants.START_VIDEO_RUNTIME_SCRIPT + recordFilePath + "/" + recordFileName);
			
			double size = 0;
			while(size == 0) {
				File videoFile = new File(recordFilePath + "/" + recordFileName);
				size = videoFile.length();
				logger.info("Video recording is not started yet");
			}
			logger.info("Video recording is started...");
			status.setJobId(String.valueOf(STATUS.Success.getID()));
			status.setVideoFileName(recordFilePath + "/" + recordFileName);
		} catch (Exception e) {
			logger.error("ERROR >>> while starting the video recording");
			status.setJobId(String.valueOf(STATUS.Failed.getID()));
			status.setReason(e.getMessage());
		}
		return status;
	}
	
	public void stopVideo() throws Exception {
		try {
			String line = "Powershell kill command is not yet executed";
			while (line != null) {
				String recordScript = sahiRootDir+Constants.SAHI_PRO_BIN_PATH+File.separator+Constants.STOP_VIDEO_FILE;
				recordScript = recordScript.replace("\\", "/");
				recordScript = recordScript.replace("//", "/");
				sendSignalRootDir = sendSignalRootDir.replace("\\", "/"); 
				
				Runtime.getRuntime().exec("\"" + recordScript+ "\" \"" + sendSignalRootDir + "/SendSignal.exe\"");
				Thread.sleep(5000);
				
				Process p=Runtime.getRuntime().exec("cmd /c tasklist | find /i \"powershell.exe\""); 
	            p.waitFor(); 
	            BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream()));
	            line = reader.readLine();
				if(line != null) {
					Thread.sleep(2000);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	
	@SuppressWarnings("unused")
	public static void sendExecutionStatus(String postUrl, JsonObject inputJson) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String remoteHostUrl = postUrl;

		HttpEntity entity = MultipartEntityBuilder.create().addTextBody("JobId", inputJson.getString("jobId"))
				.addTextBody("status", inputJson.getString("status"))
				.addTextBody("reason", inputJson.getString("status"))
				.addTextBody("importType", inputJson.getString("fileType")).build();

		HttpPost httpPost = new HttpPost(remoteHostUrl);
		httpPost.setEntity(entity);
		HttpResponse response = httpclient.execute(httpPost);
		HttpEntity result = response.getEntity();
	}


	public void sendExecutionStatus(String callbackUrl,String JobId,String status,
			String reason,String scriptFileName,String videoPath,String format, String appName, boolean recordVideo,boolean mobaX) {
		String convertedFile=null;
		String playback=null;
		String convertedScriptFile = null;
		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<String, Object>();
		if(!mobaX) {
			convertedFile = scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH + File.separator + scriptFileName + Constants.DOT_BAT;
			convertedScriptFile = scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH + File.separator + scriptFileName + Constants.DOTTXT;
			playback = scriptsRootDir + appName + File.separator + Constants.PLAYBACK_SCRIPTS_PATH + File.separator + scriptFileName + Constants.DOTTXT;

			
			if(convertedFile!=null) {
				bodyMap.add("File", new FileSystemResource(convertedFile));
			}
			
			if(convertedScriptFile != null && new File(convertedScriptFile).exists()) {
				bodyMap.add("ConvertedScriptFile", new FileSystemResource(convertedScriptFile));
			}
			
			if(playback!=null) {
				bodyMap.add("PlaybackFile", new FileSystemResource(playback));
			}
		}else {
			playback = scriptsRootDir + appName + File.separator + Constants.RECORDED_SCRIPTS_PATH + File.separator + scriptFileName + Constants.DOTTXT;
			if(playback!=null) {
				bodyMap.add("PlaybackFile", new FileSystemResource(playback));
			}
		}

		if(videoPath != null && !videoPath.isEmpty() && recordVideo) {
			bodyMap.add("FileVideo", new FileSystemResource(videoPath));
		}
		
		bodyMap.add("FileName", scriptFileName);
		bodyMap.add("Format",format);
		bodyMap.add("JobId", JobId);
		bodyMap.add("status", status);
		bodyMap.add("reason", reason);
		bodyMap.add("appName", appName);
		
		logger.info("bodyMap = "+bodyMap);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		org.springframework.http.HttpEntity<MultiValueMap<String, Object>> requestEntity = new org.springframework.http.HttpEntity<>(bodyMap, headers);

		RestTemplate restTemplate =new RestTemplate();
		ResponseEntity<String> response = null;
		try {
			logger.info("callbackUrl = "+callbackUrl);
			response = restTemplate.exchange(callbackUrl,
				HttpMethod.POST, requestEntity, String.class);
		if(response.getStatusCode()==HttpStatus.OK) {
			try { 
					boolean flag = fileExplorer.deleteDirectory(new File(scriptsRootDir + appName));
					if (flag)
						logger.info("Agent Files and Folder deleted successfully.");
					else {
						logger.info("Error while deleteing agent files");
					}
			} 
			catch (Exception e) { 
				logger.error("Error deleteting agent playback files", e);
			} 
		}
		}catch (Exception e) {
			logger.info("response = "+response);
			logger.info("ERROR >>> while pushing cli play back files from agent to server = "+e.getMessage());
		}

		System.out.println("Cobot Server response status: " + response.getStatusCode());
		System.out.println("Cobot Server response body: " + response.getBody());

		logger.info("job " + JobId + " is  completed");	
	}

	
	public void sendExecutionStatus(String postUrl, 
			String scriptFile,String scriptfileVideo,String fileName, 
			String JobId, String connectionName,String format,String recordVideo,
			String status,String reason)  {

		scriptFile =  scriptFile.replace("\\", "/");
		scriptfileVideo = scriptfileVideo.replace("\\", "/");

		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<String, Object>();
		if(scriptFile!=null) {
			bodyMap.add("File", new FileSystemResource(scriptFile));
		}
		else {
			bodyMap.add("File", null);
		}

		if(scriptfileVideo != null) {
			bodyMap.add("FileVideo", new FileSystemResource(scriptfileVideo));
		}
		else {
			bodyMap.add("FileVideo", null);
		}
		bodyMap.add("JobId", JobId);
		bodyMap.add("connectionName", connectionName);
		bodyMap.add("scriptFileName",fileName);
		bodyMap.add("format", format);
		bodyMap.add("recordVideo", recordVideo);
		bodyMap.add("status", status);
		bodyMap.add("reason", reason);


		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		org.springframework.http.HttpEntity<MultiValueMap<String, Object>> requestEntity = new org.springframework.http.HttpEntity<>(bodyMap, headers);

		RestTemplate restTemplate =new RestTemplate();

		ResponseEntity<String> response = restTemplate.exchange(postUrl,
				HttpMethod.POST, requestEntity, String.class);
		if(response.getStatusCode()==HttpStatus.OK) {
			try { 
				if(scriptFile != null) {
					Files.deleteIfExists( Paths.get(scriptFile)); 
				}
				if(scriptfileVideo !=null) {
					Files.deleteIfExists( Paths.get(scriptfileVideo)); 
				}
			} 
			catch (IOException e) { 
				logger.error("Error deleteting reorded files", e);
			} 
		}

		System.out.println("Cobot Server response status: " + response.getStatusCode());
		System.out.println("Cobot Server response body: " + response.getBody());

		logger.info("job " + JobId + " is  completed");
	}
	public String getErrorMessage(String msg) {
		switch(msg){
			case "ACCESS_DENIED":
				return Constants.ACCESS_DENIED;
			case "NOT_RECOGNIZED_COMMAND":
				return Constants.NOT_RECOGNIZED_COMMAND;
			case "UNKNOWN_DATABASE":	
				return Constants.UNKNOWN_DATABASE;
		}
		return "";
	}
}