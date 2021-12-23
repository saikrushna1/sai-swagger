package com.saviynt.identitybot.controller;

import com.saviynt.identitybot.Service.ScriptService;
import com.saviynt.identitybot.model.BotScripts;
import com.saviynt.identitybot.repository.BotScriptsRepository;
import com.saviynt.identitybot.response.SuccessResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.io.File;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/script/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ScriptController {

    @Autowired
    ScriptService scriptService;

//    @GetMapping("{page}")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
//                    required = true, dataType = "string", paramType = "header")
//    })
//
//    public ResponseEntity getScript(@RequestParam int pageSize,@RequestParam int pageNumber){
//        return new ResponseEntity<>(scriptService.getScriptsBasedOnQuery(pageSize,pageNumber), HttpStatus.OK);
//    }

    @GetMapping("v1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")
    })

    public ResponseEntity getScript(Pageable pageable){
        return new ResponseEntity<>(scriptService.getScriptsBasedOnQuery(pageable), HttpStatus.OK);
    }
  
    
    
    
    
    
    
    @DeleteMapping("{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity deleteScripts(@PathVariable("id") long id) {
        return new ResponseEntity<SuccessResponse>(scriptService.deleteScriptFile(id), HttpStatus.OK);
    }

}
