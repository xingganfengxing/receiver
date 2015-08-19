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


def rsync_hdfsbridge():
    run('mkdir -p /data/soft/hdfsbridge')
    put("/opt/deploy/hdfsbridge/hdfsbridge/*", "/data/soft/hdfsbridge")


def start_hdfsbridge():
    with cd('/data/soft/hdfsbridge/bin/'):
        run('chmod 777 hdfsbridge.sh')
        run('set -m;nohup ./hdfsbridge.sh start getRtmpAccLog &')


def stop_hdfsbridge():
    with cd('/data/soft/hdfsbridge/bin/'):
        run('chmod 777 hdfsbridge.sh')
        run('set -m;./hdfsbridge.sh stop')

def rm_files():
    run('rm -rf /data/soft/hdfsbridge')
