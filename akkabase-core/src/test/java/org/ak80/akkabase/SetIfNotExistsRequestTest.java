package org.ak80.akkabase;

import org.ak80.akkabase.test.Builder;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("PMD.MethodNamingConventions")
public class SetIfNotExistsRequestTest {

  @Test
  public void create_new_then_get_returns_key_value() {
    // Given
    String key = Builder.aKey();
    Integer value = Builder.anUniqueInt();

    // When
    SetIfNotExistsRequest setRequest = new SetIfNotExistsRequest(key, value);

    // Then
    assertThat(setRequest.getKey(), is(key));
    assertThat(setRequest.getValue(), is(value));
  }

  @Test
  public void toString_returns_key_value_in_string() {
    // Given
    String key = Builder.aKey();
    Integer value = Builder.anUniqueInt();
    SetIfNotExistsRequest setRequest = new SetIfNotExistsRequest(key, value);

    // When
    String asString = setRequest.toString();

    // Then
    assertThat(asString, is(String.format("SetIfNotExistsRequest %s=%s", key, value)));
  }

}
