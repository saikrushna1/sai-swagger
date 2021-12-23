package com.saviynt.identitybot.agent.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.JsonObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.sikuli.script.Screen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.sikuli.script.Match;
import com.google.common.collect.Lists;
import com.saviynt.identitybot.agent.constants.Constants;
import com.saviynt.identitybot.agent.constants.STATUS;
import com.saviynt.identitybot.agent.controller.StreamReader;
import com.saviynt.identitybot.agent.response.Status;
import com.saviynt.identitybot.agent.service.S3Explorer;


@Service
public class MacCLIPlayer {
	final static Logger logger = Logger.getLogger(MacCLIPlayer.class);
	
	public static String PLAYBACK_SCRIPT_PATH = "scripts/playCli.sh";
	
	public static String MAC_PID_TANSCRIPT_SUBSTRING = "Start-Transcript -Path "; 
	public static String POWER_SHELL = "pwsh";
	public static String TRANSCRIPT_PATH = "startTranscript.sh";
	public static String VIDEOSCRIPT_PATH = "startVideo.sh";
	public static String KILL_TERMINAL = "closeTerminal.sh";
	@Value("${scripts.dir}")
	String scriptsRootDir;

	
	String cliErrors=Constants.CLI_ERRORS;
	
	/*
	 * @Value("${ssm.username}") String ssmUsername;
	 * 
	 * @Value("${ssm.password}") String ssmPassword;
	 * 
	 */	
	@Value("${scripts.store}")
	String scriptsStore;

	@Autowired
	private S3Explorer s3Explorer;

	String recordFileName = null;
	String scriptFileName = null;
	public Status runTest(String scriptFileName,String videoFormat, String callbackUrl, String jobid,String content, boolean recordVideo, String appName) throws Exception {
		Status status = new Status();
		try {
			Status startVideoStatus = null;
			if (recordVideo) {
				startVideoStatus = startVideo(scriptFileName, videoFormat,appName);
			}
			
			Status parseFileStatus = parseFile(scriptFileName, content,appName);
			if(parseFileStatus.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
				Status executeBatFileStatus = executeBatFile(scriptFileName,appName);
				if(executeBatFileStatus.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
					if(recordVideo) {
						if(startVideoStatus != null && startVideoStatus.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
							status.setReason("Video Recording and Script file execution successfully. ");
							status.setJobId(String.valueOf(STATUS.Success.getID()));
						}else {
							status.setReason("Script file execution success but video recording failed.. "+startVideoStatus.getReason());
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
							status.setReason("Video Recorded Successfully but Script error. "
									+ executeBatFileStatus.getReason());
							status.setJobId(String.valueOf(STATUS.Failed.getID()));
						} else {
							status.setReason("Both Video and Script error.. "
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
						status.setReason("Video Recorded Successfully but Parse file error. "+parseFileStatus.getReason());
						status.setJobId(String.valueOf(STATUS.Success.getID()));
					}else {
						status.setReason("Both Video and Parse file error.. "+startVideoStatus.getReason() +" : "+parseFileStatus.getReason());
						status.setJobId(String.valueOf(STATUS.Failed.getID()));
					}           
				}else {
					status.setReason("Parse file error. "+parseFileStatus.getReason());
					status.setJobId(String.valueOf(STATUS.Failed.getID()));
				}
			}
			
			if (status.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
				JsonObject response = Json.createObjectBuilder().add("jobId", jobid).add("status", "Success")
						.add("reason", "").build();
				/*
				 * Status invokeStatus = invokeServiceImpl.invokePostCall(callbackUrl,
				 * response.toString());
				 * if(invokeStatus.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.
				 * getID()))) { logger.info("Job Status Successfully Sent to Cobot Server.");
				 * }else {
				 * logger.info("Job status unable to send to Cobot server, "+invokeStatus.
				 * getReason()); }
				 */
				logger.info("updating job status to SSM");
			//	updateSSMStatus(job, Constants.SUCCESS);
				logger.info("updated job status to SSM");
				//status = invokeStatus;
				
			}else if (status.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Inprocess.getID()))) {
				JsonObject response = Json.createObjectBuilder().add("jobId", jobid).add("status", "Success")
						.add("reason", status.getReason()).build();
				/*
				 * Status invokeStatus = invokeServiceImpl.invokePostCall(callbackUrl,
				 * response.toString());
				 * if(invokeStatus.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.
				 * getID()))) { logger.info("Job Status Successfully Sent to Cobot Server.");
				 * }else {
				 * logger.info("Job status unable to send to Cobot server, "+invokeStatus.
				 * getReason()); }
				 */
				logger.info("updating job status to SSM");
				//updateSSMStatus(job, Constants.SUCCESS);
				logger.info("updated job status to SSM");
				//status = invokeStatus;
			}
			else {       
				JsonObject response = Json.createObjectBuilder().add("jobId", jobid).add("status", "Failed")
						.add("reason", status.getReason()).build();
				/*
				 * Status invokeStatus = invokeServiceImpl.invokePostCall(callbackUrl,
				 * response.toString());
				 * if(invokeStatus.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.
				 * getID()))) { logger.info("Job Status Successfully Sent to Cobot Server.");
				 * }else {
				 * logger.info("Job status unable to send to Cobot server, "+invokeStatus.
				 * getReason()); }
				 */
				
				logger.info("updating job status to SSM");
				//updateSSMStatus(job, Constants.FAIL);
				logger.info("updated job status to SSM");
				//status = invokeStatus;
				
			}
			
		} catch (Exception e) {
			JsonObject response = Json.createObjectBuilder().add("jobId", jobid).add("status", "Failed")
					.add("reason", e.getMessage()).build();

			/*
			 * Status invokeStatus = invokeServiceImpl.invokePostCall(callbackUrl,
			 * response.toString());
			 * if(invokeStatus.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.
			 * getID()))) { logger.info("Job Status Successfully Sent to Cobot Server.");
			 * }else {
			 * logger.info("Job status unable to send to Cobot server, "+invokeStatus.
			 * getReason()); }
			 */
			logger.info("updating job status to SSM");
			//updateSSMStatus(job, Constants.FAIL);
			logger.info("updated job status to SSM");
			//status = invokeStatus;
		}
		return status;
	}
	
