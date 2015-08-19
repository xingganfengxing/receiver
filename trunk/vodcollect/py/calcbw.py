# !/usr/bin/env python
# -*- coding: utf-8 -*-
#    计算带宽
#    Copyright 2015-05-26 liufeng1

import re
import string
import glob


def calcOneFile(f_in):
    file_in = open(f_in)
    bw_sum = 0
    index = 0
    total = 0
    while 1:
        lines = file_in.readlines(100000)
        if not lines:
            break
        total += len(lines)
        for line in lines:
            index += 1
            if "[14/Jun/2015:22:55" in line:
                strArrs = line.split("\"")
                i = 8
                while i < len(strArrs) and len(strArrs[i]) < 20:
                    i += 1
                bwArrs = strArrs[i].split(" ")
                bwStr = bwArrs[-3]

                pattern = re.compile(r'\d*')
                if not pattern.match(bwStr):
                    bwStr = strArrs[2].split(" ")[2]

                try:
                    bw_sum += string.atoi(bwStr)
                    print ("index/total:%d/%d,bw:%d" % (index, total, bw_sum))
                except Exception:
                    pass
    file_in.close()
    return bw_sum


def calcDirFile(dir_in, pattern):
    bw_total = 0
    for file in glob.glob(dir_in + "/" + pattern):
        print file
        bw_total += calcOneFile(file)
        print ("total: %d" % bw_total)
    print "finished calced"


if __name__ == "__main__":
    # dir_in = "/data/soft/vodcollect/originlogs/20150603/"
    # calcDirFile(dir_in, "*_2015060305*")
    dir_in = "F:\\"
    calcDirFile(dir_in, "cloud_100172_2015061422.log")
