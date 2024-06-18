package org.example.emailservice;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import static org.mockito.Mockito.mock;

@TestConfiguration
public class ElasticsearchTestConfiguration {

    @Bean
    @Primary
    public ElasticsearchTemplate elasticsearchTemplate()
    {
        return mock(ElasticsearchTemplate.class);
    }
}
