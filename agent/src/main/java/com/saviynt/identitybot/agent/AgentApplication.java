package com.saviynt.identitybot.agent;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import io.github.bonigarcia.wdm.WebDriverManager;

//@EnableResourceServer
@SpringBootApplication
public class AgentApplication {

	public static void main(String[] args) {
		
		//SpringApplication.run(AgentApplication.class, args);
		SpringApplicationBuilder builder = new SpringApplicationBuilder(AgentApplication.class);
		builder.headless(false).properties("execute.mode:standalone").run(args);;
		WebDriverManager.chromedriver().setup();
		WebDriverManager.firefoxdriver().setup();
		WebDriverManager.iedriver().setup();
		/* WebDriverManager.operadriver().setup(); */
		WebDriverManager.edgedriver().setup();
	}
	
}
