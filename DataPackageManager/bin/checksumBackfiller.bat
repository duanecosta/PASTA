REM  Run this from the DataPackageManager top-level directory. %TOMCAT% must be defined.
java -cp WebRoot\WEB-INF\lib\common.jar;WebRoot\WEB-INF\lib\commons-logging-1.1.1.jar;WebRoot\WEB-INF\lib\datamanager.jar;WebRoot\WEB-INF\lib\postgresql-8.4-702.jdbc4.jar;WebRoot\WEB-INF\lib\utilities.jar;WebRoot\WEB-INF\classes;WebRoot\WEB-INF\lib\log4j-1.2.13.jar;WebRoot\WEB-INF\lib\common-io-2.1.jar;WebRoot\WEB-INF\lib\commons-codec-1.7.jar;%TOMCAT%\lib\servlet-api.jar edu.lternet.pasta.datapackagemanager.checksum.ChecksumBackfiller
