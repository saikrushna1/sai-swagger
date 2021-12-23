/**
 * 
 */
package com.saviynt.identitybot.mapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saviynt.identitybot.Util.Constants;
import com.saviynt.identitybot.Util.FileExplorer;
import com.saviynt.identitybot.Util.S3Explorer;
import com.saviynt.identitybot.model.BotAgent;
import com.saviynt.identitybot.response.BotAgentsResponse;

/**
 * @author venkat
 *
 */
@Component
public class JobUIMapper {
	final static Logger logger = Logger.getLogger(JobUIMapper.class);

	@Value("${bot.agent.status}")
	Integer status;
	
	@Value("${scripts.dir}")
	String scriptsRootDir;
	
	@Value("${testcases.root}")
	String testCaseRoot;
	
	@Value("${scripts.store}")
	String scriptsStore;
	
	@Autowired
	S3Explorer s3explorer;
	
	@Autowired
	FileExplorer fileExplorer;
	
	public List<BotAgentsResponse> updateStatus(List<BotAgent> botAgentsList) {
		List<BotAgentsResponse> responseList = new ArrayList<BotAgentsResponse>();
		for (BotAgent botAgent : botAgentsList) {
			logger.info("Agent Id =" + botAgent.getAgentId());
			logger.info("LastUpdatedTimestamp =" + botAgent.getLastUpdatedTimestamp());
			SimpleDateFormat format = new SimpleDateFormat(Constants.DATEFORMAT);
			Date currentDateTime = new Date();
			logger.info("currentDateTime =" + currentDateTime);
			Date pastDate = null;
			Date currentDate = null;

			try {
				pastDate = format.parse(botAgent.getLastUpdatedTimestamp().toString());
				currentDate = format.parse(format.format(currentDateTime));

				logger.info("pastDate= " + pastDate.getTime());
				logger.info("currentDate = " + currentDate.getTime());

				// in milliseconds
				long diff = currentDate.getTime() - pastDate.getTime();

				//long diffHours = diff / (60 * 60 * 1000) % 24;
				long diffMinutes = (diff / 1000) / 60;
				long diffDays = diff / (24 * 60 * 60 * 1000);

				logger.info(diffDays + " days, ");
				logger.info(diffMinutes + " Minutes, ");

				BotAgentsResponse response = new BotAgentsResponse();
				response.setSno(botAgent.getSno());
				response.setAgentName(botAgent.getAgentName());
				response.setAgentId(botAgent.getAgentId());
				response.setIpaddress(botAgent.getIpaddress());
				response.setLastUpdatedTimestamp(botAgent.getLastUpdatedTimestamp());
				if (diffDays == 0 && diffMinutes < status) {
					response.setStatus(Constants.ACTIVE);
				} else {
					InetAddress geek = InetAddress.getByName(botAgent.getIpaddress()); 
				    if (geek.isReachable(5000)) 
				    	response.setStatus(Constants.INACTIVE);
				    else
				    	response.setStatus(Constants.MACHINE_IS_NOT_UP);
				}
				responseList.add(response);
			} catch (Exception e) {
				logger.error("ERROR >>> white updating the bot agent status, ", e);
			}
		}
		return responseList;
	}

	public JsonArray updateStatusJson(List<BotAgent> botAgentsList) {
		JsonArray botAgents = new JsonArray();
		for (BotAgent botAgent : botAgentsList) {
			logger.info("Agent Id =" + botAgent.getAgentId());
			logger.info("LastUpdatedTimestamp =" + botAgent.getLastUpdatedTimestamp());
			SimpleDateFormat format = new SimpleDateFormat(Constants.DATEFORMAT);
			Date currentDateTime = new Date();
			logger.info("currentDateTime =" + currentDateTime);
			Date pastDate = null;
			Date currentDate = null;

			try {
				pastDate = format.parse(botAgent.getLastUpdatedTimestamp().toString());
				currentDate = format.parse(format.format(currentDateTime));

				logger.info("pastDate= " + pastDate.getTime());
				logger.info("currentDate = " + currentDate.getTime());

				// in milliseconds
				long diff = currentDate.getTime() - pastDate.getTime();

				//long diffHours = diff / (60 * 60 * 1000) % 24;
				long diffMinutes = (diff / 1000) / 60;
				long diffDays = diff / (24 * 60 * 60 * 1000);

				logger.info(diffDays + " days, ");
				logger.info(diffMinutes + " Minutes, ");

				BotAgentsResponse response = new BotAgentsResponse();
				JsonObject obj = new JsonObject();
				obj.addProperty(Constants.SNO, String.valueOf(botAgent.getSno()));
				obj.addProperty(Constants.AGENT_NAME, String.valueOf(botAgent.getAgentName()));
				obj.addProperty(Constants.AGENT_ID, String.valueOf(botAgent.getAgentId()));
				obj.addProperty(Constants.IPADDRESS, String.valueOf(botAgent.getIpaddress()));
				obj.addProperty(Constants.LASTUPDATEDTIMESTAMP, String.valueOf(botAgent.getLastUpdatedTimestamp()));
				
				response.setSno(botAgent.getSno());
				response.setAgentName(botAgent.getAgentName());
				response.setAgentId(botAgent.getAgentId());
				response.setIpaddress(botAgent.getIpaddress());
				response.setLastUpdatedTimestamp(botAgent.getLastUpdatedTimestamp());
				if (diffDays == 0 && diffMinutes < status) {
					obj.addProperty(Constants.STATUS, Constants.ACTIVE);
				} else {
					
					InetAddress geek = InetAddress.getByName(botAgent.getIpaddress()); 
				    if (geek.isReachable(5000)) 
				    	obj.addProperty(Constants.STATUS, Constants.INACTIVE);
				    else
				    	obj.addProperty(Constants.STATUS, Constants.MACHINE_IS_NOT_UP);
				}
				botAgents.add(obj);
			} catch (Exception e) {
				logger.error("ERROR >>> white updating the bot agent status, ", e);
			}
		}
		return botAgents;
	}

