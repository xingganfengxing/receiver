package com.letv.cdn.receiver.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.letv.cdn.receiver.servlet.support.BaseServlet;
import com.letv.cdn.receiver.servlet.support.IVodReceiver;
import com.letv.cdn.receiver.util.Constants;
import com.letv.cdn.receiver.util.JsonWapper;
import com.letv.cdn.receiver.util.SequenceUtil;
import com.letv.cdn.receiver.util.XMemcacheUtil;

/**
 * 点播接收入口
 * 
 * @author kk
 * 
 */
public class ReceiverServlet extends BaseServlet implements IVodReceiver{
    
    private static final Logger LOG = LoggerFactory.getLogger(ReceiverServlet.class);
    public static final SequenceUtil sequence = new SequenceUtil(Constants.receiver_curr);
    private final int retries = 10000;
    
    @Override
    protected void execute(HttpServletRequest request, HttpServletResponse response) {
    
        final JsonWapper wapper = new JsonWapper();
        String flag = Constants.RESULT_SUCCESS;
        try {
            String body = unZipBodyString(request.getInputStream());// 首先进行解压
            int counter = retries;
            long current;
            long intnext;
            do {
                current = sequence.getCurrent();
                intnext = current + 1;
                long wrapPoint = intnext - Constants.MEMCACHED_BUFFER_SIZE;
                if (wrapPoint > Constants.filter_curr)// 生产者比消费者快
                {
                    LOG.warn("===receiver wait===");
                    counter = waitMethod(counter);
                    continue;
                } else if (sequence.compareAndSet(current, intnext)) {
                    int nextTmp = (int) intnext & Constants.INDEX_MASK;
                    String next = String.valueOf(nextTmp);
                    if (!XMemcacheUtil.saveToCache(next, body.toString())) {
                        LOG.error("set object err:" + intnext);
                    }
                    Constants.receiver_curr = intnext;// 更新当前序号到缓存
                    break;
                }
            } while (true);
        } catch (Throwable e) {
            LOG.error("receiver err: ", e);
            flag = Constants.RESULT_FAILED;
            JSONObject jsonObject = wapper.nextItem();
            jsonObject.put("err", e.getMessage());
            wapper.endItem(jsonObject);
        } finally {
            try {
                wapper.writeStore(response, flag);
            } catch (Exception e) {
                LOG.error("writeResponse err: ", e);
            }
        }
    }
    
    private String unZipBodyString(InputStream br) throws IOException {
    
        String bodyStr = "";
        GZIPInputStream gzip = new GZIPInputStream(br);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[4096];
            int length = 0;
            while ((length = gzip.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            baos.flush();
            bodyStr = new String(baos.toByteArray(), "UTF-8");
        } finally {
            baos.close();
            br.close();
            gzip.close();
        }
        return bodyStr;
    }
    
    private int waitMethod(int counter) throws Exception {
    
        if (counter > 0) {
            --counter;
            Thread.sleep(1);
        } else {
            throw new Exception("receiver wait");
        }
        return counter;
    }
    
}
