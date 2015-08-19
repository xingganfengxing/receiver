package com.letv.cdn.receiver.service;

import com.letv.cdn.receiver.util.SequenceUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liufeng1
 * @date 6/5/2015
 */
public class BaseService {

    protected static final SequenceUtil sequence = new SequenceUtil();

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
}
