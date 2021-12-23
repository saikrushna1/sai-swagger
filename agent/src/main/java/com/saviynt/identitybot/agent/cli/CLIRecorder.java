package com.saviynt.identitybot.agent.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
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
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.saviynt.identitybot.agent.constants.Constants;
import com.saviynt.identitybot.agent.constants.STATUS;
import com.saviynt.identitybot.agent.response.Status;
import com.saviynt.identitybot.agent.util.BlueZone;
import com.saviynt.identitybot.agent.util.FileExplorer;
import com.saviynt.identitybot.agent.util.MobaXterm;

@Service
public class CLIRecorder {

	final static Logger logger = LoggerFactory.getLogger(CLIRecorder.class);

	@Value("${scripts.dir}")
	String scriptsRootDir;

	@Value("${sendSignal.root.dir}")
	String sendSignalRootDir;

	@Value("${sahi.root.dir}")
	String sahiRootDir;

	@Value("${cobot.server.url}")
	String cobotServer;

	@Value("${video.record}")
	String videoRecord;
	
	@Value("${drivepath.separator}")
	String drivepathSeparator;
	
	@Autowired
	private MobaXterm mobaXterm;
	
	@Autowired
	private BlueZone bluezone;
	
//	
//	@Value("${variable.commandline.exe}")
//	String commandlineExePath;
	
	@Autowired
	FileExplorer fileExplorer;
	

	
	@PostConstruct
	public void postConstruct() {
		scriptsRootDir = scriptsRootDir.replaceAll("/$", "");
		scriptsRootDir = scriptsRootDir.replaceAll("\\$", "");

		scriptsRootDir = scriptsRootDir + File.separator;

	}

	public Status record(String fileName, Boolean recordVideo, String format, String connectionName, String appPath, String userIdVal, String passwordVal, String newPasswordVal, String appId) {
		Status status = new Status();
		try {
			Status startVideoResponse = null;
			if (videoRecord.equalsIgnoreCase(Constants.VIDEO_RECORD_ON)) {
				startVideoResponse = startVideo(connectionName + "_" + fileName + "_", format, connectionName);
			}
			fileName = fileName.substring(0, 1).toUpperCase() + fileName.substring(1);

			Status recordStatus = null;
			if(StringUtils.contains(appPath, Constants.MOBAXTERM)) {
				recordStatus = mobaXterm.navigationSetUp(appPath);
			} else if(StringUtils.contains(appPath, Constants.BLUEZONE)) {
					recordStatus = bluezone.navigationSetUp(appPath, userIdVal, passwordVal, newPasswordVal, appId);
			}else {
				recordStatus = recordTest(fileName, connectionName,appPath);
			}
			if (recordStatus.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
				if (videoRecord.equalsIgnoreCase(Constants.VIDEO_RECORD_ON)) {
					if (startVideoResponse != null
							&& startVideoResponse.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
						status.setReason("Both Video and Script Recorded Successfully.");
						status.setJobId(String.valueOf(STATUS.Success.getID()));
					} else {
						status.setReason("Script Recorded Successfully but Video Recording Failed."
								+ startVideoResponse.getReason());
						status.setJobId(String.valueOf(STATUS.Inprocess.getID()));
					}
				} else {
					status.setReason("Script Recorded Successfully.");
					status.setJobId(String.valueOf(STATUS.Success.getID()));
				}
			} else {
				if (videoRecord.equalsIgnoreCase(Constants.VIDEO_RECORD_ON)) {
					if (startVideoResponse != null
							&& startVideoResponse.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
						status.setReason(
								"Video Recorded Successfully but Script Recording Failed. " + recordStatus.getReason());
						status.setJobId(String.valueOf(STATUS.Success.getID()));

					} else {
						status.setReason("Both Video and Script Recording Failed. " + startVideoResponse.getReason()
								+ " : " + recordStatus.getReason());
						status.setJobId(String.valueOf(STATUS.Failed.getID()));
					}
				} else {
					status.setReason("Script Recording Failed. " + recordStatus.getReason());
					;
					status.setJobId(String.valueOf(STATUS.Failed.getID()));
				}
			}
			status.setVideoFileName(startVideoResponse.getVideoFileName());
			status.setScriptFileName(recordStatus.getScriptFileName());

		} catch (Exception e) {
			status.setJobId(String.valueOf(STATUS.Failed.getID()));
			status.setReason(e.getMessage());
		}
		return status;
	}

