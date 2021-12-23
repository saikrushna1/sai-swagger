package com.saviynt.identitybot.Service.Impl;

import com.saviynt.identitybot.Service.TestSuitService;
import com.saviynt.identitybot.exceptions.RecordNotFoundException;
import com.saviynt.identitybot.model.BotScripts;
import com.saviynt.identitybot.model.Suite;
import com.saviynt.identitybot.repository.BotScriptsRepository;
import com.saviynt.identitybot.repository.SuiteRepository;
import com.saviynt.identitybot.response.CreateSuccessResppnse;
import com.saviynt.identitybot.response.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class TestSuitServiceImpl  implements TestSuitService {

    @Autowired
    SuiteRepository suiteRepository;

    @Autowired
    BotScriptsRepository botScriptsRepository;

    @Override
    public Page<Suite> getTestSuitsBasedOnQuery(Pageable page) {
        return suiteRepository.findAll(page);
    }

    @Override
    public CreateSuccessResppnse createNewTestSuit() {
        return null;
    }

    @Override
    public SuccessResponse deleteScriptFile(Long id) {
        try{
            if(suiteRepository.exists(id))
                suiteRepository.delete(id);
             else throw new RecordNotFoundException("Not able to find test suit with id "+id);
        }catch (Exception e){
            throw e;
        }
        return SuccessResponse.builder().message("Deleted test suit with id "+id).build();
    }

    //TODO: REFACTOR THE METHOD , USE STREAMS AND FILTER
    @Override
    public LinkedHashMap<String, List<BotScripts>> getSuitDetails(Long id) {
        Suite suite = suiteRepository.getOne(id);
        //code for tree structue
        LinkedHashMap<String, List<BotScripts>> files = new LinkedHashMap<String, List<BotScripts>>();
        List<String> appNames = botScriptsRepository.getGroupByAppNames();
        for(String appName : appNames) {
            List<BotScripts> botScriptsList = botScriptsRepository.getAllScriptsfindByAppName(appName);
            files.put(appName, botScriptsList);
        }
        return files;
    }
}
