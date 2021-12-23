package com.saviynt.identitybot.controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.saviynt.identitybot.Service.JobService;
import com.saviynt.identitybot.Service.SSMAuthService;
import com.saviynt.identitybot.Util.Constants;
import com.saviynt.identitybot.Util.FileExplorer;
import com.saviynt.identitybot.Util.S3Explorer;
import com.saviynt.identitybot.Util.ScriptUtil;
import com.saviynt.identitybot.constants.STATUS;
import com.saviynt.identitybot.mapper.JobUIMapper;
import com.saviynt.identitybot.model.BotAgent;
import com.saviynt.identitybot.model.Job;
import com.saviynt.identitybot.model.SSMConnections;
import com.saviynt.identitybot.repository.BotAgentRepository;
import com.saviynt.identitybot.repository.JobRepository;
import com.saviynt.identitybot.repository.SSMConnectionRepository;
import com.saviynt.identitybot.response.BotAgentsResponse;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/jobs")
public class SSMUIRestController {
	final static Logger logger = Logger.getLogger(SSMUIRestController.class);
	@Autowired
	JobRepository jobRepository;
	
	@Autowired
	BotAgentRepository botAgentRepository;
	
	@Autowired
	JobUIMapper mapper;
	
	@Autowired
	private S3Explorer s3explorer;
	
	@Autowired
	private FileExplorer fileExplorer;
	
	@Value("${scripts.store}")
	String scriptsStore;
	
	@Value("${scripts.dir}")
	String scriptsRootDir;
	
	@Autowired
	JobService jobService;
	
	@Value("${cobot.cliRecordconnectionJSONUrl.url}")
	String cliRecordconnectionJSONUrl;
	
	@Value("${cobot.desktopRecordconnectionJSONUrl.url}")
	String desktopRecordconnectionJSONUrl;
	
	@Autowired
	SSMAuthService authService;
	
	@Autowired
	private Environment env;
	
	@Value("${ssm.username}")
	String ssmUsername;

	@Value("${ssm.password}")
	String ssmPassword;
	
	@GetMapping("/monitor/dashboard")
	@ResponseBody
	public ResponseEntity<?> monitoDashboard() {
		logger.info("get all jobs");
		JSONObject jsonObject = new JSONObject();
		Long totalJobs = jobRepository.getTotalJobs();
		Long inprocessJobs = jobRepository.getAllInprocessJobs();
		Long failedJobs = jobRepository.getAllFailedJobs();
		Long successJobs = jobRepository.getAllSuccessJobs();
		jsonObject.put("totalJobs", totalJobs);
		jsonObject.put("inprocessJobs", inprocessJobs);
		jsonObject.put("failedJobs", failedJobs);
		jsonObject.put("successJobs", successJobs);
		logger.info(jsonObject);
		return new ResponseEntity<>(jsonObject,HttpStatus.OK);
	}
	
	@GetMapping("/monitor/process")
	@ResponseBody
	public ResponseEntity<?> monitorProcess() {
		logger.info("process jobs");
		JSONObject jsonObject = new JSONObject();
		List<Job> jobs = jobRepository.findAllByCreatedAtDesc();
		jsonObject.put("jobsList", jobs);
		logger.info(jsonObject);
		return new ResponseEntity<>(jsonObject,HttpStatus.OK);
	}
	
	@Autowired
	SSMConnectionRepository ssmConnectionRepository;
	
	@GetMapping("/monitor/agents")
	@ResponseBody
	public ResponseEntity<?> monitorAgents() {
		logger.info("monitor agents");
		JSONObject jsonObject = new JSONObject();
		List<BotAgent> botAgentsList = botAgentRepository.findAllByTimestampDesc();
		List<BotAgentsResponse> responseList = new ArrayList<BotAgentsResponse>();
		if (botAgentsList != null && botAgentsList.size() > 0) {
			responseList = mapper.updateStatus(botAgentsList);
		}
		jsonObject.put("agentList", responseList);
		logger.info(jsonObject);
		return new ResponseEntity<>(jsonObject,HttpStatus.OK);
	}
	
