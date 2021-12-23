package com.saviynt.identitybot.controller;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saviynt.identitybot.Service.SSMAuthService;
import com.saviynt.identitybot.Util.CLIRecorder;
import com.saviynt.identitybot.Util.FileExplorer;
import com.saviynt.identitybot.Util.JobUtil;
import com.saviynt.identitybot.Util.S3Explorer;
import com.saviynt.identitybot.controller.JobController;
import com.saviynt.identitybot.controller.JobResponse;
import com.saviynt.identitybot.controller.Status;
import com.saviynt.identitybot.mapper.JobUIMapper;
import com.saviynt.identitybot.model.Job;
import com.saviynt.identitybot.repository.BotAgentRepository;
import com.saviynt.identitybot.repository.JobRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class JobControllerTest {
	@Mock
	JobRepository jobRepository;

	@InjectMocks
    private JobController jobController;
	
	@Mock
	private SSMAuthService authService;
	
	@Mock
	private Environment env;

	@Mock
	BotAgentRepository botAgentRepository;

	@Mock
	JobUIMapper mapper;

	@Mock
	CLIRecorder cliRecorder;
	
	@Mock
	private S3Explorer s3explorer;
	
	@Mock
	private FileExplorer fileExplorer;
	
	
    private MockMvc mockMvc;
    
    @Mock
    HttpServletRequest request;
 
    @Mock
    HttpServletResponse response;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(jobController).build();
    }
    
    @PrepareForTest({ JobUtil.class,JobResponse.class })
	@Test
	public void testUploadFileHandler() throws Exception  {    		
			Job job = new Job();
			when(jobRepository.save(job)).thenReturn(job);
			this.mockMvc.perform(
					post("/api/job/agent").
					param("AgentIP", "192.168.0.199").
					param("File", "test").
					param("Body", "hello"))
					.andExpect(status().isOk());				
	} 
    
    @PrepareForTest({ JobUtil.class,JobResponse.class })
	@Test
	public void testExecuteScript() throws Exception  {    		
			Job job = new Job();
			when(jobRepository.save(job)).thenReturn(job);
			this.mockMvc.perform(
					post("/api/job/script").
					param("File", "test").
					param("Body", "hello"))
					.andExpect(status().isOk());				
	}
    
    @PrepareForTest({ JobUtil.class})
	@Test
	public void testUpdateJobStatus() throws Exception  {    		
    	Status status = new Status();
    	when(jobRepository.updateStatusAndReason(status.getJobId(), status.getReason(), new Long(1))).thenReturn(1);
			this.mockMvc.perform(
					post("/api/job/updatestatus").
					contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(status)))
					.andExpect(status().isOk());				
	}
    
    @Test
	public void testCreateJson() throws Exception  {    		
			this.mockMvc.perform(
					post("/api/job/createjson").
					param("content", "content").
					param("search", "{}").
					param("headers", "{}").
					param("fileName", "fileName").
					param("conntype", "conntype").
					param("connectionName", "connectionName").
					param("agentIp", "agentIp"))
					.andExpect(status().isOk());				
	}  
    
	@PrepareForTest({ JobUtil.class, JobResponse.class, IOUtils.class})	  
	@Test
	public void testCreateJob() throws Exception { 
	  Job job = new Job(); 
	  when(jobRepository.save(job)).thenReturn(job);
	   
	  Status status = new Status();
	  when(jobRepository.updateStatusAndReason(status.getJobId(),
	  status.getReason(), new Long(1))).thenReturn(1);
	  this.mockMvc.perform(MockMvcRequestBuilders.post("/api/job")
			  .accept(MediaType.APPLICATION_JSON).
			  content("{}")).andExpect(status().isOk());
	} 
	
	@Test
	public void testConnectionjson() throws Exception { 
		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/job/createjob")
			  .accept(MediaType.APPLICATION_JSON).
			  content("{'remoteIp':'remoteIp','scriptType':'scriptType','httpparams':'httpparams', 'filename':'filename'}")).andExpect(status().isOk());
	} 
	
	@Test
	public void testcontentCompile() throws Exception { 
		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/content")
			  .accept(MediaType.APPLICATION_JSON).
			  content("{'fileContent':'fileContent','fileName':'fileName'}")).andExpect(status().isOk());
	}
	
	@Test
	public void testcontentSaving() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/contentSaving")
			  .accept(MediaType.APPLICATION_JSON).
			  content("{'fileContent':'fileContent','fileName':'fileName', 'scriptType':'CLI'}")).andExpect(status().isOk());
	}
	
	@Test
	public void testfindScriptContent() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/script/getFile")
			  .accept(MediaType.APPLICATION_JSON).
			  content("{'fileName':'fileName', 'scriptType':'CLI'}")).andExpect(status().isOk());
	}
	
	@Test
	public void testrefreshBotAgents() throws Exception {
		this.mockMvc.perform(get("/api/botAgents")).andExpect(status().isOk());
	}
    
    @Test
	public void testRecordScript() throws Exception  {    		
			this.mockMvc.perform(
					post("/api/cli/record").
					param("File", "File").
					param("recordVideo", "").
					param("format", "format").
					param("connectionName","connectionName"))
					.andExpect(status().isOk());				
	}
   
    @Test
   	public void testStartVideo() throws Exception  {    		
   			this.mockMvc.perform(
   					post("/api/video/start").
   					param("File", "File").
   					param("format", "format"))
   					.andExpect(status().isOk());				
   	}
    
    @Test
    public void testKillProcesses() throws Exception {
    	this.mockMvc.perform(get("/api/video/stop")).andExpect(status().isOk()); 
    }
    
    @Test
    public void testcheckSSMConnection() throws Exception {
    	when(authService.getAuthToken("", "")).thenReturn(new Status());
    	this.mockMvc.perform(get("/api/checkSSMConnection")).andExpect(status().isOk()); 
    }
   
    
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
