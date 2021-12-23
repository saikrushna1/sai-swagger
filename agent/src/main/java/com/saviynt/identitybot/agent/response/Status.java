package com.saviynt.identitybot.agent.response;

/**
 * @author Srinivasa Reddy Challa, Raj Kumar java Bean class for representing
 *         status of a particular job
 * 
 */

public class Status {

	String jobId;
	String status;
	String reason;
	
	String scriptFileName;
	String videoFileName;

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getScriptFileName() {
		return scriptFileName;
	}

	public void setScriptFileName(String scriptFileName) {
		this.scriptFileName = scriptFileName;
	}

	public String getVideoFileName() {
		return videoFileName;
	}

	public void setVideoFileName(String videoFileName) {
		this.videoFileName = videoFileName;
	}

}
