package org.ak80.akkabase.test;

import akka.AkkaException;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.InfoFilter;
import akka.testkit.JavaTestKit;
import scala.runtime.AbstractFunction0;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Utility to assert Log and Exception messages by an actor. Don't forget to set
 * akka.loggers = [akka.testkit.TestEventListener] in application.conf
 */
public class LogAssert {

  /**
   * assert logging of info message when a piece of code is executed
   *
   * @param runnable the piece of code to execute
   * @param message  the expected message (part)
   * @param actorRef the actor who sends the message
   * @param system   actor system
   */
  public static void assertLogInfo(Runnable runnable, String message, ActorRef actorRef, ActorSystem system) {
    new JavaTestKit(system) {
      {
        InfoFilter filter = new InfoFilter(actorRef.path().toString(), message, true, false, 1);
        boolean result = filter.intercept(new AbstractFunction0<Boolean>() {
          @Override
          public Boolean apply() {
            runnable.run();
            return true;
          }
        }, system);

        assertThat(result, is(true));
      }
    };
  }

  /**
   * assert logging of exceptional message when a piece of code is executed
   *
   * @param runnable  the piece of code to execute
   * @param exception the expected exception
   * @param actorRef  the actor who sends the message
   * @param system    actor system
   */
  public static void assertException(Runnable runnable, Class<? extends AkkaException> exception, ActorRef actorRef, ActorSystem system) {
    new JavaTestKit(system) {
      {
        boolean result = new EventFilter<Boolean>(exception) {
          protected Boolean run() {
            runnable.run();
            return true;
          }
        }.from(actorRef.path().toString()).occurrences(1).exec();

        assertThat(result, is(true));
      }
    };
  }

}