	/*
	 * public void updateSSMStatus(Job job, String jobStatus){
	 * logger.info("getting token from SSM by using user = "+ssmUsername
	 * +" and Password = "+ssmPassword); Status tokenResponse =
	 * authService.getAuthToken(ssmUsername, ssmPassword);
	 * logger.info("tokenResponse = "+tokenResponse.toString()); // TOKEN IS NOT
	 * NULL AND NOT EMPTY THEN ALLOW THE CONDITION if (tokenResponse != null &&
	 * tokenResponse.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID
	 * ()))) { String token = tokenResponse.getStatus();
	 * logger.info("token = "+token); if (!token.isEmpty()) { //Job job =
	 * jobRepository.getOne(jobID);
	 * ssmAuthService.sendTaskCompleteResponseBackToSSM(job.getTaskId(), token,
	 * jobStatus); }else { logger.info("SSM Token is not generated. "); } }else {
	 * logger.info("SSM Token is not generated."); } }
	 */
	@SuppressWarnings("unchecked")
	public Status parseFile(String scriptFileName,String content, String appName) throws IOException {
		Status status = new Status();
		String line;
		StringBuffer psBuffer = new StringBuffer();
		StringBuffer sqlBuffer = new StringBuffer();
		ArrayList<String> finalConditions = new ArrayList<>();
		finalConditions.add("PS /.*>");

		if (!new File(scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH).exists())
			new File(scriptsRootDir  + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH).mkdir();

		Files.deleteIfExists(Paths.get(
				scriptsRootDir + appName + File.separator  + Constants.CONVERTED_SCRIPTS_PATH + File.separator + scriptFileName + Constants.DOTSH));
		
		File convertedScriptsDir = new File(scriptsRootDir + appName + File.separator  + Constants.CONVERTED_SCRIPTS_PATH);
		
		if(!convertedScriptsDir.exists())
		{
			convertedScriptsDir.mkdirs();
		}
		
		
		Files.createFile(Paths.get(
				scriptsRootDir + appName + File.separator  + Constants.CONVERTED_SCRIPTS_PATH + File.separator + scriptFileName + Constants.DOTSH));

		BufferedReader br = new BufferedReader(new FileReader(new File(
				scriptsRootDir+ appName + File.separator  + Constants.RECORDED_SCRIPTS_PATH + File.separator + scriptFileName + Constants.DOTTXT)));

		String oldContent = "";
		while ((line = br.readLine()) != null) {
			oldContent = oldContent + line + System.lineSeparator();
		}

		Map<String, Object> cliParams = new ObjectMapper().readValue(content, HashMap.class);
		for (String key : cliParams.keySet()) {
			oldContent = StringUtils.replace(oldContent, "{{" + key + "}}", cliParams.get(key).toString());
		}

		FileWriter writer = new FileWriter(scriptsRootDir + appName + File.separator  + Constants.RECORDED_SCRIPTS_PATH + File.separator
				+ scriptFileName + "_hidden.txt");
		writer.write(oldContent);
		writer.close();
		br.close();

		br = new BufferedReader(new FileReader(new File(scriptsRootDir+ appName + File.separator  + Constants.RECORDED_SCRIPTS_PATH
				+ File.separator + scriptFileName + "_hidden.txt")));
		//psBuffer.append("cd " + scriptsRootDir + "\n");
		psBuffer.append("#! /bin/bash\n");
		
		psBuffer.append("echo \"\"\n");
		psBuffer.append("echo \"\"\n");
		
		if (!new File(scriptsRootDir+ appName + File.separator  + Constants.PLAYBACK_SCRIPTS_PATH).exists()) {

			new File(scriptsRootDir+ appName + File.separator  + Constants.PLAYBACK_SCRIPTS_PATH).mkdir();

		}

		String scriptoutputFile = scriptsRootDir+ appName + File.separator  + Constants.PLAYBACK_SCRIPTS_PATH + File.separator + scriptFileName + Constants.DOTTXT;

		psBuffer.append("exec > >(tee "+scriptoutputFile+") 2>&1");


		psBuffer.append("\nset -x \n");
		while ((line = br.readLine()) != null) {
			if (StringUtils.startsWith(line, "mysql> ")) {
				sqlBuffer.append(line.split("> ")[1] + "\n");
			} else if (StringUtils.contains(line, "> mysql")) {
				Pattern pattern = Pattern.compile(finalConditions.get(0));
				psBuffer.append(pattern.split(line)[pattern.split(line).length - 1].trim() +  " " + scriptsRootDir + Constants.CONVERTED_SCRIPTS_PATH + "/" + scriptFileName + ".txt \n");
			} else {
				
				//psBuffer.append(line);
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

		psBuffer.append("exit 1");
		FileOutputStream powershell = new FileOutputStream(
				scriptsRootDir+ appName + File.separator  + Constants.CONVERTED_SCRIPTS_PATH + File.separator + scriptFileName + Constants.DOTSH);
		powershell.write(psBuffer.toString().getBytes());
		powershell.flush();
		powershell.close();

		if (!StringUtils.isEmpty(sqlBuffer.toString())) {
			Files.deleteIfExists(Paths.get(scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH + File.separator
					+ scriptFileName + Constants.DOTTXT));
			Files.createFile(Paths.get(scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH + File.separator
					+ scriptFileName + Constants.DOTTXT));
			FileOutputStream sqlFile = new FileOutputStream(scriptsRootDir+ appName + File.separator  + Constants.CONVERTED_SCRIPTS_PATH
					+ File.separator + scriptFileName + Constants.DOTTXT);
			sqlFile.write(sqlBuffer.toString().getBytes());
			sqlFile.flush();
			sqlFile.close();
		}

		Files.deleteIfExists(Paths.get(scriptsRootDir + appName + File.separator + Constants.RECORDED_SCRIPTS_PATH + File.separator
				+ scriptFileName + Constants.HIDDEN_DOT_TXT));
		status.setJobId(String.valueOf(STATUS.Success.getID()));
		return status;
	}


	@SuppressWarnings("resource")
	public Status executeBatFile(String fileName, String appName) throws Exception {
		logger.info("executing the bat file");
		Status status = new Status();
		try {
			if (!new File(scriptsRootDir + appName + File.separator + Constants.PLAYBACK_SCRIPTS_PATH).exists()) {
				new File(scriptsRootDir + appName + File.separator + Constants.PLAYBACK_SCRIPTS_PATH).mkdir();
			}

			logger.info("executing the play back script");
		
		String playBackScript = this.getClass().getClassLoader().getResource(PLAYBACK_SCRIPT_PATH).getFile();
		
		String transScriptFile = scriptsRootDir 
				+ appName + File.separator +Constants.CONVERTED_SCRIPTS_PATH + File.separator + fileName + ".sh";
		
		String command = "/bin/bash "+ playBackScript +" "+ transScriptFile;
		logger.info("executing commands");
//		Process process = Runtime.getRuntime().exec(command +" > " 
//				+scriptsRootDir + appName + File.separator + Constants.PLAYBACK_SCRIPTS_PATH + File.separator + fileName + ".txt)2>&1");
		Process process = Runtime.getRuntime().exec(command +" > " 
				+ scriptsRootDir + appName + File.separator + Constants.PLAYBACK_SCRIPTS_PATH + File.separator + fileName + ".txt 2>&1");
		process.waitFor(10000, TimeUnit.MILLISECONDS);
		StreamReader errorReader = new 
				StreamReader(process.getErrorStream(), "ERROR");            

		StreamReader outputReader = new 
				StreamReader(process.getInputStream(), "OUTPUT");

		errorReader.start();
		outputReader.start();

		process.waitFor();
			logger.info("reading .txt file from palybackstore folder");
			Thread.sleep(3000);
		BufferedReader stdInput = new BufferedReader(new FileReader(new File(
				scriptsRootDir + appName + File.separator + Constants.PLAYBACK_SCRIPTS_PATH + File.separator + fileName + ".txt")));
		
		String content = "";
		String line;
		logger.info("adding response to content object");
		while ((line = stdInput.readLine()) != null) {
			content = content + line + System.lineSeparator();
		}

		logger.info("preparing error map");
		Map<String, String> errorMap = new HashMap<String, String>();
		for (String error : cliErrors.split(";")) {
			errorMap.put(error.split(":")[0], error.split(":")[1]);
		}

		status.setJobId(String.valueOf(STATUS.Success.getID()));
		
		logger.info("checking response text contains any error content");
		for (String error : errorMap.keySet()) {
			if (StringUtils.contains(content, errorMap.get(error))) {
				logger.error("CLI Error "+errorMap.get(error));
				status.setJobId(String.valueOf(STATUS.Failed.getID()));
				status.setReason(getErrorMessage(error));
			}
		}
		killProcess();
		status.setJobId(String.valueOf(STATUS.Success.getID()));
		} catch (Exception e) {
			logger.error("ERROR >>> while executing the bat file = "+e.getMessage());
			status.setJobId(String.valueOf(STATUS.Failed.getID()));
			status.setReason(e.getMessage());
		}
		return status;
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
	
	public Status startVideo(String fileName, String format,String appName) throws Exception {
		logger.info("Starting video capturing.");
		Status status = new Status();
		try {
			logger.info("checking playbackVideos path exists or not");
			if (!new File(scriptsRootDir + appName + File.separator + Constants.PLAYBACK_VIDEOS_PATH).exists()) {
				new File(scriptsRootDir + appName + File.separator + Constants.PLAYBACK_VIDEOS_PATH).mkdirs();
			}
			;
			logger.info("executing powershell with ffmpeg for recording the video, after recording saving");
			String recordScript = this.getClass().getClassLoader().getResource( "scripts/"+ VIDEOSCRIPT_PATH).getFile();
			logger.info("record Script = "+recordScript);
			String videoFile = recordScript +" "+ scriptsRootDir + appName + File.separator + Constants.PLAYBACK_VIDEOS_PATH + File.separator + fileName + new Date().getTime() + format;
			String command = "/bin/bash "+ videoFile;
			Process scriptRecordingProcess = Runtime.getRuntime().exec(command);
				
			logger.info("video recorded and saved into playbackvideos folder");
			status.setJobId(String.valueOf(STATUS.Success.getID()));
			status.setVideoFileName(videoFile);
		} catch (Exception e) {
			logger.error("ERROR >>> while starting the video recording");
			status.setJobId(String.valueOf(STATUS.Failed.getID()));
			status.setReason(e.getMessage());			
		}
		return status;
	}

	public Status killProcess() throws IOException {
		logger.info("Recording Stoping...");
		Status status = new Status();
		try {
			logger.info("executing the cmd.exe for kill the poershell");
		
			String recordScript = this.getClass().getClassLoader().getResource("scripts/"+  KILL_TERMINAL).getFile();
			String command = "/bin/bash "+ recordScript;
			Process scriptRecordingProcess = Runtime.getRuntime().exec(command);
			String cmdImage = this.getClass().getClassLoader().getResource("static/img/cmd-2.png").getFile();	
			String mincmdImage = this.getClass().getClassLoader().getResource("static/img/minimizecmd.png").getFile();
			String terminateImage = this.getClass().getClassLoader().getResource("static/img/terminate.PNG").getFile();

			Screen s = new Screen();
			org.sikuli.script.Pattern cmdButton = new org.sikuli.script.Pattern(cmdImage);
			org.sikuli.script.Pattern minCmdButton = new org.sikuli.script.Pattern(mincmdImage);
			org.sikuli.script.Pattern terminateButton = new org.sikuli.script.Pattern(terminateImage);

			ArrayList<Match> minCmdList = Lists.newArrayList(s.findAll(terminateButton));
			if (minCmdList.size() != 0) {
				s.click(minCmdList.get(0));
				s.click(terminateButton);
			} else {
				s.click(cmdButton);
				s.click(terminateButton);
			}
 
			logger.info("terminating terminal");
			logger.info("Recording Stopped...");
			status.setJobId(String.valueOf(STATUS.Success.getID()));
		} catch (Exception e) {
			logger.error("ERROR >>> while stopping the recording = " + e.getMessage());
			status.setJobId(String.valueOf(STATUS.Failed.getID()));
			status.setReason(e.getMessage());
		}
		
		if (scriptsStore != null && scriptsStore.equalsIgnoreCase(Constants.S3_BUCKET)) {
			try {
				File file = new File(Constants.RECORD_VIDEO_PATH + File.separator + this.recordFileName);
				s3Explorer.saveRecordedVideo(Constants.RECORD_VIDEO_PATH, file, "RecordCLI");
			} catch (Exception e) {
				logger.error("ERROR >>> while saving recorded video in s3 bucket ", e);
			}

			try {
				File file = new File(Constants.CONVERTED_SCRIPTS_PATH + File.separator + this.scriptFileName + ".txt");
				s3Explorer.saveRecordedVideo(Constants.CONVERTED_SCRIPTS_PATH, file, "ScriptCLI");
			} catch (Exception e) {
				logger.error("ERROR >>> while saving recorded txt in s3 bucket ", e);
			}

			try {
				File file = new File(Constants.CONVERTED_SCRIPTS_PATH + File.separator + this.scriptFileName + ".bat");
				s3Explorer.saveRecordedVideo(Constants.CONVERTED_SCRIPTS_PATH, file, "ScriptCLI");
			} catch (Exception e) {
				logger.error("ERROR >>> while saving bat in s3 bucket ", e);
			}
		}
		return status;
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
}


