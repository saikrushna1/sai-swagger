package com.saviynt.identitybot.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.saviynt.identitybot.Service.JobService;
import com.saviynt.identitybot.Service.SSMAuthService;
import com.saviynt.identitybot.Util.Constants;
import com.saviynt.identitybot.Util.JobUtil;
import com.saviynt.identitybot.Util.OrExcelGenarater;
import com.saviynt.identitybot.constants.Constants.ScriptType;
import com.saviynt.identitybot.constants.STATUS;
import com.saviynt.identitybot.model.BotAgent;
import com.saviynt.identitybot.model.BotScripts;
import com.saviynt.identitybot.model.CLIResponse;
import com.saviynt.identitybot.model.Job;
import com.saviynt.identitybot.model.SSMConnections;
import com.saviynt.identitybot.repository.BotAgentRepository;
import com.saviynt.identitybot.repository.BotScriptsRepository;
import com.saviynt.identitybot.repository.JobRepository;
import com.saviynt.identitybot.repository.SSMConnectionRepository;

@RestController
@RequestMapping("/agent")
public class AgentController {
	final static Logger logger = Logger.getLogger(AgentController.class);
	

	@Autowired
	JobRepository jobRepository;

	@Autowired
	BotAgentRepository botAgentRepository;

	@Value("${cobot.scriptType}")
	String cobotScriptType;

	@Value("${scripts.dir}")
	String scriptsRootDir;

	@Value("${cobot.cliRecordconnectionJSONUrl.url}")
	String cliRecordconnectionJSONUrl;

	@Value("${cobot.desktopRecordconnectionJSONUrl.url}")
	String desktopRecordconnectionJSONUrl;

	@Autowired
	SSMConnectionRepository ssmConnectionRepository;

	@Value("${cobot.createconnectionjson.url}")
	String cobotCreateconnectionJSONUrl;

	@Autowired
	SSMAuthService authService;

	@Value("${ssm.username}")
	String ssmUsername;

	@Value("${ssm.password}")
	String ssmPassword;

	@Autowired
	JobService jobService;

	@Autowired
	BotScriptsRepository botScriptsRepository;

	/**
	 * API to submit job for script execution on remote agent
	 * 
	 * @param agentIP:     IP address of remote agent
	 * @param fileName:    Name of the script to be executed
	 * @param content:Data input for the executing script
	 * @return Response with JObID which can be used to know the status of the
	 *         script execution status
	 */

	@RequestMapping(value = "/job/web", method = RequestMethod.POST)
	public @ResponseBody JobResponse uploadFileHandler(@RequestParam("AgentIP") String agentIP,
			@RequestParam("File") String fileName, @RequestParam("Body") String content) {
		logger.info("Entered into upload file handler method");
		Job job = new Job();
		job.setStatus(STATUS.Submitted.toString());
		job.setJobStatusCode(STATUS.Submitted.getID());
		job.setContent(content);
		job.setRemoteAgentIP(agentIP);
		job.setScriptType(cobotScriptType);
		job.setScriptFileName(fileName);
		Job saveJob = jobRepository.save(job);
		JobResponse updateResponse = JobUtil.updateResponse(saveJob, null);
		return updateResponse;
	}

	@RequestMapping(value = "/job/cli/record", method = RequestMethod.POST)
	public @ResponseBody JobResponse cliRecord(@RequestParam("AgentIP") String agentIP,
			@RequestParam("File") String fileName, @RequestParam("recordVideo") String recordVideo,
			@RequestParam("format") String format, @RequestParam("connectionName") String connectionName)

	{

		logger.info("Entered into upload file handler method");
		JSONObject jsonObject = new JSONObject();

		jsonObject.accumulate("recordVideo", recordVideo).accumulate("format", format).accumulate("connectionName",
				connectionName);

		Job job = new Job();
		job.setStatus(STATUS.Submitted.toString());
		job.setJobStatusCode(STATUS.Submitted.getID());
		job.setContent(jsonObject.toString());
		job.setRemoteAgentIP(agentIP);
		job.setScriptType(ScriptType.CLI_RECORD.toString());
		job.setRetryCount(0);
		job.setAppName(connectionName);
		job.setScriptFileName(fileName);
		Job saveJob = jobRepository.save(job);
		JobResponse updateResponse = JobUtil.updateResponse(saveJob, null);
		return updateResponse;
	}

