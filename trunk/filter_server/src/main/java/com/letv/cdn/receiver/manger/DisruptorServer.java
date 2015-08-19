package com.letv.cdn.receiver.manger;

import java.lang.reflect.Constructor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.letv.cdn.common.Env;
import com.letv.cdn.common.StringUtil;
import com.letv.cdn.receiver.exception.FilterExceptionHandler;
import com.letv.cdn.receiver.model.RequestInfo;
import com.letv.cdn.receiver.service.core.RequestInfoEventFactory;
import com.letv.cdn.receiver.util.Constants;
import com.letv.cdn.receiver.util.SequenceUtil;
import com.letv.cdn.receiver.util.XMemcacheUtil;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * 业务处理服务控制
 * 
 * @author kk
 */
public class DisruptorServer{
    
    private static final Logger log = LoggerFactory.getLogger(DisruptorServer.class);
    
    private static DisruptorServer server;
    private static int NUM_EVENT_PROCESSORS;// 业务处理线程池数量
    public static int NUM_EVENT_HANDLERS;// 具体业务处理线程数
    private static int NUM_PRODUCER_PROCESSORS;// 生产者(获取缓存信息)数量
    private static int BUFFER_SIZE;// 环形队列大小
    private volatile boolean isProducerRun = false;
    private static Disruptor<RequestInfo> disruptor;
    private static final SequenceUtil sequenceUtil = new SequenceUtil(Constants.LOCAL_SEQUENCE_CURR);// Constants.LOCAL_SEQUENCE_CURR
    private ExecutorService eventHandlerExecutor;
    private ExecutorService producerExecutor;
    private final ExecutorService daemonExecutor = Executors.newFixedThreadPool(2);// 更新序号状态线程
    private RingBuffer<RequestInfo> ringBuffer;
    private EventFactory<RequestInfo> factory;
    private WaitStrategy wait;
    private ProducerType producerType;
    private EventHandler[] eventHandlerArr;
    private RequestInfoProducer[] requestInfoProducerArr;
    private ExceptionHandler exceptionHandler = new FilterExceptionHandler();
    // private MemcachedClient memcachedClient = MemcacheUtil.createClient();
    private static VolatileLong[] requestInfoProducerSequence;// 存放生产者各线程获取当前序号
    
    static {
        NUM_EVENT_HANDLERS = StringUtil.isEmpty(Env.get("numEventHandlers")) ? Runtime.getRuntime()
                .availableProcessors() : Integer.parseInt(Env.get("numEventHandlers"));
        NUM_EVENT_PROCESSORS = NUM_EVENT_HANDLERS;
        NUM_PRODUCER_PROCESSORS = StringUtil.isEmpty(Env.get("numProducerProcessors")) ? Runtime.getRuntime()
                .availableProcessors() : Integer.parseInt(Env.get("numProducerProcessors"));
        BUFFER_SIZE = StringUtil.isEmpty(Env.get("bufferSize")) ? 1024 * 8 : Integer.parseInt(Env.get("bufferSize"));
        log.info("numEventHandlers:" + NUM_EVENT_HANDLERS + ",numEventProcessors:" + NUM_EVENT_PROCESSORS
                + ",numProducerProcessors:" + NUM_PRODUCER_PROCESSORS + ",bufferSize:" + BUFFER_SIZE);
    }
    
    private DisruptorServer() {
    
    }
    
    public static synchronized void startDisruptorServer() throws Exception {
    
        if (server != null)
            return;
        server = new DisruptorServer();
        try {
            server.init();
            server.start();
        } catch (Exception ex) {
            log.error("disruptorServer start error: " + ex.getMessage());
            throw ex;
        }
        log.info("============ DisruptorServer Is Running. ============");
    }
    
    public static synchronized void stopDisruptorServer() throws Exception {
    
        if (server == null)
            return;
        try {
            server.stop();
            server = null;
        } catch (Exception ex) {
            log.error("disruptorServer stop error: " + ex.getMessage());
            throw ex;
        }
        log.info("============ DisruptorServer Has stopped. ============");
    }
    
