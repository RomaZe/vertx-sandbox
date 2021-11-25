package io.zoro.vertx.jdbc;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainJdbcApi extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(MainJdbcApi.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainJdbcApi());
  }

  @Override
  public void start(final Promise<Void> startPromise) throws Exception {
    LOG.debug("Start {}", getClass().getName());
    vertx.deployVerticle(new VerticlePing());
    vertx.deployVerticle(VerticleJDBC.class, new DeploymentOptions().setInstances(5));
    vertx.deployVerticle(new BashExecutorVerticle(), new DeploymentOptions()
    .setWorker(true));
    startPromise.complete();
  }
}
