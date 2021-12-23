package com.saviynt.identitybot.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class OrExcelGenarater {
	final static Logger logger = Logger.getLogger(OrExcelGenarater.class);

	// private final Pattern pat =
	// Pattern.compile("driver.findElement\\(.*?\\)|driver.findElements\\(.*?\\)");
	private final Pattern pat1 = Pattern.compile("By.\\w+\\(\"");
	private final Pattern pat2 = Pattern.compile("(.*?\\\"\\))");
	private final Pattern pat3 = Pattern.compile("@name=\\\\'.*?\\\\'|@class=\\\\'.*?\\\\'|@id=\\\\'.*?\\\\'");
	private Map<String, String> duplicateKey = new LinkedHashMap<>();

	public String generateOrExcel(String content, String scriptname, String appName, String scriptsRootDir)
			throws InvalidFormatException, IOException {
		// Matcher m = pat.matcher(content);
		BufferedReader reader = new BufferedReader(new StringReader(content));
		String line = null;
		if (!Files.exists(Paths.get(scriptsRootDir + "ObjectRepositoryManagement.xlsx"))) {
			Workbook workbook = new XSSFWorkbook();
			if (workbook.getSheet(appName) == null) {
				Sheet sh = workbook.createSheet(appName);

				Row r = sh.createRow(0);
				Cell c = r.createCell(0, CellType.STRING);
				c.setCellValue("ElementName");
				Cell c1 = r.createCell(1, CellType.STRING);
				c1.setCellValue("ScriptName");
				Cell c2 = r.createCell(2, CellType.STRING);
				c2.setCellValue("botxpath");
				Cell c3 = r.createCell(3, CellType.STRING);
				c3.setCellValue("xpath");
				Cell c4 = r.createCell(4, CellType.STRING);
				c4.setCellValue("name");
				Cell c5 = r.createCell(5, CellType.STRING);
				c5.setCellValue("classname");
				Cell c6 = r.createCell(6, CellType.STRING);
				c6.setCellValue("css");
				Cell c7 = r.createCell(7, CellType.STRING);
				c7.setCellValue("tag");
				Cell c8 = r.createCell(8, CellType.STRING);
				c8.setCellValue("linktext");
				Cell c9 = r.createCell(9, CellType.STRING);
				c9.setCellValue("partiallinktext");
				Cell c10 = r.createCell(10, CellType.STRING);
				c10.setCellValue("id");

				int rowNum = 1;
				while ((line = reader.readLine()) != null) {
					if (line.contains("driver.findElement(") || line.contains("driver.findElements(")) {
						line = line.replace("driver.findElement(", "").trim();

						Matcher m1 = pat1.matcher(line);
						if (m1.find()) {
							line = line.replace(m1.group(), "");
							Matcher m2 = pat2.matcher(line);
							if (m2.find()) {

								String value = m2.group().replace("\")", "");
								// logger.info("value: " + value);
								String[] alllocators = value.split("\\$\\$");
								HashMap<String, String> values = getallElementToMap(alllocators);
								// logger.info("values001: " + values);

								String keyname = values.get("id") != null ? values.get("id")
										: (values.get("name") != null ? values.get("name")
												: (values.get("class") != null ? values.get("class") : null));
								if (keyname == null) {
									if (values.get("xpath") != null) {
										Matcher key = pat3.matcher(values.get("xpath"));
										if (key.find()) {
											keyname = key.group().split("=", 2)[1].replace("\\'", "");
										}
									}
								}
								if (keyname == null) {
									Matcher othrKey = pat3.matcher(values.get("absolute"));
									if (othrKey.find()) {
										keyname = othrKey.group().split("=", 2)[1].replace("\\'", "");
									}
								}
								if (!duplicateKey.containsKey(keyname)) {
									Row s1 = sh.createRow(rowNum);
									Cell c11 = s1.createCell(0, CellType.STRING);
									c11.setCellValue(keyname);
									Cell c12 = s1.createCell(1, CellType.STRING);
									c12.setCellValue(scriptname);

									Cell c13 = s1.createCell(2, CellType.STRING);
									c13.setCellValue(values.get("absolute"));

									Cell c14 = s1.createCell(3, CellType.STRING);
									c14.setCellValue(values.get("xpath"));

									Cell c15 = s1.createCell(4, CellType.STRING);
									c15.setCellValue(values.get("name"));

									Cell c16 = s1.createCell(5, CellType.STRING);
									c16.setCellValue(values.get("class"));

									Cell c17 = s1.createCell(6, CellType.STRING);
									c17.setCellValue(values.get("css"));

									Cell c18 = s1.createCell(7, CellType.STRING);
									c18.setCellValue(values.get("tag"));

									Cell c19 = s1.createCell(8, CellType.STRING);
									c19.setCellValue(values.get("linktext"));

									Cell c20 = s1.createCell(9, CellType.STRING);
									c20.setCellValue(values.get("partiallinktext"));

									Cell c21 = s1.createCell(10, CellType.STRING);
									c21.setCellValue(values.get("id"));
									duplicateKey.put(keyname, values.get("absolute"));
									rowNum++;
								} else if (!duplicateKey.get(keyname).contains(values.get("absolute"))) {
									int t = 0;
									for (String key : duplicateKey.keySet()) {
										if (null != key && key.contains(keyname)) {
											t++;
										}
									}
									keyname = keyname + t;
									keyname = keyname.replace("\\'", "");
									Row s1 = sh.createRow(rowNum);
									Cell c11 = s1.createCell(0, CellType.STRING);
									c11.setCellValue(keyname);
									Cell c12 = s1.createCell(1, CellType.STRING);
									c12.setCellValue(scriptname);

									Cell c13 = s1.createCell(2, CellType.STRING);
									c13.setCellValue(values.get("absolute"));

									Cell c14 = s1.createCell(3, CellType.STRING);
									c14.setCellValue(values.get("xpath"));

									Cell c15 = s1.createCell(4, CellType.STRING);
									c15.setCellValue(values.get("name"));

									Cell c16 = s1.createCell(5, CellType.STRING);
									c16.setCellValue(values.get("class"));

									Cell c17 = s1.createCell(6, CellType.STRING);
									c17.setCellValue(values.get("css"));

									Cell c18 = s1.createCell(7, CellType.STRING);
									c18.setCellValue(values.get("tag"));

									Cell c19 = s1.createCell(8, CellType.STRING);
									c19.setCellValue(values.get("linktext"));

									Cell c20 = s1.createCell(9, CellType.STRING);
									c20.setCellValue(values.get("partiallinktext"));

									Cell c21 = s1.createCell(10, CellType.STRING);
									c21.setCellValue(values.get("id"));
									duplicateKey.put(keyname, values.get("absolute"));
									rowNum++;
								}

								if (keyname != null)
									content = content.replace(value,
											"#" + keyname.replace("\\'", "").replace("'\\", "") + "#");

							}
						}

					}
				}

				FileOutputStream fos = new FileOutputStream(
						new File(scriptsRootDir + "ObjectRepositoryManagement.xlsx"));
				workbook.write(fos);
				workbook.close();
				fos.close();
			}
		} else {
			Workbook workbook1 = null;

			workbook1 = new XSSFWorkbook(new FileInputStream(scriptsRootDir + "ObjectRepositoryManagement.xlsx"));
			Sheet sh = workbook1.getSheet(appName);
			if (sh == null) {
				sh = workbook1.createSheet(appName);
				Row r = sh.createRow(0);
				Cell c = r.createCell(0, CellType.STRING);
				c.setCellValue("ElementName");
				Cell c1 = r.createCell(1, CellType.STRING);
				c1.setCellValue("ScriptName");
				Cell c2 = r.createCell(2, CellType.STRING);
				c2.setCellValue("botxpath");
				Cell c3 = r.createCell(3, CellType.STRING);
				c3.setCellValue("xpath");
				Cell c4 = r.createCell(4, CellType.STRING);
				c4.setCellValue("name");
				Cell c5 = r.createCell(5, CellType.STRING);
				c5.setCellValue("classname");
				Cell c6 = r.createCell(6, CellType.STRING);
				c6.setCellValue("css");
				Cell c7 = r.createCell(7, CellType.STRING);
				c7.setCellValue("tag");
				Cell c8 = r.createCell(8, CellType.STRING);
				c8.setCellValue("linktext");
				Cell c9 = r.createCell(9, CellType.STRING);
				c9.setCellValue("partiallinktext");
				Cell c10 = r.createCell(10, CellType.STRING);
				c10.setCellValue("id");
				int rowNum = 1;
				while ((line = reader.readLine()) != null) {
					if (line.contains("driver.findElement(") || line.contains("driver.findElements(")) {
						line = line.replace("driver.findElement(", "").trim();
						Matcher m1 = pat1.matcher(line);
						if (m1.find()) {
							line = line.replace(m1.group(), "");
							Matcher m2 = pat2.matcher(line);
							if (m2.find()) {

								String value = m2.group().replace("\")", "");
								// logger.info("value: " + value);
								String[] alllocators = value.split("\\$\\$");
								HashMap<String, String> values = getallElementToMap(alllocators);
								// logger.info("values002: " + values);
								String keyname = values.get("id") != null ? values.get("id")
										: (values.get("name") != null ? values.get("name")
												: (values.get("class") != null ? values.get("class") : null));
								if (keyname == null) {
									if (values.get("xpath") != null) {
										Matcher key = pat3.matcher(values.get("xpath"));
										if (key.find()) {
											keyname = key.group().split("=", 2)[1].replace("\\'", "");
										}
									}
								}
								if (keyname == null) {
									Matcher othrKey = pat3.matcher(values.get("absolute"));
									if (othrKey.find()) {
										keyname = othrKey.group().split("=", 2)[1].replace("\\'", "");
									}
								}

								if (!duplicateKey.containsKey(keyname) && null != keyname) {
									Row s1 = sh.createRow(rowNum);
									Cell c11 = s1.createCell(0, CellType.STRING);
									c11.setCellValue(keyname);
									Cell c12 = s1.createCell(1, CellType.STRING);
									c12.setCellValue(scriptname);

									Cell c13 = s1.createCell(2, CellType.STRING);
									c13.setCellValue(values.get("absolute"));

									Cell c14 = s1.createCell(3, CellType.STRING);
									c14.setCellValue(values.get("xpath"));

									Cell c15 = s1.createCell(4, CellType.STRING);
									c15.setCellValue(values.get("name"));

									Cell c16 = s1.createCell(5, CellType.STRING);
									c16.setCellValue(values.get("class"));

									Cell c17 = s1.createCell(6, CellType.STRING);
									c17.setCellValue(values.get("css"));

									Cell c18 = s1.createCell(7, CellType.STRING);
									c18.setCellValue(values.get("tag"));

									Cell c19 = s1.createCell(8, CellType.STRING);
									c19.setCellValue(values.get("linktext"));

									Cell c20 = s1.createCell(9, CellType.STRING);
									c20.setCellValue(values.get("partiallinktext"));

									Cell c21 = s1.createCell(10, CellType.STRING);
									c21.setCellValue(values.get("id"));
									duplicateKey.put(keyname, values.get("absolute"));
									rowNum++;
								} else if (null != keyname
										&& !duplicateKey.get(keyname).contains(values.get("absolute"))) {
									int t = 0;
									for (String key : duplicateKey.keySet()) {
										if (null != key && key.contains(keyname)) {
											t++;
										}
									}
									keyname = keyname + t;
									keyname = keyname.replace("\\'", "");
									Row s1 = sh.createRow(rowNum);
									Cell c11 = s1.createCell(0, CellType.STRING);
									c11.setCellValue(keyname);
									Cell c12 = s1.createCell(1, CellType.STRING);
									c12.setCellValue(scriptname);

									Cell c13 = s1.createCell(2, CellType.STRING);
									c13.setCellValue(values.get("absolute"));

									Cell c14 = s1.createCell(3, CellType.STRING);
									c14.setCellValue(values.get("xpath"));

									Cell c15 = s1.createCell(4, CellType.STRING);
									c15.setCellValue(values.get("name"));

									Cell c16 = s1.createCell(5, CellType.STRING);
									c16.setCellValue(values.get("class"));

									Cell c17 = s1.createCell(6, CellType.STRING);
									c17.setCellValue(values.get("css"));

									Cell c18 = s1.createCell(7, CellType.STRING);
									c18.setCellValue(values.get("tag"));

									Cell c19 = s1.createCell(8, CellType.STRING);
									c19.setCellValue(values.get("linktext"));

									Cell c20 = s1.createCell(9, CellType.STRING);
									c20.setCellValue(values.get("partiallinktext"));

									Cell c21 = s1.createCell(10, CellType.STRING);
									c21.setCellValue(values.get("id"));
									duplicateKey.put(keyname, values.get("absolute"));
									rowNum++;
								}
								if (keyname != null)
									content = content.replace(value,
											"#" + keyname.replace("\\'", "").replace("'\\", "") + "#");

							}
						}
					}
				}

				FileOutputStream fos = new FileOutputStream(
						new File(scriptsRootDir + "ObjectRepositoryManagement.xlsx"));
				workbook1.write(fos);
				workbook1.close();
				fos.close();

			} else {
				int rowCount = sh.getPhysicalNumberOfRows();
				for (int k = 1; k < rowCount; k++) {
					Row k1 = sh.getRow(k);
					duplicateKey.put(k1.getCell(0).getStringCellValue(), k1.getCell(2).getStringCellValue());
				}
				while ((line = reader.readLine()) != null) {
					if (line.contains("driver.findElement(") || line.contains("driver.findElements(")) {
						line = line.replace("driver.findElement(", "").trim();

						Matcher m1 = pat1.matcher(line);
						if (m1.find()) {
							line = line.replace(m1.group(), "");
							Matcher m2 = pat2.matcher(line);
							if (m2.find()) {
								String value = m2.group().replace("\")", "");
								// logger.info("value: " + value);
								String[] alllocators = value.split("\\$\\$");
								HashMap<String, String> values = getallElementToMap(alllocators);
								// logger.info("values003: " + values);
								String keyname = values.get("id") != null ? values.get("id")
										: (values.get("name") != null ? values.get("name")
												: (values.get("class") != null ? values.get("class") : null));
								if (keyname == null) {
									if (values.get("xpath") != null) {
										Matcher key = pat3.matcher(values.get("xpath"));
										if (key.find()) {
											keyname = key.group().split("=", 2)[1].replace("\\'", "");
										}
									}
								}
								if (keyname == null) {
									Matcher othrKey = pat3.matcher(values.get("absolute"));
									if (othrKey.find()) {
										keyname = othrKey.group().split("=", 2)[1].replace("\\'", "");
									}
								}

								if (null != keyname && !duplicateKey.containsKey(keyname)) {
									Row s1 = sh.createRow(rowCount);
									Cell c11 = s1.createCell(0, CellType.STRING);
									c11.setCellValue(keyname);
									Cell c12 = s1.createCell(1, CellType.STRING);
									c12.setCellValue(scriptname);

									Cell c13 = s1.createCell(2, CellType.STRING);
									c13.setCellValue(values.get("absolute"));

									Cell c14 = s1.createCell(3, CellType.STRING);
									c14.setCellValue(values.get("xpath"));

									Cell c15 = s1.createCell(4, CellType.STRING);
									c15.setCellValue(values.get("name"));

									Cell c16 = s1.createCell(5, CellType.STRING);
									c16.setCellValue(values.get("class"));

									Cell c17 = s1.createCell(6, CellType.STRING);
									c17.setCellValue(values.get("css"));

									Cell c18 = s1.createCell(7, CellType.STRING);
									c18.setCellValue(values.get("tag"));

									Cell c19 = s1.createCell(8, CellType.STRING);
									c19.setCellValue(values.get("linktext"));

									Cell c20 = s1.createCell(9, CellType.STRING);
									c20.setCellValue(values.get("partiallinktext"));

									Cell c21 = s1.createCell(10, CellType.STRING);
									c21.setCellValue(values.get("id"));
									duplicateKey.put(keyname, values.get("absolute"));
									rowCount++;
								} else if (null != keyname
										&& !duplicateKey.get(keyname).contains(values.get("absolute"))) {
									int t = 0;
									for (String key : duplicateKey.keySet()) {
										if (null != key && key.contains(keyname)) {
											t++;
										}
									}
									keyname = keyname + t;
									keyname = keyname.replace("\\'", "");
									Row s1 = sh.createRow(rowCount);
									Cell c11 = s1.createCell(0, CellType.STRING);
									c11.setCellValue(keyname);
									Cell c12 = s1.createCell(1, CellType.STRING);
									c12.setCellValue(scriptname);

									Cell c13 = s1.createCell(2, CellType.STRING);
									c13.setCellValue(values.get("absolute"));

									Cell c14 = s1.createCell(3, CellType.STRING);
									c14.setCellValue(values.get("xpath"));

									Cell c15 = s1.createCell(4, CellType.STRING);
									c15.setCellValue(values.get("name"));

									Cell c16 = s1.createCell(5, CellType.STRING);
									c16.setCellValue(values.get("class"));

									Cell c17 = s1.createCell(6, CellType.STRING);
									c17.setCellValue(values.get("css"));

									Cell c18 = s1.createCell(7, CellType.STRING);
									c18.setCellValue(values.get("tag"));

									Cell c19 = s1.createCell(8, CellType.STRING);
									c19.setCellValue(values.get("linktext"));

									Cell c20 = s1.createCell(9, CellType.STRING);
									c20.setCellValue(values.get("partiallinktext"));

									Cell c21 = s1.createCell(10, CellType.STRING);
									c21.setCellValue(values.get("id"));
									duplicateKey.put(keyname, values.get("absolute"));
									rowCount++;
								}
								if (keyname != null)
									content = content.replace(value,
											"#" + keyname.replace("\\'", "").replace("'\\", "") + "#");
							}
						}
					}
				}
				FileOutputStream fos = new FileOutputStream(
						new File(scriptsRootDir + "ObjectRepositoryManagement.xlsx"));
				workbook1.write(fos);
				workbook1.close();
				fos.close();
			}
		}
		return content;

	}

	private HashMap<String, String> getallElementToMap(String[] alllocators) {
		HashMap<String, String> xpaths = new HashMap<>();
		for (int i = 0; i < alllocators.length; i++) {
			if (alllocators[i].startsWith("name")) {
				// logger.info("name" + alllocators[i].split("=", 2)[1]);
				if (xpaths.containsKey("name")) {
					String name = xpaths.get("name") + "##" + alllocators[i].split("=", 2)[1];
					xpaths.put("name", name.trim());
				} else
					xpaths.put("name", alllocators[i].split("=", 2)[1].trim());
			} else if (alllocators[i].startsWith("id")) {
				// logger.info("id: " + alllocators[i].split("=")[1]);
				if (xpaths.containsKey("id")) {
					String name = xpaths.get("id") + "##" + alllocators[i].split("=", 2)[1];
					xpaths.put("id", name.trim());
				} else
					xpaths.put("id", alllocators[i].split("=", 2)[1].trim());
			} else if (alllocators[i].startsWith("class")) {
				// logger.info("class: " + alllocators[i].split("=", 2)[1]);
				if (xpaths.containsKey("class")) {
					String name = xpaths.get("class") + "##" + alllocators[i].split("=", 2)[1];
					xpaths.put("class", name.trim());
				} else
					xpaths.put("class", alllocators[i].split("=", 2)[1].trim());
			} else if (alllocators[i].startsWith("css")) {
				// logger.info("css: " + alllocators[i].split("=", 2)[1]);
				if (xpaths.containsKey("css")) {
					String name = xpaths.get("css") + "##" + alllocators[i].split("=", 2)[1];
					xpaths.put("css", name.trim());
				} else
					xpaths.put("css", alllocators[i].split("=", 2)[1].trim());
			} else if (alllocators[i].startsWith("tag")) {
				// logger.info("tag: " + alllocators[i].split("=", 2)[1]);
				if (xpaths.containsKey("tag")) {
					String name = xpaths.get("tag") + "##" + alllocators[i].split("=", 2)[1];
					xpaths.put("tag", name.trim());
				} else
					xpaths.put("tag", alllocators[i].split("=", 2)[1].trim());
			} else if (alllocators[i].startsWith("xpath")) {
				// logger.info("xpath: " + alllocators[i].split("=", 2)[1]);
				if (xpaths.containsKey("xpath")) {
					String name = xpaths.get("xpath") + "##" + alllocators[i].split("=", 2)[1];
					xpaths.put("xpath", name.trim());
				} else
					xpaths.put("xpath", alllocators[i].split("=", 2)[1].trim());
			} else if (alllocators[i].startsWith("partiallinktext")) {
				if (xpaths.containsKey("partiallinktext")) {
					String name = xpaths.get("partiallinktext") + "##" + alllocators[i].split("=", 2)[1];
					xpaths.put("partiallinktext", name.trim());
				} else
					xpaths.put("partiallinktext", alllocators[i].split("=", 2)[1].trim());
				// logger.info("par: " + alllocators[i].split("=", 2)[1]);
			} else if (alllocators[i].startsWith("linktext")) {
				// logger.info("text: " + alllocators[i].split("=", 2)[1]);
				if (xpaths.containsKey("linktext")) {
					String name = xpaths.get("linktext") + "##" + alllocators[i].split("=", 2)[1];
					xpaths.put("linktext", name.trim());
				} else
					xpaths.put("linktext", alllocators[i].split("=", 2)[1].trim());
			} else {
				// logger.info("absolute: " + alllocators[i]);
				if (xpaths.containsKey("absolute")) {
					String name = xpaths.get("absolute") + "##" + alllocators[i];
					xpaths.put("absolute", name.trim());
				} else
					xpaths.put("absolute", alllocators[i].trim());
			}
		}
		return xpaths;
	}
}
