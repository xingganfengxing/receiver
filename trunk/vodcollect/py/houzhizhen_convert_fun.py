#!/usr/bin/env python
# -*- coding: utf-8 -*-
#
#    Copyright 2011 timger
#    +Author timger
#    +Gtalk&Email yishenggudou@gmail.com
#    +Msn yishenggudou@msn.cn
#    +Weibo @timger http://t.sina.com/zhanghaibo
#    +twitter @yishenggudou http://twitter.com/yishenggudou
#    Licensed under the MIT License, Version 2.0 (the "License");

u"""
可以用的处理函数
"""

import sys
import os
import gzip
DIR_PATH = '/data/cdn/letvlog'
sys.path.insert(0,os.path.join(DIR_PATH,'letvlog'))
#from conf import unparsedlog
from api.query_letv2 import query_area_by_ip, query_idc_by_ip
from api.ua import detect_device_by_user_agent
from api.datetostr import datetostr
import urlparse
import re
from api.getmaliu import get_maliu
#from rediscount import redisreport


unparsefile = open("/dev/null", 'a')


def errorlog(fun):
    def wrapper(line):
        try:
            return fun(line)
        except Exception, e:
            unparsefile.write(line+'\n')
            return None
    return wrapper



@errorlog
def slowspeed(line):
    #cdef str serverip
    #cdef str userip
    #cdef str date_
    #cdef str isp
    #cdef str idc
    #cdef str area
    #cdef str useridc
    #cdef str ptime
    #cdef str hm
    #cdef str maliu_
    #cdef str maliu
    #cdef str consumer
    #cdef str pid
    #cdef str pla
    #cdef str device
    #cdef str business
    #cdef str filetype
    #cdef str requests
    #cdef str users
    #cdef str slowrequest
    #cdef str s
    #cdef str u
    #cdef list u_
    #cdef list u_1
    fields_list = line.split('+0800]')
    s, u = fields_list[:2]
    u = u.split(' [')[0]
    #有两条日志一起打的情况在这里会报错
    # "-" 112.25.12.133 HIT 10260
    serverip_, userip, date_ = s.split()
    serverip = serverip_
    if ':' in serverip:
        serverip, port = serverip.split(':')
        serverip = serverip.strip()
    else:
        port = '80'
    if serverip == '127.0.0.1':
        #去除127.0.0.1的访问
        return None
    isp, area = query_area_by_ip[userip]
    idc = query_idc_by_ip[serverip]
    qs = query_idc_by_ip.getqs(serverip)
    if qs == '3':
        if port == '80':
            qs = '3'
        elif port == '8080':
            qs = '2'
        elif port == '443':
            qs = '1'
    qs = qs[:2]
    #print type(idc),idc
    useridc = query_idc_by_ip[userip]
    #机房之间
    if  useridc != 'None':
        isp = "letv"
        area = useridc
        #同机房
        if area == idc:
            isp = 'letv-in'
        else:
            if len(useridc) < len(idc):
                short_idc, long_idc = useridc, idc
            else:
                short_idc, long_idc = idc, useridc
            if len(long_idc) - len(short_idc) == 5 and "LIVE-" + short_idc == long_idc:
                isp = 'letv-in'
            
    ptime = datetostr(date_)
    #cdef long ptime_int
    ptime_int = int(ptime[:12])
    ptime = str(ptime_int / 5 * 5)
    u_ = u.split()
    u_1 = u.split('" "')
    #print u_
    #print u_1
    purlparseobj = urlparse.urlparse(u_[1].lstrip('/'))
    hm = u_[-3].strip()
    if hm == 'HIT':
        hit = '1'
    else:
        hit = '0'
    body_send=u_[-2]
    bytes_range=u_[-1].split('=')[-1]
    try:
        queryobj = dict([i.split('=')[:2] for i in purlparseobj.query.strip().strip('\"').split('&')])
    except Exception, e:
        queryobj_ = urlparse.parse_qs(purlparseobj.query)
        queryobj = {}
        for key, value in queryobj_.items():
            queryobj[key] = value[0]

    uuid = queryobj.get("uuid", "")
    #serverip, userip, date_ = s.split()
    #cdef long avc_i
    #cdef long aac_i
    avc_i = purlparseobj.path.upper().find('AVC')
    aac_i = purlparseobj.path.upper().find('AAC')
    if avc_i > 0 and aac_i > 0:
        maliu_ = purlparseobj.path[avc_i + 4: aac_i - 1]
        maliu = get_maliu(int(maliu_))
    else:
        #记为300
        maliu_ = '0'
        maliu = get_maliu(maliu_)

    consumer = queryobj.get('sign', 'null')
    consumer = consumer.split(',')[0]
    #consumer = consumer_ and consumer_[0] or 'null'
    #consumer = 'null'
    pid = queryobj.get('platid', 'Null') or 'Null'
    spid = queryobj.get('splatid', 'Null') or 'Null'
    spid = spid.split('?')[0].split('&')[0]
    #---
    #cdef str ua
    ua = u_1[1].replace(",", " ")
    ###ua钩子
    #_ = re.search('baidu[a-z]{0,}', ua.lower())
    #if _:
    #    ua = _.group()
    #else:
    #    ua = 'null'
    ###end
    #pla, device = detect_device_by_user_agent(ua)
    pla, device = 'null', 'null'
    filetype = purlparseobj.path.split('.')[-1].split('%')[0].split('6')[0]
    if '/' in filetype:
        filetype = 'unknow'
    video_type = queryobj.get('video_type', 'null') or 'null'
    #---
    mpid = pid
    pid = pid + '.' + spid
    #cdef str stream_id
    stream_id = queryobj.get('stream_id', '')
    termid = queryobj.get('termid', 'null').strip()
    #if mpid='10':
    #    if spid == '1001':
    #        pid = 'live_pc'
    if pid == 'Null' or pid == 'Null.Null':
        if u.find('tag=live') > 0 or stream_id:
            if u.find('.ts') > 0:
                pid = 'live_ts'
            elif u.find('.dat') > 0:
                pid = 'live_pc'
            elif  video_type == 'xml':
                pid = "live_pc"
            elif video_type == 'm3u8':
                pid = 'live_ts'
        if u.find('letv-web') > 0:
            pid = 'ad'
        #20130809 watchdog 改成3个分类
        #elif queryobj.get('tag') == 'watchdog':
        #    pid = 'watchdog'
        elif queryobj.get('tag') == 'gug':
            pid = 'ad'

    #修正带宽标签和报表一致
    if pid == '3.302' and queryobj.get('playid') == '2':
        pid = '3.302d'
    if pid == '1.101' and queryobj.get('sign') == 'baofeng':
        pid = '1.103'
    #elif pid == '1.101' and purlparseobj.path.startswith('/video/ts'):
    #    pid = '1.101ts'
    elif mpid == '0' and 'qihu360' in purlparseobj.path:
        pid = '102.10201'
    elif mpid == '0' and 'kingsoft' in purlparseobj.path:
        pid = '102.10202'
    elif mpid == '0' and 'muzhiwan' in purlparseobj.path:
        pid = '102.10203'
    elif mpid == '0' and 'hunantv' in purlparseobj.path:
        pid = '102.10204'
    elif mpid == '0' and 'jiajiatv' in purlparseobj.path:
        pid = '102.10205'
    elif mpid == '0' and 'yinyuetai' in purlparseobj.path:
        pid = '102.10206'
    elif mpid == '0' and 'changba' in purlparseobj.path:
        pid = '102.10207'
    elif pid == 'Null.Null' and queryobj.get('tag') == 'mobile' and queryobj.get('vtype') == 'mp4': # or queryobj.get('vtype') == 'play'):
        pid = '3.320'
    elif pid == 'Null.Null' and queryobj.get('vtype') == 'down':
        pid = 'undownbw'
    elif pid == 'Null.Null' and queryobj.get('sign') == 'coopdown':
        pid = '102.' + queryobj.get('tag', 'null')
    elif pid == '0.0' and queryobj.get('sign') == 'coopdown':
        pid = '102.' + queryobj.get('tag', 'null')

    u"修正3.304"
    if pid == '3.304' and termid == '4':
        pid = '3.304tv'
    bcloud_tag = None
    u"修正2.203商务"
    bcloud_tags = set(['100172', '100001', '100132', '100349', '100388', '100008',
                    '100099', '100755', '100004', '100115', '101711', '106503',
                    '117069', '101428','106250', '106551', '119135', '101165',
                    '100858','100006','101127','101005','100292','113805',
                    '120236','128512','115730',
                    '120477', '116867',
                    '100223', '100774',
"146425",
"145758", "124820",
"121218", "131793",
"144767",
"144099",
"141275",
"106551",
"106551",
"106551",
"140387",
"139844",
"139844",
"139562",
"139562",
"139562",
"134826",
"134826",
"134826",
"116867",
"116867",
"116867",
"100001", "119156", "129027",
"100001", "119156", "129027",
"137652",
"129590", "131797", "127785", "125754",
"129590", "131797", "127785", "125754",
"137126",
"137126",
"137126",
"136508",
"136508",
"136508",
"127343",
"127343",
"136098",
"123966", "132237",
"134074",
"133496",
                    "131629", "128890", "130213", "100539", "100457", "106284", "110162", "116334", 
                    "100575", "101596", "101417", "117059", "100168", "100158", "100012", "101048", 
                    "100141", "104359", "105943", "102931", "111159", "101486", "101485", "111902", 
                    "131631", "129915", "128560", "127145", "128890", "130213"])
    if pid == "2.203" or pid == "2.206":
        bcloud_str = consumer
        if bcloud_str.startswith('bcloud_'):
            bcloud_tag = bcloud_str.split('_')[-1].strip() #.replace('100', '')
            #2.203b349
            if bcloud_tag in bcloud_tags:
                pid = pid + 'b' + bcloud_tag

    if 'letv-uts/18/' in purlparseobj.path:
        pid = pid + 'sl'
    #if '/cibntv/' in purlparseobj.path:
    #    pid = '102.10208'
    """
    20140127 老夏要求
    涉及的标签  对应的参数
    -kf-geshou  &prop=10&bussid=18
    -kf-chunwan &prop=10&bussid=19
    -kf-back0   &prop=10&bussid=20
    -kf-back1   &prop=10&bussid=21
    -kf-back2   &prop=10&bussid=22
    -kf-back3   &prop=10&bussid=23
    -kf-back4   &prop=10&bussid=24
    -kf-back5   &prop=10&bussid=25
    -kf-back6   &prop=10&bussid=26
    -kf-back7   &prop=10&bussid=27
    -kf-back8   &prop=10&bussid=28
    -geshou（兼容现有客户端）   &prop=10&bussid=18
    """
    if 'letv-uts' in purlparseobj.path:
        pid_ = purlparseobj.path.split('letv-uts/')[1].split('/')[0]
        if pid_ in ['19', '20', '21', '22', '23', '24', '25', '26', '27', '28']:
            pid = pid + '.' + pid_

    if purlparseobj.path.startswith('letvabcdeasktf'):
        splatid_v = purlparseobj.path[14:]
        pid = '104.' + splatid_v

    if '/ver_' in purlparseobj.path and (not pid.startswith('2.203b') and not pid.startswith('2.206b')):
        pid = pid + '_new'

    if queryobj.get('temporarykey') and pid == '10.1012':
        pid = '10.1012key'
    elif queryobj.get('temporarykey2') and pid == '10.1012':
        pid = '10.1012key2'

    pay = queryobj.get('pay', 'null')
    if (not pay == '1') and queryobj.get('iscpn') == "f9051":
        pay = 'logined'
    #pay = pay.split(',')[0]
    pid = pid.split('?')[0]

    business = pid
    http_code, contlength, loadtime = u_1[0].strip().strip('\"').split('\" ')[1].split('http')[0].split()[:3]
    http_code = http_code.strip().strip('"')
    filetype = filetype.strip(',').split('&')[0]
    # fix 404
    # 404S 404C
    # 404-s ok  404-c error
    if '404' in http_code:
        if not ('.' in purlparseobj.path[-10:]):
            http_code = '404C'

    header_body_len = fields_list[-1].split()[6]
    try:
        if int(header_body_len) > int(contlength):
            contlength = header_body_len
    except ValueError:
        pass

    bandwidth = contlength
    #cdef str loadtime_str
    loadtime_str = loadtime.strip()
    #cdef float contlength_
    contlength_  = float(contlength)
    #cdef float loadtime_
    loadtime_ = float(loadtime_str)
    #cdef float contlength_k
    contlength_k  = contlength_ #/ 1024
    #ts_case = (contlength_k < 100000 and filetype == 'ts')
    #other_case = (contlength_k < 500000 and filetype != 'ts')
    case_all = contlength_k < 200000
    nobuss_case = (business == 'Null.Null')
    #cdef int maliu_c
    maliu_c  = int(maliu_)
    #if loadtime_ == 0 or ts_case or other_case or nobuss_case:
    # 正常计数为0 慢用户数记为1
    if loadtime_ == 0 or case_all or nobuss_case:
        slowrequest = '0'
    else:
        load_speed = contlength_ * 8 / loadtime_
        if load_speed < maliu_c:
            slowrequest = '1'
        else:
            slowrequest = '0'
    requests = '1'
    users = '0'
    #cdef str sp
    sp = ','
    #cdef str ends
    #cdef str rstline
    #cdef str rstline_
    tag = queryobj.get('tag') or "null"

    #用于计算带宽相关数据
    #rstline = ptime + sp + idc +sp+ isp +sp+ area +sp+ business +sp+ consumer +sp+ filetype +sp+ pla +sp+ device +sp+ bandwidth
    #rstline = rstline + sp + requests +sp+ slowrequest +sp+ users +sp+ maliu +sp+ userip +sp+ hit
    #items = [ptime, idc, isp, area, business, consumer, filetype, pla, device, bandwidth,
    #         requests, slowrequest, users, maliu, userip, hit]

    #rstline_ = rstline +  sp + http_code + sp + qs
    rstline_ = sp.join([ptime, idc, isp, area, business, bandwidth, maliu, requests, qs, http_code])
    #用于计算慢速比和其他数据

    #ua = 'null' #why
    path = purlparseobj.path
    #path = 'path'
    #用于做转换后计算
    btag = queryobj.get('b') or 'null'
    tss = queryobj.get('tss') or ''
    ostype = queryobj.get('ostype') or 'nu'
    utp = queryobj.get('utp') or 'nu'
    sip_sip = serverip_.split(':')
    if len(sip_sip) ==  2:
        sip, sport = sip_sip
    else:
        sip = sip_sip[0]
        sport = '80'
    converted = sp.join([ptime,
                        idc,
                        isp,
                        area,
                        business,
                        consumer,
                        filetype,
                        pla,
                        device,
                        bandwidth,
                        requests,
                        slowrequest,
                        users,
                        maliu,
                        userip,
                        hm,
                        path,
                        loadtime_str,
                        stream_id,
                        http_code,
                        sip,
                        ua,
                        qs,
                        termid,
                        tag,
                        pay,
                        body_send,
                        bytes_range ,
                        btag,
                        tss,
                        ostype,
                        utp,
                        sport,
                        uuid,
                        ])
    if business.startswith('10.') or business.startswith('live'):
        logtype = 'live'
        slowspeed_line = None
    else:
        logtype = 'cdn'
        slowspeed_line = sp.join([ptime, idc, isp, area, business, userip, slowrequest, '\n'])
    if business.startswith('102.'):
        bcloud_line = rstline_ + '\n'
    else:
        bcloud_line = None
    return (rstline_ , converted, ptime, userip, logtype, slowspeed_line, bcloud_line, bcloud_tag)


def gz_process(path,wpath):
    if os.path.exists(path):
        fw = open(wpath,'a')
        f = gzip.open(path,'rb')
        for line in f:
            rst = slowspeed(line)
            if rst:
                fw.write(rst[1]+'\n')
        f.close()
        fw.close()
if __name__ == "__main__":
    assert len(sys.argv) == 3

    gz_process(sys.argv[1],sys.argv[2])
