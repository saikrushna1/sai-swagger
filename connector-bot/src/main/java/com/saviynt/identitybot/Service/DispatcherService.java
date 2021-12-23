package com.saviynt.identitybot.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.saviynt.identitybot.CobotApplication;
import com.saviynt.identitybot.Util.CLIPlayer;
import com.saviynt.identitybot.Util.Constants;
import com.saviynt.identitybot.Util.MacCLIPlayer;
import com.saviynt.identitybot.Util.ScriptUtil;
import com.saviynt.identitybot.constants.STATUS;
import com.saviynt.identitybot.controller.Status;
import com.saviynt.identitybot.model.BotAgent;
import com.saviynt.identitybot.model.Job;
import com.saviynt.identitybot.repository.BotAgentRepository;
import com.saviynt.identitybot.repository.JobRepository;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * 
 * @author Srinivasa Reddy Challa, Raj Kumar
 * 
 *         Service that dispatches the service requests to different target
 *         systems for execution
 *
 */

@Component
public class DispatcherService {

	final static Logger logger = Logger.getLogger(DispatcherService.class);

	@Autowired
	JobRepository jobRepository;

	@Autowired
	RemoteAgentService remoteAgentService;

	@Value("${cobot.status.callback.url}")
	String statusCallBackUrl;

	@Value("${excludingPattern}")
	String excludingPattern;

	@Value("${scripts.dir}")
	String scriptsRootDir;

	@Value("${sendSignal.root.dir}")
	String sendSignalRootDir;

	@Value("${testcases.root}")
	String testcasesRoot;

	@Value("${agent.port}")
	String agentSystemPort;

	@Value("${agent.scriptUploadUrl}")
	String agentScriptUploadUrl;

	@Value("${parentProperty}")
	String parentProperty;

	@Value("${cobot.scriptType}")
	String cobotScriptType;

	@Value("${cli.scriptType}")
	String cliScriptType;

	@Value("${testbot.dataparameterzation.multiplerow.allowed}")
	String dpallowed;

	@Autowired
	private Environment env;

	@Autowired
	CLIPlayer playBackCli;

	@Autowired
	MacCLIPlayer macCLIPlayer;

	@Value("${cobot.url}")
	String cobotURL;

	@Autowired
	BotAgentRepository botAgentRepository;

	/**
	 * Execute the submitted {@code Job} which contains the script details that
	 * needs to be executed
	 * 
	 * @param string
	 * @param data:     data for the script to be executed
	 * @param appJobID: Application JOB Id which script uses to call back Cobot API
	 *                  to update the status of the executed script
	 * @param job:      {@code Job} which contains all the details of the script to
	 *                  be executed
	 * @throws Exception
	 */
	public void processJob(String appJobID, Job job, String remoteIP) throws Exception {

		logger.info("starting processJob.");

		logger.info("is multiple parameters allowed:-" + dpallowed);

		if (job.getScriptFileName() != null) {

			WebDriverManager.chromedriver().setup();

			String executeMode = env.getProperty(Constants.EXECUTE_DOT_MODE);
			logger.info("Execution Mode " + executeMode);

			System.setProperty("parentProperty", parentProperty);

			if (dpallowed.equalsIgnoreCase("Yes") && checkParameterSytanx(job)) {

				processJobForMutipleRows(appJobID, job, remoteIP);

			} else {

				processJobSingleRow(appJobID, job, remoteIP);

			}
		}

	}

	private void processJobSingleRow(String appJobID, Job job, String remoteIP) throws Exception {

		executeScript(appJobID, job, remoteIP, 0, null);

	}

	private void processJobForMutipleRows(String appJobID, Job job, String remoteIP) throws Exception {

		String rootDir = CobotApplication.loadCobotProperties().getProperty("scripts.dir");
		String excelFileName = rootDir + "TestDataManagement.xlsx";
		boolean check = false;
		if (Files.exists(Paths.get(excelFileName))) {

			FileInputStream fis = new FileInputStream(new File(excelFileName));

			try (Workbook workbook = new XSSFWorkbook(fis)) {

				Sheet sheet = workbook.getSheet(job.getAppName());
				if (null != sheet) {
					for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
						Row r = sheet.getRow(i);

						String key = r.getCell(0) != null ? r.getCell(0).getStringCellValue() : null;
						String scriptName = r.getCell(1).getStringCellValue();
						if ((key == null || key.equalsIgnoreCase("NA"))
								|| !job.getScriptFileName().equalsIgnoreCase(scriptName + ".java"))
							continue;

						check = true;
						executeScript(appJobID, job, remoteIP, i, sheet);

					}
				}
			}
			fis.close();
		}

