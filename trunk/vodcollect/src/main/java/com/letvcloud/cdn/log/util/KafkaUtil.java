package com.letvcloud.cdn.log.util;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Kafka Util
 *
 * @author kk
 */
public class KafkaUtil {

    private static final Logger log = LoggerFactory.getLogger(KafkaUtil.class);

    private static final String KAFKA_SERVER_IP = Constants.KAFKA_SERVER_IP;
    private static final String BATCH_NUM;
    private Producer<String, String> producer;
    private String topic;

    static {
        BATCH_NUM = StringUtils.isEmpty(Env.get("batchNum")) ? "1000" : Env.get("batchNum");
        log.info("kafkaServerIp:" + KAFKA_SERVER_IP + ",batchNum:" + BATCH_NUM);
    }

    public KafkaUtil() {
        initConfig();
    }

    public KafkaUtil(String topic) {
        this.topic = topic;
        initConfig();
    }

    public KafkaUtil withTopic(String topic) {
        this.topic = topic;
        return this;
    }

    private void initConfig() {
        Properties props = new Properties();
        props.put("metadata.broker.list", KAFKA_SERVER_IP);
        props.put("serializer.class", StringEncoder.class.getName());
        props.put("partitioner.class", RandomPartitioner.class.getName());
        props.put("request.required.acks", "1");
        props.put("producer.type", "async");
        props.put("batch.num.messages", BATCH_NUM);
        props.put("queue.enqueue.timeout.ms", "-1");
        ProducerConfig config = new ProducerConfig(props);
        producer = new Producer<>(config);
    }

    public Boolean sendObjectKafka(Object msg) {

        boolean flag = false;
        try {
            KeyedMessage data = new KeyedMessage(this.topic, "", msg.toString());
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
