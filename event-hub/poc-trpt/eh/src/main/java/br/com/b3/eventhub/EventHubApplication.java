package br.com.b3.eventhub;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.task.TaskExecutor;
import org.springframework.util.StopWatch;

@SpringBootApplication
public class EventHubApplication implements CommandLineRunner {

  @Autowired
  private TaskExecutor taskExecutor;
  @Autowired
  private ObjectProvider<ProducerTask> producerTask;

  public static int threads = Runtime.getRuntime().availableProcessors();
  public static CountDownLatch cdl;

  public static void main(String[] args) {
    if (args.length == 2) {
      threads = threads * Integer.parseInt(args[1]);
    }
    cdl = new CountDownLatch(threads);
    SpringApplication.run(EventHubApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    int numMessages = 10;
    if (args.length == 2) {
      numMessages = Integer.parseInt(args[0]);
    }

    var sw = new StopWatch();
    sw.start();
    for (int i = 0; i < threads; i++) {
      var producer = producerTask.getObject(numMessages, i);
      taskExecutor.execute(producer);
    }

    cdl.await();
    sw.stop();
    Logger logger = LoggerFactory.getLogger(EventHubApplication.class);
    int total = numMessages * threads;
    var elapsed = sw.getTotalTimeMillis();
    double rate = 60000. * total / elapsed;
    logger.info("Final: Sent {} messages in {}ms, {}/min", total, sw.getTotalTimeMillis(), rate);
    System.exit(0);
  }
}
