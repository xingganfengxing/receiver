package com.letv.cdn.receiver.util;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.DefaultPartitioner;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

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
    
    private static final Logger LOG = LoggerFactory.getLogger(KafkaUtil.class);
    public static final String SYNC_TYPE = "sync";
    public static final String ASYNC_TYPE = "async";
    private static final String KAFKA_SERVER_IP;
    private static final String BATCH_NUM;
    private Producer<String, String> producer;
    private String topic;
    
    static {
        KAFKA_SERVER_IP = StringUtil.isEmpty(Env.get("kafkaServerIp")) ? "127.0.0.1" : Env.get("kafkaServerIp");
        BATCH_NUM = StringUtil.isEmpty(Env.get("batchNum")) ? "200" : Env.get("batchNum");
        LOG.info("kafkaServerIp:" + KAFKA_SERVER_IP + ",batchNum:" + BATCH_NUM);
    }
    
    public KafkaUtil() {
    
    }
    
    public KafkaUtil(String type) {
    
        this("", type);
    }
    
    public KafkaUtil(String topic, String type) {
    
        this.topic = topic;
        Properties props = new Properties();
        props.put("metadata.broker.list", KAFKA_SERVER_IP);
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", DefaultPartitioner.class.getName());
        props.put("request.required.acks", "1");
        props.put("producer.type", type);
        props.put("message.send.max.retries", "1");
        props.put("batch.num.messages", BATCH_NUM);
        props.put("queue.enqueue.timeout.ms", "-1");
        ProducerConfig config = new ProducerConfig(props);
        producer = new Producer<String, String>(config);
    }
    
    public Boolean sendObjectKafka(Object msg) {
    
        KeyedMessage data = new KeyedMessage(this.topic, null, msg.toString());
        producer.send(data);
        return true;
    }
    
    public Boolean sendObjectKafka(Object msg, String topic) {
    
        KeyedMessage data = new KeyedMessage(topic, null, msg.toString());
        producer.send(data);
        return true;
    }
    
    public void close() {
    
        if (null != producer) {
            producer.close();
        }
    }
}
