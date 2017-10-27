package com.vng.talktv.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisConnectionException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import java.util.Map;
import java.util.Set;

@Stateless
public class RedisService {
    private final static Logger LOGGER = LoggerFactory.getLogger(RedisService.class);

    private static JedisPoolConfig config = new JedisPoolConfig();

    private static JedisPool pool = new JedisPool(config, "127.0.0.1", 6379);

    @PostConstruct
    private void initPool(){
        config.setMaxIdle(10000);
        config.setMaxTotal(1000*60*60);
    }

    private static Jedis jedis = new Jedis("127.0.0.1", 6379);

    public String SET(String key, String value) {
        return jedis.set(key, value);
    }

    public Long EXPIRE(String key, int timeInSecond) {
        try {
            jedis.expire(key, timeInSecond);
        } catch (JedisConnectionException jce) {
            jedis = new Jedis("127.0.0.1", 6379);
            jedis.expire(key, timeInSecond);
        }
        return jedis.expire(key, timeInSecond);
    }

    public String HGET(String hmKey, String field) {
        try {
            String value = jedis.hget(hmKey, field);
            return value;
        } catch (NullPointerException e) {
            LOGGER.error("Null Pointer Exception --- Key: " + hmKey + " or Field does not exist");
            return "-1";
        } catch (JedisConnectionException jce) {
            jedis = new Jedis("127.0.0.1", 6379);
            String value = jedis.hget(hmKey, field);
            return value;
        }
    }

    public String GET(String key) {
        try {
            String value = jedis.get(key);
            return value;
        } catch (NullPointerException e) {
            LOGGER.error("Null Pointer Exception --- Key: " + key + " does not exist");
            return "-1";
        } catch (JedisConnectionException jce) {
            jedis = new Jedis("127.0.0.1", 6379);
            return GET(key);
        }
    }

    public long HINCRBY(String key, String field, int value) {
        try {
            return jedis.hincrBy(key, field, value);
        } catch (JedisConnectionException jce) {
            jedis = new Jedis("127.0.0.1", 6379);
            return this.HINCRBY(key, field, value);
        }
    }

    public boolean HEXIST(String idolId, String field) {
        try {
            return jedis.hexists(idolId, field);
        } catch (JedisConnectionException jce) {
            jedis = new Jedis("127.0.0.1", 6379);
            return this.HEXIST(idolId, field);
        }
    }

    public Double ZINCRBY(String key, double score, String member) {
        try {
            return jedis.zincrby(key, score, member);
        } catch (JedisConnectionException jce) {
            jedis = new Jedis("127.0.0.1", 6379);
            return this.ZINCRBY(key, score, member);
        }
    }

    public long ZCOUNTALL(String key) {
        try {
            return jedis.zcount(key, "-inf", "+inf");
        } catch (JedisConnectionException jce) {
            jedis = new Jedis("127.0.0.1", 6379);
            return this.ZCOUNTALL(key);
        }
    }

    public long ZREVRANK(String key, String member) {
        try {
            long rank = jedis.zrevrank(key, member);
            return rank;
        } catch (NullPointerException e) {
            LOGGER.error("Null Pointer Exception --- Key: " + key + " does not exist");
            return -1;
        } catch (JedisConnectionException jce) {
            jedis = new Jedis("127.0.0.1", 6379);
            return this.ZREVRANK(key, member);
        }
//        Object rank = jedis.zrevrank(key, member);
//        if (rank != null)
//            return jedis.zrevrank(key, member);
//        else
//            return -1;
    }

    public long DEL(String key) {
        try {
            return jedis.del(key);
        } catch (JedisConnectionException jce) {
            jedis = new Jedis("127.0.0.1", 6379);
            return this.DEL(key);
        }
    }

    public Map<String, String> HGETALL(String key) {
        try {
            return jedis.hgetAll(key);
        } catch (JedisConnectionException jce) {
            jedis = new Jedis("127.0.0.1", 6379);
            return this.HGETALL(key);
        }
    }

    public Set<Tuple> ZREVRANGEWITHSCORES(String key, long start, long end) {
        try {
            return jedis.zrevrangeWithScores(key, start, end);
        } catch (JedisConnectionException jce) {
            jedis = new Jedis("127.0.0.1", 6379);
            return this.ZREVRANGEWITHSCORES(key, start, end);
        }
    }

    @PreDestroy
    public void CLOSE() {
        jedis.close();
    }
}
