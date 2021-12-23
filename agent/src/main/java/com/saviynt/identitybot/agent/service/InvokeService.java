package com.saviynt.identitybot.agent.service;

public interface InvokeService {
	public void invokePostCall(String postUrl, String inputJson) throws Exception;
}
