package com.letv.cdn.receiver.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * zip工具类
 * Created by liufeng1 on 2014/12/22.
 */
public class ZipUtils {

    public static String unZipBodyString(InputStream br) throws IOException {

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
}
