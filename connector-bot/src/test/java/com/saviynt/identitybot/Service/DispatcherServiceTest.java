package com.saviynt.identitybot.Service;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.saviynt.identitybot.Service.DispatcherService;
import com.saviynt.identitybot.Util.JobUtil;
import com.saviynt.identitybot.constants.STATUS;
import com.saviynt.identitybot.model.Job;
import com.saviynt.identitybot.repository.JobRepository;
@RunWith(SpringRunner.class)
@SpringBootTest
public class DispatcherServiceTest {
	
	@Autowired
	JobRepository jobRepository;

	@Autowired
	DispatcherService siService;
	@Value("${cobot.scriptType}")
	String cobotScriptType;

	@Value("${cli.scriptType}")
	String cliScriptType;
	
	
	@PrepareForTest({JobUtil.class})
	@Test
	public void testProcessJob() throws Exception {		
		
		Job job = new Job();
		job.setStatus(STATUS.Submitted.toString());
		job.setJobStatusCode(STATUS.Submitted.getID());
		job.setContent("content");
		job.setScriptFileName("DemoTest.java");
		job.setRetryCount(0);
		assertTrue(job.getContent(), true);
		Job saveJob = jobRepository.save(job);
		saveJob.setScriptType(cobotScriptType);
		saveJob.setContent("{'content':'test','data':[], 'searchdata':[],'recordVideo':true,'format':'.av'}");
		int retryCount = job.getRetryCount();
		int id = jobRepository.updateStatus(++retryCount,STATUS.Inprocess.toString(), saveJob.getId(),1l,"");
		String appJobID = JobUtil.getAppJobIDFromDBJobID(saveJob.getId());
		siService.processJob( appJobID, saveJob, saveJob.getRemoteAgentIP());
		assertTrue(true);
	}

}
