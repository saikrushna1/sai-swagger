package com.saviynt.identitybot.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
@Table(name = "suite")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "createdAt", "updatedAt" }, allowGetters = true)
public class Suite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String suiteName;
	
	private String suiteDesc;

	private Long executionType;
	
	private String createdBy;
	
	private String browserType;
	
	private String parallelExecution;
	
	@OneToMany(cascade = CascadeType.ALL)  
	@JoinColumn(name="suite_id",nullable = false)
    private List<Job> job;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	//@Temporal(TemporalType.DATE)
    private Date createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSuiteName() {
		return suiteName;
	}

	public void setSuiteName(String suiteName) {
		this.suiteName = suiteName;
	}

	public String getSuiteDesc() {
		return suiteDesc;
	}

	public void setSuiteDesc(String suiteDesc) {
		this.suiteDesc = suiteDesc;
	}

	public Long getExecutionType() {
		return executionType;
	}

	public void setExecutionType(Long executionType) {
		this.executionType = executionType;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public List<Job> getJob() {
		return job;
	}

	public void setJob(List<Job> job) {
		this.job = job;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getBrowserType() {
		return browserType;
	}

	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}

	public String getParallelExecution() {
		return parallelExecution;
	}

	public void setParallelExecution(String parallelExecution) {
		this.parallelExecution = parallelExecution;
	}

	@Override
	public String toString() {
		return "Suite [id=" + id + ", suiteName=" + suiteName + ", suiteDesc=" + suiteDesc + ", executionType="
				+ executionType + ", createdBy=" + createdBy + ", browserType=" + browserType + ", parallelExecution="
				+ parallelExecution + ", job=" + job + ", createdAt=" + createdAt + "]";
	}
	
}
