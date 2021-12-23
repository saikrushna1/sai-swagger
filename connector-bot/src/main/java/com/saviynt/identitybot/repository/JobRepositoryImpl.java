package com.saviynt.identitybot.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.saviynt.identitybot.model.Job;

/**
 * 
 * @author Srinivasa Reddy Challa, Raj Kumar
 *
 */
public class JobRepositoryImpl implements JobRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	/**
	 * update the status of the {@code Job} with given id
	 * 
	 * @param status: status of the job execution
	 * @param id:     id of the {@code Job}
	 * @return updated status
	 */
	@Override
	@Transactional
	public int updateStatus(int retryCount,String status, Long id, Long botagent,String remoteIp) {

		Job job = entityManager.find(Job.class, id);
		job.setStatus(status);
		job.setRetryCount(retryCount);
		job.setBotAgent(botagent);
		job.setAgentIp(remoteIp);
		return 0;
	}

	/**
	 * update the status & reason for the status of the {@code Job} with given id
	 * 
	 * @param status: status of the job execution
	 * @param reason  : reason for this status
	 * @param id:     id of the {@code Job}
	 * @return updated status
	 */

	@Override
	@Transactional
	public int updateStatusAndReason(String status, String reason, Long id) {

		Job job = entityManager.find(Job.class, id);
		job.setStatus(status);
		if (reason != null) {
			job.setStatusCause(reason);
		}
		return 0;
	}
}
