package io.zoro.vertx.eventbus.customcodec;

public class Pong {

  private int id;

  // Default constructor
  public Pong() {

  }

  public Pong(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Pong{" +
      "id=" + id +
      '}';
  }
}
