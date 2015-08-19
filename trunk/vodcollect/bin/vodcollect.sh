#!/bin/bash
PROJECT_PATH=/data/soft/vodcollect;
function start(){
	#ip=`ifconfig | grep inet | sed -n '1p' | awk '{print $2}' | awk -F ':' '{print $2}'`
	ip=`hostname`
    jmxports=$@
	i=-1
	for jmxport in $jmxports
	do
	if [ $i != -1 ]
	then
	java -cp ${PROJECT_PATH}:${PROJECT_PATH}/lib/*:${PROJECT_PATH}/vodcollect.jar\
        -server -Xmx30G -Xms30G -XX:PermSize=512M -XX:MaxPermSize=512M\
        -XX:MaxTenuringThreshold=15 -XX:NewRatio=1 -XX:SurvivorRatio=4\
        -XX:+UseParallelGC -XX:+UseParallelOldGC -XX:MaxGCPauseMillis=100\
        -XX:LargePageSizeInBytes=128M -XX:+HeapDumpOnOutOfMemoryError\
        -Djava.library.path=/usr/local/lib -Djava.rmi.server.hostname=$ip\
        -Dcom.sun.management.jmxremote.port=$jmxport\
        -DJMAX_PORT=$jmxport -Dcom.sun.management.jmxremote.ssl=false\
        -Dcom.sun.management.jmxremote.authenticate=false\
        -Dcom.sun.management.jmxremote.password=false\
        com.letvcloud.cdn.log.main.CollectMain &
	fi
	let i++
	done
}
function stop(){
	tpid=`jps | grep CollectMain | awk '{print $1}'`
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
      echo "Usage: vodcollect(start jmxport0 jmxport1 jmxport2 jmxport3...|stop)" 
esac
exit 0
