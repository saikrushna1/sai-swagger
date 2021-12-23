package com.saviynt.identitybot.agent.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.json.Json;
import javax.json.JsonObject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.opencsv.CSVWriter;

import io.github.bonigarcia.wdm.WebDriverManager;


public class ScrapTableData {
	private WebDriver driver;

	@SuppressWarnings("deprecation")
	public void setUp() {
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("useAutomationExtension", false);
		Map<String, Object> prefs = new HashMap<String, Object>();
		
		options.setExperimentalOption("excludeSwitches", new String[] {"enable-automation"});
		prefs.put("credentials_enable_service", false);
		prefs.put("profile.password_manager_enabled", false);
		options.setExperimentalOption("prefs", prefs); DesiredCapabilities capabilities = DesiredCapabilities.chrome(); 
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);  
		driver = new ChromeDriver(capabilities);
		driver.manage().timeouts().implicitlyWait(180, TimeUnit.SECONDS);
	}

	public void tearDown() {
		driver.quit();
	}
	
	public void test(List<String> data, String fileName, String tableName) throws Exception {
		try {
			int[] count = { 0 };
			driver.navigate().to("https://planning-test-eklm-test.pbcs.us2.oraclecloud.com/HyperionPlanning");
			driver.manage().window().maximize();
			click("xpath", "//input[@id='username']", data, count);
			findElement(driver, By.xpath("//input[@id='username']")).sendKeys(data.get(count[0]++));
			click("xpath", "//input[@id='password']", data, count);
			findElement(driver, By.xpath("//input[@id='password']")).sendKeys(data.get(count[0]++));
			click("xpath", "//button[@id='signin']", data, count);
			waitForPageLoad();
			click("xpath", "//a[@id='cil3']", data, count);
			click("xpath", "//span[text()='Access Control']/parent::a", data, count);
			Thread.sleep(10000);
			scrapTableData(driver, fileName, tableName);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}
	
	void waitForPageLoad() {
		new WebDriverWait(driver, 120).until(
			      webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
	}	

	public void runTest(List<String> data, String fileName, String tableName, String callbackUrl, String jobid) {
		try {
			setUp();
			test(data, fileName, tableName);
			tearDown();

			JsonObject response = Json.createObjectBuilder().add("jobId", jobid).add("status", "Success")
					.add("reason", "").add("fileType", "Groups").add("fileName", "ImportGroups").build();

			sendExecutionStatus(callbackUrl, response);
		} catch (Exception e) {
			tearDown();

			JsonObject response = Json.createObjectBuilder().add("jobId", jobid).add("status", "Failed")
					.add("reason", e.getMessage()).add("fileType", "account").add("fileName", "").build();
			try {
				sendExecutionStatus(callbackUrl, response);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void click(String locatorName, String elementId, java.util.List<String> data, int[] count) throws Exception {
		List<WebElement> objects = getElements(locatorName, elementId);
		if (objects != null && objects.size() != 0 && !objects.get(0).isDisplayed()) {
			List<WebElement> parentObject = findRootElement(locatorName, elementId);
			if (parentObject != null && parentObject.size() != 0) {
				Actions builder = new Actions(driver);
				builder.moveToElement(parentObject.get(0)).perform();
			}
		}
		String type = objects.get(0).getAttribute("type");
		if (type != null && type.equals("radio")) {
			if (locatorName.equalsIgnoreCase("xpath")) {
				findElements(driver, By.xpath(elementId)).get(0).click();
				count[0]++;
			} else if (findElements(driver, By.xpath("//label[contains(.,'" + data.get(count[0])
					+ "')]/input[@type='radio'][@" + locatorName + "='" + elementId + "']")).size() != 0) {
				findElements(driver, By.xpath("//label[contains(.,'" + data.get(count[0]++)
						+ "')]/input[@type='radio'][@" + locatorName + "='" + elementId + "']")).get(0).click();
			} else if (findElements(driver, By.xpath("//input[contains(.,'" + data.get(count[0]) + "')][@type='radio'][@"
					+ locatorName + "='" + elementId + "']")).size() != 0) {
				findElements(driver, By.xpath("//input[contains(.,'" + data.get(count[0]++) + "')][@type='radio'][@"
						+ locatorName + "='" + elementId + "']")).get(0).click();
			}
		} else if (type != null && type.equals("checkbox")) {
			if (locatorName.equalsIgnoreCase("xpath")) {
				findElements(driver, By.xpath(elementId)).get(0).click();
				count[0]++;
			} else if (findElements(driver, By.xpath(locatorName.equalsIgnoreCase("xpath") ? elementId
					: "//label[contains(.,'" + data.get(count[0]) + "')]")).size() != 0) {
				findElement(driver, By.xpath(locatorName.equalsIgnoreCase("xpath") ? elementId
						: "//label[contains(.,'" + data.get(count[0]++) + "')]")).click();
			} else if (findElements(driver, By.xpath(locatorName.equalsIgnoreCase("xpath") ? elementId
					: "//input[contains(.,'" + data.get(count[0]) + "')]")).size() != 0) {
				findElement(driver, By.xpath(locatorName.equalsIgnoreCase("xpath") ? elementId
						: "//input[contains(.,'" + data.get(count[0]++) + "')]")).click();
			}
		} else {
			getElements(locatorName, elementId).get(0).click();
		}
	}

	public List<WebElement> findRootElement(String locatorName, String locatorValue) throws Exception {
		List<WebElement> objectsList = null;
		String parentProperty = System.getProperty("parentProperty");
		if (StringUtils.equals(locatorName, "name")) {
			objectsList = findElements(driver, By.xpath("//*[@name='" + locatorValue + "']" + parentProperty));
		} else if (StringUtils.equals(locatorName, "xpath")) {
			objectsList = findElements(driver, By.xpath(locatorValue + parentProperty));
		} else if (StringUtils.equals(locatorName, "linkText")) {
			objectsList = findElements(driver, By.xpath("//*[text()='" + locatorValue + "']" + parentProperty));
		} else if (StringUtils.equals(locatorName, "partialLinkText")) {
			objectsList = findElements(driver, By.xpath("//*[contains(text(),'" + locatorValue + "')] + parentProperty"));
		} else if (StringUtils.equals(locatorName, "id")) {
			objectsList = findElements(driver, By.xpath("//*[@id='" + locatorValue + "']" + parentProperty));
		} else if (StringUtils.equals(locatorName, "class")) {
			objectsList = findElements(driver, By.xpath("//*[@class='" + locatorValue + "']" + parentProperty));
		}
		return objectsList;
	}

	public List<WebElement> getElements(String locatorName, String elementId) throws Exception {
		List<WebElement> objects = new ArrayList<WebElement>();
		if (StringUtils.equals(locatorName, "name")) {
			return findElements(driver, By.name(elementId));
		} else if (StringUtils.equals(locatorName, "xpath")) {
			return findElements(driver, By.xpath(elementId));
		} else if (StringUtils.equals(locatorName, "cssSelector")) {
			return findElements(driver, By.cssSelector(elementId));
		} else if (StringUtils.equals(locatorName, "linkText")) {
			return findElements(driver, By.linkText(elementId));
		} else if (StringUtils.equals(locatorName, "partialLinkText")) {
			return findElements(driver, By.partialLinkText(elementId));
		} else if (StringUtils.equals(locatorName, "id")) {
			return findElements(driver, By.id(elementId));
		} else if (StringUtils.equals(locatorName, "class")) {
			return findElements(driver, By.className(elementId));
		}
		return objects;
	}
	
	public void scrapTableData(WebDriver driver, String fileName, String tableName) throws Exception {
		try {
			Path path = Paths.get(fileName);
			Files.deleteIfExists(path);
			Files.createFile(path);
			File file = new File(path.toString());
			FileWriter outputfile = new FileWriter(file); 
			CSVWriter writer = new CSVWriter(outputfile); 
			
			waitForPageLoad(driver);
			WebElement webTable = findElement(driver, By.xpath("//table[@summary='This table contains column headers corresponding to the data body table below']"));
			List<WebElement> headers = webTable.findElements(By.tagName("span"));
			while(headers.size() != 3) {
				headers = webTable.findElements(By.tagName("span"));
			}
			
			String[] headerArray = new String[headers.size()-1]; 
			for (int k = 0; k < headers.size() - 1; k++) {
				String col = headers.get(k).getText().replace(" ", "");
				headerArray[k] = col.substring(0, 1).toLowerCase() + col.substring(1);
			}
			writer.writeNext(headerArray);
			
			String tableXpath = "//table[@summary='" + tableName + "']";
			String rowCount = findElement(driver, By.xpath(tableXpath)).getAttribute("_rowcount");
			int iterations = (Integer.parseInt(rowCount) / 25) + (Integer.parseInt(rowCount) % 25 != 0 ? 1 : 0);
			for (int i = 1; i <= iterations; i++) {
				int rowValue = 25;
				if((Integer.parseInt(rowCount) <= 25) || iterations == i) {
					rowValue = Integer.parseInt(rowCount) % 25;
				}
				for (int j = 1; j <= rowValue ; j++) {
					List<WebElement> cols = findElements(driver, By.xpath(tableXpath + "["+ i +"]/tbody/tr["+ j +"]//td/span"));
					String[] dataArray = new String[cols.size()]; 
					for (int k = 0; k < cols.size(); k++) {
						dataArray[k] = cols.get(k).getText();
					}
					writer.writeNext(dataArray);
					cols.get(cols.size()-2).click();
					cols.get(cols.size()-2).click();
					Actions action = new Actions(driver);
					action.sendKeys(Keys.DOWN).perform();
				}
			}
			writer.close(); 
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static void waitForPageLoad(WebDriver driver) {
		new WebDriverWait(driver, 120).until(
			      webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
	}
	
	public static WebElement findElement(WebDriver driver, By byElement) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 180);
		wait.until(ExpectedConditions.visibilityOfElementLocated(byElement));
		return driver.findElement(byElement);
	}
	
	public static List<WebElement> findElements(WebDriver driver, By byElement) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 180);
		wait.until(ExpectedConditions.visibilityOfElementLocated(byElement));
		return driver.findElements(byElement);
	}
	
	@SuppressWarnings("unused")
	public static void sendExecutionStatus(String postUrl, JsonObject inputJson) throws Exception {

		CloseableHttpClient httpclient = HttpClients.createDefault();


		File file = new File("C:/dump/ImportGroups.csv");

		String remoteHostUrl=postUrl;
		FileInputStream input = new FileInputStream(file);
		byte[] byteArray = IOUtils.toByteArray(input);

		HttpEntity entity = MultipartEntityBuilder
				.create()
				.addTextBody("JobId", inputJson.getString("jobId"))
				.addTextBody("status", inputJson.getString("status"))
				.addTextBody("reason", inputJson.getString("status"))
				.addTextBody("importType", inputJson.getString("fileType"))
				.addTextBody("FileName", inputJson.getString("fileName"))
				.addBinaryBody("File",byteArray, ContentType.create("application/octet-stream"), "filename")
				.build();

		HttpPost httpPost = new HttpPost(remoteHostUrl);
		httpPost.setEntity(entity);
		HttpResponse response = httpclient.execute(httpPost);
		HttpEntity result = response.getEntity();
	}
}
