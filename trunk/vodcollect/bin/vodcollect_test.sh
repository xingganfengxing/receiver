#!/bin/bash
PROJECT_PATH=/data/soft/vodcollect;
function start(){
	ip=`ifconfig | grep inet | sed -n '1p' | awk '{print $2}' | awk -F ':' '{print $2}'`
	#ip=`hostname`
    jmxports=$@
	i=-1
	for jmxport in $jmxports
	do
	if [ $i != -1 ]
	then
	java -cp ${PROJECT_PATH}:${PROJECT_PATH}/lib/*:${PROJECT_PATH}/vodcollect.jar\
        -server -Xmx1G -Xms1G -XX:PermSize=64M -XX:MaxPermSize=64M\
        -XX:MaxTenuringThreshold=15 -XX:NewRatio=1 -XX:SurvivorRatio=4\
        -XX:+UseParallelGC -XX:+UseParallelOldGC -XX:MaxGCPauseMillis=100\
        -XX:LargePageSizeInBytes=4M -XX:+HeapDumpOnOutOfMemoryError\
        -Djava.library.path=/usr/local/lib -DJMAX_PORT=$jmxport\
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
