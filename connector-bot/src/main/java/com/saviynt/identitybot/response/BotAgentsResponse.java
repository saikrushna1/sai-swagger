/**
 * 
 */
package com.saviynt.identitybot.response;

import java.util.Date;

/**
 * @author venkat
 *
 */
public class BotAgentsResponse {
	
	private Long sno;
	private String agentId;
	private String agentName;
	private String ipaddress;
	private Date lastUpdatedTimestamp;
	private String status;

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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getSno() {
		return sno;
	}
	public void setSno(Long sno) {
		this.sno = sno;
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
	@Override
	public String toString() {
		return "BotAgentsResponse [sno=" + sno + ", agentId=" + agentId + ", agentName=" + agentName + ", ipaddress="
				+ ipaddress + ", lastUpdatedTimestamp=" + lastUpdatedTimestamp + ", status=" + status + "]";
	}
	
	
	
	
}
