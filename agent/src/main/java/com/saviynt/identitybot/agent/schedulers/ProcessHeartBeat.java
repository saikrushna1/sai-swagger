package com.saviynt.identitybot.agent.schedulers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.saviynt.identitybot.agent.constants.Constants;

@Component
@EnableAsync
public class ProcessHeartBeat {

	private static final Logger logger = LoggerFactory.getLogger(ProcessHeartBeat.class);

	@Value("${cobot.server.url}")
	String cobotServerUrl;
	@Value("${cobot.agent.id}")
	String cobotAgentId;
	
	@Value("${testbot.userid}")
	String testBotUserId;
	@Value("${testbot.password}")
	String testBotPassword;

	@Async
	@Scheduled(fixedRateString = "${cobot.server.heartbeat.interval}", initialDelay = 1000)
	public void scheduleFixedRateTaskAsync() {
		try {
			JSONObject cobotResponse = null;
			InetAddress localhost = InetAddress.getLocalHost();
			
			JSONObject urlParameters = new JSONObject();
			String ipAddress = null;
			
			try {
				for(String ip:getLocalIPAddresses()) {
					
					ipAddress = ip;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("Unable to find IP address");
			}
			if(ipAddress!=null) {
				String encoded = Base64.getEncoder().encodeToString((testBotUserId+":"+testBotPassword).getBytes(StandardCharsets.UTF_8));  //Java 8
				logger.info(testBotUserId);
				logger.info(testBotPassword);
				logger.info(encoded);
				urlParameters.put(Constants.AGENT_ID, cobotAgentId);
				urlParameters.put(Constants.AGENT_IP, ipAddress);
				urlParameters.put(Constants.AGENT_NAME, localhost.getHostName().trim());
				urlParameters.put(Constants.LOAD_BALANCER_FLAG, 0);
				urlParameters.put(Constants.NUMBER_OF_INSTANCES, 0);
				logger.info(cobotServerUrl+Constants.COBOT_HEART_BEAT_URL);
				URL url = new URL(cobotServerUrl+Constants.COBOT_HEART_BEAT_URL);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/json; utf-8");
				con.setRequestProperty("Accept", "application/json");
				con.setRequestProperty("Authorization", "Basic "+encoded);
				con.setDoOutput(true);

				try (OutputStream os = con.getOutputStream()) {
					byte[] input = urlParameters.toString().getBytes("utf-8");
					os.write(input, 0, input.length);
				}

				int code = con.getResponseCode();

				try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
					StringBuilder response = new StringBuilder();
					String responseLine = null;
					while ((responseLine = br.readLine()) != null) {
						response.append(responseLine.trim());
					}
					try {
						cobotResponse = new JSONObject(response.toString());
						if (cobotResponse.get(Constants.JOB_STATUS).equals(Constants.SUCCESS)) {
							logger.info("Heart beat successfully updated to cobot server");
						} else {
							logger.error("Unable to update Heart beat");
						}
					} catch (JSONException err) {
						logger.error("Unable to parse heart beat response to JSON object");
					}
				} catch (Exception e) {
					logger.error("Unable to send heart beat to cobot server");
					e.printStackTrace();
				}
			}else {
				logger.error("Unable to find system ip address");
			}
			

		} catch (Exception e) {
			logger.error("Unable to send heart beat to cobot server");
		}
	}
	public static List<String> getLocalIPAddresses() throws Exception
	{
	    Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
	    ArrayList<String> localIPAddresses = new ArrayList<String>();

	    while (networkInterfaces.hasMoreElements())
	    {
	    	NetworkInterface networkInterface = networkInterfaces.nextElement();
	    	String displayName = networkInterface.getDisplayName();

	    	if (displayName!=null && !displayName.contains(Constants.VIRTUALBOX))
	        {
	            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses())
	            {
	            	InetAddress address = interfaceAddress.getAddress();

	                // Find the local IP addresses
	                if (address.isSiteLocalAddress())
	                {
	                	String localIPAddress = interfaceAddress.getAddress().toString().replace("/", "");
	                    localIPAddresses.add(localIPAddress);
	                }
	            }
	        }
	    }

	    if (localIPAddresses.isEmpty())
	    {
	        throw new IllegalStateException("Expected the computer's local IP address but didn't get one!");
	    }

	    return localIPAddresses;
	}
}
