package org.ak80.akkabase;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import akka.testkit.TestActorRef;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

import static org.ak80.akkabase.test.Builder.aKey;
import static org.ak80.akkabase.test.Builder.anUniqueInt;
import static org.ak80.akkabase.test.FutureTools.*;
import static org.ak80.akkabase.test.LogAssert.assertLogInfo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class AkkabaseDbTest {

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
  public void receive_SetMessage_then_place_key_value_in_map() {
    // Given
    TestActorRef<AkkabaseDb> actorRef = TestActorRef.create(system, AkkabaseDb.create());
    String key = aKey();
    Integer value = anUniqueInt();

    // When
    actorRef.tell(new SetRequest(key, value), ActorRef.noSender());

    // Then
    AkkabaseDb akkabaseDb = actorRef.underlyingActor();
    assertThat(akkabaseDb.getMap().get(key), is(value));
  }

  @Test
  public void receive_SetMessage_then_reply_with_success() throws Exception {
    // Given
    TestActorRef<AkkabaseDb> actorRef = TestActorRef.create(system, AkkabaseDb.create());
    String key = aKey();
    Integer value = anUniqueInt();

    // When
    String successKey = askReply(new SetRequest(key, value), actorRef);

    // Then
    assertThat(successKey, is(key));
  }

  @Test
  public void receive_SetMessage_with_same_key_again_then_replace_key_value_in_map() {
    // Given
    TestActorRef<AkkabaseDb> actorRef = TestActorRef.create(system, Props.create(AkkabaseDb.class));
    String key = aKey();
    Integer value0 = anUniqueInt();
    Integer value1 = anUniqueInt();

    actorRef.tell(new SetRequest(key, value0), ActorRef.noSender());

    // When
    actorRef.tell(new SetRequest(key, value1), ActorRef.noSender());

    // Then
    AkkabaseDb akkabaseDb = actorRef.underlyingActor();
    assertThat(akkabaseDb.getMap().get(key), is(value1));
  }

  @Test
  public void receive_SetMessage_with_other_key_then_have_key_value_in_map() {
    // Given
    TestActorRef<AkkabaseDb> actorRef = TestActorRef.create(system, Props.create(AkkabaseDb.class));
    String key0 = aKey();
    String key1 = aKey();
    Integer value0 = anUniqueInt();
    Integer value1 = anUniqueInt();

    actorRef.tell(new SetRequest(key0, value0), ActorRef.noSender());

    // When
    actorRef.tell(new SetRequest(key1, value1), ActorRef.noSender());

    // Then
    AkkabaseDb akkabaseDb = actorRef.underlyingActor();
    assertThat(akkabaseDb.getMap().get(key0), is(value0));
    assertThat(akkabaseDb.getMap().get(key1), is(value1));
  }

  @Test
  public void receive_SetMessage_then_log() {
    // Given
    SetRequest setRequest = new SetRequest(aKey(), anUniqueInt());
    ActorRef actorRef = system.actorOf(Props.create(AkkabaseDb.class));

    // When
    Runnable when = () -> actorRef.tell(setRequest, ActorRef.noSender());

    // Then
    assertLogInfo(when, "received set request: " + setRequest, actorRef, system);
  }

  @Test
  public void receive_GetMessage_when_key_exists_then_reply_with_value() throws Exception {
    // Given
    TestActorRef<AkkabaseDb> actorRef = TestActorRef.create(system, AkkabaseDb.create());
    String key = aKey();
    Integer value = anUniqueInt();
    tell(new SetRequest(key, value), actorRef);

    // When
    Object successValue = askReply(new GetRequest(key), actorRef);

    // Then
    assertThat(successValue, is(value));
  }

  @Test
  public void receive_GetMessage_then_log() throws Exception {
    // Given
    TestActorRef<AkkabaseDb> actorRef = TestActorRef.create(system, AkkabaseDb.create());
    String key = aKey();
    Integer value = anUniqueInt();
    tell(new SetRequest(key, value), actorRef);

    GetRequest getRequest = new GetRequest(key);

    // When
    Runnable when = () -> askReply(getRequest, actorRef);

    // Then
    assertLogInfo(when, "received get request: " + getRequest, actorRef, system);
  }

  @Test
  public void receive_GetMessage_when_key_not_exists_then_not_found_exception() throws Exception {
    // Given
    TestActorRef<AkkabaseDb> actorRef = TestActorRef.create(system, AkkabaseDb.create());
    String key = aKey();

    // When
    CompletableFuture future = askFuture(new GetRequest(key), actorRef);

    // Then
    KeyNotFoundException failure = getFail(future);
    assertThat(failure.getKey(), is(key));
  }

  @Test
  public void receive_unknown_message_then_log() {
    // Given
    ActorRef actorRef = system.actorOf(Props.create(AkkabaseDb.class));

    // When
    Runnable when = () -> actorRef.tell(aKey(), ActorRef.noSender());

    // Then
    assertLogInfo(when, "received unknown message ", actorRef, system);
  }

  @Test
  public void receive_SetIfNotExistsMessage_and_not_exists_then_place_key_value_in_map() {
    // Given
    TestActorRef<AkkabaseDb> actorRef = TestActorRef.create(system, AkkabaseDb.create());
    String key = aKey();
    Integer value = anUniqueInt();

    // When
    actorRef.tell(new SetIfNotExistsRequest(key, value), ActorRef.noSender());

    // Then
    AkkabaseDb akkabaseDb = actorRef.underlyingActor();
    assertThat(akkabaseDb.getMap().get(key), is(value));
  }

  @Test
  public void receive_SetIfNotExistsMessage_and_not_exists_return_value() {
    // Given
    TestActorRef<AkkabaseDb> actorRef = TestActorRef.create(system, AkkabaseDb.create());
    Integer value = anUniqueInt();

    // When
    Object successValue = askReply(new SetIfNotExistsRequest(aKey(), value), actorRef);

    // Then
    assertThat(successValue, is(value));
  }

  @Test
  public void receive_SetIfNotExistsMessage_and_exists_then_dont_place_key_value_in_map() {
    // Given
    TestActorRef<AkkabaseDb> actorRef = TestActorRef.create(system, AkkabaseDb.create());
    String key = aKey();
    Integer value0 = anUniqueInt();
    Integer value1 = anUniqueInt();
    actorRef.tell(new SetRequest(key, value0), ActorRef.noSender());

    // When
    actorRef.tell(new SetIfNotExistsRequest(key, value1), ActorRef.noSender());

    // Then
    AkkabaseDb akkabaseDb = actorRef.underlyingActor();
    assertThat(akkabaseDb.getMap().get(key), is(value0));
  }

  @Test
  public void receive_SetIfNotExistsMessage_exists_then_return_old_value() {
    // Given
    TestActorRef<AkkabaseDb> actorRef = TestActorRef.create(system, AkkabaseDb.create());
    String key = aKey();
    Integer value0 = anUniqueInt();
    Integer value1 = anUniqueInt();
    actorRef.tell(new SetRequest(key, value0), ActorRef.noSender());

    // When
    Object successValue = askReply(new SetIfNotExistsRequest(key, value1), actorRef);

    // Then
    assertThat(successValue, is(value0));
  }

  @Test
  public void receive_SetIfNotExistsMessage_then_log() {
    // Given
    TestActorRef<AkkabaseDb> actorRef = TestActorRef.create(system, AkkabaseDb.create());
    String key = aKey();
    Integer value = anUniqueInt();

    SetIfNotExistsRequest setRequest = new SetIfNotExistsRequest(key, value);

    // When
    Runnable when = () -> actorRef.tell(setRequest, ActorRef.noSender());
    ;

    // Then
    assertLogInfo(when, "received set-if-not-exists request: " + setRequest, actorRef, system);
  }

  @Test
  public void receive_DeleteMessage_and_exists_then_deleted_in_map() {
    // Given
    TestActorRef<AkkabaseDb> actorRef = TestActorRef.create(system, AkkabaseDb.create());
    String key = aKey();
    Integer value = anUniqueInt();
    actorRef.tell(new SetRequest(key, value), ActorRef.noSender());

    // When
    actorRef.tell(new DeleteRequest(key), ActorRef.noSender());

    // Then
    AkkabaseDb akkabaseDb = actorRef.underlyingActor();
    assertThat(akkabaseDb.getMap().get(key), is(nullValue()));
  }

  @Test
  public void receive_DeleteMessage_and_exists_then_return_true() {
    // Given
    TestActorRef<AkkabaseDb> actorRef = TestActorRef.create(system, AkkabaseDb.create());
    String key = aKey();
    Integer value = anUniqueInt();
    actorRef.tell(new SetRequest(key, value), ActorRef.noSender());

    // When
    Object successValue = askReply(new DeleteRequest(key), actorRef);

    // Then
    assertThat(successValue, is(true));
  }

  @Test
  public void receive_DeleteMessage_and_not_exists_then_return_false() {
    // Given
    TestActorRef<AkkabaseDb> actorRef = TestActorRef.create(system, AkkabaseDb.create());

    // When
    Object successValue = askReply(new DeleteRequest(aKey()), actorRef);

    // Then
    assertThat(successValue, is(false));
  }

  @Test
  public void receive_DeleteMessage_then_log() {
    // Given
    TestActorRef<AkkabaseDb> actorRef = TestActorRef.create(system, AkkabaseDb.create());
    String key = aKey();
    Integer value = anUniqueInt();
    actorRef.tell(new SetRequest(key, value), ActorRef.noSender());

    DeleteRequest deleteRequest = new DeleteRequest(key);

    // When
    Runnable when = () -> actorRef.tell(deleteRequest, ActorRef.noSender());

    // Then
    assertLogInfo(when, "received delete request: " + deleteRequest, actorRef, system);
  }

}