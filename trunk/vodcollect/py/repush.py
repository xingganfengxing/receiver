# !/usr/bin/env python
# -*- coding: utf-8 -*-
#    redis突然挂掉，cdn_logs_queue队列还未处理的文件信息丢失,vodcollect接收节点无法在获取这部分文件流。
#    需要大数据节点定时扫描本地缓存目录，获取到本地缓存目录存在且cdn_logs_queue队列中不存在的文件集合，sleep（30s）后，
#    若文件仍存在本地，则重新rpush进cdn_logs_queue中。
#    Copyright 2015-03-31 liufeng1
import os
import time
import logging
from logging.handlers import RotatingFileHandler
import os.path
import subprocess
import redis


def initLogger(logpath):
    logger = logging.getLogger('mylogger')
    logger.setLevel(logging.DEBUG)

    # 创建一个handler，用于写入日志文件
    rth = RotatingFileHandler(logpath, maxBytes=10 * 1024 * 1024, backupCount=5)
    rth.setLevel(logging.INFO)
    formatter = logging.Formatter('%(name)-12s: %(levelname)-8s %(message)s')
    rth.setFormatter(formatter)

    # 给logger添加handler
    logger.addHandler(rth)
    return logger


def getlocalIp(cache_ip_path):
    if os.path.exists(cache_ip_path):
        fo = open(cache_ip_path)
        try:
            ip = fo.read()
        finally:
            fo.close()
        return ip
    else:
        shellCommand = "python /data/cdn/waterfall/waterfall/lib/system.py | tail -n 1 "
        p = subprocess.Popen(shellCommand, stdout=subprocess.PIPE)
        ip = p.stdout.read().decode("utf8")
        fo = open(cache_ip_path, 'w')
        try:
            fo.write(ip)
        finally:
            fo.close()
        return ip


# 10分钟以前的文件才能repush
def canRepush(filepath):
    file_time = os.path.getmtime(filepath)
    sys_time = time.time()
    if (sys_time - file_time) / 60 > 10:
        return True
    else:
        return False


def rpushUnConsumeFile(r_host, r_port, r_db, key,
                       file_path, rpush_log_path, cache_ip_path):
    logger = initLogger(rpush_log_path)

    redisClient = redis.StrictRedis(r_host, r_port, r_db)
    size = redisClient.llen(key)
    if size > 10000:
        logger.error("queue is too long,size:" + str(size))
        return
    redisQueueFiles = []
    for i in range(size):
        tFile = redisClient.lindex(key, i)
        if tFile is not None:
            redisQueueFiles.append(tFile)

    time.sleep(30)  # 休眠30秒 防止重发

    ip = getlocalIp(cache_ip_path)

    files_dir = os.listdir(file_path)
    files = [f for f in files_dir if canRepush(file_path + f)]
    for file in files:
        filepath = file_path + file
        if os.path.exists(filepath):
            ipFilePath = ip + ":" + filepath
            if ipFilePath not in redisQueueFiles:
                redisClient.rpush(key, ipFilePath)
                logger.info("rpush " + ipFilePath + " success!")


if __name__ == "__main__":
    # redis基础变量定义
    r_host = "10.200.91.249"
    r_port = 6379
    r_db = 0

    key = "cdn_logs_queue"
    rpush_log_path = "repush.log"
    cache_ip_path = "ip.txt"

    # log目录
    file_path = "/letvlogroot/"

    rpushUnConsumeFile(r_host, r_port, r_db, key, file_path, rpush_log_path, cache_ip_path)
