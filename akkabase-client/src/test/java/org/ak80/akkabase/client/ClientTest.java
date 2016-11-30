package org.ak80.akkabase.client;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import org.ak80.akkabase.*;
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

  @Test
  public void setIfNoExists_then_send_SetIfNotExistsRequest() throws InterruptedException, ExecutionException, TimeoutException {
    // Given
    ActorRef echoActor = system.actorOf(Props.create(EchoActor.class));
    Client client = new Client(system, system.actorSelection(echoActor.path()));

    SetIfNotExistsRequest setRequest = new SetIfNotExistsRequest(aKey(), anUniqueInt());

    // When
    CompletableFuture<SetIfNotExistsRequest> future = (CompletableFuture) client.setIfNotExists(setRequest.getKey(), setRequest.getValue());

    // Then
    SetIfNotExistsRequest echoRequest = future.get(1000, TimeUnit.MILLISECONDS);
    assertThat(echoRequest.toString(), is(setRequest.toString()));
  }


  @Test
  public void delete_then_send_DeleteRequest() throws InterruptedException, ExecutionException, TimeoutException {
    // Given
    ActorRef echoActor = system.actorOf(Props.create(EchoActor.class));
    Client client = new Client(system, system.actorSelection(echoActor.path()));

    DeleteRequest deleteRequest = new DeleteRequest(aKey());

    // When
    CompletableFuture<DeleteRequest> future = (CompletableFuture) client.delete(deleteRequest.getKey());

    // Then
    DeleteRequest echoRequest = future.get(1000, TimeUnit.MILLISECONDS);
    assertThat(echoRequest.toString(), is(deleteRequest.toString()));
  }


}