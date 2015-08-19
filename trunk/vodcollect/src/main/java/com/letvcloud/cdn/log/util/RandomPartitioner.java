package com.letvcloud.cdn.log.util;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

/**
 * Created by liufeng1 on 2015/1/21.
 */
public class RandomPartitioner implements Partitioner {

    public RandomPartitioner(VerifiableProperties props) {

    }

    public int partition(Object o, int i) {
        return ArrayIndexUtil.getRandomPortIndex(i);
    }
}
