package cn.libofeng.air.examples.redis;

import cn.libofeng.air.examples.redis.Example;
import cn.libofeng.air.examples.redis.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URL;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/spring-context.xml")
@ActiveProfiles("development")
public class ExampleXmlTest {

    @Autowired
    private Example example;

    @Test
    public void testAddLink() throws Exception {

        example.addLink("userId", new URL("Http://www.baidu.com"));

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName("TestUser");
        user.setPassword("password");

        example.addUser(user);
    }
}