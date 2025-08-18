package de.unistuttgart.iste.meitrex.common.config;

import de.unistuttgart.iste.meitrex.common.dapr.TopicPublisher;
import io.dapr.client.DaprClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicPublisherConfiguration {
    @Bean
    public TopicPublisher topicPublisher() {
        return new TopicPublisher(new DaprClientBuilder().build());
    }
}