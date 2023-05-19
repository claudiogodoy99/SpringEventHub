package br.com.b3.eventhub;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import reactor.kafka.sender.SenderOptions;

@Configuration
class Beans {

  @Bean
  public TaskExecutor taskExecutor() {
    var threads = EventHubApplication.threads;
    var executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(threads);
    executor.setMaxPoolSize(threads);
    executor.setQueueCapacity(threads);
    return executor;
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_PROTOTYPE)
  ProducerTask producerTask(int numMessages, int id) {
    return new ProducerTask(numMessages, id);
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_PROTOTYPE)
  public ReactiveKafkaProducerTemplate<Integer, String> reactiveKafkaProducerTemplate(
      KafkaProperties properties) {
    return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(
        properties.buildProducerProperties()));
  }
}
