package org.ak80.akkabase

import akka.actor.AbstractActor
import akka.event.Logging
import akka.japi.pf.ReceiveBuilder

class AkkabaseDb : AbstractActor() {

    val log = Logging.getLogger(context.system(), this)

    val map: MutableMap<String, Object> = mutableMapOf()

    init {
        receive(ReceiveBuilder
                .match(SetMessage::class.java, { this.receiveSetMessage(it) })
                .matchAny { o -> log.info("received unknown message {}", o) }
                .build())
    }

    fun receiveSetMessage(message: SetMessage) {
        log.info("received set request: {}", message)
        map.put(message.key, message.value)
    }

}