package com.saviynt.identitybot.agent.controller;

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
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
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saviynt.identitybot.agent.cli.CLIPlayer;
import com.saviynt.identitybot.agent.cli.CLIRecorder;
import com.saviynt.identitybot.agent.cli.MacCLIRecorder;
import com.saviynt.identitybot.agent.constants.Constants;
import com.saviynt.identitybot.agent.constants.STATUS;
import com.saviynt.identitybot.agent.model.Response;
import com.saviynt.identitybot.agent.response.Status;
import com.saviynt.identitybot.agent.util.BlueZone;
import com.saviynt.identitybot.agent.util.CliRecorderPopup;
import com.saviynt.identitybot.agent.util.FileExplorer;
import com.saviynt.identitybot.agent.util.MobaXterm;

import in.co.sahi.datastore.ReportDB;
import in.co.sahi.distributed.SuiteInfo;
import net.sf.sahi.config.Configuration;
import net.sf.sahi.test.TestRunner;
import net.sf.sahi.util.Utils;

/**
 * Handles requests for the application file upload requests
 */
@Controller
@RequestMapping("/agent/api")
public class AgentController {

	public static final String SEARCH_EXCEPTION = "SearchTextNotFoundException.class";

	public static Map<String, String> variableMap = null;
	
	@Value("${pywinauto.recorder.path}")
	String pywinAutoRecorderPath;
	
//	@Value("${secclient.oauth2.client.clientId}")
//	String clientId;
//
//	@Value("${secclient.oauth2.client.clientSecret}")
//	String clientSecret;
//
//	@Value("${secclient.oauth2.client.accessTokenUri}")
//	String accessTokenUri;
//
//	@Value("${secclient.oauth2.client.scope}")
//	String scope;

	@Value("${scripts.dir}")
	String scriptsRootDir;

	@Value("${sendSignal.root.dir}")
	String sendSignalRootDir;

	@Value("${sahi.root.dir}")
	String sahiRootDir;

	@Value("${sahi.recorder.url}")
	String sahiRecorderUrl;

	@Value("${cobot.server.url}")
	String cobotServer;

	@Value("${testbot.userid}")
	String testBotUserId;
	
	@Value("${testbot.password}")
	String testBotPassword;
	
	@Autowired
	private MobaXterm mobaXterm;
	
	@Autowired
	private BlueZone bluezone;

	@Autowired
	private Environment env;

	@Autowired
	CLIRecorder cliRecorder;

	@Autowired
	CLIPlayer cLIPlayer;

	@Autowired
	MacCLIRecorder macCLIRecorder;

	@Value("${video.record}")
	String videoRecord;

	@Autowired
	FileExplorer fileExplorer;

	private static final Logger logger = LoggerFactory.getLogger(AgentController.class);

	static final String LIBS_PATH = "libs";
	static final String ALL_JARS = "*";

