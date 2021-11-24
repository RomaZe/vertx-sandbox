package io.zoro.vertx.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerExample extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(WorkerExample.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new WorkerExample());
  }

  @Override
  public void start(Promise<Void> startPromise) {
    vertx.deployVerticle(new WorkerVerticle(), new DeploymentOptions()
      .setWorker(true)
      .setWorkerPoolSize(1)
      .setWorkerPoolName("my-worker-verticle")
    );
    startPromise.complete();
    executeBlockinCode();
  }

  private void executeBlockinCode() {
    vertx.executeBlocking(event -> {
      LOG.debug("Executing blocking call");
      try {
        Thread.sleep(5000); // Run some code here
        event.complete();
//        event.fail("Force failed");
      } catch (InterruptedException e) {
        LOG.error("Failed: ", e);
        event.fail(e);
      }
    }, result -> {
      if (result.succeeded()) {
        LOG.debug("Blocking call done");
      } else {
        LOG.debug("Blocking call failed due to:", result.cause());
      }
    });
  }

}
