package com.saviynt.identitybot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.saviynt.identitybot.model.Suite;



public interface SuiteRepository extends JpaRepository<Suite, Long>{
	
	@Modifying
	@Transactional
	@Query("update Suite s set s.executionType = ?, s.browserType = ? , s.parallelExecution = ? where s.id = ? ")
	void updateSuiteById(Long executionType,String browserType,String parallelExecution,Long id);
	
	@Query("FROM Suite b where b.suiteName = ?")
	List<Suite> getAllSuitesfindBySuiteName(String suiteName);
	
	

}
