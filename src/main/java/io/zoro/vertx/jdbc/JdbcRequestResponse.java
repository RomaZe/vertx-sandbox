package io.zoro.vertx.jdbc;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcRequestResponse extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(JdbcRequestResponse.class);


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
      JsonObject message = new JsonObject()
        .put("message", "Hello, World!")
        .put("version", 1);
      EventBus eventBus = vertx.eventBus();
      LOG.debug("Sending: {}", message);
      eventBus.<JsonArray>request(ADDRESS, message, reply -> {
        LOG.debug("Response: {}", reply.result().body());
      });

    }
  }

  static class ResponseVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(RequestVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<JsonArray>consumer(RequestVerticle.ADDRESS, message -> {
        LOG.debug("Received message: {}", message.body());
        message.reply(new JsonArray().add("one").add("two").add("three"));
      });
    }
  }
}
