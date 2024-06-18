package org.example.emailservice;

import org.example.emailservice.ElasticsearchTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {ProfItSoftJavaEmailApplication.class, ElasticsearchTestConfiguration.class})
class ProfItSoftJavaEmailApplicationTests {

    @Test
    void contextLoads() {
    }
}
