package com.letv.cdn.receiver.test;

import java.util.concurrent.CountDownLatch;

import com.letv.cdn.receiver.manger.DisruptorServer;
import com.letv.cdn.receiver.manger.MessageServer;
import com.letv.cdn.receiver.util.MessageUtil;

public class TestA extends AbstractPerfTestDisruptor{
    
    public static final long ITERATIONS = MemcacheProducer.ITERATIONS;// 迭代次数
    public static CountDownLatch latch = new CountDownLatch(1);
    
    // public static CountDownLatch latchDis;
    
    @Override
    protected long runDisruptorPass() throws Exception {
    
        long opsPerSecond = 0L;
        
        // latchDis = new CountDownLatch(1);
        try {
            DisruptorServer.startDisruptorServer();// 启动服务
            //MessageServer.startMessageServer();
            long start = System.currentTimeMillis();
            latch.await();
            // opsPerSecond = (System.currentTimeMillis() - start);
            opsPerSecond = (ITERATIONS * 22 * 1000L / (System.currentTimeMillis() - start));
        } finally {
            DisruptorServer.stopDisruptorServer();
           // MessageServer.stopMessageServer();
            // System.gc();
            System.out.println("Persistence size:" + MessageUtil.getPersistenceSize());
            //System.out.println("Message size:" + MessageUtil.getMessageSize());
            // Thread.sleep(2000);
        }
        
        return opsPerSecond;
    }
    
    @Override
    protected int getRequiredProcessorCount() {
    
        return 2;
        
    }
    
    public static void main(String[] args) throws Exception {
    
        TestA test = new TestA();
        test.testImplementations();
    }
}
