package br.com.b3.eventhub;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
class TaskExecutorBean {

  @Bean
  public TaskExecutor taskExecutor() {
    var cores = EventHubApplication.cores;
    var executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(cores);
    executor.setMaxPoolSize(cores);
    executor.setQueueCapacity(cores);
    return executor;
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_PROTOTYPE)
  ProducerTask producerTask(int numMessages, int id) {
    return new ProducerTask(numMessages, id);
  }

}
