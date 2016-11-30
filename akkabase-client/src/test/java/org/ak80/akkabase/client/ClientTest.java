package org.ak80.akkabase.client;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import org.ak80.akkabase.GetRequest;
import org.ak80.akkabase.MessageKt;
import org.ak80.akkabase.SetRequest;
import org.ak80.akkabase.test.EchoActor;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.ak80.akkabase.test.Builder.aKey;
import static org.ak80.akkabase.test.Builder.anUniqueInt;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@SuppressWarnings("PMD.MethodNamingConventions")
public class ClientTest {

  private static ActorSystem system;

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
  public void init_then_create_akkabasedb_actor() {
    // Given
    ActorSystem actorSystem = spy(system);
    String remoteAddress = "remote";

    // When
    new Client(actorSystem, remoteAddress);

    // Then
    verify(actorSystem).actorSelection(MessageKt.getDbActor(remoteAddress));
  }

  @Test
  public void set_then_send_SetRequest() throws InterruptedException, ExecutionException, TimeoutException {
    // Given
    ActorRef echoActor = system.actorOf(Props.create(EchoActor.class));
    Client client = new Client(system, system.actorSelection(echoActor.path()));

    SetRequest setRequest = new SetRequest(aKey(), anUniqueInt());

    // When
    CompletableFuture<SetRequest> future = (CompletableFuture) client.set(setRequest.getKey(), setRequest.getValue());

    // Then
    SetRequest echoRequest = future.get(1000, TimeUnit.MILLISECONDS);
    assertThat(echoRequest.toString(), is(setRequest.toString()));
  }

  @Test
  public void get_then_send_GetRequest() throws InterruptedException, ExecutionException, TimeoutException {
    // Given
    ActorRef echoActor = system.actorOf(Props.create(EchoActor.class));
    Client client = new Client(system, system.actorSelection(echoActor.path()));

    GetRequest getRequest = new GetRequest(aKey());

    // When
    CompletableFuture<GetRequest> future = (CompletableFuture) client.get(getRequest.getKey());

    // Then
    GetRequest echoRequest = future.get(1000, TimeUnit.MILLISECONDS);
    assertThat(echoRequest.toString(), is(getRequest.toString()));
  }

}