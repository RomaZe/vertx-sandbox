package io.zoro.vertx.jdbc;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.jdbcclient.*;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerticleJDBC extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(VerticleJDBC.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.debug("Start {}", getClass().getName());

    try {
      Class.forName("org.postgresql.Driver");
    } catch (Exception e) {
      LOG.error("Upload driver class failed: ", e);
      return;
    }

    JDBCPool pool = JDBCPool.pool(vertx, getJDBCConnectOptions(), new PoolOptions().setMaxSize(16));

    LOG.debug("Run SQL query....");
    Long startTime = System.nanoTime();
    pool
      .query("select count(*) as amount from table11;")
      .execute()
      .onFailure(e -> {
        LOG.debug("Connection failed: {}", e);
      })
      .onSuccess(rows -> {
        for (Row row : rows) {
          LOG.debug("Number of rows: {}", row.getInteger("amount"));
        }
        long elapsedTime = System.nanoTime() - startTime;
        LOG.debug("Finished SQL query in " + (elapsedTime / 1000000000.0) + " sec" );
      });
  }

  private JDBCConnectOptions getJDBCConnectOptions() {
    return new JDBCConnectOptions()
      .setJdbcUrl("jdbc:postgresql://10.92.16.33:5432/adb")
      .setUser("postgres")
      .setPassword("123");
  }


}
