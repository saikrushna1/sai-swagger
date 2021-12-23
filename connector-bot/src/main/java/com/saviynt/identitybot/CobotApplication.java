package com.saviynt.identitybot;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.saviynt.identitybot.constants.Constants;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Srinivasa Reddy Challa, Raj Kumar
 * 
 *         Application Startup class
 *
 */

@EnableEncryptableProperties
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@EnableJpaAuditing
public class CobotApplication extends SpringBootServletInitializer {
	final static Logger logger = Logger.getLogger(CobotApplication.class);

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		Properties loadCobotProperties = loadCobotProperties();
		return application.sources(CobotApplication.class).properties("execute.mode:servlet-container").properties(loadCobotProperties);
	}

	public static void main(String[] args) throws Exception {
		Properties loadCobotProperties = loadCobotProperties();
		SpringApplicationBuilder builder = new SpringApplicationBuilder(CobotApplication.class);
		builder.headless(false).properties("execute.mode:standalone").properties(loadCobotProperties).run(args);		
	}
	
	public static Properties loadCobotProperties() {
		String propertiesPath = System.getenv("COBOT_HOME") + Constants.CONFIG_PATH; 
		
		//String propertiesPath = "/Users/peddledigitals/Documents/Personal/Kiran-Personal/02_Products/Saviynt_Identity_Bot/Artifacts" + Constants.CONFIG_PATH;
		logger.info(propertiesPath);
		Properties properties=new Properties();
		
		 try (InputStream input = new FileInputStream(propertiesPath)) {
	            properties.load(input);
	        } catch (IOException ex) {
	           logger.error("Invalid application Properties file " + propertiesPath);
	        }
		
		 return properties;
	}
	
}
