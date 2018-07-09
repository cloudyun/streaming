/**
 * Copyright: Copyright (c) 2017 
 * Company:深圳市深网视界科技有限公司
 * 
 * @author dell
 * @date 2017年5月8日 下午6:54:44
 * @version V1.0
 */
package com.yands.streaming.kafka;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: KfkProducer
 * @Description: KfkProducer
 * @author xupengtao
 * @date 2017年5月8日 下午6:54:44
 *
 */
public class KfkProducer {

    /**
     * producer context
     */
    private static Map<String, KfkProducerContext> CONTEXT_MAP = new ConcurrentHashMap<>();

    /**
     * 同步对象
     */
    private static final Object SYNC_ROOT = new Object();

    /**
     * @Title: createKfkProducer
     * @Description: 创建kafka生产者
     * @param zkConnect
     *            zookeeper ip & port
     * @param brokerlist
     *            kafka ip & port
     * @param topic
     *            主题
     * @param messages
     *            消息
     * @throws Exception
     *             异常
     */
    public static boolean sendMessage(String zkConnect, String brokerlist, String topicName, int partitionNum, String... messages) throws Exception {
        if (brokerlist == null || brokerlist.equals("") || partitionNum <= 0 || messages == null || messages.length == 0) {
            throw new Exception("send message param is not right.");
        }
        KfkProducerContext context = null;
        if (!CONTEXT_MAP.containsKey(brokerlist)) {
            synchronized (SYNC_ROOT) {
                context = new KfkProducerContext(zkConnect, brokerlist);
                CONTEXT_MAP.put(brokerlist, context);
            }
        }
        else {
            context = CONTEXT_MAP.get(brokerlist);
        }

        for (String message : messages) {
            context.send(String.valueOf(message.hashCode()), message, topicName);
        }

        return true;
    }

    /**
     * @Title: destroyKfkProducer
     * @Description: 销毁kfk生产者
     * @param brokerlist
     *            kafka ip & port
     */
    public static void destroyKfkProducer(String brokerlist) {
        if (CONTEXT_MAP.containsKey(brokerlist)) {
            CONTEXT_MAP.get(brokerlist).close();
            CONTEXT_MAP.remove(brokerlist);
        }

    }
}
