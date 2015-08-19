package com.letv.cdn.receiver.util;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.DefaultPartitioner;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.letv.cdn.common.Env;
import com.letv.cdn.common.StringUtil;

/**
 * Kafka Util
 * 
 * @author kk
 */
public class KafkaUtil{
    
    private static final Logger log = LoggerFactory.getLogger(KafkaUtil.class);
    
    private static final String KAFKA_SERVER_IP;
    private static final String BATCH_NUM;
    private Producer<String, String> producer;
    private String topic;
    
    static {
        KAFKA_SERVER_IP = StringUtil.isEmpty(Env.get("kafkaServerIp")) ? "127.0.0.1" : Env.get("kafkaServerIp");
        BATCH_NUM = StringUtil.isEmpty(Env.get("batchNum")) ? "200" : Env.get("batchNum");
        log.info("kafkaServerIp:" + KAFKA_SERVER_IP + ",batchNum:" + BATCH_NUM);
    }
    
    public KafkaUtil() {
    
    }
    
    public KafkaUtil(String topic) {
    
        this.topic = topic;
        Properties props = new Properties();
        props.put("metadata.broker.list", KAFKA_SERVER_IP);
        props.put("serializer.class", StringEncoder.class.getName());
        props.put("partitioner.class", DefaultPartitioner.class.getName());
        props.put("request.required.acks", "1");
        props.put("producer.type", "async");
        props.put("batch.num.messages", BATCH_NUM);
        props.put("queue.enqueue.timeout.ms", "-1");
        ProducerConfig config = new ProducerConfig(props);
        producer = new Producer<String, String>(config);
    }
    
    public Boolean sendObjectKafka(Object msg) {
    
        boolean flag = false;
        try {
            KeyedMessage data = new KeyedMessage(this.topic, null, msg.toString());
            producer.send(data);
            flag = true;
        } catch (Exception e) {
            log.error("kafka send " + msg + " failed:", e);
        }
        return flag;
    }
    
    public void close() {
    
        if (null != producer) {
            producer.close();
        }
    }
}
