# !/usr/bin/env python
# -*- coding: utf-8 -*-
#    查找直播hls,flv日志
#    Copyright 2015-05-26 liufeng1

import glob
import gzip
import datetime
import time
import logging
from logging.handlers import RotatingFileHandler

LIVE_FLAG = "clive_cname"


def initLogger(logpath):
    logger = logging.getLogger('mylogger')
    logger.setLevel(logging.DEBUG)

    # 创建一个handler，用于写入日志文件
    rth = RotatingFileHandler(logpath, maxBytes=10 * 1024 * 1024, backupCount=5)
    rth.setLevel(logging.INFO)
    formatter = logging.Formatter('%(asctime)s %(levelname)s %(message)s')
    rth.setFormatter(formatter)

    # 给logger添加handler
    logger.addHandler(rth)
    return logger

logPath = "/data/soft/vodcollect/logs_8999/live_hls_flv.log"
# logPath = "F:\\output\\live_hls_flv.log"
LOG = initLogger(logPath)


def filter(line):
    return LIVE_FLAG in line


def parse(line):
    parseDict = {}

    # 截取日志时间
    start = line.find("[") + 1
    end = line.find("]")
    timeStr = line[start:end]
    ptime = datetime.datetime.strptime(timeStr, "%d/%b/%Y:%H:%M:%S +0800")
    timeAppend = ptime.strftime("%Y%m%d")
    parseDict['timeday'] = timeAppend

    # 计算协议
    protocol = ""
    if ".m3u8" in line or ".ts" in line:
        protocol = "hls"
    if ".flv" in line:
        protocol = "flv"
    parseDict['protocol'] = protocol

    # 截取域名
    start = line.find(LIVE_FLAG) + len(LIVE_FLAG) + 1
    end = line.find(" ", start)
    domain = line[start:end]
    parseDict['domain'] = domain

    return parseDict


def findOneFile(f_in, outPath):
    f = gzip.open(f_in, 'rb')
    file_content = f.read()

    # live_协议_域名_天
    outFileKey = outPath + "/live_"
    saveDict = {}
    for line in file_content.split("\n"):
        if filter(line):
            parseDict = parse(line)
            outFileKey = outFileKey \
                         + parseDict["protocol"] + "_" \
                         + parseDict["domain"] + "_" \
                         + parseDict["timeday"] + ".log"
            if outFileKey not in saveDict.keys():
                resultList = []
                saveDict[outFileKey] = resultList
            saveDict[outFileKey].append(line)
        outFileKey = outPath + "/live_"

    for key in saveDict:
        fileOut = open(key, 'a')
        fileOut.write("\n".join(saveDict[key]))
        LOG.info("write %d lines to file %s", len(saveDict[key]), key)
        fileOut.close()

    f.close()


def findInDir(zmqPath, outPath, pattern):
    startTime = endTime = time.clock()
    fileList = glob.glob(zmqPath + "/" + pattern)
    totalCount = len(fileList)
    LOG.info("start find live_hls_flv log,total file size:%d", totalCount)
    count = 0
    for file in fileList:
        findOneFile(file, outPath)
        count += 1
        endTime = time.clock()
        LOG.info("count/total %d/%d,finded file %s,sumCost : %.03f seconds"
                    % (count, totalCount, file, (endTime - startTime)))
    LOG.info("finded %d files,totalCost : %.03f seconds" % (count, (endTime - startTime)))


if __name__ == "__main__":
    # zmqPath = "F:\\livelog"
    # outPath = "F:\\output"
    # findInDir(zmqPath, outPath, "*.gz")

    dayStr = datetime.date.today().strftime("%Y%m%d")
    zmqPath = "/data/soft/vodcollect/zmqfiles/" + dayStr
    outPath = "/data/soft/vodcollect/originlogs/" + dayStr
    findInDir(zmqPath, outPath, "*" + dayStr + "1[3-4]*")
