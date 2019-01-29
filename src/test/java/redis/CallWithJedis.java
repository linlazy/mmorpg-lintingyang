package redis;

import redis.clients.jedis.Jedis;

/**
 * @author linlazy
 */
public interface CallWithJedis {

    public void call(Jedis jedis);
}
