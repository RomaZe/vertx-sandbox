package io.zoro.vertx.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.zoro.vertx.worker.WorkerExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestResponseExample extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(RequestResponseExample.class);


  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new RequestVerticle());
    vertx.deployVerticle(new ResponseVerticle());
  }

  static class RequestVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(RequestVerticle.class);
    public static final String ADDRESS = "my.request.address";

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      String message = "Hello, World!";
      EventBus eventBus = vertx.eventBus();
      LOG.debug("Sending: {}", message);
      eventBus.<String>request(ADDRESS, message, reply -> {
        LOG.debug("Response: {}", reply.result().body());
      });

    }
  }

  static class ResponseVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(RequestVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<String>consumer(RequestVerticle.ADDRESS, message -> {
        LOG.debug("Received message: {}", message.body());
        message.reply("Received your message. Thanks!");
      });
    }
  }
}
