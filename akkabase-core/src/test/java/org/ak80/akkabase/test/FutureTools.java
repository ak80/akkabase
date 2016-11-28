package org.ak80.akkabase.test;

import akka.actor.ActorRef;
import scala.concurrent.Future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static akka.pattern.Patterns.ask;
import static scala.compat.java8.FutureConverters.toJava;

@SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
public class FutureTools {

  public static void tell(Object message, ActorRef actorRef) {
    actorRef.tell(message, ActorRef.noSender());
  }

  public static <T> CompletableFuture<T> askFuture(Object message, ActorRef actorRef) {
    Future sFuture = ask(actorRef, message, 1000);
    CompletionStage<T> cs = toJava(sFuture);
    return (CompletableFuture<T>) cs;
  }

  public static <T> T askReply(Object message, ActorRef actorRef) {
    try {
      return (T) askFuture(message, actorRef).get();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T getFail(CompletableFuture future) throws InterruptedException, java.util.concurrent.ExecutionException {
    return (T) future.handle((success, fail) -> fail).get();
  }


}
