package com.saviynt.identitybot.Util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.json.JSONArray;
import org.json.JSONObject;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.utils.SourceRoot;
import com.saviynt.identitybot.constants.STATUS;
import com.saviynt.identitybot.controller.Status;
import com.saviynt.identitybot.model.Job;

/**
 * 
 * @authors Srinivasa Reddy Challa, Raj Kumar
 * 
 *          Util Class to Transform, Compile And Execute Script File
 */

public class ScriptUtil {

	final static Logger logger = Logger.getLogger(ScriptUtil.class);
	final static String APP_NAME = "testbot";
	 static String method = "";

	/**
	 * Method to Transform the Raw Selenium File to Parameterized File so that it
	 * can be made generic & reusable and run with different params
	 * @param sheet 
	 * @param row 
	 * 
	 * @param scriptFile:     Raw Selenium Script File
	 * @param scriptsRepoDir: Directory location where Raw Scripts are stored
	 * @param scriptRootDir:  Directory location where Parameterized Scripts are
	 *                        stored after transformation
	 */
	public static Status transformScriptFile(Job job, String scriptsRepoDir, String scriptRootDir,String appName, int row, Sheet sheet) {
		logger.info("enter transformScriptFile method");
		//String appName = job.getScriptFileName().replace(Constants.DOT_JAVA, "").trim();
		logger.info("appName = "+appName);
		String scriptFromRepo = scriptsRepoDir + appName + File.separator + job.getScriptFileName();		
		logger.info("scriptFromRepo = "+scriptFromRepo);
		
		String transformedJavaFile = scriptRootDir + Constants.SUFFIX + job.getScriptFileName();		
		logger.info("transformedJavaFile = "+transformedJavaFile);
		Status status = new Status();
		try {
			File file = new File(transformedJavaFile);
			logger.info("checking and creating file if does not exists.");
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			boolean checkCoBotCommonCodeMethod = checkCoBotCommonCode(scriptFromRepo,"CallCustomeMethod");
			if(checkCoBotCommonCodeMethod) {
				scriptFromRepo = commonMethodParsing(scriptsRepoDir, job);
			}
			
			boolean checkCoBotCommonCodeScript = checkCoBotCommonCode(scriptsRepoDir + appName + File.separator + job.getScriptFileName(),"CallTestScript");
			if(checkCoBotCommonCodeScript) {
				scriptFromRepo =  scriptMethodParsing(scriptsRepoDir, job,checkCoBotCommonCodeMethod);
			}
			logger.info("calling convertSeleniumCode by using scriptFromRepo = "+scriptFromRepo+" and transformedJavaFile = "+transformedJavaFile);
			status = JavaCodeUtil.convertSeleniumCode(new File(scriptFromRepo), new File(transformedJavaFile),appName,row ,sheet,job.getBrowserType());
			logger.info("exporting started"+transformedJavaFile+"----repo"+scriptFromRepo);
			
		}catch(Exception e) {
			status.setJobId(String.valueOf(STATUS.Failed.getID()));
			status.setReason(e.getMessage());
		}
		return status;
	}
	
	
	private static String commonMethodParsing(String scriptsRepoDir, Job job) throws Exception {
		
		String scriptFromRepo = scriptsRepoDir + File.separator+ job.getAppName() + File.separator + job.getScriptFileName();
		String commonFilePath = scriptsRepoDir +File.separator+ Constants.COMMON_SCRIPTS + File.separator + job.getScriptFileName();
		File dest = new File(commonFilePath);
		File directory = new File(String.valueOf(scriptsRepoDir + Constants.COMMON_SCRIPTS));
		try {
			if (!directory.exists()) {
				directory.mkdir();
			}
			if (!dest.exists()) {
				dest.createNewFile();
			}
			boolean fileCopied = copyFileUsingStream(new File(scriptFromRepo), dest);
			if (fileCopied) {
				logger.info("File successfully copied form script dir to common dir");
				String srcMethodFile = scriptsRepoDir + Constants.COMMON_FOLDER + File.separator
						+ Constants.COMMON_METHOD_FILE;
				convertToGenerateCommonMethodScript(commonFilePath, "CallCustomeMethod",
						srcMethodFile, scriptsRepoDir + Constants.COMMON_FOLDER);
			}

		} catch (Exception e) {
          throw new Exception(e.getMessage());
		}
       return commonFilePath;
	}
	
