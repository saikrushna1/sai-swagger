package com.saviynt.identitybot.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saviynt.identitybot.Service.Impl.InvokeServiceImpl;
import com.saviynt.identitybot.Service.SSMAuthService;
import com.saviynt.identitybot.constants.STATUS;
import com.saviynt.identitybot.controller.Status;
import com.saviynt.identitybot.model.Job;
import com.saviynt.identitybot.repository.JobRepository;

@Service
public class CLIPlayer {
	final static Logger logger = Logger.getLogger(CLIPlayer.class);
	@Value("${scripts.dir}")
	String scriptsRootDir;

	@Value("${cli.errors}")
	String cliErrors;
	
	@Value("${ssm.username}")
	String ssmUsername;

	@Value("${ssm.password}")
	String ssmPassword;
	
	@Autowired
	JobRepository jobRepository;
	
	@Autowired
	SSMAuthService authService;
	
	@Autowired
	SSMAuthService ssmAuthService;
	
	@Autowired
	InvokeServiceImpl invokeServiceImpl;

	public Status runTest(Job job, String callbackUrl, String jobid,String appName) throws Exception {
		Status status = new Status();
		try {
			Status startVideoStatus = null;
			if (job.getRecordVideo()) {
				startVideoStatus = startVideo(job.getScriptFileName(), job.getVideoFormat(),appName);
			}
			
			Status parseFileStatus = parseFile(job,appName);
			if(parseFileStatus.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
				Status executeBatFileStatus = executeBatFile(job.getScriptFileName(),appName);
				if(executeBatFileStatus.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
					if(job.getRecordVideo()) {
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
					if (job.getRecordVideo()) {
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
				if(job.getRecordVideo()) {
					if(startVideoStatus != null && startVideoStatus.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
						status.setReason("Video Recorded Successfully but Parse file error. "+parseFileStatus.getReason());
						status.setJobId(String.valueOf(STATUS.Failed.getID()));
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
				Status invokeStatus = invokeServiceImpl.invokePostCall(callbackUrl, response.toString());
				if(invokeStatus.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
					logger.info("Job Status Successfully Sent to Cobot Server.");
				}else {
					logger.info("Job status unable to send to Cobot server, "+invokeStatus.getReason());
				}
				logger.info("updating job status to SSM");
				//updateSSMStatus(job, Constants.SUCCESS,Constants.SUCCESS);
				logger.info("updated job status to SSM");
				status = invokeStatus;
				
			}else if (status.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Inprocess.getID()))) {
				JsonObject response = Json.createObjectBuilder().add("jobId", jobid).add("status", "Success")
						.add("reason", status.getReason()).build();
				Status invokeStatus = invokeServiceImpl.invokePostCall(callbackUrl, response.toString());
				if(invokeStatus.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
					logger.info("Job Status Successfully Sent to Cobot Server.");
				}else {
					logger.info("Job status unable to send to Cobot server, "+invokeStatus.getReason());
				}
				logger.info("updating job status to SSM");
				//updateSSMStatus(job, Constants.SUCCESS,Constants.SUCCESS);
				logger.info("updated job status to SSM");
				status = invokeStatus;
			}
			else {       
				JsonObject response = Json.createObjectBuilder().add("jobId", jobid).add("status", "Failed")
						.add("reason", status.getReason()).build();
				Status invokeStatus = invokeServiceImpl.invokePostCall(callbackUrl, response.toString());
				if(invokeStatus.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
					logger.info("Job Status Successfully Sent to Cobot Server.");
				}else {
					logger.info("Job status unable to send to Cobot server, "+invokeStatus.getReason());
				}
				
				logger.info("updating job status to SSM");
				//updateSSMStatus(job, Constants.FAIL,status.getReason());
				logger.info("updated job status to SSM");
				status = invokeStatus;
				
			}
			
		} catch (Exception e) {
			JsonObject response = Json.createObjectBuilder().add("jobId", jobid).add("status", "Failed")
					.add("reason", e.getMessage()).build();

			Status invokeStatus = invokeServiceImpl.invokePostCall(callbackUrl, response.toString());
			if(invokeStatus.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
				logger.info("Job Status Successfully Sent to Cobot Server.");
			}else {
				logger.info("Job status unable to send to Cobot server, "+invokeStatus.getReason());
			}
			logger.info("updating job status to SSM");
			//updateSSMStatus(job, Constants.FAIL,e.getMessage());
			logger.info("updated job status to SSM");
			status = invokeStatus;
		}
		return status;
	}
	
//	public void updateSSMStatus(Job job, String jobStatus,String comment){
//		logger.info("getting token from SSM by using user = "+ssmUsername +" and Password = "+ssmPassword);
//		Status tokenResponse = authService.getAuthToken(ssmUsername, ssmPassword);
//		logger.info("tokenResponse = "+tokenResponse.toString());
//		// TOKEN IS NOT NULL AND NOT EMPTY THEN ALLOW THE CONDITION
//		if (tokenResponse != null && tokenResponse.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {  
//			String token = tokenResponse.getStatus();
//			logger.info("token = "+token);
//			if (!token.isEmpty()) {
//				//Job job = jobRepository.getOne(jobID);
//				ssmAuthService.sendTaskUpdateResponseBackToSSM(job.getTaskId(), token, jobStatus,comment);
//			}else {
//				logger.info("SSM Token is not generated. ");
//			}
//		}else {
//			logger.info("SSM Token is not generated.");
//		}
//	}

	@SuppressWarnings("unchecked")
	public Status parseFile(Job job,String appName) throws Exception {
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
					+ job.getScriptFileName() + ".bat"));
			logger.info("creating .bat file in " + scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH + File.separator
					+ job.getScriptFileName() + ".bat");
			Files.createFile(Paths.get(scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH + File.separator
					+ job.getScriptFileName() + ".bat"));

			logger.info("reading .txt file in " + scriptsRootDir + appName + File.separator + Constants.RECORDED_SCRIPTS_PATH + File.separator
					+ job.getScriptFileName() + ".txt");
			BufferedReader br = new BufferedReader(new FileReader(new File(scriptsRootDir
					+ appName + File.separator + Constants.RECORDED_SCRIPTS_PATH + File.separator + job.getScriptFileName() + ".txt")));

			String oldContent = "";
			while ((line = br.readLine()) != null) {
				oldContent = oldContent + line + System.lineSeparator();
			}
			logger.info("appending content to list = " + oldContent);
			logger.info("preparing the cli params from job content");
			Map<String, Object> cliParams = new ObjectMapper().readValue(job.getContent().toString(), HashMap.class);
			logger.info("prepared cliparams = " + cliParams);

			logger.info("adding cli params to olcContent");
			for (String key : cliParams.keySet()) {
				oldContent = StringUtils.replace(oldContent, "{{" + key + "}}", cliParams.get(key).toString());
			}
			logger.info("added cli params to olcContent = " + oldContent);

			logger.info("appending above oldcontent to hidden.txt file");
			FileWriter writer = new FileWriter(scriptsRootDir + appName + File.separator + Constants.RECORDED_SCRIPTS_PATH + File.separator
					+ job.getScriptFileName() + "_hidden.txt");
			writer.write(oldContent);
			writer.close();
			br.close();

			logger.info("reading the hidden.txt file");
			br = new BufferedReader(new FileReader(new File(scriptsRootDir + appName + File.separator + Constants.RECORDED_SCRIPTS_PATH
					+ File.separator + job.getScriptFileName() + "_hidden.txt")));
			psBuffer.append("cd " + scriptsRootDir + "\n");
			while ((line = br.readLine()) != null) {
				if (StringUtils.startsWith(line, "mysql> ")) {
					sqlBuffer.append(line.split("> ")[1] + "\n");
				} else if (StringUtils.contains(line, "mysql")) {
					psBuffer.append("for /F \"tokens=*\" %%A in (" + scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH
							+ File.separator + job.getScriptFileName() + ".txt) do ");
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
			psBuffer.append("exit 1");
			logger.info("psBuffer = " + psBuffer);
			FileOutputStream powershell = new FileOutputStream(scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH
					+ File.separator + job.getScriptFileName() + ".bat");
			powershell.write(psBuffer.toString().getBytes());
			powershell.flush();
			powershell.close();
			logger.info("checking and saving script file");
			if (!StringUtils.isEmpty(sqlBuffer.toString())) {
				Files.deleteIfExists(Paths.get(scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH + File.separator
						+ job.getScriptFileName() + ".txt"));
				Files.createFile(Paths.get(scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH + File.separator
						+ job.getScriptFileName() + ".txt"));
				FileOutputStream sqlFile = new FileOutputStream(scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH
						+ File.separator + job.getScriptFileName() + ".txt");
				sqlFile.write(sqlBuffer.toString().getBytes());
				sqlFile.flush();
				sqlFile.close();
			}
			logger.info("deleting the hidden.txt file if exists");
			Files.deleteIfExists(Paths.get(scriptsRootDir + appName + File.separator + Constants.RECORDED_SCRIPTS_PATH + File.separator
					+ job.getScriptFileName() + "_hidden.txt"));
			logger.info("deleted the hidden.txt file if exists");
			
			status.setJobId(String.valueOf(STATUS.Success.getID()));
		} catch (Exception e) {
			status.setJobId(String.valueOf(STATUS.Failed.getID()));
			status.setReason(e.getMessage());
			killProcess();
		}
		return status;
	}

	@SuppressWarnings("resource")
	public Status executeBatFile(String fileName,String appName) throws Exception {
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
			
			logger.info("saving the play script to .txt file");
			Runtime.getRuntime().exec("cmd /c start /wait cmd.exe /k powershell type " + scriptsRootDir
					+ appName + File.separator + Constants.PLAYBACK_SCRIPTS_PATH + File.separator + fileName + ".txt");

			BufferedReader stdInput = new BufferedReader(new FileReader(
					new File(scriptsRootDir + appName + File.separator + Constants.PLAYBACK_SCRIPTS_PATH + File.separator + fileName + ".txt")));
			Thread.sleep(3000);
			String content = "";
			String line;
			logger.info("reading the above saved script file.");
			while ((line = stdInput.readLine()) != null) {
				content = content + line + System.lineSeparator();
			}
			logger.info("content = "+content);
			logger.info("killing the process");
			killProcess();
			
			Map<String, String> errorMap = new HashMap<String, String>();
			for (String error : cliErrors.split(";")) {
				errorMap.put(error.split(":")[0], error.split(":")[1]);
			}
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
			killProcess();
		}
		return status;
	}

	public Status startVideo(String fileName, String format,String appName) throws Exception {
		logger.info("Starting video capturing.");
		Status status = new Status();
		try {
			logger.info("checking playbackVideos path exists or not");
			if (!new File(scriptsRootDir + appName + File.separator + "playbackVideos").exists()) {
				new File(scriptsRootDir + appName + File.separator + "playbackVideos").mkdir();
			}
			logger.info("called killProcess function");
			killProcess();
			Thread.sleep(5000);
			logger.info("executing powershell with ffmpeg for recording the video, after recording saving");
			Runtime.getRuntime().exec(
					"cmd /c start /min /wait powershell.exe ffmpeg -f gdigrab -i desktop -framerate 32 -vcodec libx264 "
							+ scriptsRootDir + appName + File.separator + "playbackVideos/" + fileName + new Date().getTime() + format);
			Thread.sleep(5000);
			logger.info("video recorded and saved into playbackvideos folder");
			status.setJobId(String.valueOf(STATUS.Success.getID()));
		} catch (Exception e) {
			logger.error("ERROR >>> while starting the video recording");
			status.setJobId(String.valueOf(STATUS.Failed.getID()));
			status.setReason(e.getMessage());
		}
		return status;
	}

	public void killProcess() throws Exception {
		logger.info("executing the cmd.exe for killing the powershell.exe");
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command("cmd.exe", "/c", "taskkill /im powershell.exe");
		processBuilder.start();

		processBuilder.command("cmd.exe", "/c", "taskkill /im cmd.exe");
		processBuilder.start();
		Thread.sleep(3000);
		logger.info("klled powershell.exe");
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