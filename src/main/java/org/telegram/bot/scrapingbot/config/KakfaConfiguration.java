package org.telegram.bot.scrapingbot.config;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.telegram.bot.scrapingbot.model.kafka.AvailableSizesRequest;
import org.telegram.bot.scrapingbot.model.kafka.AvailableSizesResponse;
import org.telegram.bot.scrapingbot.model.kafka.GarmentAdvice;
import org.telegram.bot.scrapingbot.util.Constants;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KakfaConfiguration {

    @Value("${kafka.consumer}")
    private String consumer;

    @Value("${kafka.server}")
    private String server;

    @Value("${kafka.jaas}")
    private String jaasConfig;

    private <T> ConsumerFactory<String, T> consumerFactory(Class<T> messageType) {
        JsonDeserializer<T> deserializer = new JsonDeserializer<>(messageType);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, consumer);
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, Constants.KAFKA_OFFSET_CONFIG);
        config.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, Constants.KAFKA_PROTOCOL);
        config.put(SaslConfigs.SASL_MECHANISM, Constants.KAFKA_MECHANISM);
        config.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);

        return new DefaultKafkaConsumerFactory(config, new StringDeserializer(), deserializer);
    }

    private <T> ProducerFactory<String, T> producerFactory(Class<T> messageType) {
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        config.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, Constants.KAFKA_PROTOCOL);
        config.put(SaslConfigs.SASL_MECHANISM, Constants.KAFKA_MECHANISM);
        config.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AvailableSizesRequest> availableSizesConsumerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AvailableSizesRequest> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(AvailableSizesRequest.class));
        factory.setBatchListener(true);
        return factory;
    }

    @Bean
    public KafkaTemplate<String, GarmentAdvice> garmentProducerFactory() {
        return new KafkaTemplate<>(producerFactory(GarmentAdvice.class));
    }

    @Bean
    public KafkaTemplate<String, AvailableSizesResponse> availableSizesResponseProducerFactory() {
        return new KafkaTemplate<>(producerFactory(AvailableSizesResponse.class));
    }
}
