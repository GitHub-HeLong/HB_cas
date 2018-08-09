set SCRIPT_DIR=%~dp0
for %%I in ("%SCRIPT_DIR%") do set CATALINA_BASE_TEST=%%~dpfIapache-tomcat-test
echo CATALINA_BASE_FACE
for %%I in ("%SCRIPT_DIR%") do set CATALINA_HOME_TEST=%%~dpfIapache-tomcat-test
for %%I in ("%SCRIPT_DIR%") do set JAVA_HOME=%%~dpfIJVM\jre7
CALL %CATALINA_BASE_TEST%\bin\startup.bat