	public Status recordTest(String fileName, String appName, String commandlineExePath) throws Exception {
		logger.info("started recordTest with fileName = " + fileName);
		logger.info("connectionName = " + appName);
		logger.info("drivepathSeparator = " + drivepathSeparator);
		Status status = new Status();
		String file = "";
		try {
			logger.info("checking whether file exists or not");
			Files.deleteIfExists(Paths.get(scriptsRootDir + appName + File.separator + Constants.RECORDED_SCRIPTS_PATH
					+ File.separator + fileName + ".txt"));
			if (!new File(scriptsRootDir + appName + File.separator + Constants.RECORDED_SCRIPTS_PATH).exists()) {
				new File(scriptsRootDir + appName + File.separator + Constants.RECORDED_SCRIPTS_PATH).mkdirs();
			}
			logger.info("starting script capturing and appending to .txt file");
			
			commandlineExePath = commandlineExePath.replace("\\", "/");
			if(commandlineExePath.contains("/")) {
				commandlineExePath = commandlineExePath.substring(0, commandlineExePath.lastIndexOf("/"));
			} else {
				commandlineExePath = "";
			}
			
			Process scriptRecordingProcess =null;
			if (drivepathSeparator.equalsIgnoreCase(Constants.TRUE)) {
				scriptRecordingProcess = Runtime.getRuntime()
						.exec("cmd /c start /wait cmd.exe  /k \"cd /d " + commandlineExePath + " && powershell -NoExit  Start-Transcript -Path " + scriptsRootDir
								+ appName + File.separator + Constants.RECORDED_SCRIPTS_PATH + File.separator + fileName + ".txt\"");
			} else if (drivepathSeparator.equalsIgnoreCase(Constants.FALSE)) {
				scriptRecordingProcess = Runtime.getRuntime()
						.exec("cmd /c start /wait cmd.exe  /k \"cd " + commandlineExePath + " && powershell -NoExit  Start-Transcript -Path " + scriptsRootDir
								+ appName + File.separator + Constants.RECORDED_SCRIPTS_PATH + File.separator + fileName + ".txt\"");
			}
			
			scriptRecordingProcess.waitFor();

			logger.info("reading reacorderd script file.");
			BufferedReader br = new BufferedReader(new FileReader(new File(scriptsRootDir + appName + File.separator
					+ Constants.RECORDED_SCRIPTS_PATH + File.separator + fileName + ".txt")));
			String content = "";
			String line = "";
			boolean psFound = false;
			logger.info("reading line by line");
			while ((line = br.readLine()) != null) {
				if (!psFound && StringUtils.startsWith(line, "PS ")) {
					psFound = true;
				}

				if (psFound) {
					content = content + line + System.lineSeparator();
				}
			}
			if (drivepathSeparator.equalsIgnoreCase(Constants.TRUE)) {
				content = content + "cd /d " + commandlineExePath;
			} else if (drivepathSeparator.equalsIgnoreCase(Constants.FALSE)) {
				content = content + "cd " + commandlineExePath;
			}
			
			logger.info("contnet = " + content);
			logger.info("writing content into converted script .txt file");
			FileWriter writer = new FileWriter(scriptsRootDir + appName + File.separator
					+ Constants.RECORDED_SCRIPTS_PATH + File.separator + fileName + ".txt");
			writer.write(content);
			writer.close();
			br.close();
			file = scriptsRootDir + appName + File.separator + Constants.RECORDED_SCRIPTS_PATH + File.separator
					+ fileName + ".txt";
			status.setJobId(String.valueOf(STATUS.Success.getID()));
			status.setScriptFileName(file);
		} catch (Exception e) {
			status.setJobId(String.valueOf(STATUS.Failed.getID()));
			status.setReason(e.getMessage());
			logger.info("ERROR >>>  while recorded script to converted script = " + e.getMessage());
		}
		return status;
	}