	private static String scriptMethodParsing(String scriptsRepoDir, Job job, boolean checkCoBotCommonCodeMethod) throws Exception {

		String commonFilePath = scriptsRepoDir + Constants.COMMON_SCRIPTS + File.separator + job.getScriptFileName();
		File dest = new File(commonFilePath);
		File directory = new File(String.valueOf(scriptsRepoDir + Constants.COMMON_SCRIPTS));
		try {
			if (!checkCoBotCommonCodeMethod) {
				if (!directory.exists()) {
					directory.mkdir();
				}
				if (!dest.exists()) {
					dest.createNewFile();
				}
				copyFileUsingStream(checkCoBotCommonCodeMethod ? dest: new File(scriptsRepoDir+File.separator + job.getAppName() + File.separator + job.getScriptFileName()),dest);
				logger.info("File successfully copied form script dir to common dir");
			}
			convertToGenerateCommonCodecript(commonFilePath, "CallTestScript", scriptsRepoDir);
		} catch (Exception e) {
			  throw new Exception(e.getMessage());
		}

		return commonFilePath;
	}

	private static void  convertToGenerateCommonCodecript(String src, String value,String scriptsRootDir) throws Exception {
		try {
			ArrayList<String> generateScript = new ArrayList<String>();
			File commonMethodsFile = new File(src);
			if(commonMethodsFile.exists()) {
				FileReader fr = new FileReader(commonMethodsFile); 
				BufferedReader br = new BufferedReader(fr); 
				String line;
				Map<String,String> map = new HashMap<>();
				while ((line = br.readLine()) != null) {
					if (line.contains(value)) {
						String methodDetails = null;
					     Matcher m1 = Pattern.compile("\\(([^)]+)\\)").matcher(line);
					     while(m1.find()) {
					    	 methodDetails = m1.group(1);    
					     }
					     String[] methodDetailsArray = methodDetails.split(",");
					     if(methodDetailsArray.length==2) {
					    	 String applicationName = methodDetailsArray[0];
					    	 String scriptName = methodDetailsArray[1];
					    	 String filePath = scriptsRootDir+applicationName;
					    	 File checkfile = new File(filePath+ File.separator+scriptName+Constants.DOT_JAVA);
					    	 if(!checkfile.exists()) {
					    		 throw new Exception("Either Script Name or Application name is wrong :-" +scriptName);
					    	 }
					    	 map.put(scriptName, filePath);
					    	 
					    	 String firstLetStr = scriptName.substring(0, 1);
					         String remLetStr = scriptName.substring(1);
					    	 line = firstLetStr.toLowerCase()+remLetStr+"(data,dataCount);";
					     }else {
					    	 throw new Exception("Either Script Name or Application name not provided in CallTestScript");
					     }
					}
					generateScript.add(line);
				}
				fr.close(); 
				
				removeEnd(generateScript);
				for (Map.Entry<String, String> set : map.entrySet()) {

					String mdetail = getScriptMethod(set.getKey(), set.getValue(), set.getKey() + Constants.DOT_JAVA);
					if(mdetail!=null) {
						mdetail = mdetail.replace("driver.close();", "");
						generateScript.add(mdetail);
					}else {
						throw new Exception("Either Script Name or Application name not provided wrong  in CallTestScript");
					}

				}

				generateScript.add("}");
				
				PrintWriter writer = new PrintWriter(src);
				for (String entry : generateScript) {
					writer.println(entry);
				}

				writer.flush();
				writer.close();
				logger.info("convertToGenerateScript success");
			}else {
				 throw new Exception("Selected script does not exist" );
			}
			
		} catch (Exception e) {
			logger.error(e);
			throw new Exception(e.getMessage());
		}
	}

	public static boolean checkCoBotCommonCode(String src,String value) throws Exception {
		boolean checkCommonMethod = false;
		try {
			File commonMethodsFile = new File(src);
			if(commonMethodsFile.exists()) {
				FileReader fr = new FileReader(commonMethodsFile); // reads the file
				BufferedReader br = new BufferedReader(fr); // creates a buffering character input stream
				String line;
				while ((line = br.readLine()) != null) {
					if (line.contains(value)) {
						checkCommonMethod = true;
						break;
					}
				}
				fr.close(); // closes the stream and release the resources
			}else {
				logger.error(commonMethodsFile.getAbsoluteFile() +"Not exists");
				throw new Exception(commonMethodsFile.getAbsoluteFile() +"Not exists");
			}
		} catch (Exception e) {
			logger.error(e);
			throw new Exception(e.getMessage());
		}
		return checkCommonMethod;
	}