	@GetMapping("/manage/scriptfiles")
	@ResponseBody
	public ResponseEntity<?> manageScriptfiles() {
		logger.info("manage script files");
		JSONObject jsonObject = new JSONObject();
		LinkedHashMap<String, LinkedHashMap<String, List<String>>> scriptFiles = mapper.prepareScriptTypes();
		jsonObject.put("scriptFiles", scriptFiles);
		logger.info(jsonObject);
		return new ResponseEntity<>(jsonObject,HttpStatus.OK);
	}
	
	@GetMapping("/manage/scriptfiles/getfile")
	@ResponseBody
	public ResponseEntity<?> manageScriptfilesGetfile(HttpServletRequest request) {
		
		JSONObject myObj = new JSONObject();
		try {
			logger.info("Fetching Bot agents data..");
			
			String scriptType = request.getParameter(Constants.SCRIPT_TYPE);
			logger.info("script Type = "+scriptType);	
			
			String fileName = request.getParameter(Constants.FILENAME);
			logger.info("File is = "+request.getParameter(Constants.FILENAME));
			
			String rootDir = request.getParameter(Constants.ROOTDIR);
			logger.info("Root Dir = "+request.getParameter(Constants.ROOTDIR));
				
			String path = null;
			String fileContent = null;
			if (scriptsStore != null && scriptsStore.equalsIgnoreCase(Constants.SCRIPT_STORE_LOCAL)) {
				if (scriptType.equalsIgnoreCase(Constants.CLI)) {
					path = scriptsRootDir + rootDir+ File.separator + Constants.RECORDED_SCRIPTS_PATH ;
				} else {
					path = scriptsRootDir + rootDir;
				}
				logger.info("path = "+path);
				fileContent = fileExplorer.readContent(path, fileName);
			}else if(scriptsStore != null && scriptsStore.equalsIgnoreCase(Constants.S3_BUCKET)) {
				fileContent = s3explorer.readFileAsString(path, fileName);
			}
			logger.info("content = "+fileContent);
			
			if (fileContent != null) {
				myObj.put(Constants.STATUS,Constants.SUCCESS);
				myObj.put(Constants.DATA, fileContent);
			} else {
				myObj.put(Constants.STATUS, Constants._ERROR);
				myObj.put(Constants.DATA, Constants.DATA_NOT_FOUNT);
			}
			
		}catch (Exception e) {
			myObj.put(Constants.STATUS, Constants._ERROR);
			myObj.put(Constants.DATA, Constants.DATA_NOT_FOUNT);
		}
		return new ResponseEntity<>(myObj,HttpStatus.OK);
	}
	
	@PostMapping("/manage/scriptfiles/file/compile")
	@ResponseBody
	public ResponseEntity<?> manageScriptfilesFileCompile(HttpServletRequest request) {
		logger.info("manage script file compile");
		JSONObject myObj = new JSONObject();
		try {

			logger.info("content= " + request.getParameter(Constants.FILE_CONTENT));
			logger.info("filename =" + request.getParameter(Constants.FILENAME));
			
			logger.info("creating temp file");
			String fileName = fileExplorer.createTempFile(request.getParameter(Constants.FILE_CONTENT),
					request.getParameter(Constants.FILENAME));
			logger.info("TempFile =" + fileName);
			if (fileName != null && !fileName.isEmpty()) {
				String executeMode = env.getProperty(Constants.EXECUTE_DOT_MODE);
				logger.info("Execution Mode " + executeMode);
				logger.info("compiling the above created file");
				String error = ScriptUtil.compileScriptFileContent(fileName, executeMode);
				logger.info("compile response = "+error);
				
				logger.info("preparing myobj based on above response");
				if (error.isEmpty()) {
					myObj.put(Constants.STATUS, Constants.SUCCESS);
					myObj.put(Constants.MESSAGE, Constants.VALID_SUCCESS);
				} else {
					error = error.replace(fileName, request.getParameter(Constants.FILENAME));
					String className = fileName.split("\\\\")[fileName.split("\\\\").length - 1].replace(".java", "");
					error = error.replace(className, request.getParameter(Constants.FILENAME).replace(".java", ""));
					logger.info("className= " + className);
					myObj.put(Constants.STATUS, Constants._ERROR);
					myObj.put(Constants.MESSAGE, error);
				}
			} else {
				myObj.put(Constants.STATUS, Constants._ERROR);
				myObj.put(Constants.MESSAGE, Constants.FILE_CREATION_ERROR);
			}
			logger.info("myObj = "+myObj);
		
		}catch (Exception e) {
			myObj.put(Constants.STATUS, Constants._ERROR);
			myObj.put(Constants.MESSAGE, Constants.FILE_CREATION_ERROR);
		}
		return new ResponseEntity<>(myObj,HttpStatus.OK);
	}
	
