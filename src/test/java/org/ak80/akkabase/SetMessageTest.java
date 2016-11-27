package org.ak80.akkabase;

import org.junit.Test;

import static org.ak80.akkabase.test.Builder.aKey;
import static org.ak80.akkabase.test.Builder.anUniqueInt;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SetMessageTest {

  @Test
  public void create_new_then_get_returns_key_value() {
    // Given
    String key = aKey();
    Integer value = anUniqueInt();

    // When
    SetMessage setMessage = new SetMessage(key, value);

    // Then
    assertThat(setMessage.getKey(), is(key));
    assertThat(setMessage.getValue(), is(value));
  }

  @Test
  public void toString_returns_key_value_in_string() {
    // Given
    String key = aKey();
    Integer value = anUniqueInt();
    SetMessage setMessage = new SetMessage(key, value);

    // When
    String asString = setMessage.toString();

    // Then
    assertThat(asString, is(String.format("SetMessage %s=%s", key, value)));
  }

}
