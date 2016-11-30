package org.ak80.akkabase;

import org.ak80.akkabase.test.Builder;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SetRequestTest {

  @Test
  public void create_new_then_get_returns_key_value() {
    // Given
    String key = Builder.aKey();
    Integer value = Builder.anUniqueInt();

    // When
    SetRequest setRequest = new SetRequest(key, value);

    // Then
    assertThat(setRequest.getKey(), is(key));
    assertThat(setRequest.getValue(), is(value));
  }

  @Test
  public void toString_returns_key_value_in_string() {
    // Given
    String key = Builder.aKey();
    Integer value = Builder.anUniqueInt();
    SetRequest setRequest = new SetRequest(key, value);

    // When
    String asString = setRequest.toString();

    // Then
    assertThat(asString, is(String.format("SetRequest %s=%s", key, value)));
  }

}
