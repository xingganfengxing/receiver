package com.letv.cdn.receiver.servlet.support;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.letv.cdn.receiver.model.DataResponse;

/**
 * Servlet 基类
 * 
 * @author kk
 * 
 */
public abstract class BaseServlet extends HttpServlet{
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
        doHttp(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
    
        doHttp(request, response);
    }
    
    /**
     * 响应
     * 
     */
    private void doHttp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        this.execute(request, response);
    }
    
    /**
     * 输出
     * 
     */
    protected void writeResponse(HttpServletResponse response, DataResponse dataResponse) throws IOException {
    
        PrintWriter writer = null;
        try {
            response.setContentType("text/html;charset=utf-8");
            writer = response.getWriter();
            writer.write(dataResponse.toString());
            writer.flush();
        } finally {
            if (null != writer) {
                writer.close();
            }
        }
    }
    
    /**
     * 封装request参数到Map
     *
     */
    protected Map getParameterMap(HttpServletRequest request) throws Exception {
    
        Map paramMap = new HashMap();
        Enumeration enumTmp = request.getParameterNames();
        while (enumTmp.hasMoreElements()) {
            String key = (String) enumTmp.nextElement();
            try {
                paramMap.put(key, URLDecoder.decode(convertToUTF8(request.getParameter(key)), "UTF-8"));
            } catch (Exception e) {
                paramMap.put(key, convertToUTF8(request.getParameter(key)));
            }
        }
        return paramMap;
    }
    
    protected Map getParameterMap(String line) {
    
        Map paramMap = new HashMap();
        String[] arr = line.split("&");
        int index = -1;
        for (int i = 0; i < arr.length; i++) {
            index = arr[i].indexOf("=");
            if (index != -1) {
                if (arr[i].length() > index + 1) {
                    paramMap.put(arr[i].substring(0, index), arr[i].substring(index + 1));
                } else {
                    paramMap.put(arr[i].substring(0, index), null);
                }
                
            }
        }
        return paramMap;
    }
    
    protected String convertToUTF8(String s) throws Exception {
    
        if ((s == null) || (s.length() == 0))
            return s;
        try {
            byte[] b = s.getBytes("ISO8859_1");
            for (int i = 0; i < b.length; ++i)
                if (b[i] + 0 < 0)
                    return new String(b, "UTF-8");
            b = s.getBytes("UTF-8");
            for (int i = 0; i < b.length; i++)
                if (b[i] + 0 < 0)
                    return new String(b, "UTF-8");
        } catch (Exception e) {
            throw e;
        }
        return s;
    }
    
    /**
     * 从request inputstream中读取String
     *
     * @throws IOException
     */
    protected String getBodyString(InputStream br) throws IOException {
    
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
    
    /**
     * 从request inputstream中读取String
     *
     * @throws IOException
     */
    protected String getBodyString(BufferedReader br) throws IOException {
    
        StringBuilder ssb = new StringBuilder();
        String inputLine;
        try {
            while ((inputLine = br.readLine()) != null) {
                ssb.append(inputLine);
                ssb.append("\n");
            }
        } finally {
            br.close();
        }
        return ssb.toString();
    }
    
    /**
     * 执行
     * 
     */
    protected abstract void execute(HttpServletRequest request, HttpServletResponse response);
}