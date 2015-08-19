package baseTest;

import java.util.HashMap;
import java.util.regex.Pattern;

public class TestD{
    public static void main(String[] args) {
    
        String body = "119.167.147.9:80 27.206.97.27 [04/Nov/2014:12:04:00 +0800] \"GET /79/26/109/bcloud/100132/202013637-avc-856755-aac-62832-157800-18681325-9fd45d3e03fc5a5e7289bd2d107f532d-1406773538144.letv?crypt=47aa7f2e237&b=946&nlh=3072&nlt=45&bf=20&p2p=1&video_type=flv&termid=1&tss=no&geo=CN-7-191-10&tm=1410434400&key=db99cc62befb8ff1801d3e7b4f88bb1e&platid=2&splatid=203&proxy=2007470872,2007471042&mmsid=21992621&playid=0&vtype=17&cvid=220961692011&tag=flash&bcloud=S7&sign=bcloud_bb.1&pay=0&ostype=windows&hwtype=un&tn=0.48280957620590925&rateid=1300&gn=730&buss=6&qos=3&cips=27.206.97.27&rstart=9961472&rend=10223615 HTTP/1.1\" 200 10 2.803 \"http://3w.beva.cn/erge/images/ErGePlayer.swf?v=141\" \"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36\" \"-\" 119.167.147.9 HIT 27909 bytes=9961472-10223615   262144 [11/Sep/2014:16:14:58 +0800] [11/Sep/2014:16:14:58 +0800] [11/Sep/2014:16:16:05 +0800] [11/Sep/2014:16:16:05 +0800] 70.393 70.393 2.802 2.802 ok 262144";
        String[] bodyLine = body.split("\n");
        for (String line : bodyLine) {
            try {
                StringBuffer ssb = new StringBuffer();
                Pattern pattern = Pattern.compile("\"");
                String[] temp = pattern.split(line);// 总体拆分
                
                int tmp = temp[0].indexOf("[");
                int tmp2 = temp[0].indexOf(" ");
                String ptime = temp[0].substring(tmp + 1, temp[0].length() - 2);
                String userip = temp[0].substring(tmp2 + 1, tmp - 1);
                // String serverip = temp[0].substring(0, tmp2);
                String serverip = temp[0].substring(0, temp[0].indexOf(":"));
                String[] tmpArr = temp[2].split(" ");
                String bandwith = tmpArr[2];
                String httpcode = tmpArr[1];
                String responsetime = tmpArr[3];
                
                tmp = temp[1].indexOf("?");
                tmpArr = temp[1].substring(tmp + 1, temp[1].length() - 9).split("&");
                HashMap<String, String> map = new HashMap<String, String>();
                String[] tmpArr2 = null;
                for (String str : tmpArr) {
                    tmpArr2 = str.split("=");
                    map.put(tmpArr2[0], tmpArr2.length < 2 ? "" : tmpArr2[1]);
                }
                String maliu = map.containsKey("b") ? map.get("b") : "";
                String platid = map.containsKey("platid") ? map.get("platid") : "";
                String splatid = map.containsKey("splatid") ? map.get("splatid") : "";
                String playid = map.containsKey("playid") ? map.get("playid") : "";
                String geo = map.containsKey("geo") ? map.get("geo") : "";
                if ("2".equals(platid) && "0".equals(playid)) {// 持久化云视频的点播
                    // MessageUtil.addPersistence(line);
                   // KafkaProcessManager.getPersistenceProcessById(ordinal + 1).sendObjectKafka(line);
                }
                String customerName = "";
                if (!"10".equals(platid)) {
                    String[] cntmp = temp[1].substring(0, 40).split("/");
                    if ("2".equals(platid)) {
                        customerName = cntmp[4] + "_" + cntmp[5];
                    } else {
                        customerName = cntmp[4];
                    }
                }
                String sign = map.containsKey("sign") ? map.get("sign") : "";
                
                ssb.append(ptime);
                ssb.append("\t");
                ssb.append(bandwith);
                ssb.append("\t");
                ssb.append(maliu);
                ssb.append("\t");
                ssb.append(httpcode);
                ssb.append("\t");
                ssb.append(userip);
                ssb.append("\t");
                ssb.append(serverip);
                ssb.append("\t");
                ssb.append(platid);
                ssb.append("\t");
                ssb.append(splatid);
                ssb.append("\t");
                ssb.append(sign);
                ssb.append("\t");
                ssb.append(playid);
                ssb.append("\t");
                ssb.append(responsetime);
                
                ssb.append("\t");
                ssb.append(customerName);
                ssb.append("\t");
                ssb.append(geo);
                
                String join = ssb.toString();
                // MessageUtil.addMessage(join);
               // KafkaProcessManager.getMessageProcessById(ordinal).sendObjectKafka(join);
                // log.debug("send kafka data:" + join);
                System.out.println(join);
            } catch (Exception ex) {
               System.err.println(ex);
            }
        }
    }
}
