package org.ak80.akkabase.client;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import org.ak80.akkabase.GetRequest;
import org.ak80.akkabase.MessageKt;
import org.ak80.akkabase.SetRequest;

import java.util.concurrent.CompletionStage;

import static akka.pattern.Patterns.ask;
import static scala.compat.java8.FutureConverters.toJava;

public class Client {

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
  }

  public CompletionStage set(String key, Object value) {
    return toJava(ask(remoteDb, new SetRequest(key, value), 2000));
  }

  public CompletionStage<Object> get(String key) {
    return toJava(ask(remoteDb, new GetRequest(key), 2000));
  }

}
