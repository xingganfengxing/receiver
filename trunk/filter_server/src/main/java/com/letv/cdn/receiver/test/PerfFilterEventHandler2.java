package com.letv.cdn.receiver.test;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.letv.cdn.receiver.model.RequestInfo;
import com.lmax.disruptor.EventHandler;

/**
 * 
 * 简单过滤业务测试
 */
public class PerfFilterEventHandler2 implements EventHandler<RequestInfo>{
    
    private static final Logger log = LoggerFactory.getLogger(PerfFilterEventHandler2.class);
    private long count = TestA.ITERATIONS - 1;
    private CountDownLatch latch = TestA.latch;
    private int id = 0;
    
    public PerfFilterEventHandler2() {

    }
    
    public PerfFilterEventHandler2(int id) {

        this.id = id;
    }
    
    public void onEvent(RequestInfo requestInfo, long sequence, boolean endOfBatch) throws Exception {

        // System.out.println("PerfFilterEventHandler2: " + sequence);
       // if ((sequence % 1) == id) {
            String body = requestInfo.getBody();
            String[] bodyLine = body.split("\n");
            System.out.println(bodyLine.length);
            if (count == sequence) {
                latch.countDown();
                log.info("======= countDown =======");
            }
            // for (String line : bodyLine) {
            // try {
            // StringBuffer ssb = new StringBuffer();
            // Pattern pattern = Pattern.compile("\"");
            // String[] temp = pattern.split(line);// 总体拆分
            //                    
            // int tmp = temp[0].indexOf("[");
            // int tmp2 = temp[0].indexOf(" ");
            // String ptime = temp[0].substring(tmp + 1, temp[0].length() - 2);
            // String userip = temp[0].substring(tmp2 + 1, tmp - 1);
            // String serverip = temp[0].substring(0, tmp2);
            //                    
            // String[] tmpArr = temp[2].split(" ");
            // String bandwith = tmpArr[2];
            // String httpcode = tmpArr[1];
            // String responsetime = tmpArr[3];
            //                    
            // tmp = temp[1].indexOf("?");
            // tmpArr = temp[1].substring(tmp + 1, temp[1].length() -
            // 9).split("&");
            // HashMap<String, String> map = new HashMap<String, String>();
            // String[] tmpArr2 = null;
            // for (String str : tmpArr) {
            // tmpArr2 = str.split("=");
            // map.put(tmpArr2[0], tmpArr2.length < 2 ? "" : tmpArr2[1]);
            // }
            // String maliu = map.containsKey("b") ? map.get("b") : "";
            // String platid = map.containsKey("platid") ? map.get("platid") :
            // "";
            // String splatid = map.containsKey("splatid") ? map.get("splatid")
            // : "";
            // String playid = map.containsKey("playid") ? map.get("playid") :
            // "";
            // String sign = map.containsKey("sign") ? map.get("sign") : "";
            //                    
            // ssb.append(ptime);
            // ssb.append("\t");
            // ssb.append(bandwith);
            // ssb.append("\t");
            // ssb.append(maliu);
            // ssb.append("\t");
            // ssb.append(httpcode);
            // ssb.append("\t");
            // ssb.append(userip);
            // ssb.append("\t");
            // ssb.append(serverip);
            // ssb.append("\t");
            // ssb.append(platid);
            // ssb.append("\t");
            // ssb.append(splatid);
            // ssb.append("\t");
            // ssb.append(sign);
            // ssb.append("\t");
            // ssb.append(playid);
            // ssb.append("\t");
            // ssb.append(responsetime);
            //                    
            // String join = ssb.toString();
            // // KafkaUtil.sendObjectKafka(join);
            // // log.info("send kafka data:" + join);
            // } catch (Exception ex) {
            //                    
            // } finally {
            // if (count == sequence) {
            // latch.countDown();
            // log.info("======= countDown =======");
            // }
            // }
            // }
       // }
        
    }
}
