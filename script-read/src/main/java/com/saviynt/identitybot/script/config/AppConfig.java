package com.saviynt.identitybot.script.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@ComponentScan("com.saviynt.identitybot")
@PropertySource("classpath:application.properties")
public class AppConfig {

	@Bean
	RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		ObjectMapper objectMapper = new ObjectMapper();
		converter.setObjectMapper(objectMapper);
		restTemplate.getMessageConverters().add(converter);
		return restTemplate;
	}

	@Bean
	public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {

		String angentHome = System.getenv("AGENT_HOME");
		String propertiesPath = "";
		if(angentHome!= null) {
			 propertiesPath = angentHome+ "/application.properties";
		}
		PropertySourcesPlaceholderConfigurer properties = new PropertySourcesPlaceholderConfigurer();
		if(angentHome!=null) {
			properties.setLocation(new FileSystemResource(propertiesPath));
		}
		properties.setLocalOverride(true);
		properties.setIgnoreResourceNotFound(false);

		return properties;
	}

}
