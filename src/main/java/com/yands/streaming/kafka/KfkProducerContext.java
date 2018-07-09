package com.yands.streaming.kafka;

import java.util.Properties;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;

import kafka.serializer.StringEncoder;

/**
 * @ClassName: KfkProducerContext
 * @Description: Kafka生产者
 * @author xupengtao
 * @date 2017年4月10日 下午12:02:26
 *
 */
class KfkProducerContext {

    /**
     * LOGGER
     */
    private static final Logger LOGGER = Logger.getLogger(KfkProducerContext.class);

    /**
     * metadata.broker.list, 如192.168.1.249:9092,192.168.1.250:9092
     */
    private String brokerlist;

    /**
     * zookeeper.connect 连接路径, 如192.168.1.249:2181,192.168.1.250:2181
     */
    @SuppressWarnings("unused")
	private String zkConnect;

    /**
     * 生产者
     */
    private KafkaProducer<Object, String> producer = null;

    /**
     * 构造函数
     * 
     * @param brokerlist
     *            kafka服务器列表, 如192.168.1.249:9092,192.168.1.250:9092
     * @param zkConnect
     *            zk服务器列表
     */
    public KfkProducerContext(String zkConnect, String brokerlist) {
        this.zkConnect = zkConnect;
        this.brokerlist = brokerlist;

        this.producer = createProducer();
    }

    /**
     * @Title: send
     * @Description: 发送消息
     * @param key
     *            Key
     * @param message
     *            Message
     * @param topic
     *            topic
     */
    public void send(Object key, String message, final String topic) {
        ProducerRecord<Object, String> record = new ProducerRecord<Object, String>(topic, key, message);

        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception e) {
                if (e != null)
                    LOGGER.error(String.format("send message to topic [%s] error.", topic), e);
                else {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("The offset of the record we just sent is: " + metadata.offset());
                        LOGGER.debug("The partition of the record we just sent is: " + metadata.partition());
                    }
                }

            }
        });
    }

    /**
     * @Title: close
     * @Description: 关闭连接
     */
    public void close() {
        this.producer.close();
    }

    /**
     * @Title: createProducer
     * @Description: 创建生产者
     * @return KafkaProducer<Object, String>
     */
    private KafkaProducer<Object, String> createProducer() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", this.brokerlist);
        properties.put("metadata.broker.list", this.brokerlist);
        properties.put("key.serializer", StringSerializer.class.getName());
        properties.put("serializer.class", StringEncoder.class.getName());
        properties.put("value.serializer", StringSerializer.class.getName());
        properties.put("request.required.acks", "1");
        KafkaProducer<Object, String> producer = new KafkaProducer<Object, String>(properties);
        return producer;
    }

}
