set DOMAIN=ywhl-PC
set SCRIPT_DIR=%~dp0
set KEY_DIR=%SCRIPT_DIR%keys\https\
@echo off
if exist %KEY_DIR% goto exist
MD %KEY_DIR%
goto createkey
pause
:exist
del /s /q %KEY_DIR%
:createkey
rem 生成证书
%SCRIPT_DIR%JVM/jdk1.7.0_17/bin/keytool -genkey -alias cas -keypass administrator -storepass administrator -keyalg RSA -keystore %KEY_DIR%key -dname "CN=%DOMAIN%,OU=top,O=top,L=GUILIN,ST=GUILIN,C=ZH"

%SCRIPT_DIR%JVM/jdk1.7.0_17/bin/keytool -export -alias cas -keypass administrator -storepass administrator -file %KEY_DIR%key.crt -keystore %KEY_DIR%key

%SCRIPT_DIR%JVM/jdk1.7.0_17/bin/keytool -import -alias cas -file %KEY_DIR%key.crt -keypass administrator -storepass administrator -keystore %KEY_DIR%cacerts

pause