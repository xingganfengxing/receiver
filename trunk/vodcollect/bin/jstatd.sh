#!/bin/bash
function start(){
	ip=`ifconfig | grep inet | sed -n '1p' | awk '{print $2}' | awk -F ':' '{print $2}'`
    jstatd -J-Djava.security.policy=/data/soft/vodcollect/bin/vodcollect.policy -J-Djava.rmi.server.hostname=$ip &
}
function stop(){
        tpid=`jps | grep "Jstatd" | awk '{print $1}'`
        for pid in $tpid
        do
            kill -9 $pid
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
      echo "Usage: jstatd(start|stop)" 
esac
exit 0
