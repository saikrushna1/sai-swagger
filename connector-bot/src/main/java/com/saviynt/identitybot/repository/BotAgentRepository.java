/**
 * 
 */
package com.saviynt.identitybot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.saviynt.identitybot.model.BotAgent;



/**
 * @author venkat
 *
 */
@Repository
public interface BotAgentRepository extends JpaRepository<BotAgent, Long> {

	BotAgent findByAgentId(String agentId);
	
	@Modifying
	@Transactional
	@Query("update BotAgent b set b.ipaddress = ? , b.lastUpdatedTimestamp = now() where b.sno = ?")
	void updateIpaddressAndLastUpdatedTimestampBySno(String ipaddress,Long sno);
	
	
	@Query("FROM BotAgent ORDER BY lastUpdatedTimestamp DESC")
	List<BotAgent> findAllByTimestampDesc();

	@Query(value="SELECT * FROM bot_agent b where TIMESTAMPDIFF(MINUTE,b.last_updated_timestamp,NOW()) <= :status AND b.load_balancer_flag=0 LIMIT 1", nativeQuery = true)
	List<BotAgent> findByLastUpdatedTimestamp(@Param("status")Integer status);
	
	@Modifying
	@Transactional
	@Query("update BotAgent b set b.loadBalancerFlag = 0 where b.sno = ?")
	void updateBotAgentLoadBalancerBySno(Long sno);


	@Modifying
	@Transactional
	@Query("update BotAgent b set b.numberOfInstances = ? where b.sno = ?")
	void updateBotAgentNumberOfInstancesBySno(Integer numberOfInstances,Long sno);
	
	@Modifying
	@Transactional
	@Query("update BotAgent b set b.loadBalancerFlag = ?, b.numberOfInstances = ? where b.sno = ?")
	void updateBotAgentLoadBalancerFlagBySno(Integer loadBalancerFlag ,Integer numberOfInstances,Long sno);
	
	@Modifying
	@Transactional
	@Query("update BotAgent b set b.loadBalancerFlag = ?, b.numberOfInstances = ? where b.agentId = ?")
	void updateBotAgentLoadBalancerFlagByAgentId(Integer loadBalancerFlag ,Integer numberOfInstances,String agentId);
	
	
	@Modifying
	@Transactional
	@Query("update Job b set b.status = 'Fail' where b.status in ('Submitted','Inprocess') ")
	void updateJobStatus();

	@Query("FROM BotAgent b where b.agentId =? or b.ipaddress=? and b.loadBalancerFlag=0")
	BotAgent findByAgentIdOrIpaddressWithLoadBalancer(String agentId,String ipaddress);
	
	@Query("FROM BotAgent b where b.agentId =? or b.ipaddress=?")
	BotAgent findByAgentIdOrIpaddress(String agentId,String ipaddress);
	
	@Query("FROM BotAgent b ORDER BY b.numberOfInstances ASC")
	List<BotAgent> findAllByNumberOfInstancesByAsc();
	
	@Query("FROM BotAgent b where b.sno =?")
	BotAgent findByAgentSno(Long sno);
	
	@Modifying
	@Transactional
	@Query("update BotAgent b set b.loadBalancerFlag = ?, b.numberOfInstances = ? where b.sno = ? and b.loadBalancerFlag = 0")
	int updateBotAgentLoadBalancerFlagBySnoAndLoadbalancerFlag(Integer loadBalancerFlag ,Integer numberOfInstances,Long sno);
}
