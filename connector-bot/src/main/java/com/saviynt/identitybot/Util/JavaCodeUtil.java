package com.saviynt.identitybot.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;

import com.saviynt.identitybot.CobotApplication;
import com.saviynt.identitybot.constants.STATUS;
import com.saviynt.identitybot.controller.Status;

/**
 * 
 * @author Srinivasa Reddy Challa, Raj Kumar
 * 
 *         Util Class to Transform Raw Selenium script to Parameterized Script
 *
 */
public class JavaCodeUtil {

	final static Logger logger = Logger.getLogger(JavaCodeUtil.class);

	/**
	 * Method to transform Raw Selenium script to Parameterized Script
	 * 
	 * @param src    : Source location of the Selenium Script file to be read from
	 * @param dest   : File Location the selenium Script has to be written to after
	 *               script transformation
	 * @param sheet2
	 * @param row
	 */
	public static Status convertSeleniumCode(File src, File dest, String appName, int row, Sheet sheetparam,
			String browserType) {
		Pattern pattern = Pattern.compile("<<.*?>>");
		Pattern pat = Pattern.compile("#.*?#");
		Status status = new Status();
		BufferedReader reader = null;
		PrintWriter writer = null;
		Map<String, Object> dataMap = new LinkedHashMap<>();
		Map<String, Object> orMap = new LinkedHashMap<>();
		logger.info("enter into convertSeleniumCode method");
		try {
			String fileName = src.getAbsolutePath();
			logger.info("reading src file = " + src.getAbsolutePath());
			String rootDir = CobotApplication.loadCobotProperties().getProperty("scripts.dir");

			String excelFileName = rootDir + "TestDataManagement.xlsx";
			String orFile = rootDir + "ObjectRepositoryManagement.xlsx";
			logger.info("excelFileName: " + excelFileName);
			logger.info("orfile: " + orFile);
			reader = new BufferedReader(new FileReader(src.getAbsolutePath()));
			writer = new PrintWriter(dest);

			String className = fileName.substring(fileName.lastIndexOf(File.separatorChar) + 1).replace(".java", "");
			logger.info("className = " + className);

			orMap = ExcelReader.readObjectRepository(appName, orFile);
			
			dataMap = ExcelReader.readTestDataManagement(excelFileName);
			

			String line = reader.readLine();
			String lastLine = null;
			String prevLine = "";
			String locatorName = "";
			String elementName = "";
			String checkElementExist = "";

			boolean initTestMetnodConvert = false;
			boolean addedImports = false;
			boolean isAribaApplication = false;

			ArrayList<String> javaCodeList = new ArrayList<String>();
			// logger.info("line = "+line);
			while (line != null) {
				
				Pattern pat1 = Pattern.compile("^//.*");
				Matcher m1 = pat1.matcher(line.trim());
				if (m1.find()) {
					line = reader.readLine();
					continue;
				}

				Matcher m = pat.matcher(line);
				while (orMap.size() > 0 && m.find()) {
					String temp = line;
					if (!temp.trim().contains("ScriptReadApplication")) {
						String orKey = m.group();
						String actKey = orKey.replace("#", "").trim();
						String value = String.valueOf(orMap.getOrDefault(actKey, actKey));
						line = line.replace(orKey, value);
					}
				}
				Matcher matcher = pattern.matcher(line);
				while (dataMap.size() > 0 && matcher.find()) {
					String inputKey = matcher.group();
					String inputKey1 = inputKey.replace("<<", "").replace(">>", "").trim();
					String value ="";
					if (row != 0) {
						String field = inputKey1.split("\\.")[2];
						String rowkey = appName + "." + "TestData" + row + "." + field;
						 value = String.valueOf(dataMap.getOrDefault(rowkey, ""));
						if(value.isEmpty()){
							 value = String.valueOf(dataMap.getOrDefault(inputKey1, ""));
						}
						
						line = line.replace(inputKey, value);
					}else {
						value = String.valueOf(dataMap.getOrDefault(inputKey1, ""));
						line = line.replace(inputKey, value);
					}
					if(value.isEmpty()){
						 throw new Exception("Value not found in TestDataManagement.xlsx for this key - " +inputKey1);
					}

				}
				if (initTestMetnodConvert) {
					if (StringUtils.contains(line, "driver.get(\"https://s1.ariba.com")) {
						isAribaApplication = true;
					}
					if (StringUtils.contains(line, "driver.switchTo().frame(")) {
						prevLine = line;
						line = "";
					} else if (StringUtils.contains(prevLine, "driver.switchTo().frame(")
							&& !line.trim().startsWith("{")) {
						prevLine = "";
						if (line.trim().contains("WebElement element")) {
							locatorName = StringUtils.substringsBetween(line.trim(), "driver.findElement(By.", "(")[0];
							elementName = line.trim()
									.replace("WebElement element = driver.findElement(By." + locatorName + "(\"", "");
							line = "WebElement element = getElement(" + "\"" + locatorName + "="
									+ elementName.replace("));", ");");
							checkElementExist = "checkElementExist(" + "\"" + locatorName + "="
									+ elementName.substring(0, elementName.lastIndexOf("));")) + ", data, dataCount)";
							String switchFrame = "noOfFrames = driver.findElements(By.tagName(\"iframe\")).size();\n\tfor (int i = 0; i < noOfFrames; i++) { \n\tdriver.switchTo().defaultContent();\n\tdriver.switchTo().frame(i);\n\titeration = i;\n\tif("
									+ checkElementExist + ") {\n\tbreak;\n\t}\n\t}\n";
							line = updateTestMethodContents(switchFrame + "\n {\n" + line, isAribaApplication,
									javaCodeList);
							if (isAribaApplication) {

							}
						} else {
							if (line.trim().length() > 0) {
								String prefix = line.trim().substring(0, line.trim().lastIndexOf(").") - 1);
								locatorName = StringUtils.substringsBetween(prefix, "driver.findElement(By.", "(")[0];
								elementName = line.trim().replace("driver.findElement(By." + locatorName + "(\"", "");
								checkElementExist = "checkElementExist(" + "\"" + locatorName + "="
										+ elementName.substring(0, elementName.lastIndexOf("))."))
										+ ", data, dataCount)";
								String switchFrame = "noOfFrames = driver.findElements(By.tagName(\"iframe\")).size();\n\tfor (int i = 0; i < noOfFrames; i++) { \n\tdriver.switchTo().defaultContent();\n\tdriver.switchTo().frame(i);\n\titeration = i; \n\tif("
										+ checkElementExist + ") {\n\tbreak;\n\t}\n\t}\n";
								line = updateTestMethodContents(line, isAribaApplication, javaCodeList);
								line = updateTestMethodContents(switchFrame + "\n\t" + line, isAribaApplication,
										javaCodeList);
							}

						}
					} else if (line.contains("assertThat(driver.switchTo().alert().getText()")) {
						line = "if(StringUtils.equalsIgnoreCase(data.get(dataCount[0]++), \"OK\")) {\n\tdriver.switchTo().alert().accept();\n\t} else {\n\tdriver.switchTo().alert().dismiss();\n\t}";
					} else if (line.contains("driver.switchTo().alert().accept();")
							|| line.contains("driver.switchTo().alert().dismiss();")) {
						line = "";
					} else if (!line.trim().startsWith("{")) {
						prevLine = "";
						line = updateTestMethodContents(line, isAribaApplication, javaCodeList);
						if (line.trim().startsWith("WebElement element = getElement(\"tagName=body\");")) {
							line = "tagName=body";
							javaCodeList.remove(javaCodeList.size() - 1);
						} else if (StringUtils.equals(javaCodeList.get(javaCodeList.size() - 1).trim(),
								"tagName=body")) {
							if (line.trim().equals("}")) {
								javaCodeList.remove(javaCodeList.size() - 1);
							}
							line = "";
						}
					} else if (line.trim().contains("driver.manage().window().setSize(new Dimension(")) {
						line = "";
					} else if (line.trim().equals("Actions builder = new Actions(driver);")) {
						line = "";
					} else {
						line = updateTestMethodContents(line, isAribaApplication, javaCodeList);
					}
				}
				if (line.contains("public void waitForElement")) {
					line = "";
				}
				if (line.contains("// Generated by Selenium IDE")) {
					line = "";
				} else if (!addedImports && line.trim().startsWith("import ")) {
					logger.info("adding imports");
					addImports(javaCodeList);
					addedImports = true;
				} else if (line.trim().contains("public void ") && lastLine != null
						&& lastLine.trim().contains("@Test")) {
					initTestMetnodConvert = true;
					line = "public void test(List<String> data, List<String> searchData) throws Exception {\n\tint[] dataCount={0}; \n\tint[] searchDataCount= {0}; \n\tint noOfFrames = 0; \n";
				} else if (line.trim().contains("public class")) {
					String exceptionClass = "\nclass SearchTextNotFoundException extends Exception{\n\tprivate static final long serialVersionUID = 1L;\n\tSearchTextNotFoundException(){}\n\tpublic SearchTextNotFoundException(String message) {\n\t\tsuper(message);\n\t}\n} \n";
					line = exceptionClass + "\npublic class " + className
							+ "{ \n\tString prevElementId = \"\"; \n\tString foundId = \"\";\n\tint iteration = 0;\nstatic String gloabalScriptVariableUniqueKay = \"\";\nstatic JSONObject globalDataList=new JSONObject();\nstatic JSONObject parentDataList = null;";
				} else if (line.trim().startsWith("package com.example.tests")) {
					line = "";
				} else if (line.trim().contains("driver = new ChromeDriver();")) {
					if (browserType.equals(Constants.CHROME)) {
						String incongnitoCrome = "\n\tChromeOptions options = new ChromeOptions();\n\toptions.setExperimentalOption(\"useAutomationExtension\", false);\n\tMap<String, Object> prefs = new HashMap<String, Object>();\n\t"
								+ "\n\toptions.setExperimentalOption(\"excludeSwitches\", new String[] {\"enable-automation\"});\n\tprefs.put(\"credentials_enable_service\", false);\n\tprefs.put(\"profile.password_manager_enabled\", false);\n\toptions.setExperimentalOption(\"prefs\", prefs);"
								+ " DesiredCapabilities capabilities = DesiredCapabilities.chrome(); \n\tcapabilities.setCapability(ChromeOptions.CAPABILITY, options); ";

						line = incongnitoCrome
								+ " \n\tdriver = new ChromeDriver(capabilities);\n\tdriver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS) ;";
					} else if (browserType.equals(Constants.MOZILLA)) {
						line = "\ndriver = new FirefoxDriver();";
					} else if (browserType.equals(Constants.IE)) {
						line = "\ndriver=new InternetExplorerDriver();";
					} else if (browserType.equals(Constants.MICROSOFTEDGE)) {
						line = "\ndriver = new EdgeDriver();";
					} else if (browserType.equals(Constants.SAFARI)) {
						line = "\ndriver = new SafariDriver();";
					}

				}
				if (line.trim().contains("driver.quit()")) {
					line = "try {driver.quit();}catch (Exception e){System.out.println(\"Nothing to do with it\");}";
				}
				if (line.trim().contains("Keys.ENTER);")) {
					if (StringUtils.contains(javaCodeList.get(javaCodeList.size() - 1).trim().split("data.get\\(")[0],
							line.trim().split("Keys.ENTER")[0])) {
						javaCodeList.set(javaCodeList.size() - 1, javaCodeList.get(javaCodeList.size() - 1)
								.replace("data.get(dataCount[0]++));", "data.get(dataCount[0]++) + Keys.ENTER);"));
						line = "";
					} else {
						javaCodeList.add("\t" + line);
					}
				} else {
					String targetValue = "";
					if (line.contains("(") && (line.contains("click();") || line.contains("enterText("))) {
						if(StringUtils.substringsBetween(line.trim(), "(\"", "\"")!=null) {
							targetValue = StringUtils.substringsBetween(line.trim(), "(\"", "\"")[0];
							if (StringUtils.contains(javaCodeList.get(javaCodeList.size() - 3).trim(),
									"WebElement element = getElement(\"" + targetValue)) {
								javaCodeList.remove(javaCodeList.size() - 1);
								javaCodeList.remove(javaCodeList.size() - 1);
								javaCodeList.remove(javaCodeList.size() - 1);
								javaCodeList.remove(javaCodeList.size() - 1);
							}
						}
					}
				}
				if (!line.equals("")) {
					lastLine = line;
					javaCodeList.add("\t" + line);
				}
				line = reader.readLine();
			}

			reader.close();
			javaCodeList.remove(javaCodeList.size() - 1);
			logger.info("adding method");
			addMethods(javaCodeList, className, browserType);

			javaCodeList.add("}");

			for (String entry : javaCodeList) {
				writer.println(entry);
			}

			writer.flush();
			writer.close();
			status.setJobId(String.valueOf(STATUS.Success.getID()));
		} catch (IOException e) {
			logger.info("ERROR >>> while convertSeleniumCode = " + e.getMessage());
			status.setJobId(String.valueOf(STATUS.Failed.getID()));
			status.setReason(e.getMessage());
		} catch (Exception e1) {

			status.setJobId(String.valueOf(STATUS.Failed.getID()));
			status.setReason(e1.getMessage());
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					status.setJobId(String.valueOf(STATUS.Failed.getID()));
					status.setReason(e.getMessage());
				}
			}
			if (writer != null) {
				writer.close();
			}
		}
		return status;
	}

	/**
	 * Add the addition Imports required with additional code added to Raw selenium
	 * script file
	 * 
	 * @param javaCodeList
	 * @throws Exception
	 */

	public static void addImports(ArrayList<String> javaCodeList) throws Exception {
		BufferedReader javaImportsReader = null;
		InputStream javaMainstream = null;
		try {
			logger.info("add imports from /templates/java_imports.txt file");
			javaMainstream = JavaCodeUtil.class.getResourceAsStream("/templates/java_imports.txt");

			javaImportsReader = new BufferedReader(new InputStreamReader(javaMainstream));

			String line = javaImportsReader.readLine();
			logger.info("reading imports and adding to script");
			while (line != null) {
				javaCodeList.add(line);
				line = javaImportsReader.readLine();
			}
		} catch (IOException e) {
			javaImportsReader.close();
			throw new Exception("Adding import statments failed");
		}
	}

	/**
	 * Add the additional Methods to Raw Script file like methods to update the
	 * script execution status to cobot server ect
	 * 
	 * @param javaCodeList : Code that contains the transformed selenium code to
	 *                     which Additional methods are to be added
	 * @param className    : Name of the Script file that will be replaced in the
	 *                     method template placeholder
	 * @throws Exception
	 */

	public static void addMethods(ArrayList<String> javaCodeList, String className, String browserType)
			throws Exception {
		BufferedReader javaMainReader = null;
		InputStream javaMainstream = null;
		try {
			if (Constants.OPERATING_SYSTEM_NAME.startsWith(Constants.WINDOWS_OS)) {
				logger.info("reading /templates/java_methods.txt file");
				javaMainstream = JavaCodeUtil.class.getResourceAsStream("/templates/java_methods.txt");
			} else {
				logger.info("reading /templates/mac_java_methods.txt file");
				javaMainstream = JavaCodeUtil.class.getResourceAsStream("/templates/mac_java_methods.txt");
			}
			javaMainReader = new BufferedReader(new InputStreamReader(javaMainstream));

			String line = javaMainReader.readLine();

			while (line != null) {
				if (line.trim().contains("<CLASS_NAME>")) {
					line = line.trim().replaceAll("<CLASS_NAME>", className);
				}
				if (line.contains("System.setProperty")) {
					if (browserType.equals(Constants.CHROME)) {
						line = "System.setProperty(\"webdriver.chrome.driver\", driverpath);";
					} else if (browserType.equals(Constants.MOZILLA)) {
						line = "System.setProperty(\"webdriver.gecko.driver\", driverpath);";
					} else if (browserType.equals(Constants.IE)) {
						line = "System.setProperty(\"webdriver.ie.driver\", driverpath);";
					} else if (browserType.equals(Constants.MICROSOFTEDGE)) {
						line = "System.setProperty(\"webdriver.edge.driver\", driverpath);";
					}
				}
				javaCodeList.add("\t\t" + line);
				line = javaMainReader.readLine();
			}
		} catch (IOException e) {
			if (javaMainReader != null) {
				try {
					javaMainReader.close();
				} catch (IOException err) {
				}
			}
			throw new Exception("Add common method failed");
		}
	}

	public static String removeLast(String s, int n) {
		if (null != s && !s.isEmpty()) {
			s = s.substring(0, s.length() - n);
		}
		return s;
	}

	public static String updateTestMethodContents(String line, boolean isAribaApplication,
			ArrayList<String> javaCodeList) {
		if (!line.trim().contains("noOfFrames")) {
			if (line.trim().contains("driver.get(")) {
				line = line.trim().replace("driver.get(", "\tdriver.navigate().to(");
				javaCodeList.add(line);
				line = "\t driver.manage().window().maximize();";
			} else if (line.trim().contains(".sendKeys(")) {
				String locatorName = StringUtils.substringsBetween(line.trim(), "driver.findElement(By.", "(")[0];
				String elemantName = line.trim().split(".sendKeys")[0].trim();
				if (!line.trim().contains(".sendKeys(Keys.ENTER);")) {
					String parameterStrArray[] = line.trim().split(".sendKeys");

					String parameterStr = "";
					if (parameterStrArray[1].startsWith("(parentDataList")) {
						parameterStr = parameterStrArray[1].trim().replace("(p", "p").replace(");", "");
					} else {
						parameterStr = parameterStrArray[1].trim().replace("(\"", "").replace("\");", "");
					}

					String parameter = null;
					if (parameterStr.contains(";")) {
						if (parameterStr.startsWith("(") && parameterStr.endsWith(");")) {
							parameterStr = parameterStr.substring(1).replace(");", "");
							parameter = parameterStr;
						} else if (parameterStr.endsWith(";")) {
							// for sql queries
							parameter = "\"" + parameterStr + "\"";
						}

					} else {
						if (parameterStr.startsWith("parentDataList")) {
							parameter = parameterStr;
						} else {
							parameter = "\"" + parameterStr + "\"";
						}

					}
					
					line = "enterText(getElement(" + "\"" + locatorName + "="
							+ elemantName.trim().replace("driver.findElement(By." + locatorName + "(\"", "")
								.replace("))", ")," + parameter + ");");
					

				} else {
					line = "enterText(getElement(" + "\"" + locatorName + "="
							+ elemantName.trim().replace("driver.findElement(By." + locatorName + "(\"", "")
									.replace("))", "), Keys.ENTER);");
				}
			} else if (line.trim().contains("selectByVisibleText(")) {
				String prefix = line.trim().substring(0, line.trim().lastIndexOf(".selectByVisibleText(") + 1);
				String suffix = line.trim().substring(line.trim().lastIndexOf(".selectByVisibleText(") + 1);
				int quoteIndex = suffix.indexOf("\"");
				if (quoteIndex != -1) {
					String ops = suffix.substring(0, suffix.indexOf("\""));
					ops = ops + "data.get(dataCount[0]++));";
					line = prefix + ops;
				}
			} else if (line.trim().contains("WebElement dropdown = driver.findElement")) {
				line = line.trim().replace(line.trim().substring(0, line.trim().indexOf("=") + 1), "").trim();
				line = line.trim().substring(0, line.trim().lastIndexOf(";"));
				line = "Select select = new Select(" + line + ");";
			} else if (line.trim().contains("dropdown.findElement")
					|| line.trim().contains("driver.manage().window().setSize(new Dimension(")) {
				Pattern pattern1 = Pattern.compile("option\\[.*]");
				Matcher m1 = pattern1.matcher(line.trim());
				if (m1.find()) {
					String opt = m1.group();
					int i = opt.indexOf("=");
					opt.substring(i);
					opt = opt.substring(i+1,opt.length()-1);
					opt =opt.trim().replace("\'", "") ;
					line = "select.selectByVisibleText(\""+opt+"\");";
				}
				
			} else if (line.trim().contains("textvalidator_cobot_div")) {
				line = "if (!validateText(searchData, searchDataCount)) { \n\tthrow new SearchTextNotFoundException(\"Search text not found.\"); \n\t}";
			} else if (line.trim().endsWith(".click();") && !line.contains("dropdown.findElement")) {
				if(line.contains("driver.findElement(")){
					String locatorName = StringUtils.substringsBetween(line.trim(), "driver.findElement(By.", "(")[0];
					String elemantName = line.trim().replace("driver.findElement(By." + locatorName + "(\"", "")
							.replace(")).click();", "");
					line = "click(" + "\"" + locatorName + "=" + elemantName + ",data,dataCount);";
				}
				
			} else if (line.trim().startsWith("WebElement element = driver.findElement(By")) {
				String locatorName = StringUtils.substringsBetween(line.trim(), "driver.findElement(By.", "(")[0];
				String elemantName = line.trim()
						.replace("WebElement element = driver.findElement(By." + locatorName + "(\"", "");
				line = "WebElement element = getElement(" + "\"" + locatorName + "="
						+ elemantName.replace("));", ");\n\tActions builder = new Actions(driver);");
			} else if (line.trim().contains("builder.moveToElement")) {
				if (isAribaApplication) {
					if (StringUtils.equals(line.trim(), "builder.moveToElement(element).clickAndHold().perform();")) {
						line = "builder.moveToElement(element).click().perform();";
					} else if (StringUtils.equals(line.trim(), "builder.moveToElement(element).release().perform();")) {
						line = "";
					} else {
						line = "if(element != null && element.isDisplayed()) { \n\t" + line + "\n\t}";
					}
				} else {
					line = "if(element != null && element.isDisplayed()) { \n\t" + line + "\n\t}";
				}
			} else if (line.trim().equals("Actions builder = new Actions(driver);")) {
				line = "";
			} else if (line.trim().contains("customReport")) {
				String tempLine = line.trim().replace("customReport(", "").replace(");", "");

				logger.info("$" + tempLine);
				line = "customReportLog(" + tempLine + ");";
				logger.info("$$$$$$$$$$$" + line);
			} else if (line.trim().contains("validateElement")) {
				String tempLine = line.trim().replace("validateElement(", "").replace(");", "");

				logger.info("$" + tempLine);
				line = "validateElementLog(" + tempLine + ");";
				logger.info("$$$$$$$$$$$" + line);
			}
		}
		return line;
	}

}
