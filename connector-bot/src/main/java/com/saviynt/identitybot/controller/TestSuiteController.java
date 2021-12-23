package com.saviynt.identitybot.controller;

import com.saviynt.identitybot.Service.TestSuitService;
import com.saviynt.identitybot.Util.Constants;
import com.saviynt.identitybot.constants.STATUS;
import com.saviynt.identitybot.model.BotScripts;
import com.saviynt.identitybot.model.Job;
import com.saviynt.identitybot.model.Suite;
import com.saviynt.identitybot.payload.UpdateSuite;
import com.saviynt.identitybot.repository.JobRepository;
import com.saviynt.identitybot.repository.SuiteRepository;
import com.saviynt.identitybot.response.SuccessResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/api/suite/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TestSuiteController {


    @Autowired
    private TestSuitService testSuitService;

    @Autowired
    private SuiteRepository suiteRepository;

    @Autowired
    private JobRepository jobRepository;


    @GetMapping("v2")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity getScript(Pageable pageable){
        return new ResponseEntity<>(testSuitService.getTestSuitsBasedOnQuery(pageable), HttpStatus.OK);
    }


    @DeleteMapping("{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity deleteSuit(@PathVariable Long id ){
        return new ResponseEntity<SuccessResponse>(testSuitService.deleteScriptFile(id), HttpStatus.OK);
    }


    @GetMapping("{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")
    })

    public ResponseEntity getSuitDetails(@PathVariable Long id){
        return new ResponseEntity<LinkedHashMap<String, List<BotScripts>>>(testSuitService.getSuitDetails(id),HttpStatus.OK);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")
    })
    @RequestMapping(value = "createNew", method = RequestMethod.POST)
    public ResponseEntity<?> handleNewSuite(HttpServletRequest servletRequest, Principal principal) {
        String content = null;
        Suite suiteObj = null;
        try {
            content = IOUtils.toString(servletRequest.getReader());
            JSONObject bodyObj = new JSONObject(content);
            Object suiteNameObj = bodyObj.get("suiteName");
            Object suiteDescObj = bodyObj.get("suiteDesc");
//			Object exeType = bodyObj.get("exeType");
//			Object browserType = bodyObj.get("browserType");
            if (suiteNameObj != null) {
                //to know suitename already there in suites table or not.
                List<Suite> suiteListObj = suiteRepository.getAllSuitesfindBySuiteName((String) suiteNameObj);

                if(suiteListObj.size()==0) {
                    String suiteName = null;
                    Object jobObj = bodyObj.get("job");
                    Suite suite = new Suite();
                    if (suiteNameObj != null) {
                        suiteName = (String) suiteNameObj;
                        suite.setSuiteName(suiteName);
                    }
                    if (suiteDescObj != null) {
                        String suiteDesc = (String) suiteDescObj;
                        suite.setSuiteDesc(suiteDesc);
                    }
                    suite.setCreatedBy(principal.getName());
//				if (exeType != null) {
//					Long exeTypeObj = Long.valueOf((String) exeType);
//					suite.setExecutionType(exeTypeObj);
//				}
//				if (browserType != null) {
//					String browserTypeObj = (String) browserType;
//					suite.setBrowserType(browserTypeObj);
//				}
                    if (jobObj != null) {
                        List<Job> jobList = new ArrayList<Job>();
                        JSONArray jobsArray = (JSONArray) jobObj;
                        for (int i = 0; i < jobsArray.length(); i++) {
                            Object scriptObj = jobsArray.get(i);
                            JSONObject jsonObject = (JSONObject) scriptObj;
                            Job job = new Job();
                            Object jobContentObj = jsonObject.get("content");
                            JSONParser parser = new JSONParser();
                            net.minidev.json.JSONObject obj = null;
                            try {
                                //converting json in string form into json object
                                obj = (net.minidev.json.JSONObject) parser.parse(jobContentObj.toString());
                                Object ipObj = obj.get(Constants.REMOTE_IP);
                                // String scriptType = null;
                                String remoteIp = null;
                                if (ipObj != null) {
                                    if (String.valueOf(ipObj).length() > 0) {
                                        remoteIp = ipObj.toString().trim();
                                    }
                                }
                                Object scriptTypeObj = obj.get(Constants.SCRIPT_TYPE);
                                // String scriptType = null;
                                String scriptType = null;
                                if (ipObj != null) {
                                    if (String.valueOf(scriptTypeObj).length() > 0) {
                                        scriptType = scriptTypeObj.toString().trim();
                                    }
                                }

                                job.setContent(jobContentObj.toString());
                                job.setStatus("transient");
                                job.setJobStatusCode(STATUS.Submitted.getID());
                                job.setRemoteAgentIP(remoteIp);
                                // job.setContent(content);
                                job.setScriptType(scriptType);
//							if(obj.get(Constants.FILE_NAME).toString().endsWith(".java")) {
//								job.setScriptType("Web");
//							}
//							if(obj.get(Constants.FILE_NAME).toString().endsWith(".py")) {
//								job.setScriptType("PYWIN");
//							}
//							if(obj.get(Constants.FILE_NAME).toString().endsWith(".txt")) {
//								job.setScriptType("CLI");
//							}
                                job.setRetryCount(0);
                                job.setRecordVideo(Boolean.valueOf(obj.get(Constants.RECORD_VIDEO).toString()));
                                job.setVideoFormat(obj.get(Constants.FORMAT).toString());
                                job.setAppName(obj.get(Constants.APPNAME).toString());
                                job.setSuiteName(suiteName);
                                // job.setHttpparams(obj.get(Constants.HTTP_PARAMS).toString());
                                job.setScriptFileName(obj.get(Constants.FILE_NAME).toString());
                                if (obj.get(Constants.TASKID) != null) {
                                    job.setTaskId(obj.get(Constants.TASKID).toString());
                                }
                                job.setParalleExcecutionStatus(obj.get(Constants.PARALLELEXECUTION_STATUS).toString());
                                jobList.add(job);
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        suite.setJob(jobList);
                    }
//                    logger.info(suite);
                    suiteObj = suiteRepository.save(suite);

                }
            }else {
                return new ResponseEntity<String>("Invalid details", HttpStatus.OK);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(suiteObj!=null) {
            return new ResponseEntity<>(suiteObj, HttpStatus.OK);
        }else {
            suiteObj = new Suite();
            return new ResponseEntity<>(suiteObj, HttpStatus.OK);
        }

    }




    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")
    })
    @PatchMapping(value = "/run/{id}")
    public ResponseEntity<?> handleUpdateSuite(@PathVariable Long id ,@RequestBody UpdateSuite updateSuite) {
        String content = null;
        Suite suiteObj = null;
        try {

            Suite suite = new Suite();

            if (updateSuite.getExeType() != null) {
                suite.setExecutionType(updateSuite.getExeType());
            }
            if (updateSuite.getBrowserType() != null) {
                suite.setBrowserType(updateSuite.getBrowserType());
            }
            String pExexutionObj = null;
            if (updateSuite.getPExexution() != null) {
                suite.setParallelExecution(updateSuite.getPExexution());
            }

            suite.setId(id);
            Suite suiteOneObj = suiteRepository.getOne(suite.getId());
            suite.setSuiteName(suiteOneObj.getSuiteName());
            suite.setSuiteDesc(suiteOneObj.getSuiteDesc());
            suite.setCreatedAt(suiteOneObj.getCreatedAt());
            suite.setCreatedBy(suiteOneObj.getCreatedBy());

            if (updateSuite.getJob() != null) {
                List<Job> jobList = new ArrayList<Job>();
                List jobsArray =  updateSuite.getJob();
                for (int i = 0; i < jobsArray.size(); i++) {
                    LinkedHashMap scriptObj =(LinkedHashMap<String,String>) jobsArray.get(i);
                    Job job = new Job();
                    String jobContent = scriptObj.get("content").toString();
                    JSONParser parser = new JSONParser();
                    net.minidev.json.JSONObject obj = null;
                    try {
                        obj = (net.minidev.json.JSONObject) parser.parse(jobContent);
                        Object ipObj = obj.get(Constants.REMOTE_IP);
                        // String scriptType = null;
                        String remoteIp = null;
                        if (ipObj != null) {
                            if (String.valueOf(ipObj).length() > 0) {
                                remoteIp = ipObj.toString().trim();
                            }
                        }

                        Object scriptTypeObj = obj.get(Constants.SCRIPT_TYPE);
                        // String scriptType = null;
                        String scriptType = null;
                        if (ipObj != null) {
                            if (String.valueOf(scriptTypeObj).length() > 0) {
                                scriptType = scriptTypeObj.toString().trim();
                            }
                        }


                        job.setContent(jobContent);
                        job.setStatus("transient");
                        job.setJobStatusCode(STATUS.Submitted.getID());
                        job.setRemoteAgentIP(remoteIp);
                        // job.setContent(content);
                        job.setScriptType(scriptType);
//						if(obj.get(Constants.FILE_NAME).toString().endsWith(".java")) {
//							job.setScriptType("Web");
//						}
//						if(obj.get(Constants.FILE_NAME).toString().endsWith(".py")) {
//							job.setScriptType("PYWIN");
//						}
//						if(obj.get(Constants.FILE_NAME).toString().endsWith(".txt")) {
//							job.setScriptType("CLI");
//						}
                        job.setBrowserType(updateSuite.getBrowserType());
                        job.setRetryCount(0);
                        job.setRecordVideo(Boolean.valueOf(obj.get(Constants.RECORD_VIDEO).toString()));
                        job.setVideoFormat(obj.get(Constants.FORMAT).toString());
                        job.setAppName(obj.get(Constants.APPNAME).toString());
                        job.setSuiteName(suiteOneObj.getSuiteName());
                        // job.setHttpparams(obj.get(Constants.HTTP_PARAMS).toString());
                        job.setScriptFileName(obj.get(Constants.FILE_NAME).toString());
                        if (obj.get(Constants.TASKID) != null) {
                            job.setTaskId(obj.get(Constants.TASKID).toString());
                        }
                        job.setParalleExcecutionStatus(obj.get(Constants.PARALLELEXECUTION_STATUS).toString());
                        jobList.add(job);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                suite.setJob(jobList);
            }
            //Deleted all records in jobs table based on suite id
            jobRepository.deleteJobsBySuiteId(suite.getId());
            //Update suite details in suite table.Insert script details in jobs table
            suiteRepository.saveAndFlush(suite);
            // suiteRepository.updateSuiteById(suite.getExecutionType(), suite.getBrowserType(), suite.getParallelExecution(), suite.getId());
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            //Update jobs details in jobs table and changing status to submitted at query level
            jobRepository.updateJobBySuiteId(suiteOneObj.getSuiteName(),pExexutionObj,timestamp,suite.getId());
            suiteObj = suite;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(suiteObj!=null) {
            return new ResponseEntity<>(suiteObj, HttpStatus.OK);
        }else {
            return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")
    })
    @RequestMapping(value = "/addScripts", method = RequestMethod.POST)
    public ResponseEntity<?> addscripts(HttpServletRequest servletRequest,Principal principal) {
        String content = null;
        Suite suiteObj = null;
        try {
            content = IOUtils.toString(servletRequest.getReader());
            JSONObject bodyObj = new JSONObject(content);
            Object exeType = bodyObj.get("exeType");
            Object browserType = bodyObj.get("browserType");
            Object pExexution = bodyObj.get("pExexution");
            Object id = bodyObj.getLong("id");
            Object jobObj = bodyObj.get("job");
            Suite suite = new Suite();
            if (exeType != null) {
                Long exeTypeObj = Long.valueOf((String) exeType);
                suite.setExecutionType(exeTypeObj);
            }
            if (browserType != null) {
                String browserTypeObj = (String) browserType;
                suite.setBrowserType(browserTypeObj);
            }
            String pExexutionObj = null;
            if (pExexution != null) {
                pExexutionObj = (String) pExexution;
                suite.setParallelExecution(pExexutionObj);
            }
            Long idObj = 0l;
            if (id != null) {
                idObj = (Long) id;
                suite.setId(idObj);
            }
            Suite suiteOneObj = suiteRepository.getOne(suite.getId());
            suite.setSuiteName(suiteOneObj.getSuiteName());
            suite.setSuiteDesc(suiteOneObj.getSuiteDesc());
            suite.setCreatedAt(suiteOneObj.getCreatedAt());
            suite.setCreatedBy(suiteOneObj.getCreatedBy());
            if (jobObj != null) {
                List<Job> jobList = new ArrayList<Job>();
                JSONArray jobsArray = (JSONArray) jobObj;
                for (int i = 0; i < jobsArray.length(); i++) {
                    Object scriptObj = jobsArray.get(i);
                    JSONObject jsonObject = (JSONObject) scriptObj;
                    Job job = new Job();
                    String jobContent = jsonObject.getString("content");
                    JSONParser parser = new JSONParser();
                    net.minidev.json.JSONObject obj = null;
                    try {
                        obj = (net.minidev.json.JSONObject) parser.parse(jobContent);
                        Object ipObj = obj.get(Constants.REMOTE_IP);
                        // String scriptType = null;
                        String remoteIp = null;
                        if (ipObj != null) {
                            if (String.valueOf(ipObj).length() > 0) {
                                remoteIp = ipObj.toString().trim();
                            }
                        }
                        Object scriptTypeObj = obj.get(Constants.SCRIPT_TYPE);
                        // String scriptType = null;
                        String scriptType = null;
                        if (ipObj != null) {
                            if (String.valueOf(scriptTypeObj).length() > 0) {
                                scriptType = scriptTypeObj.toString().trim();
                            }
                        }
                        String browserTypeObj = (String) browserType;
                        job.setContent(jobContent);
                        job.setStatus("transient");
                        job.setJobStatusCode(STATUS.Submitted.getID());
                        job.setRemoteAgentIP(remoteIp);
                        // job.setContent(content);
                        job.setScriptType(scriptType);
//						if(obj.get(Constants.FILE_NAME).toString().endsWith(".java")) {
//							job.setScriptType("Web");
//						}
//						if(obj.get(Constants.FILE_NAME).toString().endsWith(".py")) {
//							job.setScriptType("PYWIN");
//						}
//						if(obj.get(Constants.FILE_NAME).toString().endsWith(".txt")) {
//							job.setScriptType("CLI");
//						}
                        job.setBrowserType(browserTypeObj);
                        job.setRetryCount(0);
                        job.setRecordVideo(Boolean.valueOf(obj.get(Constants.RECORD_VIDEO).toString()));
                        job.setVideoFormat(obj.get(Constants.FORMAT).toString());
                        job.setAppName(obj.get(Constants.APPNAME).toString());
                        job.setSuiteName(suiteOneObj.getSuiteName());
                        // job.setHttpparams(obj.get(Constants.HTTP_PARAMS).toString());
                        job.setScriptFileName(obj.get(Constants.FILE_NAME).toString());
                        if (obj.get(Constants.TASKID) != null) {
                            job.setTaskId(obj.get(Constants.TASKID).toString());
                        }
                        job.setParalleExcecutionStatus(obj.get(Constants.PARALLELEXECUTION_STATUS).toString());
                        jobList.add(job);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                suite.setJob(jobList);
            }
            //Deleted all records in jobs table based on suite id
            jobRepository.deleteJobsBySuiteId(suite.getId());
            //Update suite details in suite table.Insert script details in jobs table
            suiteRepository.saveAndFlush(suite);
            // suiteRepository.updateSuiteById(suite.getExecutionType(), suite.getBrowserType(), suite.getParallelExecution(), suite.getId());
            suiteObj = suite;

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(suiteObj!=null) {
            return new ResponseEntity<>(suiteObj, HttpStatus.OK);
        }else {
            return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



}
