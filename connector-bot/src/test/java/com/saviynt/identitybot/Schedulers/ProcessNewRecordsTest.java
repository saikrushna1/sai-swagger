package com.saviynt.identitybot.Schedulers;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.saviynt.identitybot.Schedulers.ProcessNewRecords;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessNewRecordsTest {

	@Test
	public void testScheduleFixedRateTaskAsync() {
		ProcessNewRecords processNewRecords = new ProcessNewRecords();
		processNewRecords.scheduleFixedRateTaskAsync();
		assertTrue(true);
	}
}
