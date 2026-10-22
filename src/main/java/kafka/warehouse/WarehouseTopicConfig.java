package kafka.warehouse;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class WarehouseTopicConfig {
    @Bean
    public NewTopic warehouseTopic(){
        return TopicBuilder.name("warehouse").partitions(3).replicas(1).build();
    }
}
