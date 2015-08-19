package com.letv.cdn.receiver.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class OnlineMapping{
    public static ConcurrentMap<String, Boolean> OLMapping = new ConcurrentHashMap<String, Boolean>();
}