	public LinkedHashMap<String, LinkedHashMap<String, List<String>>> prepareScriptTypes() {

		LinkedHashMap<String, LinkedHashMap<String, List<String>>> filesList = new LinkedHashMap<String, LinkedHashMap<String, List<String>>>();
		LinkedHashMap<String, List<String>> cliFiles = null ;
		LinkedHashMap<String, List<String>> webFiles = null ;
		LinkedHashMap<String, List<String>> desktopFiles = null ;
		if(scriptsStore.equalsIgnoreCase(Constants.SCRIPT_STORE_LOCAL)) {
			cliFiles = fileExplorer.findFiles(scriptsRootDir, Constants.CLI);// + Constants.RECORDED_SCRIPTS_PATH
			webFiles = fileExplorer.findFiles(scriptsRootDir, Constants.WEB);
			desktopFiles = fileExplorer.findFiles(scriptsRootDir, Constants.DESKTOP);
		} /*
			 * else if(scriptsStore.equalsIgnoreCase(Constants.S3_BUCKET)) { try { cliFiles
			 * = s3explorer.listObjectKeys(scriptsRootDir, Constants.RECORDED_SCRIPTS_PATH);
			 * webFiles = s3explorer.listObjectKeys(scriptsRootDir, ""); } catch (Exception
			 * e) { logger.error("ERROR >>> while reading files from s3 bucket. "+e); } }
			 */
		
		logger.info("CLI Files = "+cliFiles);
		logger.info("WEB Files = "+webFiles);
		if(webFiles.size()>0)
		filesList.put(Constants.WEB, webFiles);
		if(cliFiles.size()>0)
		filesList.put(Constants.CLI, cliFiles);
		if(desktopFiles.size()>0)
		filesList.put(Constants.DESKTOP, desktopFiles);
		try {
			LinkedHashMap<String, List<String>> commonFiles = new LinkedHashMap<String, List<String>>();
			List<String> commonFile = new ArrayList<String>();
			File commonMethodsFile = new File(scriptsRootDir+Constants.COMMON_FOLDER+File.separator+Constants.COMMON_METHOD_FILE);
			if(commonMethodsFile.exists()) {
				commonFile.add(Constants.COMMON_METHOD_FILE);
				commonFiles.put(Constants.COMMON_FOLDER, commonFile);
				filesList.put(Constants.COMMON, commonFiles);
			}else {
				File source = new File(getClass().getClassLoader().getResource(Constants.COMMON_METHOD_FILE).getFile());
				File dest = new File(scriptsRootDir+Constants.COMMON_FOLDER+File.separator+Constants.COMMON_METHOD_FILE);
				if (dest.getParentFile().mkdir()) {
					dest.createNewFile();
				} else {
				    throw new IOException("Failed to create directory " + dest.getParent());
				}
				boolean copingFileToLocal = copyFileUsingStream(source,dest);
				if(copingFileToLocal) {
					commonFile.add(Constants.COMMON_METHOD_FILE);
					commonFiles.put(Constants.COMMON_FOLDER, commonFile);
					filesList.put(Constants.COMMON, commonFiles);
				}else {
					logger.error("common file not copied");
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			logger.error(e);
		}
		return filesList;
	}

	public static boolean copyFileUsingStream(File source, File dest) throws IOException {
		boolean statusFlag = false;
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	        statusFlag = true;
	    }catch (Exception e) {
			// TODO: handle exception
		} finally {
	        is.close();
	        os.close();
	    }
	    return statusFlag;
	}

}