		if (!check) {
			processJobSingleRow(appJobID, job, remoteIP);
		}

	}

	private void executeScript(String appJobID, Job job, String remoteIP, int row, Sheet sheet) throws Exception {

		

			String executeMode = env.getProperty(Constants.EXECUTE_DOT_MODE);
			String scriptsDir = scriptsRootDir;
			logger.info("Execution Mode " + executeMode);

			String scriptRootDir = null;
			if (job.getScriptType().equals(cobotScriptType) || job.getScriptType().equals(cliScriptType)) {
				scriptRootDir = testcasesRoot;
			}
			logger.info("scriptRootDir = " + scriptRootDir);

			logger.info("calling transformScriptFile method");

			Status transformScriptFileStatus = new Status();
			transformScriptFileStatus.setJobId(String.valueOf(STATUS.Failed.getID()));
			transformScriptFileStatus.setReason("Invalid File Format for convert");
			
			logger.info("calling transformScriptFile method");
			if(job.getScriptFileName().endsWith(Constants.DOT_JAVA)) {
				transformScriptFileStatus = ScriptUtil.transformScriptFile(job, scriptsDir, scriptRootDir,
						job.getAppName(), row, sheet);
			}else if(job.getScriptFileName().endsWith(Constants.DOT_PY)) {
				transformScriptFileStatus.setJobId(String.valueOf(STATUS.Success.getID()));
			}
			
			if (transformScriptFileStatus.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {

				Status compileScriptFileStatus = new Status();
				compileScriptFileStatus.setJobId(String.valueOf(STATUS.Failed.getID()));
				compileScriptFileStatus.setReason("Invalid File Format to compile");
				
				
				if(job.getScriptFileName().endsWith(Constants.DOT_JAVA)) {
					compileScriptFileStatus = ScriptUtil.compileScriptFile(job, scriptRootDir, executeMode,
							job.getAppName());
				}else if(job.getScriptFileName().endsWith(Constants.DOT_PY)) {
					compileScriptFileStatus.setJobId(String.valueOf(STATUS.Success.getID()));
				}
				
				

				if (compileScriptFileStatus.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
					logger.info("compile success");

					logger.info("remoteIP = " + remoteIP);
					if (remoteIP == null) {

						runLocalIPScript(job, scriptRootDir, statusCallBackUrl, appJobID);
					} else {

						runRemoteIPScript(transformScriptFileStatus, compileScriptFileStatus, job, scriptRootDir,
								statusCallBackUrl, appJobID, remoteIP);
					}
				} else {
					updateJobStatus(job, compileScriptFileStatus);
				}
			} else {
				updateJobStatus(job, transformScriptFileStatus);
			}
		

	}

	private void runLocalIPScript(Job job, String scriptRootDir, String statusCallBackUrl, String appJobID)
			throws Exception {

		logger.info("cliScriptType = " + cliScriptType);
		if (job.getScriptType().equals(cliScriptType)) {

			runCliScript(job, statusCallBackUrl, appJobID);

		} else {

			loadRunnerClass(job, scriptRootDir, statusCallBackUrl, appJobID);
		}

	}

	private void runRemoteIPScript(Status transformScriptFileStatus, Status compileScriptFileStatus, Job job,
			String scriptRootDir, String statusCallBackUrl, String appJobID, String remoteIP) throws Exception {

		logger.info("ScriptType = " + job.getScriptType());
		if (Constants.WEB.equalsIgnoreCase(job.getScriptType())) {

			JSONObject parentScriptOutputdataList = new JSONObject();
			if (transformScriptFileStatus.isParentFlag()) {

				String parentKey = transformScriptFileStatus.getParentScriptKey().trim();

				logger.info("parentKey " + parentKey);
				parentScriptOutputdataList = updateParentFile(parentKey);
			}

			runWeb(job, appJobID, remoteIP, transformScriptFileStatus.isParentFlag(), parentScriptOutputdataList,
					scriptRootDir);

		} else if (com.saviynt.identitybot.constants.Constants.ScriptType.CLI_RECORD.toString()
				.equalsIgnoreCase(job.getScriptType())) {

			cliRecordScript(appJobID, job.getContent(), job.getScriptFileName(), remoteIP, job.getAppName());

		} else if (Constants.CLI.toString().equalsIgnoreCase(job.getScriptType())) {

			remoteCliScript(job, appJobID, remoteIP);

		} else if (Constants.DESKTOP.toString().equalsIgnoreCase(job.getScriptType())) {
			runRemoteDesktop(job, appJobID, remoteIP);
		}else if (Constants.PYWIN.toString().equalsIgnoreCase(job.getScriptType())) {
			String recordCallbackUpdateStatus = null;
			Boolean recordVideo = job.getRecordVideo();
			String videoFormat = job.getVideoFormat();

			if (recordVideo == null) {
				recordVideo = false;
			}
			if(job.getParalleExcecutionStatus().equalsIgnoreCase(Constants.ON)) {
				recordVideo = false;
			}
			if (recordVideo) {
				recordCallbackUpdateStatus = cobotURL
						+ com.saviynt.identitybot.constants.Constants.COBOT_STATUS_URLS.PLAY_REMOTE_DESKTOP_PYWIN;
			} else {
				recordCallbackUpdateStatus = cobotURL
						+ com.saviynt.identitybot.constants.Constants.COBOT_STATUS_URLS.PLAY_REMOTE_DESKTOP_PYWIN_WITHOUT_VIDEO;
			}

			if (videoFormat == null) {
				videoFormat = ".mp4";
			}

			remoteAgentService.playDesktopPywinJob(agentSystemPort,
					com.saviynt.identitybot.constants.Constants.AGENT_URLS.EXECUTE_REMOTE_DESKTOP_PYWIN,
					recordCallbackUpdateStatus, appJobID, job.getContent(), job.getScriptFileName(), remoteIP, videoFormat,
					recordVideo.toString(), job.getAppName(),job.getApplicationPath());
		}else if (Constants.PYTEST.toString().equalsIgnoreCase(job.getScriptType())) {
			try {
				
				String command = null;
//				if(fileName.startsWith("test_")) {
//					command = "cmd /c pytest "+fileName;
//				}else {
//					command = "cmd /c python "+fileName;
//				}
				command = "cmd /c pytest "+scriptsRootDir+job.getAppName()+File.separator+job.getScriptFileName();
			    Process p = Runtime.getRuntime().exec(command);
			    p.waitFor();
			    BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
			    BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			          String line;
			          String responseObj = null;
			          String passMsg = null;
			          logger.info("----BRI----");
			        while ((line = bri.readLine()) != null) {
			        	logger.info(line);
			        	if(line.contains("passed")) {
			        		passMsg = line;
			        	}
			        	if(responseObj!=null) {
			        		responseObj+="\n";
			        		responseObj+=line;
			        	}else {
			        		responseObj=line;
			        	}
			        


						
			        	if(responseObj.contains("FAILURES")) {
//			        		myObj.addProperty(Constants.STATUS, Constants.FAIL);
//							myObj.addProperty(Constants.MESSAGE, responseObj);
			        		System.out.print("responseObj");
			        		System.out.print(responseObj);
			        		if(responseObj.length()>2999) {
			        			responseObj = responseObj.substring(0, 2999);
			        		}
			    			jobRepository.updateStatusAndReason(STATUS.Failed.toString(), responseObj, job.getId());
			    			botAgentRepository.updateBotAgentLoadBalancerBySno(job.getBotAgent().longValue());
			    			BotAgent botAgent = botAgentRepository.findByAgentSno(job.getBotAgent().longValue());
			    			int numberOfInstances = botAgent.getNumberOfInstances();
			    			if (numberOfInstances > 0) {
			    				botAgentRepository.updateBotAgentNumberOfInstancesBySno(--numberOfInstances,
			    						job.getBotAgent().longValue());
			    			}
			        		System.out.println("failed..."+responseObj);
			        	}else {
			        		
//							myObj.addProperty(Constants.STATUS, Constants.SUCCESS);
//							myObj.addProperty(Constants.MESSAGE, passMsg+"\n\n"+Constants.VALID_SUCCESS);
			        		jobRepository.updateStatusAndReason(STATUS.Success.toString(), null, job.getId());
			        		botAgentRepository.updateBotAgentLoadBalancerBySno(job.getBotAgent().longValue());
			    			BotAgent botAgent = botAgentRepository.findByAgentSno(job.getBotAgent().longValue());
			    			int numberOfInstances = botAgent.getNumberOfInstances();
			    			if (numberOfInstances > 0) {
			    				botAgentRepository.updateBotAgentNumberOfInstancesBySno(--numberOfInstances,
			    						job.getBotAgent().longValue());
			    			}
			        		System.out.println("pass...");
			        	}

			          }
			          bri.close();
			          logger.info("----BRE----");
			          while ((line = bre.readLine()) != null) {
			        	  line+=line;
				        	if(line.contains("compile successfully completed")) {
//				        		myObj.addProperty(Constants.STATUS, Constants.SUCCESS);
//								myObj.addProperty(Constants.MESSAGE, Constants.VALID_SUCCESS);
				        		jobRepository.updateStatusAndReason(STATUS.Success.toString(), null, job.getId());
				        		botAgentRepository.updateBotAgentLoadBalancerBySno(job.getBotAgent().longValue());
				    			BotAgent botAgent = botAgentRepository.findByAgentSno(job.getBotAgent().longValue());
				    			int numberOfInstances = botAgent.getNumberOfInstances();
				    			if (numberOfInstances > 0) {
				    				botAgentRepository.updateBotAgentNumberOfInstancesBySno(--numberOfInstances,
				    						job.getBotAgent().longValue());
				    			}
				        		System.out.println("compile success...");
				        	}else {
				        		System.out.println("error fail...");
//				        		myObj.addProperty(Constants.STATUS, Constants._ERROR);
//								myObj.addProperty(Constants.MESSAGE, line);
				        		if(responseObj.length()>2999) {
				        			responseObj = responseObj.substring(0, 2999);
				        		}
				    			jobRepository.updateStatusAndReason(STATUS.Failed.toString(), responseObj, job.getId());
				        		botAgentRepository.updateBotAgentLoadBalancerBySno(job.getBotAgent().longValue());
				    			BotAgent botAgent = botAgentRepository.findByAgentSno(job.getBotAgent().longValue());
				    			int numberOfInstances = botAgent.getNumberOfInstances();
				    			if (numberOfInstances > 0) {
				    				botAgentRepository.updateBotAgentNumberOfInstancesBySno(--numberOfInstances,
				    						job.getBotAgent().longValue());
				    			}
				        	}
			          }
			          bre.close();
			          p.waitFor();
			          p.destroy();

			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			System.out.print("Console pytest execution.....");
		}

	}

	private JSONObject updateParentFile(String parentKey) {

		JSONObject parentScriptOutputdataList = new JSONObject();
		File parentOutputFile = new File(scriptsRootDir + Constants.GLOBAL_SCRIPT_DATA + File.separator + parentKey
				+ File.separator + parentKey + Constants.DOTTXT);
		logger.info("parentOutputFile " + parentOutputFile.getName());
		logger.info("getAbsolutePath " + parentOutputFile.getAbsolutePath());
		if (parentOutputFile.exists()) {
			try {
				FileReader fr = new FileReader(parentOutputFile);
				BufferedReader br = new BufferedReader(fr);
				StringBuffer sb = new StringBuffer();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line);
					sb.append("\n");
				}
				fr.close();
				logger.info("Contents of File: ");
				logger.info(sb.toString());
				parentScriptOutputdataList = new JSONObject(sb.toString());

				logger.info("input parentScriptOutputdataList  data " + parentScriptOutputdataList);
			} catch (Exception e) {
				logger.error(e.toString());
			}
		}

		return parentScriptOutputdataList;

	}

	private Status runCliScript(Job job, String statusCallBackUrl, String appJobID) throws Exception {

		logger.info("calling the runTest method");
		Status runTestStatus = null;
		if (Constants.OPERATING_SYSTEM_NAME.startsWith(Constants.WINDOWS_OS)) {

			runTestStatus = playBackCli.runTest(job, statusCallBackUrl, appJobID, job.getAppName());

		} else if (Constants.OPERATING_SYSTEM_NAME.startsWith(Constants.MAC_OS)) {

			runTestStatus = macCLIPlayer.runTest(job, statusCallBackUrl, appJobID, job.getAppName());
		}
		logger.info("end runTest method, " + runTestStatus.getReason());
		return runTestStatus;
	}

	private Status loadRunnerClass(Job job, String scriptRootDir, String statusCallBackUrl, String appJobID)
			throws Exception {

		Status loadAndRunClassStatus = null;

		logger.info("calling loadAndRunClass method");

		if (Constants.OPERATING_SYSTEM_NAME.startsWith(Constants.WINDOWS_OS)) {
			loadAndRunClassStatus = ScriptUtil.loadAndRunClass(scriptsRootDir, job.getScriptFileName(),
					job.getContent(), scriptRootDir, statusCallBackUrl, appJobID, job.getAppName());
		} else {
			loadAndRunClassStatus = ScriptUtil.loadAndRunClassForMac(scriptsRootDir, job.getScriptFileName(),
					job.getContent(), scriptRootDir, statusCallBackUrl, appJobID, job.getAppName());
		}
		logger.info("end loadAndRunClass method, " + loadAndRunClassStatus.getReason());

		return loadAndRunClassStatus;

	}

	private void remoteCliScript(Job job, String appJobID, String remoteIP) {

		String recordCallbackUpdateStatus = null;
		Boolean recordVideo = job.getRecordVideo();
		String videoFormat = job.getVideoFormat();

		if (recordVideo == null) {
			recordVideo = false;
		}

		if (job.getParalleExcecutionStatus().equalsIgnoreCase(Constants.ON)) {
			recordVideo = false;
		}

		if (recordVideo) {
			recordCallbackUpdateStatus = cobotURL
					+ com.saviynt.identitybot.constants.Constants.COBOT_STATUS_URLS.PLAY_REMOTE_CLI;
		} else {
			recordCallbackUpdateStatus = cobotURL
					+ com.saviynt.identitybot.constants.Constants.COBOT_STATUS_URLS.PLAY_REMOTE_CLI_WITHOUT_VIDEO;
		}

		if (videoFormat == null) {
			videoFormat = ".mp4";
		}

		remoteAgentService.playCliJob(agentSystemPort,
				com.saviynt.identitybot.constants.Constants.AGENT_URLS.EXECUTE_REMOTE_CLI, recordCallbackUpdateStatus,
				appJobID, job.getContent(), job.getScriptFileName(), remoteIP, videoFormat, recordVideo.toString(),
				job.getAppName());
	}

	private void runWeb(Job job, String appJobID, String remoteIP, boolean readParentScriptOutputFlag,
			JSONObject parentScriptOutputdataList, String scriptRootDir) throws Exception {

		String fileName = job.getScriptFileName().replace(Constants.DOT_JAVA, Constants.DOT_CLASS);
		remoteAgentService.executeRemoteJob(scriptRootDir, agentSystemPort,
				com.saviynt.identitybot.constants.Constants.AGENT_URLS.EXECUTE_REMOTE_WEB, statusCallBackUrl, appJobID,
				job.getContent(), fileName, remoteIP, excludingPattern, job.getSuiteName(), readParentScriptOutputFlag,
				parentScriptOutputdataList, job.getBrowserType());
	}

	private void cliRecordScript(String appJobID, String data, String scriptFileName, String remoteIP, String appName) {

		String recordCallbackUpdateStatus = cobotURL
				+ com.saviynt.identitybot.constants.Constants.COBOT_STATUS_URLS.RECORD_REMOTE_CLI;

		remoteAgentService.recordCliJob(agentSystemPort,
				com.saviynt.identitybot.constants.Constants.AGENT_URLS.RECORD_REMOTE_CLI, recordCallbackUpdateStatus,
				appJobID, data, scriptFileName, remoteIP, appName);
	}

	private void runRemoteDesktop(Job job, String appJobID, String remoteIP) {

		String recordCallbackUpdateStatus = null;
		Boolean recordVideo = job.getRecordVideo();
		String videoFormat = job.getVideoFormat();

		if (recordVideo == null) {
			recordVideo = false;
		}
		if (job.getParalleExcecutionStatus().equalsIgnoreCase(Constants.ON)) {
			recordVideo = false;
		}
		if (recordVideo) {
			recordCallbackUpdateStatus = cobotURL
					+ com.saviynt.identitybot.constants.Constants.COBOT_STATUS_URLS.PLAY_REMOTE_DESKTOP;
		} else {
			recordCallbackUpdateStatus = cobotURL
					+ com.saviynt.identitybot.constants.Constants.COBOT_STATUS_URLS.PLAY_REMOTE_DESKTOP_WITHOUT_VIDEO;
		}

		if (videoFormat == null) {
			videoFormat = ".mp4";
		}

		remoteAgentService.playDesktopJob(agentSystemPort,
				com.saviynt.identitybot.constants.Constants.AGENT_URLS.EXECUTE_REMOTE_DESKTOP,
				recordCallbackUpdateStatus, appJobID, job.getContent(), job.getScriptFileName(), remoteIP, videoFormat,
				recordVideo.toString(), job.getAppName(), job.getApplicationPath());
	}

	private void updateJobStatus(Job job, Status transformScriptFileStatus) {

		logger.info(transformScriptFileStatus.getReason());
		if (job.getBotAgent().longValue() > 0) {
			if (job.getParalleExcecutionStatus().equals(Constants.OFF)) {
				botAgentRepository.updateBotAgentLoadBalancerBySno(job.getBotAgent().longValue());
			}
			BotAgent botAgent = botAgentRepository.findByAgentSno(job.getBotAgent().longValue());
			int numberOfInstances = botAgent.getNumberOfInstances();
			if (numberOfInstances > 0) {
				botAgentRepository.updateBotAgentNumberOfInstancesBySno(--numberOfInstances,
						job.getBotAgent().longValue());
			}
		}
		jobRepository.updateStatusAndReason("Failed", transformScriptFileStatus.getReason(), job.getId());

	}

	private boolean checkInOrSheet(Job job) throws Exception {
		Pattern pattern = Pattern.compile("<<.*?>>");
		String rootDir = CobotApplication.loadCobotProperties().getProperty("scripts.dir");
		String orFile = rootDir + "ObjectRepositoryManagement.xlsx";
		if (Files.exists(Paths.get(orFile))) {
			FileInputStream inputStream = new FileInputStream(new File(orFile));
			try (Workbook workbook = new XSSFWorkbook(inputStream)) {
				Sheet sheet = workbook.getSheet(job.getAppName());
				if (null != sheet) {
					for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
						Row r = sheet.getRow(i);
						String value = r.getCell(2).getStringCellValue();
						if (pattern.matcher(value).find()) {
							return true;
						}
					}
				}
			} catch (Exception e) {
				logger.error("eror: " + e.getMessage());
			}

			inputStream.close();
		}

		return false;
	}

	private boolean checkParameterSytanx(Job job)  {
		try {
		Pattern pattern = Pattern.compile("<<.*?>>");
		String commonFilePath = scriptsRootDir + job.getAppName() + File.separator + job.getScriptFileName();
		List<String> matchedLines = Files.lines(Paths.get(commonFilePath)).filter(s -> pattern.matcher(s).find())
				.collect(Collectors.toList());

		boolean checkCoBotCommonCodeMethod = ScriptUtil.checkCoBotCommonCode(commonFilePath, "CallCustomeMethod");
		if (checkCoBotCommonCodeMethod && matchedLines.isEmpty()) {
			String srcMethodFile = scriptsRootDir + Constants.COMMON_FOLDER + File.separator
					+ Constants.COMMON_METHOD_FILE;
			matchedLines = Files.lines(Paths.get(srcMethodFile)).filter(s -> pattern.matcher(s).find())
					.collect(Collectors.toList());
		}

		boolean checkCoBotCommonCodeScript = ScriptUtil.checkCoBotCommonCode(commonFilePath, "CallTestScript");
		if (checkCoBotCommonCodeScript && matchedLines.isEmpty()) {
			List<String> tetline = Files.lines(Paths.get(commonFilePath)).filter(s -> s.contains("CallTestScript"))
					.collect(Collectors.toList());
			for (String script : tetline) {

				String methodDetails = null;
				Matcher m1 = Pattern.compile("\\(([^)]+)\\)").matcher(script);
				while (m1.find()) {
					methodDetails = m1.group(1);
				}
				String[] methodDetailsArray = methodDetails.split(",");
				if (methodDetailsArray.length ==2) {
					String applicationName = methodDetailsArray[0];
					String scriptName = methodDetailsArray[1];
					String filePath = scriptsRootDir + applicationName + File.separator + scriptName
							+ Constants.DOT_JAVA;
					matchedLines = Files.lines(Paths.get(filePath)).filter(s -> pattern.matcher(s).find())
							.collect(Collectors.toList());
					if (matchedLines.isEmpty()) {
						continue;
					} else {
						break;
					}
				}
			}
		}

		boolean isExist = checkInOrSheet(job);

		if (!matchedLines.isEmpty() || isExist) {

			return true;
		}
		
	} catch (Exception e) {
		
	}

		return false;
	}
}
