package br.com.b3.eventhub;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.task.TaskExecutor;

@SpringBootApplication
public class EventHubApplication implements CommandLineRunner {

  @Autowired
  private TaskExecutor taskExecutor;
  @Autowired
  private ObjectProvider<ProducerTask> producerTask;

  public static void main(String[] args) {
    SpringApplication.run(EventHubApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    Integer numMessages = 10;
    if (args.length == 1) {
      numMessages = Integer.parseInt(args[0]);
    }

    int cores = Runtime.getRuntime().availableProcessors();

    for (int i = 0; i < cores; i++) {
       var producer = producerTask.getObject(numMessages, i);
       taskExecutor.execute(producer);
    }
  }
}
