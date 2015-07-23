package cn.libofeng.air.examples.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;
import java.net.URL;


@Component
public class Example {
    private final Logger logger = LoggerFactory.getLogger(Example.class);

    // inject the actual template
    @Autowired
    private ShardedJedisPool shardedJedisPool;

    // inject the actual template
    @Autowired
    private RedisTemplate<String, String> template;

    // inject the actual template
    @Autowired
    private RedisTemplate<String, User> userTemplate;

    // inject the template as ListOperations
    // can also inject as Value, Set, ZSet, and HashOperations
    @Resource(name = "redisTemplate")
    private ListOperations<String, String> listOps;

    public void addLink(String userId, URL url) {
        listOps.leftPush(userId, url.toExternalForm());
        // or use template directly
        template.boundListOps(userId).leftPush(url.toExternalForm());

        String value1 = listOps.rightPop(userId);
        logger.debug("value1={}", value1);
        String value2 = template.boundListOps(userId).rightPop();
        logger.debug("value2={}", value2);

        ShardedJedis jedis = shardedJedisPool.getResource();
        jedis.lpush(userId, url.toExternalForm());
        String value3 = jedis.rpop(userId);
        logger.debug("value3={}", value3);

        String value4 = template.boundListOps(userId).rightPop();
        logger.debug("value4={}", value4); // should be null
    }

    public void addUser(User user) {
        userTemplate.boundListOps("user:").leftPush(user);
        User userPop = userTemplate.boundListOps("user:").rightPop();
        logger.debug("userPop={}", userPop);
    }
}
