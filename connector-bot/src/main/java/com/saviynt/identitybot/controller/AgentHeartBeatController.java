package com.saviynt.identitybot.controller;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.saviynt.identitybot.Util.Constants;
import com.saviynt.identitybot.model.BotAgent;
import com.saviynt.identitybot.repository.BotAgentRepository;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class AgentHeartBeatController {

	final static Logger logger = Logger.getLogger(AgentHeartBeatController.class);
	@Autowired
	BotAgentRepository botAgentRepository;

	@RequestMapping(value = "/agent/heartbeat", method = RequestMethod.POST)
	public @ResponseBody JobResponse uploadFileHandler(@RequestBody BotAgent agentStatus) {
		JobResponse jobResponse = new JobResponse();
			try {
				BotAgent botAgent = botAgentRepository.findByAgentId(agentStatus.getAgentId());
				if(botAgent!=null) {
					botAgentRepository.updateIpaddressAndLastUpdatedTimestampBySno(agentStatus.getIpaddress(), botAgent.getSno());
				}else {
					botAgentRepository.save(agentStatus);
				}
				jobResponse.setJobStatus(Constants.SUCCESS);
			}catch (Exception e) {
				e.printStackTrace();
				jobResponse.setJobStatus(Constants.FAIL);
			}
			return jobResponse;
	}

}
