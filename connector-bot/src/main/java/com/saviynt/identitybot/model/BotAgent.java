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

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "bot_agent")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "lastUpdatedTimestamp" }, allowGetters = true)
public class BotAgent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sno;

	@Column(columnDefinition = "VARCHAR(500)")
	private String agentId;
	
	@Column(columnDefinition = "VARCHAR(500)")
	private String agentName;
	
	@Column(columnDefinition = "VARCHAR(500)")
	private String ipaddress;
	
	private Integer loadBalancerFlag;

	private Integer numberOfInstances;
	
	@Column(nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date lastUpdatedTimestamp;
	
	
	
	public Integer getNumberOfInstances() {
		return numberOfInstances;
	}

	public void setNumberOfInstances(Integer numberOfInstances) {
		this.numberOfInstances = numberOfInstances;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public Long getSno() {
		return sno;
	}

	public void setSno(Long sno) {
		this.sno = sno;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public Date getLastUpdatedTimestamp() {
		return lastUpdatedTimestamp;
	}

	public void setLastUpdatedTimestamp(Date lastUpdatedTimestamp) {
		this.lastUpdatedTimestamp = lastUpdatedTimestamp;
	}

	public Integer getLoadBalancerFlag() {
		return loadBalancerFlag;
	}

	public void setLoadBalancerFlag(Integer loadBalancerFlag) {
		this.loadBalancerFlag = loadBalancerFlag;
	}

}