	@RequestMapping(value = "/job/web/scrapfileplaystatus", method = RequestMethod.POST)
	public ResponseEntity<?> scrapfileplaystatus(@RequestParam("FileVideo") MultipartFile scriptfileVideo,
			@RequestParam(name = "scrappingFile", required = false) MultipartFile scrappingFile,
			@RequestParam("project") String project, @RequestParam("status") String status) {

		logger.info("Entered into CLI playback response handler method");

		Gson g = new Gson();

		try {
			if (!scrappingFile.isEmpty() && scrappingFile != null && project != null) {

				byte[] bytes = scrappingFile.getBytes();

				File dir = new File(scriptsRootDir + project);
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
			if (!scriptfileVideo.isEmpty() && scriptfileVideo != null && project != null) {

				byte[] bytes = scriptfileVideo.getBytes();

				File dir = new File(scriptsRootDir + project + File.separator + Constants.PLAYBACK_VIDEOS_PATH);
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(
						dir.getAbsolutePath() + File.separator + scriptfileVideo.getOriginalFilename());
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				logger.info("saved the video file " + serverFile.getAbsolutePath());

			}
		} catch (Exception e) {
			logger.error("Script file save failed ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} finally {
			Status statusObj = g.fromJson(status, Status.class);
			jobService.jobUpdateStatus(statusObj);
		}
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "/job/web/playstatus", method = RequestMethod.POST)
	public ResponseEntity<?> uploadwebstatus(@RequestParam("FileVideo") MultipartFile scriptfileVideo,
			@RequestParam("project") String project, @RequestParam("status") String status,
			@RequestParam("gloabalScriptVariableUniqueKay") String gloabalScriptVariableUniqueKay,
			@RequestParam("globalDataList") List<String> globalDataList) {

		logger.info("Entered into CLI playback response handler method");

		Gson g = new Gson();

		System.out.println("gloabalScriptVariableUniqueKay =" + gloabalScriptVariableUniqueKay);
		System.out.println("globalDataList =" + globalDataList);

		try {

			if (!scriptfileVideo.isEmpty() && scriptfileVideo != null && project != null) {

				byte[] bytes = scriptfileVideo.getBytes();

				File dir = new File(scriptsRootDir + project + File.separator + Constants.PLAYBACK_VIDEOS_PATH);
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(
						dir.getAbsolutePath() + File.separator + scriptfileVideo.getOriginalFilename());
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				logger.info("saved the video file " + serverFile.getAbsolutePath());

			}
		} catch (Exception e) {
			logger.error("Script file save failed ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} finally {
			Status statusObj = g.fromJson(status, Status.class);
			jobService.jobUpdateStatus(statusObj);
		}
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "/job/upload/webvideo", method = RequestMethod.POST)
	public ResponseEntity<?> uploadRecordVideo(@RequestParam("FileVideo") MultipartFile scriptfileVideo,
			@RequestParam("project") String project) {

		logger.info("Entered into CLI playback response handler method");

		if (!scriptfileVideo.isEmpty() && scriptfileVideo != null && project != null) {
			try {

				byte[] bytes = scriptfileVideo.getBytes();

				File dir = new File(scriptsRootDir + project + File.separator + Constants.RECORD_VIDEO_PATH);
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(
						dir.getAbsolutePath() + File.separator + scriptfileVideo.getOriginalFilename());
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				logger.info("saved the video file " + serverFile.getAbsolutePath());

				return ResponseEntity.ok().build();

			} catch (Exception e) {
				logger.error("Script file save failed ", e);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		}

		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "/job/checkScripts", method = RequestMethod.POST)
	public @ResponseBody String checkScripts(@RequestParam("connectionName") String connectionName,
			@RequestParam("scriptName") String scriptName) {
		logger.info("Entered into checkScripts service");
		logger.info("Request parameter values");
		logger.info("connectionName = " + connectionName);
		logger.info("scriptName = " + scriptName);
		try {
			List<BotScripts> scripts = botScriptsRepository.getAllScriptsfindByAppnameAndScriptname(connectionName,
					scriptName);
			if (scripts.size() > 0) {
				return Constants.FAIL;
			} else {
				return Constants.SUCCESS;
			}
		} catch (Exception e) {
			return Constants._ERROR;
		}
	}

	@RequestMapping(value = "/job/createjson", method = RequestMethod.POST)
	public @ResponseBody String createJson(@RequestParam("content") String content,
			@RequestParam("search") String search, @RequestParam("headers") String headers,
			@RequestParam("fileName") String fileName, @RequestParam("conntype") String connectionType,
			@RequestParam("connectionName") String connectionName, @RequestParam("agentIp") String agentIp,
			@RequestParam("botUserId") String botUserId) {

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
				generateExcelFile(connectionName, scriptsDir);
			} else {
				logger.info("File already exists.");
			}
			logger.info("writing content into above created file");
			// Write Content
			content=new OrExcelGenarater().generateOrExcel(content, fileName, connectionName, scriptsDir);
			FileWriter writer = new FileWriter(file);
			writer.write(content);
			writer.close();
			logger.info("content: " + content);
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
			// logger.info("Added values to connectionJson = "+connectionJson.toString());

			logger.info("Adding key, values to callObj");
			callObj.put(Constants.NAME, connectionName);
			callObj.put(Constants.CONNECTION, connectionName);
			callObj.put(Constants.URL, cobotCreateconnectionJSONUrl);
			callObj.put(Constants.HTTP_METHOD, Constants.POST);
			callObj.put(Constants.HTTP_CONTENT_TYPE, Constants.APP_SLASH_JSON);
			// logger.info("Added key, values to callObj = "+callObj.toString());

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
			// logger.info("Added key, values to httpParams = "+httpParams.toString());

			logger.info("Adding key, values to httpHeaders");
			httpHeaders.put(Constants.AUTHORIZATION, Constants.BASIC_AUTHORIZATION);
			// logger.info("Added key, values to httpHeaders = "+httpHeaders);

			logger.info("Adding httpParams, httpHeaders to callObj");
			callObj.put(Constants.HTTPPARAMS, httpParams);
			callObj.put(Constants.HTTP_HEADERS, httpHeaders);
			// logger.info("Added httpParams, httpHeaders to callObj =
			// "+callObj.toString());

			logger.info("Adding callObj to call");
			call.put(callObj);
			// logger.info("Added callObj to call = "+call.toString());

			logger.info("Adding call object to connectionJson");
			connectionJson.put(Constants.CALL, call);
			// logger.info("Added call object to connectionJson =
			// "+connectionJson.toString());

			// logger.info("getting token from SSM by using user = "+ssmUsername +" and
			// Password = "+ssmPassword);
			BotScripts botScripts = new BotScripts();
			botScripts.setAppName(connectionName);
			botScripts.setHttp_payload(httpParams.toString());
			botScripts.setScriptFileName(fileName);
			botScripts.setScriptType(Constants.WEB);
			botScripts.setCreatedBy(botUserId);
			BotScripts botScriptsObj = botScriptsRepository.findByScriptFileNameAndAppNameAndScriptType(fileName,
					connectionName, connectionType);
			if (botScriptsObj != null) {
				botScriptsRepository.updateUserIdById(botUserId, botScriptsObj.getId());
			} else {
				botScriptsRepository.save(botScripts);
			}

			return Constants.SUCCESS;
//			Status tokenResponse = authService.getAuthToken(ssmUsername, ssmPassword);
			// logger.info("tokenResponse = "+tokenResponse.toString());
			// TOKEN IS NOT NULL AND NOT EMPTY THEN ALLOW THE CONDITION
//			if (tokenResponse != null && tokenResponse.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {  
//				String token = tokenResponse.getStatus();
//				//logger.info("token = "+token);
//				if (!token.isEmpty()) {
//					JSONObject jsonObject = authService.createConnectionJSON(connectionJson, token, connectionName,connectionType);
//					logger.info("jsonObject = "+jsonObject);
//					if (jsonObject != null) {
//						logger.info("Connection successfully created in SSM");
//						return jsonObject.getString(Constants.MSG);
//					} else {
//						logger.error("Unable to create SSM connection");
//						return Constants.ERROR_SSM_CONNECTION;
//					}
//				}else {
//					logger.info("SSM Token is not generated. ");
//					return Constants.TOKEN_ERROR_MSG;
//				}
//			}else {
//				logger.info("SSM Token is not generated.");
//				return tokenResponse.getReason();
//			}
		} catch (IOException | InvalidFormatException e) {
			logger.error(e.getMessage());
			return Constants.ERROR;
		}

	}

	@RequestMapping(value = "/job/desktop/recordstatus", method = RequestMethod.POST)
	public ResponseEntity<?> desktopRecordStatus(@RequestParam("File") MultipartFile scriptfile,
			@RequestParam("FileVideo") MultipartFile scriptfileVideo,
			@RequestParam("connectionName") String connectionType,
			@RequestParam("scriptFileName") String scriptFileName, @RequestParam("format") String format,
			@RequestParam("recordVideo") boolean recordVideo, @RequestParam("status") String status,
			@RequestParam("reason") String reason, @RequestParam("appName") String appName) {

		logger.info("Entered into upload file handler method");
		CLIResponse response = new CLIResponse();
		try {
			if (!scriptfile.isEmpty() && scriptFileName != null) {
				try {

					byte[] bytes = scriptfile.getBytes();

					File dir = new File(scriptsRootDir + appName);
					if (!dir.exists())
						dir.mkdirs();

					// Create the file on server
					File serverFile = new File(
							dir.getAbsolutePath() + File.separator + scriptFileName + Constants.DOTSAH);
					if (!serverFile.exists()) {
						serverFile.createNewFile();
					}
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(bytes);
					stream.close();
				} catch (Exception e) {
					logger.error("Script file save failed ", e);
				}
			}

			if (scriptfileVideo != null && !scriptfileVideo.isEmpty()) {
				try {

					byte[] bytes = scriptfileVideo.getBytes();

					File dir = new File(scriptsRootDir + appName + File.separator + Constants.RECORD_VIDEO_PATH);
					if (!dir.exists())
						dir.mkdirs();

					// Create the file on server
					File serverFile = new File(
							dir.getAbsolutePath() + File.separator + scriptfileVideo.getOriginalFilename());
					if (!serverFile.exists()) {
						serverFile.createNewFile();
					}
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(bytes);
					stream.close();
				} catch (Exception e) {
					logger.error("Script vidoe save failed ", e);
				}
			}

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
			callObj.put(Constants.URL, desktopRecordconnectionJSONUrl);
			callObj.put(Constants.HTTP_METHOD, Constants.POST);
			callObj.put(Constants.HTTP_CONTENT_TYPE, Constants.APP_SLASH_JSON);

			httpParams.put(Constants.FILE, scriptFileName);
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

			// logger.info("connectionJson ->"+connectionJson);

			SSMConnections connections = new SSMConnections();
			connections.setScriptFileName(scriptFileName);
			connections.setRecordVideo(recordVideo);
			connections.setVideoFormat(format);
			connections.setConnectionType(connectionType);
			connections.setConnectionName(appName);
			connections.setRemoteIp(Constants.EMPTY);
			connections.setParalleExcecutionStatus(Constants.OFF);
			connections.setScriptType(Constants.DESKTOP);
			SSMConnections connectionsObj = ssmConnectionRepository
					.findByScriptFileNameAndConnectionNameAndScriptType(scriptFileName, appName, Constants.DESKTOP);
			if (connectionsObj != null) {
				ssmConnectionRepository.updateRemoteIpAndExecutionStatusByConnectionNameAndScriptName(recordVideo,
						format, Constants.EMPTY, Constants.OFF, appName, scriptFileName, connectionsObj.getId());
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
				// logger.info("token = "+token);
				if (!token.isEmpty()) {
					JSONObject jsonObject = authService.createConnectionJSON(connectionJson, token, appName,
							connectionType);
					logger.info("jsonObject = " + jsonObject);
					if (jsonObject != null) {
						logger.info("Connection successfully created in SSM");
						response.setText(jsonObject.getString(Constants.MSG).toString());
						response.setStatus(true);
					} else {
						logger.error("Unable to create SSM connection");
						response.setText(Constants.ERROR_SSM_CONNECTION);
						response.setStatus(false);
					}
				} else {
					logger.info("SSM Token is not generated. ");
					response.setText(Constants.TOKEN_ERROR_MSG);
					response.setStatus(false);
				}
			} else {
				logger.info("SSM Token is not generated.");
				response.setText(tokenResponse.getReason());
				response.setStatus(false);
			}
		} catch (Exception e) {
			response.setText(Constants.ERROR_SSM_CONNECTION);
			response.setStatus(false);
			logger.error("ERROR >>> while updating status to SSM = " + e.getMessage());
		}
		return ResponseEntity.ok(response);

	}

	@RequestMapping(value = "/job/desktop/desktoprecordstatuswithoutvideo", method = RequestMethod.POST)
	public ResponseEntity<?> desktoprecordstatuswithoutvideo(@RequestParam("File") MultipartFile scriptfile,
			@RequestParam("connectionName") String connectionType,
			@RequestParam("scriptFileName") String scriptFileName, @RequestParam("format") String format,
			@RequestParam("recordVideo") boolean recordVideo, @RequestParam("status") String status,
			@RequestParam("reason") String reason, @RequestParam("appName") String appName) {

		logger.info("Entered into upload file handler method");
		CLIResponse response = new CLIResponse();
		try {
			if (!scriptfile.isEmpty() && scriptFileName != null) {
				try {

					byte[] bytes = scriptfile.getBytes();

					File dir = new File(scriptsRootDir + appName);
					if (!dir.exists())
						dir.mkdirs();

					// Create the file on server
					File serverFile = new File(
							dir.getAbsolutePath() + File.separator + scriptFileName + Constants.DOTSAH);
					if (!serverFile.exists()) {
						serverFile.createNewFile();
					}
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(bytes);
					stream.close();
				} catch (Exception e) {
					logger.error("Script file save failed ", e);
				}
			}

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
			callObj.put(Constants.URL, desktopRecordconnectionJSONUrl);
			callObj.put(Constants.HTTP_METHOD, Constants.POST);
			callObj.put(Constants.HTTP_CONTENT_TYPE, Constants.APP_SLASH_JSON);

			httpParams.put(Constants.FILE, scriptFileName);
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

			// logger.info("connectionJson ->"+connectionJson);

			SSMConnections connections = new SSMConnections();
			connections.setScriptFileName(scriptFileName);
			connections.setRecordVideo(recordVideo);
			connections.setVideoFormat(format);
			connections.setConnectionType(connectionType);
			connections.setConnectionName(appName);
			connections.setRemoteIp(Constants.EMPTY);
			connections.setParalleExcecutionStatus(Constants.OFF);
			connections.setScriptType(Constants.DESKTOP);
			SSMConnections connectionsObj = ssmConnectionRepository
					.findByScriptFileNameAndConnectionNameAndScriptType(scriptFileName, appName, Constants.DESKTOP);
			if (connectionsObj != null) {
				ssmConnectionRepository.updateRemoteIpAndExecutionStatusByConnectionNameAndScriptName(recordVideo,
						format, Constants.EMPTY, Constants.OFF, appName, scriptFileName, connectionsObj.getId());
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
				// logger.info("token = "+token);
				if (!token.isEmpty()) {
					JSONObject jsonObject = authService.createConnectionJSON(connectionJson, token, appName,
							connectionType);
					logger.info("jsonObject = " + jsonObject);
					if (jsonObject != null) {
						logger.info("Connection successfully created in SSM");
						response.setText(jsonObject.getString(Constants.MSG).toString());
						response.setStatus(true);
					} else {
						logger.error("Unable to create SSM connection");
						response.setText(Constants.ERROR_SSM_CONNECTION);
						response.setStatus(false);
					}
				} else {
					logger.info("SSM Token is not generated. ");
					response.setText(Constants.TOKEN_ERROR_MSG);
					response.setStatus(false);
				}
			} else {
				logger.info("SSM Token is not generated.");
				response.setText(tokenResponse.getReason());
				response.setStatus(false);
			}
		} catch (Exception e) {
			response.setText(Constants.ERROR_SSM_CONNECTION);
			response.setStatus(false);
			logger.error("ERROR >>> while updating status to SSM = " + e.getMessage());
		}
		return ResponseEntity.ok(response);

	}

	@RequestMapping(value = "/job/cli/recordstatus", method = RequestMethod.POST)
	public ResponseEntity<?> cliRecordStatus(@RequestParam("File") MultipartFile scriptfile,
			@RequestParam("FileVideo") MultipartFile scriptfileVideo,
			@RequestParam("connectionName") String connectionType,
			@RequestParam("scriptFileName") String scriptFileName, @RequestParam("format") String format,
			@RequestParam("recordVideo") boolean recordVideo, @RequestParam("status") String status,
			@RequestParam("reason") String reason, @RequestParam("appName") String appName) {

		logger.info("Entered into upload file handler method");
		CLIResponse response = new CLIResponse();
		try {
			if (!scriptfile.isEmpty() && scriptFileName != null) {
				try {

					byte[] bytes = scriptfile.getBytes();

					File dir = new File(scriptsRootDir + appName + File.separator + Constants.RECORDED_SCRIPTS_PATH);
					if (!dir.exists())
						dir.mkdirs();

					// Create the file on server
					File serverFile = new File(
							dir.getAbsolutePath() + File.separator + scriptFileName + Constants.DOTTXT);
					if (!serverFile.exists()) {
						serverFile.createNewFile();
					}
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(bytes);
					stream.close();
				} catch (Exception e) {
					logger.error("Script file save failed ", e);
				}
			}

			if (scriptfileVideo != null && !scriptfileVideo.isEmpty()) {
				try {

					byte[] bytes = scriptfileVideo.getBytes();

					File dir = new File(scriptsRootDir + appName + File.separator + Constants.RECORD_VIDEO_PATH);
					if (!dir.exists())
						dir.mkdirs();

					// Create the file on server
					File serverFile = new File(
							dir.getAbsolutePath() + File.separator + scriptfileVideo.getOriginalFilename());
					if (!serverFile.exists()) {
						serverFile.createNewFile();
					}
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(bytes);
					stream.close();
				} catch (Exception e) {
					logger.error("Script vidoe save failed ", e);
				}
			}

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

			httpParams.put(Constants.FILE, scriptFileName);
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

			// logger.info("connectionJson ->"+connectionJson);

			SSMConnections connections = new SSMConnections();
			connections.setScriptFileName(scriptFileName);
			connections.setRecordVideo(recordVideo);
			connections.setVideoFormat(format);
			connections.setConnectionType(connectionType);
			connections.setConnectionName(appName);
			connections.setRemoteIp(Constants.EMPTY);
			connections.setParalleExcecutionStatus(Constants.OFF);
			connections.setScriptType(Constants.CLI);
			SSMConnections connectionsObj = ssmConnectionRepository
					.findByScriptFileNameAndConnectionNameAndScriptType(scriptFileName, appName, Constants.CLI);
			if (connectionsObj != null) {
				ssmConnectionRepository.updateRemoteIpAndExecutionStatusByConnectionNameAndScriptName(recordVideo,
						format, Constants.EMPTY, Constants.OFF, appName, scriptFileName, connectionsObj.getId());
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
				// logger.info("token = "+token);
				if (!token.isEmpty()) {
					JSONObject jsonObject = authService.createConnectionJSON(connectionJson, token, appName,
							connectionType);
					logger.info("jsonObject = " + jsonObject);
					if (jsonObject != null) {
						logger.info("Connection successfully created in SSM");
						response.setText(jsonObject.getString(Constants.MSG).toString());
						response.setStatus(true);
					} else {
						logger.error("Unable to create SSM connection");
						response.setText(Constants.ERROR_SSM_CONNECTION);
						response.setStatus(false);
					}
				} else {
					logger.info("SSM Token is not generated. ");
					response.setText(Constants.TOKEN_ERROR_MSG);
					response.setStatus(false);
				}
			} else {
				logger.info("SSM Token is not generated.");
				response.setText(tokenResponse.getReason());
				response.setStatus(false);
			}
		} catch (Exception e) {
			response.setText(Constants.ERROR_SSM_CONNECTION);
			response.setStatus(false);
			logger.error("ERROR >>> while updating status to SSM = " + e.getMessage());
		}
		return ResponseEntity.ok(response);

	}

	@RequestMapping(value = "/job/cli/clirecordstatuswithoutvideo", method = RequestMethod.POST)
	public ResponseEntity<?> cliRecordStatusWithoutVideo(@RequestParam("File") MultipartFile scriptfile,
			@RequestParam("connectionName") String connectionType,
			@RequestParam("scriptFileName") String scriptFileName, @RequestParam("format") String format,
			@RequestParam("recordVideo") boolean recordVideo, @RequestParam("status") String status,
			@RequestParam("reason") String reason, @RequestParam("appName") String connectionName) {

		logger.info("Entered into upload file handler method");
		CLIResponse response = new CLIResponse();
		try {
			if (!scriptfile.isEmpty() && scriptFileName != null) {
				try {

					byte[] bytes = scriptfile.getBytes();

					File dir = new File(
							scriptsRootDir + connectionName + File.separator + Constants.RECORDED_SCRIPTS_PATH);
					if (!dir.exists())
						dir.mkdirs();

					// Create the file on server
					File serverFile = new File(
							dir.getAbsolutePath() + File.separator + scriptfile.getOriginalFilename());
					if (!serverFile.exists()) {
						serverFile.createNewFile();
					}
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(bytes);
					stream.close();
				} catch (Exception e) {
					logger.error("Script file save failed ", e);
				}
			}

			JSONObject connectionJson = new JSONObject();
			JSONObject responseColsToPropsMap = new JSONObject();
			JSONArray call = new JSONArray();

			JSONObject callObj = new JSONObject();
			JSONObject httpParams = new JSONObject();
			JSONObject httpHeaders = new JSONObject();

			connectionJson.put(Constants.ACCOUNT_ID_PATH, Constants.SSM_TASK_ID);
			connectionJson.put(Constants.DATE_FORMAT, "");
			connectionJson.put(Constants.RESPONSE_COLS_TO_PROPS_MAP, responseColsToPropsMap);

			callObj.put(Constants.NAME, connectionName);
			callObj.put(Constants.CONNECTION, connectionName);
			callObj.put(Constants.URL, cliRecordconnectionJSONUrl);
			callObj.put(Constants.HTTP_METHOD, Constants.POST);
			callObj.put(Constants.HTTP_CONTENT_TYPE, Constants.APP_SLASH_JSON);

			httpParams.put(Constants.FILE, scriptFileName);
			httpParams.put(Constants.RECORD_VIDEO, recordVideo);
			httpParams.put(Constants.FORMAT, format);
			httpParams.put(Constants.CLI_PARAMS, Constants.EMPTY);
			httpParams.put(Constants.REMOTE_IP, Constants.EMPTY);
			httpParams.put(Constants.PARALLELEXECUTION_STATUS, Constants.OFF);
			httpParams.put(Constants.APPNAME, connectionName);
			httpParams.put(Constants.TASKID, Constants.SSM_VAR_TASKID);

			httpHeaders.put(Constants.AUTHORIZATION, Constants.BASIC_AUTHORIZATION);

			callObj.put(Constants.HTTPPARAMS, httpParams);
			callObj.put(Constants.HTTP_HEADERS, httpHeaders);

			call.put(callObj);

			connectionJson.put(Constants.CALL, call);

			// logger.info("connectionJson ->"+connectionJson);

			SSMConnections connections = new SSMConnections();
			connections.setScriptFileName(scriptFileName);
			connections.setRecordVideo(recordVideo);
			connections.setVideoFormat(format);
			connections.setConnectionName(connectionName);
			connections.setConnectionType(connectionType);
			connections.setRemoteIp(Constants.EMPTY);
			connections.setParalleExcecutionStatus(Constants.OFF);
			connections.setScriptType(Constants.CLI);
			SSMConnections connectionsObj = ssmConnectionRepository
					.findByScriptFileNameAndConnectionNameAndScriptType(scriptFileName, connectionName, Constants.CLI);
			if (connectionsObj != null) {
				ssmConnectionRepository.updateRemoteIpAndExecutionStatusByConnectionNameAndScriptName(recordVideo,
						format, Constants.EMPTY, Constants.OFF, connectionName, scriptFileName, connectionsObj.getId());
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
				// logger.info("token = "+token);
				if (!token.isEmpty()) {
					JSONObject jsonObject = authService.createConnectionJSON(connectionJson, token, connectionName,
							connectionType);
					logger.info("jsonObject = " + jsonObject);
					if (jsonObject != null) {
						logger.info("Connection successfully created in SSM");
						response.setText(jsonObject.getString(Constants.MSG).toString());
						response.setStatus(true);
					} else {
						logger.error("Unable to create SSM connection");
						response.setText(Constants.ERROR_SSM_CONNECTION);
						response.setStatus(false);
					}
				} else {
					logger.info("SSM Token is not generated. ");
					response.setText(Constants.TOKEN_ERROR_MSG);
					response.setStatus(false);
				}
			} else {
				logger.info("SSM Token is not generated.");
				response.setText(tokenResponse.getReason());
				response.setStatus(false);
			}
		} catch (Exception e) {
			response.setText(Constants.ERROR_SSM_CONNECTION);
			response.setStatus(false);
			logger.error("ERROR >>> while updating status to SSM = " + e.getMessage());
		}
		return ResponseEntity.ok(response);

	}

	@PostMapping("/job/cli/run")
	public @ResponseBody JobResponse runScript(HttpServletRequest servletRequest) throws IOException {

		JobResponse updateResponse = null;
		String content = IOUtils.toString(servletRequest.getReader());
		if (content != null && content.trim().length() > 0) {
			JSONObject contentObj = new JSONObject(content);
			Job job = new Job();
			job.setStatus(STATUS.Submitted.toString());
			job.setJobStatusCode(STATUS.Submitted.getID());
			job.setRecordVideo(contentObj.getBoolean(Constants.RECORD_VIDEO));
			job.setVideoFormat(contentObj.getString(Constants.FORMAT));
			job.setRemoteAgentIP(contentObj.getString(Constants.AGENT_IP));
			job.setAppName(contentObj.getString(Constants.APPNAME));
			try {
				job.setContent(contentObj.getJSONObject(Constants.CLI_PARAMS).toString());
			} catch (JSONException e) {
				job.setContent(Constants.EMPTY_JSON);
			}

			job.setScriptFileName(contentObj.getString(Constants.FILE));
			job.setScriptType(com.saviynt.identitybot.constants.Constants.ScriptType.CLI_PLAYBACK.toString());
			Job saveJob = jobRepository.save(job);
			updateResponse = JobUtil.updateResponse(saveJob, null);
		}

		return updateResponse;
	}

//	@PostMapping("/job/desktop/run")
//	public @ResponseBody JobResponse runDesktopScript(HttpServletRequest servletRequest) throws IOException {
//		JobResponse updateResponse = null;
//		String content = IOUtils.toString(servletRequest.getReader());
//		if(content!=null && content.trim().length()>0) {
//			JSONObject contentObj = new JSONObject(content);
//			Job job = new Job();
//			job.setStatus(STATUS.Submitted.toString());
//			job.setJobStatusCode(STATUS.Submitted.getID());
//			job.setRecordVideo(contentObj.getBoolean(Constants.RECORD_VIDEO));
//			job.setVideoFormat(contentObj.getString(Constants.FORMAT));
//			job.setRemoteAgentIP(contentObj.getString(Constants.AGENT_IP));
//			job.setAppName(contentObj.getString(Constants.APPNAME));
//			try {
//				job.setContent(contentObj.getJSONObject(Constants.DESKTOP_PARAMS).toString());
//			}catch(JSONException e) {
//				job.setContent(Constants.EMPTY_JSON);
//			}
//			
//			job.setScriptFileName(contentObj.getString(Constants.FILE));
//			job.setScriptType(com.peddle.digital.cobot.constants.Constants.ScriptType.DESKTOP_PLAYBACK.toString());
//			Job saveJob = jobRepository.save(job);
//			updateResponse = JobUtil.updateResponse(saveJob, null);
//		}
//		
//		return updateResponse;
//	}

	@PostMapping("/job/cli/playstatus")
	public ResponseEntity<?> cliPlayStatus(
			@RequestParam(name = "File", required = false) MultipartFile convertedBatScriptfile,
			@RequestParam(name = "ConvertedScriptFile", required = false) MultipartFile convertedScriptFile,
			@RequestParam(name = "PlaybackFile", required = false) MultipartFile playbackScript,
			@RequestParam(name = "FileVideo", required = false) MultipartFile scriptfileVideo,
			@RequestParam("FileName") String scriptFileName, @RequestParam("Format") String format,
			@RequestParam("JobId") String jobid, @RequestParam("status") String status,
			@RequestParam("reason") String reason, @RequestParam("appName") String appName) {

		logger.info("Entered into CLI playback response handler method");

		try {
			if (convertedBatScriptfile != null && !convertedBatScriptfile.isEmpty()) {
				try {

					byte[] bytes = convertedBatScriptfile.getBytes();

					File dir = new File(scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH);
					if (!dir.exists())
						dir.mkdirs();

					// Create the file on server
					File serverFile = new File(
							dir.getAbsolutePath() + File.separator + convertedBatScriptfile.getOriginalFilename());
					if (!serverFile.exists()) {
						serverFile.createNewFile();
					}
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(bytes);
					stream.close();
				} catch (Exception e) {
					logger.error("Script file save failed ", e);
				}
			}

			if (convertedScriptFile != null && !convertedScriptFile.isEmpty()) {
				try {

					byte[] bytes = convertedScriptFile.getBytes();

					File dir = new File(scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH);
					if (!dir.exists())
						dir.mkdirs();

					// Create the file on server
					File serverFile = new File(
							dir.getAbsolutePath() + File.separator + convertedScriptFile.getOriginalFilename());
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(bytes);
					stream.close();
				} catch (Exception e) {
					logger.error("Script file save failed ", e);
				}
			}
			if (playbackScript != null && !playbackScript.isEmpty()) {
				try {

					byte[] bytes = playbackScript.getBytes();

					File dir = new File(scriptsRootDir + appName + File.separator + Constants.PLAYBACK_SCRIPTS_PATH);
					if (!dir.exists())
						dir.mkdirs();

					// Create the file on server
					File serverFile = new File(
							dir.getAbsolutePath() + File.separator + playbackScript.getOriginalFilename());
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(bytes);
					stream.close();
				} catch (Exception e) {
					logger.error("Script file save failed ", e);
				}
			}

			if (!scriptfileVideo.isEmpty() && scriptFileName != null) {
				try {

					byte[] bytes = scriptfileVideo.getBytes();

					File dir = new File(scriptsRootDir + appName + File.separator + Constants.PLAYBACK_VIDEOS_PATH);
					if (!dir.exists())
						dir.mkdirs();

					// Create the file on server
					File serverFile = new File(
							dir.getAbsolutePath() + File.separator + scriptfileVideo.getOriginalFilename());
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(bytes);
					stream.close();
				} catch (Exception e) {
					logger.error("Script video save failed ", e);
				}
			}

			logger.info("Recieved Job Update Rquest for JOB" + jobid);
			Long jobDBId = JobUtil.getDBJobIDFromAppJobID(jobid);
			Job job = jobRepository.getOne(jobDBId);
			jobRepository.updateStatusAndReason(status, reason, jobDBId);
			// if(job.getParalleExcecutionStatus().equals(Constants.OFF)) {
			botAgentRepository.updateBotAgentLoadBalancerBySno(job.getBotAgent().longValue());
			// }
			BotAgent botAgent = botAgentRepository.findByAgentSno(job.getBotAgent().longValue());
			int numberOfInstances = botAgent.getNumberOfInstances();
			if (numberOfInstances > 0) {
				botAgentRepository.updateBotAgentNumberOfInstancesBySno(--numberOfInstances,
						job.getBotAgent().longValue());
			}
			Status tokenResponse = authService.getAuthToken(ssmUsername, ssmPassword);
			// logger.info("tokenResponse = "+tokenResponse.toString());
			// TOKEN IS NOT NULL AND NOT EMPTY THEN ALLOW THE CONDITION
			if (tokenResponse != null
					&& tokenResponse.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
				String token = tokenResponse.getStatus();
				// logger.info("token = "+token);
				if (!token.isEmpty()) {
					// Job job = jobRepository.getOne(jobDBId);
					// botAgentRepository.updateBotAgentLoadBalancerBySno(job.getBotAgent().longValue());
					authService.sendTaskUpdateResponseBackToSSM(job.getTaskId(), token, status, reason);
				} else {
					logger.info("SSM Token is not generated. ");
				}
			} else {
				logger.info("SSM Token is not generated.");
			}
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			logger.error("Script video save failed ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/job/cli/playstatuswithoutvideo")
	public ResponseEntity<?> cliPlayStatusWithoutVideo(
			@RequestParam(name = "File", required = false) MultipartFile convertedBatScriptfile,
			@RequestParam(name = "ConvertedScriptFile", required = false) MultipartFile convertedScriptFile,
			@RequestParam(name = "PlaybackFile", required = false) MultipartFile playbackScript,
			@RequestParam("FileName") String scriptFileName, @RequestParam("Format") String format,
			@RequestParam("JobId") String jobid, @RequestParam("status") String status,
			@RequestParam("reason") String reason, @RequestParam("appName") String appName) {

		logger.info("Entered into CLI playback response handler method");

		try {
			if (convertedBatScriptfile != null && !convertedBatScriptfile.isEmpty()) {
				try {

					byte[] bytes = convertedBatScriptfile.getBytes();

					File dir = new File(scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH);
					if (!dir.exists())
						dir.mkdirs();

					// Create the file on server
					File serverFile = new File(
							dir.getAbsolutePath() + File.separator + convertedBatScriptfile.getOriginalFilename());
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(bytes);
					stream.close();
				} catch (Exception e) {
					logger.error("Script file save failed ", e);
				}
			}

			if (convertedScriptFile != null && !convertedScriptFile.isEmpty()) {
				try {

					byte[] bytes = convertedScriptFile.getBytes();

					File dir = new File(scriptsRootDir + appName + File.separator + Constants.CONVERTED_SCRIPTS_PATH);
					if (!dir.exists())
						dir.mkdirs();

					// Create the file on server
					File serverFile = new File(
							dir.getAbsolutePath() + File.separator + convertedScriptFile.getOriginalFilename());
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(bytes);
					stream.close();
				} catch (Exception e) {
					logger.error("Script file save failed ", e);
				}
			}

			if (playbackScript != null && !playbackScript.isEmpty()) {
				try {

					byte[] bytes = playbackScript.getBytes();

					File dir = new File(scriptsRootDir + appName + File.separator + Constants.PLAYBACK_SCRIPTS_PATH);
					if (!dir.exists())
						dir.mkdirs();

					// Create the file on server
					File serverFile = new File(
							dir.getAbsolutePath() + File.separator + playbackScript.getOriginalFilename());
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(bytes);
					stream.close();
				} catch (Exception e) {
					logger.error("Script file save failed ", e);
				}
			}

			logger.info("Recieved Job Update Rquest for JOB" + jobid);
			Long jobDBId = JobUtil.getDBJobIDFromAppJobID(jobid);
			Job job = jobRepository.getOne(jobDBId);
			jobRepository.updateStatusAndReason(status, reason, jobDBId);
			// if(job.getParalleExcecutionStatus().equals(Constants.OFF)) {
			botAgentRepository.updateBotAgentLoadBalancerBySno(job.getBotAgent().longValue());
			// }
			BotAgent botAgent = botAgentRepository.findByAgentSno(job.getBotAgent().longValue());
			int numberOfInstances = botAgent.getNumberOfInstances();
			if (numberOfInstances > 0) {
				botAgentRepository.updateBotAgentNumberOfInstancesBySno(--numberOfInstances,
						job.getBotAgent().longValue());
			}
			Status tokenResponse = authService.getAuthToken(ssmUsername, ssmPassword);
			// logger.info("tokenResponse = "+tokenResponse.toString());
			// TOKEN IS NOT NULL AND NOT EMPTY THEN ALLOW THE CONDITION
			if (tokenResponse != null
					&& tokenResponse.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
				String token = tokenResponse.getStatus();
				// logger.info("token = "+token);
				if (!token.isEmpty()) {
					// Job job = jobRepository.getOne(jobDBId);
					// botAgentRepository.updateBotAgentLoadBalancerBySno(job.getBotAgent().longValue());
					authService.sendTaskUpdateResponseBackToSSM(job.getTaskId(), token, status, reason);
				} else {
					logger.info("SSM Token is not generated. ");
				}
			} else {
				logger.info("SSM Token is not generated.");
			}
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			logger.error("Script video save failed ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/job/desktop/playstatuswithoutvideo")
	public ResponseEntity<?> desktopPlayStatusWithoutVideo(@RequestParam("FileName") String scriptFileName,
			@RequestParam("Format") String format, @RequestParam("JobId") String jobid,
			@RequestParam("status") String status, @RequestParam("reason") String reason,
			@RequestParam("appName") String appName) {
		Status jobStatus = new Status();
		jobStatus.setJobId(jobid);
		jobStatus.setStatus(status);
		jobStatus.setReason(reason);
		jobService.jobUpdateStatus(jobStatus);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/job/desktop/playstatus")
	public ResponseEntity<?> desktopPlayStatusWithoutVideo(@RequestParam("FileVideo") MultipartFile scriptfileVideo,
			@RequestParam("FileName") String scriptFileName, @RequestParam("Format") String format,
			@RequestParam("JobId") String jobid, @RequestParam("status") String status,
			@RequestParam("reason") String reason, @RequestParam("appName") String appName) {

		logger.info("Entered into CLI playback response handler method");

		try {

			if (!scriptfileVideo.isEmpty() && scriptFileName != null) {
				try {

					byte[] bytes = scriptfileVideo.getBytes();

					File dir = new File(scriptsRootDir + appName + File.separator + Constants.PLAYBACK_VIDEOS_PATH);
					if (!dir.exists())
						dir.mkdirs();

					// Create the file on server
					File serverFile = new File(
							dir.getAbsolutePath() + File.separator + scriptfileVideo.getOriginalFilename());
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(bytes);
					stream.close();
				} catch (Exception e) {
					logger.error("Script video save failed ", e);
				}
			}

			logger.info("Recieved Job Update Rquest for JOB" + jobid);
			Long jobDBId = JobUtil.getDBJobIDFromAppJobID(jobid);
			Job job = jobRepository.getOne(jobDBId);
			jobRepository.updateStatusAndReason(status, reason, jobDBId);
			// if(job.getParalleExcecutionStatus().equals(Constants.OFF)) {
			botAgentRepository.updateBotAgentLoadBalancerBySno(job.getBotAgent().longValue());
			// }
			BotAgent botAgent = botAgentRepository.findByAgentSno(job.getBotAgent().longValue());
			int numberOfInstances = botAgent.getNumberOfInstances();
			if (numberOfInstances > 0) {
				botAgentRepository.updateBotAgentNumberOfInstancesBySno(--numberOfInstances,
						job.getBotAgent().longValue());
			}
			Status tokenResponse = authService.getAuthToken(ssmUsername, ssmPassword);
			// logger.info("tokenResponse = "+tokenResponse.toString());
			// TOKEN IS NOT NULL AND NOT EMPTY THEN ALLOW THE CONDITION
			if (tokenResponse != null
					&& tokenResponse.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
				String token = tokenResponse.getStatus();
				// logger.info("token = "+token);
				if (!token.isEmpty()) {
					// Job job = jobRepository.getOne(jobDBId);
					// botAgentRepository.updateBotAgentLoadBalancerBySno(job.getBotAgent().longValue());
					authService.sendTaskUpdateResponseBackToSSM(job.getTaskId(), token, status, reason);
				} else {
					logger.info("SSM Token is not generated. ");
				}
			} else {
				logger.info("SSM Token is not generated.");
			}
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			logger.error("Script video save failed ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	private void generateExcelFile(String connectionName, String scriptDir) throws IOException, InvalidFormatException {

		String excelFileName = scriptDir;
		excelFileName = excelFileName + "TestDataManagement.xlsx";
		if (!Files.exists(Paths.get(excelFileName))) {
			Workbook workbook = null;
			workbook = new XSSFWorkbook();

			Sheet s = workbook.createSheet(connectionName);
			Row r = s.createRow(0);
			Cell c = r.createCell(0, CellType.STRING);
			c.setCellValue("Key");
			Cell c1 = r.createCell(1, CellType.STRING);
			c1.setCellValue("ScriptName");
			Cell c2 = r.createCell(2, CellType.STRING);
			c2.setCellValue("Input1");
			Cell c3 = r.createCell(3, CellType.STRING);
			c3.setCellValue("Input2");
			Cell c4 = r.createCell(4, CellType.STRING);
			c4.setCellValue("Input3");
			Cell c5 = r.createCell(5, CellType.STRING);
			c5.setCellValue("Input4");
			Cell c6 = r.createCell(6, CellType.STRING);
			c6.setCellValue("Input5");
			Cell c7 = r.createCell(7, CellType.STRING);
			c7.setCellValue("Input6");
			Cell c8 = r.createCell(8, CellType.STRING);
			c8.setCellValue("Input7");
			Cell c9 = r.createCell(9, CellType.STRING);
			c9.setCellValue("Input8");
			Cell c10 = r.createCell(10, CellType.STRING);
			c10.setCellValue("Input9");
			Cell c11 = r.createCell(11, CellType.STRING);
			c11.setCellValue("Input10");
			Row r1 = s.createRow(1);
			Cell c01 = r1.createCell(0, CellType.STRING);
			c01.setCellValue("TestData1");
			Cell c02 = r1.createCell(1, CellType.STRING);
			c02.setCellValue("Hello");
			Cell c03 = r1.createCell(2, CellType.STRING);
			c03.setCellValue("Guess");
			Cell c04 = r1.createCell(3, CellType.STRING);
			c04.setCellValue("World");
			FileOutputStream fos = new FileOutputStream(new File(excelFileName));
			workbook.write(fos);
			fos.close();
			logger.info("creating xlxs file with name: " + excelFileName);
			workbook.close();
		} else {
			Runtime.getRuntime().exec("taskkill /f /im excel.exe");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {

			}
			Workbook workbook = null;

			workbook = new XSSFWorkbook(new FileInputStream(excelFileName));
			if (workbook.getSheet(connectionName) == null) {
				Sheet s = workbook.createSheet(connectionName);
				Row r = s.createRow(0);
				Cell c = r.createCell(0, CellType.STRING);
				c.setCellValue("Key");
				Cell c1 = r.createCell(1, CellType.STRING);
				c1.setCellValue("ScriptName");
				Cell c2 = r.createCell(2, CellType.STRING);
				c2.setCellValue("Input1");
				Cell c3 = r.createCell(3, CellType.STRING);
				c3.setCellValue("Input2");
				Cell c4 = r.createCell(4, CellType.STRING);
				c4.setCellValue("Input3");
				Cell c5 = r.createCell(5, CellType.STRING);
				c5.setCellValue("Input4");
				Cell c6 = r.createCell(6, CellType.STRING);
				c6.setCellValue("Input5");
				Cell c7 = r.createCell(7, CellType.STRING);
				c7.setCellValue("Input6");
				Cell c8 = r.createCell(8, CellType.STRING);
				c8.setCellValue("Input7");
				Cell c9 = r.createCell(9, CellType.STRING);
				c9.setCellValue("Input8");
				Cell c10 = r.createCell(10, CellType.STRING);
				c10.setCellValue("Input9");
				Cell c11 = r.createCell(11, CellType.STRING);
				c11.setCellValue("Input10");
				Row r1 = s.createRow(1);
				Cell c01 = r1.createCell(0, CellType.STRING);
				c01.setCellValue("TestData1");
				Cell c02 = r1.createCell(1, CellType.STRING);
				c02.setCellValue("Hello");
				Cell c03 = r1.createCell(2, CellType.STRING);
				c03.setCellValue("Guess");
				Cell c04 = r1.createCell(3, CellType.STRING);
				c04.setCellValue("World");
				FileOutputStream fos = new FileOutputStream(excelFileName);
				workbook.write(fos);
				fos.close();

				logger.info("creating xlxs file with name: " + excelFileName);
			}
			workbook.close();

		}
	}
	@PostMapping("/job/desktop/pywin/playstatuswithoutvideo")
	public ResponseEntity<?> pywinPlayStatusWithoutVideo(@RequestParam("FileName") String scriptFileName,
			@RequestParam("Format") String format, @RequestParam("JobId") String jobid,
			@RequestParam("status") String status, @RequestParam("reason") String reason,
			@RequestParam("appName") String appName) {
		Status jobStatus = new Status();
		jobStatus.setJobId(jobid);
		jobStatus.setStatus(status);
		jobStatus.setReason(reason);
		jobService.jobUpdateStatus(jobStatus);
		return ResponseEntity.ok().build();
	}
	@PostMapping("/job/desktop/pywin/playstatus")
	public ResponseEntity<?> pywinPlayStatusWithoutVideo(@RequestParam("FileVideo") MultipartFile scriptfileVideo,
			@RequestParam("FileName") String scriptFileName, @RequestParam("Format") String format,
			@RequestParam("JobId") String jobid, @RequestParam("status") String status,
			@RequestParam("reason") String reason, @RequestParam("appName") String appName) {

		logger.info("Entered into CLI playback response handler method");

		try {

			if (!scriptfileVideo.isEmpty() && scriptFileName != null) {
				try {

					byte[] bytes = scriptfileVideo.getBytes();

					File dir = new File(scriptsRootDir + appName + File.separator + Constants.PLAYBACK_VIDEOS_PATH);
					if (!dir.exists())
						dir.mkdirs();

					// Create the file on server
					File serverFile = new File(
							dir.getAbsolutePath() + File.separator + scriptfileVideo.getOriginalFilename());
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(bytes);
					stream.close();
				} catch (Exception e) {
					logger.error("Script video save failed ", e);
				}
			}

			logger.info("Recieved Job Update Rquest for JOB" + jobid);
			Long jobDBId = JobUtil.getDBJobIDFromAppJobID(jobid);
			Job job = jobRepository.getOne(jobDBId);
			jobRepository.updateStatusAndReason(status, reason, jobDBId);
			// if(job.getParalleExcecutionStatus().equals(Constants.OFF)) {
			botAgentRepository.updateBotAgentLoadBalancerBySno(job.getBotAgent().longValue());
			// }
			BotAgent botAgent = botAgentRepository.findByAgentSno(job.getBotAgent().longValue());
			int numberOfInstances = botAgent.getNumberOfInstances();
			if (numberOfInstances > 0) {
				botAgentRepository.updateBotAgentNumberOfInstancesBySno(--numberOfInstances,
						job.getBotAgent().longValue());
			}
			Status tokenResponse = authService.getAuthToken(ssmUsername, ssmPassword);
			// logger.info("tokenResponse = "+tokenResponse.toString());
			// TOKEN IS NOT NULL AND NOT EMPTY THEN ALLOW THE CONDITION
			if (tokenResponse != null
					&& tokenResponse.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
				String token = tokenResponse.getStatus();
				// logger.info("token = "+token);
				if (!token.isEmpty()) {
					// Job job = jobRepository.getOne(jobDBId);
					// botAgentRepository.updateBotAgentLoadBalancerBySno(job.getBotAgent().longValue());
					//authService.sendTaskUpdateResponseBackToSSM(job, token, status, reason);
				} else {
					logger.info("SSM Token is not generated. ");
				}
			} else {
				logger.info("SSM Token is not generated.");
			}
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			logger.error("Script video save failed ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	@RequestMapping(value = "/job/desktop/pywinauot/pywinrecordstatuswithoutvideo", method = RequestMethod.POST)
	public ResponseEntity<?> pywinrecordstatuswithoutvideo(@RequestParam("File") MultipartFile scriptfile,
			@RequestParam("connectionName") String connectionType,
			@RequestParam("scriptFileName") String scriptFileName, @RequestParam("format") String format,
			@RequestParam("recordVideo") boolean recordVideo, @RequestParam("status") String status,
			@RequestParam("appPath") String appPath,@RequestParam("userId") String userId) {

		logger.info("Entered into upload file handler method");
		CLIResponse response = new CLIResponse();
		try {
			if (!scriptfile.isEmpty() && scriptFileName != null) {
				try {

					byte[] bytes = scriptfile.getBytes();

					File dir = new File(scriptsRootDir + connectionType);
					if (!dir.exists())
						dir.mkdirs();

					// Create the file on server
					File serverFile = new File(
							dir.getAbsolutePath() + File.separator + scriptFileName );
					if (!serverFile.exists()) {
						serverFile.createNewFile();
					}
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(bytes);
					stream.close();
				} catch (Exception e) {
					logger.error("Script file save failed ", e);
				}
			}

			/*
			 * JSONObject connectionJson = new JSONObject(); JSONObject
			 * responseColsToPropsMap = new JSONObject(); JSONArray call = new JSONArray();
			 * 
			 * JSONObject callObj = new JSONObject(); JSONObject httpParams = new
			 * JSONObject(); JSONObject httpHeaders = new JSONObject();
			 * 
			 * connectionJson.put(Constants.ACCOUNT_ID_PATH, Constants.SSM_TASK_ID);
			 * connectionJson.put(Constants.DATE_FORMAT, "");
			 * connectionJson.put(Constants.RESPONSE_COLS_TO_PROPS_MAP,
			 * responseColsToPropsMap);
			 * 
			 * callObj.put(Constants.NAME, appName); callObj.put(Constants.CONNECTION,
			 * appName); callObj.put(Constants.URL, desktopRecordconnectionJSONUrl);
			 * callObj.put(Constants.HTTP_METHOD, Constants.POST);
			 * callObj.put(Constants.HTTP_CONTENT_TYPE, Constants.APP_SLASH_JSON);
			 * 
			 * httpParams.put(Constants.FILE, scriptFileName);
			 * httpParams.put(Constants.RECORD_VIDEO, recordVideo);
			 * httpParams.put(Constants.FORMAT, format);
			 * httpParams.put(Constants.CLI_PARAMS, Constants.EMPTY);
			 * httpParams.put(Constants.REMOTE_IP, Constants.EMPTY);
			 * httpParams.put(Constants.PARALLELEXECUTION_STATUS, Constants.OFF);
			 * httpParams.put(Constants.APPNAME, appName);
			 * httpParams.put(Constants.APPLICATION_PATH, appPath);
			 * httpParams.put(Constants.TASKID, Constants.SSM_VAR_TASKID); START - Added
			 * below code by raj for new requirement on 10-02-2021
			 * httpParams.put(Constants.SSM_SECURITY_SYSTEM, Constants.EMPTY);
			 * httpParams.put(Constants.SSM_ENDPOINT, Constants.EMPTY);
			 * httpParams.put(Constants.SSM_ACCOUNTNAME, Constants.EMPTY); END
			 * httpHeaders.put(Constants.AUTHORIZATION, Constants.BASIC_AUTHORIZATION);
			 * 
			 * callObj.put(Constants.HTTPPARAMS, httpParams);
			 * callObj.put(Constants.HTTP_HEADERS, httpHeaders);
			 * 
			 * call.put(callObj);
			 * 
			 * connectionJson.put(Constants.CALL, call);
			 * 
			 * //logger.info("connectionJson ->"+connectionJson);
			 * 
			 * SSMConnections connections =new SSMConnections();
			 * connections.setScriptFileName(scriptFileName);
			 * connections.setRecordVideo(recordVideo); connections.setVideoFormat(format);
			 * connections.setConnectionType(connectionType);
			 * connections.setConnectionName(appName);
			 * connections.setRemoteIp(Constants.EMPTY);
			 * connections.setParalleExcecutionStatus(Constants.OFF);
			 * connections.setScriptType(Constants.DESKTOP); SSMConnections connectionsObj =
			 * ssmConnectionRepository.findByScriptFileNameAndConnectionNameAndScriptType(
			 * scriptFileName,appName,Constants.DESKTOP); if(connectionsObj!=null) {
			 * ssmConnectionRepository.
			 * updateRemoteIpAndExecutionStatusByConnectionNameAndScriptName(recordVideo,
			 * format,Constants.EMPTY,Constants.OFF,appName, scriptFileName,
			 * connectionsObj.getId()); }else { ssmConnectionRepository.save(connections); }
			 * 
			 * //logger.info("getting token from SSM by using user = "+ssmUsername
			 * +" and Password = "+ssmPassword); Status tokenResponse =
			 * authService.getAuthToken(ssmUsername, ssmPassword);
			 * //logger.info("tokenResponse = "+tokenResponse.toString()); // TOKEN IS NOT
			 * NULL AND NOT EMPTY THEN ALLOW THE CONDITION if (tokenResponse != null &&
			 * tokenResponse.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID
			 * ()))) { String token = tokenResponse.getStatus();
			 * //logger.info("token = "+token); if (!token.isEmpty()) { JSONObject
			 * jsonObject = authService.createConnectionJSON(connectionJson, token,
			 * appName,connectionType); logger.info("jsonObject = "+jsonObject); if
			 * (jsonObject != null) { logger.info("Connection successfully created in SSM");
			 * response.setText(jsonObject.getString(Constants.MSG).toString());
			 * response.setStatus(true); } else {
			 * logger.error("Unable to create SSM connection");
			 * response.setText(Constants.ERROR_SSM_CONNECTION); response.setStatus(false);
			 * } }else { logger.info("SSM Token is not generated. ");
			 * response.setText(Constants.TOKEN_ERROR_MSG); response.setStatus(false); }
			 * }else { logger.info("SSM Token is not generated."); response.setText(
			 * tokenResponse.getReason()); response.setStatus(false); }
			 */
			JSONObject httpParams = new JSONObject();
			httpParams.put(Constants.FILE_NAME, scriptFileName);
			httpParams.put(Constants.FILE, scriptFileName);
			httpParams.put(Constants.PYWIN_PARAMS, "");
			httpParams.put(Constants.SCRIPT_TYPE, "PYWIN");
			httpParams.put(Constants.HTTP_PARAMS, Constants.EMPTY_ARRAY);
			httpParams.put(Constants.REMOTE_IP, "");
			httpParams.put(Constants.PARALLELEXECUTION_STATUS, Constants.OFF);
			httpParams.put(Constants.RECORD_VIDEO, false);
			httpParams.put(Constants.FORMAT, Constants.DOTMP4);
			httpParams.put(Constants.APPNAME, connectionType);
			httpParams.put(Constants.PARALLELEXECUTION_STATUS, "off");
			httpParams.put(Constants.TASKID, Constants.SSM_VAR_TASKID);
			httpParams.put(Constants.APPLICATION_PATH, appPath);
			httpParams.put(Constants.APP_PATH, connectionType);
			
			BotScripts botScripts = new BotScripts();
			botScripts.setAppName(connectionType);
			botScripts.setHttp_payload(httpParams.toString());
			botScripts.setScriptFileName(scriptFileName);
			botScripts.setScriptType("PYWIN");
			botScripts.setCreatedBy(userId);
			BotScripts botScriptsObj = botScriptsRepository.findByScriptFileNameAndAppNameAndScriptType(scriptFileName,
					connectionType, connectionType);
			if (botScriptsObj != null) {
				botScriptsRepository.updateUserIdById(userId, botScriptsObj.getId());
			} else {
				botScriptsRepository.save(botScripts);
			}
		} catch (Exception e) {
			response.setText(Constants.ERROR_SSM_CONNECTION);
			response.setStatus(false);
			logger.error("ERROR >>> while updating status to SSM = " + e.getMessage());
		}
		return ResponseEntity.ok(response);

	}
}
