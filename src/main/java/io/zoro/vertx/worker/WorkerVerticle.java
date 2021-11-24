package io.zoro.vertx.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(WorkerVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws InterruptedException {
    startPromise.complete();
    LOG.debug("Deployed as a Worker Verticle {}", WorkerVerticle.class.getName());
    Thread.sleep(10000);
    LOG.debug("Worker verticle finished its job");
  }
}
