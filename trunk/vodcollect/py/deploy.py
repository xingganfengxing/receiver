# !/usr/bin/env python
# -*- coding: utf-8 -*-
# fabric 发布脚本
# Copyright 23/4/2015 liufeng1
import sys, time
from fabric.api import *

netifs = {}
now = time.strftime('%Y%m%d-%H%M%S', time.localtime(time.time()))


def host(str=''):
    if str == '-':
        list = _to_list(sys.stdin)
    else:
        list = str.split('\n')
    env.hosts = _to_hosts(list)
    env.passwords = _to_passwords(list)
    print env.passwords
    print env.hosts


def _to_list(handle):
    list = []
    while True:
        line = handle.readline().strip()
        if line:
            list.append(line)
        else:
            return list


def _to_hosts(list):
    hosts = []
    for line in list:
        kvp = line.split('|')
        hosts.append(kvp[1])
    return hosts


def _to_passwords(list):
    passwords = {}
    for line in list:
        kvp = line.split('|')
        passwords[kvp[1]] = kvp[2]
    return passwords


def _to_netifs(list):
    for line in list:
        kvp = line.split('|')
        netifs[kvp[1]] = kvp[0]
    return netifs


def rsync_zmq():
    run('mkdir -p /data/soft')
    put('/opt/deploy/soft/zeromq-2.2.0.tar.gz', '/data/soft')
    put('/opt/deploy/soft/jzmq-2.2.2', '/data/soft')


def install_zmq():
    with cd('/data/soft/'):
        run('tar -zxvf zeromq-2.2.0.tar.gz')
        with cd('/data/soft/zeromq-2.2.0'):
            run('./autogen.sh')
            run('./configure')
            run('make install')


def install_jzmq():
    with cd('/data/soft/jzmq-2.2.2'):
        run('chmod 777 autogen.sh')
        run('chmod 777 configure')
        run('./autogen.sh')
        run('./configure -with-zeromq=/data/soft/zeromq-2.2.0')
        run('make install')
        run('echo "/usr/local/lib" >> /etc/ld.so.conf')
        run('sudo ldconfig')


def rsync_vodcollect_server():
    run('mkdir -p /data/soft/vodcollect_server')
    put("/opt/deploy/vodcollect/vodcollect_server", "/data/soft")


def start_vodcollect_server():
    with cd('/data/soft/vodcollect_server/bin/'):
        run('chmod 777 zmqserver.sh')
        run('set -m;nohup ./zmqserver.sh start 28999 &')


def stop_vodcollect_server():
    with cd('/data/soft/vodcollect_server/bin/'):
        run('set -m;nohup ./zmqserver.sh stop &')


def rsync_vodcollect_test():
    rsync_vodcollect()


def start_vodcollect_test():
    with cd('/data/soft/vodcollect/bin/'):
        run('chmod 777 vodcollect_test.sh')
        run('chmod 777 jstatd.sh')
        run('set -m;nohup ./jstatd.sh start &')
        run('set -m;nohup ./vodcollect_test.sh start 8999 &')


def stop_vodcollect_test():
    with cd('/data/soft/vodcollect/bin/'):
        run('set -m;./jstatd.sh stop')
        run('set -m;./vodcollect_test.sh stop')


def rsync_vodcollect():
    run('mkdir -p /data/soft/vodcollect')
    put("/opt/deploy/vodcollect/vodcollect", "/data/soft")


def start_vodcollect():
    with cd('/data/soft/vodcollect/bin/'):
        run('chmod 777 vodcollect.sh')
        run('chmod 777 jstatd.sh')
        run('set -m;nohup ./jstatd.sh start &')
        run('set -m;nohup ./vodcollect.sh start 8999&')


def stop_vodcollect():
    with cd('/data/soft/vodcollect/bin/'):
        run('chmod 777 vodcollect.sh')
        run('chmod 777 jstatd.sh')
        run('set -m;./jstatd.sh stop')
        run('set -m;./vodcollect.sh stop')


def rsync_log_recover():
    run('mkdir -p /data/soft/logrecover')
    put("/opt/deploy/vodcollect/logrecover", "/data/soft")


def start_log_recover():
    with cd('/data/soft/logrecover/bin/'):
        run('chmod 777 logrecover.sh')
        run('set -m;nohup ./logrecover.sh start &')


def stop_log_recover():
    with cd('/data/soft/logrecover/bin/'):
        run('chmod 777 logrecover.sh')
        run('set -m;./logrecover.sh stop')


def desen_crontab():
    put("/opt/deploy/vodcollect/vodcollect/bin/desen.sh", "/data/soft/vodcollect/bin")
    run('chmod 777 /data/soft/vodcollect/bin/desen.sh')
    run('echo "0 1 * * * /data/soft/vodcollect/bin/desen.sh" >> /var/spool/cron/root')


def clear_desen_crontab():
    run('echo "" > /var/spool/cron/root')


def rm_files():
    # run('rm -rf /data/soft/vodcollect/logs_8999/live_hls_flv.log')
    # run('rm -rf /data/soft/vodcollect/originlogs/20150612/live_hls_r.gslbtest.lecloud.com_20150612.log')
    run('rm -rf /data/soft/vodcollect/lib/lib')
    run('rm -rf /data/soft/vodcollect/lib/vodcollect.jar')


def test():
    # run('ls /data/soft/vodcollect/originlogs/20150612 | grep live | grep -v cdn_')
    run('cat /data/soft/vodcollect/originlogs/20150612/live_flv_r.gslbtest.lecloud.com_20150612.log | wc -l')


def testCat():
    with cd('/data/soft/vodcollect/logs_8999'):
        run('cat *log* | grep "hls" | grep -v "desen" | grep -v "total"')


def runFind():
    put("/opt/deploy/vodcollect/vodcollect/bin/live_hls_flv.py", "/data/soft/vodcollect/bin")
    put("/opt/deploy/vodcollect/vodcollect/bin/livehlsflv.sh", "/data/soft/vodcollect/bin")
    run('chmod 777 /data/soft/vodcollect/bin/live_hls_flv.py')
    run('chmod 777 /data/soft/vodcollect/bin/livehlsflv.sh')
    run('set -m;/data/soft/vodcollect/bin/livehlsflv.sh start')


def stopFind():
    run('set -m;/data/soft/vodcollect/bin/livehlsflv.sh stop')


def runRunman():
    put("/opt/deploy/vodcollect/vodcollect/bin/runman.py", "/data/soft/vodcollect/bin")
    put("/opt/deploy/vodcollect/vodcollect/bin/runman.sh", "/data/soft/vodcollect/bin")
    run('chmod 777 /data/soft/vodcollect/bin/runman.py')
    run('chmod 777 /data/soft/vodcollect/bin/runman.sh')
    run('set -m;/data/soft/vodcollect/bin/runman.sh start')


def stopRunman():
    run('set -m;/data/soft/vodcollect/bin/runman.sh stop')
