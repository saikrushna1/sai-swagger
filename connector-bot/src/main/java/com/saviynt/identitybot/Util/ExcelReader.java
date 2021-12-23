package com.saviynt.identitybot.Util;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

	final static Logger logger = Logger.getLogger(ScriptUtil.class);

	public static Map<String, Object> readObjectRepository(String appName, String orFile) throws Exception {
		Map<String, Object> orMap = new LinkedHashMap<>();
		if (Files.exists(Paths.get(orFile))) {
			FileInputStream inputStream = new FileInputStream(new File(orFile));
			try (Workbook workbook = new XSSFWorkbook(inputStream)) {
				Sheet sheet = workbook.getSheet(appName);
				if (null != sheet) {
					Row headerRow = sheet.getRow(0);
					for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
						String value = null;
						String orValue = "";
						Row r = sheet.getRow(i);
						StringBuilder builder = new StringBuilder();
						boolean onlyXpath = false;
						for (int j = 1; j < r.getPhysicalNumberOfCells(); j++) {
							String header = headerRow.getCell(j).getStringCellValue();
							orValue = "";
							if (r.getCell(j) != null) {
								if (r.getCell(j).getCellType().equals(CellType.STRING)) {
									value = r.getCell(j).getStringCellValue();
									if (value.contains("##")) {
										String[] va = value.split("##");
										for (int k = 0; k < va.length; k++) {
											orValue = orValue + va[k] + "$$";
										}
									} else if (j < r.getPhysicalNumberOfCells())
										if (onlyXpath) {
											orValue = orValue + value;
										} else {
											orValue = orValue + value + "$$";
										}
									else
										orValue = orValue + value;
									if (header.equalsIgnoreCase("botxpath"))
										if (orValue.trim().length() == 2) {
											onlyXpath = true;
										} else {
											builder = builder.append(orValue);
										}
									else if (!header.equalsIgnoreCase("ScriptName")) {
										if (onlyXpath) {
											builder = builder.append(orValue);
										} else {
											builder = builder.append(header.toLowerCase() + "=" + orValue);
										}
									}
								} else if (r.getCell(j).getCellType().equals(CellType.NUMERIC))
									orValue = orValue + String.valueOf(r.getCell(j).getNumericCellValue());
							}

						}
						orMap.put(r.getCell(0).getStringCellValue(), builder.toString());
						// logger.info("ormap: " + orMap);
					}
				}
			} catch (Exception e) {
				logger.error("eror: " + e.getMessage());
				throw new Exception("While reading TestDataManagement.xlsx failed");

			}

			inputStream.close();
		}

		return orMap;
	}

	public static Map<String, Object> readTestDataManagement(String excelFileName) throws Exception {

		Map<String, Object> dataMap = new LinkedHashMap<>();
		DataFormatter dataFormatter = new DataFormatter();

		if (Files.exists(Paths.get(excelFileName))) {

			FileInputStream fis = new FileInputStream(new File(excelFileName));
			try (Workbook workbook = new XSSFWorkbook(fis)) {
				  FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
			
				Iterator<Sheet> sheetIterator = workbook.iterator();
				while (sheetIterator.hasNext()) {
					Sheet sheet = sheetIterator.next();
					if (null != sheet) {
						for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
							String key = null;
							String value = null;
							Row r = sheet.getRow(i);
							if (null != r) {
								for (int j = r.getFirstCellNum(); j <= r.getLastCellNum(); j++) {
									if (null != r.getCell(j)) {
										key = sheet.getSheetName() + "." + r.getCell(0) + "."
												+ sheet.getRow(0).getCell(j);
										if (r.getCell(j).getCellType().equals(CellType.STRING)) {
											value = r.getCell(j).getStringCellValue();
										} else if (r.getCell(j).getCellType().equals(CellType.BOOLEAN)) {
											value = dataFormatter.formatCellValue(r.getCell(j));
										} else if (DateUtil.isCellDateFormatted(r.getCell(j))) {
											value = r.getCell(j).toString();
										} else if (r.getCell(j).getCellType().equals(CellType.NUMERIC)) {
											value = dataFormatter.formatCellValue(r.getCell(j));
										} else if (r.getCell(j).getCellType().equals(CellType.FORMULA)) {
											 switch (evaluator.evaluateFormulaCell(r.getCell(j))) {
										        case BOOLEAN:
										        	value = String.valueOf(r.getCell(j).getBooleanCellValue());
										            break;
										        case NUMERIC:
										        	value = String.valueOf(r.getCell(j).getNumericCellValue());
										            break;
										        case STRING:
										        	value = r.getCell(j).getStringCellValue();
										            break;
										    }
										}
											
											dataMap.put(key.trim(), value);
									}
								}
							}
						}
					}
				}
			} catch (Exception e) {
				logger.error("eror: " + e.getMessage());
				throw new Exception("While reading TestDataManagement.xlsx failed");
			}
			fis.close();
		}
		return dataMap;
	}
	
	

	public static Map<String, Object> readRow(String appName, Sheet sheetparam, int row) {
		Map<String, Object> dataMap = new LinkedHashMap<>();
		DataFormatter dataFormatter = new DataFormatter();
		String key = null;
		String value = null;
		Row r = sheetparam.getRow(row);
		if (null != r) {
			for (int j = r.getFirstCellNum(); j <= r.getLastCellNum(); j++) {
				if (null != r.getCell(j)) {
					key = appName + "." + "TestData" + row + "." + sheetparam.getRow(0).getCell(j);
					if (r.getCell(j).getCellType().equals(CellType.STRING)) {
						value = r.getCell(j).getStringCellValue();
					} else if (r.getCell(j).getCellType().equals(CellType.BOOLEAN)) {
						value = dataFormatter.formatCellValue(r.getCell(j));
					} else if (DateUtil.isCellDateFormatted(r.getCell(j))) {
						value = r.getCell(j).toString();
					} else if (r.getCell(j).getCellType().equals(CellType.NUMERIC))
						value = dataFormatter.formatCellValue(r.getCell(j));
					dataMap.put(key.trim(), value);
				}
			}
		}

		return dataMap;

	}

}
