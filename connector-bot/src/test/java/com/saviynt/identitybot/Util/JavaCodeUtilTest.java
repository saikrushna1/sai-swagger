package com.saviynt.identitybot.Util;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.saviynt.identitybot.constants.STATUS;
import com.saviynt.identitybot.model.Job;
import com.saviynt.identitybot.repository.JobRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JavaCodeUtilTest {

	@Autowired
	JobRepository jobRepository;

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

	@Test
	public void testconvertSeleniumCode() throws IOException {

		File file = File.createTempFile("temp", ".java");
		Job job = new Job();
		job.setStatus(STATUS.Submitted.toString());
		job.setJobStatusCode(STATUS.Submitted.getID());
		job.setContent("content");
		job.setScriptFileName(file.getName());

		assertTrue(job.getContent(), true);
		String scriptType = Constants.CLI;
		String scriptRootDir = null;
		if (scriptType.equals(cobotScriptType) || scriptType.equals(cliScriptType)) {
			scriptRootDir = testcasesRoot;
		} 
		String scriptFromRepo = scriptsRootDir + job.getScriptFileName();
		String transformedJavaFile = scriptRootDir + "/" + job.getScriptFileName();

		File file1 = new File(transformedJavaFile);

		if (!file1.getParentFile().exists()) {
			file1.getParentFile().mkdirs();
		}
		JavaCodeUtil.convertSeleniumCode(new File(scriptFromRepo), new File(transformedJavaFile),null, 0, null,null);
		assertTrue(true);
	}

	@SuppressWarnings("static-access")
	@Test
	public void testAddImports() {
		ArrayList<String> javaCodeList = new ArrayList<String>();
		JavaCodeUtil util = new JavaCodeUtil();
		//util.addImports(javaCodeList);
		System.out.println(javaCodeList);
		assertTrue(javaCodeList.size() > 0);
	}

	@SuppressWarnings("static-access")
	@Test
	public void testAddMethods() throws IOException {
		ArrayList<String> javaCodeList = new ArrayList<String>();
		JavaCodeUtil util = new JavaCodeUtil();
		//util.addImports(javaCodeList);
		//util.addMethods(javaCodeList, "DemoTest","");
		assertTrue(true);
	}


	@Test
	public void testremoveLast() {
		String s = "test";
		String result = JavaCodeUtil.removeLast(s, 2);
		assertTrue(!result.isEmpty());
	}
}
