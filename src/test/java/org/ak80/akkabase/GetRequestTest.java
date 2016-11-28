package org.ak80.akkabase;

import org.junit.Test;

import static org.ak80.akkabase.test.Builder.aKey;
import static org.ak80.akkabase.test.Builder.anUniqueInt;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("PMD.MethodNamingConventions")
public class GetRequestTest {

  @Test
  public void create_new_then_get_returns_key_value() {
    // Given
    String key = aKey();
    Integer value = anUniqueInt();

    // When
    GetRequest getRequest = new GetRequest(key);

    // Then
    assertThat(getRequest.getKey(), is(key));
  }

  @Test
  public void toString_returns_key_value_in_string() {
    // Given
    String key = aKey();
    Integer value = anUniqueInt();
    GetRequest getRequest = new GetRequest(key);

    // When
    String asString = getRequest.toString();

    // Then
    assertThat(asString, is(String.format("GetRequest %s", key)));
  }

}
