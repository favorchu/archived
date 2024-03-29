<?xml version="1.0"?>
<!DOCTYPE hibernate-mapping PUBLIC 
	"-//Hibernate/Hibernate Mapping DTD 3.0//EN"
	"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">

<#if hmgs?exists && hmgs.hasNonDefaultSettings()>
<hibernate-mapping
<#if hmgs.hasDefaultPackage()>
 package="${hmgs.defaultPackage}"
 </#if>
<#if hmgs.hasSchemaName()>
 schema="${hmgs.schemaName}"
 </#if>
<#if hmgs.hasNonDefaultCascade()>
 default-cascade="${hmgs.defaultCascade}"
 </#if>
<#if hmgs.hasNonDefaultAccess()>
 default-access="${hmgs.defaultAccess}"
 </#if>
<#if !hmgs.isDefaultLazy()>
	default-lazy="false"
	</#if>
<#if !hmgs.isAutoImport()>
	auto-import="false"
</#if>>
<#else>
<hibernate-mapping>
</#if>

<#include "persistentclass.hbm.ftl"/>

</hibernate-mapping>