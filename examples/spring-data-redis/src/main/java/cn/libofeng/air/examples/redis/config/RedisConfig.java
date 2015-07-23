package cn.libofeng.air.examples.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.LinkedList;
import java.util.List;

@Configuration
@ComponentScan(
        basePackages = "cn.libofeng.air.examples.redis",
        excludeFilters = {@ComponentScan.Filter(Configuration.class)})
@PropertySource(name = "configProperties", value = {"classpath:config/redis.properties"})
public class RedisConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Value("${redis.pool.maxTotal}")
    private int poolMaxTotal;

    @Value("${redis.pool.maxIdle}")
    private int poolMaxIdle;

    @Value("${redis.pool.maxWaitMillis}")
    private int poolMaxWaitMillis;

    @Value("${redis.pool.testOnBorrow}")
    private boolean poolTestOnBorrow;


    @Value("${redis.shard.01.host}")
    private String shard01Host;


    @Value("${redis.shard.01.port}")
    private int shard01Port;


    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(poolMaxTotal);
        config.setMaxIdle(poolMaxIdle);
        config.setMaxWaitMillis(poolMaxWaitMillis);
        config.setTestOnBorrow(poolTestOnBorrow);
        return config;
    }


    @Bean
    public JedisShardInfo shardInfo01() {
        return new JedisShardInfo(shard01Host, shard01Port);
    }

    @Bean
    public ShardedJedisPool shardedJedisPool() {
        List<JedisShardInfo> shardInfoList = new LinkedList<JedisShardInfo>();
        shardInfoList.add(shardInfo01());

        return new ShardedJedisPool(jedisPoolConfig(), shardInfoList);
    }


    @Bean
    public JedisConnectionFactory jedisConnFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory(jedisPoolConfig());
        factory.setUsePool(true);
        factory.setShardInfo(shardInfo01());
        return factory;
    }

    @Bean
    public RedisTemplate redisTemplate() {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(jedisConnFactory());
        return template;
    }

}
