package com.saviynt.identitybot.agent.util;


import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.sikuli.hotkey.Keys;
import org.sikuli.script.App;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saviynt.identitybot.agent.constants.Constants;
import com.saviynt.identitybot.agent.constants.STATUS;
import com.saviynt.identitybot.agent.response.Status;

@Service
public class BlueZone {
	
	@Value("${agent.image.path}")
	String bluezoneImagePath;
	
	final static Logger logger = LoggerFactory.getLogger(BlueZone.class);
	
	public Status navigationSetUp(String exePath, String userIdVal, String passwordVal, String newPasswordVal, String appId) {
		Status status = new Status();
		try {
			String resolutionImagespath = getResolutionImagespath();
			logger.info("Resolution images path "+resolutionImagespath);
			App app = new App(exePath);
			app.open();
			Thread.sleep(5000);
			Screen screen = new Screen();
			Pattern zosIcon = new Pattern(bluezoneImagePath + "/" + resolutionImagespath+"/zos.PNG");
			screen.wait(zosIcon, 30);
			screen.click(zosIcon);
			
			Pattern userid = new Pattern(bluezoneImagePath + "/" + resolutionImagespath+"/userid.PNG");
			screen.wait(userid, 30);
			screen.click(userid);
			
			Pattern cursor = new Pattern(bluezoneImagePath + "/" + resolutionImagespath+"/cursor.PNG");
			screen.wait(cursor, 30);
			screen.type(cursor, userIdVal);

			Pattern password = new Pattern(bluezoneImagePath + "/" + resolutionImagespath+"/password.PNG");
			screen.wait(password, 30);
			screen.click(password);
			
			screen.wait(cursor, 30);
			screen.type(cursor, passwordVal);
			
			if(!StringUtils.isEmpty(newPasswordVal)) {
				Pattern newPassword = new Pattern(bluezoneImagePath + "/" + resolutionImagespath+"/newpassword.PNG");
				screen.wait(newPassword, 30);
				screen.click(newPassword);
			}
			
			screen.wait(cursor, 30);
			screen.type(cursor, newPasswordVal);

			Pattern application = new Pattern(bluezoneImagePath + "/" + resolutionImagespath+"/application.PNG");
			screen.wait(application, 30);
			screen.click(application);
			
			screen.wait(cursor, 30);
			screen.type(cursor, appId + Keys.ENTER);
			
			status.setJobId(String.valueOf(STATUS.Success.getID()));
		}catch (Exception e) {
			status.setJobId(String.valueOf(STATUS.Failed.getID()));
			status.setReason(e.getMessage());
			e.printStackTrace();
		}
		return status;
	}
	
	public void stopBluezoneRecording(String exePath,String scriptsRootDir,String connectionName,String fileName) throws Exception {
		bluezoneImagePath  = bluezoneImagePath.replace("\\", "//");
		String resolutionImagespath = getResolutionImagespath();
		logger.info("Resolution images path "+resolutionImagespath);
		File txtFileDir = new File(scriptsRootDir + connectionName + File.separator + Constants.RECORDED_SCRIPTS_PATH);
		if(!txtFileDir.exists()) {
			txtFileDir.mkdirs();
		}
		scriptsRootDir = txtFileDir +File.separator+ fileName+Constants.DOTTXT;
		scriptsRootDir = scriptsRootDir.replace("\\", "/");
		logger.info("Bluezone generated txt file location : "+scriptsRootDir);
		
		App app = new App(exePath);
		app.open();
		Screen screen = new Screen();
		Pattern commandObject = new Pattern(bluezoneImagePath + "/"+resolutionImagespath+"/Command.PNG");
		screen.type(commandObject, "doskey /HISTORY > " + scriptsRootDir + Keys.ENTER);
		Thread.sleep(5000);
		
		BufferedReader br = new BufferedReader(new FileReader(new File(scriptsRootDir)));
		String oldContent = "";
		String line;
		while ((line = br.readLine()) != null) {
			oldContent = oldContent + line + System.lineSeparator();
		}
		oldContent = oldContent + exePath;
	
		FileWriter writer = new FileWriter(scriptsRootDir);
		writer.write(oldContent);
		br.close();
		writer.close();
		app.close();
	}
	
	public Status executeBluezonScript(String fileName, String params, String userIdVal, String passwordVal, String newPasswordVal, String appId) {
		Status status = new Status();
		try {
			bluezoneImagePath  = bluezoneImagePath.replace("\\", "//");
			String resolutionImagespath = getResolutionImagespath();
			logger.info("Resolution images path "+resolutionImagespath);
			String appPath = "";
			String line;
			Screen screen = new Screen();
			Pattern commandObject = new Pattern(bluezoneImagePath + "/"+resolutionImagespath+"/Command.PNG");
			BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
			String oldContent = "";
			while ((line = br.readLine()) != null) {
				oldContent = oldContent + line + System.lineSeparator();
				if(StringUtils.contains(line, "doskey /HISTORY")) {
					appPath = br.readLine();
				}
			}
			logger.info("preparing the cli params from job content");
			Map<String, Object> cliParams = new ObjectMapper().readValue(params, HashMap.class);
			logger.info("prepared cliparams = " + cliParams);

			logger.info("adding cli params to olcContent");
			for (String key : cliParams.keySet()) {
				oldContent = StringUtils.replace(oldContent, "{{" + key + "}}", cliParams.get(key).toString());
			}
			FileWriter writer = new FileWriter(fileName);
			writer.write(oldContent);
			writer.close();
			
			br.close();
			navigationSetUp(appPath, userIdVal, passwordVal, newPasswordVal, appId);
			
			br = new BufferedReader(new FileReader(new File(fileName)));
			while ((line = br.readLine()) != null) {
				if(!StringUtils.contains(line, "doskey /HISTORY")) {
					screen.wait(commandObject, 20);
					screen.type(commandObject, line + Keys.ENTER);
				} else {
					br.readLine();
				}
			}
			br.close();
			App app = new App(appPath);
			app.close();
			status.setJobId(String.valueOf(STATUS.Success.getID()));
		}catch (Exception e) {
			status.setJobId(String.valueOf(STATUS.Failed.getID()));
			status.setReason(e.getMessage());
			e.printStackTrace();
		}
		return status;
	}
	
	public String getResolutionImagespath() {
		String resolutionPath ="";
		int screenSize = Toolkit.getDefaultToolkit().getScreenResolution();
		if(1<=screenSize && screenSize<=100) {
			resolutionPath = "100";
		}else if(101<=screenSize && screenSize<=125) {
			resolutionPath = "125";
		}else if(126<=screenSize && screenSize<=150) {
			resolutionPath = "150";
		}else if(151<=screenSize && screenSize<=175) {
			resolutionPath = "175";
		}
		return resolutionPath;
	}
}
