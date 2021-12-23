package com.saviynt.identitybot.Service.Impl;

import com.saviynt.identitybot.Service.ScriptService;
import com.saviynt.identitybot.exceptions.RecordNotFoundException;
import com.saviynt.identitybot.model.BotScripts;
import com.saviynt.identitybot.repository.BotScriptsRepository;
import com.saviynt.identitybot.response.SuccessResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class ScriptServiceImpl implements ScriptService {
    final static Logger logger = Logger.getLogger(ScriptService.class);

    @Autowired
    private BotScriptsRepository botScriptsRepository;

    @Value("${scripts.dir}")
    String scriptsRootDir;


    //TODO: NEED TO GET DATA BASED IN FILTER
//    public List<BotScripts> getScriptsBasedOnQuery(){
//        List<BotScripts> scripts = botScriptsRepository.findAll();
//        return scripts;
//    }

    
//    public Page<BotScripts> getScriptsBasedOnQuery(int pageSize,int pageNumber){
//      Pageable paging = PageRequest.of(pageSize,pageNumber);   	
//      Page<BotScripts> scripts = botScriptsRepository.findAll(paging);
//      return scripts;
//  }
 
  
  public Page<BotScripts> getScriptsBasedOnQuery(Pageable page){   	
  Page<BotScripts> scripts = botScriptsRepository.findAll(page);
  return scripts;
}
 
    
    
    
    

    public SuccessResponse deleteScriptFile(Long id) {
        BotScripts botScripts = botScriptsRepository.getOne(id);
        if(botScripts !=null) {
            String filePath = scriptsRootDir+botScripts.getAppName()+ "/"+botScripts.getScriptFileName();
            File scritFile = new File(filePath);
            if(scritFile.exists()) {
                if (scritFile.delete()) {
                    logger.info("Deleted the file: " + scritFile.getName());
                    botScriptsRepository.delete(id);
                    return SuccessResponse.builder().message("Deleted the file: " + scritFile.getName()).build();
                } else {
                    logger.info("Failed to delete the file.");
                    return null;
//                    throw new Exception("Failed to delete the file.");
                }
            }else {
                logger.error("script file not exists with path :"+filePath);
                throw new RecordNotFoundException("script file not exists with path :"+filePath);
            }

        }else {
            logger.error("Cannot delete script, script file not availeble.");
            throw new RecordNotFoundException("script file not exists with id "+id);
        }

    }






	


}
