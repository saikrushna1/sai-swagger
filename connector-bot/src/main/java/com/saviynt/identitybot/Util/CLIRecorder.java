package com.saviynt.identitybot.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.saviynt.identitybot.constants.STATUS;
import com.saviynt.identitybot.controller.Status;

@Service
public class CLIRecorder {

	final static Logger logger = Logger.getLogger(CLIRecorder.class);

	@Value("${scripts.dir}")
	String scriptsRootDir;

	@Value("${cobot.status.callback.url}")
	String cobotStatusCallbackURL;

	@Value("${scripts.store}")
	String scriptsStore;

	@Autowired
	private S3Explorer s3Explorer;

	String recordFileName = null;
	String scriptFileName = null;

	public Status record(String fileName, Boolean recordVideo, String format, String connectionName) {
		Status status =  new Status();
		try {
			Status startVideoResponse = null;
			if (recordVideo) {
				startVideoResponse = startVideo(connectionName + "_" + fileName + "_", format);
			}
			Status recordStatus = recordTest(fileName.substring(0, 1).toUpperCase() + fileName.substring(1), connectionName);
			if(recordStatus.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
				if(recordVideo) {
					if(startVideoResponse != null && startVideoResponse.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
						status.setReason("Both Video and Script Recorded Successfully.");
						status.setJobId(String.valueOf(STATUS.Success.getID()));
					}else {
						status.setReason("Script Recorded Successfully but Video Recording Failed."+ startVideoResponse.getReason());
						status.setJobId(String.valueOf(STATUS.Inprocess.getID()));
					}
				}else {
					status.setReason("Script Recorded Successfully.");
					status.setJobId(String.valueOf(STATUS.Success.getID()));
				}
			}else {		
				if(recordVideo) {
					if(startVideoResponse != null && startVideoResponse.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
						status.setReason("Video Recorded Successfully but Script Recording Failed. "+recordStatus.getReason());
						status.setJobId(String.valueOf(STATUS.Success.getID()));
					}else {
						status.setReason("Both Video and Script Recording Failed. "+startVideoResponse.getReason() +" : "+recordStatus.getReason());
						status.setJobId(String.valueOf(STATUS.Failed.getID()));
					}           
				}else {
					status.setReason("Script Recording Failed. "+recordStatus.getReason());;
					status.setJobId(String.valueOf(STATUS.Failed.getID()));
				}
			}
		} catch (Exception e) {
			status.setJobId(String.valueOf(STATUS.Failed.getID()));
			status.setReason(e.getMessage());
		}
		return status;
	}

	public Status recordTest(String fileName, String connectionName) throws Exception {
		logger.info("started recordTest with fileName = "+fileName);
		logger.info("connectionName = "+connectionName);
		Status status = new Status();
		try {
			logger.info("checking whether file exists or not");
			Files.deleteIfExists(
					Paths.get(scriptsRootDir + connectionName + File.separator + Constants.RECORDED_SCRIPTS_PATH + File.separator + fileName + ".txt"));
			if (!new File(scriptsRootDir + connectionName + File.separator + Constants.RECORDED_SCRIPTS_PATH).exists()) {
				new File(scriptsRootDir + connectionName + File.separator + Constants.RECORDED_SCRIPTS_PATH).mkdirs();
			}
			logger.info("starting script capturing and appending to .txt file");
			Process scriptRecordingProcess = Runtime.getRuntime()
					.exec("cmd /c start /wait cmd.exe  /k powershell -NoExit  Start-Transcript -Path " + scriptsRootDir
							+ connectionName + File.separator + Constants.RECORDED_SCRIPTS_PATH + File.separator + fileName + ".txt");
			scriptRecordingProcess.waitFor();

			logger.info("reading reacorderd script file.");
			BufferedReader br = new BufferedReader(new FileReader(
					new File(scriptsRootDir + connectionName + File.separator + Constants.RECORDED_SCRIPTS_PATH + File.separator + fileName + ".txt")));
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
			logger.info("contnet = " + content);
			logger.info("writing content into converted script .txt file");
			FileWriter writer = new FileWriter(
					scriptsRootDir + connectionName + File.separator + Constants.RECORDED_SCRIPTS_PATH + File.separator + fileName + ".txt");
			writer.write(content);
			writer.close();
			br.close();
			status.setJobId(String.valueOf(STATUS.Success.getID()));
		} catch (Exception e) {
			status.setJobId(String.valueOf(STATUS.Failed.getID()));
			status.setReason(e.getMessage());
			logger.info("ERROR >>>  while recorded script to converted script = " + e.getMessage());
		}
		Status killProcessResponse =  killProcess();
		return status;
	}

	public Status startVideo(String fileName, String format) throws Exception {
		logger.info("checking the scriptsRootDir exists or not");
		Status status = new Status();
		String appName = fileName.split("_")[0].trim();
		logger.info("appName = "+appName);
		if (!new File(scriptsRootDir + appName + File.separator + Constants.RECORD_VIDEO_PATH).exists()) {
			new File(scriptsRootDir + appName + File.separator + Constants.RECORD_VIDEO_PATH).mkdirs();
		}
		try {
			logger.info("executing the powershell open command");
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command("cmd.exe", "/c", "taskkill /im powershell.exe");
			processBuilder.start();
			Thread.sleep(3000);
			this.scriptFileName = fileName;
			this.recordFileName = fileName + new Date().getTime() + format;
			logger.info("recordFileName = " + this.recordFileName);
			Runtime.getRuntime().exec(
					"cmd /c start /min /wait powershell.exe ffmpeg -f gdigrab -i desktop -framerate 32 -vcodec libx264 "
							+ scriptsRootDir + appName + File.separator + Constants.RECORD_VIDEO_PATH + File.separator + recordFileName);
			Thread.sleep(5000);
			status.setJobId(String.valueOf(STATUS.Success.getID()));
		} catch (Exception e) {
			logger.error("ERROR while opening the shell command = " + e.getMessage());
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
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command("cmd.exe", "/c", "taskkill /im powershell.exe");
			processBuilder.start();

			processBuilder.command("cmd.exe", "/c", "taskkill /im cmd.exe");
			processBuilder.start();
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

	public static Status sendExecutionStatus(String postUrl, String inputJson) throws Exception {
		
		Status status = new Status();
		
		logger.info("postUrl = "+postUrl);
		logger.info("inputJson = "+inputJson);
		URL url = new URL(postUrl);

		logger.info("creating HttpURLConnection connection with above url");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json; utf-8");
		con.setRequestProperty("Accept", "application/json");
		con.setDoOutput(true);
		
		logger.info("writing inputJson to HttpURLConnection object");
		try (OutputStream os = con.getOutputStream()) {
			byte[] input = inputJson.getBytes("utf-8");
			os.write(input, 0, input.length);
		}
		int code = con.getResponseCode();
		logger.info("HttpURLConnection Object Response Code = "+code);
		// IF RESPONSE CODE IS 200
		if (code == STATUS.Success.getID()) {
			try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
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
		}else if(code == STATUS.Unauthorized.getID()) {
			logger.info("Unauthorized");
			status.setJobId(String.valueOf(code));
			status.setReason(Constants.UNAUTHORIZED_MSG);
		}else if(code == STATUS.Forbidden.getID()) {
			logger.info("Forbidden");
			status.setJobId(String.valueOf(code));
			status.setReason("No permissions for this inputJson = "+inputJson);
		}else if(code == STATUS.UNKNOWN.getID()) {
			logger.info("Service Unavailable");
			status.setJobId(String.valueOf(code));
			status.setReason("Service Unavailable");
		}
		return status;
	}
}
