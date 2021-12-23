package com.saviynt.identitybot.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


import com.saviynt.identitybot.Util.Constants;
import com.saviynt.identitybot.mapper.JobUIMapper;
import com.saviynt.identitybot.model.BotAgent;
import com.saviynt.identitybot.model.BotScripts;
import com.saviynt.identitybot.model.Job;
import com.saviynt.identitybot.model.Suite;
import com.saviynt.identitybot.repository.BotAgentRepository;
import com.saviynt.identitybot.repository.BotScriptsRepository;
import com.saviynt.identitybot.repository.JobRepository;
import com.saviynt.identitybot.repository.SuiteRepository;
import com.saviynt.identitybot.response.BotAgentsResponse;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@Controller
@RequestMapping("/ui/")
public class JobUIController {
	final static Logger logger = Logger.getLogger(JobUIController.class);

	@Autowired
	JobRepository jobRepository;
	
	@Autowired
	BotScriptsRepository  botScriptsRepository;

	@Autowired
	BotAgentRepository botAgentRepository;

	@Autowired
	JobUIMapper mapper;
	
	@Autowired
	SuiteRepository suiteRepository;
	
	@Value("${scripts.dir}")
	String scriptsRootDir;
	
	
	
	

	@GetMapping("jobs")
	public String showAllJobs(Model model) {
		logger.info("get all jobs");
		List<Job> jobs = jobRepository.findAllByCreatedAtDesc();
		model.addAttribute("jobs", jobs);
		model.addAttribute("url", "jobs");
		return "jobs";
	}
	
	
	
	
	@GetMapping("projectDetails")
	public String porjectDetails(Model model) {
		logger.info("project details");
		return "projectdetails";
	}
	
			

	@GetMapping("botAgents")
	public String showAllBotAgents(Model model) {
		logger.info("get all bot agents");
		List<BotAgent> botAgentsList = botAgentRepository.findAllByTimestampDesc();
		List<BotAgentsResponse> responseList = new ArrayList<BotAgentsResponse>();
		if (botAgentsList != null && botAgentsList.size() > 0) {
			responseList = mapper.updateStatus(botAgentsList);
		}
		model.addAttribute("responseList", responseList);
		model.addAttribute("url", "botAgent");
		return "botagents";
	}

