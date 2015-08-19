#!/bin/bash

function start(){
    nohup /usr/bin/python /data/soft/vodcollect/bin/runman.py >/dev/null 2>&1 &
}

function stop(){
    kill `ps -ef | grep runman | awk -F" " '{print $2}' | head -2`
}

case "$1" in
   'start')
      start
      ;;
   'stop')
      stop
      ;;
   *)
      echo "Usage:runman(start|stop)"
esac
exit 0
