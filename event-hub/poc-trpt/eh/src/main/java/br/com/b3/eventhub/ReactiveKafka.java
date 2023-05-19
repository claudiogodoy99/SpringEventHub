package br.com.b3.eventhub;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.sender.SenderOptions;

@Configuration
public class ReactiveKafka {
  @Bean
  @Scope(BeanDefinition.SCOPE_PROTOTYPE)
  public ReactiveKafkaProducerTemplate<Integer, String> reactiveKafkaProducerTemplate(
      KafkaProperties properties) {
    return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(
        properties.buildProducerProperties()));
  }
}