#!/bin/bash
PROJECT_PATH=/data/soft/hdfsbridge;
function start(){
    ip=`hostname`
    commandArgs=$2
	java -cp ${PROJECT_PATH}:${PROJECT_PATH}/lib/*:${PROJECT_PATH}/hdfsbridge.jar\
        -server -Xms2G -Xmx3G -XX:PermSize=512M -XX:MaxPermSize=512M\
        -XX:MaxTenuringThreshold=15 -XX:NewRatio=1 -XX:SurvivorRatio=4\
        -XX:+UseParallelGC -XX:+UseParallelOldGC -XX:MaxGCPauseMillis=100\
        -XX:LargePageSizeInBytes=128M -XX:+HeapDumpOnOutOfMemoryError\
        -Djava.library.path=/usr/local/lib -Djava.rmi.server.hostname=$ip\
        -Dcom.sun.management.jmxremote.port=9999\
        -Dcom.sun.management.jmxremote.ssl=false\
        -Dcom.sun.management.jmxremote.authenticate=false\
        -Dcom.sun.management.jmxremote.password=false\
        com.letvcloud.cdn.log.main.HdfsBridgeMain $commandArgs &
}
function stop(){
	tpid=`jps | grep HdfsBridgeMain | awk '{print $1}'`
	for pid in $tpid
	do
    	    kill $pid
	done 
	echo "killed:"
	echo $tpid
}

case "$1" in
   'start')
      start "$@"
      ;;
   'stop')
      stop
      ;;
   *)
      echo "Usage: hdfsbridge(start getRtmpAccLog|stop)"
esac
exit 0
