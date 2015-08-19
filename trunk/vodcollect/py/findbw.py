# !/usr/bin/env python
# -*- coding: utf-8 -*-
#    查找异常带宽
#    Copyright 2015-05-26 liufeng1

import re
import os
import string


def findOneFile(f_in, f_out):
    file_in = open(f_in)
    bws = []
    while 1:
        lines = file_in.readlines(100000)
        if not lines:
            break
        for line in lines:
            # if "splatid=302" in line:
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
                    if string.atoi(bwStr) > 100000000:
                        bws.append(bwStr)
                        bws.append(line)
                except Exception:
                    pass

    file_in.close()

    if len(bws) > 0:
        file_out = open(f_out, 'w+')
        file_out.writelines("\n".join(bws))
        file_out.close()


def findDirFile(dir_in, dir_out):
    for file in os.listdir(dir_in):
        findOneFile(dir_in + file, dir_out + file)
        print "scaned file:" + file.title()
    print "finished scaned"


if __name__ == "__main__":
    dir_in = "/data/soft/vodcollect/originlogs/20150527/"
    dir_out = "/test/bw/"

    # dir_in = "F:\\logs\\"
    # dir_out = "F:\\bw\\"

    findDirFile(dir_in, dir_out)
