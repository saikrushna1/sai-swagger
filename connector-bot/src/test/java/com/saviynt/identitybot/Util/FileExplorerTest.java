package com.saviynt.identitybot.Util;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.saviynt.identitybot.Util.Constants;
import com.saviynt.identitybot.Util.FileExplorer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileExplorerTest {
	
	@Value("${testcases.root}")
	String testCaseRoot;

	@Value("${scripts.dir}")
	String scriptsRootDir;
	
	@Test
	public void testReadContent() throws IOException {
		File temp = File.createTempFile("temp", ".txt");
		 String filePath = temp.getCanonicalPath();
		 Path path = Paths.get(filePath );
		List<String> allLines = new ArrayList<String>();
		try {
			allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
			for (String content : allLines) {
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail("failed while reading files.");
		}
		assertTrue(true);
	}
	
	@Test
	public void testcreateTempFile() throws IOException {
		File temp = File.createTempFile("temp", ".java");
		String content = "content";
		String fileName = temp.getName();
		FileExplorer explorer = new FileExplorer();
		String result = explorer.createTempFile(content, fileName);
		assertTrue(result.length() > 0);
	}
	
	@Test
	public void testupdateContent() throws IOException {
		File temp = File.createTempFile("temp", ".txt");
		String content = "content";
		String absolutePath = temp.getAbsolutePath();
		testCaseRoot = absolutePath.
		    substring(0,absolutePath.lastIndexOf(File.separator));
		String fileName = temp.getName();		
		FileExplorer explorer = new FileExplorer();
		String result = explorer.updateContent(testCaseRoot, fileName, content);
		assertTrue(result.isEmpty());
	}
	
	@Test
	public void testfindFiles() throws IOException {
		File tempJava = File.createTempFile("temp", ".java");
		File tempTxt = File.createTempFile("temp", ".txt");
		String absolutePath = tempJava.getAbsolutePath();
		testCaseRoot = absolutePath.
		    substring(0,absolutePath.lastIndexOf(File.separator));
		
		FileExplorer explorer = new FileExplorer();		
		LinkedHashMap<String, List<String>> filesList = explorer.findFiles(testCaseRoot, Constants.WEB);
		assertTrue(filesList.size() > 0);
	}
}
