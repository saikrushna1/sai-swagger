package com.saviynt.identitybot.Service;

import com.saviynt.identitybot.controller.Status;

public interface InvokeService {
	public Status invokePostCall(String postUrl, String inputJson) throws Exception;
}
