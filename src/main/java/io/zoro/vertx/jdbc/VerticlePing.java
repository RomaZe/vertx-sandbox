package io.zoro.vertx.jdbc;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.zoro.vertx.verticles.VerticleN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerticlePing extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(VerticlePing.class);

  @Override
  public void start(final Promise<Void> startPromise) {
    vertx.setPeriodic(500, id -> {
      LOG.debug("Run ping from {} ", getClass().getSimpleName());
    });
    startPromise.complete();
  }
}

