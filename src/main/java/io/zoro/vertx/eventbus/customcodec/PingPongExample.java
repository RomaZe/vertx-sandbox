package io.zoro.vertx.eventbus.customcodec;

import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import io.zoro.vertx.worker.WorkerExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingPongExample extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(PingPongExample.class);


  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new PingVerticle(), LogOnError());
    vertx.deployVerticle(new PongVerticle(), LogOnError());
  }

  private static Handler<AsyncResult<String>> LogOnError() {
    return ar -> {
      if (ar.failed()) {
        LOG.error("err", ar.cause());
      }
    };
  }

  static class PingVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(PingVerticle.class);
    public static final String ADDRESS = "my.request.address";

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      // Register codec only once
      EventBus eventBus = vertx.eventBus();
      eventBus.registerDefaultCodec(Ping.class, new LocalMessageCodec<>(Ping.class));

      Ping message = new Ping("Hello, World!", true);
      LOG.debug("Sending: {}", message);
      eventBus.<Pong>request(ADDRESS, message, reply -> {
        if (reply.failed()) {
          LOG.error("Failed: ", reply.cause());
          return;
        }
        LOG.debug("Response: {}", reply.result().body());
      });
      startPromise.complete();
    }
  }

  static class PongVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(PongVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      // Register codec only once
      EventBus eventBus = vertx.eventBus();
      eventBus.registerDefaultCodec(Pong.class, new LocalMessageCodec<>(Pong.class));

      eventBus.<Ping>consumer(PingVerticle.ADDRESS, message -> {
        LOG.debug("Received message: {}", message.body());
        message.reply(new Pong(0));
      }).exceptionHandler(error -> {
        LOG.error("Error: ", error);
      });
      startPromise.complete();
    }
  }
}
