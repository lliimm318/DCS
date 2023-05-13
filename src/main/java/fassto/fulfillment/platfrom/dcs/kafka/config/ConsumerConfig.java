package fassto.fulfillment.platfrom.dcs.kafka.config;

import fassto.fulfillment.platfrom.dcs.kafka.request.KafkaMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ConsumerConfig implements KafkaListenerConfigurer {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.key-deserializer}")
    private String keyDeSerializer;

    @Value("${spring.kafka.consumer.customer.ack-mode}")
    private ContainerProperties.AckMode ackMode;

    @Value("${spring.kafka.consumer.customer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${spring.kafka.consumer.customer.poll-time-out}")
    private int pollTimeOut;

    @Value("${spring.kafka.consumer.customer.concurrency}")
    private int concurrency;

    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializer;

    private final LocalValidatorFactoryBean validator;

    // payload validation
    @Override
    public void configureKafkaListeners(KafkaListenerEndpointRegistrar registrar) {
        registrar.setValidator(this.validator);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, KafkaMessageRequest>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KafkaMessageRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ackMode);
        factory.setConcurrency(concurrency);
        factory.getContainerProperties().setPollTimeout(pollTimeOut);
        factory.setReplyTemplate(replyTemplate());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, KafkaMessageRequest> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        return Map.of(
                org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class,
                org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class,
                ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, keyDeSerializer,
                ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class,
                org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset,
                JsonDeserializer.TRUSTED_PACKAGES, "*"
        );
    }

    @Bean
    public KafkaTemplate<String, KafkaMessageRequest> replyTemplate() {
        return new KafkaTemplate<>(replyProducerFactory());
    }

    @Bean
    public ProducerFactory<String, KafkaMessageRequest> replyProducerFactory() {
        return new DefaultKafkaProducerFactory<>(replyProducerConfigs());
    }

    @Bean
    public Map<String, Object> replyProducerConfigs() {
        return Map.of(
                org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer,
                org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );
    }

    

}
