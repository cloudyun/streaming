package com.yands.streaming.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.yands.streaming.util.LoadConfig.Config;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

/**  
 * @Title:  RedisCluster.java   
 * @Package com.lingda.vid.util.redis   
 * @Description:    (redis集群模式)   
 * @author: gaoyun     
 * @edit by: 
 * @date:   2018年6月6日 下午6:26:34   
 * @version V1.0 
 */ 
public class RedisCluster {

	private static final Logger LOG = Logger.getLogger(RedisCluster.class);
	
	private static final String FACE_KEYS = "facekeys";
	
	private static final String BODY_KEYS = "bodykeys";
	
	private JedisCluster client = null;
	
	public RedisCluster() {
		getClient();
	}
	
	/**
	 * 获取JedisCluster实例
	 * @return
	 */
	public JedisCluster getClient() {
		if (client != null) {
			return client;
		}
		try {
			// 加载redis配置文件
			Config config = LoadConfig.getInstance("RedisConfig");
			Set<HostAndPort> hosts = new HashSet<HostAndPort>();
			String urls = config.get("redisCluster");
			for (String url : urls.split(",")) {
				String[] split = url.split(":");
				hosts.add(new HostAndPort(split[0], Integer.valueOf(split[1])));
			}
			int timeout = Integer.valueOf(config.get("redisTimeout"));
			JedisPoolConfig poolConfig = new JedisPoolConfig();
			poolConfig.setMaxIdle(Integer.valueOf(config.get("redisMaxIdle")));
			poolConfig.setMaxTotal(Integer.valueOf(config.get("redisMaxActive")));
			poolConfig.setMaxWaitMillis(Integer.valueOf(config.get("redisMaxWait")));
			poolConfig.setMaxIdle(Integer.valueOf(config.get("redisMaxIdle")));
			client = new JedisCluster(hosts, timeout, poolConfig);
			return client;
		} catch (Exception e) {
			close();
			client = null;
			LOG.error(e);
			return null;
		}
	}
	
	public void close() {
		if (client == null) {
			return;
		}
		try {
			client.close();
		} catch (Exception e) {
			LOG.error(e);
		}
	}
	
	/**
	 * 向缓存中设置字符串内容
	 * 
	 * @param key key
	 * @param seconds 过期时间，单位秒
	 * @param value value
	 * @return
	 * @throws Exception
	 */
	public String setex(String key, String value, int seconds) {
		JedisCluster client = getClient();
		if (client == null) {
			return null;
		}
		try {
			return client.setex(key, seconds, value);
		} catch (Exception e) {
			LOG.error(e);
			return null;
		}
	}

	/**
	 * 向缓存中设置字符串内容
	 * 
	 * @param key key
	 * @param value value
	 * @return
	 * @throws Exception
	 */
	public String set(String key, String value) {
		JedisCluster client = getClient();
		if (client == null) {
			return null;
		}
		try {
			return client.set(key, value);
		} catch (Exception e) {
			LOG.error(e);
			return null;
		}
	}

	/**
	 * 获取key的内容
	 * @param key
	 * @return
	 */
	public String get(String key) {
		JedisCluster client = getClient();
		if (client == null) {
			return null;
		}
		try {
			return client.get(key);
		} catch (Exception e) {
			LOG.error(e);
			return null;
		}
	}

	public boolean lexist(String key, String value) {
		JedisCluster client = getClient();
		if (client == null) {
			return false;
		}
		if (value == null) {
			return false;
		}
		
		try {
			return value.equals(client.lindex(key, 0));
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e);
			return false;
		}
	}

	public Long sadd(String key, String value) {
		JedisCluster client = getClient();
		if (client == null) {
			return null;
		}
		
		try {
			return client.sadd(key, value);
		} catch (Exception e) {
			LOG.error(e);
			return null;
		}
	}

	public Boolean sismember(String key, String value) {
		JedisCluster client = getClient();
		if (client == null) {
			return null;
		}
		
		try {
			return client.sismember(key, value);
		} catch (Exception e) {
			LOG.error(e);
			return null;
		}
	}

	public Long push(String key) {
		JedisCluster client = getClient();
		if (client == null) {
			return null;
		}
		if (key == null) {
			return null;
		}
		try {
			if (key.indexOf("face") >= 0 && !key.equals(client.lindex(FACE_KEYS, 0))) {
				return client.lpush(FACE_KEYS, key);
			}
			if (key.indexOf("body") >= 0 && !key.equals(client.lindex(BODY_KEYS, 0))) {
				return client.lpush(BODY_KEYS, key);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e);
			return null;
		}
	}

	public Long rpush(String key, String value) {
		JedisCluster client = getClient();
		if (client == null) {
			return null;
		}
		try {
			return client.rpush(key, value);
		} catch (Exception e) {
			LOG.error(e);
			return null;
		}
	}

	public List<String> lrange(String key) {
		JedisCluster client = getClient();
		if (client == null) {
			return null;
		}
		try {
			return client.lrange(key, 0, -1);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e);
			return null;
		}
	}

	/**
	 * 获取key的内容
	 * @param key
	 * @return
	 */
	public Set<String> hkeys(String key) {
		JedisCluster client = getClient();
		if (client == null) {
			return null;
		}
		try {
			return client.hkeys(key);
		} catch (Exception e) {
			LOG.error(e);
			return null;
		}
	}

	public Long hset(String key, String field, String value) {
		
		JedisCluster client = getClient();
		if (client == null) {
			return null;
		}
		try {
			return client.hset(key, field, value);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e);
			return null;
		}
	}

	public String hget(String key, String field) {
		JedisCluster client = getClient();
		if (client == null) {
			return null;
		}
		try {
			return client.hget(key, field);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e);
			return null;
		}
	}

	public Long del(String key) {
		JedisCluster client = getClient();
		if (client == null) {
			return null;
		}
		try {
			return client.del(key);
		} catch (Exception e) {
			return null;
		}
	}

	public String lindex(String key, long index) {
		JedisCluster client = getClient();
		if (client == null) {
			return null;
		}
		try {
			return client.lindex(key, index);
		} catch (Exception e) {
			return null;
		}
	}
}