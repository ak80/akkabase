package org.ak80.akkabase;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MessagesKtTest {

  @Test
  public void getAkkabaseDbActorPath() {
    // Given
    String remote = "remote";

    // When
    String path = MessageKt.getDbActor(remote);

    // Then
    assertThat(path, is("akka.tcp://akkabase@remote/user/akkabase-db"));
  }

  @Test
  public void getAkkabaseString() {
    assertThat(MessageKt.getServerSystem(), is("akkabase"));
    assertThat(MessageKt.getDbActor(), is("akkabase-db"));
  }

}
