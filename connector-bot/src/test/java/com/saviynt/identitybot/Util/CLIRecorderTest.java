package com.saviynt.identitybot.Util;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.saviynt.identitybot.Util.CLIRecorder;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CLIRecorderTest {

	String fileName = "clirecord";
	Boolean recordVideo = true;
	String format = ".avi";
	String appName = ".avi";
	
	@Autowired
	CLIRecorder cliRecorder;
	
	@Test
	public void testRecord() {
		cliRecorder.record(fileName, recordVideo, format,appName);
		assertTrue(true);
	}
	
	@Test
	public void testRecordTest() throws Exception {
		cliRecorder.recordTest(fileName, "");
		assertTrue(true);
	}
	
	@Test
	public void testStartVideo() throws Exception {
		cliRecorder.startVideo(fileName, format);
		assertTrue(true);
	}
	
	@Test
	public void testKillProcess() throws IOException {
		cliRecorder.killProcess();
		assertTrue(true);
	}
}
