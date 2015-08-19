package com.letv.cdn.receiver.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.letv.cdn.receiver.util.MemcacheUtil;

public class MemcacheProducer{
    
    private static final Logger LOG = LoggerFactory.getLogger(EmptyFilterEventHandler.class);
    
    public static final long ITERATIONS = 1000 * 10;
    public static final int INDEX_MASK = 1024 * 256 - 1;
    
    public static void main(String[] args) {
    
        //MemcachedClient memcachedClient = MemcacheUtil.createClient();
        // String body =
        // "119.167.147.9:80 27.206.97.27 [11/Sep/2014:16:16:08 +0800] \"GET /79/26/109/bcloud/100132/202013637-avc-856755-aac-62832-157800-18681325-9fd45d3e03fc5a5e7289bd2d107f532d-1406773538144.letv?crypt=47aa7f2e237&b=946&nlh=3072&nlt=45&bf=20&p2p=1&video_type=flv&termid=1&tss=no&geo=CN-15-191-2&tm=1410434400&key=db99cc62befb8ff1801d3e7b4f88bb1e&platid=2&splatid=203&proxy=2007470872,2007471042&mmsid=21992621&playid=0&vtype=17&cvid=220961692011&tag=flash&bcloud=S7&sign=bcloud_100132&pay=0&ostype=windows&hwtype=un&tn=0.48280957620590925&rateid=1300&gn=730&buss=6&qos=3&cips=27.206.97.27&rstart=9961472&rend=10223615 HTTP/1.1\" 200 262144 2.803 \"http://3w.beva.cn/erge/images/ErGePlayer.swf?v=141\" \"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36\" \"-\" 119.167.147.9 HIT 27909 bytes=9961472-10223615   262144 [11/Sep/2014:16:14:58 +0800] [11/Sep/2014:16:14:58 +0800] [11/Sep/2014:16:16:05 +0800] [11/Sep/2014:16:16:05 +0800] 70.393 70.393 2.802 2.802 ok 262144\n123.125.89.50:80 123.125.1.49 [11/Sep/2014:16:47:59 +0800] \"GET /80/14/44/letv-uts/14/ver_00_16-28289965-avc-1774429-aac-96000-3042640-715876803-93f19e55f9e557d009506fe18ea3de39-1409760352903_mp4/ver_00_16_50_268_3_4069636_216580136.ts?proxy=1875826473,2007471043&tag=&platid=5&tss=ios&splatid=501&playid=0&b=1882&bf=15&nlh=3072&path= HTTP/1.0\" 206 217088 0.018 \"-\" \"stagefright/1.2 (Linux;Android 4.0.3) Mozilla/5.0(iPad; U; CPU iPhone OS 3_2 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B314 Safari/531.21.10 QuickTime\" \"-\" 123.125.89.50 HIT 205881 bytes=564-217651 CACHE_HIT  217088 [11/Sep/2014:16:45:48 +0800] [11/Sep/2014:16:45:48 +0800] [11/Sep/2014:16:47:59 +0800] [11/Sep/2014:16:47:59 +0800] 131.232 131.232 0.000 0.000 ok 217088\n60.169.5.130:80 61.136.228.202 [22/Sep/2014:16:52:35 +0800] \"GET /m3u8/bjwsHD_1300/desc.m3u8?stream_id=bjwsHD_1300&ltm=1411393417&lkey=23c594c039e2ef1f6daf2f81df940dc2&platid=10&splatid=1011&tag=live&video_type=m3u8&useloc=0&mslice=5&path=60.169.5.187&buss=0&qos=2&cips=116.210.134.111&geo=CN-17-224-1&tmn=1411375417&pnl=1002,819,283&rson=500&ext=m3u8&sign=live_tv&termid=3&playid=1&play=0&ostype=android&hwtype=C1S&lstm=1XVzCN&lsbv=o&lssv=4BorPL&lsdg=ErT2oQoEGBPCy9bQzquAHI27cNGz&m3v=1&utp=687&utpid=8483aa2560c64fb6af7dc95c5784cc7e&appid=1000&utpnettype=1&rnd=52149932&timeshift=-65&tm=1411376170&key=4fafc3abb5798aaf28e8c205ba62d165&m3u8_args=dGFnPWxpdmUmcGxhdGlkPTEwJnNwbGF0aWQ9MTAxMSZwbGF5aWQ9MSZwYXRoPTYxLjEzNi4yMjguMTQwLDYwLjE2OS41LjEzMCw2MC4xNjkuNS4xODc= HTTP/1.0\" 200 3955 0.001 \"-\" \"owninneragent\" \"-\" 60.169.5.130 MISS 33447 -   3955 [22/Sep/2014:16:52:19 +0800] [22/Sep/2014:16:52:19 +0800] [22/Sep/2014:16:52:35 +0800] [22/Sep/2014:16:52:35 +0800] 15.860 15.860 0.000 0.000 ok 3955\n119.167.147.9:80 111.196.202.160 [11/Sep/2014:16:59:42 +0800] \"GET /49/51/25/letv-uts/14/ver_00_14-13080846-avc-549249-aac-32000-2740280-201969038-4fc15cb031e4b191a87ba3d940f4f033-1393280954882.mp4?crypt=92aa7f2e58900&b=589&nlh=3072&nlt=45&bf=8000&p2p=1&video_type=mp4&termid=2&tss=no&geo=CN-1-5-2&tm=1410502800&key=d7cdb4125e17e977900f269f8ef3d8ca&platid=3&splatid=302&proxy=2007470872,2007470985&mmsid=3233410&playid=2&vtype=13&cvid=1307888345487&sign=mb&dname=mobile&tag=mobile&pay=0&ostype=android&hwtype=iphone&gn=730&buss=47&qos=3&cips=111.196.202.160 HTTP/1.1\" 302 174 0.000 \"-\" \"Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1\" \"-\" 119.167.147.9 HIT 28763 bytes=123461136-134646024   174 [11/Sep/2014:16:59:07 +0800] [11/Sep/2014:16:59:07 +0800] [11/Sep/2014:16:59:42 +0800] [11/Sep/2014:16:59:42 +0800] 34.821 34.821 0.000 0.000 ok 174\n119.167.147.9:80 27.206.97.27 [11/Sep/2014:16:16:08 +0800] \"GET /79/26/109/bcloud/100132/202013637-avc-856755-aac-62832-157800-18681325-9fd45d3e03fc5a5e7289bd2d107f532d-1406773538144.letv?crypt=47aa7f2e237&b=946&nlh=3072&nlt=45&bf=20&p2p=1&video_type=flv&termid=1&tss=no&geo=CN-15-191-2&tm=1410434400&key=db99cc62befb8ff1801d3e7b4f88bb1e&platid=2&splatid=203&proxy=2007470872,2007471042&mmsid=21992621&playid=0&vtype=17&cvid=220961692011&tag=flash&bcloud=S7&sign=bcloud_100132&pay=0&ostype=windows&hwtype=un&tn=0.48280957620590925&rateid=1300&gn=730&buss=6&qos=3&cips=27.206.97.27&rstart=9961472&rend=10223615 HTTP/1.1\" 200 262144 2.803 \"http://3w.beva.cn/erge/images/ErGePlayer.swf?v=141\" \"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36\" \"-\" 119.167.147.9 HIT 27909 bytes=9961472-10223615   262144 [11/Sep/2014:16:14:58 +0800] [11/Sep/2014:16:14:58 +0800] [11/Sep/2014:16:16:05 +0800] [11/Sep/2014:16:16:05 +0800] 70.393 70.393 2.802 2.802 ok 262144\n123.125.89.50:80 123.125.1.49 [11/Sep/2014:16:47:59 +0800] \"GET /80/14/44/letv-uts/14/ver_00_16-28289965-avc-1774429-aac-96000-3042640-715876803-93f19e55f9e557d009506fe18ea3de39-1409760352903_mp4/ver_00_16_50_268_3_4069636_216580136.ts?proxy=1875826473,2007471043&tag=&platid=5&tss=ios&splatid=501&playid=0&b=1882&bf=15&nlh=3072&path= HTTP/1.0\" 206 217088 0.018 \"-\" \"stagefright/1.2 (Linux;Android 4.0.3) Mozilla/5.0(iPad; U; CPU iPhone OS 3_2 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B314 Safari/531.21.10 QuickTime\" \"-\" 123.125.89.50 HIT 205881 bytes=564-217651 CACHE_HIT  217088 [11/Sep/2014:16:45:48 +0800] [11/Sep/2014:16:45:48 +0800] [11/Sep/2014:16:47:59 +0800] [11/Sep/2014:16:47:59 +0800] 131.232 131.232 0.000 0.000 ok 217088\n60.169.5.130:80 61.136.228.202 [22/Sep/2014:16:52:35 +0800] \"GET /m3u8/bjwsHD_1300/desc.m3u8?stream_id=bjwsHD_1300&ltm=1411393417&lkey=23c594c039e2ef1f6daf2f81df940dc2&platid=10&splatid=1011&tag=live&video_type=m3u8&useloc=0&mslice=5&path=60.169.5.187&buss=0&qos=2&cips=116.210.134.111&geo=CN-17-224-1&tmn=1411375417&pnl=1002,819,283&rson=500&ext=m3u8&sign=live_tv&termid=3&playid=1&play=0&ostype=android&hwtype=C1S&lstm=1XVzCN&lsbv=o&lssv=4BorPL&lsdg=ErT2oQoEGBPCy9bQzquAHI27cNGz&m3v=1&utp=687&utpid=8483aa2560c64fb6af7dc95c5784cc7e&appid=1000&utpnettype=1&rnd=52149932&timeshift=-65&tm=1411376170&key=4fafc3abb5798aaf28e8c205ba62d165&m3u8_args=dGFnPWxpdmUmcGxhdGlkPTEwJnNwbGF0aWQ9MTAxMSZwbGF5aWQ9MSZwYXRoPTYxLjEzNi4yMjguMTQwLDYwLjE2OS41LjEzMCw2MC4xNjkuNS4xODc= HTTP/1.0\" 200 3955 0.001 \"-\" \"owninneragent\" \"-\" 60.169.5.130 MISS 33447 -   3955 [22/Sep/2014:16:52:19 +0800] [22/Sep/2014:16:52:19 +0800] [22/Sep/2014:16:52:35 +0800] [22/Sep/2014:16:52:35 +0800] 15.860 15.860 0.000 0.000 ok 3955\n119.167.147.9:80 111.196.202.160 [11/Sep/2014:16:59:42 +0800] \"GET /49/51/25/letv-uts/14/ver_00_14-13080846-avc-549249-aac-32000-2740280-201969038-4fc15cb031e4b191a87ba3d940f4f033-1393280954882.mp4?crypt=92aa7f2e58900&b=589&nlh=3072&nlt=45&bf=8000&p2p=1&video_type=mp4&termid=2&tss=no&geo=CN-1-5-2&tm=1410502800&key=d7cdb4125e17e977900f269f8ef3d8ca&platid=3&splatid=302&proxy=2007470872,2007470985&mmsid=3233410&playid=2&vtype=13&cvid=1307888345487&sign=mb&dname=mobile&tag=mobile&pay=0&ostype=android&hwtype=iphone&gn=730&buss=47&qos=3&cips=111.196.202.160 HTTP/1.1\" 302 174 0.000 \"-\" \"Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1\" \"-\" 119.167.147.9 HIT 28763 bytes=123461136-134646024   174 [11/Sep/2014:16:59:07 +0800] [11/Sep/2014:16:59:07 +0800] [11/Sep/2014:16:59:42 +0800] [11/Sep/2014:16:59:42 +0800] 34.821 34.821 0.000 0.000 ok 174";
        String body = "183.218.12.130:80 117.171.1.246 [08/Oct/2014:17:02:00 +0800] \"GET /m3u8/lb_movie_1300/desc.m3u8?stream_id=lb_movie_1300&ltm=1412775303&lkey=0166e21d18708c088f50ed10429ace86&platid=10&splatid=1001&tag=live&video_type=m3u8&useloc=0&mslice=5&path=223.82.251.73,112.25.62.139,112.25.27.108&ver=live_3&buss=27&qos=3&cips=117.171.1.246&geo=CN-14-179-4&tmn=1412757303&pnl=368,146,243&rson=0&ext=m3u8&sign=live_web&scheme=rtmp&termid=1&pay=0&uid=-&ostype=WindowsXP&hwtype=un&playid=1&abtimeshift=1412758940&rdm=1412758921031 HTTP/1.1\" 200 1275 0.019 \"http://player.hz.letv.com/live.swf\" \"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36\" \"-\" 183.218.12.130 MISS 13605 -   1275 [08/Oct/2014:16:59:18 +0800] [08/Oct/2014:16:59:18 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] 162.772 162.772 0.000 0.000 ok 1275\n183.218.12.130:80 120.203.204.68 [08/Oct/2014:17:02:00 +0800] \"GET /m3u8/lb_movie_1300/desc.m3u8?stream_id=lb_movie_1300&ltm=1412776916&lkey=4dc33999d41d71572ad67e749ec9a792&platid=10&splatid=1001&tag=live&video_type=m3u8&useloc=0&mslice=5&path=223.82.251.73,112.25.62.139,112.25.27.109&ver=live_3&buss=27&qos=3&cips=120.203.204.68&geo=CN-14-179-4&tmn=1412758916&pnl=368,146,243&rson=0&ext=m3u8&sign=live_web&scheme=rtmp&termid=1&pay=0&uid=-&ostype=WindowsXP&hwtype=un&playid=1&abtimeshift=1412758912&rdm=1412758920001 HTTP/1.1\" 200 4406 0.023 \"http://player.hz.letv.com/live.swf\" \"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1\" \"-\" 183.218.12.130 MISS 12948 -   4406 [08/Oct/2014:17:01:57 +0800] [08/Oct/2014:17:01:57 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] 3.601 3.601 0.000 0.000 ok 4406\n183.218.12.130:80 120.210.173.112 [08/Oct/2014:17:02:00 +0800] \"GET /m3u8/guangxi/2014100817/1412758895_5960_1268248.ts?tag=live&platid=10&splatid=1011&playid=1&path=183.218.12.138,112.25.12.136,112.25.27.105&rnd=80170303 HTTP/1.1\" 200 564 0.312 \"-\" \"-\" \"-\" 183.218.12.130 MISS 13544 -   1268248 [08/Oct/2014:16:57:16 +0800] [08/Oct/2014:16:57:16 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] 284.274 284.274 0.246 0.246 ok 564\n183.218.12.130:80 218.207.37.108 [08/Oct/2014:17:02:00 +0800] \"GET /m3u8/lb_error_1000/desc.m3u8?stream_id=lb_error_1000&ltm=1412773991&lkey=b8374549e4a6e96af3ab8e07e79da583&platid=10&splatid=1011&tag=live&video_type=m3u8&useloc=0&mslice=3&path=183.218.12.134,112.25.62.130,112.25.27.108&ver=live_3&buss=0&qos=3&cips=218.207.37.108&geo=CN-22-294-4&tmn=1412755991&pnl=368,146,243&rson=0&ext=m3u8&sign=live_tv&termid=3&playid=1&play=0&ostype=android&hwtype=C1S&lstm=1XbVVn&lsbv=o&lssv=iZCnN&lsdg=Emd7ZBnyczV1De2CLjzR181BK6wf&m3v=1&utp=690&utpid=e5c837b2b71a42deac112390615a9728&appid=1000&utpnettype=3&rnd=12373076&timeshift=-15&tm=1412759159&key=323a936fce27d6b4032e14cd9e471049 HTTP/1.1\" 200 6355 0.023 \"-\" \"AppleCoreMedia/1.0.0.9A405 (iPad; U; CPU OS 5_0_1 like Mac OS X; zh_cn)\" \"-\" 183.218.12.130 MISS 13544 -   6355 [08/Oct/2014:17:01:20 +0800] [08/Oct/2014:17:01:20 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] 40.006 40.006 0.000 0.000 ok 6355\n183.218.12.130:80 223.82.251.71 [08/Oct/2014:17:02:00 +0800] \"GET /m3u8/bjws/desc.m3u8?stream_id=bjws&ltm=1412775067&lkey=81bc74de977d91954f7b423c2336a1c1&platid=10&splatid=1001&tag=live&video_type=m3u8&useloc=0&mslice=5&path=112.25.62.134,112.25.27.105&buss=0&qos=2&cips=218.204.104.172&geo=CN-14-179-4&tmn=1412757067&pnl=368,146,243&rson=0&ext=m3u8&sign=live_web&scheme=rtmp&termid=1&pay=0&uid=-&ostype=WindowsXP&hwtype=un&playid=1&abtimeshift=1412758899&rdm=1412759185359&m3u8_args=dGFnPWxpdmUmcGxhdGlkPTEwJnNwbGF0aWQ9MTAwMSZwbGF5aWQ9MSZwYXRoPTE4My4yMTguMTIuMTMwLDExMi4yNS42Mi4xMzQsMTEyLjI1LjI3LjEwNQ== HTTP/1.0\" 200 1746 0.017 \"http://player.letvcdn.com/p/201410/08/08/08/newplayer/1/MLPLetvPlayer.swf\" \"owninneragent\" \"-\" 183.218.12.130 MISS 13561 -   1746 [08/Oct/2014:17:01:39 +0800] [08/Oct/2014:17:01:39 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] 21.047 21.047 0.000 0.000 ok 1746\n183.218.12.130:80 183.216.224.202 [08/Oct/2014:17:02:00 +0800] \"GET /m3u8/jiangxi/desc.m3u8?stream_id=jiangxi&ltm=1412775644&lkey=dcecc1a9b2871cc67916ade6a012b035&platid=10&splatid=1011&tag=live&video_type=m3u8&useloc=0&mslice=3&path=183.218.12.136,112.25.62.135,112.25.27.104&buss=0&qos=3&cips=183.216.224.202&geo=CN-14-179-4&tmn=1412757644&pnl=368,146,243&rson=0&ext=m3u8&sign=live_tv&termid=3&playid=1&play=0&ostype=android&hwtype=C1S&lstm=1XbmnU&lsbv=z&lssv=1Vcun4&lsdg=3LKlc1yRm3CLOedz9GcJ4qDrzreBwMd7X&m3v=1&utp=690&utpid=f0eacbc83c2642efa798656e6a64e1d8&appid=1000&utpnettype=3&rnd=61590248&timeshift=-45&tm=1412759150&key=940074bb7cb6054178c47a7b79c08118 HTTP/1.1\" 200 2485 0.017 \"-\" \"AppleCoreMedia/1.0.0.9A405 (iPad; U; CPU OS 5_0_1 like Mac OS X; zh_cn)\" \"-\" 183.218.12.130 MISS 13538 -   2485 [08/Oct/2014:17:01:07 +0800] [08/Oct/2014:17:01:07 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] 53.362 53.362 0.000 0.000 ok 2485\n183.218.12.130:80 183.208.98.146 [08/Oct/2014:17:02:00 +0800] \"GET /m3u8/jiangsuHD_1800/desc.m3u8?stream_id=jiangsuHD_1800&ltm=1412767200&lkey=4e3d9af9a5e9a4ac9725b99a1a7f6db2&platid=10&splatid=1012&tag=live&video_type=m3u8&useloc=0&mslice=5&path=183.218.12.138,112.25.12.136,112.25.27.123&buss=0&qos=3&cips=183.208.98.146&geo=CN-10-127-4&tmn=1412749200&pnl=368,131,243&rson=0&ext=m3u8&sign=live_tv&temporarykey=db7c39a0ee39ab2d4d2e781d5&termid=3&playid=1&play=0&ostype=android&hwtype=C1S&m3v=1&utp=683&utpid=a8fba26d5a6544b790a7e9fef26840d6&appid=0&rnd=98581240&timeshift=-60&tm=1412759156&key=feaee355f1ea72074d816a78b47aeea9 HTTP/1.1\" 200 3971 0.019 \"-\" \"AppleCoreMedia/1.0.0.9A405 (iPad; U; CPU OS 5_0_1 like Mac OS X; zh_cn)\" \"-\" 183.218.12.130 MISS 13605 -   3971 [08/Oct/2014:17:00:35 +0800] [08/Oct/2014:17:00:35 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] 85.623 85.623 0.000 0.000 ok 3971\n183.218.12.130:80 120.193.188.4 [08/Oct/2014:17:02:00 +0800] \"GET /m3u8/cctv6/2014100817/1412758890_8600_1273700.ts?stream_id=cctv6&ltm=1412776235&lkey=9e91fbcbbe6557754e29d7e00cbe9870&platid=10&splatid=1012&tag=live&video_type=m3u8&useloc=0&mslice=3&path=223.82.251.73,112.25.62.137,112.25.27.105&buss=0&qos=3&cips=120.193.188.4&geo=CN-5-59-4&tmn=1412758235&pnl=368,146,243&rson=0&ext=m3u8&sign=live_tv&temporarykey=db7c39a0ee39ab2d4d2e781d5&termid=3&playid=1&play=0&ostype=android&hwtype=C1S&m3v=1&utp=683&utpid=ce8ad17cb10146cca9de9468a194641f_tmp&appid=1000&rnd=32931048 HTTP/1.1\" 200 746840 0.080 \"-\" \"AppleCoreMedia/1.0.0.9A405 (iPad; U; CPU OS 5_0_1 like Mac OS X; zh_cn)\" \"-\" 183.218.12.130 MISS 13546 -   1273700 [08/Oct/2014:17:01:59 +0800] [08/Oct/2014:17:01:59 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] 1.582 1.582 0.075 0.074 ok 746840\n183.218.12.130:80 112.53.72.38 [08/Oct/2014:17:02:00 +0800] \"GET /m3u8/cctv3HD_1300/desc.m3u8?stream_id=cctv3HD_1300&ltm=1412769323&lkey=68448eef0966f3dc3479172cd2d9fbe8&platid=10&splatid=1011&tag=live&video_type=m3u8&useloc=0&mslice=3&path=183.218.12.138,112.25.12.136,112.25.27.123&buss=0&qos=3&cips=112.53.72.38&geo=CN-15-186-4&tmn=1412751323&pnl=368,131,243&rson=0&ext=m3u8&sign=live_tv&termid=3&playid=1&play=0&ostype=android&hwtype=C1S&lstm=1Xbl87&lsbv=p&lssv=2junLR&lsdg=KFPdcrYv9DZA9BzQmPUUHz7pdNjnjfyzS&m3v=1&utp=690&utpid=396c4e4725f242b98aca1f74c1fa9782&appid=1000&utpnettype=3&rnd=19647022&timeshift=-35&tm=1412759180&key=6cbde36ddb265fa5dbd3d64b2ef8a478 HTTP/1.1\" 200 2482 0.016 \"-\" \"AppleCoreMedia/1.0.0.11B511 (iPad; U; CPU OS 7_0_3 like Mac OS X; zh_cn)\" \"-\" 183.218.12.130 MISS 13544 -   2482 [08/Oct/2014:17:00:18 +0800] [08/Oct/2014:17:00:18 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] 101.975 101.975 0.000 0.000 ok 2482\n183.218.12.130:80 183.218.12.136 [08/Oct/2014:17:02:00 +0800] \"GET /m3u8/cctv6HD_1800/2014100817/1412758902_7080_1986972.ts?stream_id=cctv6HD_1800&ltm=1412776499&lkey=719e6e6738af8a5ed1285b4b247b44f4&platid=10&splatid=1012&tag=live&video_type=m3u8&useloc=0&mslice=5&path=112.25.62.133,112.25.27.123&buss=0&qos=3&cips=120.210.177.241&geo=CN-12-151-4&tmn=1412758499&pnl=368,146,243&rson=0&ext=m3u8&sign=live_tv&temporarykey=db7c39a0ee39ab2d4d2e781d5&termid=3&playid=1&play=0&ostype=android&hwtype=C1S&m3v=1&utp=683&utpid=e64a5667972f4e819a4142faff6d8624_tmp&appid=1000&rnd=65660310 HTTP/1.0\" 200 1986972 0.058 \"-\" \"owninneragent\" \"-\" 183.218.12.130 HIT 13544 -   1986972 [08/Oct/2014:16:59:21 +0800] [08/Oct/2014:16:59:21 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] 159.594 159.594 0.058 0.058 ok 1986972\n127.0.0.1:80 127.0.0.1 [08/Oct/2014:17:02:00 +0800] \"GET /m3u8/cctv3HD_1800/2014100816/1412758652_6360_1290432.ts?stream_id=cctv3HD_1800&ltm=1412776695&lkey=cf9a90575c2fb1e4fb2b28c878ff30fa&platid=10&splatid=1012&tag=live&video_type=m3u8&useloc=0&mslice=5&path=223.82.251.67,112.25.62.130,112.25.27.123&buss=0&qos=3&cips=111.12.16.96&geo=CN-20-276-4&tmn=1412758695&pnl=368,146,243&rson=0&ext=m3u8&sign=live_tv&temporarykey=db7c39a0ee39ab2d4d2e781d5&termid=3&playid=1&play=0&ostype=android&hwtype=C1S&m3v=1&utp=683&utpid=f0675e98a26d48f49028013fb14c874b_tmp&appid=1000&rnd=99423932 HTTP/1.0\" 200 12288 120.012 \"-\" \"AppleCoreMedia/1.0.0.9A405 (iPad; U; CPU OS 5_0_1 like Mac OS X; zh_cn1.7.3-R-20140619.1000)\" \"-\" owninneragent MISS 13546 -   1290432 [08/Oct/2014:17:00:00 +0800] [08/Oct/2014:17:00:00 +0800] [08/Oct/2014:17:00:00 +0800] [08/Oct/2014:17:00:00 +0800] 120.012 120.012 120.005 120.005 error 1290432\n183.218.12.130:80 117.167.246.200 [08/Oct/2014:17:02:00 +0800] \"GET /m3u8/cctv2/desc.m3u8?stream_id=cctv2&ltm=1412769632&lkey=b97f14b7351175c86ff5c861007e65cb&platid=10&splatid=1011&tag=live&video_type=m3u8&useloc=0&mslice=3&path=223.82.251.73,112.25.12.141,112.25.27.104&buss=0&qos=3&cips=117.167.246.200&geo=CN-14-176-4&tmn=1412751632&pnl=368,131,243&rson=0&ext=m3u8&sign=live_tv&termid=3&playid=1&play=0&ostype=android&hwtype=C1S&lstm=1XblD6&lsbv=1E&lssv=Yfsor&lsdg=G5u6DaOp434IUoruotW5uOIAROIOPL5vG&m3v=1&utp=690&utpid=cd02c74a040b4ab69f0b793a31eef9a4&appid=1000&utpnettype=3&rnd=15376728&timeshift=-40&tm=1412759172&key=51ed7c220793358da2a3ee362b938af6 HTTP/1.1\" 200 2481 0.020 \"-\" \"AppleCoreMedia/1.0.0.11B511 (iPad; U; CPU OS 7_0_3 like Mac OS X; zh_cn)\" \"-\" 183.218.12.130 MISS 13546 -   2481 [08/Oct/2014:17:01:35 +0800] [08/Oct/2014:17:01:35 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] 24.906 24.906 0.000 0.000 ok 2481\n183.218.12.130:80 223.82.251.65 [08/Oct/2014:17:02:00 +0800] \"GET /m3u8/cctv6HD_1800/desc.m3u8?stream_id=cctv6HD_1800&ltm=1412772958&lkey=3559c4452ed27add2b1f38e3e2f4dd9b&platid=10&splatid=1012&tag=live&video_type=m3u8&useloc=0&mslice=3&path=112.25.62.130,112.25.27.123&buss=0&qos=3&cips=110.209.95.161&geo=CN-19-246-3&tmn=1412754958&pnl=368,146,243&rson=0&ext=m3u8&sign=live_tv&temporarykey=db7c39a0ee39ab2d4d2e781d5&termid=3&playid=1&play=0&ostype=android&hwtype=C1S&m3v=1&utp=683&utpid=c5131af241704ff9b95e7a72492a17b6&appid=1000&rnd=58510730&timeshift=-35&tm=1412759198&key=92eecd9fa691fabd5292c496784ca064&m3u8_args=dGFnPWxpdmUmcGxhdGlkPTEwJnNwbGF0aWQ9MTAxMiZwbGF5aWQ9MSZwYXRoPTE4My4yMTguMTIuMTMwLDExMi4yNS42Mi4xMzAsMTEyLjI1LjI3LjEyMw== HTTP/1.0\" 200 2496 0.017 \"-\" \"owninneragent\" \"-\" 183.218.12.130 MISS 13561 -   2496 [08/Oct/2014:17:01:59 +0800] [08/Oct/2014:17:01:59 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] 1.322 1.322 0.001 0.000 ok 2496\n183.218.12.130:80 117.165.205.88 [08/Oct/2014:17:02:00 +0800] \"GET /m3u8/anhui/2014100817/1412758838_5960_888864.ts?stream_id=anhui&ltm=1412774259&lkey=6f3f9862c7dfcbbccf571676c9f93ac7&platid=10&splatid=1012&tag=live&video_type=m3u8&useloc=0&mslice=5&path=223.82.251.67,112.25.62.134,112.25.27.104&buss=0&qos=3&cips=117.165.205.88&geo=CN-14-176-4&tmn=1412756259&pnl=368,146,243&rson=0&ext=m3u8&sign=live_tv&temporarykey=db7c39a0ee39ab2d4d2e781d5&termid=3&playid=1&play=0&ostype=android&hwtype=C1S&m3v=1&utp=683&utpid=4c93d11b85374968a7753c56560a5982&appid=1000&rnd=61039179 HTTP/1.1\" 206 555540 0.000 \"-\" \"AppleCoreMedia/1.0.0.9A405 (iPad; U; CPU OS 5_0_1 like Mac OS X; zh_cn)\" \"-\" 183.218.12.130 HIT 13538 bytes=333324-888863   555540 [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] 0.000 0.000 0.000 0.000 ok 555540\n183.218.12.130:80 123.65.206.206 [08/Oct/2014:17:02:00 +0800] \"GET /m3u8/cctvshaoer/desc.m3u8?stream_id=cctvshaoer&ltm=1412775466&lkey=de77bae281261ad79d56f057e8841e8d&platid=10&splatid=1011&tag=live&video_type=m3u8&useloc=0&mslice=5&path=223.82.251.71,112.25.62.138,112.25.27.104&buss=0&qos=3&cips=123.65.206.206&geo=CN-19-246-3&tmn=1412757466&pnl=368,146,243&rson=0&ext=m3u8&sign=live_tv&termid=3&playid=1&play=0&ostype=android&hwtype=C1S&lstm=1Xbmkj&lsbv=1B&lssv=17on3c&lsdg=ICdJBZeawslCpc0JTCGaTj&m3v=1&utp=689&utpid=6dc6b3738e9841d6996d561de581ddf3&appid=1000&utpnettype=3&rnd=6210652&timeshift=-55&tm=1412759122&key=de8d5e7da5c7e07cab48641bc6c40ee2 HTTP/1.1\" 200 3956 0.016 \"-\" \"Lavf53.32.100\" \"-\" 183.218.12.130 MISS 13531 -   3956 [08/Oct/2014:16:59:08 +0800] [08/Oct/2014:16:59:08 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] 171.961 171.961 0.000 0.000 ok 3956\n183.218.12.130:80 223.82.251.69 [08/Oct/2014:17:02:00 +0800] \"GET /m3u8/cctv6HD_1800/desc.m3u8?stream_id=cctv6HD_1800&ltm=1412771932&lkey=3653ccba74dacd2c0c0d2241406574a6&platid=10&splatid=1012&tag=live&video_type=m3u8&useloc=0&mslice=5&path=112.25.62.138,112.25.27.123&buss=0&qos=3&cips=117.150.198.187&geo=CN-17-220-4&tmn=1412753932&pnl=368,146,243&rson=0&ext=m3u8&sign=live_tv&temporarykey=db7c39a0ee39ab2d4d2e781d5&termid=3&playid=1&play=0&ostype=android&hwtype=C1S&utp=667&m3v=1&timeshift=-50&tm=1412759218&key=f5fbe87d1cca2e1e6ec43b3a65a9fc27&utpid=69131562382d402c95be3db18eaae53f_tmp&appid=0&m3u8_args=dGFnPWxpdmUmcGxhdGlkPTEwJnNwbGF0aWQ9MTAxMiZwbGF5aWQ9MSZwYXRoPTE4My4yMTguMTIuMTMwLDExMi4yNS42Mi4xMzgsMTEyLjI1LjI3LjEyMw== HTTP/1.0\" 200 3968 0.018 \"-\" \"owninneragent\" \"-\" 183.218.12.130 MISS 13561 -   3968 [08/Oct/2014:17:00:23 +0800] [08/Oct/2014:17:00:23 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] 97.047 97.047 0.000 0.000 ok 3968\n183.218.12.130:80 117.140.62.182 [08/Oct/2014:17:02:00 +0800] \"GET /m3u8/cctv5_800/desc.m3u8?stream_id=cctv5_800&ltm=1412773597&lkey=602e0442ea853ea10b08b32c392db9a1&platid=10&splatid=1014&tag=live&video_type=m3u8&useloc=0&mslice=5&path=223.82.251.65,112.25.62.139,112.25.27.104&buss=0&qos=3&cips=117.140.62.76&geo=CN-20-276-4&tmn=1412755597&pnl=368,146,243&rson=0&ext=m3u8&sign=live_tv&termid=3&playid=1&play=0&ostype=android&hwtype=C1S&lstm=1XbmEb&lsbv=x&lssv=2UiBfo&lsdg=Fvqz9ugcqyB6ywOsY8qLEfC5ufm&m3v=1&utp=683&utpid=60814c85339435aaa4c987ef11094383&appid=10&rnd=15983137 HTTP/1.1\" 200 2465 0.019 \"-\" \"AppleCoreMedia/1.0.0.9A405 (iPad; U; CPU OS 5_0_1 like Mac OS X; zh_cn)\" \"-\" 183.218.12.130 MISS 13561 -   2465 [08/Oct/2014:17:01:16 +0800] [08/Oct/2014:17:01:16 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] 43.976 43.976 0.000 0.000 ok 2465\n183.218.12.130:80 117.166.79.88 [08/Oct/2014:17:02:00 +0800] \"GET /m3u8/lb_movie_1300/desc.m3u8?stream_id=lb_movie_1300&ltm=1412774773&lkey=ee226c44d63bed5296c7550ba71509fe&platid=10&splatid=1001&tag=live&video_type=m3u8&useloc=0&mslice=5&path=223.82.251.73,112.25.62.139,112.25.27.108&ver=live_3&buss=27&qos=3&cips=117.166.79.88&geo=CN-14-176-4&tmn=1412756773&pnl=368,146,243&rson=0&ext=m3u8&sign=live_web&scheme=rtmp&termid=1&pay=0&uid=-&ostype=Windows7&hwtype=un&playid=1&abtimeshift=1412758940&rdm=1412759016335 HTTP/1.1\" 200 1274 0.018 \"http://player.hz.letv.com/live.swf\" \"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36\" \"-\" 183.218.12.130 MISS 13605 -   1274 [08/Oct/2014:16:58:30 +0800] [08/Oct/2014:16:58:30 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] 210.221 210.221 0.000 0.000 ok 1274\n183.218.12.130:80 112.7.75.216 [08/Oct/2014:17:02:00 +0800] \"GET /m3u8/cctv3HD_1300/2014100816/1412758684_6960_960680.ts?stream_id=cctv3HD_1300&ltm=1412776911&lkey=9b7537c55e0c4254d808fef34bc7653c&platid=10&splatid=1012&tag=live&video_type=m3u8&useloc=0&mslice=5&path=183.218.12.138,112.25.62.153,112.25.27.122&buss=0&qos=4&cips=112.7.75.216&geo=CN-15-187-4&tmn=1412758911&pnl=368,146,243&rson=0&ext=m3u8&sign=live_tv&temporarykey=db7c39a0ee39ab2d4d2e781d5&termid=3&playid=1&play=0&ostype=android&hwtype=C1S&m3v=1&utp=670&utpid=ce8ad17cb10146cca9de9468a194641f_tmp&appid=1 HTTP/1.0\" 206 435284 0.020 \"-\" \"AppleCoreMedia/1.0.0.9A405 (iPad; U; CPU OS 5_0_1 like Mac OS X; zh_cn)\" \"-\" 183.218.12.130 MISS 13538 bytes=525396-960679   435284 [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] 0.020 0.020 0.014 0.014 ok 435284\n183.218.12.130:80 223.82.251.67 [08/Oct/2014:17:02:00 +0800] \"GET /m3u8/bjws/desc.m3u8?stream_id=bjws&ltm=1412775153&lkey=3e8b4bafc93b196e5eae37b164a1d96a&platid=10&splatid=1001&tag=live&video_type=m3u8&useloc=0&mslice=5&path=112.25.62.130,112.25.27.105&buss=0&qos=3&cips=123.88.140.110&geo=CN-19-246-3&tmn=1412757153&pnl=368,146,243&rson=0&ext=m3u8&sign=live_web&scheme=rtmp&termid=1&pay=0&uid=-&ostype=WindowsXP&hwtype=un&playid=1&abtimeshift=1412739905&rdm=1412758982176&m3u8_args=dGFnPWxpdmUmcGxhdGlkPTEwJnNwbGF0aWQ9MTAwMSZwbGF5aWQ9MSZwYXRoPTE4My4yMTguMTIuMTMwLDExMi4yNS42Mi4xMzAsMTEyLjI1LjI3LjEwNQ== HTTP/1.0\" 200 3953 0.017 \"http://player.hz.letv.com/live.swf\" \"owninneragent\" \"-\" 183.218.12.130 MISS 13561 -   3953 [08/Oct/2014:17:01:08 +0800] [08/Oct/2014:17:01:08 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] 52.511 52.511 0.000 0.000 ok 3953\n183.218.12.130:80 223.73.104.80 [08/Oct/2014:17:02:00 +0800] \"GET /m3u8/cctv1HD_1300/2014100816/1412758686_6040_966132.ts?stream_id=cctv1HD_1300&ltm=1412776904&lkey=052be976f19f3a5aa997011bc19f51c1&platid=10&splatid=1012&tag=live&video_type=m3u8&useloc=0&mslice=3&path=183.218.12.134,112.25.62.132,112.25.27.122&buss=0&qos=3&cips=223.73.104.80&geo=CN-19-0-4&tmn=1412758904&pnl=368,146,243&rson=0&ext=m3u8&sign=live_tv&temporarykey=db7c39a0ee39ab2d4d2e781d5&termid=3&playid=1&play=0&ostype=android&hwtype=C1S&m3v=1&utp=683&utpid=ce8ad17cb10146cca9de9468a194641f_tmp&appid=1&rnd=22581029 HTTP/1.1\" 206 437728 0.001 \"-\" \"AppleCoreMedia/1.0.0.9A405 (iPad; U; CPU OS 5_0_1 like Mac OS X; zh_cn)\" \"-\" 183.218.12.130 HIT 13561 bytes=528404-966131   437728 [08/Oct/2014:17:01:46 +0800] [08/Oct/2014:17:01:46 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] 14.498 14.498 0.000 0.000 ok 437728\n183.218.12.130:80 183.218.12.138 [08/Oct/2014:17:02:00 +0800] \"GET /m3u8/shanxi1/desc.m3u8?stream_id=shanxi1&ltm=1412774653&lkey=09e2425027adc04bd5a2a2d31f3475ca&platid=10&splatid=1011&tag=live&video_type=m3u8&useloc=0&mslice=3&path=112.25.62.130,112.25.27.104&buss=0&qos=3&cips=117.162.67.48&geo=CN-14-176-4&tmn=1412756653&pnl=368,146,243&rson=0&ext=m3u8&sign=live_tv&termid=3&playid=1&play=0&ostype=android&hwtype=C1S&lstm=1XbmXd&lsbv=10&lssv=lQ8u4&lsdg=8eb99CPeHSKLe0n313OEoS&m3v=1&utp=690&utpid=ff985414d9c5431a8b375e5468d2f04e&appid=1000&utpnettype=1&rnd=12820390&timeshift=-40&tm=1412759069&key=ee6006b25140d9b6bce88c6abc86d7eb&m3u8_args=dGFnPWxpdmUmcGxhdGlkPTEwJnNwbGF0aWQ9MTAxMSZwbGF5aWQ9MSZwYXRoPTE4My4yMTguMTIuMTMwLDExMi4yNS42Mi4xMzAsMTEyLjI1LjI3LjEwNA== HTTP/1.0\" 200 2478 0.018 \"-\" \"owninneragent\" \"-\" 183.218.12.130 MISS 13546 -   2478 [08/Oct/2014:17:01:05 +0800] [08/Oct/2014:17:01:05 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] 55.326 55.326 0.000 0.000 ok 2478\n183.218.12.130:80 117.163.136.224 [08/Oct/2014:17:02:00 +0800] \"GET /m3u8/lb_movie_1300/desc.m3u8?stream_id=lb_movie_1300&ltm=1412775447&lkey=d5b3a4a9015a5ccf521990f41b0d1354&platid=10&splatid=1001&tag=live&video_type=m3u8&useloc=0&mslice=5&path=223.82.251.73,112.25.62.138,112.25.27.109&ver=live_3&buss=27&qos=3&cips=117.163.136.224&geo=CN-14-176-4&tmn=1412757447&pnl=368,146,243&rson=0&ext=m3u8&sign=live_web&scheme=rtmp&termid=1&pay=0&uid=-&ostype=WindowsXP&hwtype=un&playid=1&abtimeshift=1412758940&rdm=1412758921171 HTTP/1.1\" 200 1279 0.021 \"http://www.letv.com/\" \"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.137 Safari/537.36 LBBROWSER\" \"-\" 183.218.12.130 MISS 13605 -   1279 [08/Oct/2014:16:58:26 +0800] [08/Oct/2014:16:58:26 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] 214.541 214.541 0.000 0.000 ok 1279\n183.218.12.130:80 120.210.173.252 [08/Oct/2014:17:02:00 +0800] \"GET /m3u8/sichuan/2014100817/1412758857_5960_1571116.ts?stream_id=sichuan&ltm=1412776127&lkey=2841da773e7bae8a586eaef1a49fa68e&platid=10&splatid=1011&tag=live&video_type=m3u8&useloc=0&mslice=3&path=183.218.12.132,112.25.62.131,112.25.27.104&buss=0&qos=3&cips=120.210.173.252&geo=CN-12-151-4&tmn=1412758127&pnl=368,146,243&rson=0&ext=m3u8&sign=live_tv&termid=3&playid=1&play=0&ostype=android&hwtype=C1S&lstm=1XbmvP&lsbv=1R&lssv=2SqR4k&lsdg=36pRx3PWleIJ4yTBmRNfTV4cDCee&m3v=1&utp=690&utpid=c184b36fdd9e4ef8a31e56941537a94c&appid=1000&utpnettype=3&rnd=269261 HTTP/1.1\" 206 527376 0.001 \"-\" \"AppleCoreMedia/1.0.0.9A405 (iPad; U; CPU OS 5_0_1 like Mac OS X; zh_cn)\" \"-\" 183.218.12.130 HIT 13531 bytes=528276-1055651   527376 [08/Oct/2014:17:01:59 +0800] [08/Oct/2014:17:01:59 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] 1.014 1.014 0.000 0.000 ok 527376\n183.218.12.130:80 223.73.100.84 [08/Oct/2014:17:02:00 +0800] \"GET /m3u8/lb_jilu_1000/desc.m3u8?stream_id=lb_jilu_1000&ltm=1412775478&lkey=bd02009514494203f6fc25b356c78ca8&platid=10&splatid=1009&tag=live&video_type=m3u8&useloc=0&mslice=3&path=223.82.251.71,112.25.62.168,112.25.27.108&ver=live_3&buss=5&qos=5&cips=223.73.100.84&geo=CN-19-0-4&tmn=1412757478&pnl=368,146,243&rson=0&ext=m3u8&sign=live_tv&pay=0&ostype=android&hwtype=S240F&termid=4&utp=645&m3v=1&timeshift=-90&tm=1412759132&key=97433e6ebdb1912d8517f8864c0a2226 HTTP/1.1\" 200 7339 0.021 \"-\" \"stagefright/1.2 (Linux;Android 4.0.3) Mozilla/5.0(iPad; U; CPU iPhone OS 3_2 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B314 Safari/531.21.10 QuickTime\" \"-\" 183.218.12.130 MISS 12928 -   7339 [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] [08/Oct/2014:17:02:00 +0800] 0.021 0.021 0.000 0.000 ok 7339";
        System.out.println(body.getBytes().length);
        System.out.println(body.split("\n").length);
        for (int i = 0; i < ITERATIONS; i++) {
            try {
                MemcacheUtil.set(String.valueOf(i & INDEX_MASK), 0, body.toString());
                Thread.sleep(1);
            } catch (Exception e) {
                LOG.error(e.getMessage());
            }
        }
        //MemcachedClient.shutdown();
        System.out.println("====memcacheProducer end====");
    }
}