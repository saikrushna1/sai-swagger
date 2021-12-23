package com.saviynt.identitybot.model;

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
@Table(name = "bot_scripts")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "createdAt", "updatedAt" }, allowGetters = true)
public class BotScripts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String appName;
	
	private String scriptFileName;
	
	private String moduleName;
	
	private String typeOfTest;
	
	private String url;

	private String scriptType;
	
	private String createdBy;
	
	private String updatedBy;
	
	@Column(columnDefinition = "VARCHAR(1000)")
	private String http_payload;

	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	//@Temporal(TemporalType.DATE)
    private Date createdAt;
 
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getScriptFileName() {
		return scriptFileName;
	}

	public void setScriptFileName(String scriptFileName) {
		this.scriptFileName = scriptFileName;
	}

	public String getScriptType() {
		return scriptType;
	}

	public void setScriptType(String scriptType) {
		this.scriptType = scriptType;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getHttp_payload() {
		return http_payload;
	}

	public void setHttp_payload(String http_payload) {
		this.http_payload = http_payload;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	
	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getTypeOfTest() {
		return typeOfTest;
	}

	public void setTypeOfTest(String typeOfTest) {
		this.typeOfTest = typeOfTest;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\", \"appName\":\"" + appName + "\", \"scriptFileName\":\"" + scriptFileName + "\", \"moduleName\":\""+moduleName+"\", \"typeOfTest\":\""+typeOfTest+"\",\"url\":\""+url+"\",\"scriptType\":\""
				+ scriptType + "\", \"createdBy\":\"" + createdBy + "\", \"updatedBy\":\"" + updatedBy + "\", \"http_payload\":"
				+ http_payload + ", \"createdAt\":\"" + createdAt + "\", \"updatedAt\":\"" + updatedAt + "\"}";
	}
	
	
}
