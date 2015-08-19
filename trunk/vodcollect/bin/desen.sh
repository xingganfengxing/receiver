#日志脱敏
#!/bin/bash

PRO_LOG_BATHPATH="/data/soft/vodcollect/originlogs"
LOGPATH="/data/soft/vodcollect/logs_8999/desen.log"

function process(){

    #将sip替换为-
    sed 's/^[0-9]\{1,3\}.[0-9]\{1,3\}.[0-9]\{1,3\}.[0-9]\{1,3\}:[0-9]\{1,\}/-/g' $1 >> $2

    #过滤掉包含127.0.0.1的行
    sed -i '/^- 127.0.0.1/d' $2
}

function main(){
    
    echo '' > $LOGPATH
    
    day=`date -d last-day +%Y%m%d`
    
    all_start=`date +%s`
       
    path=$PRO_LOG_BATHPATH"/"$day
    
    list=`ls $path`
    i=0
    for f in $list
    do
        start=`date +%s`
        process $path"/"$f $path"/"$f".desen"
        end=`date +%s`
        let cost=$end-$start
       
        t_now=`date "+%Y-%m-%d %H:%M:%S"`
        echo $t_now":desen file:"$path"/"$f",cost:"$cost"s,file count:"$i >> $LOGPATH
        let i+=1
    done

    all_end=`date +%s`
    
    let all_cost=$all_end-$all_start
    t_now=`date "+%Y-%m-%d %H:%M:%S"`
    echo $t_now":desen all success,file_count:"$i",cost "$all_cost"s" >> $LOGPATH
}

main
