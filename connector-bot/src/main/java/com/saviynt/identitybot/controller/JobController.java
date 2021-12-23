package com.saviynt.identitybot.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.saviynt.identitybot.model.*;
import com.saviynt.identitybot.repository.*;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.saviynt.identitybot.Service.JobService;
import com.saviynt.identitybot.Service.SSMAuthService;
import com.saviynt.identitybot.Util.CLIRecorder;
import com.saviynt.identitybot.Util.CliRecorderPopup;
import com.saviynt.identitybot.Util.Constants;
import com.saviynt.identitybot.Util.FileExplorer;
import com.saviynt.identitybot.Util.JobUtil;
import com.saviynt.identitybot.Util.MacCLIRecorder;
import com.saviynt.identitybot.Util.S3Explorer;
import com.saviynt.identitybot.Util.ScriptUtil;
import com.saviynt.identitybot.constants.STATUS;
import com.saviynt.identitybot.mapper.JobUIMapper;

import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

/**
 * @author Srinivasa Reddy Challa, Raj Kumar
 * 
 *         Rest API related submitting Automated script jobs for execution
 * 
 */

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class JobController {

	final static Logger logger = Logger.getLogger(JobController.class);

	@Autowired
	JobRepository jobRepository;

	@Autowired
	SSMConnectionRepository ssmConnectionRepository;

	@Autowired
	SSMAuthService authService;

	@Autowired
	SuiteRepository suiteRepository;
	@Autowired
	BotScriptsRepository botScriptsRepository;

	@Value("${scripts.dir}")
	String scriptsRootDir;

	@Value("${cobot.createconnectionjson.url}")
	String cobotCreateconnectionJSONUrl;

	@Value("${cobot.cliRecordconnectionJSONUrl.url}")
	String cliRecordconnectionJSONUrl;

	@Value("${cobot.desktopRecordconnectionJSONUrl.url}")
	String desktopRecordconnectionJSONUrl;

	@Value("${ssm.username}")
	String ssmUsername;

	@Value("${ssm.password}")
	String ssmPassword;

	@Value("${cobot.scriptType}")
	String cobotScriptType;

	@Value("${testcases.root}")
	String testCaseRoot;

	@Value("${scripts.store}")
	String scriptsStore;

	@Autowired
	private Environment env;

	@Autowired
	BotAgentRepository botAgentRepository;

	@Autowired
	JobUIMapper mapper;

	@Autowired
	CLIRecorder cliRecorder;

	@Autowired
	MacCLIRecorder macCLIRecorder;

	@Autowired
	private S3Explorer s3explorer;

	@Autowired
	private FileExplorer fileExplorer;

	@Autowired
	private SSMAuthService ssmAuthService;

	@Autowired
	private CliRecorderPopup cliRecorderPopup;

	@Autowired
	JobService jobService;

	/**
	 * API to submit job for script execution on remote agent
	 * 
//	 * @param agentIP:     IP address of remote agent
//	 * @param fileName:    Name of the script to be executed
//	 * @param content:Data input for the executing script
	 * @return Response with JObID which can be used to know the status of the
	 *         script execution status
	 */

//	@RequestMapping(value = "/job/agent", method = RequestMethod.POST)
//	public @ResponseBody JobResponse uploadFileHandler(@RequestParam("AgentIP") String agentIP,
//			@RequestParam("File") String fileName, @RequestParam("Body") String content) {
//		logger.info("Entered into upload file handler method");
//		Job job = new Job();
//		job.setStatus(STATUS.Submitted.toString());
//		job.setJobStatusCode(STATUS.Submitted.getID());
//		job.setContent(content);
//		job.setRemoteAgentIP(agentIP);
//		job.setScriptType(cobotScriptType);
//		job.setScriptFileName(fileName);
//		Job saveJob = jobRepository.save(job);
//		JobResponse updateResponse = JobUtil.updateResponse(saveJob, null);
//		return updateResponse;
//	}
	@RequestMapping(value = "/suite/createNewSuite", method = RequestMethod.POST)
	public ResponseEntity<?> handleNewSuite(HttpServletRequest servletRequest,Principal principal) {
		String content = null;
		Suite suiteObj = null;
		try {
			content = IOUtils.toString(servletRequest.getReader());
			JSONObject bodyObj = new JSONObject(content);
			Object suiteNameObj = bodyObj.get("suiteName");
			Object suiteDescObj = bodyObj.get("suiteDesc");
//			Object exeType = bodyObj.get("exeType");
//			Object browserType = bodyObj.get("browserType");
			if (suiteNameObj != null) {
				//to know suitename already there in suites table or not.
				List<Suite> suiteListObj = suiteRepository.getAllSuitesfindBySuiteName((String) suiteNameObj);
			
			if(suiteListObj.size()==0) {
				String suiteName = null;
				Object jobObj = bodyObj.get("job");
				Suite suite = new Suite();
				if (suiteNameObj != null) {
					suiteName = (String) suiteNameObj;
					suite.setSuiteName(suiteName);
				}
				if (suiteDescObj != null) {
					String suiteDesc = (String) suiteDescObj;
					suite.setSuiteDesc(suiteDesc);
				}
				suite.setCreatedBy(principal.getName());
//				if (exeType != null) {
//					Long exeTypeObj = Long.valueOf((String) exeType);
//					suite.setExecutionType(exeTypeObj);
//				}
//				if (browserType != null) {
//					String browserTypeObj = (String) browserType;
//					suite.setBrowserType(browserTypeObj);
//				}
				if (jobObj != null) {
					List<Job> jobList = new ArrayList<Job>();
					JSONArray jobsArray = (JSONArray) jobObj;
					for (int i = 0; i < jobsArray.length(); i++) {
						Object scriptObj = jobsArray.get(i);
						JSONObject jsonObject = (JSONObject) scriptObj;
						Job job = new Job();
						Object jobContentObj = jsonObject.get("content");
						JSONParser parser = new JSONParser();
						net.minidev.json.JSONObject obj = null;
						try {
							//converting json in string form into json object
							obj = (net.minidev.json.JSONObject) parser.parse(jobContentObj.toString());
							Object ipObj = obj.get(Constants.REMOTE_IP);
							// String scriptType = null;
							String remoteIp = null;
							if (ipObj != null) {
								if (String.valueOf(ipObj).length() > 0) {
									remoteIp = ipObj.toString().trim();
								}
							}
							Object scriptTypeObj = obj.get(Constants.SCRIPT_TYPE);
							// String scriptType = null;
							String scriptType = null;
							if (ipObj != null) {
								if (String.valueOf(scriptTypeObj).length() > 0) {
									scriptType = scriptTypeObj.toString().trim();
								}
							}
							
							job.setContent(jobContentObj.toString());
							job.setStatus("transient");
							job.setJobStatusCode(STATUS.Submitted.getID());
							job.setRemoteAgentIP(remoteIp);
							// job.setContent(content);
							job.setScriptType(scriptType);
//							if(obj.get(Constants.FILE_NAME).toString().endsWith(".java")) {
//								job.setScriptType("Web");
//							}
//							if(obj.get(Constants.FILE_NAME).toString().endsWith(".py")) {
//								job.setScriptType("PYWIN");
//							}
//							if(obj.get(Constants.FILE_NAME).toString().endsWith(".txt")) {
//								job.setScriptType("CLI");
//							}
							job.setRetryCount(0);
							job.setRecordVideo(Boolean.valueOf(obj.get(Constants.RECORD_VIDEO).toString()));
							job.setVideoFormat(obj.get(Constants.FORMAT).toString());
							job.setAppName(obj.get(Constants.APPNAME).toString());
							job.setSuiteName(suiteName);
							// job.setHttpparams(obj.get(Constants.HTTP_PARAMS).toString());
							job.setScriptFileName(obj.get(Constants.FILE_NAME).toString());
							if (obj.get(Constants.TASKID) != null) {
								job.setTaskId(obj.get(Constants.TASKID).toString());
							}
							job.setParalleExcecutionStatus(obj.get(Constants.PARALLELEXECUTION_STATUS).toString());
							jobList.add(job);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					suite.setJob(jobList);
				}
				logger.info(suite);
				suiteObj = suiteRepository.save(suite);
			
			}
			}else {
				return new ResponseEntity<String>("Invalid details", HttpStatus.OK);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(suiteObj!=null) {
			return new ResponseEntity<>(suiteObj, HttpStatus.OK);
		}else {
			suiteObj = new Suite();
			return new ResponseEntity<>(suiteObj, HttpStatus.OK);
		}
		
	}

	@Deprecated
	@RequestMapping(value = "/suite/addscripts", method = RequestMethod.POST)
	public ResponseEntity<?> addscripts(HttpServletRequest servletRequest,Principal principal) {
		String content = null;
		Suite suiteObj = null;
		try {
			content = IOUtils.toString(servletRequest.getReader());
			JSONObject bodyObj = new JSONObject(content);
			Object exeType = bodyObj.get("exeType");
			Object browserType = bodyObj.get("browserType");
			Object pExexution = bodyObj.get("pExexution");
			Object id = bodyObj.getLong("id");
			Object jobObj = bodyObj.get("job");
			Suite suite = new Suite();
			if (exeType != null) {
				Long exeTypeObj = Long.valueOf((String) exeType);
				suite.setExecutionType(exeTypeObj);
			}
			if (browserType != null) {
				String browserTypeObj = (String) browserType;
				suite.setBrowserType(browserTypeObj);
			}
			String pExexutionObj = null;
			if (pExexution != null) {
				pExexutionObj = (String) pExexution;
				suite.setParallelExecution(pExexutionObj);
			}
			Long idObj = 0l;
			if (id != null) {
				idObj = (Long) id;
				suite.setId(idObj);
			}
			Suite suiteOneObj = suiteRepository.getOne(suite.getId());
			suite.setSuiteName(suiteOneObj.getSuiteName());
			suite.setSuiteDesc(suiteOneObj.getSuiteDesc());
			suite.setCreatedAt(suiteOneObj.getCreatedAt());
			suite.setCreatedBy(suiteOneObj.getCreatedBy());
			if (jobObj != null) {	
				List<Job> jobList = new ArrayList<Job>();
				JSONArray jobsArray = (JSONArray) jobObj;
				for (int i = 0; i < jobsArray.length(); i++) {
					Object scriptObj = jobsArray.get(i);
					JSONObject jsonObject = (JSONObject) scriptObj;
					Job job = new Job();
					String jobContent = jsonObject.getString("content");
					JSONParser parser = new JSONParser();
					net.minidev.json.JSONObject obj = null;
					try {
						obj = (net.minidev.json.JSONObject) parser.parse(jobContent);
						Object ipObj = obj.get(Constants.REMOTE_IP);
						// String scriptType = null;
						String remoteIp = null;
						if (ipObj != null) {
							if (String.valueOf(ipObj).length() > 0) {
								remoteIp = ipObj.toString().trim();
							}
						}
						Object scriptTypeObj = obj.get(Constants.SCRIPT_TYPE);
						// String scriptType = null;
						String scriptType = null;
						if (ipObj != null) {
							if (String.valueOf(scriptTypeObj).length() > 0) {
								scriptType = scriptTypeObj.toString().trim();
							}
						}
						String browserTypeObj = (String) browserType;
						job.setContent(jobContent);
						job.setStatus("transient");
						job.setJobStatusCode(STATUS.Submitted.getID());
						job.setRemoteAgentIP(remoteIp);
						// job.setContent(content);
						job.setScriptType(scriptType);
//						if(obj.get(Constants.FILE_NAME).toString().endsWith(".java")) {
//							job.setScriptType("Web");
//						}
//						if(obj.get(Constants.FILE_NAME).toString().endsWith(".py")) {
//							job.setScriptType("PYWIN");
//						}
//						if(obj.get(Constants.FILE_NAME).toString().endsWith(".txt")) {
//							job.setScriptType("CLI");
//						}
						job.setBrowserType(browserTypeObj);
						job.setRetryCount(0);
						job.setRecordVideo(Boolean.valueOf(obj.get(Constants.RECORD_VIDEO).toString()));
						job.setVideoFormat(obj.get(Constants.FORMAT).toString());
						job.setAppName(obj.get(Constants.APPNAME).toString());
						job.setSuiteName(suiteOneObj.getSuiteName());
						// job.setHttpparams(obj.get(Constants.HTTP_PARAMS).toString());
						job.setScriptFileName(obj.get(Constants.FILE_NAME).toString());
						if (obj.get(Constants.TASKID) != null) {
							job.setTaskId(obj.get(Constants.TASKID).toString());
						}
						job.setParalleExcecutionStatus(obj.get(Constants.PARALLELEXECUTION_STATUS).toString());
						jobList.add(job);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				suite.setJob(jobList);
			}
			//Deleted all records in jobs table based on suite id
			 jobRepository.deleteJobsBySuiteId(suite.getId());
			 //Update suite details in suite table.Insert script details in jobs table
			suiteRepository.saveAndFlush(suite);
			// suiteRepository.updateSuiteById(suite.getExecutionType(), suite.getBrowserType(), suite.getParallelExecution(), suite.getId());
			 suiteObj = suite;
			 
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(suiteObj!=null) {
			return new ResponseEntity<>(suiteObj, HttpStatus.OK);
		}else {
			return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

    @Deprecated
	@RequestMapping(value = "/suite/updateSuite", method = RequestMethod.POST)
	public ResponseEntity<?> handleUpdateSuite(HttpServletRequest servletRequest) {
		String content = null;
		Suite suiteObj = null;
		try {
			content = IOUtils.toString(servletRequest.getReader());
			JSONObject bodyObj = new JSONObject(content);
			Object exeType = bodyObj.get("exeType");
			Object browserType = bodyObj.get("browserType");
			Object pExexution = bodyObj.get("pExexution");
			Object id = bodyObj.getLong("id");
			Object jobObj = bodyObj.get("job");
			Suite suite = new Suite();
			if (exeType != null) {
				Long exeTypeObj = Long.valueOf((String) exeType);
				suite.setExecutionType(exeTypeObj);
			}
			if (browserType != null) {
				String browserTypeObj = (String) browserType;
				suite.setBrowserType(browserTypeObj);
			}
			String pExexutionObj = null;
			if (pExexution != null) {
				pExexutionObj = (String) pExexution;
				suite.setParallelExecution(pExexutionObj);
			}
			Long idObj = 0l;
			if (id != null) {
				idObj = (Long) id;
				suite.setId(idObj);
			}
			Suite suiteOneObj = suiteRepository.getOne(suite.getId());
			suite.setSuiteName(suiteOneObj.getSuiteName());
			suite.setSuiteDesc(suiteOneObj.getSuiteDesc());
			suite.setCreatedAt(suiteOneObj.getCreatedAt());
			suite.setCreatedBy(suiteOneObj.getCreatedBy());
			if (jobObj != null) {	
				List<Job> jobList = new ArrayList<Job>();
				JSONArray jobsArray = (JSONArray) jobObj;
				for (int i = 0; i < jobsArray.length(); i++) {
					Object scriptObj = jobsArray.get(i);
					JSONObject jsonObject = (JSONObject) scriptObj;
					Job job = new Job();
					String jobContent = jsonObject.getString("content");
					JSONParser parser = new JSONParser();
					net.minidev.json.JSONObject obj = null;
					try {
						obj = (net.minidev.json.JSONObject) parser.parse(jobContent);
						Object ipObj = obj.get(Constants.REMOTE_IP);
						// String scriptType = null;
						String remoteIp = null;
						if (ipObj != null) {
							if (String.valueOf(ipObj).length() > 0) {
								remoteIp = ipObj.toString().trim();
							}
						}
						
						Object scriptTypeObj = obj.get(Constants.SCRIPT_TYPE);
						// String scriptType = null;
						String scriptType = null;
						if (ipObj != null) {
							if (String.valueOf(scriptTypeObj).length() > 0) {
								scriptType = scriptTypeObj.toString().trim();
							}
						}
						
						String browserTypeObj = (String) browserType;
						job.setContent(jobContent);
						job.setStatus("transient");
						job.setJobStatusCode(STATUS.Submitted.getID());
						job.setRemoteAgentIP(remoteIp);
						// job.setContent(content);
						job.setScriptType(scriptType);
//						if(obj.get(Constants.FILE_NAME).toString().endsWith(".java")) {
//							job.setScriptType("Web");
//						}
//						if(obj.get(Constants.FILE_NAME).toString().endsWith(".py")) {
//							job.setScriptType("PYWIN");
//						}
//						if(obj.get(Constants.FILE_NAME).toString().endsWith(".txt")) {
//							job.setScriptType("CLI");
//						}
						job.setBrowserType(browserTypeObj);
						job.setRetryCount(0);
						job.setRecordVideo(Boolean.valueOf(obj.get(Constants.RECORD_VIDEO).toString()));
						job.setVideoFormat(obj.get(Constants.FORMAT).toString());
						job.setAppName(obj.get(Constants.APPNAME).toString());
						job.setSuiteName(suiteOneObj.getSuiteName());
						// job.setHttpparams(obj.get(Constants.HTTP_PARAMS).toString());
						job.setScriptFileName(obj.get(Constants.FILE_NAME).toString());
						if (obj.get(Constants.TASKID) != null) {
							job.setTaskId(obj.get(Constants.TASKID).toString());
						}
						job.setParalleExcecutionStatus(obj.get(Constants.PARALLELEXECUTION_STATUS).toString());
						jobList.add(job);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				suite.setJob(jobList);
			}
			//Deleted all records in jobs table based on suite id
			 jobRepository.deleteJobsBySuiteId(suite.getId());
			 //Update suite details in suite table.Insert script details in jobs table
			suiteRepository.saveAndFlush(suite);
			// suiteRepository.updateSuiteById(suite.getExecutionType(), suite.getBrowserType(), suite.getParallelExecution(), suite.getId());
			Timestamp timestamp = new Timestamp(System.currentTimeMillis()); 
			//Update jobs details in jobs table and changing status to submitted at query level
			jobRepository.updateJobBySuiteId(suiteOneObj.getSuiteName(),pExexutionObj,timestamp,suite.getId());
			 suiteObj = suite;
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(suiteObj!=null) {
			return new ResponseEntity<>(suiteObj, HttpStatus.OK);
		}else {
			return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@RequestMapping(value = "/job/agent", method = RequestMethod.POST)
	public @ResponseBody JobResponse handleAgentJob(@RequestBody String jsonString) {

		logger.info("Processing Web Playback");
		JSONObject obj = new JSONObject(jsonString);
		String agentIP = obj.getString("remoteIp");
		String fileName = obj.getString("filename");
		String cobotScriptType = obj.getString("scriptType");
		String appName = obj.getString("appname");
		boolean recordVideo = obj.getBoolean("recordVideo");
		String videoFormat = obj.getString("format");

		Job job = new Job();
		job.setStatus(STATUS.Submitted.toString());
		job.setJobStatusCode(STATUS.Submitted.getID());
		job.setContent(jsonString);
		job.setRemoteAgentIP(agentIP);
		job.setScriptType(cobotScriptType);
		job.setScriptFileName(fileName);
		job.setAppName(appName);
		job.setRecordVideo(recordVideo);
		job.setVideoFormat(videoFormat);
		Job saveJob = jobRepository.save(job);
		JobResponse updateResponse = JobUtil.updateResponse(saveJob, null);
		return updateResponse;

	}

	/**
	 * API to submit job for executing automated script
	 * 
	 * @param fileName: Name of the script to be executed
	 * @param content:  Data input for the executing script
	 * @return Response with JObID which can be used to know the status of the
	 *         script execution status
	 */

	@RequestMapping(value = "/job/script", method = RequestMethod.POST)
	public @ResponseBody JobResponse executeScript(@RequestParam("File") String fileName,
			@RequestParam("Body") String content) {
		Job job = new Job();
		job.setStatus(STATUS.Submitted.toString());
		job.setJobStatusCode(STATUS.Submitted.getID());
		job.setContent(content);
		job.setScriptFileName(fileName);
		job.setScriptType(cobotScriptType);
		Job saveJob = jobRepository.save(job);
		JobResponse updateResponse = JobUtil.updateResponse(saveJob, null);
		return updateResponse;
	}

	/**
	 * API to update the status of the job
	 * 
	 * @param jobStatus: status for the job to be updated
	 * @return HTTP code for job update
	 */

	@PostMapping("/job/updatestatus")
	public ResponseEntity<?> updateJobStatus(@RequestBody Status jobStatus) {
		jobService.jobUpdateStatus(jobStatus);
		return ResponseEntity.ok().build();
	}

	/**
	 * API to update the status of the job protected by Oauth
	 * 
//	 * @param jobStatus: status for the job to be updated
	 * @return HTTP code for job update
	 */

	@RequestMapping(value = "/job/web/scrapfileplaystatus", method = RequestMethod.POST)
	public ResponseEntity<?> uploadwebstatus(@RequestParam("scrappingFile") MultipartFile scrappingFile,
			@RequestParam("jobId") String jobId, @RequestParam("status") String status,
			@RequestParam("reason") String reason) {

		try {
			Long jobDBId = JobUtil.getDBJobIDFromAppJobID(jobId);
			Job job = jobRepository.findById(jobDBId);
			if (!scrappingFile.isEmpty() && scrappingFile != null) {

				byte[] bytes = scrappingFile.getBytes();

				File dir = new File(scriptsRootDir + job.getAppName());
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(
						dir.getAbsolutePath() + File.separator + scrappingFile.getOriginalFilename());
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				logger.info("saved the video file " + serverFile.getAbsolutePath());
			}
			Status jobStatus = new Status();
			jobStatus.setJobId(jobId);
			jobStatus.setStatus(status);
			jobStatus.setReason(reason);
			jobService.jobUpdateStatus(jobStatus);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().build();
	}

	@PostMapping("/job/updatestatusoauth")
	public ResponseEntity<?> updateJobStatusauth( @RequestParam("reportFile") MultipartFile multipartFile,
			@RequestParam("status") String status, @RequestParam("suitename") String suitename, @RequestParam("appname") String appname, @RequestParam("gloabalScriptVariableUniqueKay") String gloabalScriptVariableUniqueKay, @RequestParam("globalDataList") String globalDataList) {
		try {

			if(globalDataList.trim().length()>0) {
				logger.info("Reading parameters from playback script");
				logger.info("gloabalScriptVariableUniqueKay "+gloabalScriptVariableUniqueKay);
				logger.info("gloabalScriptVariableUniqueKay "+globalDataList);
				if(gloabalScriptVariableUniqueKay.trim().length()>0) {
					JSONObject dataObject=new JSONObject(globalDataList);
					// array to JsonArray
					logger.info("dataObject "+dataObject);
				      File dir = new File(scriptsRootDir + File.separator + Constants.GLOBAL_SCRIPT_DATA +File.separator+  gloabalScriptVariableUniqueKay);
						if (!dir.exists())
							dir.mkdirs();
						

						File serverFile = new File(
								dir.getAbsolutePath() + File.separator + gloabalScriptVariableUniqueKay+Constants.DOTTXT);
						BufferedWriter stream = new BufferedWriter(new FileWriter(serverFile));
						stream.write(dataObject.toString());
						stream.close();
						logger.info("saved gloabalScriptVariableUniqueKay file " + serverFile.getAbsolutePath());
				}	
			}
			JSONObject obj = new JSONObject(status);
			if (!multipartFile.isEmpty() && multipartFile != null) {

				byte[] bytes = multipartFile.getBytes();

				File dir = new File(scriptsRootDir + suitename);
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(
						dir.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename());
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				logger.info("saved the video file " + serverFile.getAbsolutePath());
			}
			Status statusObj = new Status();
			statusObj.setJobId(obj.get("jobId").toString());
			statusObj.setStatus(obj.get("status").toString());
			statusObj.setReason(obj.get("reason").toString());
			jobService.jobUpdateStatus(statusObj);
			return ResponseEntity.ok().build();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	/**
	 * API : convert script to .java file and Creating the connection in SSM
	 * application
	 * 
	 * @param content:        Data input for the executing script
	 * @param search:         Text validation messages
	 * @param headers:        Dynamic input parameters
	 * @param fileName:       Name of the script to be executed
//	 * @param conntype:       Connection type for SSM
	 * @param connectionName: Connection name for SSM
	 * @param agentIp:        Remote agent IP address
	 * @return SSM connection status
	 */

	@RequestMapping(value = "/job/createjson", method = RequestMethod.POST)
	public @ResponseBody String createJson(@RequestParam("content") String content,
			@RequestParam("search") String search, @RequestParam("headers") String headers,
			@RequestParam("fileName") String fileName, @RequestParam("conntype") String connectionType,
			@RequestParam("connectionName") String connectionName, @RequestParam("agentIp") String agentIp) {

		logger.info("Entered into createJson service");
		logger.info("Request parameter values");
		logger.info("Content = " + content);
		logger.info("Search = " + search);
		logger.info("headers = " + headers);
		logger.info("fileName = " + fileName);
		logger.info("connectionType = " + connectionType);
		logger.info("connectionName = " + connectionName);
		logger.info("agentIp = " + agentIp);

		String scriptsDir = scriptsRootDir;
		logger.info("scriptsRootDir = " + scriptsDir);

		logger.info("Creating file " + fileName + " in " + scriptsDir);
		File directory = new File(scriptsDir + connectionName);
		File file = new File(scriptsDir + connectionName + File.separator + fileName);
		// Create the file
		try {
			if (!directory.exists()) {
				directory.mkdir();
			}
			if (file.createNewFile()) {
				logger.info("File is created in " + file.getAbsolutePath());
			} else {
				logger.info("File already exists.");
			}
			logger.info("writing content into above created file");
			// Write Content
			FileWriter writer = new FileWriter(file);
			writer.write(content);
			writer.close();

			logger.info("Converting header parameter into JsonObject");
			JsonParser parser = new JsonParser();
			JsonObject jsonObjHeader = null;
			if (headers.trim().trim().length() > 0) {
				jsonObjHeader = parser.parse(headers).getAsJsonObject();
			}
			logger.info("Converted header parameter into JsonObject = " + jsonObjHeader);

			logger.info("Converting search parameter into JsonObject");
			JsonObject SearchObject = null;
			if (search.trim().trim().length() > 0) {
				SearchObject = parser.parse(search).getAsJsonObject();
			}
			logger.info("Converted search parameter into JsonObject = " + SearchObject);

			logger.info("Converting above created search JsonObject into JSONArray");
			JSONArray jsonSearchArrayData = new JSONArray();
			if (SearchObject != null) {
				for (Entry<String, JsonElement> entry : SearchObject.entrySet()) {
					String key = entry.getKey();
					jsonSearchArrayData.put(key);
				}
			}
			logger.info("Converted above created search JsonObject into JSONArray = " + jsonSearchArrayData.toString());

			logger.info("Spliting above created header JsonObject into JSONArray");
			JSONArray jsonArrayData = new JSONArray();
			JSONArray jsonArraykeys = new JSONArray();
			if (jsonObjHeader != null) {
				for (Entry<String, JsonElement> entry : jsonObjHeader.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue().getAsString();
					jsonArrayData.put(value);
					jsonArraykeys.put(key);
				}
			}
			logger.info("Splited above created header JsonObject into JSONArray");
			logger.info("jsonArrayData = " + jsonArrayData);
			logger.info("jsonArraykeys = " + jsonArraykeys);

			JSONObject connectionJson = new JSONObject();
			JSONObject responseColsToPropsMap = new JSONObject();
			JSONArray call = new JSONArray();

			JSONObject callObj = new JSONObject();
			JSONObject httpParams = new JSONObject();
			JSONObject httpHeaders = new JSONObject();

			logger.info("Adding key, values to connectionJson");
			connectionJson.put(Constants.ACCOUNT_ID_PATH, Constants.SSM_TASK_ID);
			connectionJson.put(Constants.DATE_FORMAT, "");
			connectionJson.put(Constants.RESPONSE_COLS_TO_PROPS_MAP, responseColsToPropsMap);
			logger.info("Added values to connectionJson = " + connectionJson.toString());

			logger.info("Adding key, values to callObj");
			callObj.put(Constants.NAME, connectionName);
			callObj.put(Constants.CONNECTION, connectionName);
			callObj.put(Constants.URL, cobotCreateconnectionJSONUrl);
			callObj.put(Constants.HTTP_METHOD, Constants.POST);
			callObj.put(Constants.HTTP_CONTENT_TYPE, Constants.APP_SLASH_JSON);
			logger.info("Added key, values to callObj = " + callObj.toString());

			logger.info("Adding key, values to httpParams");
			httpParams.put(Constants.FILE_NAME, fileName);
			httpParams.put(Constants.DATA, jsonArrayData);
			httpParams.put(Constants.SEARCH_DATA, jsonSearchArrayData);
			httpParams.put(Constants.SCRIPT_TYPE, cobotScriptType);
			httpParams.put(Constants.HTTP_PARAMS, Constants.EMPTY_ARRAY);
			httpParams.put(Constants.REMOTE_IP, agentIp);
			httpParams.put(Constants.PARALLELEXECUTION_STATUS, Constants.OFF);
			httpParams.put(Constants.RECORD_VIDEO, false);
			httpParams.put(Constants.FORMAT, Constants.DOTMP4);
			httpParams.put(Constants.APPNAME, connectionName);
			httpParams.put(Constants.TASKID, Constants.SSM_VAR_TASKID);
			logger.info("Added key, values to httpParams = " + httpParams.toString());

			logger.info("Adding key, values to httpHeaders");
			httpHeaders.put(Constants.AUTHORIZATION, Constants.BASIC_AUTHORIZATION);
			logger.info("Added key, values to httpHeaders = " + httpHeaders);

			logger.info("Adding httpParams, httpHeaders to callObj");
			callObj.put(Constants.HTTPPARAMS, httpParams);
			callObj.put(Constants.HTTP_HEADERS, httpHeaders);
			logger.info("Added httpParams, httpHeaders to callObj = " + callObj.toString());

			logger.info("Adding callObj to call");
			call.put(callObj);
			// logger.info("Added callObj to call = "+call.toString());

			logger.info("Adding call object to connectionJson");
			connectionJson.put(Constants.CALL, call);
			logger.info("Added call object to connectionJson = " + connectionJson.toString());

			// logger.info("getting token from SSM by using user = "+ssmUsername +" and
			// Password = "+ssmPassword);
			Status tokenResponse = authService.getAuthToken(ssmUsername, ssmPassword);
			// logger.info("tokenResponse = "+tokenResponse.toString());
			// TOKEN IS NOT NULL AND NOT EMPTY THEN ALLOW THE CONDITION
			if (tokenResponse != null
					&& tokenResponse.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
				String token = tokenResponse.getStatus();
				// logger.info("token = "+token);
				if (!token.isEmpty()) {
					JSONObject jsonObject = authService.createConnectionJSON(connectionJson, token, connectionName,
							connectionType);
					logger.info("jsonObject = " + jsonObject);
					if (jsonObject != null) {
						logger.info("Connection successfully created in SSM");
						return jsonObject.getString(Constants.MSG);
					} else {
						logger.error("Unable to create SSM connection");
						return Constants.ERROR_SSM_CONNECTION;
					}
				} else {
					logger.info("SSM Token is not generated. ");
					return Constants.TOKEN_ERROR_MSG;
				}
			} else {
				logger.info("SSM Token is not generated.");
				return tokenResponse.getReason();
			}
		} catch (IOException e) {
			return Constants.ERROR;
		}

	}

	@PostMapping("/job")
	public JobResponse createJob(HttpServletRequest servletRequest) throws IOException {
		logger.info("Enter job service.");
		String content = IOUtils.toString(servletRequest.getReader());
		logger.info("content = " + content);

		Job job = new Job();
		job.setStatus(STATUS.Submitted.toString());
		job.setJobStatusCode(STATUS.Submitted.getID());
		job.setContent(content);
		JSONObject contentObj = new JSONObject(content);
		if (contentObj.getString(Constants.TASKID) != null) {
			job.setTaskId(contentObj.getString(Constants.TASKID).toString());
		}
		logger.info("saving job data into table");
		Job saveJob = jobRepository.save(job);
		logger.info("Saved job data into table");
		logger.info("saveJob = " + saveJob);

		logger.info("preparing JobResponse from saveJob object ");
		JobResponse updateResponse = JobUtil.updateResponse(saveJob, null);
		logger.info("updateResponse = " + updateResponse);
		return updateResponse;
	}

	@PostMapping("/job/createjob")
	@ResponseBody
	public ResponseEntity<?> connectionjson(HttpServletRequest servletRequest) throws IOException {
		logger.info("Enter into createjob service");
		String content = IOUtils.toString(servletRequest.getReader());
		logger.info("content = " + content);

		logger.info("preparing json object and assiging content, remote ip and script type");
		JSONObject obj = new JSONObject(content);
		Object ipObj = obj.get(Constants.REMOTE_IP);
		Object scriptObj = obj.get(Constants.SCRIPT_TYPE);
		String scriptType = null;
		String remoteIp = null;
		if (ipObj != null) {
			if (String.valueOf(ipObj).length() > 0) {
				remoteIp = ipObj.toString().trim();
			}
		}

		logger.info("remoteIp = " + remoteIp);
		if (scriptObj != null) {
			if (String.valueOf(scriptObj).length() > 0) {
				scriptType = scriptObj.toString().trim();
			}
		}
		logger.info("scriptType = " + scriptType);

		logger.info("preparing job object");
		Job job = new Job();
		job.setStatus(STATUS.Submitted.toString());
		job.setJobStatusCode(STATUS.Submitted.getID());
		job.setRemoteAgentIP(remoteIp);
		job.setContent(content);
		job.setScriptType(scriptType);
		job.setRetryCount(0);
		job.setRecordVideo(Boolean.valueOf(obj.get(Constants.RECORD_VIDEO).toString()));
		job.setVideoFormat(obj.get(Constants.FORMAT).toString());
		job.setAppName(obj.get(Constants.APPNAME).toString());
		// job.setHttpparams(obj.get(Constants.HTTP_PARAMS).toString());
		job.setScriptFileName(obj.get(Constants.FILE_NAME).toString());
		if (obj.getString(Constants.TASKID) != null) {
			job.setTaskId(obj.getString(Constants.TASKID).toString());
		}
		job.setParalleExcecutionStatus(obj.getString(Constants.PARALLELEXECUTION_STATUS).toString());
		logger.info("job = " + job);

		logger.info("saving job object");

		SSMConnections connectionsObj = ssmConnectionRepository.findByScriptFileNameAndConnectionNameAndScriptType(
				obj.getString(Constants.FILE_NAME), obj.getString(Constants.APPNAME).toString(), Constants.WEB);
		if (connectionsObj != null) {
			ssmConnectionRepository.updateRemoteIpAndExecutionStatusByConnectionNameAndScriptName(
					Boolean.valueOf(obj.get(Constants.RECORD_VIDEO).toString()), obj.get(Constants.FORMAT).toString(),
					remoteIp, obj.getString(Constants.PARALLELEXECUTION_STATUS).toString(),
					obj.getString(Constants.APPNAME).toString(), obj.getString(Constants.FILE_NAME),
					connectionsObj.getId());
		}

		Job saveJob = jobRepository.save(job);
		logger.info("saved job data into table ");

		logger.info("preparing jobresponse from savejob object");
		JobResponse updateResponse = JobUtil.updateResponse(saveJob, null);
		logger.info("updateResponse = " + updateResponse);
		return new ResponseEntity<>(updateResponse, HttpStatus.FAILED_DEPENDENCY);
	}

	@PostMapping("/content")
	public void contentCompile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("content= " + request.getParameter(Constants.FILE_CONTENT));
		logger.info("filename =" + request.getParameter(Constants.FILENAME));

		JsonObject myObj = new JsonObject();
		response.setContentType(Constants.APP_SLASH_JSON);

		logger.info("creating temp file");
		String fileName = fileExplorer.createTempFile(request.getParameter(Constants.FILE_CONTENT),
				request.getParameter(Constants.FILENAME));
		logger.info("TempFile =" + fileName);
		if (fileName != null && !fileName.isEmpty()) {
			String executeMode = env.getProperty(Constants.EXECUTE_DOT_MODE);
			logger.info("Execution Mode " + executeMode);
			logger.info("compiling the above created file");
			String error = "";
			if(fileName.endsWith(Constants.DOT_JAVA)) {
				error = ScriptUtil.compileScriptFileContent(fileName, executeMode);
			}else if(fileName.endsWith(Constants.DOT_PY)){
				try {
					
					String command = null;
//					if(fileName.startsWith("test_")) {
//						command = "cmd /c pytest "+fileName;
//					}else {
//						command = "cmd /c python "+fileName;
//					}
					command = "cmd /c pytest "+fileName;
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
				        		myObj.addProperty(Constants.STATUS, Constants.FAIL);
								myObj.addProperty(Constants.MESSAGE, responseObj);
				        	}else {
				        		
								myObj.addProperty(Constants.STATUS, Constants.SUCCESS);
								myObj.addProperty(Constants.MESSAGE, passMsg+"\n\n"+Constants.VALID_SUCCESS);
				        	}

				          }
				          bri.close();
				          logger.info("----BRE----");
				          while ((line = bre.readLine()) != null) {
				        	  line+=line;
					        	if(line.contains("compile successfully completed")) {
					        		myObj.addProperty(Constants.STATUS, Constants.SUCCESS);
									myObj.addProperty(Constants.MESSAGE, Constants.VALID_SUCCESS);
					        	}else {
					        		myObj.addProperty(Constants.STATUS, Constants._ERROR);
									myObj.addProperty(Constants.MESSAGE, line);
					        	}
				          }
				          bre.close();
				          p.waitFor();
				          p.destroy();

				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			
			logger.info("compile response = " + error);

			logger.info("preparing myobj based on above response");
			if(fileName.endsWith(Constants.DOT_JAVA)) {
				if (error.isEmpty()) {
					myObj.addProperty(Constants.STATUS, Constants.SUCCESS);
					myObj.addProperty(Constants.MESSAGE, Constants.VALID_SUCCESS);
				} else {
					error = error.replace(fileName, request.getParameter(Constants.FILENAME));
					String className = fileName.split("\\\\")[fileName.split("\\\\").length - 1].replace(".java", "");
					error = error.replace(className, request.getParameter(Constants.FILENAME).replace(".java", ""));
					logger.info("className= " + className);
					myObj.addProperty(Constants.STATUS, Constants._ERROR);
					myObj.addProperty(Constants.MESSAGE, error);
				}
			}
			
		} else {
			myObj.addProperty(Constants.STATUS, Constants._ERROR);
			myObj.addProperty(Constants.MESSAGE, Constants.FILE_CREATION_ERROR);
		}
		logger.info("myObj = " + myObj);
		PrintWriter writer = response.getWriter();
		writer.write(myObj.toString());
		writer.close();
	}

	@PostMapping("/contentSaving")
	public void contentSaving(HttpServletRequest request, HttpServletResponse response) throws Exception {

		logger.info("content " + request.getParameter(Constants.FILE_CONTENT));
		logger.info("filename =" + request.getParameter(Constants.FILENAME));

		logger.info("job = " + request.getParameter(Constants.FILENAME));
		String fileNameWithExt = request.getParameter(Constants.FILENAME);

		String rootDir = request.getParameter(Constants.ROOTDIR);
		logger.info("Root Dir = " + request.getParameter(Constants.ROOTDIR));

		String scriptType = request.getParameter(Constants.SCRIPT_TYPE);
		logger.info("script Type = " + scriptType);
		String path = null;
		String status = null;
		if (scriptType != null
				&& (scriptType.equalsIgnoreCase(Constants.CLI) || scriptType.equalsIgnoreCase(Constants.DESKTOP))) {
			String content = request.getParameter(Constants.FILE_CONTENT);

			JSONObject finalCliParams = jobService.findFinalParams(content);
			/* if(!finalCliParams.isEmpty()) { */
			try {
				String[] fileNameArry = fileNameWithExt.split("\\.");
				String fileName = fileNameArry[0];

				if (scriptType.equalsIgnoreCase(Constants.DESKTOP)) {
					path = scriptsRootDir + rootDir;
				} else if (scriptType.equalsIgnoreCase(Constants.CLI)) {
					path = scriptsRootDir + rootDir + File.separator + Constants.RECORDED_SCRIPTS_PATH;
				}
				status = jobService.saveFile(path, request.getParameter(Constants.FILENAME),
						request.getParameter(Constants.FILE_CONTENT));
//				logger.info("fetching connection by fileName = " + fileName);
//				SSMConnections connections = ssmConnectionRepository
//						.findByScriptFileNameAndConnectionNameAndScriptType(fileName, rootDir, scriptType);
//				logger.info("cononections = " + connections);
//
//				JSONObject connectionJson = new JSONObject();
//				JSONObject responseColsToPropsMap = new JSONObject();
//				JSONArray call = new JSONArray();
//
//				JSONObject callObj = new JSONObject();
//				JSONObject httpParams = new JSONObject();
//				JSONObject httpHeaders = new JSONObject();
//
//				logger.info("Adding key, values to connectionJson");
//				connectionJson.put(Constants.ACCOUNT_ID_PATH, Constants.SSM_TASK_ID);
//				connectionJson.put(Constants.DATE_FORMAT, "");
//				connectionJson.put(Constants.RESPONSE_COLS_TO_PROPS_MAP, responseColsToPropsMap);
//				logger.info("Added key, values to connectionJson = " + connectionJson);
//
//				logger.info("Adding key, values to callObj");
//				callObj.put(Constants.NAME, connections.getConnectionName());
//				callObj.put(Constants.CONNECTION, connections.getConnectionName());
//
//				callObj.put(Constants.HTTP_METHOD, Constants.POST);
//				callObj.put(Constants.HTTP_CONTENT_TYPE, Constants.APP_SLASH_JSON);
//				logger.info("Added key, values to callObj = " + callObj);
//
//				logger.info("Adding key, values to httpParams");
//				httpParams.put(Constants.FILE, fileName);
//				httpParams.put(Constants.RECORD_VIDEO, connections.getRecordVideo());
//				httpParams.put(Constants.FORMAT, connections.getVideoFormat());
//				if (scriptType.equalsIgnoreCase(Constants.CLI)) {
//					httpParams.put(Constants.CLI_PARAMS, finalCliParams.isEmpty() ? Constants.EMPTY : finalCliParams);
//					callObj.put(Constants.URL, cliRecordconnectionJSONUrl);
//				} else if (scriptType.equalsIgnoreCase(Constants.DESKTOP)) {
//					httpParams.put(Constants.DESKTOP_PARAMS,
//							finalCliParams.isEmpty() ? Constants.EMPTY : finalCliParams);
//					callObj.put(Constants.URL, desktopRecordconnectionJSONUrl);
//				}
//				httpParams.put(Constants.REMOTE_IP, connections.getRemoteIp());
//				httpParams.put(Constants.PARALLELEXECUTION_STATUS, connections.getParalleExcecutionStatus());
//				httpParams.put(Constants.APPNAME, rootDir);
//				httpParams.put(Constants.TASKID, Constants.SSM_VAR_TASKID);
//				logger.info("Added key, values to httpParams = " + httpParams);
//
//				logger.info("Adding key, values to httpHeaders");
//				httpHeaders.put(Constants.AUTHORIZATION, Constants.BASIC_AUTHORIZATION);
//				logger.info("Added key, values to httpHeaders = " + httpHeaders);
//
//				logger.info("Adding httpParams, httpHeaders to callObj");
//				callObj.put(Constants.HTTPPARAMS, httpParams);
//				callObj.put(Constants.HTTP_HEADERS, httpHeaders);
//				// logger.info("Adding httpParams, httpHeaders to callObj = "+callObj);
//
//				logger.info("Adding callObj to call");
//				call.put(callObj);
//				// logger.info("Added callObj to call = "+call);
//
//				logger.info("Adding call to connectionJson");
//				connectionJson.put(Constants.CALL, call);
//				logger.info("Added call to connectionJson = " + connectionJson);
//
//				// logger.info("getting token from SSM by using user = "+ssmUsername +" and
//				// Password = "+ssmPassword);
//				Status tokenResponse = authService.getAuthToken(ssmUsername, ssmPassword);
//				// logger.info("tokenResponse = "+tokenResponse.toString());
//				// TOKEN IS NOT NULL AND NOT EMPTY THEN ALLOW THE CONDITION
//				if (tokenResponse != null
//						&& tokenResponse.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
//					String token = tokenResponse.getStatus();
//					// logger.info("token = " + token);
//					if (!token.isEmpty()) {
//						JSONObject jsonObject = authService.createConnectionJSON(connectionJson, token,
//								connections.getConnectionName(), connections.getConnectionType());
//						logger.info("jsonObject = " + jsonObject);
//						if (jsonObject != null) {
//							logger.info(jsonObject.getString(Constants.MSG).toString());
//							if (scriptType.equalsIgnoreCase(Constants.DESKTOP)) {
//								path = scriptsRootDir + rootDir;
//							} else if (scriptType.equalsIgnoreCase(Constants.CLI)) {
//								path = scriptsRootDir + rootDir + File.separator + Constants.RECORDED_SCRIPTS_PATH;
//							}
//							status = jobService.saveFile(path, request.getParameter(Constants.FILENAME),
//									request.getParameter(Constants.FILE_CONTENT));
//						} else {
//							status = "Unable to update connection";
//							logger.error("Unable to update connection");
//						}
//					} else {
//						status = "SSM Token is not generated.";
//						logger.info("SSM Token is not generated. ");
//					}
//				} else {
//					status = "Unable to connect SSM";
//					logger.info("SSM Token is not generated.");
//				}
				
			} catch (Exception e) {
				status = "Unable to update connection";
				logger.error("Unable to update connection");
			}
			/*
			 * }else { if(scriptType.equalsIgnoreCase(Constants.DESKTOP)) { path =
			 * scriptsRootDir + rootDir; }else
			 * if(scriptType.equalsIgnoreCase(Constants.CLI)) { path = scriptsRootDir +
			 * rootDir + File.separator + Constants.RECORDED_SCRIPTS_PATH ; } status =
			 * jobService.saveFile(path, request.getParameter(Constants.FILENAME),
			 * request.getParameter(Constants.FILE_CONTENT)); }
			 */
		} else {
			path = scriptsRootDir + rootDir;
			status = jobService.saveFile(path, request.getParameter(Constants.FILENAME),
					request.getParameter(Constants.FILE_CONTENT));
		}
		logger.info("path = " + path);
		JsonObject myObj = new JsonObject();
		response.setContentType(Constants.APP_SLASH_JSON);

		if (status != null && status.isEmpty()) {
			myObj.addProperty(Constants.STATUS, Constants.SUCCESS);
			myObj.addProperty(Constants.MESSAGE, Constants.FILE_SAVE_SUCCESS);
		} else {
			myObj.addProperty(Constants.STATUS, Constants.ERROR);
			myObj.addProperty(Constants.MESSAGE, status);
		}

		PrintWriter writer = response.getWriter();
		writer.write(myObj.toString());
		writer.close();
	}

	@PostMapping("/saveNewApplication")
	public void saveNewScript(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject myObj = new JSONObject();
		try {
			String fileNameWithExt = request.getParameter(Constants.FILENAME);
			String appName = request.getParameter(Constants.AppName);
	        String url=request.getParameter(Constants.URL);
	        String typeOfTest=request.getParameter(Constants.TYPE_OF_TEST);
	        String moduleName=request.getParameter(Constants.MODULE_NAME);
	        if(typeOfTest.equals(Constants.MODULE_WEB) || typeOfTest.equals(Constants.MODULE_API_JAVA) ) {
	        	if(!fileNameWithExt.endsWith(".java")) {
		        	fileNameWithExt += ".java";
		        }
	        }
	        if(typeOfTest.equals(Constants.MODULE_DESKTOP) || typeOfTest.equals(Constants.MODULE_API_PYTHON)) {
	        	fileNameWithExt = "test_"+fileNameWithExt;
	        	if(!fileNameWithExt.endsWith(".py")) {
		        	fileNameWithExt += ".py";
		        }
	        }
	        if(typeOfTest.equals(Constants.MODULE_CLI)) {
	        	if(!fileNameWithExt.endsWith(".txt")) {
		        	fileNameWithExt += ".txt";
		        }
	        }
			String path = scriptsRootDir ;
			boolean status= fileExplorer.createAppDirAndSaveScript(path,appName,
					fileNameWithExt);
			
			 if (status) {
				myObj.put(Constants.STATUS, Constants.SUCCESS);
				myObj.put(Constants.MESSAGE, Constants.FILE_SAVE_SUCCESS);
			} else {
				myObj.put(Constants.STATUS, Constants.ERROR);
				myObj.put(Constants.MESSAGE, "Script name already exist.");
			}
		   if(status) {
			 JSONObject httpPayload = new JSONObject();
			 JSONArray array=new  JSONArray();
			 httpPayload.put("filename", fileNameWithExt);
			 httpPayload.put("remoteIp", "");
			 httpPayload.put("data",array);
			 httpPayload.put("appname", appName);
			 httpPayload.put("scriptType", typeOfTest);
			 httpPayload.put("recordVideo", false);
			 httpPayload.put("format", ".mp4");
			 httpPayload.put("httpparams", array);
			 httpPayload.put("searchdata", array);
			 httpPayload.put("parallelexecution", "off");
			 httpPayload.put("taskid", "0");
			 //saving bot script to db
			BotScripts botScripts = new BotScripts();
			botScripts.setAppName(appName);
			botScripts.setHttp_payload(httpPayload.toString());
			botScripts.setScriptFileName(fileNameWithExt);
			botScripts.setScriptType(typeOfTest);
			botScripts.setTypeOfTest(typeOfTest);
			botScripts.setModuleName(moduleName);
			botScripts.setUrl(url);
			botScripts.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
			botScripts.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
			botScripts.setCreatedAt(new Date());
			botScripts.setUpdatedAt(new Date());
			botScriptsRepository.save(botScripts);
		   }
		}catch (Exception e) {
			// TODO: handle exception
			myObj.put(Constants.STATUS, Constants.ERROR);
			myObj.put(Constants.MESSAGE, e.getMessage());
		}

		PrintWriter writer = response.getWriter();
		writer.write(myObj.toString());
		writer.close();
	}


	@PostMapping("/script/getFile")
	public void findScriptContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Fetching Bot agents data..");

		String scriptType = request.getParameter(Constants.SCRIPT_TYPE);
		logger.info("script Type = " + scriptType);

		String fileName = request.getParameter(Constants.FILENAME);
		logger.info("File is = " + request.getParameter(Constants.FILENAME));

		String rootDir = request.getParameter(Constants.ROOTDIR);
		logger.info("Root Dir = " + request.getParameter(Constants.ROOTDIR));

		String path = null;
		String fileContent = null;
		if (scriptsStore != null && scriptsStore.equalsIgnoreCase(Constants.SCRIPT_STORE_LOCAL)) {
			if (scriptType.equalsIgnoreCase(Constants.CLI)) {
				path = scriptsRootDir + rootDir + File.separator + Constants.RECORDED_SCRIPTS_PATH;
			} else {
				path = scriptsRootDir + rootDir;
			}
			logger.info("path = " + path);
			fileContent = fileExplorer.readContent(path, fileName);
		} else if (scriptsStore != null && scriptsStore.equalsIgnoreCase(Constants.S3_BUCKET)) {
			fileContent = s3explorer.readFileAsString(path, fileName);
		}
		logger.info("content = " + fileContent);

		JsonObject myObj = new JsonObject();
		response.setContentType(Constants.APP_SLASH_JSON);

		if (fileContent != null) {
			myObj.addProperty(Constants.STATUS, Constants.SUCCESS);
			myObj.addProperty(Constants.DATA, fileContent);
		} else {
			myObj.addProperty(Constants.STATUS, Constants._ERROR);
			myObj.addProperty(Constants.DATA, Constants.DATA_NOT_FOUNT);
		}

		PrintWriter writer = response.getWriter();
		writer.write(myObj.toString());
		writer.close();

	}

	@GetMapping("/botAgents")
	public void refreshBotAgents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Fetching Bot agents data..");
		List<BotAgent> botAgentsList = botAgentRepository.findAllByTimestampDesc();
		JsonArray botAgentsJson = new JsonArray();

		logger.info("converting botAgentsList to JsonArray");
		if (botAgentsList != null && botAgentsList.size() > 0) {
			botAgentsJson = mapper.updateStatusJson(botAgentsList);
		}
		logger.info("converted botAgentsList to JsonArray");

		logger.info("preparing myObj for response");
		JsonObject myObj = new JsonObject();
		response.setContentType(Constants.APP_SLASH_JSON);

		if (botAgentsJson != null) {
			myObj.addProperty(Constants.STATUS, Constants.SUCCESS);
			myObj.addProperty(Constants.DATA, botAgentsJson.toString());
		} else {
			myObj.addProperty(Constants.STATUS, Constants._ERROR);
			myObj.addProperty(Constants.DATA, Constants.DATA_NOT_FOUNT);
		}
		logger.info("myObj = " + myObj);
		PrintWriter writer = response.getWriter();
		writer.write(myObj.toString());
		writer.close();
	}
	
	@GetMapping("/updateAgent")
	public void updateAgent(@RequestParam String agentId, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.info("Updated Bot agents data..");

		logger.info("preparing myObj for response");
		JsonObject myObj = new JsonObject();
		response.setContentType(Constants.APP_SLASH_JSON);
		botAgentRepository.updateBotAgentLoadBalancerFlagByAgentId(0, 0, agentId);
		botAgentRepository.updateJobStatus();
		myObj.addProperty(Constants.STATUS, Constants.SUCCESS);
		myObj.addProperty(Constants.DATA, "Updated sucessfully");

		logger.info("myObj = " + myObj);
		PrintWriter writer = response.getWriter();
		writer.write(myObj.toString());
		writer.close();
	}

	@RequestMapping(value = "/checkSSMConnection", method = RequestMethod.GET)
	public @ResponseBody String checkSSMConnection() {
//		//logger.info("getting token from SSM by using user = "+ssmUsername +" and Password = "+ssmPassword);
//		Status tokenResponse = authService.getAuthToken(ssmUsername, ssmPassword);
//		//logger.info("tokenResponse = "+tokenResponse.toString());
//		// TOKEN IS NOT NULL AND NOT EMPTY THEN ALLOW THE CONDITION
//		if (tokenResponse != null && tokenResponse.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {  
//			String token = tokenResponse.getStatus();
//			//logger.info("token = "+token);
//			return token != null ? Constants.SUCCESS : Constants.FAIL;
//		}else {
//			return tokenResponse.getReason();
//		}
		return "success";

	}

	@RequestMapping(value = "/cli/ssm", method = RequestMethod.POST)
	public @ResponseBody CLIResponse ssmScript(@RequestParam("File") String fileName,
			@RequestParam("recordVideo") Boolean recordVideo, @RequestParam("format") String format,
			@RequestParam("connectionName") String connectionName, @RequestParam("appName") String appName) {
		CLIResponse response = new CLIResponse();
		try {

			JSONObject connectionJson = new JSONObject();
			JSONObject responseColsToPropsMap = new JSONObject();
			JSONArray call = new JSONArray();

			JSONObject callObj = new JSONObject();
			JSONObject httpParams = new JSONObject();
			JSONObject httpHeaders = new JSONObject();

			connectionJson.put(Constants.ACCOUNT_ID_PATH, Constants.SSM_TASK_ID);
			connectionJson.put(Constants.DATE_FORMAT, "");
			connectionJson.put(Constants.RESPONSE_COLS_TO_PROPS_MAP, responseColsToPropsMap);

			callObj.put(Constants.NAME, appName);
			callObj.put(Constants.CONNECTION, appName);
			callObj.put(Constants.URL, cliRecordconnectionJSONUrl);
			callObj.put(Constants.HTTP_METHOD, Constants.POST);
			callObj.put(Constants.HTTP_CONTENT_TYPE, Constants.APP_SLASH_JSON);

			httpParams.put(Constants.FILE, fileName);
			httpParams.put(Constants.RECORD_VIDEO, recordVideo);
			httpParams.put(Constants.FORMAT, format);
			httpParams.put(Constants.CLI_PARAMS, Constants.EMPTY);
			httpParams.put(Constants.REMOTE_IP, Constants.EMPTY);
			httpParams.put(Constants.PARALLELEXECUTION_STATUS, Constants.OFF);
			httpParams.put(Constants.APPNAME, appName);
			httpParams.put(Constants.TASKID, Constants.SSM_VAR_TASKID);

			httpHeaders.put(Constants.AUTHORIZATION, Constants.BASIC_AUTHORIZATION);

			callObj.put(Constants.HTTPPARAMS, httpParams);
			callObj.put(Constants.HTTP_HEADERS, httpHeaders);

			call.put(callObj);

			connectionJson.put(Constants.CALL, call);

			SSMConnections connections = new SSMConnections();
			connections.setScriptFileName(fileName);
			connections.setRecordVideo(recordVideo);
			connections.setVideoFormat(format);
			connections.setConnectionName(appName);
			connections.setConnectionType(connectionName);
			connections.setRemoteIp(Constants.EMPTY);
			connections.setParalleExcecutionStatus(Constants.OFF);
			connections.setScriptType(Constants.CLI);
			SSMConnections connectionsObj = ssmConnectionRepository
					.findByScriptFileNameAndConnectionNameAndScriptType(fileName, appName, Constants.CLI);
			if (connectionsObj != null) {
				ssmConnectionRepository.updateRemoteIpAndExecutionStatusByConnectionNameAndScriptName(recordVideo,
						format, Constants.EMPTY, Constants.OFF, appName, fileName, connectionsObj.getId());
			} else {
				ssmConnectionRepository.save(connections);
			}

			// logger.info("getting token from SSM by using user = "+ssmUsername +" and
			// Password = "+ssmPassword);
			Status tokenResponse = authService.getAuthToken(ssmUsername, ssmPassword);
			// logger.info("tokenResponse = "+tokenResponse.toString());
			// TOKEN IS NOT NULL AND NOT EMPTY THEN ALLOW THE CONDITION
			if (tokenResponse != null
					&& tokenResponse.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
				String token = tokenResponse.getStatus();
				// logger.info("token = " + token);
				if (!token.isEmpty()) {

					JSONObject jsonObject = authService.createConnectionJSON(connectionJson, token, appName,
							connectionName);
					logger.info(connectionJson);
					if (jsonObject != null) {
						response.setText(jsonObject.getString(Constants.MSG).toString());
						response.setStatus(true);
					} else {
						response.setText(Constants.ERROR_SSM_CONNECTION);
						response.setStatus(false);
					}
				} else {
					logger.info(tokenResponse.getReason());
					response.setText(tokenResponse.getReason());
					response.setStatus(false);
				}
			} else {
				logger.info(tokenResponse.getReason());
				response.setText(tokenResponse.getReason());
				response.setStatus(false);
			}

		} catch (Exception e) {
			response.setText(Constants.ERROR_SSM_CONNECTION);
			response.setStatus(false);
			cliRecorderPopup.closeWindow();
		}
		return response;
	}

	@RequestMapping(value = "/cli/record", method = RequestMethod.POST)
	public @ResponseBody CLIResponse recordScript(@RequestParam("File") String fileName,
			@RequestParam("recordVideo") Boolean recordVideo, @RequestParam("format") String format,
			@RequestParam("connectionName") String connectionName) {
		logger.info("enter into cli/record service");
		logger.info("request parameters ");
		logger.info("fileName = " + fileName);
		logger.info("recordVideo = " + recordVideo);
		logger.info("format = " + format);
		logger.info("connectionName = " + connectionName);
		CLIResponse response = new CLIResponse();
		cliRecorderPopup.showWindow();
		try {
			logger.info("Recording Started...");
			Status recordStatus = null;
			logger.info("OS NAME = " + Constants.OPERATING_SYSTEM_NAME);
			if (Constants.OPERATING_SYSTEM_NAME.startsWith(Constants.WINDOWS_OS)) {
				recordStatus = cliRecorder.record(fileName, recordVideo, format, connectionName);
			} else if (Constants.OPERATING_SYSTEM_NAME.startsWith(Constants.MAC_OS)) {
				recordStatus = macCLIRecorder.record(fileName, recordVideo, format, connectionName);
			}
			if (!recordStatus.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Failed.getID()))) {
				logger.info("Recoding completed...");
			} else {
				response.setText(recordStatus.getReason());
				response.setStatus(false);
			}

			response.setText("Successfully Recorded");
			response.setStatus(true);

		} catch (Exception e) {
			response.setText("Error Recording: " + e.getMessage());
			response.setStatus(false);
			cliRecorderPopup.closeWindow();
		}
		return response;
	}

	@RequestMapping(value = "/video/start", method = RequestMethod.POST)
	public @ResponseBody CLIResponse startVideo(@RequestParam("File") String fileName,
			@RequestParam("format") String format, @RequestParam("project") String connectionName) {
		logger.info("enter into video/start service");
		CLIResponse response = new CLIResponse();
		logger.info("File = " + fileName + " format = " + format);
		try {
			logger.info("checking record video path exists or not");

			if (!new File(scriptsRootDir + connectionName + File.separator + Constants.RECORD_VIDEO_PATH).exists()) {
				new File(scriptsRootDir + connectionName + File.separator + Constants.RECORD_VIDEO_PATH).mkdirs();
			}
			logger.info("executing the cmd.exe to opening the powershell ");
			if (Constants.OPERATING_SYSTEM_NAME.startsWith(Constants.WINDOWS_OS)) {
				ProcessBuilder processBuilder = new ProcessBuilder();
				processBuilder.command("cmd.exe", "/c", "taskkill /im powershell.exe");
				processBuilder.start();
				Thread.sleep(5000);
				logger.info("executing the powershell.exe to open the ffmpeg for recording the video");
				String recordFileName = connectionName + "_" + fileName + "_" + new Date().getTime() + format;
				Runtime.getRuntime().exec(
						"cmd /c start /min /wait powershell.exe ffmpeg -f gdigrab -i desktop -framerate 32 -vcodec libx264 "
								+ scriptsRootDir + connectionName + File.separator + Constants.RECORD_VIDEO_PATH
								+ File.separator + recordFileName);
				Thread.sleep(5000);
				double size = 0;
				while (size == 0) {
					File file = new File(scriptsRootDir + connectionName + File.separator + Constants.RECORD_VIDEO_PATH
							+ File.separator + recordFileName);
					size = file.length();
				}

				response.setText(Constants.SUCCESS);
			} else if (Constants.OPERATING_SYSTEM_NAME.startsWith(Constants.MAC_OS)) {
				Status status = macCLIRecorder.startVideo(fileName, connectionName, format);
				response.setText(Constants.SUCCESS);
			}
			return response;
		} catch (Exception e) {
			response.setText(e.getMessage());
			return response;
		}
	}

	@RequestMapping(value = "/video/stop", method = RequestMethod.POST)
	public @ResponseBody CLIResponse killProcesses(@RequestParam("type") String type) {
		CLIResponse response = new CLIResponse();
		logger.info("enter video/stop service");
		try {
			if (Constants.OPERATING_SYSTEM_NAME.startsWith(Constants.WINDOWS_OS)) {
				ProcessBuilder processBuilder = new ProcessBuilder();
				logger.info("executing cmd.exe for  killing the powershell");
				processBuilder.command("cmd.exe", "/c", "taskkill /im powershell.exe");
				processBuilder.start();
				response.setStatus(true);
				response.setText(Constants.SUCCESS);
				logger.info("powershell terminated successfully.");
			} else if (Constants.OPERATING_SYSTEM_NAME.startsWith(Constants.MAC_OS)) {
				Status status = macCLIRecorder.killProcess();
				response.setStatus(true);
				response.setText(Constants.SUCCESS);
			}
			if (type.equals("cli")) {
				cliRecorderPopup.closeWindow();
			}
			return response;
		} catch (Exception e) {
			response.setStatus(false);
			response.setText(e.getMessage());
			return response;
		}
	}

	@PostMapping("/job/cli/run")
	@ResponseBody
	public ResponseEntity<?> runScript(HttpServletRequest servletRequest) throws IOException {
		logger.info("enter into job/cli/run service");
		JobResponse updateResponse = null;
		logger.info("converting reader to string");
		String content = IOUtils.toString(servletRequest.getReader());
		logger.info("converted content = " + content);
		if (content != null && content.trim().length() > 0) {
			logger.info("preparing the job object for saving");
			JSONObject contentObj = new JSONObject(content);
			Job job = new Job();
			job.setStatus(STATUS.Submitted.toString());
			job.setJobStatusCode(STATUS.Submitted.getID());
			job.setRecordVideo(contentObj.getBoolean(Constants.RECORD_VIDEO));
			job.setVideoFormat(contentObj.getString(Constants.FORMAT));
			Object ipObj = contentObj.get(Constants.REMOTE_IP);
			String remoteIp = null;
			if (ipObj != null) {
				if (String.valueOf(ipObj).length() > 0) {
					remoteIp = ipObj.toString().trim();
				}
			}
			job.setRemoteAgentIP(remoteIp);
			logger.info("remoteIp = " + remoteIp);
			try {
				job.setContent(contentObj.getJSONObject(Constants.CLI_PARAMS).toString());

			} catch (JSONException e) {
				job.setContent(Constants.EMPTY_JSON);
			}
			if (contentObj.getString(Constants.TASKID) != null) {
				job.setTaskId(contentObj.getString(Constants.TASKID).toString());
			}

			job.setScriptFileName(contentObj.getString(Constants.FILE));
			job.setScriptType(Constants.CLI);
			job.setRetryCount(0);
			job.setAppName(contentObj.getString(Constants.APPNAME).toString());
			job.setParalleExcecutionStatus(contentObj.getString(Constants.PARALLELEXECUTION_STATUS).toString());
			logger.info("job object = " + job);
			logger.info("saving job object into table");
			SSMConnections connectionsObj = ssmConnectionRepository.findByScriptFileNameAndConnectionNameAndScriptType(
					contentObj.getString(Constants.FILE), contentObj.getString(Constants.APPNAME).toString(),
					Constants.CLI);
			if (connectionsObj != null) {
				ssmConnectionRepository.updateRemoteIpAndExecutionStatusByConnectionNameAndScriptName(
						contentObj.getBoolean(Constants.RECORD_VIDEO), contentObj.getString(Constants.FORMAT), remoteIp,
						contentObj.getString(Constants.PARALLELEXECUTION_STATUS).toString(),
						contentObj.getString(Constants.APPNAME).toString(), contentObj.getString(Constants.FILE),
						connectionsObj.getId());
			}
			Job saveJob = jobRepository.save(job);
			logger.info("preparing the jobresponse from savejob object");
			updateResponse = JobUtil.updateResponse(saveJob, null);
			logger.info("updateResponse = " + updateResponse);
		}

		return new ResponseEntity<>(updateResponse, HttpStatus.FAILED_DEPENDENCY);
	}

	@PostMapping("/job/desktop/run")
	@ResponseBody
	public ResponseEntity<?> runDesktopScript(HttpServletRequest servletRequest) throws IOException {
		logger.info("enter into job/desktop/run service");
		JobResponse updateResponse = null;
		logger.info("converting reader to string");
		String content = IOUtils.toString(servletRequest.getReader());
		logger.info("converted content = " + content);
		if (content != null && content.trim().length() > 0) {
			logger.info("preparing the job object for saving");
			JSONObject contentObj = new JSONObject(content);
			Job job = new Job();
			job.setStatus(STATUS.Submitted.toString());
			job.setJobStatusCode(STATUS.Submitted.getID());
			job.setRecordVideo(contentObj.getBoolean(Constants.RECORD_VIDEO));
			job.setVideoFormat(contentObj.getString(Constants.FORMAT));
			Object ipObj = contentObj.get(Constants.REMOTE_IP);
			String remoteIp = null;
			if (ipObj != null) {
				if (String.valueOf(ipObj).length() > 0) {
					remoteIp = ipObj.toString().trim();
				}
			}
			job.setRemoteAgentIP(remoteIp);
			logger.info("remoteIp = " + remoteIp);
			try {
				job.setContent(contentObj.getJSONObject(Constants.DESKTOP_PARAMS).toString());

			} catch (JSONException e) {
				job.setContent(Constants.EMPTY_JSON);
			}
			if (contentObj.getString(Constants.TASKID) != null) {
				job.setTaskId(contentObj.getString(Constants.TASKID).toString());
			}

			job.setScriptFileName(contentObj.getString(Constants.FILE));
			job.setScriptType(Constants.DESKTOP);
			job.setRetryCount(0);
			job.setAppName(contentObj.getString(Constants.APPNAME).toString());
			job.setParalleExcecutionStatus(contentObj.getString(Constants.PARALLELEXECUTION_STATUS).toString());
			logger.info("job object = " + job);
			logger.info("saving job object into table");
			SSMConnections connectionsObj = ssmConnectionRepository.findByScriptFileNameAndConnectionNameAndScriptType(
					contentObj.getString(Constants.FILE), contentObj.getString(Constants.APPNAME).toString(),
					Constants.DESKTOP);
			if (connectionsObj != null) {
				ssmConnectionRepository.updateRemoteIpAndExecutionStatusByConnectionNameAndScriptName(
						contentObj.getBoolean(Constants.RECORD_VIDEO), contentObj.getString(Constants.FORMAT), remoteIp,
						contentObj.getString(Constants.PARALLELEXECUTION_STATUS).toString(),
						contentObj.getString(Constants.APPNAME).toString(), contentObj.getString(Constants.FILE),
						connectionsObj.getId());
			}
			Job saveJob = jobRepository.save(job);
			logger.info("preparing the jobresponse from savejob object");
			updateResponse = JobUtil.updateResponse(saveJob, null);
			logger.info("updateResponse = " + updateResponse);
		}

		return new ResponseEntity<>(updateResponse, HttpStatus.FAILED_DEPENDENCY);
	}

}
