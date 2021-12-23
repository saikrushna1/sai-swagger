package com.saviynt.identitybot.Service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.saviynt.identitybot.Service.DispatcherService;
import com.saviynt.identitybot.model.Job;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InvokeServiceImplTest {
	
	@Test
	public void testProcessJob() throws Exception {
		DispatcherService siService = new DispatcherService();
		Job job = new Job();
		siService.processJob("appJobID", job, "remoteIP");
	}
	
}
