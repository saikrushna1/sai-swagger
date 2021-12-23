package com.saviynt.identitybot.Util;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import com.saviynt.identitybot.Util.ScriptUtil;
import com.saviynt.identitybot.constants.STATUS;
import com.saviynt.identitybot.model.Job;
import com.saviynt.identitybot.repository.JobRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScriptUtilTest {
	@Autowired
	JobRepository jobRepository;
	
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
	Environment env;
	
	@Test
	public void testtransformScriptFile() throws IOException {
		File file = File.createTempFile("temp", ".java");
		Job job = new Job();
		job.setStatus(STATUS.Submitted.toString());
		job.setJobStatusCode(STATUS.Submitted.getID());
		job.setContent("content");
		job.setScriptFileName(file.getName());
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write("class A{}");
		bw.close();
		 String absolutePath = file.getAbsolutePath();
		 scriptsRootDir = absolutePath.
		    substring(0,absolutePath.lastIndexOf(File.separator));
		job.setScriptFileName(file.getName());
		ScriptUtil.transformScriptFile(job, scriptsRootDir+"\\", testcasesRoot,"", 0, null);
		assertTrue(true);
	}
	
	@Test
	public void testcompileScriptFile() throws IOException {
		File file = File.createTempFile("temp", ".java");
		Job job = new Job();
		job.setStatus(STATUS.Submitted.toString());
		job.setJobStatusCode(STATUS.Submitted.getID());
		job.setContent("content");
		job.setScriptFileName(file.getName());
		String executeMode = env.getProperty("execute.mode");
		try {
			String absolutePath = file.getAbsolutePath();
			 testcasesRoot = absolutePath.
			    substring(0,absolutePath.lastIndexOf(File.separator));
			job.setScriptFileName(file.getName());
			ScriptUtil.compileScriptFile(job, testcasesRoot, executeMode,"");
		}catch (Exception e) {
			fail("failed while compiling script file");
		}
		assertTrue(true);
	}
	
	@Test
	public void testcompileScriptFileContent() throws Exception {
		String executeMode = env.getProperty("execute.mode");
		File file = File.createTempFile("temp", ".java");
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write("class A{}");
		bw.close();
		 String absolutePath = file.getAbsolutePath();
		 scriptsRootDir = absolutePath.
		    substring(0,absolutePath.lastIndexOf(File.separator));
		 String error = ScriptUtil.compileScriptFileContent(file.getName(), executeMode);
		 assertTrue(error.isEmpty());
	}
	
	@Test
	public void testloadAndRunClass() throws IOException {
		File file = File.createTempFile("temp", ".class");
		Job job = new Job();
		job.setStatus(STATUS.Submitted.toString());
		job.setJobStatusCode(STATUS.Submitted.getID());
		job.setContent("content");
		job.setScriptFileName(file.getName());
		try {
			//File file = File.createTempFile("temp", ".class");
			job.setContent("{'content':'test','data':[], 'searchdata':[],'recordVideo':true,'format':'.av'}");
			String absolutePath = file.getAbsolutePath();
			testcasesRoot = absolutePath.
			    substring(0,absolutePath.lastIndexOf(File.separator));
			job.setScriptFileName(file.getName());
			String appJobID = "";
//		ScriptUtil.loadAndRunClass(scriptsRootDir, file.getAbsolutePath(), job.getContent(), testcasesRoot, cobotStatusCallbackURL,
//				appJobID,"");
		}catch (Exception e) {
			e.printStackTrace();
			fail("failed while compiling script file");
		}
		assertTrue(true);
	}
}
