package com.saviynt.identitybot.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.saviynt.identitybot.model.Job;

/**
 * @author Srinivasa Reddy Challa, Raj Kumar
 *
 */

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JobRepositoryCustom {

	/**
	 * get first one {@code Job} object where RemoteAgent IP is not null This is
	 * used for executing scripts on remote agent.
	 * 
	 * @param status
	 * @return {@code Job}
	 */
	@Query(value="SELECT count(*) FROM jobs", nativeQuery = true)
	Long getTotalJobs();
	
	@Query(value="SELECT count(*) FROM jobs where status='Success' ", nativeQuery = true)
	Long getAllSuccessJobs();
	
	@Query(value="SELECT count(*) FROM jobs where status='Inprocess' or status='RetryInprogress' ", nativeQuery = true)
	Long getAllInprocessJobs();
	
	@Query(value="SELECT count(*) FROM jobs where status='Fail'", nativeQuery = true)
	Long getAllFailedJobs();
	
	//@Query(value="SELECT * FROM jobs b WHERE b.status = 'Submitted' or b.status ='Failed' or (b.remote_agentip is not null and b.status = 'Submitted')", nativeQuery = true)
	//@Query(value="SELECT * FROM jobs b WHERE b.status = 'Submitted' or b.status ='Failed' or (b.remote_agentip is not null and b.status = 'Submitted') limit 1", nativeQuery = true)
	  @Query(value="SELECT * FROM jobs b WHERE b.status = 'Submitted' or b.status ='Failed' or (b.remote_agentip is not null and b.status = 'Submitted') order by  suite_id asc , id asc limit 1", nativeQuery = true)
	List<Job> findFirst1ByStatusOrRemoteAgentIPIsNotNull();

	/**
	 * get first one {@code Job} object where RemoteAgent IP is null This is used
	 * for executing scripts on same machine where cobot server is running
	 * 
	 * @param status
	 * @return {@code Job}
	 */

	//Job findFirst1ByStatusAndRemoteAgentIPIsNull(String status);
	
	//Job findFirst1ByStatus(String status);

	@Query("FROM Job ORDER BY id ASC")
	List<Job> findAllByCreatedAtDesc();
	
	@Query("FROM Job GROUP BY scriptFileName ORDER BY id ASC")
	List<Job> findGroupByAllScripts();
	
	Job findById(Long parseInt);
	
	@Query("FROM Job b where b.status ='Inprocess' or b.status ='RetryInprogress' and botAgent=?")
	List<Job> findByAgentSnoWithApplicatoinName(Long agentId);
	
	@Modifying
	@Transactional
	@Query("update Job  set suite_name = ?,paralle_excecution_status=?, status ='Submitted', retryCount = 0 , created_at = ?  where suite_id = ? ")
	void updateJobBySuiteId(String suiteName,String pExexutionObj, Timestamp currentTime, Long suite_id);
	
	@Modifying
	@Transactional
	@Query(value="delete FROM jobs where suite_id=?", nativeQuery = true)
	void deleteJobsBySuiteId(Long id);
}
