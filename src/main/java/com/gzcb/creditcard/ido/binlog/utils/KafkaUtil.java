package com.gzcb.creditcard.ido.binlog.utils;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * Kafka客户端操作工具类
 * 0.10.2版本
 * @author lwj
 * @date 2018/2/5
 */
public class KafkaUtil {
    private static KafkaProducer<String, String> producer = null;
    private static Properties props = null;
    private static PropertiesUtil pps = null;
    static {
        pps = new PropertiesUtil("/kafka.properties");
        props = new Properties();
        props.put("bootstrap.servers", pps.getString("kafka.broker.servers"));
        props.put("acks", "all");
        props.put("retries", "0");
        props.put("batch.size", "16384");
        props.put("linger.ms", "1");
        props.put("buffer.memory", "33554432");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<>(props);
    }

    /**
     * 将消息生产到kafka集群
     * @param topic
     * @param message
     */
    public static void produce(String topic, String message){
        producer.send(new ProducerRecord<String, String>(topic, message));
    }
}