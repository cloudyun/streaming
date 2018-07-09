package com.yands.streaming.util;

import java.util.List;
import java.util.Set;

import com.yands.streaming.util.LoadConfig.Config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

/**  
 * @Title:  RedisUtil.java   
 * @Package com.lingda.vid.util.redis   
 * @Description:    (redis连接工具)   
 * @author: gaoyun     
 * @edit by: 
 * @date:   2018年6月11日 上午11:10:03   
 * @version V1.0 
 */ 
public class RedisUtil {
	
	private static final String FACE_KEYS = "facekeys";
	
	private static final String BODY_KEYS = "bodykeys";

	private static Config config = LoadConfig.getInstance("RedisConfig");
	
	private static String IS_CLUSTER = config.get("isCluster");
	
	private static boolean isCluster = Boolean.parseBoolean(IS_CLUSTER == null ? "false" : IS_CLUSTER);
	
	private RedisCluster cluster;
	
	private RedisAlone alone;
	
	public RedisUtil() {
		if (isCluster && cluster == null) {
			cluster = new RedisCluster();
		}
		if (!isCluster && alone == null) {
			alone = new RedisAlone();
		}
	}
	
	public void close() {
		if (isCluster && cluster != null) {
			cluster.close();
		}
//		if (!isCluster && alone != null) {
//			alone.close();
//		}
		if (!isCluster && alone != null) {
			alone.returnResource();
		}
	}
	
	public String get(String key) {
		return isCluster ? cluster.get(key) : alone.get(key);
	}
	
	public String set(String key, String value) {
		return isCluster ? cluster.set(key, value) : alone.set(key, value);
	}
	
	public String hget(String key, String field) {
		return isCluster ? cluster.hget(key, field) : alone.hget(key, field);
	}
	
	public Long hset(String key, String field, String value) {
		return isCluster ? cluster.hset(key, field, value) : alone.hset(key, field, value);
	}
	
	public String lindex(String key, long index) {
		return isCluster ? cluster.lindex(key, index) : alone.lindex(key, index);
	}
	
	public String setex(String key, String value, int seconds) {
		return isCluster ? cluster.setex(key, value, seconds) : alone.setex(key, value, seconds);
	}
	
	public Long push(String key) {
		return isCluster ? pushCluster(key) : pushAlone(key);
	}

	public Long pushCluster(String key) {
		if (key == null) {
			return null;
		}
		JedisCluster client = cluster.getClient();
		if (client == null ) {
			return null;
		}
		if (key.indexOf("face") >= 0 && !key.equals(client.lindex(FACE_KEYS, 0))) {
			return client.lpush(FACE_KEYS, key);
		}
		if (key.indexOf("body") >= 0 && !key.equals(client.lindex(BODY_KEYS, 0))) {
			return client.lpush(BODY_KEYS, key);
		}
		return null;
	}

	public Long pushAlone(String key) {
		if (key == null) {
			return null;
		}
		Jedis client = alone.getJedis();
		if (client == null) {
			return null;
		}
		if (key.indexOf("face") >= 0 && !key.equals(client.lindex(FACE_KEYS, 0))) {
			Long lpush = client.lpush(FACE_KEYS, key);
			alone.releaseJedis(client);
			return lpush;
		}
		if (key.indexOf("body") >= 0 && !key.equals(client.lindex(BODY_KEYS, 0))) {
			Long lpush = client.lpush(BODY_KEYS, key);
			alone.releaseJedis(client);
			return lpush;
		}
		return null;
	}
	
	public List<String> lrange(String key) {
		return isCluster ? cluster.lrange(key) : alone.lrange(key, 0, -1);
	}
	
	public Set<String> hkeys(String key) {
		return isCluster ? cluster.hkeys(key) : alone.hkeys(key);
	}
	
	public Long del(String key) {
		return isCluster ? cluster.del(key) : alone.del(key);
	}
	
	public Long lpush(String key, String value) {
		if (value == null) {
			return null;
		}
		if (isCluster) {
			JedisCluster client = cluster.getClient();
			if (client == null) {
				return null;
			}
			if (!value.equals(client.lindex(key, 0))) {
				return client.lpush(key, value);
			}
		} else {
			Jedis client = alone.getJedis();
			if (client == null) {
				return null;
			}
			if (!value.equals(client.lindex(key, 0))) {
				Long lpush = client.lpush(key, value);
				alone.releaseJedis(client);
				return lpush;
			}
		}
		return null;
	}
}