for /f %%a in ('powershell -Command "Get-Date -format yyyyMMdd_HHMM"') do set datetime=%%a
echo %datetime%
SET JAVA_EXE=D:\BD-CNPIS\Software\jdk-11.0.1\bin\java
SET CLASSPATH=D:\BD-CNPIS\QuickScripts\Common\jars\*
SET DBNAME=CNPIS
SET folder=%DBNAME%.%datetime%
SET DBDRIVER=oracle.jdbc.OracleDriver
SET CONN=jdbc:oracle:thin:@localhost:1521:%DBNAME%
SET DBUSER=cnpis_dba
SET DBPASSWORD=password
rd /s/q %folder%


REM Export Permission
%JAVA_EXE%  -cp %CLASSPATH%   db.tools.sql2.result2csv.Result2CsvApp ^
-f %DBUSER%.ROLE_ROLE_PRIVS ^
-sql "select * from ROLE_ROLE_PRIVS where ROLE IN (select granted_role from USER_ROLE_PRIVS where USERNAME= USER)" ^
-d %DBDRIVER% -con  %CONN% -u  %DBUSER% -p %DBPASSWORD% -o %folder% 

%JAVA_EXE%  -cp %CLASSPATH%   db.tools.sql2.result2csv.Result2CsvApp ^
-f %DBUSER%.ROLE_TAB_PRIVS ^
-sql "select * from ROLE_TAB_PRIVS  where ROLE IN (select granted_role from USER_ROLE_PRIVS where USERNAME= USER)" ^
-d %DBDRIVER% -con  %CONN% -u  %DBUSER% -p %DBPASSWORD% -o %folder% 

%JAVA_EXE%  -cp %CLASSPATH%   db.tools.sql2.result2csv.Result2CsvApp ^
-f %DBUSER%.ROLE_SYS_PRIVS ^
-sql "select * from ROLE_SYS_PRIVS  where ROLE IN (select granted_role from USER_ROLE_PRIVS where USERNAME= USER)" ^
-d %DBDRIVER% -con  %CONN% -u  %DBUSER% -p %DBPASSWORD% -o %folder% 
 
 
REM Export Schema
%JAVA_EXE%  -cp %CLASSPATH%   db.tools.sql2.row2file.Row2fileApp ^
-o %folder% ^
-cs subfolder ^
-cf filename ^
-cc content ^
-sql "select object_type as subfolder,  OBJECT_NAME ||'.SQL' As filename,  DBMS_METADATA.GET_DDL(object_type,OBJECT_NAME) as content  from ALL_OBJECTS where owner = 'CNPIS_DBA' and object_type  not in ('LOB','PACKAGE BODY') and not (object_type ='TYPE' and (OBJECT_NAME like 'KU$_%%'  or OBJECT_NAME like 'SMP_VD%%')) and not (object_type ='VIEW' and (OBJECT_NAME  NOT like 'V_CNPIS%%' ))" ^
-d %DBDRIVER% ^
-con  %CONN% ^
-u  %DBUSER% ^
-p %DBPASSWORD% ^
-tr


7z a %folder%.7z %folder%\* && rd /s/q %folder%

