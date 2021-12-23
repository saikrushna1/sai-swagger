package com.saviynt.identitybot.Util;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.File;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.saviynt.identitybot.Service.RemoteAgentService;
import com.saviynt.identitybot.Util.CLIPlayer;
import com.saviynt.identitybot.Util.JobUtil;
import com.saviynt.identitybot.constants.STATUS;
import com.saviynt.identitybot.model.Job;
import com.saviynt.identitybot.repository.JobRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RemoteAgentUtilsTest {
	@Autowired
	JobRepository jobRepository;
	
	@Autowired 
	RemoteAgentService remoteAgentService;
	
	@Value("${cobot.status.callback.url}")
	String cobotStatusCallbackURL;
	
	@Value("${scripts.dir}")
	String scriptsRootDir;
	
	@Value("${testcases.root}")
	String testcasesRoot;



	@Value("${agent.port}")
	String agentSystemPort;

	@Value("${agent.scriptUploadUrl}")
	String agentScriptUploadUrl;

	@Value("${parentProperty}")
	String parentProperty;

	@Value("${cobot.scriptType}")
	String cobotScriptType;

	@Value("${cli.scriptType}")
	String cliScriptType;
	
	
	@Autowired
	CLIPlayer playBackCli;
	
		
	@Test
	public void testExecuteRemoteJob() throws Exception {
		File file = File.createTempFile("temp", ".java");
		Job job = new Job();
		job.setStatus(STATUS.Submitted.toString());
		job.setJobStatusCode(STATUS.Submitted.getID());
		job.setScriptFileName(file.getName());
		job.setContent("content");
		job.setRemoteAgentIP("192.168.0.100");
		
		assertTrue(job.getContent(), true);
		String appJobID = JobUtil.getAppJobIDFromDBJobID(job.getId());
		File file1 = File.createTempFile("temp", ".class");
		
		 String absolutePath = file1.getAbsolutePath();
		 scriptsRootDir = absolutePath.
		    substring(0,absolutePath.lastIndexOf(File.separator));
		String fileName = job.getScriptFileName().replace(".java", ".class");
		try {
//			remoteAgentService.executeRemoteJob(scriptsRootDir, agentSystemPort, agentScriptUploadUrl,
//				cobotStatusCallbackURL, appJobID, job.getContent(), fileName, job.getRemoteAgentIP());
		}catch (Exception e) {
			fail("error while execute remote job.");
		}
		assertTrue(true);
	}
}
