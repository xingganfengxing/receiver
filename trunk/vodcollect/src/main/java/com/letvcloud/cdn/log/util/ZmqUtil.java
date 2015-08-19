package com.letvcloud.cdn.log.util;

import org.zeromq.ZMQ;

/**
 * ZMQ工具类
 * Created by liufeng1 on 2015/1/21.
 */
public class ZmqUtil {

    private static ZMQ.Context zmqContext = ZMQ.context(1);

    public static byte[] sendRec(String msg, String zmqUrl) {
        ZMQ.Socket zmqSocket = zmqContext.socket(ZMQ.REQ);
        zmqSocket.connect(zmqUrl);
        zmqSocket.send(msg, ZMQ.NOBLOCK);
        byte[] recBytes = zmqSocket.recv(0);
        //如果不关闭则报org.zeromq.ZMQException: Too many open files(0x18)
        zmqSocket.close();

        return recBytes;
    }
}
