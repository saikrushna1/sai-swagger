package com.saviynt.identitybot.Util;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.saviynt.identitybot.Util.JobUtil;
import com.saviynt.identitybot.constants.STATUS;
import com.saviynt.identitybot.controller.JobResponse;
import com.saviynt.identitybot.model.Job;
import com.saviynt.identitybot.repository.JobRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JobUtilTest {

	@Autowired
	JobRepository jobRepository;

		
	@Test
	public void testUpdateResponse() throws IOException {
		File file = File.createTempFile("temp", ".java");
		Job job = new Job();
		job.setStatus(STATUS.Submitted.toString());
		job.setJobStatusCode(STATUS.Submitted.getID());
		job.setContent("content");
		job.setScriptFileName(file.getName());
		Job saveJob = jobRepository.save(job);
		assertTrue(job.getContent(), true);
		JobResponse respobse =	JobUtil.updateResponse(saveJob, null);
		assertTrue(respobse != null);
	}
	
	@Test
	public void testgetDBJobIDFromAppJobID() {
		String appJobId = "";
		Long respobse =	JobUtil.getDBJobIDFromAppJobID(appJobId);
		assertTrue(respobse != null);
	}
}
