package io.zoro.vertx.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PointToPointExample extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Sender());
    vertx.deployVerticle(new Receiver());
  }

  static class Sender extends AbstractVerticle {

    public static final Logger LOG = LoggerFactory.getLogger(Sender.class);

    @Override
    public void start(final Promise<Void> startPromise) throws Exception {
      String message = "Sending a message...";
      vertx.setPeriodic(1000, id -> {
        // Send message every 1 second
        vertx.eventBus().send(Sender.class.getName(), message);
      });
      startPromise.complete();
    }
  }

  static class Receiver extends  AbstractVerticle {

    public static final Logger LOG = LoggerFactory.getLogger(Receiver.class);

    @Override
    public void start(final Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<String>consumer(Sender.class.getName(), message -> {
        LOG.debug("Message received: {}", message.body());
      });

    }
  }


}
