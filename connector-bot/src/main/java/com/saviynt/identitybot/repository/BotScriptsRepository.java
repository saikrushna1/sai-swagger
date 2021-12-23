package com.saviynt.identitybot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.saviynt.identitybot.model.BotScripts;

public interface BotScriptsRepository extends JpaRepository<BotScripts, Long>{



//public interface BotScriptsRepository extends PagingAndSortingRepository<BotScripts,Long>{
	
	BotScripts findByScriptFileNameAndAppNameAndScriptType(String fileName, String connectionName, String connectionType);

	@Modifying
	@Transactional
	@Query("update BotScripts c set c.updatedBy = ? where c.id = ? ")
	void updateUserIdById(String updatedBy,Long id);
	
	@Query("SELECT b.appName FROM BotScripts b GROUP BY b.appName")
	List<String> getGroupByAppNames();
	
	@Query("SELECT b.scriptType FROM BotScripts b GROUP BY b.scriptType order by b.scriptType asc")
	List<String> getGroupByScriptTypes();
	
	@Query("FROM BotScripts b where b.appName = ? order by b.scriptFileName asc")
	List<BotScripts> getAllScriptsfindByAppName(String appName);
	
	@Query("SELECT b.appName FROM BotScripts b where b.scriptType = ? order by b.appName asc")
	List<String> getAllAppNamesfindByScriptType(String scriptType);
	
	
	@Query("FROM BotScripts b where b.appName = ? and b.scriptType = ?")
	List<BotScripts> getAllScriptsfindByAppnameAndScriptname(String appName,String scriptName);
	
	
}
