package org.ak80.akkabase.it;

import org.ak80.akkabase.client.Client;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

import static org.ak80.akkabase.test.Builder.aKey;
import static org.ak80.akkabase.test.Builder.anUniqueInt;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("PMD.MethodNamingConventions")
public class ClientIT {

  private static Client client = new Client("127.0.0.1:2552");

  @Test
  public void sendRecord_can_be_retrieved() throws Exception {
    // Get
    String key = aKey();
    int value = anUniqueInt();

    // When
    client.set(key, value);

    // Then
    Integer result = (Integer) ((CompletableFuture) client.get(key)).get();
    assertThat(result, is(value));
  }

  @Test
  public void sendRecord_then_delete_then_setIfNotExists_can_be_retrieved() throws Exception {
    // Get
    String key = aKey();
    int value0 = anUniqueInt();
    int value1 = anUniqueInt();

    // When
    client.set(key, value0);
    client.delete(key);
    client.setIfNotExists(key, value1);

    // Then
    Integer result = (Integer) ((CompletableFuture) client.get(key)).get();
    assertThat(result, is(value1));
  }

}