package com.saviynt.identitybot.repository;

/**
 * 
 * @author Srinivasa Reddy Challa, Raj Kumar
 *
 */

public interface JobRepositoryCustom {

	/**
	 * update the status of the {@code Job} with given id
	 * @param botAgent 
	 * 
	 * @param status: status of the job execution
	 * @param id:     id of the {@code Job}
	 * @return updated status
	 */
	int updateStatus(int retryCount,String status, Long id, Long botAgent,String remoteIp);

	/**
	 * update the status & reason for the status of the {@code Job} with given id
	 * 
	 * @param status: status of the job execution
	 * @param reason  : reason for this status
	 * @param id:     id of the {@code Job}
	 * @return updated status
	 */

	int updateStatusAndReason(String status, String reason, Long id);
}
