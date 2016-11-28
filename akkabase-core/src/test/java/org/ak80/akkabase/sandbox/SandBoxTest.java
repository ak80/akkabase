package org.ak80.akkabase.sandbox;

import akka.actor.*;
import akka.japi.pf.ReceiveBuilder;
import akka.testkit.JavaTestKit;
import org.ak80.akkabase.test.FutureTools;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import scala.PartialFunction;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SandBoxTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private static ActorSystem system;

  private ActorRef actorRef;

  private static final String PING = "Ping";

  private static final String PONG = "Pong";

  private static final int TIME_OUT = 1000;

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
    CompletableFuture<String> jFuture = askPong(PING);

    // Then
    assertEquals(PONG, jFuture.get(TIME_OUT, TimeUnit.MILLISECONDS));
  }

  @Test
  public void shouldReplyToUnknownMessageWithFailure() throws Exception {
    // Given
    actorRef = system.actorOf(PongActor.create());

    // Then
    expectedException.expect(ExecutionException.class);

    // When
    CompletableFuture<String> jFuture = askPong("unknown");
    jFuture.get(TIME_OUT, TimeUnit.MILLISECONDS);
    fail("not reachable");
  }

  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  @Test
  public void printToConsole() throws InterruptedException {
    actorRef = system.actorOf(PongActor.create());
    askPong(PING).thenAccept(x -> System.out.println("replied with: " + x));
    Thread.sleep(TIME_OUT);
  }

  public CompletableFuture<String> askPong(String message) {
    return FutureTools.askFuture(message, actorRef);
  }

  public static class PongActor extends AbstractActor {

    public static Props create() {
      return Props.create(PongActor.class);
    }

    @Override
    public PartialFunction receive() {
      return ReceiveBuilder
          .matchEquals(PING, s -> sender().tell(PONG, ActorRef.noSender()))
          .matchAny(x -> sender().tell(new Status.Failure(new Exception("unknown message")), self()))
          .build();
    }

  }


}

