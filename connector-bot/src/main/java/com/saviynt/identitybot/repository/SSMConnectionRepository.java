package com.saviynt.identitybot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.saviynt.identitybot.model.SSMConnections;

@Repository
public interface SSMConnectionRepository extends JpaRepository<SSMConnections, Long>{

	
	SSMConnections findByScriptFileNameAndConnectionNameAndScriptType(String fileName,String rootDir,String scriptType);
	
	@Modifying
	@Transactional
	@Query("update SSMConnections c set c.recordVideo = ? , c.videoFormat = ?, c.remoteIp = ? , c.paralleExcecutionStatus = ? ,c.connectionName = ? ,c.scriptFileName = ? where c.id = ? ")
	void updateRemoteIpAndExecutionStatusByConnectionNameAndScriptName(Boolean recordVideo,String videoFormat,String remoteIp,String paralleExcecutionStatus,String connectionName,String scriptFileName,Long id);
	
//	@Modifying
//	@Transactional
//	@Query("update SSMConnections c set c.connectionName = ? , c.connectionType = ? , scriptFileName = ? where c.id = ? ")
//	void updateConnectionNameAndConnectionTypeAndScriptFileNameById(String rootDir,String rootDir1,String fileName,Long id);
}
