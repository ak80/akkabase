package org.ak80.akkabase;

import org.ak80.akkabase.test.Builder;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class KeyNotFoundExceptionTest {

  @Test
  public void create_new_then_get_returns_key_value() {
    // Given
    String key = Builder.aKey();

    // When
    KeyNotFoundException keyNotFoundException = new KeyNotFoundException(key);

    // Then
    assertThat(keyNotFoundException.getKey(), is(key));
  }

  @Test
  public void toString_returns_key_value_in_string() {
    // Given
    String key = Builder.aKey();

    // When
    KeyNotFoundException keyNotFoundException = new KeyNotFoundException(key);

    // Then
    assertThat(keyNotFoundException.toString(), is(KeyNotFoundException.class.getName()));
  }

}
