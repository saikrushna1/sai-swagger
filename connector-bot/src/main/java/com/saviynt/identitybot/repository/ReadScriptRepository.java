package com.saviynt.identitybot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.saviynt.identitybot.model.ReadScript;

@Repository
public interface ReadScriptRepository extends JpaRepository<ReadScript, Long> {

	@Query(nativeQuery = true, value = "SELECT * FROM read_script as rs WHERE rs.app_name = :appName and  rs.script_name = :scriptName and rs.key_name IN (:keyNmes)")
	public List<ReadScript> findByKeyName(@Param(value="appName")String appName, @Param(value="scriptName")String scriptName, @Param(value="keyNmes")List<String> keyNmes);

}