	@GetMapping("scripts")
	public String showAllScripts(Model model) {
		logger.info("get all scripts");
		List<BotScripts> scripts = botScriptsRepository.findAll();
		model.addAttribute("scripts", scripts);
		model.addAttribute("url", "scripts");
		return "scripts";
	}
	@GetMapping("delteScripts/{id}")
	public String deleteScripts(Model model,@PathVariable("id") long id) {
		logger.info("get all scripts");;
		BotScripts botScripts = botScriptsRepository.getOne(id);
		
		if(botScripts !=null) {
			
			String filePath = scriptsRootDir+botScripts.getAppName()+File.separator+botScripts.getScriptFileName();
			File scritFile = new File(filePath); 
			if(scritFile.exists()) {
				if (scritFile.delete()) { 
			    	logger.info("Deleted the file: " + scritFile.getName());
			    	botScriptsRepository.delete(id);
			    } else {
			    	logger.info("Failed to delete the file.");
			    } 
			}else {
				logger.error("script file not exists. path :"+filePath);
			}
			
		}else {
			logger.error("Cannot delete script, script file not availeble.");
		}
		
		List<BotScripts> scripts = botScriptsRepository.findAll();
		model.addAttribute("scripts", scripts);
		model.addAttribute("url", "scripts");
		return "scripts";
	}
	@GetMapping("home")
	public String showHomeScripts(Model model) {
		logger.info("get all home");
		model.addAttribute("url", "home");
		return "home";
	}
	@GetMapping("source")
	public String showAllSources(Model model) {
		logger.info("get all scripts");
		//LinkedHashMap<String, LinkedHashMap<String, List<String>>> scriptFiles = mapper.prepareScriptTypes();
		
		LinkedHashMap<String, LinkedHashMap<String, List<BotScripts>>> dataList = new LinkedHashMap<String, LinkedHashMap<String, List<BotScripts>>>();
		List<String> scriptTypes = botScriptsRepository.getGroupByScriptTypes();
		for(String scriptType:scriptTypes) {
			LinkedHashMap<String, List<BotScripts>> files = new LinkedHashMap<String, List<BotScripts>>();
			List<String> appNames = botScriptsRepository.getAllAppNamesfindByScriptType(scriptType);
			for(String appName : appNames) {
				List<BotScripts> botScriptsList = botScriptsRepository.getAllScriptsfindByAppName(appName);
				files.put(appName, botScriptsList);
			}
			scriptType = scriptType.equals(Constants.PYWIN)?"DESKTOP":scriptType;
			dataList.put(scriptType, files);
		}
		try {
			BotScripts botScripts=new BotScripts();
			botScripts.setScriptFileName(Constants.COMMON_METHOD_FILE);
			botScripts.setAppName(Constants.COMMON_FOLDER);
			botScripts.setScriptType(Constants.COMMON);
			LinkedHashMap<String, List<BotScripts>> commonFiles = new LinkedHashMap<String, List<BotScripts>>();
			List<BotScripts> commonFile = new ArrayList<BotScripts>();
			File commonMethodsFile = new File(scriptsRootDir+Constants.COMMON_FOLDER+File.separator+Constants.COMMON_METHOD_FILE);
			if(commonMethodsFile.exists()) {
				commonFile.add(botScripts);
				commonFiles.put(Constants.COMMON_FOLDER, commonFile);
				dataList.put(Constants.COMMON, commonFiles);
			}else {
				
				File source = new File(getClass().getClassLoader().getResource(Constants.COMMON_METHOD_FILE).getFile());
				File dest = new File(scriptsRootDir+Constants.COMMON_FOLDER+File.separator+Constants.COMMON_METHOD_FILE);
				if (dest.getParentFile().mkdir()) {
					dest.createNewFile();
				} else {
				    throw new IOException("Failed to create directory " + dest.getParent());
				}
				boolean copingFileToLocal = mapper.copyFileUsingStream(source,dest);
				if(copingFileToLocal) {
					commonFile.add(botScripts);
					commonFiles.put(Constants.COMMON_FOLDER, commonFile);
					dataList.put(Constants.COMMON, commonFiles);
				}else {
					logger.error("common file not copied");
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			logger.error(e);
		}
		
		model.addAttribute("dataList", dataList);
		model.addAttribute("fileContent", "");
		model.addAttribute("url", "source");
		
		return "source";
	}

	@Deprecated
	@GetMapping("suites")
	public String showAllSuites(Model model) {
		logger.info("get all scripts");
		List<Suite> suiteList = suiteRepository.findAll();
		model.addAttribute("suiteList", suiteList);
		model.addAttribute("url", "suites");
		return "suites";
	}

	@GetMapping("suitesDetails/{id}")
	public String showSuitesDetails(Model model,@PathVariable("id") long id) {
		logger.info("get all scripts");
		Suite suite = suiteRepository.getOne(id);
		
		//code for tree structue
		LinkedHashMap<String, List<BotScripts>> files = new LinkedHashMap<String, List<BotScripts>>();
		List<String> appNames = botScriptsRepository.getGroupByAppNames();
		for(String appName : appNames) {
			List<BotScripts> botScriptsList = botScriptsRepository.getAllScriptsfindByAppName(appName);
			files.put(appName, botScriptsList);
		}
		model.addAttribute("scriptFiles", files);
		
		model.addAttribute("suite", suite);
		model.addAttribute("url", "suitesDetails");
		return "suitedetails";
	}

	@Deprecated
	@GetMapping("delteSuite/{id}")
	public String delteSuite(Model model,@PathVariable("id") long id) {
		logger.info("Delete suite");
		suiteRepository.delete(id);
		List<Suite> suiteList = suiteRepository.findAll();
		model.addAttribute("suiteList", suiteList);
		model.addAttribute("url", "suites");
		return "suites";
	}
	
	@GetMapping("suitesReportDetails/{suitename}")
	public String suitesReportDetails(Model model,@PathVariable("suitename") String suitename) {
		logger.info("get all suitesReportDetails");
		StringBuffer buffer = new StringBuffer();
		try {
			File file = new File(scriptsRootDir+suitename+File.separator+"summary-report.html"); 
			
			  if(file.exists()) {
				  BufferedReader br = new BufferedReader(new FileReader(file)); 
				  
				  String st; 
				  while ((st = br.readLine()) != null) 
					  buffer.append(st);  
			  }else {
				  buffer.append("<h2>Report Not Exists</h2>"); 
			  }
			  

		}catch (Exception e) {
			// TODO: handle exception
		}
		model.addAttribute("content", buffer.toString());
		model.addAttribute("url", "jobs");
		return "suitesreportdetails";
	}
	
	@GetMapping("newSuite")
	public String showNewSuites(Model model) {
		logger.info("get all scripts");
		//LinkedHashMap<String, LinkedHashMap<String, List<String>>> scriptFiles = mapper.prepareScriptTypes();
		//model.addAttribute("scriptFiles", scriptFiles);
//		LinkedHashMap<String, List<BotScripts>> files = new LinkedHashMap<String, List<BotScripts>>();
//		List<String> appNames = botScriptsRepository.getGroupByAppNames();
//		for(String appName : appNames) {
//			List<BotScripts> botScriptsList = botScriptsRepository.getAllScriptsfindByAppName(appName);
//			files.put(appName, botScriptsList);
//		}
		LinkedHashMap<String, LinkedHashMap<String, List<BotScripts>>> dataList = new LinkedHashMap<String, LinkedHashMap<String, List<BotScripts>>>();
		List<String> scriptTypes = botScriptsRepository.getGroupByScriptTypes();
		for(String scriptType:scriptTypes) {
			LinkedHashMap<String, List<BotScripts>> files = new LinkedHashMap<String, List<BotScripts>>();
			List<String> appNames = botScriptsRepository.getAllAppNamesfindByScriptType(scriptType);
			for(String appName : appNames) {
				List<BotScripts> botScriptsList = botScriptsRepository.getAllScriptsfindByAppName(appName);
				files.put(appName, botScriptsList);
			}
			scriptType = scriptType.equals(Constants.PYWIN)?"DESKTOP":scriptType;
			dataList.put(scriptType, files);
		}
		
		try {
			BotScripts botScripts=new BotScripts();
			botScripts.setScriptFileName(Constants.COMMON_METHOD_FILE);
			botScripts.setAppName(Constants.COMMON_FOLDER);
			botScripts.setScriptType(Constants.COMMON);
			LinkedHashMap<String, List<BotScripts>> commonFiles = new LinkedHashMap<String, List<BotScripts>>();
			List<BotScripts> commonFile = new ArrayList<BotScripts>();
			File commonMethodsFile = new File(scriptsRootDir+Constants.COMMON_FOLDER+File.separator+Constants.COMMON_METHOD_FILE);
			if(commonMethodsFile.exists()) {
				commonFile.add(botScripts);
				commonFiles.put(Constants.COMMON_FOLDER, commonFile);
				dataList.put(Constants.COMMON, commonFiles);
			}else {
				
				File source = new File(getClass().getClassLoader().getResource(Constants.COMMON_METHOD_FILE).getFile());
				File dest = new File(scriptsRootDir+Constants.COMMON_FOLDER+File.separator+Constants.COMMON_METHOD_FILE);
				if (dest.getParentFile().mkdir()) {
					dest.createNewFile();
				} else {
				    throw new IOException("Failed to create directory " + dest.getParent());
				}
				boolean copingFileToLocal = mapper.copyFileUsingStream(source,dest);
				if(copingFileToLocal) {
					commonFile.add(botScripts);
					commonFiles.put(Constants.COMMON_FOLDER, commonFile);
					dataList.put(Constants.COMMON, commonFiles);
				}else {
					logger.error("common file not copied");
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			logger.error(e);
		}
		model.addAttribute("fileContent", "");
		model.addAttribute("url", "suites");
		model.addAttribute("dataList", dataList);
		logger.info(dataList);
		return "newsuite";
	}

}
