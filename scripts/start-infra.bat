@echo off
echo Starting Kafka (KRaft mode)...
start "Kafka" cmd /k "cd /d D:\Downloads\kafka\bin\windows && kafka-server-start.bat D:\Downloads\kafka\config\kraft\server.properties"

echo Starting ActiveMQ...
start "ActiveMQ" cmd /k "cd /d D:\Downloads\apache-activemq-6.3.0\bin\win64 && activemq.bat start"

echo Both launching in separate windows - give them ~10s before starting the services.
echo ActiveMQ admin console: http://localhost:81boot61/admin
pause