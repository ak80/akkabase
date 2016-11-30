package org.ak80.akkabase.client;

import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ClientIsolatedTest {

  private ActorSystem system;

  @After
  public void tearDown() {
    if (system != null) {
      JavaTestKit.shutdownActorSystem(system);
      system = null;
    }
  }

  @Test
  public void init_then_create_system_and_akkabasedb_actor() {
    // Given
    String remoteAddress = "remote";

    // When
    Client client = new Client(remoteAddress);
    system = client.getActorSystem();

    // Then
    assertThat(client.getActorSystem().name(), is("LocalSystem"));
  }

}