package com.saviynt.identitybot.agent.util;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FileExplorer {
	final static Logger logger = LoggerFactory.getLogger(FileExplorer.class);
	public boolean deleteDirectory(File dir) {
		if (dir.isDirectory()) { 
			File[] children = dir.listFiles(); 
			for (int i = 0; i < children.length; i++) {
				System.gc();
				if(children[i].isFile()) {
					logger.info("File = "+children[i].getName());
					try {
					children[i].delete();
					}catch (Exception e) {
						logger.error("Error while deleting file "+children[i].getName()+" = "+e.getMessage());
					}
				}else if(children[i].isDirectory()) {
					logger.info("folder = "+children[i].getPath());
				boolean success = deleteDirectory(children[i]);
				
				}
			} 
		}
		return dir.delete();
	}
	public boolean deleteFile(File dir,File fileName) {
		if (dir.isDirectory()) { 
			File[] children = dir.listFiles(); 
			for (int i = 0; i < children.length; i++) {
				System.gc();
				if(children[i].isFile()) {
//					logger.info("Temp ="+fileName.getName());
//					logger.info("File = "+children[i].getName());
					try {
						if(children[i].getName().equals(fileName.getName())) {
							children[i].delete();
							return true;
						}
					
					}catch (Exception e) {
						logger.error("Error while deleting file "+children[i].getName()+" = "+e.getMessage());
						return false;
					}
				}
			} 
		}
		return false;
	}
}
