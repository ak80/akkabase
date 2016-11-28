package org.ak80.akkabase;

import akka.actor.*;
import akka.japi.pf.ReceiveBuilder;
import akka.testkit.JavaTestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import scala.PartialFunction;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.ak80.akkabase.test.FutureTools.askFuture;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SandBoxTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private static ActorSystem system;

  private ActorRef actorRef;

  @BeforeClass
  public static void setUp() {
    system = ActorSystem.create();
  }

  @AfterClass
  public static void tearDown() {
    JavaTestKit.shutdownActorSystem(system);
    system = null;
  }

  @Test
  public void shouldReplyToPingWithPong() throws Exception {
    // Given
    actorRef = system.actorOf(PongActor.create());

    // When
    CompletableFuture<String> jFuture = askPong("Ping");

    // Then
    assertEquals("Pong", jFuture.get(1000, TimeUnit.MILLISECONDS));
  }

  @Test
  public void shouldReplyToUnknownMessageWithFailure() throws Exception {
    // Given
    actorRef = system.actorOf(PongActor.create());

    // Then
    expectedException.expect(ExecutionException.class);

    // When
    CompletableFuture<String> jFuture = askPong("unknown");
    jFuture.get(1000, TimeUnit.MILLISECONDS);
    fail("not reachable");
  }

  @Test
  public void printToConsole() throws InterruptedException {
    actorRef = system.actorOf(PongActor.create());
    askPong("Ping").thenAccept(x -> System.out.println("replied with: " + x));
    Thread.sleep(1000);
  }

  public CompletableFuture<String> askPong(String message) {
    return askFuture(message, actorRef);
  }

  public static class PongActor extends AbstractActor {

    public static Props create() {
      return Props.create(PongActor.class);
    }

    @Override
    public PartialFunction receive() {
      return ReceiveBuilder
          .matchEquals("Ping", s -> sender().tell("Pong", ActorRef.noSender()))
          .matchAny(x -> sender().tell(new Status.Failure(new Exception("unknown message")), self()))
          .build();
    }

  }


}