	private static void  convertToGenerateCommonMethodScript(String src,String value,String srcMethodFile,String path) throws Exception {
		try {
			
			ArrayList<String> generateScript = new ArrayList<String>();
			File commonMethodsFile = new File(src);
			if(commonMethodsFile.exists()) {
				FileReader fr = new FileReader(commonMethodsFile); // reads the file
				BufferedReader br = new BufferedReader(fr); // creates a buffering character input stream
				String line;
				Set<String> mList = new HashSet<>();
				while ((line = br.readLine()) != null) {
					if (line.contains(value)) {
						String methodDetails = null;
					     Matcher m1 = Pattern.compile("\\((.*)\\)").matcher(line);
					     while(m1.find()) {
					    	 methodDetails = m1.group(1);    
					     }
					     
					     methodDetails = methodDetails.replace(");", "");
					     if(methodDetails.trim().endsWith("(")) {
					    	 methodDetails = methodDetails+"data,dataCount);";
					     }else {
					    	 methodDetails = methodDetails+",data,dataCount);";
					     }
					        line= methodDetails;
					        int s = methodDetails.indexOf("(");
					    	 String methodName = methodDetails.substring(0,s);
					    	 if(methodName.contains("=")) {
					    		 methodName = methodName.split("=")[1].trim() ;
					    	 }
					    	 mList.add(methodName);
					}
					generateScript.add(line);
				}
				fr.close(); // closes the stream and release the resources
				removeEnd(generateScript);
				
				for (String mName : mList) {
					String mdetail = getcommoncodeMethod(mName, path);
					if (mdetail == null || mdetail.isEmpty()) {
						throw new Exception(mName +" - method does not exist in common librarry");
					}
					generateScript.add(mdetail);
				}
				generateScript.add("}");
				PrintWriter writer = new PrintWriter(src);
				for (String entry : generateScript) {
					writer.println(entry);
				}

				writer.flush();
				writer.close();
				logger.info("convertToGenerateScript success");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	private static void removeEnd(List<String> generateScript) {

		for (int i = generateScript.size() - 1; i > 0; i--) {

			if (generateScript.get(i).trim().isEmpty()) {
				continue;
			} else if (generateScript.get(i).trim().endsWith("}")) {
				generateScript.remove(i);
				break;
			}

		}

	}
	
	/**
	 * Method to compile the Parameterized Script file
	 * 
	 * @param scriptFile:    Name for the Parameterized Script file
	 * @param scriptRootDir: Directory location where Parameterized Scripts are
	 *                       located
	 * @throws Exception
	 */
	public static Status compileScriptFile(Job job, String scriptRootDir, String executeMode,String appName) throws Exception {
		Status status = new Status();
		
		logger.info("enter into compileScriptFile method");
		//String appName = job.getScriptFileName().replace(Constants.DOT_JAVA, "").trim();
		logger.info("appName = "+appName);
		String transformedJavaFile = scriptRootDir + Constants.SUFFIX + job.getScriptFileName();
		logger.info("transformedJavaFile = "+transformedJavaFile);
		String classpath = "";
		
		logger.info("Servlet Container = "+Constants.SERVLET_CONTAINER);
		if (Constants.SERVLET_CONTAINER.equals(executeMode)) {
			String catilanaHome = System.getProperty(Constants.CATALINA_HOME);
			logger.info("catilanaHome = "+catilanaHome);
			if (catilanaHome != null) {
				classpath = catilanaHome + Constants.WEBAPPS + APP_NAME + Constants.WEB_INF_LIB;
			}
			logger.info("classpath = "+classpath);
		} else {
			classpath = System.getProperty(Constants.JAVA_CLASS_PATH);
			logger.info("classpath = "+classpath);
		}

		String osName = System.getProperty(Constants.OS_NAME);
		logger.info("osName = "+osName);
		if (StringUtils.startsWithIgnoreCase(osName.trim(), Constants.WIN)) {
			classpath = Constants.FORWARD_SLASH + classpath + Constants.FORWARD_SLASH;
		}
		logger.info("classpath = "+classpath);

		logger.info("compiling the java file.");
		Process process = Runtime.getRuntime().exec(Constants.JAVAC_CP + classpath + Constants.SPACE +transformedJavaFile);
		
		
		StreamReader errorReader = new StreamReader(process.getErrorStream(), Constants.ERROR.toUpperCase());
		logger.info("compile response errorReader = "+errorReader.toString());
		try {
			InputStreamReader isr = new InputStreamReader(process.getErrorStream());
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			String error = "";
			int len = 0;
			while ((line = br.readLine()) != null) {
				if (!line.contains("Note:")) {
					status.setJobId(String.valueOf(STATUS.Failed.getID()));
					error += line + Constants.csvNewLine;
					logger.info("SCRIPT" + " > " + line);
					if(error.length() > 500)
						break;
				}
			}
			if(!error.isEmpty() && status.getJobId().equalsIgnoreCase(String.valueOf(STATUS.Failed.getID()))) {
				status.setReason(error);
			}else {
				status.setJobId(String.valueOf(STATUS.Success.getID()));
			}

		} catch (IOException ioe) {
			logger.error("ERROR >>> while compiling the script file = "+ioe.getMessage());
			status.setJobId(String.valueOf(STATUS.Failed.getID()));
			status.setReason(ioe.getMessage());
		}
		StreamReader outputReader = new StreamReader(process.getInputStream(), Constants.OUTPUT);
		logger.info("compile response outputReader = "+outputReader.toString());
		//status.setJobId(String.valueOf(STATUS.Success.getID()));
		errorReader.start();
		outputReader.start();

		process.waitFor();
		logger.info("compilation Finished");
		return status;
	}

	/**
	 * Method to compile the Parameterized Script file
	 * 
	 * @param scriptFile:    Name for the Parameterized Script file
	 * @param scriptRootDir: Directory location where Parameterized Scripts are
	 *                       located
	 * @throws Exception
	 */
	public static String compileScriptFileContent(String scriptFile, String executeMode) throws Exception {
		String error = new String();
		String transformedJavaFile = scriptFile;

		String classpath = "";

		if (Constants.SERVLET_CONTAINER.equals(executeMode)) {
			String catilanaHome = System.getProperty(Constants.CATALINA_HOME);
			if (catilanaHome != null) {
				classpath = catilanaHome + Constants.WEBAPPS + APP_NAME + Constants.WEB_INF_LIB;
			}
		} else {
			classpath = System.getProperty(Constants.JAVA_CLASS_PATH);
		}

		String osName = System.getProperty(Constants.OS_NAME);

		if (StringUtils.startsWithIgnoreCase(osName.trim(), Constants.WIN)) {
			classpath = Constants.FORWARD_SLASH + classpath + Constants.FORWARD_SLASH;
		}
		String compliePath =Constants.JAVAC_CP + classpath + Constants.SPACE + transformedJavaFile;
		System.out.println(compliePath);

		Process process = Runtime.getRuntime().exec(Constants.JAVAC_CP + classpath + Constants.SPACE + transformedJavaFile);

		StreamReader errorReader = new StreamReader(process.getErrorStream(), Constants.ERROR.toUpperCase());
		try {
			InputStreamReader isr = new InputStreamReader(process.getErrorStream());
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				error += line + Constants.csvNewLine;
				logger.info("SCRIPT" + " > " + line);
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		StreamReader outputReader = new StreamReader(process.getInputStream(), Constants.OUTPUT);

		errorReader.start();
		outputReader.start();

		process.waitFor();
		logger.info("compilation Finished");
		return error;
	}

	/**
	 * Method To execute Script class in a separate java process
	 * 
	 * @param scriptFileName:    Name modified selenium script file to be executed
	 * @param data:              Input data for the script while executing
	 * @param scriptRootDir:     Directory location where transformed script files
	 *                           are located
	 * @param statusCallBackUrl: Cobot API for the script to update the status of
	 *                           script execution
	 * @param appJobID:          Task ID assigned for the script
	 * @throws IOException
	 * @throws InterruptedException
	 */

	public static void runScriptFile(String scriptFileName, String data, String scriptRootDir, String statusCallBackUrl,
			String appJobID) throws IOException, InterruptedException {

		String scriptclassFile = scriptFileName.replace(Constants.DOT_JAVA, "");

		String classpath = System.getProperty(Constants.JAVA_CLASS_PATH);
		String cromeDriverpath = System.getProperty(Constants.WEBDRIVER_CHROME_DRIVER);

		Process runscript = Runtime.getRuntime().exec(Constants.JAVAC_CP + Constants.FORWARD_SLASH + classpath + Constants.COLON + scriptRootDir + Constants.FORWARD_SLASH
				+ scriptclassFile + Constants.SPACE + data + Constants.SPACE + cromeDriverpath + Constants.SPACE + statusCallBackUrl + Constants.SPACE + appJobID);

		runscript.waitFor();

		InputStream errorStream = runscript.getErrorStream();
		StringWriter scriptRunWriter = new StringWriter();
		IOUtils.copy(errorStream, scriptRunWriter, Constants.UTF_8.toUpperCase());
		String runErrorStr = scriptRunWriter.toString();
		errorStream.close();
		logger.error(runErrorStr);

	}

	/**
	 * Method to execute script class by dynamically loading the script class file
	 * into the current loader
	 * @param appJobID2 
	 * 
	 * @param scriptFileName:    Name modified selenium script file to be executed
	 * @param data:              Input data for the script while executing
	 * @param scriptRootDir:     Directory location where transformed script files
	 *                           are located
	 * @param statusCallBackUrl: Cobot API for the script to update the status of
	 *                           script execution
	 * @param appJobID:          Task ID assigned for the script
	 * @throws IOException
	 * @throws InterruptedException
	 */

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public static Status loadAndRunClass(String scriptsRootDir, 
			String scriptFileName, 
			String content, 
			String scriptRootDir,
			String statusCallBackUrl, 
			String appJobID,String appName) throws Exception {
		Status status = new Status();
		try {
		//String appName = scriptFileName.replace(Constants.DOT_JAVA, "");
		logger.info("appName = "+appName);
		logger.info("enter loadAndRunClass method");
		File root = new File(scriptRootDir + File.separator);
		
		logger.info("converting content into JSONObject");
		JSONObject obj = new JSONObject(content);
		logger.info("obj = "+obj);
		
		logger.info("getting data field from above converted JSONObject and assigned to JSONArray");
		JSONArray jsonArray = obj.getJSONArray(Constants.DATA);
		logger.info("jsonArray = "+jsonArray);
		
		logger.info("assigned jsonarray values to list");
		List<Object> list = jsonArray.toList();
		List<String> data = new ArrayList<String>();
		for (Object a : list) {
			data.add(String.valueOf(a));
		}
		logger.info("data = "+data);
		
		logger.info("converting search into jsonarray ");
		jsonArray = obj.getJSONArray(Constants.SEARCH_DATA);
		list = jsonArray.toList();
		List<String> searchData = new ArrayList<String>();
		logger.info("assigned jsonarray values to search list");
		for (Object a : list) {
			searchData.add(String.valueOf(a));
		}
		logger.info("searchData = "+searchData);
		
		String parallelexecution = obj.getString(Constants.PARALLELEXECUTION_STATUS);
		Boolean recordVideo = false;
		if(parallelexecution.equalsIgnoreCase(Constants.ON)) {
			recordVideo = false;
		}else {
			recordVideo = obj.getBoolean(Constants.RECORD_VIDEO);
		}
		String format = obj.getString(Constants.FORMAT);
		URLClassLoader child = new URLClassLoader(new URL[] { root.toURI().toURL() },
				ScriptUtil.class.getClassLoader());
		logger.info("child = "+child);
		String scriptclassFile = scriptFileName.replace(Constants.DOT_JAVA, "");

		logger.info("scriptclassFile = "+scriptclassFile);
		Class classToLoad = Class.forName(scriptclassFile, true, child);
		logger.info("classToLoad calling getMethod");
		logger.info("scriptsRootDir = "+scriptsRootDir);
		Method method = classToLoad.getMethod(Constants.RUNTEST, String.class, java.util.List.class, java.util.List.class, Boolean.class, String.class, String.class, String.class);
		Object instance = classToLoad.newInstance();
		Object result = method.invoke(instance, scriptsRootDir + appName + File.separator , data, searchData, recordVideo, format, statusCallBackUrl, appJobID);
			status.setJobId(String.valueOf(STATUS.Success.getID()));
		logger.info("classToLoad ending getMethod");
		}catch (Exception e) {
			e.printStackTrace();
			status.setJobId(String.valueOf(STATUS.Failed.getID()));
			status.setReason(e.getMessage());
		}
		return status;
	}
	

	/**
	 * Method to execute script class by dynamically loading the script class file
	 * into the current loader
	 * @param appJobID2 
	 * 
	 * @param scriptFileName:    Name modified selenium script file to be executed
	 * @param data:              Input data for the script while executing
	 * @param scriptRootDir:     Directory location where transformed script files
	 *                           are located
	 * @param statusCallBackUrl: Cobot API for the script to update the status of
	 *                           script execution
	 * @param appJobID:          Task ID assigned for the script
	 * @throws IOException
	 * @throws InterruptedException
	 */

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public static Status loadAndRunClassForMac(String scriptsRootDir, String scriptFileName, String content, String scriptRootDir,
			String statusCallBackUrl, String appJobID, String appName) throws Exception {
		Status status = new Status();
		try {
		File root = new File(scriptRootDir);
		logger.info("appName = "+appName);
		JSONObject obj = new JSONObject(content);
		JSONArray jsonArray = obj.getJSONArray(Constants.DATA);
		List<Object> list = jsonArray.toList();
		List<String> data = new ArrayList<String>();
		for (Object a : list) {
			data.add(String.valueOf(a));
		}

		jsonArray = obj.getJSONArray(Constants.SEARCH_DATA);
		list = jsonArray.toList();
		List<String> searchData = new ArrayList<String>();
		for (Object a : list) {
			searchData.add(String.valueOf(a));
		}

		Boolean recordVideo = obj.getBoolean(Constants.RECORD_VIDEO);
		String format = obj.getString(Constants.FORMAT);
		URLClassLoader child = new URLClassLoader(new URL[] { root.toURI().toURL() },
				ScriptUtil.class.getClassLoader());

		String scriptclassFile = scriptFileName.replace(Constants.DOT_JAVA, "");

		Class classToLoad = Class.forName(scriptclassFile, true, child);
		Method method = classToLoad.getMethod(Constants.RUNTEST, java.util.List.class, java.util.List.class, Boolean.class, String.class, String.class, String.class);
		Object instance = classToLoad.newInstance();
		Object result = method.invoke(instance, scriptsRootDir+appName+File.separator, data, searchData, recordVideo, format, statusCallBackUrl, appJobID);
		status.setJobId(String.valueOf(STATUS.Success.getID()));
		logger.info("classToLoad ending getMethod");
		}catch (Exception e) {
			e.printStackTrace();
			status.setJobId(String.valueOf(STATUS.Failed.getID()));
			status.setReason(e.getMessage());
		}
		return status;
	}
	private static boolean copyFileUsingStream(File source, File dest) throws IOException {
		boolean fileCreated = false;
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	        fileCreated = true;
	    } finally {
	        is.close();
	        os.close();
	    }
	    return fileCreated;
	}
	
	private static String getcommoncodeMethod(String methodNames, String filepath) {

		StringBuffer sb = new StringBuffer();
		SourceRoot sourceRoot = new SourceRoot(Paths.get(filepath));

		CompilationUnit cu = sourceRoot.parse("", Constants.COMMON_METHOD_FILE);

		cu.accept(new ModifierVisitor<Void>() {
			@Override
			public Visitable visit(final MethodDeclaration n, final Void arg) {
				if (methodNames.equalsIgnoreCase(n.getNameAsString())) {
					
					String method  = n.getDeclarationAsString();
					int  ind  = method.indexOf(")");
					String  mName  = 	method.substring(0,ind);
					if(mName.trim().endsWith("(")) {
						mName = mName + " List<String> data , int[] dataCount) throws Exception ";
					}else {
						mName = mName + ", List<String> data , int[] dataCount) throws Exception ";
					}
					sb.append(mName + n.getBody().get().toString() + "\n");
				}
				return super.visit(n, arg);
			}
		}, null);
		return sb.toString();
	}

	private static String getScriptMethod(String methodNames, String filepath, String fileName) {

		StringBuffer sb = new StringBuffer();
		SourceRoot sourceRoot = new SourceRoot(Paths.get(filepath));

		CompilationUnit cu = sourceRoot.parse("", fileName);

		cu.accept(new ModifierVisitor<Void>() {
			@Override
			public Visitable visit(final MethodDeclaration n, final Void arg) {
				if (methodNames.equalsIgnoreCase(n.getNameAsString())) {
					
					String  mName  = n.getDeclarationAsString().replace(")", "");
					
					sb.append(mName + " List<String> data , int[] dataCount) throws Exception " + n.getBody().get().toString() + "\n");
				}
				return super.visit(n, arg);
			}
		}, null);
		return sb.toString();
	}
}
