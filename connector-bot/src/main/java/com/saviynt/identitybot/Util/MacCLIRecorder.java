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
//import org.sikuli.script.Match;
//import org.sikuli.script.Pattern;
//import org.sikuli.script.Screen;
//import com.google.common.collect.Lists;
//import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.saviynt.identitybot.constants.STATUS;
import com.saviynt.identitybot.controller.Status;

@Service
public class MacCLIRecorder {
	final static Logger logger = Logger.getLogger(MacCLIRecorder.class);

	
	
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
	
	public static String MAC_PID_TANSCRIPT_SUBSTRING = "Start-Transcript -Path "; 
	public static String POWER_SHELL = "pwsh";
	public static String TRANSCRIPT_PATH = "startTranscript.sh";
	public static String VIDEOSCRIPT_PATH = "startVideo.sh";
	public static String KILL_TERMINAL = "closeTerminal.sh";

	public Status record(String fileName, Boolean recordVideo, String format, String connectionName) {
		Status status =  new Status();
		try {
			Status startVideoResponse = null;
			if (recordVideo) {
				startVideoResponse = startVideo(fileName, connectionName , format);
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
		String transcritfile = null;
		try {
			logger.info("checking whether file exists or not");
			transcritfile =scriptsRootDir+ connectionName + File.separator +  Constants.RECORDED_SCRIPTS_PATH + File.separator + fileName + ".txt";

			Files.deleteIfExists(Paths.get(transcritfile));
			File scriptFile = new File(transcritfile);
			if(!scriptFile.exists())
			{
				File parentDir = scriptFile.getParentFile();
				if(!parentDir.exists() )
				{
					parentDir.mkdirs();
				}
				scriptFile.createNewFile();
			}
			
			logger.info("starting script capturing and appending to .txt file");
			String recordScript = this.getClass().getClassLoader().getResource("scripts/" + TRANSCRIPT_PATH).getFile();
			String command = "/bin/bash "+ recordScript +" "+ transcritfile;
			Process scriptRecordingProcess = Runtime.getRuntime().exec(command);

			StreamReader errorReader = new StreamReader(scriptRecordingProcess.getErrorStream(), "ERROR");            

			StreamReader outputReader = new 
					StreamReader(scriptRecordingProcess.getInputStream(), "OUTPUT");

			errorReader.start();
			outputReader.start();


			scriptRecordingProcess.waitFor();
			Thread.sleep(12000);
			boolean isTranscriptTernimalClosed = waitForTerminalToClose(transcritfile);

			if(!isTranscriptTernimalClosed)
			{
				String terminalCloseTimeout= "Power Windows is not closed. Terminal Close Waiting Timeout";
				logger.error(terminalCloseTimeout);
				throw new Exception(terminalCloseTimeout);
			}

			BufferedReader br = new BufferedReader(new FileReader(new File(transcritfile)));
			String content = "";
			String line = "";
			boolean psFound = false;
			while ((line = br.readLine()) != null) {
				if (!psFound && StringUtils.startsWith(line, Constants.PS)) {
					psFound = true;
				}

				if (psFound) {
					content = content + line + System.lineSeparator();
				}
			}

			FileWriter writer = new FileWriter(transcritfile);
			writer.write(content);
			writer.close();
			br.close();

		
			status.setJobId(String.valueOf(STATUS.Success.getID()));
		} catch (Exception e) {
			status.setJobId(String.valueOf(STATUS.Failed.getID()));
			status.setReason(e.getMessage());
			logger.info("ERROR >>>  while recorded script to converted script = " + e.getMessage());
		}
		//Status killProcessResponse =  killProcess();
		return status;
	}

	public Status startVideo(String fileName, String appName,String format) throws Exception {
		logger.info("checking the scriptsRootDir exists or not");
		Status status = new Status();

		logger.info("appName = "+appName);
		if (!new File(scriptsRootDir + appName + File.separator + Constants.RECORD_VIDEO_PATH).exists()) {
			new File(scriptsRootDir + appName + File.separator + Constants.RECORD_VIDEO_PATH).mkdirs();
		}
		try {
			logger.info("scriptsRootDir = "+scriptsRootDir);
			logger.info("executing the powershell open command");
			String recordScript = this.getClass().getClassLoader().getResource( "scripts/"+ VIDEOSCRIPT_PATH).getFile();
			logger.info("record Script = "+recordScript);
			String command = "/bin/bash "+ recordScript +" "+ scriptsRootDir + appName + File.separator + Constants.RECORD_VIDEO_PATH + File.separator +appName+"_"+ fileName +"_"+ new Date().getTime() + ".mkv";
			Process scriptRecordingProcess = Runtime.getRuntime().exec(command);
		
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
		
			String recordScript = this.getClass().getClassLoader().getResource("scripts/"+  KILL_TERMINAL).getFile();
			String command = "/bin/bash "+ recordScript;
			Process scriptRecordingProcess = Runtime.getRuntime().exec(command);

			String cmdImage = this.getClass().getClassLoader().getResource("static/img/cmd-2.png").getFile();
			String mincmdImage = this.getClass().getClassLoader().getResource("static/img/minimizecmd.png").getFile();
			String terminateImage = this.getClass().getClassLoader().getResource("static/img/terminate.PNG").getFile();
//			Screen s = new Screen();
//			Pattern cmdButton = new Pattern(cmdImage);
//			Pattern minCmdButton = new Pattern(mincmdImage);
//			Pattern terminateButton = new Pattern(terminateImage);
//
//			ArrayList<Match> minCmdList = Lists.newArrayList(s.findAll(terminateButton));
//			if (minCmdList.size() != 0) {
//				s.click(minCmdList.get(0));
//				s.click(terminateButton);
//			} else {
//				s.click(cmdButton);
//				s.click(terminateButton);
//			} 

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
	

	private  boolean waitForTerminalToClose( String fileName) throws Exception
	{
		for(int i=0 ; i<=2000;i++)
		{
			boolean isTerminalClosed = hasTranscriptTerminalClosed(fileName);
			if(!isTerminalClosed)
			{
				logger.debug("waiting for recording terminal to close");
				Thread.sleep(5000);
			}
			else
			{
				return true;
			}
		}
		return false;
	}


	public static boolean hasTranscriptTerminalClosed(String fileName) throws Exception{

		String command="ps -a -x | grep \"pwsh.*"+fileName+"\"";
	

		String[] cmd = {
				"/bin/sh",
				"-c",
				command
		};

		Process process = Runtime.getRuntime().exec(cmd);

		BufferedReader r =  new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = null;


		while((line=r.readLine())!=null) {
			if(line.contains(MAC_PID_TANSCRIPT_SUBSTRING+fileName) && line.contains(POWER_SHELL))
			{
				return false;
			}
		}
		return true;
	}
}
