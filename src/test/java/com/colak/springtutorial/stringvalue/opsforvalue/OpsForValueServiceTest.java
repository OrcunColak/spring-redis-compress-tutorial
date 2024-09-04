package com.colak.springtutorial.stringvalue.opsforvalue;

import com.colak.springtutorial.model.User;
import com.colak.springtutorial.stringvalue.OpsForValueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class OpsForValueServiceTest {

    @Autowired
    private OpsForValueService opsForValueService;

    @Test
    void testDelete() {
        String key = "key1";
        User user = new User("test");

        opsForValueService.delete(key);

        Boolean result = opsForValueService.setIfAbsent(key, user, 5L, TimeUnit.SECONDS);
        assertEquals(Boolean.TRUE, result);

        User getValue = (User) opsForValueService.getAndExpire(key, Duration.ofMinutes(1));
        assertEquals("test", getValue.name());
    }

}
