package org.ak80.akkabase.client;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.ak80.akkabase.*;

import java.util.concurrent.CompletionStage;

import static akka.pattern.Patterns.ask;
import static scala.compat.java8.FutureConverters.toJava;

public class Client {

  private static final int TIME_OUT = 2000;

  private final ActorSystem system;

  private final ActorSelection remoteDb;

  public Client(String remoteAddress) {
    this(ActorSystem.create("LocalSystem"), remoteAddress);
  }

  public Client(ActorSystem system, String remoteAddress) {
    this(system, system.actorSelection(MessageKt.getDbActor(remoteAddress)));
  }

  public Client(ActorSystem system, ActorSelection actorSelection) {
    this.system = system;
    this.remoteDb = actorSelection;

    LoggingAdapter log = Logging.getLogger(system, this);
    log.info("Started client with actor " + actorSelection.pathString());
  }

  public CompletionStage set(String key, Object value) {
    return sendRequest(new SetRequest(key, value));
  }

  public CompletionStage<Object> get(String key) {
    return sendRequest(new GetRequest(key));
  }

  public CompletionStage setIfNotExists(String key, Object value) {
    return sendRequest(new SetIfNotExistsRequest(key, value));
  }

  public CompletionStage delete(String key) {
    return sendRequest(new DeleteRequest(key));
  }

  private CompletionStage<Object> sendRequest(Object object) {
    return toJava(ask(remoteDb, object, TIME_OUT));
  }

  protected ActorSystem getActorSystem() {
    return system;
  }

}
