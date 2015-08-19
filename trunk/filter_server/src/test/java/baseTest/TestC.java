package baseTest;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.spy.memcached.OperationTimeoutException;

import com.letv.cdn.receiver.util.MemcacheUtil;

public class TestC{
    public static void main(String[] args) throws Exception {
    
        // String str = "1\t2\t\t3\t4\t";
        // String[] arr = str.split("\t");
        // for (String s : arr) {
        // System.out.println(s);
        // }
       // MemcachedClient memcachedClient = MemcacheUtil.createClient();
        Thread.sleep(50000);
        try {
            // System.out.println(memcachedClient.set("cdn-filter_0_filter_sequence_curr",
            // 0, 100));
            // System.out.println(memcachedClient.incr("cdn-filter_0_filter_sequence_curr",
            // Integer.MAX_VALUE));
            //System.out.println(memcachedClient.get("987"));
            Object myObj = null;
            Future<Object> f = (Future<Object>) MemcacheUtil.asyncGet("123");
            Future<Object> f2 = (Future<Object>) MemcacheUtil.asyncGet("456");
            System.out.println("==============================第一==============================");
            
            try {
                myObj = f.get(5, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                f.cancel(false);
                System.out.println("TimeoutException1");
            }
            
            try {
                myObj = f2.get(5, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                //f2.cancel(false);
                System.out.println("TimeoutException2");
            }
            System.out.println(f.isCancelled());
//            System.out.println("==============================第二==============================");
//            System.out.println(memcachedClient.get("987"));
            // memcachedClient.getAndTouch(key, exp)
            // System.out.println(memcachedClient.get("cdn-receiver_1_sequence_curr"));
            // System.out.println(memcachedClient.get("0"));
            
            // System.out.println(memcachedClient.get("120")==null);
            // memcachedClient.shutdown();
        } catch (OperationTimeoutException ex) {
            // ex.printStackTrace();
            System.out.println("============================================================");
        } finally {
             //memcachedClient.shutdown();
            System.out.println("==============================finally==============================");
        }
    }
}
