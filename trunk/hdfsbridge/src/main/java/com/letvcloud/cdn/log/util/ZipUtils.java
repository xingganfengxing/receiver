package com.letvcloud.cdn.log.util;

import com.letvcloud.cdn.log.exception.UnZipFileException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * zip工具类
 * Created by liufeng1 on 2014/12/22.
 */
public class ZipUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(ZipUtils.class);

    public static String unZipBodyString(InputStream br) throws UnZipFileException {

        String bodyStr = "";
        GZIPInputStream gzip = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            gzip = new GZIPInputStream(br);
            byte[] buffer = new byte[4096];
            int length = 0;
            while ((length = gzip.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            baos.flush();
            bodyStr = new String(baos.toByteArray(), "UTF-8");
        } catch (Exception e) {
            throw new UnZipFileException(e.getMessage());
        } finally {
            IOUtils.closeQuietly(baos);
            IOUtils.closeQuietly(br);
            IOUtils.closeQuietly(gzip);
        }
        return bodyStr;
    }

    /**
     * 遇 UnZipFileException 递归解压bytes
     *
     * @param bytes
     * @return
     */
    public static String recureUnZip(byte[] bytes,int times) {

        if(times <= 0){
            return "";
        }

        InputStream inputStream = new ByteArrayInputStream(bytes);
        String body = "";

        try {
            body = ZipUtils.unZipBodyString(inputStream);// 首先进行解压 outofmemException
        } catch (UnZipFileException e) {
            LOGGER.error("unzip fail,caused by{}", e.getMessage());
            LOGGER.info("start reunzip:");
            body = recureUnZip(bytes,--times);
        }

        return body;
    }
}
