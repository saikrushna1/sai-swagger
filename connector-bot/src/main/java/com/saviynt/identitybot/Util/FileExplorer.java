/**
 * 
 */
package com.saviynt.identitybot.Util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author venkat
 *
 */
@Component
public class FileExplorer {

	final static Logger logger = Logger.getLogger(FileExplorer.class);

	public String readContent(String filePath, String scriptFileName) {
		Path path = Paths.get(filePath + File.separator + scriptFileName);
		List<String> allLines = new ArrayList<String>();
		String fileContent = "";
		try {
			logger.info("scriptFileName = "+scriptFileName);
			allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
			for (String content : allLines) {
				fileContent += content + Constants.csvNewLine;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileContent;
	}

	public String createTempFile(String content, String fileName) {
		try {
			logger.info("Creating Temp file...");
			File file = null;
			if(fileName.endsWith(Constants.DOT_JAVA)) {
				fileName = fileName.replace(Constants.DOT_JAVA, "").trim();
				file = File.createTempFile(Constants.FILECONTENT_TEMP, Constants.DOT_JAVA);
			}else if(fileName.endsWith(Constants.DOT_PY)) {
				fileName = fileName.replace(Constants.DOT_PY, "").trim();
				file = File.createTempFile(Constants.FILECONTENT_TEMP_PY, Constants.DOT_PY);
			}
			
			
			logger.info("Created temp file with name = " + file.getName());
			if (file.exists()) {
				if (content.contains(fileName)) {
					content = content.replace(fileName, file.getName().replace(Constants.DOT_JAVA, ""));
				}
				BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				bw.write(content);
				bw.close();
				logger.info("Temp File created = " + file.getName());
				logger.info("File full path =" + file.getAbsolutePath());
				return file.getAbsolutePath();
			} else {
				logger.info("Temp File cannot be created: " + file.getName());
				return null;
			}
		} catch (Exception e) {
			logger.error("ERROR >> while creating temp file. " + e.getMessage());
		}
		return null;
	}

	public String updateContent(String testCaseRoot, String fileName, String content) {
		try {
			logger.info("Updating content in " + fileName + " file.");
			String root = testCaseRoot + File.separator + fileName;
			File file = new File(root);
			if (file.exists()) {
				BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				bw.write(content);
				bw.close();
				logger.info("File content updated: " + file.getName());
				return "";
			}
			else {
				logger.info("File cannot be updated: " + file.getName());
				return "File cannot be updated";
			}
		} catch (Exception e) {
			logger.error("ERROR >> while updating content in " + fileName + " file. \n" + e.getMessage());
			return e.getMessage();
		}
	}

	public LinkedHashMap<String, List<String>> findFiles(String path, String type) {
		String[] pathnames;
		LinkedHashMap<String, List<String>> files = new LinkedHashMap<String, List<String>>();
        File f = new File(path);
        pathnames = f.list();
        logger.info("Files ="+Arrays.toString(pathnames));
		if (pathnames != null) {
			for (String pathname : pathnames) {
				logger.info(pathname);
				if (type.equalsIgnoreCase(Constants.WEB)) {
					if((!pathname.equals(Constants.COMMON_FOLDER)) && (!pathname.equals(Constants.COMMON_SCRIPTS))) {
						File scriptDir = new File(path + pathname);
						if (scriptDir.isDirectory()) {
							List<String> scriptFilesList = new ArrayList<String>();
							File scriptFiles = new File(path + pathname);
							String[] filesArray;
							filesArray = scriptFiles.list();
							logger.info("Script Files =" + Arrays.toString(filesArray));
							if (filesArray != null) {
								for (String scriptFile : filesArray) {
									logger.info(scriptFile);
									if (scriptFile.contains(Constants.DOT_JAVA)) {
										scriptFilesList.add(scriptFile);
									}
								}
							}
							if(!scriptFilesList.isEmpty()) {
							files.put(pathname, scriptFilesList);
							logger.info("files = " + files);
							}
						}
					
					}
				}else if (type.equalsIgnoreCase(Constants.DESKTOP)) {
					File scriptDir = new File(path + pathname);
					if (scriptDir.isDirectory()) {
						List<String> scriptFilesList = new ArrayList<String>();
						File scriptFiles = new File(path + pathname);
						String[] filesArray;
						filesArray = scriptFiles.list();
						logger.info("Script Files =" + Arrays.toString(filesArray));
						if (filesArray != null) {
							for (String scriptFile : filesArray) {
								logger.info(scriptFile);
								if (scriptFile.contains(Constants.DOTSAH) || scriptFile.contains(Constants.DOT_PY)) {
									scriptFilesList.add(scriptFile);
								}
							}
						}
						if(!scriptFilesList.isEmpty()) {
						files.put(pathname, scriptFilesList);
						logger.info("files = " + files);
						}
					}
				}else if(type.equalsIgnoreCase(Constants.CLI)) {
					File scriptDir = new File(path + pathname + File.separator + Constants.RECORDED_SCRIPTS_PATH);
					if (scriptDir.isDirectory()) {
						List<String> scriptFilesList = new ArrayList<String>();
						File scriptFiles = new File(path + pathname +File.separator + Constants.RECORDED_SCRIPTS_PATH);
						String[] filesArray;
						filesArray = scriptFiles.list();
						logger.info("Script Files =" + Arrays.toString(filesArray));
						if (filesArray != null) {
							for (String scriptFile : filesArray) {
								logger.info(scriptFile);
								if (scriptFile.contains(Constants.DOTTXT)) {
									scriptFilesList.add(scriptFile);
								}
							}
						}
						if(!scriptFilesList.isEmpty()) {
						files.put(pathname, scriptFilesList);
						logger.info("files = " + files);
						}
					}
				}
				/*
				 * else if (pathname.contains(Constants.DOT_JAVA) ||
				 * pathname.contains(Constants.DOTTXT)) { files.add(pathname); }
				 */
			}
		}
		return files;
	}

	public boolean createAppDirAndSaveScript(String path, String appName, String scriptName) throws IOException {
		File appRootDir = new File(path+File.separator+appName);
		if(!appRootDir.isDirectory() ){
			appRootDir.mkdir();
		}
		File scriptFile = null;
		if(scriptName.contains(Constants.DOTTXT)) {
			File appCliRootDir = new File(path+File.separator+appName+File.separator+Constants.RECORDED_SCRIPTS_PATH);
			if(!appCliRootDir.isDirectory() ){
				appCliRootDir.mkdir();
			}
			scriptFile=new File(path+File.separator+appName+File.separator+Constants.RECORDED_SCRIPTS_PATH+File.separator+scriptName);
		}else {
			scriptFile=new File(path+File.separator+appName+File.separator+scriptName);
		}
		
		if(scriptFile!=null) {
			if(!scriptFile.exists()) {
				if(scriptName.contains(Constants.DOT_JAVA)) {
					File source = new File(getClass().getClassLoader().getResource(Constants.COMMON_METHOD_FILE).getFile());
					if (copyFileUsingStream(source, scriptFile,scriptName))
						return true;
					else return false;
				}else if(scriptName.contains(Constants.DOT_PY)) {
					File source = new File(getClass().getClassLoader().getResource(Constants.PYTHON_DEFAULT_IMPORTS).getFile());
					if (copyPyFileUsingStream(source, scriptFile,scriptName))
						return true;
					else return false;
				}else {
					scriptFile.createNewFile();
				}
					
			}else {
				return false;
			}
		}else {
			return false;
		}
		return true;
	}
	
	private static boolean copyPyFileUsingStream(File source, File dest,String scriptName) throws IOException  {
		boolean statusFlag = false;
		StringBuffer sb=new StringBuffer();    //constructs a string buffer with no characters  
		try  
		{  
		String className = scriptName.substring(0,scriptName.indexOf("."));
		FileReader fr=new FileReader(source);   //reads the file  
		BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream  
		
		String line;  
		while((line=br.readLine())!=null)  
		{ 
		sb.append(line);      //appends line to string buffer  
		sb.append("\n");     //line feed   
		}  
		fr.close();    //closes the stream and release the resources  
		System.out.println("Contents of File: ");  
		System.out.println(sb.toString());   //returns a string that textually represents the object  
		}  
		catch(IOException e)  
		{  
			statusFlag = false;
		e.printStackTrace();  
		}  
		
		try {
		      FileWriter myWriter = new FileWriter(dest);
		      myWriter.write(sb.toString());
		      myWriter.close();
		      statusFlag = true;
		      System.out.println("Successfully wrote to the  file.");
		    } catch (IOException e) {
		    	statusFlag = false;
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		return statusFlag;
		
	}
	private static boolean copyFileUsingStream(File source, File dest,String scriptName) throws IOException {
		boolean statusFlag = false;
//		InputStream is = null;
//		OutputStream os = null;
//		try {
//			is = new FileInputStream(source);
//			os = new FileOutputStream(dest);
//			byte[] buffer = new byte[1024];
//			int length;
//			while ((length = is.read(buffer)) > 0) {
//				os.write(buffer, 0, length);
//			}
//			statusFlag = true;
//		}catch (Exception e) {
//			// TODO: handle exception
//		} finally {
//			is.close();
//			os.close();
//		}
		StringBuffer sb=new StringBuffer();    //constructs a string buffer with no characters  
		try  
		{  
		String className = scriptName.substring(0,scriptName.indexOf("."));
		FileReader fr=new FileReader(source);   //reads the file  
		BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream  
		
		String line;  
		while((line=br.readLine())!=null)  
		{ 
		if(line.contains("public class AppSpecificMethods")) {
			line="public class "+className+"{";
		}
		sb.append(line);      //appends line to string buffer  
		sb.append("\n");     //line feed   
		}  
		fr.close();    //closes the stream and release the resources  
		System.out.println("Contents of File: ");  
		System.out.println(sb.toString());   //returns a string that textually represents the object  
		}  
		catch(IOException e)  
		{  
			statusFlag = false;
		e.printStackTrace();  
		}  
		
		try {
		      FileWriter myWriter = new FileWriter(dest);
		      myWriter.write(sb.toString());
		      myWriter.close();
		      statusFlag = true;
		      System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		    	statusFlag = false;
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		return statusFlag;
	}
}
