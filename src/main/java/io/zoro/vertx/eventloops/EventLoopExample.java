package io.zoro.vertx.eventloops;

import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class EventLoopExample extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(EventLoopExample.class);

  public static void main(String[] args) {

    Vertx vertx = Vertx.vertx(
      new VertxOptions()
      .setMaxEventLoopExecuteTime(500)
      .setMaxEventLoopExecuteTimeUnit(TimeUnit.MILLISECONDS)
      .setBlockedThreadCheckInterval(1)
      .setBlockedThreadCheckIntervalUnit(TimeUnit.SECONDS)
      .setEventLoopPoolSize(2)  // Restrict Number of instances. Will be run only 2 instead 5
    );
    vertx.deployVerticle(EventLoopExample.class.getName(), new DeploymentOptions()
      .setInstances(5)
    );
  }

  @Override
  public void start(Promise<Void> startPromise) throws InterruptedException {
    LOG.debug("Start {}", getClass().getName());
    startPromise.complete();

    // Don't do it
//    Thread.sleep(5000);
  }
}