	public Status startVideo(String fileName, String format, String appName) throws Exception {
		logger.info("checking the scriptsRootDir exists or not");
		Status status = new Status();
		try {
			stopVideo();
			logger.info("scriptsRootDir = " + scriptsRootDir);
			String recordFileName = fileName + "_" + new Date().getTime() + format;
			if (!new File(scriptsRootDir + File.separator + appName).exists()) {
				new File(scriptsRootDir + File.separator + appName).mkdirs();
			}
			String recordFilePath = scriptsRootDir + File.separator + appName + File.separator + Constants.RECORD_VIDEO_PATH;
			if (!new File(recordFilePath).exists()) {
				new File(recordFilePath).mkdirs();
			}
			recordFilePath = recordFilePath.replace("\\", "/");
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
			status.setVideoFileName(recordFileName);
		} catch (Exception e) {
			logger.error("ERROR while opening the shell command = " + e.getMessage());
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

	public Status sendDesktopExecutionStatus(String postUrl, String scriptFile, String scriptfileVideo, String fileName,
			String JobId, String connectionName, String format, String recordVideo, String appName, String status,
			String reason, String appPath) throws IOException {
		String sahiRootDir = this.sahiRootDir + Constants.SAHI_PRO_SCRIPT_PATH;
		Status callBackStatus = new Status();
		fileName = fileName.substring(0, 1).toUpperCase() + fileName.substring(1);
		if (videoRecord.equalsIgnoreCase(Constants.VIDEO_RECORD_ON)) {
			String videoPath = scriptsRootDir + appName + File.separator + Constants.RECORD_VIDEO_PATH;
			videoPath = videoPath.replace("\\", "/");
			File videoFile = new File(videoPath);
			if (videoFile.exists()) {
				scriptfileVideo = videoFile.list()[0];
			}
			logger.info("scriptfileVideo = " + scriptfileVideo);
			scriptfileVideo = videoPath + File.separator + scriptfileVideo;
		}
		String scriptPath = sahiRootDir;
		scriptPath = scriptPath.replace("\\", "/");
		scriptFile = scriptPath + File.separator + fileName + Constants.DOTSAH;
		String scriptFileTemp = scriptPath + File.separator + fileName + "_temp" + Constants.DOTSAH;
		logger.info("scriptFile = " + scriptFile);

		BufferedReader br = new BufferedReader(new FileReader(new File(scriptFile)));
		String oldContent = "";
		oldContent = "_execute(\"" + appPath + "\", true, 10000);"
				+ System.lineSeparator() + "_wait(6000);" + System.lineSeparator();

		String line;
		while ((line = br.readLine()) != null) {
			oldContent = oldContent + line + System.lineSeparator();
		}
		oldContent = oldContent + "_execute(\"taskkill /F /IM " + appPath.substring(
				appPath.lastIndexOf("/") + 1, appPath.length()) + "\")";
		logger.info("appending content to list = " + oldContent);
		
		br.close();
		FileWriter writer = new FileWriter(scriptFileTemp);
		writer.write(oldContent);
		writer.close();

		logger.info("scriptFile = " + scriptFile);

		scriptFileTemp = scriptFileTemp.replace("\\", "/");
		if (scriptfileVideo != null && !scriptfileVideo.isEmpty()) {
			scriptfileVideo = scriptfileVideo.replace("\\", "/");
		}

		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<String, Object>();
		if (scriptFile != null) {
			bodyMap.add("File", new FileSystemResource(scriptFileTemp));
		}

		if (scriptfileVideo != null && !scriptfileVideo.isEmpty()) {
			bodyMap.add("FileVideo", new FileSystemResource(scriptfileVideo));
		}

		bodyMap.add("connectionName", connectionName);
		bodyMap.add("scriptFileName", fileName);
		bodyMap.add("format", format);
		bodyMap.add("recordVideo", recordVideo);
		bodyMap.add("status", status);
		bodyMap.add("reason", reason);
		bodyMap.add("appName", appName);

		logger.info("bodyMap = " + bodyMap);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		org.springframework.http.HttpEntity<MultiValueMap<String, Object>> requestEntity = new org.springframework.http.HttpEntity<>(
				bodyMap, headers);

		RestTemplate restTemplate = new RestTemplate();
		if (videoRecord.equalsIgnoreCase(Constants.VIDEO_RECORD_ON))
			postUrl = cobotServer + Constants.CobotServerUrls.RECORDER_DESKTOP_SCRIPT;
		else
			postUrl = cobotServer + Constants.CobotServerUrls.RECORDER_DESKTOP_SCRIPT_WITHOUT_VIDEO;
		logger.info("postUrl =" + postUrl);
		logger.info("requestEntity = " + requestEntity);
		try {
			ResponseEntity<String> response = restTemplate.exchange(postUrl, HttpMethod.POST, requestEntity,
					String.class);
			logger.info("response = " + response);
			if (response.getStatusCode() == HttpStatus.OK) {
				logger.info("success");
				try {
					boolean flag = fileExplorer.deleteFile(new File(sahiRootDir), new File(scriptFileTemp));
					if (flag)
						logger.info("Temp file deleted successfully.");
					else {
						logger.info("Error while deleteing temp file");
					}
				} catch (Exception e) {
					logger.error("Error deleteting reorded files", e);
				}
				callBackStatus.setJobId(String.valueOf(STATUS.Success.getID()));
				callBackStatus.setReason(response.getBody());
			} else {
				callBackStatus.setJobId(String.valueOf(STATUS.Failed.getID()));
				callBackStatus.setReason(response.getBody());
			}
			System.out.println("Cobot Server response status: " + response.getStatusCode());
			System.out.println("Cobot Server response body: " + response.getBody());
		} catch (Exception e) {
			logger.info("ERROR >>> while export agent file to cobot server : " + e.getMessage());
			callBackStatus.setJobId(String.valueOf(STATUS.Failed.getID()));
			callBackStatus.setReason(e.getMessage());
		}
		return callBackStatus;
	}

	public Status sendExecutionStatus(String postUrl, String scriptFile, String scriptfileVideo, String fileName,
			String JobId, String connectionName, String format, String recordVideo, String appName, String status,
			String reason) {
		Status callBackStatus = new Status();
		fileName = fileName.substring(0, 1).toUpperCase() + fileName.substring(1);
		if (videoRecord.equalsIgnoreCase(Constants.VIDEO_RECORD_ON)) {
			String videoPath = scriptsRootDir + appName + File.separator + Constants.RECORD_VIDEO_PATH;
			videoPath = videoPath.replace("\\", "/");
			File videoFile = new File(videoPath);
			if (videoFile.exists()) {
				scriptfileVideo = videoFile.list()[0];
			}
			logger.info("scriptfileVideo = " + scriptfileVideo);
			scriptfileVideo = videoPath + File.separator + scriptfileVideo;
		}
		String scriptPath = scriptsRootDir + appName + File.separator + Constants.RECORDED_SCRIPTS_PATH;
		scriptPath = scriptPath.replace("\\", "/");
		scriptFile = scriptPath + File.separator + fileName + Constants.DOTTXT;

		logger.info("scriptFile = " + scriptFile);

		scriptFile = scriptFile.replace("\\", "/");
		if (scriptfileVideo != null && !scriptfileVideo.isEmpty()) {
			scriptfileVideo = scriptfileVideo.replace("\\", "/");
		}

		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<String, Object>();
		if (scriptFile != null) {
			bodyMap.add("File", new FileSystemResource(scriptFile));
		}

		if (scriptfileVideo != null && !scriptfileVideo.isEmpty()) {
			bodyMap.add("FileVideo", new FileSystemResource(scriptfileVideo));
		}

		bodyMap.add("connectionName", connectionName);
		bodyMap.add("scriptFileName", fileName);
		bodyMap.add("format", format);
		bodyMap.add("recordVideo", recordVideo);
		bodyMap.add("status", status);
		bodyMap.add("reason", reason);
		bodyMap.add("appName", appName);

		logger.info("bodyMap = " + bodyMap);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		org.springframework.http.HttpEntity<MultiValueMap<String, Object>> requestEntity = new org.springframework.http.HttpEntity<>(
				bodyMap, headers);

		RestTemplate restTemplate = new RestTemplate();
		if (videoRecord.equalsIgnoreCase(Constants.VIDEO_RECORD_ON))
			postUrl = cobotServer + Constants.CobotServerUrls.RECORDER_CLI_SCRIPT;
		else
			postUrl = cobotServer + Constants.CobotServerUrls.RECORDER_CLI_SCRIPT_WITHOUT_VIDEO;
		logger.info("postUrl =" + postUrl);
		logger.info("requestEntity = " + requestEntity);
		try {
			ResponseEntity<String> response = restTemplate.exchange(postUrl, HttpMethod.POST, requestEntity,
					String.class);
			logger.info("response = " + response);
			if (response.getStatusCode() == HttpStatus.OK) {
				logger.info("success");
				try {
					boolean flag = fileExplorer.deleteDirectory(new File(scriptsRootDir + appName));
					if (flag)
						logger.info("Agent Files and Folder deleted successfully.");
					else {
						logger.info("Error while deleteing agent files");
					}
				} catch (Exception e) {
					logger.error("Error deleteting reorded files", e);
				}
				callBackStatus.setJobId(String.valueOf(STATUS.Success.getID()));
				callBackStatus.setReason(response.getBody());
			} else {
				callBackStatus.setJobId(String.valueOf(STATUS.Failed.getID()));
				callBackStatus.setReason(response.getBody());
			}
			System.out.println("Cobot Server response status: " + response.getStatusCode());
			System.out.println("Cobot Server response body: " + response.getBody());
		} catch (Exception e) {
			logger.info("ERROR >>> while export agent file to cobot server : " + e.getMessage());
			callBackStatus.setJobId(String.valueOf(STATUS.Failed.getID()));
			callBackStatus.setReason(e.getMessage());
		}
		logger.info("job " + JobId + " is completed");
		return callBackStatus;
	}

	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolve = new CommonsMultipartResolver();
		multipartResolve.setMaxUploadSize(-1);
		return multipartResolve;
	}
}
