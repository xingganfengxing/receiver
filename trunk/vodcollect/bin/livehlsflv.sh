#!/bin/bash

function start(){
    nohup /usr/bin/python /data/soft/vodcollect/bin/live_hls_flv.py >/dev/null 2>&1 &
}

function stop(){
    kill `ps -ef | grep live_hls_flv | awk -F" " '{print $2}' | head -2`
}

case "$1" in
   'start')
      start
      ;;
   'stop')
      stop
      ;;
   *)
      echo "Usage:livehlsflv(start|stop)" 
esac
exit 0
