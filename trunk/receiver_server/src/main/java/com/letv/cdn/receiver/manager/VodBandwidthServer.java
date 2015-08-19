package com.letv.cdn.receiver.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.letv.cdn.receiver.model.VodBandwidthMap;

public class VodBandwidthServer{
    
    private static final Logger LOG = LoggerFactory.getLogger(VodBandwidthServer.class);
    
    private static VodBandwidthServer server;
    private ScheduledExecutorService threadsPool = Executors.newScheduledThreadPool(1);
    
    private VodBandwidthServer() {
    
    }
    
    public static synchronized void startVodBandwidthServer() throws Exception {
    
        if (server != null)
            return;
        server = new VodBandwidthServer();
        try {
            server.start();
        } catch (Exception ex) {
            LOG.error("VodBandwidthServer start error: " + ex.getMessage());
            throw ex;
        }
        LOG.info("============ VodBandwidthServer Is Running. ============");
    }
    
    public static synchronized void stopVodBandwidthServer() throws Exception {
    
        if (server == null)
            return;
        try {
            server.stop();
            server = null;
        } catch (Exception ex) {
            LOG.error("VodBandwidthServer stop error: " + ex.getMessage());
            throw ex;
        }
        LOG.info("============ VodBandwidthServer Has stopped. ============");
    }
    
    private void start() throws Exception {
    
        threadsPool.scheduleAtFixedRate(new VodBandwidthThread(), 5, 5, TimeUnit.SECONDS);
    }
    
    private void stop() throws Exception {
    
        if (null != server) {
            threadsPool.shutdown();
        }
    }
    
    class VodBandwidthThread implements Runnable{
        
        public VodBandwidthThread() {
        
        }
        
        public void run() {
        
            try {
                VodBandwidthMap.pushBw();
            } catch (Throwable t) {
                LOG.error("VodBandwidthThread err:", t);
            }
        }
    }
}