    private void init() throws Exception {
    
        eventHandlerExecutor = Executors.newFixedThreadPool(NUM_EVENT_PROCESSORS);
        producerExecutor = Executors.newFixedThreadPool(NUM_PRODUCER_PROCESSORS, new RequestInfoProducerFactory());
        factory = new RequestInfoEventFactory();
        String waitStrategyStr = null == Env.get("waitStrategy") ? "block" : Env.get("waitStrategy");
        log.info("waitStrategy:" + waitStrategyStr);
        if ("block".equals(waitStrategyStr)) {
            wait = new BlockingWaitStrategy();
        } else if ("sleep".equals(waitStrategyStr)) {
            wait = new SleepingWaitStrategy();
        } else if ("yield".equals(waitStrategyStr)) {
            wait = new YieldingWaitStrategy();
        } else if ("busy".equals(waitStrategyStr)) {
            wait = new BusySpinWaitStrategy();
        } else {
            wait = new BlockingWaitStrategy();
        }
        String handlerClass = Env.get("handlerClass");
        if (null == handlerClass) {
            throw new Exception("handlerClass is null!");
        }
        log.info("handlerClass:" + handlerClass);
        eventHandlerArr = new EventHandler[NUM_EVENT_HANDLERS];
        for (int i = 0; i < NUM_EVENT_HANDLERS; i++) {
            Class clazz = Class.forName(handlerClass);
            Constructor cons = clazz.getConstructor(Integer.class);
            eventHandlerArr[i] = (EventHandler) cons.newInstance(Integer.valueOf(i));
        }
        // eventHandlerArr = new EventHandler[2];
        // eventHandlerArr[0] = new PerfFilterEventHandler(0);
        // eventHandlerArr[1] = new PerfFilterEventHandler(1);
        requestInfoProducerArr = new RequestInfoProducer[NUM_PRODUCER_PROCESSORS];
        requestInfoProducerSequence = new VolatileLong[NUM_PRODUCER_PROCESSORS];
        for (int i = 0; i < NUM_PRODUCER_PROCESSORS; i++) {
            requestInfoProducerArr[i] = new RequestInfoProducer();
            requestInfoProducerSequence[i] = new VolatileLong();
            requestInfoProducerSequence[i].value = sequenceUtil.getCurrent();
        }
        producerType = NUM_PRODUCER_PROCESSORS <= 1 ? ProducerType.SINGLE : ProducerType.MULTI;
        log.info("producerType:" + producerType);
        disruptor = new Disruptor<RequestInfo>(factory, BUFFER_SIZE, eventHandlerExecutor, producerType, wait);
        ringBuffer = disruptor.getRingBuffer();
    }
    
    private void start() throws Exception {
    
        disruptor.handleExceptionsWith(exceptionHandler);// 加入自定义异常,控制业务执行流
        disruptor.handleEventsWith(this.eventHandlerArr);
        disruptor.start();
        isProducerRun = true;
        daemonExecutor.execute(new updateReceiverSequenceThread());
        daemonExecutor.execute(new updateProducerSequenceThread());
        for (int i = 0; i < requestInfoProducerArr.length; i++) {
            producerExecutor.execute(requestInfoProducerArr[i]);
        }
    }
    
    private void stop() throws Exception {
    
        if (null != disruptor) {
            isProducerRun = false;
            // disruptor.shutdown();
            disruptor.halt();
            producerExecutor.shutdown();
            eventHandlerExecutor.shutdown();
            daemonExecutor.shutdown();
            // memcachedClient.shutdown();
        }
    }
    
    class RequestInfoProducer implements Runnable{
        //MemcachedClient memcachedClient = MemcacheUtil.createClient();
        private final int retries = 200;
        
        public RequestInfoProducer() {
        
        }
        
