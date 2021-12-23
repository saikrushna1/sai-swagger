/**
 * 
 */
package com.saviynt.identitybot.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.saviynt.identitybot.mapper.JobUIMapper;
import com.saviynt.identitybot.repository.BotAgentRepository;
import com.saviynt.identitybot.repository.JobRepository;

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.ResponseEntity;
/**
 * @author venkat
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class JobUIControllerTest {
	
	@Autowired
	JobRepository jobRepository;

	@Autowired
	BotAgentRepository botAgentRepository;

	@Autowired
	JobUIMapper mapper;
	
	@LocalServerPort
    int randomServerPort;
	
	@Test
    public void testShowAllJobsSuccess() throws URISyntaxException 
    {
        RestTemplate restTemplate = new RestTemplate();
        
        final String baseUrl = "http://localhost:"+randomServerPort+"/ui/jobs";
        URI uri = new URI(baseUrl);

        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
        
        //Verify request succeed
        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertEquals(true, result.getBody().contains("jobs"));
    }
	
	@Test
    public void testShowAllBotAgentsSuccess() throws URISyntaxException 
    {
        RestTemplate restTemplate = new RestTemplate();
        
        final String baseUrl = "http://localhost:"+randomServerPort+"/ui/botAgents";
        URI uri = new URI(baseUrl);

        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
        
        //Verify request succeed
        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertEquals(true, result.getBody().contains("botAgents"));
    }
	
	@Test
    public void testShowAllScriptsSuccess() throws URISyntaxException 
    {
        RestTemplate restTemplate = new RestTemplate();
        
        final String baseUrl = "http://localhost:"+randomServerPort+"/ui/scripts";
        URI uri = new URI(baseUrl);

        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
        
        //Verify request succeed
        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertEquals(true, result.getBody().contains("scripts"));
    }
}