	@Autowired
	private CliRecorderPopup cliRecorderPopup;

	
	@RequestMapping(value = "/check/script", method = RequestMethod.POST)
	public @ResponseBody Response checkScript(@RequestParam("connectionName") String connectionName,
			@RequestParam("scriptName") String scriptName)
			throws Exception {
		logger.info("check request parameters ");
		logger.info("connectionName = " + connectionName);
		logger.info("scriptName = " + scriptName);
		Response response = new Response();
		 String authStr = testBotUserId+":"+testBotPassword;
		String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
		try {
			MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<String, Object>();
			bodyMap.add("connectionName", connectionName);
			bodyMap.add("scriptName", scriptName);

			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Basic " + base64Creds);
			// headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			org.springframework.http.HttpEntity<MultiValueMap<String, Object>> requestEntity = new org.springframework.http.HttpEntity<>(
					bodyMap, headers);

			RestTemplate restTemplate = new RestTemplate();
			cobotServer = cobotServer.replaceAll("/$", "");
			String uploadWebRecordURL = cobotServer + Constants.CobotServerUrls.CHECK_WEB_SCRIPTS;

			ResponseEntity<String> responseFromApi = restTemplate.exchange(uploadWebRecordURL, HttpMethod.POST, requestEntity, String.class);
			if (responseFromApi.getStatusCode() == HttpStatus.OK) {

				logger.info("Cobot Server response status: " + responseFromApi.getStatusCode());
				logger.info("Cobot Server response body: " + responseFromApi.getBody());
				logger.info("responseFromApi.getBody().toString().equals(Constants.SUCCESS): " + responseFromApi.getBody().toString().equals(Constants.SUCCESS));
				logger.info("responseFromApi.getBody().toString().equals(Constants.FAIL): " + responseFromApi.getBody().toString().equals(Constants.FAIL));
				if(responseFromApi.getBody().toString().equals(Constants.SUCCESS)) {
					response.setText("success");
					response.setStatus(true);
				}else if(responseFromApi.getBody().toString().equals("fail")){
					response.setText("Script already exists");
					response.setStatus(false);
				}else {
					response.setText("Internal server error");
					response.setStatus(false);
				}
			}else {
				response.setText("Server connection error");
				response.setStatus(false);
			}
		
		}catch (Exception e) {
			response.setText("Error Recording: " + e.getMessage());
			response.setStatus(false);
		}
		return response;
	}
	
	/**
	 * Upload single file using Spring Controller
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/executescript", method = RequestMethod.POST)
	public @ResponseBody String uploadFileHandler(@RequestParam("File") MultipartFile file,
			@RequestParam("SearchExceptionFile") MultipartFile searchExceptionFile, @RequestParam("Body") String body,
			@RequestParam("JobId") String jobid, @RequestParam("CallBackURL") String callBackURL,
			@RequestParam("FileName") String fileName,@RequestParam("excludingPattern") String excludingPattern,@RequestParam("suiteName") String suiteName,@RequestParam("browserType") String browserType,boolean readParentScriptOutputFlag,String parentScriptOutputdataList) {

		String name = fileName;
		// Creating the directory to store file
		String rootPath = env.getProperty("scripts.dir") + suiteName;
		File dir = new File(rootPath );
		if (!dir.exists())
			dir.mkdirs();

		if (!searchExceptionFile.isEmpty()) {
			try {
				byte[] bytes = searchExceptionFile.getBytes();

				// Create the file on server
				File serverFile = new File(
						dir.getAbsolutePath() + File.separator+ searchExceptionFile.getOriginalFilename());
				if(serverFile.createNewFile()) {
					logger.info("file created");
				}
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		File tempFile = null;
		File tempBatFile = null;
		if (!file.isEmpty()) {
			try {

				byte[] bytes = file.getBytes();

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				File root = new File(rootPath);

				String scriptclassFile = name.replace(".class", "");

				// String classpath = System.getProperty("java.class.path");
				String cromeDriverpath = "";
				if(browserType!=null) {
					if(browserType.equals(Constants.CHROME)) {
						cromeDriverpath = System.getProperty("webdriver.chrome.driver");
					}else if(browserType.equals(Constants.MOZILLA)) {
						cromeDriverpath = System.getProperty("webdriver.gecko.driver");
					}else if(browserType.equals(Constants.IE)) {
						cromeDriverpath = System.getProperty("webdriver.ie.driver");
					}else if(browserType.equals(Constants.MICROSOFTEDGE)) {
						cromeDriverpath = System.getProperty("webdriver.edge.driver");
					}else if(browserType.equals(Constants.SAFARI)) {
						cromeDriverpath = "";
					}
				}
				logger.info("browserType " + browserType);
				logger.info("cromeDriverpath " + cromeDriverpath);
				String installDir = System.getProperty("installdir");
				logger.info("installDir " + installDir);
				tempFile = File.createTempFile("temp" + System.currentTimeMillis(), ".json");
				logger.info("tempFile path " + tempFile.getAbsolutePath());
				String tempFilePath = null;
				String absolutePath = null;
				if (Constants.OPERATING_SYSTEM_NAME.startsWith(Constants.WINDOWS_OS)) {
					tempFilePath = tempFile.getPath().substring(0, tempFile.getPath().lastIndexOf("\\"));
				} else {
					tempFilePath = tempFile.getPath().substring(0, tempFile.getPath().lastIndexOf("/"));
				}
				logger.info("tempFilePath" + tempFilePath);
				if (Constants.OPERATING_SYSTEM_NAME.startsWith(Constants.WINDOWS_OS)) {
					absolutePath = tempFile.getAbsolutePath().replace("\\", "/");
					cromeDriverpath = cromeDriverpath.replace("\\", "/");
				}else {
					absolutePath = tempFile.getAbsolutePath();
				}
				logger.info("absolutePath" + absolutePath);
				logger.info("cromeDriverpath" + cromeDriverpath);
				logger.info("body" + body);
				if(readParentScriptOutputFlag) {
					JSONObject scriptContent = new JSONObject(body);
					scriptContent.put("parentData", parentScriptOutputdataList);
					body = scriptContent.toString();
				}
				
				Files.write(Paths.get(absolutePath), body.getBytes());
				String line = null;
				if (Constants.OPERATING_SYSTEM_NAME.startsWith(Constants.WINDOWS_OS)) {
					FileUtils.copyFile(new File(sendSignalRootDir.replace("/", "\\") + "\\sendSignal.exe"),
							new File(tempFilePath + "\\sendSignal.exe"));
					BufferedReader br = new BufferedReader(new FileReader(new File(
							sahiRootDir.replace("\\", "/").replace("\\", "/") + "/userdata/bin/stopVideo.bat")));
					line = br.readLine();
					String bodyContent = "";
					while (line != null) {
						bodyContent = bodyContent + line + System.lineSeparator();
						line = br.readLine();
					}
					FileWriter writer = new FileWriter(tempFilePath + "\\stopVideo.bat");
					writer.write(bodyContent);
					writer.flush();
					writer.close();
					br.close();
				}
				String seliniumJarPath = installDir + File.separatorChar + LIBS_PATH + File.separatorChar + ALL_JARS;

				logger.info(seliniumJarPath);
				
				//
				// logger.info("java -cp \"" + seliniumJarPath +";"+classpath+";"+
				// rootPath +"\" "+scriptclassFile + " "+absolutePath+" "+
				// cromeDriverpath+" "+callBackURL+" "+ jobid + " " + accessTokenUri
				// + " "+clientId+" "+clientSecret+" "+scope);

				//
				
				String recordFilePath = scriptsRootDir + suiteName; 
				String execStr = "java -cp \"" + seliniumJarPath + ";" + rootPath + "\" " + scriptclassFile + " "
						+ absolutePath + " " + cromeDriverpath + " " + cobotServer + " " + jobid + " " + excludingPattern+ " " + testBotUserId+ " " + testBotPassword +" "+recordFilePath+" "+ suiteName +" "+readParentScriptOutputFlag;
				logger.info(execStr);
				Process process = Runtime.getRuntime().exec(execStr);

				// Process process = Runtime.getRuntime().exec("java -cp \"" + seliniumJarPath
				// +";"+
				// rootPath +"\" "+scriptclassFile + " "+body+" "+
				// cromeDriverpath+" "+callBackURL+" "+ jobid + " " + accessTokenUri
				// + " "+clientId+" "+clientSecret+" "+scope);

				StreamReader errorReader = new StreamReader(process.getErrorStream(), "ERROR");

				StreamReader outputReader = new StreamReader(process.getInputStream(), "OUTPUT");

				errorReader.start();
				outputReader.start();

				process.waitFor();

				InputStream errorStream = process.getErrorStream();
				StringWriter scriptRunWriter = new StringWriter();
				IOUtils.copy(errorStream, scriptRunWriter, "UTF-8");
				String runErrorStr = scriptRunWriter.toString();
				errorStream.close();

				InputStream is = process.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));

				line = null;
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
				}

				logger.error(runErrorStr);

				logger.info("Server File Location=" + serverFile.getAbsolutePath());

				return "Your successfully uploaded file=" + name;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
				return "You failed to upload " + name + " => " + e.getMessage();
			} finally {
				if (tempFile != null && tempFile.exists()) {
					tempFile.delete();
				}
				if (tempBatFile != null && tempBatFile.exists()) {
					tempBatFile.delete();
				}
			}
		} else {
			return "You failed to upload " + name + " because the file was empty.";
		}
	}

	@RequestMapping(value = "/cli/execute", method = RequestMethod.POST)
	public @ResponseBody Response playScript(@RequestParam("JobId") String jobID,
			@RequestParam("CallBackURL") String callBackURL, @RequestParam("Body") String data,
			@RequestParam("FileName") String scriptFileName, @RequestParam("Format") String format,
			@RequestParam("RecordVideo") String recordVideo, @RequestParam("File") MultipartFile file,
			@RequestParam("AppName") String appName) {

		Response response = new Response();
		try {

			boolean mobaX = writeFileTo(file, scriptFileName, appName);
			Status status = cLIPlayer.runTest(scriptFileName, format, callBackURL, jobID, data,
					Boolean.parseBoolean(recordVideo), appName, mobaX);

			if (status.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
				logger.info("Cli playback completed");
				response.setStatus(true);
				response.setText(Constants.SUCCESS);
			} else {
				logger.info("Cli playback failed");
				response.setStatus(false);
				response.setText(status.getReason());
			}
		} catch (Exception e) {
			response.setText("Error playbakc Cli Script: " + e.getMessage());
			response.setStatus(false);
		}
		return response;
	}

	public boolean writeFileTo(MultipartFile file, String fileName, String appName) throws Exception {
		boolean mobaX = false;
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				String rootPath = env.getProperty("scripts.dir");
				String recorderFilePath = rootPath + File.separator + appName + File.separator
						+ Constants.RECORDED_SCRIPTS_PATH;
				recorderFilePath = recorderFilePath.replace("\\", "/");
				File dir = new File(recorderFilePath + File.separator);
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				String line;
				String appPath = "";
				File serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				logger.info("file path " + dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
				BufferedReader br = new BufferedReader(
						new FileReader(new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename())));
				while ((line = br.readLine()) != null) {
					if (StringUtils.contains(line, "doskey /HISTORY")) {
						appPath = br.readLine();
					}
				}
				if (appPath.trim().length() > 0) {
					mobaX = true;
				}
				br.close();

			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
		return mobaX;
	}

	public void writeDesktopFileTo(MultipartFile file, String fileName, String appName) throws Exception {
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				String rootPath = env.getProperty("scripts.dir");
				String recorderFilePath = rootPath + File.separator + appName;
				recorderFilePath = recorderFilePath.replace("\\", "/");
				File dir = new File(recorderFilePath + File.separator);
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
	}

	/**
	 * Upload multiple file using Spring Controller
	 */
	@RequestMapping(value = "/launchcmd", method = RequestMethod.POST)
	public @ResponseBody void launchcmd() {

		try {
			Runtime process = Runtime.getRuntime();
			process.exec("cmd /c start cmd.exe /K powershell");

			System.out.println("launched CMD");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/desktop/ssm", method = RequestMethod.POST)
	public @ResponseBody Response ssmDesktopScript(@RequestParam("File") String fileName,
			@RequestParam("recordVideo") Boolean recordVideo, @RequestParam("format") String format,
			@RequestParam("connectionName") String connectionName, @RequestParam("appName") String appName,
			@RequestParam("appPath") String appPath) {
		Response response = new Response();
		try {
			String callBackURL = "";

			if (videoRecord.equalsIgnoreCase(Constants.VIDEO_RECORD_ON)) {
				Thread.sleep(30000);
				stopVideo();
			}

			Status callBackStatus = cliRecorder.sendDesktopExecutionStatus(callBackURL, "", "", fileName, "",
					connectionName, format, Boolean.toString(recordVideo), appName, "Success", "", appPath);
			if (callBackStatus.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
				JSONObject obj = new JSONObject(callBackStatus.getReason().toString());
				response.setText(obj.get("text").toString());
				response.setStatus((Boolean) obj.get("status"));
			} else {
				JSONObject obj = new JSONObject(callBackStatus.getReason().toString());
				response.setText(obj.get("text").toString());
				response.setStatus((Boolean) obj.get("status"));
			}

		} catch (Exception e) {
			response.setText(Constants.ERROR_SSM_CONNECTION);
			response.setStatus(false);
		}
		return response;
	}

	@RequestMapping(value = "/cli/ssm", method = RequestMethod.POST)
	public @ResponseBody Response ssmScript(@RequestParam("File") String fileName,
			@RequestParam("recordVideo") Boolean recordVideo, @RequestParam("format") String format,
			@RequestParam("connectionName") String connectionName, @RequestParam("appName") String appName) {
		Response response = new Response();
		try {
			String callBackURL = "";

			Status callBackStatus = cliRecorder.sendExecutionStatus(callBackURL, "", "", fileName, "", connectionName,
					format, Boolean.toString(recordVideo), appName, "Success", "");
			if (callBackStatus.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
				JSONObject obj = new JSONObject(callBackStatus.getReason().toString());
				response.setText(obj.get("text").toString());
				response.setStatus((Boolean) obj.get("status"));
			} else {
				JSONObject obj = new JSONObject(callBackStatus.getReason().toString());
				response.setText(obj.get("text").toString());
				response.setStatus((Boolean) obj.get("status"));
			}

		} catch (Exception e) {
			response.setText(Constants.ERROR_SSM_CONNECTION);
			response.setStatus(false);
		}
		return response;
	}

	@RequestMapping(value = "/desktop/record", method = RequestMethod.POST)
	public @ResponseBody Response recordDesktopScript(@RequestParam("File") String fileName,
			@RequestParam("recordVideo") Boolean recordVideo, @RequestParam("format") String format,
			@RequestParam("appPath") String appPath, @RequestParam("connectionName") String connectionName,@RequestParam("pywinrecorder") Boolean pywinRecorder)
			throws Exception {
		logger.info("enter into desktop/record service");
		logger.info("request parameters ");
		logger.info("fileName = " + fileName);
		logger.info("recordVideo = " + recordVideo);
		logger.info("format = " + format);
		logger.info("connectionName = " + connectionName);
		logger.info("appPath = " + appPath);
		Response responseback = new Response();
		try {
			if(pywinRecorder) {
				logger.info("pywinauto recorder started...");
				try {
					try {
						String path = pywinAutoRecorderPath+"/pywinauto_recorder.py "+fileName;
						boolean exportToCI = false;
						logger.info(path);
						String command = "cmd /c python "+path;
					    Process p = Runtime.getRuntime().exec(command);
					    p.waitFor();
					    BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
					    BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
					          String line;
					          logger.info("----BRI----");
					        while ((line = bri.readLine()) != null) {
					        	if(line.contains("Cobot recording successful")) {
					        		exportToCI = true;
					        	}
					        	logger.info(line);
					          }
					          bri.close();
					          logger.info("----BRE----");
					          while ((line = bre.readLine()) != null) {
					        	  logger.info(line);
					          }
					          bre.close();
					          p.waitFor();
					          p.destroy();
						if(exportToCI) {
							//String installDir = System.getProperty("installdir");
							String pythonRecordedFilePath = "C:"+File.separator+"PywinAutoRecordedFiles"+File.separator+fileName+".py";
							
							File fileToBeModified = new File(pythonRecordedFilePath);
							String oldContent = "";
							BufferedReader reader = new BufferedReader(new FileReader(fileToBeModified));
							String content = reader.readLine();
							while (content != null)
							{
							        oldContent = oldContent + content + System.lineSeparator();
							        content = reader.readLine();
							}

							String newContent = oldContent+"\nprint(\"Playback successfully completed\")";
							FileWriter writer = new FileWriter(fileToBeModified);
							writer.write(newContent);
							reader.close();
							writer.close();
							ResponseEntity<String> response = null;
							MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<String, Object>();
							 String authStr = testBotUserId+":"+testBotPassword;
							 logger.info("authStr "+authStr);
							String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
							try {
								bodyMap.add("File", new FileSystemResource(pythonRecordedFilePath));
								bodyMap.add("connectionName", connectionName);
								bodyMap.add("scriptFileName", fileName+".py");
								bodyMap.add("format", format);
								bodyMap.add("recordVideo", recordVideo);
								bodyMap.add("status", "Success");
								bodyMap.add("appPath", appPath);
								bodyMap.add("userId", testBotUserId);
								 logger.info("bodyMap "+bodyMap);
								HttpHeaders headers = new HttpHeaders();
								headers.add("Authorization", "Basic " + base64Creds);
								headers.setContentType(MediaType.MULTIPART_FORM_DATA);
								org.springframework.http.HttpEntity<MultiValueMap<String, Object>> requestEntity = new org.springframework.http.HttpEntity<>(
										bodyMap, headers);

								RestTemplate restTemplate = new RestTemplate();
								
								String uploadWebRecordURL = cobotServer + Constants.CobotServerUrls.RECORDER_DESKTOP_PYWINAUTO_SCRIPT_WITHOUT_VIDEO;
								logger.info("uploadWebRecordURL "+uploadWebRecordURL);
								response = restTemplate.exchange(uploadWebRecordURL, HttpMethod.POST, requestEntity, String.class);
								if (response.getStatusCode() == HttpStatus.OK) {
									logger.info("success");
									try {
										boolean flag = fileExplorer.deleteFile(new File("C:"+File.separator+"PywinAutoRecordedFiles"), new File(fileName+".py"));
										if (flag)
											logger.info("Recorded file deleted successfully.");
										else {
											logger.info("Error while deleteing temp file");
										}
									} catch (Exception e) {
										logger.error("Error deleteting reorded files", e);
									}
								} else {
									responseback.setText("Unable to export file to CoBot server");
									responseback.setStatus(false);
								}
								System.out.println("Cobot Server response status: " + response.getStatusCode());
								System.out.println("Cobot Server response body: " + response.getBody());
							} catch (Exception e) {
								logger.info("ERROR >>> while export agent file to cobot server : " + e.getMessage());
								responseback.setText(e.getMessage());
								responseback.setStatus(false);
							}
							
//							MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<String, Object>();
//							
//							bodyMap.add("File", new FileSystemResource(pythonRecordedFilePath));
//							bodyMap.add("connectionName", connectionName);
//							bodyMap.add("scriptFileName", fileName);
//							bodyMap.add("format", format);
//							bodyMap.add("recordVideo", recordVideo);
//							bodyMap.add("status", "Success");
//							bodyMap.add("appPath", appPath);
//
//							logger.info("bodyMap = " + bodyMap);
//							HttpHeaders headers = new HttpHeaders();
//							headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//							org.springframework.http.HttpEntity<MultiValueMap<String, Object>> requestEntity = new org.springframework.http.HttpEntity<>(
//									bodyMap, headers);
//							RestTemplate restTemplate = new RestTemplate();
//							try {
//								String  postUrl = cobotServer + Constants.CobotServerUrls.RECORDER_DESKTOP_PYWINAUTO_SCRIPT_WITHOUT_VIDEO;;
//								ResponseEntity<String> response = restTemplate.exchange(postUrl, HttpMethod.POST, requestEntity,
//										String.class);
//								logger.info("response = " + response);
//								if (response.getStatusCode() == HttpStatus.OK) {
//									logger.info("success");
//									try {
//										boolean flag = fileExplorer.deleteFile(new File("C:"+File.separator+"PywinAutoRecordedFiles"), new File(fileName+".py"));
//										if (flag)
//											logger.info("Recorded file deleted successfully.");
//										else {
//											logger.info("Error while deleteing temp file");
//										}
//									} catch (Exception e) {
//										logger.error("Error deleteting reorded files", e);
//									}
//								} else {
//									responseback.setText("Unable to export file to CoBot server");
//									responseback.setStatus(false);
//								}
//								System.out.println("Cobot Server response status: " + response.getStatusCode());
//								System.out.println("Cobot Server response body: " + response.getBody());
//							} catch (Exception e) {
//								logger.info("ERROR >>> while export agent file to cobot server : " + e.getMessage());
//								responseback.setText(e.getMessage());
//								responseback.setStatus(false);
//							}
							responseback.setText("Success");
							responseback.setStatus(true);
						}else {
							responseback.setText("Unable to create python file");
							responseback.setStatus(false);
						}
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				logger.info("pywinauto recorder completed...");
			}else {
				startSahiPro();
				launchApp(appPath, fileName, connectionName, format);
			}
			
		} catch (ConnectException e) {
			try {
				Thread.sleep(5000);
				launchApp(appPath, fileName, connectionName, format);
			} catch (Exception e2) {
				throw new Exception(e2);
			}
		} catch (Exception e) {
			responseback.setText("Error Recording: " + e.getMessage());
			responseback.setStatus(false);
		}
		return responseback;
	}

	@SuppressWarnings("unused")
	@RequestMapping(value = "/desktop/execute", method = RequestMethod.POST)
	public @ResponseBody Response playBackDesktopScript(@RequestParam("JobId") String jobID,
			@RequestParam("CallBackURL") String callBackURL, @RequestParam("Body") String data,
			@RequestParam("FileName") String scriptFileName, @RequestParam("Format") String format,
			@RequestParam("RecordVideo") String recordVideo, @RequestParam("File") MultipartFile file,
			@RequestParam("AppName") String appName) throws Exception {
		logger.info("enter into desktop/execute service");
		logger.info("request parameters ");
		logger.info("fileName = " + scriptFileName);
		logger.info("recordVideo = " + recordVideo);
		logger.info("format = " + format);

		Response response = new Response();
		try {
			writeDesktopFileTo(file, scriptFileName, appName);
			startSahiPro();
			String[] status = executeScript(scriptFileName, data, appName, Boolean.parseBoolean(recordVideo), format);
			String videoPath = null;
			if (Boolean.parseBoolean(recordVideo)) {
				videoPath = status[1];
				videoPath = videoPath.replace("\\", "/");
				videoPath = videoPath.replace("//", "/");
				stopVideo();
			}
			if (StringUtils.equals(status[0], "SUCCESS")) {
				JsonObject inputJson = Json.createObjectBuilder().add("jobId", jobID).add("status", "Success")
						.add("reason", "").build();
				// sendExecutionStatus(callBackURL, inputJson.toString(), null);

				sendExecutionStatus(callBackURL, jobID, "Success", "", scriptFileName, videoPath, format, appName,
						Boolean.valueOf(recordVideo));
			} else {
				JsonObject inputJson = Json.createObjectBuilder().add("jobId", jobID).add("status", "Failed")
						.add("reason", "Unable to execute the script").build();
				sendExecutionStatus(callBackURL, jobID, "Failed", "Unable to execute the script", scriptFileName,
						videoPath, format, appName, Boolean.valueOf(recordVideo));
			}
		} catch (Exception e) {
			JsonObject inputJson = Json.createObjectBuilder().add("jobId", jobID).add("status", "Failed")
					.add("reason", "Unable to execute the script").build();
			sendExecutionStatus(callBackURL, jobID, "Failed", "Unable to execute the script", scriptFileName, null,
					format, appName, Boolean.valueOf(recordVideo));
		}
		return response;
	}

	@RequestMapping(value = "/cli/record", method = RequestMethod.POST)
	public @ResponseBody Response recordScript(@RequestParam("File") String fileName,
			@RequestParam("recordVideo") Boolean recordVideo, @RequestParam("format") String format,
			@RequestParam("connectionName") String connectionName, @RequestParam("appPath") String appPath,
			@RequestParam(name = "userIdVal", required = false) String userIdVal, 
			@RequestParam(name = "passwordVal", required = false) String passwordVal, 
			@RequestParam(name = "newPasswordVal", required = false) String newPasswordVal, 
			@RequestParam(name = "appId", required = false) String appId) {
		logger.info("enter into cli/record service");
		logger.info("request parameters ");
		logger.info("fileName = " + fileName);
		logger.info("recordVideo = " + recordVideo);
		logger.info("format = " + format);
		logger.info("connectionName = " + connectionName);
		Response response = new Response();
		cliRecorderPopup.showWindow();
		try {
			logger.info("Recording Started...");
			Status recordStatus = null;
			logger.info("OS NAME = " + Constants.OPERATING_SYSTEM_NAME);
			if (Constants.OPERATING_SYSTEM_NAME.startsWith(Constants.WINDOWS_OS)) {
				recordStatus = cliRecorder.record(fileName, recordVideo, format, connectionName, appPath, userIdVal, passwordVal, newPasswordVal, appId);
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

	@RequestMapping(value = "/job/createjson", method = RequestMethod.POST)
	public @ResponseBody String createJson(@RequestParam("content") String content,
			@RequestParam("search") String search, @RequestParam("headers") String headersStr,
			@RequestParam("fileName") String fileName, @RequestParam("conntype") String connectionType,
			@RequestParam("connectionName") String connectionName, @RequestParam("agentIp") String agentIp,
			@RequestParam("botUserId") String botUserId, @RequestParam("botPassword") String botPassword) {
		ResponseEntity<String> response = null;
		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<String, Object>();
		 String authStr = botUserId+":"+botPassword;
		String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
		try {
			bodyMap.add("content", content);
			bodyMap.add("search", search);
			bodyMap.add("headers", headersStr);
			bodyMap.add("fileName", fileName);
			bodyMap.add("conntype", connectionType.trim());
			bodyMap.add("connectionName", connectionName.trim());
			bodyMap.add("agentIp", agentIp);
			bodyMap.add("botUserId", botUserId);

			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Basic " + base64Creds);
			// headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			org.springframework.http.HttpEntity<MultiValueMap<String, Object>> requestEntity = new org.springframework.http.HttpEntity<>(
					bodyMap, headers);

			RestTemplate restTemplate = new RestTemplate();
			cobotServer = cobotServer.replaceAll("/$", "");
			String uploadWebRecordURL = cobotServer + Constants.CobotServerUrls.UPLOAD_WEB_SCRIPT;

			response = restTemplate.exchange(uploadWebRecordURL, HttpMethod.POST, requestEntity, String.class);
			if (response.getStatusCode() == HttpStatus.OK) {

				logger.info("Cobot Server response status: " + response.getStatusCode());
				logger.info("Cobot Server response body: " + response.getBody());
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (videoRecord.equalsIgnoreCase(Constants.VIDEO_RECORD_OFF)) {
				return Constants.FAIL + e.getMessage();
			}
		}
		if (videoRecord.equalsIgnoreCase(Constants.VIDEO_RECORD_ON)) {
			try {
				uploadWebRecordVideo(connectionName, fileName);
				return response.getBody();
			} catch (Exception e) {
				e.printStackTrace();
				return Constants.FAIL + e.getMessage();
			}
		} else {
			logger.info("Video recording is off mode.");
		}
		return response.getBody();
	}

	@SuppressWarnings("unused")
	@RequestMapping(value = "/video/start", method = RequestMethod.POST)
	public @ResponseBody Response startVideo(@RequestParam("File") String fileName,
			@RequestParam("format") String format, @RequestParam("project") String connectionName) {
		logger.info("enter into video/start service");
		Response response = new Response();
		logger.info("File = " + fileName + " format = " + format);
		try {
			if (videoRecord.equalsIgnoreCase(Constants.VIDEO_RECORD_ON)) {
				logger.info("checking record video path exists or not");

				scriptsRootDir = scriptsRootDir.replaceAll("/$", "");
				scriptsRootDir = scriptsRootDir.replaceAll("\\$", "");

				logger.info("executing the cmd.exe to opening the powershell ");
				if (Constants.OPERATING_SYSTEM_NAME.startsWith(Constants.WINDOWS_OS)) {
					startRecordingVideo(fileName, format, connectionName, Constants.RECORD_VIDEO_PATH);
					response.setText(Constants.SUCCESS);
				} else if (Constants.OPERATING_SYSTEM_NAME.startsWith(Constants.MAC_OS)) {
					Status status = macCLIRecorder.startVideo(fileName, connectionName, format);
					response.setText(Constants.SUCCESS);
				}
			} else {
				logger.info("Video recording is off mode.");
				response.setText(Constants.SUCCESS);
			}
			return response;
		} catch (Exception e) {
			response.setText(e.getMessage());
			return response;
		}
	}

	@SuppressWarnings("unused")
	@RequestMapping(value = "/video/stop", method = RequestMethod.POST)
	public @ResponseBody Response killProcesses(@RequestParam("File") String fileName,
			@RequestParam("project") String project, @RequestParam("type") String type,
			@RequestParam("exePath") String exePath) {
		Response response = new Response();
		logger.info("enter video/stop service");
		try {
			if (type.equals("cli")) {
				if (StringUtils.contains(exePath, Constants.MOBAXTERM)) {
					mobaXterm.stopMobaxRecording(exePath, scriptsRootDir, project, fileName);
				} else if (StringUtils.contains(exePath, Constants.BLUEZONE)) {
					bluezone.stopBluezoneRecording(exePath, scriptsRootDir, project, fileName);
				}

				cliRecorderPopup.closeWindow();

			}
			if (Constants.OPERATING_SYSTEM_NAME.startsWith(Constants.WINDOWS_OS)) {
				if (videoRecord.equalsIgnoreCase(Constants.VIDEO_RECORD_ON)) {
					Thread.sleep(30000);
					stopVideo();
				}
				response.setStatus(true);
				response.setText(Constants.SUCCESS);
				logger.info("powershell terminated successfully.");
			} else if (Constants.OPERATING_SYSTEM_NAME.startsWith(Constants.MAC_OS)) {
				Status status = macCLIRecorder.killProcess();
				response.setStatus(true);
				response.setText(Constants.SUCCESS);
			}

			return response;
		} catch (Exception e) {
			response.setStatus(false);
			response.setText(e.getMessage());
			return response;
		}
	}

	public void stopVideo() throws Exception {
		try {
			String line = "Powershell kill command is not yet executed";
			while (line != null) {
				String recordScript = sahiRootDir + Constants.SAHI_PRO_BIN_PATH + File.separator
						+ Constants.STOP_VIDEO_FILE;
				recordScript = recordScript.replace("\\", "/");
				recordScript = recordScript.replace("//", "/");
				sendSignalRootDir = sendSignalRootDir.replace("\\", "/");

				Runtime.getRuntime().exec("\"" + recordScript + "\" \"" + sendSignalRootDir + "/SendSignal.exe\"");
				Thread.sleep(5000);

				Process p = Runtime.getRuntime().exec("cmd /c tasklist | find /i \"powershell.exe\"");
				p.waitFor();
				BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				line = reader.readLine();
				if (line != null) {
					Thread.sleep(2000);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	private void uploadWebRecordVideo(String project, String recordedFile) {
		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<String, Object>();
		scriptsRootDir = scriptsRootDir.replaceAll("/$", "");
		scriptsRootDir = scriptsRootDir.replaceAll("\\$", "");
		String uploadVideoDirPath = scriptsRootDir + File.separator + project + File.separator
				+ Constants.RECORD_VIDEO_PATH;

		uploadVideoDirPath = uploadVideoDirPath.replace("\\", "/");
		File uploadVideoDir = new File(uploadVideoDirPath);
		recordedFile = recordedFile.replace(".java", "");

		File[] listOfFiles = uploadVideoDir.listFiles();
		File recordedVideoFile = null;
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()
					&& listOfFiles[i].getName().toLowerCase().contains(recordedFile.toLowerCase())) {
				recordedVideoFile = listOfFiles[i];
				break;
			}
		}

		try {

			if (recordedVideoFile != null && cobotServer != null) {
				String authStr = testBotUserId+":"+testBotPassword;
				String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
				bodyMap.add("FileVideo", new FileSystemResource(recordedVideoFile));

				bodyMap.add("project", project);

				HttpHeaders headers = new HttpHeaders();
				headers.add("Authorization", "Basic " + base64Creds);
				headers.setContentType(MediaType.MULTIPART_FORM_DATA);
				org.springframework.http.HttpEntity<MultiValueMap<String, Object>> requestEntity = new org.springframework.http.HttpEntity<>(
						bodyMap, headers);

				RestTemplate restTemplate = new RestTemplate();
				cobotServer = cobotServer.replaceAll("/$", "");
				String uploadWebRecordURL = cobotServer + Constants.CobotServerUrls.UPLOAD_RECORD_WEB_VIDEO;

				ResponseEntity<String> response = restTemplate.exchange(uploadWebRecordURL, HttpMethod.POST,
						requestEntity, String.class);
				if (response.getStatusCode() == HttpStatus.OK) {
					try {

						Files.deleteIfExists(Paths.get(recordedVideoFile.getAbsolutePath()));
						if (uploadVideoDir.exists() && uploadVideoDir.list().length == 0) {
							Files.deleteIfExists(Paths.get(uploadVideoDir.getAbsolutePath()));
						}

					} catch (IOException e) {
						logger.error("Error deleteting reorded files", e);
					}

					logger.info("Cobot Server response status: " + response.getStatusCode());
					logger.info("Cobot Server response body: " + response.getBody());
				}
				logger.info("response = " + response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		AgentController controller = new AgentController();

		controller.launchcmd();

	}

	@RequestMapping(value = "/checkAgentConnection", method = RequestMethod.GET)
	public @ResponseBody String checkSSMConnection() {
		try {
			if (Constants.OPERATING_SYSTEM_NAME.startsWith(Constants.WINDOWS_OS)) {
				//cliRecorderPopup.closeWindow();
			} else if (Constants.OPERATING_SYSTEM_NAME.startsWith(Constants.MAC_OS)) {

			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return Constants.SUCCESS;
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/getScriptName", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> getScriptName() {
		return variableMap;
	}

	@SuppressWarnings("unused")
	public void startSahiPro() throws Exception {
		System.out.println("startSahiPro method started...");
		ProcessBuilder processBuilder = new ProcessBuilder();
		List<String> pidList = runProcess();
		System.out.println(
				"cmd /c start /min /wait cmd.exe  /k \"cd " + sahiRootDir + "/userdata/bin && start_sahi.bat\"");
		Runtime.getRuntime()
				.exec("cmd /c start /min /wait cmd.exe  /k \"cd " + sahiRootDir + "/userdata/bin && start_sahi.bat\"");

		pidList = null;
		while (pidList == null) {
			Thread.sleep(2000);
			pidList = runProcess();
			System.out.println("pidList==> " + pidList);
			System.out.println("pidList.size()==> " + pidList.size());
			System.out.println("pidList.size() == 0 ==> " + (pidList.size() == 0));
			if (pidList.size() == 0) {
				pidList = null;
			}
		}
		System.out.println("startSahiPro method end...");
	}

	public void launchApp(String appPath, String fileName, String connectionName, String format) throws Exception {
		System.out.println("launchapp method started..");
		Files.deleteIfExists(Paths.get(scriptsRootDir + "/launchApp.sah"));
		FileWriter writer = new FileWriter(scriptsRootDir + "/launchApp.sah");
		writer.write("_execute(\"" + appPath.replace("\\", "/") + "\", true, 5000);");
		writer.flush();
		writer.close();

		variableMap = new HashMap<String, String>();
		variableMap.put("appPath", appPath.replace("\\", "/"));
		variableMap.put("format", format);
		variableMap.put("fileName", fileName);
		variableMap.put("connectionName", connectionName);

		closeApp(appPath);
		System.out.println("launching browser...");
		String testcaseName = scriptsRootDir + "/launchApp.sah";
		SuiteInfo suiteInfo = new SuiteInfo();
		suiteInfo.setSuitePath(testcaseName);
		suiteInfo.setHost("localhost");
		suiteInfo.setPort("9999");
		suiteInfo.setThreads("1");
		suiteInfo.setStartWith("WINDOWS");

		TestRunner runner = new TestRunner(suiteInfo);
		runner.execute();

		System.out.println("sucessfully launched.");
		Files.deleteIfExists(Paths.get(scriptsRootDir + "/launchApp.sah"));
		if (videoRecord.equalsIgnoreCase(Constants.VIDEO_RECORD_ON)) {
			startRecordingVideo(fileName, format, connectionName, Constants.RECORD_VIDEO_PATH);
		}

		Files.deleteIfExists(Paths.get(sahiRootDir + "/userdata/scripts/"
				+ (StringUtils.endsWith(fileName, ".sah") ? fileName : fileName + ".sah")));
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			Desktop.getDesktop().browse(new URI(sahiRecorderUrl));
		}
	}

	public void closeApp(String appPath) throws Exception {
		appPath = appPath.replace("\\", "/");
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command("cmd.exe", "/c",
				"taskkill /im " + appPath.substring(appPath.lastIndexOf("/") + 1, appPath.length()));
		processBuilder.start();
		Thread.sleep(2000);
	}

	public List<String> runProcess() throws IOException {
		List<String> pidList = new ArrayList<String>();
		try {
			System.out.println("cmd => cmd.exe  /k netstat -aon | findstr 9999");
			Process process = Runtime.getRuntime().exec("cmd /c netstat -aon | findstr 9999");
			process.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = reader.readLine();
			System.out.println("line => " + line);
			while (line != null) {
				if (line.contains("LISTENING") && !pidList.contains(line.split("LISTENING")[1].trim())) {
					pidList.add(line.split("LISTENING")[1].trim());
				} else if (line.contains("ESTABLISHED") && !pidList.contains(line.split("ESTABLISHED")[1].trim())) {
					pidList.add(line.split("ESTABLISHED")[1].trim());
				}
				System.out.println("line_865 => " + line);
				line = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			System.out.println("Exception => " + e.getMessage());
		}
		return pidList;
	}

	@SuppressWarnings("unchecked")
	public String[] executeScript(String fileName, String content, String appName, boolean recordVideo, String format)
			throws Exception {
		String rootPath = env.getProperty("scripts.dir");
		String recorderFilePath = rootPath + File.separator + appName;
		recorderFilePath = recorderFilePath.replace("\\", "/");
		File dir = new File(recorderFilePath + File.separator);

		// Create the file on server
		String tcName = dir.getAbsolutePath() + File.separator + fileName + Constants.DOTSAH;
		String tempTcFileName = dir.getAbsolutePath() + File.separator + fileName + "_temp" + Constants.DOTSAH;

		String oldContent = "";
		BufferedReader reader = new BufferedReader(new FileReader(tcName));
		String line = reader.readLine();
		closeApp(StringUtils.substringBetween(line, "(", ","));
		while (line != null) {
			String waitstmt = "";
			if (!StringUtils.startsWith(line, "_execute") && !StringUtils.startsWith(line, "_setMode")
					&& !StringUtils.startsWith(line, "_selectWindow")) {
				waitstmt = StringUtils.substringBetween(line, "(", "\")") == null ? ""
						: "_wait(60000, _isVisible(" + StringUtils.substringBetween(line, "(", "\")") + "\")));"
								+ System.lineSeparator();
			}
			line = waitstmt + line.replace("{{", "$").replace("}}", "");
			oldContent = oldContent + line + System.lineSeparator();
			line = reader.readLine();
		}
		reader.close();

		FileWriter writer = new FileWriter(tempTcFileName);
		writer.write(oldContent);
		writer.close();

		HashMap<String, Object> desktopParams = new ObjectMapper().readValue(content, HashMap.class);
		HashMap<String, Object> variableHashMap = new HashMap<String, Object>();

		SuiteInfo suiteInfo = new SuiteInfo();
		suiteInfo.setSuitePath(tempTcFileName);
		suiteInfo.setHost("localhost");
		suiteInfo.setPort("9999");
		suiteInfo.setThreads("5");
		suiteInfo.setStartWith("WINDOWS");
		String userDefinedId = Utils.generateId();
		suiteInfo.setUserDefinedId(userDefinedId);

		TestRunner runner = new TestRunner(suiteInfo);
		for (String key : desktopParams.keySet()) {
			variableHashMap.put("$" + key, desktopParams.get(key));
		}

		if (!MapUtils.isEmpty(variableHashMap)) {
			runner.setInitJS(variableHashMap);
		}

		String videoFileName = "";
		if (recordVideo) {
			videoFileName = startRecordingVideo(fileName, format, appName, Constants.PLAYBACK_VIDEOS_PATH);
		}

		String status = runner.execute();
		String[] response = new String[2];
		fileExplorer.deleteFile(new File(dir.getAbsolutePath()), new File(tempTcFileName));
		if (!StringUtils.equalsIgnoreCase("SUCCESS", status)) {
			ArrayList<ArrayList<String>> result = getErrorMessage(userDefinedId);
			response[0] = result.toString();
			response[1] = videoFileName;
			if (recordVideo) {
				Thread.sleep(30000);
				stopVideo();
				Thread.sleep(10000);
			}
			return response;
		} else {
			response[0] = status;
			response[1] = videoFileName;
			if (recordVideo) {
				Thread.sleep(30000);
				stopVideo();
				Thread.sleep(10000);
			}
			return response;
		}
	}

	private ArrayList<ArrayList<String>> getErrorMessage(String userDefinedId) throws Exception {
		Configuration.init(sahiRootDir, sahiRootDir + "/userdata/");
		ReportDB instance = ReportDB.getInstance();

		ArrayList<ArrayList<String>> result = instance
				.selectAs2DList("select StepReports.StepMessage, StepReports.FailureMessage from SuiteReports "
						+ "inner join ScriptReports on SuiteReports.SuiteReportId = ScriptReports.SuiteReportId "
						+ "inner join StepReports on StepReports.ScriptReportId = ScriptReports.ScriptReportId "
						+ "where UserDefinedId = '" + userDefinedId
						+ "' and (StepReports.Messagetype like 'ERROR' or StepReports.Messagetype like 'Failure')");
		return result;
	}

	public void sendExecutionStatus(String callbackUrl, String JobId, String status, String reason,
			String scriptFileName, String videoPath, String format, String appName, boolean recordVideo)
			throws Exception {
		
		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<String, Object>();
		String authStr = testBotUserId+":"+testBotPassword;
		String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
		if (videoPath != null && !videoPath.isEmpty() && recordVideo) {
			bodyMap.add("FileVideo", new FileSystemResource(videoPath));
		}

		bodyMap.add("FileName", scriptFileName);
		bodyMap.add("Format", format);
		bodyMap.add("JobId", JobId);
		bodyMap.add("status", status);
		bodyMap.add("reason", reason);
		bodyMap.add("appName", appName);

		logger.info("bodyMap = " + bodyMap);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		org.springframework.http.HttpEntity<MultiValueMap<String, Object>> requestEntity = new org.springframework.http.HttpEntity<>(
				bodyMap, headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = null;
		try {
			logger.info("callbackUrl = " + callbackUrl);
			response = restTemplate.exchange(callbackUrl, HttpMethod.POST, requestEntity, String.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				try {
					boolean flag = fileExplorer.deleteDirectory(new File(scriptsRootDir + appName));
					if (flag)
						logger.info("Agent Files and Folder deleted successfully.");
					else {
						logger.info("Error while deleteing agent files");
					}
				} catch (Exception e) {
					logger.error("Error deleteting agent playback files", e);
				}
			}
		} catch (Exception e) {
			logger.info("response = " + response);
			logger.info("ERROR >>> while pushing cli play back files from agent to server = " + e.getMessage());
		}

		System.out.println("Cobot Server response status: " + response.getStatusCode());
		System.out.println("Cobot Server response body: " + response.getBody());

		logger.info("job " + JobId + " is  completed");
	}

	public String startRecordingVideo(String fileName, String format, String appName, String videoRecordingPath)
			throws Exception {
		logger.info("Start recording video method started");
		stopVideo();
		String recordFileName = fileName + "_" + new Date().getTime() + format;
		if (!new File(scriptsRootDir + File.separator + appName).exists()) {
			new File(scriptsRootDir + File.separator + appName).mkdirs();
		}
		String recordFilePath = scriptsRootDir + File.separator + appName + File.separator + videoRecordingPath;
		if (!new File(recordFilePath).exists()) {
			new File(recordFilePath).mkdirs();
		}
		recordFilePath = recordFilePath.replace("\\", "/").replace("//", "/");

		logger.info("executing the powershell.exe to open the ffmpeg for recording the video");
		Runtime.getRuntime().exec(Constants.START_VIDEO_RUNTIME_SCRIPT + recordFilePath + "/" + recordFileName);

		double size = 0;
		while (size == 0) {
			File videoFile = new File(recordFilePath + "/" + recordFileName);
			size = videoFile.length();
			logger.info("Video recording is not started yet");
		}
		logger.info("Video recording is started...");
		return recordFilePath + "/" + recordFileName;
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "/desktop/pywin/execute", method = RequestMethod.POST)
	public @ResponseBody Response playBackDesktopPywinScript(@RequestParam("JobId") String jobID,
			@RequestParam("CallBackURL") String callBackURL, @RequestParam("Body") String data,
			@RequestParam("FileName") String scriptFileName, @RequestParam("Format") String format,
			@RequestParam("RecordVideo") String recordVideo, @RequestParam("File") MultipartFile file,
			@RequestParam("AppName") String appName,@RequestParam("appPath") String appPath) throws Exception {
		logger.info("enter into desktop/execute service");
		logger.info("request parameters ");
		logger.info("fileName = " + scriptFileName);
		logger.info("recordVideo = " + recordVideo);
		logger.info("format = " + format);

		Response response = new Response();
		try {
			writeDesktopFileTo(file, scriptFileName, appName);
			String[] status= new String[2];
			try {
				String rootPath = env.getProperty("scripts.dir");
				String recorderFilePath = rootPath + File.separator + appName;
				recorderFilePath = recorderFilePath.replace("\\", "/");
				File dir = new File(recorderFilePath + File.separator);
				String path = dir.getAbsolutePath() + File.separator + file.getOriginalFilename();
				boolean exportToCI = false;
				logger.info(path);
				String command = "cmd /c python "+path;
			    Process p = Runtime.getRuntime().exec(command);
			    p.waitFor();
			    BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
			    BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			          String line;
			          logger.info("----BRI----");
			        while ((line = bri.readLine()) != null) {
			        	if(line.contains("Cobot recording successful")) {
			        		exportToCI = true;
			        	}
			        	logger.info(line);
			        	if(line.contains("Playback successfully completed")) {
			        		status[0]="SUCCESS";
			        	}else {
			        		status[0]="FAIL";
			        		status[1]=line;
			        	}
			          }
			          bri.close();
			          logger.info("----BRE----");
			          while ((line = bre.readLine()) != null) {
			        	  logger.info(line);
			          }
			          bre.close();
			          p.waitFor();
			          p.destroy();

			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			String videoPath = null;
			
			if(status[0]!=null) {
				logger.info("Job Status -"+status[0]);
				logger.info("Job Reason -"+status[1]);
				if (StringUtils.equals(status[0], "SUCCESS")) {
					JsonObject inputJson = Json.createObjectBuilder().add("jobId", jobID).add("status", "Success")
							.add("reason", "").build();
					// sendExecutionStatus(callBackURL, inputJson.toString(), null);

					sendExecutionStatus(callBackURL, jobID, "Success", "", scriptFileName, videoPath, format, appName,
							Boolean.valueOf(recordVideo));
				} else {
					if(status[1]!=null) {
						sendExecutionStatus(callBackURL, jobID, "Failed", status[1], scriptFileName,
								videoPath, format, appName, Boolean.valueOf(recordVideo));
					}
					
				}
			}
			
		} catch (Exception e) {
			JsonObject inputJson = Json.createObjectBuilder().add("jobId", jobID).add("status", "Failed")
					.add("reason", "Unable to execute the script").build();
			sendExecutionStatus(callBackURL, jobID, "Failed", "Unable to execute the script", scriptFileName, null,
					format, appName, Boolean.valueOf(recordVideo));
		}
		return response;
	}
}
