package com.saviynt.identitybot.Service;

import com.saviynt.identitybot.model.BotScripts;
import com.saviynt.identitybot.model.Suite;
import com.saviynt.identitybot.response.CreateSuccessResppnse;
import com.saviynt.identitybot.response.SuccessResponse;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TestSuitService {


    public Page<Suite> getTestSuitsBasedOnQuery(Pageable pageable);

    public CreateSuccessResppnse createNewTestSuit();


    public SuccessResponse deleteScriptFile(Long id);

    LinkedHashMap<String, List<BotScripts>> getSuitDetails(Long id);
}
