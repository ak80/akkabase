package org.ak80.akkabase.test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tools to build objects needed in test
 */
public class Builder {

  private static final AtomicInteger uniqueCounter = new AtomicInteger(0);

  public static Integer anUniqueInt() {
    return uniqueCounter.getAndIncrement();
  }

  public static String aKey() {
    return "KEY_".concat(Integer.toString(anUniqueInt()));
  }

}
