package org.ak80.akkabase;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import akka.testkit.TestActorRef;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.ak80.akkabase.test.Builder.aKey;
import static org.ak80.akkabase.test.Builder.anUniqueInt;
import static org.ak80.akkabase.test.LogAssert.assertLogInfo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class AkkabaseDbTest {

  static ActorSystem system;

  @BeforeClass
  public static void setup() {
    system = ActorSystem.create();
  }

  @AfterClass
  public static void teardown() {
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
    actorRef.tell(new SetMessage(key, value), ActorRef.noSender());

    // Then
    AkkabaseDb akkabaseDb = actorRef.underlyingActor();
    assertThat(akkabaseDb.getMap().get(key), is(value));
  }

  @Test
  public void receive_SetMessage_with_same_key_again_then_replace_key_value_in_map() {
    // Given
    TestActorRef<AkkabaseDb> actorRef = TestActorRef.create(system, Props.create(AkkabaseDb.class));
    String key = aKey();
    Integer value0 = anUniqueInt();
    Integer value1 = anUniqueInt();

    actorRef.tell(new SetMessage(key, value0), ActorRef.noSender());

    // When
    actorRef.tell(new SetMessage(key, value1), ActorRef.noSender());

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

    actorRef.tell(new SetMessage(key0, value0), ActorRef.noSender());

    // When
    actorRef.tell(new SetMessage(key1, value1), ActorRef.noSender());

    // Then
    AkkabaseDb akkabaseDb = actorRef.underlyingActor();
    assertThat(akkabaseDb.getMap().get(key0), is(value0));
    assertThat(akkabaseDb.getMap().get(key1), is(value1));
  }

  @Test
  public void receive_SetMessage_then_log() {
    // Given
    SetMessage setMessage = new SetMessage(aKey(), anUniqueInt());
    ActorRef actorRef = system.actorOf(Props.create(AkkabaseDb.class));

    // When
    Runnable when = () -> actorRef.tell(setMessage, ActorRef.noSender());

    // Then
    assertLogInfo(when, "received set request: " + setMessage, actorRef, system);
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

}