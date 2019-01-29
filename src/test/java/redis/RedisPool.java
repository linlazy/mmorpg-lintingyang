package redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * @author linlazy
 */
public class RedisPool {
    private JedisPool jedisPool;

    private int reconnectionTimes;

    public RedisPool() {
        this.jedisPool = new JedisPool();
    }

    public void execute(CallWithJedis callWithJedis){
        Jedis jedis = jedisPool.getResource();
        try{
            callWithJedis.call(jedis);
        }catch (JedisConnectionException e){
            reconnection(callWithJedis,jedis,reconnectionTimes);
        }finally {
            jedis.close();
        }
    }

    private void reconnection(CallWithJedis callWithJedis, Jedis jedis,int reconnectionTimes) {
        if(reconnectionTimes >0){
            try{
                callWithJedis.call(jedis);
            }catch (JedisConnectionException e){
                reconnection(callWithJedis,jedis,--reconnectionTimes);
            }finally {
                jedis.close();
            }
        }

    }
}
