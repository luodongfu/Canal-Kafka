#!/bin/bash
current_path=`pwd`
base=$current_path/..
logback_configurationFile=$base/conf/logback.xml

JAVA=$(which java)
JAVA_OPTS="-server -Xms2048m -Xmx2048m -Xmn512m -XX:SurvivorRatio=2 -XX:PermSize=96m -XX:MaxPermSize=256m -Xss256k -XX:-UseAdaptiveSizePolicy -XX:MaxTenuringThreshold=15 -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:+HeapDumpOnOutOfMemoryError -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Dfile.encoding=UTF-8"

for i in $base/lib/*;
	do CLASSPATH=$i:"$CLASSPATH";
done
CLASSPATH="$base/conf:$CLASSPATH";

CANAL_OPTS="-DappName=canal-client -Dlogback.configurationFile=$logback_configurationFile"

$JAVA $JAVA_OPTS $CANAL_OPTS -classpath .:$CLASSPATH com.gzcb.creditcard.ido.binlog.BinLogServerApplication 1>>$base/logs/canal.log 2>&1 &

echo 'start success...'