	@PostMapping("/manage/scriptfiles/file/contentSaving")
	@ResponseBody
	public ResponseEntity<?> manageScriptfilesFileContentSaving(HttpServletRequest request) {
		logger.info("manage script file content saving");
		JSONObject myObj = new JSONObject();
		String status = null;
		try {


			logger.info("content " + request.getParameter(Constants.FILE_CONTENT));
			logger.info("filename =" + request.getParameter(Constants.FILENAME));
			
			logger.info("job = "+request.getParameter(Constants.FILENAME));
			String fileNameWithExt = request.getParameter(Constants.FILENAME);
			
			String rootDir = request.getParameter(Constants.ROOTDIR);
			logger.info("Root Dir = "+request.getParameter(Constants.ROOTDIR));
			
			String scriptType = request.getParameter(Constants.SCRIPT_TYPE);
			logger.info("script Type = "+scriptType);
			String path = null;
			
			if(scriptType != null && (scriptType.equalsIgnoreCase(Constants.CLI) || scriptType.equalsIgnoreCase(Constants.DESKTOP) )) {
				String content = request.getParameter(Constants.FILE_CONTENT);
				
				org.json.JSONObject finalCliParams = jobService.findFinalParams(content);
				/* if(!finalCliParams.isEmpty()) { */
					try {
						String[] fileNameArry = fileNameWithExt.split("\\.");
						String fileName = fileNameArry[0];
						
						logger.info("fetching connection by fileName = "+fileName);
						SSMConnections  connections = ssmConnectionRepository.findByScriptFileNameAndConnectionNameAndScriptType(fileName,rootDir,scriptType);
						logger.info("cononections = "+connections);
						
						org.json.JSONObject connectionJson = new org.json.JSONObject();
						org.json.JSONObject responseColsToPropsMap = new org.json.JSONObject();
						JSONArray call = new JSONArray();

						org.json.JSONObject callObj = new org.json.JSONObject();
						org.json.JSONObject httpParams = new org.json.JSONObject();
						org.json.JSONObject httpHeaders = new org.json.JSONObject();
						
						logger.info("Adding key, values to connectionJson");
						connectionJson.put(Constants.ACCOUNT_ID_PATH, Constants.SSM_TASK_ID);
						connectionJson.put(Constants.DATE_FORMAT, "");
						connectionJson.put(Constants.RESPONSE_COLS_TO_PROPS_MAP, responseColsToPropsMap);
						logger.info("Added key, values to connectionJson = "+connectionJson);
						
						logger.info("Adding key, values to callObj");
						callObj.put(Constants.NAME, connections.getConnectionName());
						callObj.put(Constants.CONNECTION, connections.getConnectionName());
						
						callObj.put(Constants.HTTP_METHOD, Constants.POST);
						callObj.put(Constants.HTTP_CONTENT_TYPE, Constants.APP_SLASH_JSON);
						logger.info("Added key, values to callObj = "+callObj);
						
						logger.info("Adding key, values to httpParams");
						httpParams.put(Constants.FILE, fileName);
						httpParams.put(Constants.RECORD_VIDEO, connections.getRecordVideo());
						httpParams.put(Constants.FORMAT, connections.getVideoFormat());
						if(scriptType.equalsIgnoreCase(Constants.CLI)) {
							httpParams.put(Constants.CLI_PARAMS, finalCliParams.isEmpty()? Constants.EMPTY:finalCliParams);
							callObj.put(Constants.URL, cliRecordconnectionJSONUrl);
						}else if(scriptType.equalsIgnoreCase(Constants.DESKTOP)) {
							httpParams.put(Constants.DESKTOP_PARAMS, finalCliParams.isEmpty()? Constants.EMPTY:finalCliParams);
							callObj.put(Constants.URL, desktopRecordconnectionJSONUrl);
						}
						httpParams.put(Constants.REMOTE_IP, connections.getRemoteIp());
						httpParams.put(Constants.PARALLELEXECUTION_STATUS, connections.getParalleExcecutionStatus());
						httpParams.put(Constants.APPNAME, rootDir);
						httpParams.put(Constants.TASKID, Constants.SSM_VAR_TASKID);
						logger.info("Added key, values to httpParams = "+httpParams);
						
						logger.info("Adding key, values to httpHeaders");
						httpHeaders.put(Constants.AUTHORIZATION, Constants.BASIC_AUTHORIZATION);
						logger.info("Added key, values to httpHeaders = "+httpHeaders);
						
						logger.info("Adding httpParams, httpHeaders to callObj");
						callObj.put(Constants.HTTPPARAMS, httpParams);
						callObj.put(Constants.HTTP_HEADERS, httpHeaders);
						logger.info("Adding httpParams, httpHeaders to callObj = "+callObj);
						
						logger.info("Adding callObj to call");
						call.put(callObj);
						//logger.info("Added callObj to call = "+call);
						
						logger.info("Adding call to connectionJson");
						connectionJson.put(Constants.CALL, call);
						//logger.info("Added call to connectionJson = "+connectionJson);
						
						//logger.info("getting token from SSM by using user = "+ssmUsername +" and Password = "+ssmPassword);
						Status tokenResponse = authService.getAuthToken(ssmUsername, ssmPassword);
						logger.info("tokenResponse = "+tokenResponse.toString());
						// TOKEN IS NOT NULL AND NOT EMPTY THEN ALLOW THE CONDITION
						if (tokenResponse != null
								&& tokenResponse.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
							String token = tokenResponse.getStatus();
							//logger.info("token = " + token);
							if (!token.isEmpty()) {
								org.json.JSONObject jsonObject = authService.createConnectionJSON(connectionJson, token,
										connections.getConnectionName(),connections.getConnectionType());
								logger.info("jsonObject = " + jsonObject);
								if (jsonObject != null) {
									logger.info(jsonObject.getString(Constants.MSG).toString());
									if(scriptType.equalsIgnoreCase(Constants.DESKTOP)) {
										path = scriptsRootDir + rootDir;
									}else if(scriptType.equalsIgnoreCase(Constants.CLI)) {
										path = scriptsRootDir + rootDir + File.separator + Constants.RECORDED_SCRIPTS_PATH  ;
									}
									status = jobService.saveFile(path, request.getParameter(Constants.FILENAME), request.getParameter(Constants.FILE_CONTENT));
								} else {
									status = "Unable to update connection";
									logger.error("Unable to update connection");
								}
							} else {
								status = "SSM Token is not generated.";
								logger.info("SSM Token is not generated. ");
							}
						} else {
							status = "Unable to connect SSM";
							logger.info("SSM Token is not generated.");
						}					
					}catch (Exception e) {
						status = "Unable to update connection";
						logger.error("Unable to update connection");
					}
/*				}else {	
					if(scriptType.equalsIgnoreCase(Constants.DESKTOP)) {	
						path = scriptsRootDir + rootDir;	
					}else if(scriptType.equalsIgnoreCase(Constants.CLI)) {	
						path = scriptsRootDir + rootDir + File.separator + Constants.RECORDED_SCRIPTS_PATH  ;	
					}	
					status = jobService.saveFile(path, request.getParameter(Constants.FILENAME), request.getParameter(Constants.FILE_CONTENT));
				}*/
			}else {
				path = scriptsRootDir + rootDir;
				status = jobService.saveFile(path, request.getParameter(Constants.FILENAME), request.getParameter(Constants.FILE_CONTENT));
			}
			logger.info("path = "+path);

			if (status != null && status.isEmpty()) {
				myObj.put(Constants.STATUS, Constants.SUCCESS);
				myObj.put(Constants.MESSAGE, Constants.FILE_SAVE_SUCCESS);
			} else {
				myObj.put(Constants.STATUS, Constants.ERROR);
				myObj.put(Constants.MESSAGE, status);
			}


		
		}catch (Exception e) {
			myObj.put(Constants.STATUS, Constants._ERROR);
			myObj.put(Constants.MESSAGE, status);
		}
		return new ResponseEntity<>(myObj,HttpStatus.OK);
	}
}
