package com.letv.cdn.receiver.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 流的相关转换
 * @author liufeng1
 * @date 9/4/2015
 */
public class StreamUtils {
    /**
     * 从request inputstream中读取String
     * @throws IOException
     */
    public static String getBodyString(InputStream br) throws IOException {

        String bodyStr = "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[4096];
            int length = 0;
            while ((length = br.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            baos.flush();
            bodyStr = new String(baos.toByteArray(), "UTF-8");
        } finally {
            br.close();
            baos.close();
        }
        return bodyStr;
    }
}
