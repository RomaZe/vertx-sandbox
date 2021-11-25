package io.zoro.vertx.jdbc;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BashExecutorVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(BashExecutorVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.debug("Run bash command...");
    cmdExecute();
    startPromise.complete();
  }

  private void cmdExecute() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    processBuilder.command("bash", "-c", "dd if=/dev/zero of=/tmp/diskbench1 bs=1M count=10024 conv=fdatasync oflag=direct");

    // Run a shell script
    //processBuilder.command("path/to/hello.sh");

    try {

      Process process = processBuilder.start();

      StringBuilder output = new StringBuilder();

      BufferedReader reader = new BufferedReader(
        new InputStreamReader(process.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line + "\n");
      }

      int exitVal = process.waitFor();
      if (exitVal == 0) {
        LOG.debug("Bash command finished with success");
//        System.out.println(output);
//        System.exit(0);
      } else {
        //abnormal...
      }

    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
