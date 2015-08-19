package com.letv.cdn.receiver.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonWapper{
    
    private static String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";
    private JSONArray array = new JSONArray();
    
    public synchronized JSONObject syncNextItem() {
    
        JSONObject jsonObject = new JSONObject();
        return jsonObject;
    }
    
    public synchronized void syncEndItem(JSONObject jsonObject) {
    
        array.add(jsonObject);
    }
    
    public JSONObject nextItem() {
    
        JSONObject jsonObject = new JSONObject();
        return jsonObject;
    }
    
    public void endItem(JSONObject jsonObject) {
    
        array.add(jsonObject);
    }
    
    public String getArrayJson() {
    
        return array.toString();
    }
    
    public String getStoreJson(String flag) {
    
        JSONObject result = new JSONObject();
        result.put("success", flag);
        result.put("messages", array.toString());
        return result.toString();
    }
    
    public String getStoreJson(String flag, String[] keys, Object[] values) {
    
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", flag);
        jsonObject.put("messages", array);
        if (null != keys && null != values) {
            for (int i = 0; i < keys.length; i++) {
                jsonObject.put(keys[i], values[i]);
            }
        }
        return jsonObject.toString();
    }
    
    public void writeStore(HttpServletResponse response, String flag) throws IOException {
    
        PrintWriter writer = null;
        try {
            String json = this.getStoreJson(flag);
            response.setContentType(CONTENT_TYPE_JSON);
            writer = response.getWriter();
            writer.write(json);
            writer.flush();
        } finally {
            if (null != writer) {
                writer.close();
            }
        }
    }
    
    public void writeStore(HttpServletResponse response, String flag, String[] keys, Object[] values)
            throws IOException {
    
        PrintWriter writer = null;
        try {
            response.setContentType(CONTENT_TYPE_JSON);
            writer = response.getWriter();
            writer.write(this.getStoreJson(flag, keys, values));
            writer.flush();
        } finally {
            if (null != writer) {
                writer.close();
            }
        }
    }
}
