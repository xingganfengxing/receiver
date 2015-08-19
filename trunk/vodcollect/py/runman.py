# !/usr/bin/env python
# -*- coding: utf-8 -*-
#    跑男原始日志存储
#    ws_bpxd_1800
#    ws_bpxd_1300
#    ws_bpxd_800
#    ws_bpxd_350
#    Copyright 2015-05-26 liufeng1

import glob
import gzip
import datetime
import time
import logging
from logging.handlers import RotatingFileHandler


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


def filter(line):
    return "ws_bpxd_1800" in line or \
           "ws_bpxd_1300" in line or \
           "ws_bpxd_800" in line or \
           "ws_bpxd_350" in line or \
           "hz_bpxd_1300" in line or \
           "hz_bpxd_800" in line or \
           "hz_bpxd_1800" in line or \
           "hz_bpxd_350" in line


def parse(line):
    parseDict = {}

    # 截取日志时间
    start = line.find("[") + 1
    end = line.find("]")
    timeStr = line[start:end]
    ptime = datetime.datetime.strptime(timeStr, "%d/%b/%Y:%H:%M:%S +0800")
    timeAppend = ptime.strftime("%Y%m%d%H")
    parseDict['timeHour'] = timeAppend

    # 截取streamid
    quotesArr = line.split("\"")
    start = quotesArr[1].find("?") + 1
    url = quotesArr[1][start:]
    yuArr = url.split("&")

    for params in yuArr:
        dengArr = params.split("=")
        parseDict[dengArr[0]] = dengArr[1]

    return parseDict


def findOneFile(f_in, outPath):
    f = gzip.open(f_in, 'rb')
    file_content = f.read()
    outFileKey = outPath + "/live_"
    saveDict = {}
    for line in file_content.split("\n"):
        if filter(line):
            parseDict = parse(line)
            outFileKey = outFileKey + parseDict["lb_src_streamid"] \
                         + "_" + parseDict["timeHour"] + ".log"
            if outFileKey not in saveDict.keys():
                resultList = []
                saveDict[outFileKey] = resultList
            saveDict[outFileKey].append(line)
        outFileKey = outPath + "/live_"

    for key in saveDict:
        fileOut = open(key, 'w+')
        fileOut.write("\n".join(saveDict[key]))
        logger.info("write %d lines to file %s", len(saveDict[key]), key)
        fileOut.close()

    f.close()


def findInDir(zmqPath, outPath, pattern, logger):
    startTime = endTime = time.clock()
    fileList = glob.glob(zmqPath + "/" + pattern)
    totalCount = len(fileList)
    logger.info("start find runman log,total file size:%d", totalCount)
    count = 0
    for file in fileList:
        findOneFile(file, outPath)
        count += 1
        endTime = time.clock()
        logger.info("count/total %d/%d,finded file %s,sumCost : %.03f seconds"
                    % (count, totalCount, file, (endTime - startTime)))
    logger.info("finded %d files,totalCost : %.03f seconds" % (count, (endTime - startTime)))


if __name__ == "__main__":
    # zmqPath = "F:\\zmqfiles"
    # outPath = "F:\\output"
    # logPath = "F:\\output\\runman.log"
    # logger = initLogger(logPath)
    # findInDir(zmqPath, outPath, "runmantest.txt.gz", logger)

    dayStr = (datetime.date.today() - datetime.timedelta(days=1)).strftime("%Y%m%d")
    zmqPath = "/data/soft/vodcollect/zmqfiles/" + dayStr
    outPath = "/data/soft/vodcollect/originlogs/" + dayStr
    logPath = "/data/soft/vodcollect/logs_8999/runman.log"
    logger = initLogger(logPath)
    findInDir(zmqPath, outPath, "*" + dayStr + "2[1-2]*", logger)
