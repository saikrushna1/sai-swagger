package com.saviynt.identitybot.Schedulers;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.saviynt.identitybot.Schedulers.ProcessFailedRecords;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessFailedRecordsTest {

	@Test
	public void testScheduleFixedRateTaskAsync() throws Exception {
		ProcessFailedRecords processFailedRecords = new ProcessFailedRecords();
		processFailedRecords.scheduleFixedRateTaskAsync();
		assertTrue(true);
	}
}
