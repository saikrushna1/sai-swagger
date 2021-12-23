package com.saviynt.identitybot.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Srinivasa Reddy Challa, Raj Kumar
 * 
 *         Model class for Storing submitted script job for execution
 *
 */

@Entity
@Table(name = "jobs")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "createdAt", "updatedAt" }, allowGetters = true)
public class Job {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String status;

	@Column(columnDefinition = "VARCHAR(3000)")
	private String statusCause;

	private int jobStatusCode;

	private String remoteAgentIP;

	private String scriptFileName;

	private String scriptType;

	private Boolean recordVideo;
	
	private String videoFormat;
	
	private String browserType;
	

	@Column(columnDefinition = "VARCHAR(1000)")
	private String content;

	@Column(columnDefinition = "VARCHAR(500)")
	private String httpparams;

	@Column(nullable = false, updatable = false)
	@CreationTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
	private Timestamp createdAt;
	
	/*
	 * @Column(nullable = false, updatable = false)
	 * 
	 * @CreationTimestamp
	 * 
	 * @Temporal(TemporalType.DATE) private Date createdAt;
	 */

	@Column(nullable = false)
	@UpdateTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
	private Timestamp updatedAt;
	
	private String taskId;

	private int retryCount;
	
	private String appName;
	
	private Long botAgent;
	
	private String paralleExcecutionStatus;
	
	private String applicationPath;
	
	private String agentIp;
	
	private String suiteName;
	
	
	public String getSuiteName() {
		return suiteName;
	}

	public void setSuiteName(String suiteName) {
		this.suiteName = suiteName;
	}

	public String getAgentIp() {
		return agentIp;
	}

	public void setAgentIp(String agentIp) {
		this.agentIp = agentIp;
	}

	public String getApplicationPath() {
		return applicationPath;
	}

	public void setApplicationPath(String applicationPath) {
		this.applicationPath = applicationPath;
	}

	public String getParalleExcecutionStatus() {
		return paralleExcecutionStatus;
	}

	public void setParalleExcecutionStatus(String paralleExcecutionStatus) {
		this.paralleExcecutionStatus = paralleExcecutionStatus;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getScriptType() {
		return scriptType;
	}

	public void setScriptType(String scriptType) {
		this.scriptType = scriptType;
	}

	public Boolean getRecordVideo() {
		return recordVideo;
	}

	public void setRecordVideo(Boolean recordVideo) {
		this.recordVideo = recordVideo;
	}
	
	public String getVideoFormat() {
		return videoFormat;
	}

	public void setVideoFormat(String videoFormat) {
		this.videoFormat = videoFormat;
	}

	public String getHttpparams() {
		return httpparams;
	}

	public void setHttpparams(String httpparams) {
		this.httpparams = httpparams;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/*
	 * public Timestamp getCreatedAt() { return createdAt; }
	 */
	
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getJobStatusCode() {
		return jobStatusCode;
	}

	public void setJobStatusCode(int jobStatusCode) {
		this.jobStatusCode = jobStatusCode;
	}

	public String getRemoteAgentIP() {
		return remoteAgentIP;
	}

	public void setRemoteAgentIP(String remoteAgentIP) {
		this.remoteAgentIP = remoteAgentIP;
	}

	public String getScriptFileName() {
		return scriptFileName;
	}

	public void setScriptFileName(String scriptFileName) {
		this.scriptFileName = scriptFileName;
	}

	public String getStatusCause() {
		return statusCause;
	}

	public void setStatusCause(String statusCause) {
		this.statusCause = statusCause;
	}

	public Long getBotAgent() {
		return botAgent;
	}

	public void setBotAgent(Long botAgent) {
		this.botAgent = botAgent;
	}
	public String getBrowserType() {
		return browserType;
	}

	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}


}
