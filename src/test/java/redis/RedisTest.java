package redis;

/**
 * @author linlazy
 */
public class RedisTest {
    public static void main(String[] args) {
       RedisPool redisPool = new RedisPool();

        Holder<Long> holder = new Holder<>();
       redisPool.execute(jedis -> {
           Long count = jedis.zcard("aaaa");
           holder.setValue(count);
       });

        System.out.println(holder.getValue());
    }

}
