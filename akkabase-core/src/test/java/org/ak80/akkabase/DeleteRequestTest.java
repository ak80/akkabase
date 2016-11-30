package org.ak80.akkabase;

import org.ak80.akkabase.test.Builder;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("PMD.MethodNamingConventions")
public class DeleteRequestTest {

  @Test
  public void create_new_then_get_returns_key_value() {
    // Given
    String key = Builder.aKey();

    // When
    DeleteRequest deleteRequest = new DeleteRequest(key);

    // Then
    assertThat(deleteRequest.getKey(), is(key));
  }

  @Test
  public void toString_returns_key_value_in_string() {
    // Given
    String key = Builder.aKey();
    DeleteRequest deleteRequest = new DeleteRequest(key);

    // When
    String asString = deleteRequest.toString();

    // Then
    assertThat(asString, is(String.format("DeleteRequest %s", key)));
  }

}
