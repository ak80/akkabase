package org.ak80.akkabase.test;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * Echo the received message to the sender
 */
public class EchoActor extends AbstractActor {

  @Override
  public PartialFunction<Object, BoxedUnit> receive() {
    return ReceiveBuilder.matchAny(msg -> sender().tell(msg, self())).build();
  }

}
