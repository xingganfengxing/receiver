package com.letvcloud.cdn.log.util;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 获取通信端口工具类
 * Created by liufeng1 on 2014/12/31.
 */
public class ArrayIndexUtil {
    private static AtomicInteger index = new AtomicInteger(0);
    private static Random random = new Random();

    /**
     * 随机选择索引
     * @param length 数组长度
     * @return
     */
    public static int getRandomPortIndex(int length) {
        return random.nextInt(length);
    }

    /**
     * 顺序选择索引
     * @param length
     * @return
     */
    public static int getNextIndex(int length) {
        index.getAndIncrement();
        index.compareAndSet(length, 0);
        return index.get();
    }
}