        public void run() {
        
            String next = null;
            long intnext = 0L;
            // Object receiver_curr = null;
            Object obj = null;
           // try {
                while (isProducerRun) {
                    try {
                        int counter = retries;
                        intnext = sequenceUtil.next();
                        // receiver_curr =
                        // memcachedClient.get(Constants.RECEIVER_LOCAL_SEQUENCER_KEY_CURR);//
                        // 获取receiver当前更新到序号
                        // while (null == receiver_curr || intnext >
                        // Long.parseLong(receiver_curr.toString())) {//
                        // 消费者比生产者快要等待
                        // // log.warn("===disruptor wait===");
                        // counter = waitMethod(counter);
                        // receiver_curr =
                        // memcachedClient.get(Constants.RECEIVER_LOCAL_SEQUENCER_KEY_CURR);
                        // }
                        int l = (int) intnext & Constants.INDEX_MASK;
                        if ((l % Constants.CLUSTER_NUM) == Constants.FILTER_SERVER_ID) {// 集群处理
                            while (intnext > Constants.receiver_curr) {// 消费者比生产者快要等待
                                counter = waitMethod(counter);
                            }
                            next = String.valueOf(l);
                            obj = XMemcacheUtil.getFromCache(next);// 缓存中取出
                            if (null == obj) {
                                continue;
                            }
                            publishRingBuffer(obj);
                            requestInfoProducerSequence[Integer.parseInt(Thread.currentThread().getName())].value = intnext;// 更新序号
                        }
                    } catch (Throwable t) {
                        log.error("requestInfoProducer err,thread-" + Thread.currentThread().getName() + ":", t);
                    }
                }
//            } finally {
//                try {
//                    memcachedClient.shutdown();
//                } catch (Exception e) {
//                    log.error(e.getMessage());
//                }
//            }
        }
        
        private void publishRingBuffer(Object obj) {
        
            // 获取下一个序号
            long sequence = ringBuffer.next();
            RequestInfo model = null;
            // 获取序号所对应的实体
            model = ringBuffer.get(sequence);
            model.setBody(obj.toString());
            // 发布实例，通知消费者可取
            ringBuffer.publish(sequence);
        }
        
        private int waitMethod(int counter) throws InterruptedException {
        
            if (counter > 100) {
                --counter;
            } else if (counter > 0) {
                --counter;
                Thread.yield();
            } else {
                // LockSupport.parkNanos(1L);
                Thread.sleep(1);
                // log.debug("wait receiver...");
            }
            return counter;
        }
    }
    
    class updateProducerSequenceThread implements Runnable{
        //private MemcachedClient memcachedClient = MemcacheUtil.createClient();
        
        public updateProducerSequenceThread() {
        
        }
        
        public void run() {
        
            //try {
                while (isProducerRun) {
                    try {
                        XMemcacheUtil.saveToCache(Constants.FILTER_LOCAL_SEQUENCER_KEY_CURR, 0, getMinimumSequence());
                        Thread.sleep(1);
                    } catch (Throwable t) {
                        log.error("updateProducerSequenceThread err:", t);
                    }
                }
//            } finally {
//                try {
//                    memcachedClient.shutdown();
//                } catch (Exception e) {
//                    log.error(e.getMessage());
//                }
//            }
        }
        
        private long getMinimumSequence() {
        
            long mum = requestInfoProducerSequence[0].value;
            for (int i = 1; i < requestInfoProducerSequence.length; i++) {
                long value = requestInfoProducerSequence[i].value;
                mum = Math.min(mum, value);
            }
            return mum;
        }
    }
    
    class updateReceiverSequenceThread implements Runnable{
        //private MemcachedClient memcachedClient = MemcacheUtil.createClient();
        
        public updateReceiverSequenceThread() {
        
        }
        
        public void run() {
        
            //try {
                while (isProducerRun) {
                    try {
                        Object receiver_curr_obj = XMemcacheUtil.getFromCache(Constants.RECEIVER_LOCAL_SEQUENCER_KEY_CURR);
                        if (null != receiver_curr_obj) {
                            Constants.receiver_curr = Long.parseLong(receiver_curr_obj.toString());
                        }
                        Thread.sleep(1);
                    } catch (Throwable t) {
                        log.error("updateReceiverSequenceThread err:", t);
                    }
                }
//            } finally {
//                try {
//                    memcachedClient.shutdown();
//                } catch (Exception e) {
//                    log.error(e.getMessage());
//                }
//            }
        }
    }
    
    static class RequestInfoProducerFactory implements ThreadFactory{
        final AtomicInteger threadNumber = new AtomicInteger(0);
        
        public Thread newThread(Runnable r) {
        
            Thread t = new Thread(r, String.valueOf(threadNumber.getAndIncrement()));
            return t;
        }
    }
    
    final static class VolatileLong{
        public volatile long value = -1L;
        public long p1, p2, p3, p4, p5, p6;
    }
}
