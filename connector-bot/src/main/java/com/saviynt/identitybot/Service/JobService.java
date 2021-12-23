package com.saviynt.identitybot.Service;

import java.io.File;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.saviynt.identitybot.Util.Constants;
import com.saviynt.identitybot.Util.FileExplorer;
import com.saviynt.identitybot.Util.JobUtil;
import com.saviynt.identitybot.Util.S3Explorer;
import com.saviynt.identitybot.constants.STATUS;
import com.saviynt.identitybot.controller.Status;
import com.saviynt.identitybot.model.BotAgent;
import com.saviynt.identitybot.model.Job;
import com.saviynt.identitybot.repository.BotAgentRepository;
import com.saviynt.identitybot.repository.JobRepository;
import com.saviynt.identitybot.repository.SSMConnectionRepository;

@Service
public class JobService {

	@Autowired
	JobRepository jobRepository;

	@Autowired
	SSMConnectionRepository ssmConnectionRepository;

	@Autowired
	SSMAuthService authService;

	@Autowired
	BotAgentRepository botAgentRepository;

	@Value("${ssm.username}")
	String ssmUsername;

	@Value("${ssm.password}")
	String ssmPassword;

	@Value("${scripts.store}")
	String scriptsStore;
	
	@Autowired
	private FileExplorer fileExplorer;
	
	@Autowired
	private S3Explorer s3explorer;
	
	final static Logger logger = Logger.getLogger(JobService.class);

	public void jobUpdateStatus(Status jobStatus) {
		logger.info("Recieved Job Update Rquest for JOB" + jobStatus.getJobId());
		Long jobDBId = JobUtil.getDBJobIDFromAppJobID(jobStatus.getJobId());
		jobRepository.updateStatusAndReason(jobStatus.getStatus(), jobStatus.getReason(), jobDBId);

		Job job = jobRepository.getOne(jobDBId);
		if (job.getBotAgent().longValue() > 0) {
			if (job.getParalleExcecutionStatus().equals(Constants.OFF) || job.getScriptType().equals(Constants.DESKTOP)) {
				botAgentRepository.updateBotAgentLoadBalancerBySno(job.getBotAgent().longValue());
			}
			BotAgent botAgent = botAgentRepository.findByAgentSno(job.getBotAgent().longValue());
			int numberOfInstances = botAgent.getNumberOfInstances();
			if (numberOfInstances > 0) {
				botAgentRepository.updateBotAgentNumberOfInstancesBySno(--numberOfInstances,
						job.getBotAgent().longValue());
			}

		}

		//logger.info("getting token from SSM by using user = " + ssmUsername + " and Password = " + ssmPassword);
		Status tokenResponse = authService.getAuthToken(ssmUsername, ssmPassword);
		//logger.info("tokenResponse = " + tokenResponse.toString());
		// TOKEN IS NOT NULL AND NOT EMPTY THEN ALLOW THE CONDITION
		if (tokenResponse != null
				&& tokenResponse.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Success.getID()))) {
			String token = tokenResponse.getStatus();
			//logger.info("token = " + token);
			if (!token.isEmpty()) {
				// Job job = jobRepository.getOne(jobDBId);
				// botAgentRepository.updateBotAgentLoadBalancerBySno(job.getBotAgent().longValue());
				authService.sendTaskUpdateResponseBackToSSM(job.getTaskId(), token, jobStatus.getStatus(),
						jobStatus.getReason());
			} else {
				logger.info("SSM Token is not generated. ");
			}
		} else {
			logger.info("SSM Token is not generated.");
		}
	}
	public JSONObject findFinalParams(String content) {
		//List<String> finalParams = new ArrayList<String>();
		JSONObject cliParamObj = new JSONObject();
		String[] fileContentArray = content.split(Constants.SLASH_N);
		logger.info(Arrays.toString(fileContentArray));
		for (String value : fileContentArray) {
			String[] array = value.split(" ");
			for (String s : array) {
				String result = "";
				if (s.contains(Constants.DOUBLE_OPEN_CURLY_BRASES)) {
					boolean flag = false;
					for (int i = 0; i < s.length(); i++) {
						if (s.charAt(i) == Constants.CLOSE_CURLY_BRASES )
							continue;
						else if(!flag && s.charAt(i) == Constants.OPEN_CURLY_BRASES) {
							flag = true;
							continue;
						}
						else if(flag && s.charAt(i) != Constants.OPEN_CURLY_BRASES && s.charAt(i) != Constants.CLOSE && s.charAt(i) != Constants.SEMICOLON && s.charAt(i) != Constants.SLASHR )
							result += s.charAt(i);	
					}
				}
				if (!result.isEmpty())
					cliParamObj.put(result, "${"+result+"}");
			}
		}
		return cliParamObj;
	}
	
	public String saveFile(String path,String fileName,String content) {
		String status = fileExplorer.updateContent(path, fileName,
				content);
		
		if (scriptsStore != null && scriptsStore.equalsIgnoreCase(Constants.S3_BUCKET)) {
			try {
				File file = new File(path + File.separator + fileName);
				s3explorer.saveRecordedVideo(path, file, "");
			} catch (Exception e) {
				logger.error("ERROR >>> while saving updated file in s3 bucket ", e);
			}
		}
		return status;
	}
}
