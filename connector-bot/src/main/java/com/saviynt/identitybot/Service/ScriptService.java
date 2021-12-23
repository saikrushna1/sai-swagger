package com.saviynt.identitybot.Service;

import com.saviynt.identitybot.controller.JobUIController;
import com.saviynt.identitybot.exceptions.RecordNotFoundException;
import com.saviynt.identitybot.model.BotScripts;
import com.saviynt.identitybot.repository.BotScriptsRepository;
import com.saviynt.identitybot.response.SuccessResponse;
import org.apache.log4j.Logger;
import org.apache.regexp.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


import java.io.File;
import java.util.List;
import java.util.Map;

@Service
public interface ScriptService {

    //TODO: NEED TO GET DATA BASED IN FILTER
	
 //   public List<BotScripts> getScriptsBasedOnQuery();

  
//	public Page<BotScripts> getScriptsBasedOnQuery(int pageSize,int pageNumber);
	
	
	public Page<BotScripts> getScriptsBasedOnQuery(Pageable pageable);


	
    public SuccessResponse deleteScriptFile(Long id);

    }



