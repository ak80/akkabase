package org.ak80.akkabase

import akka.actor.AbstractActor
import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.Status
import akka.event.Logging
import akka.japi.pf.ReceiveBuilder

class AkkabaseDb : AbstractActor() {

    companion object Factory {
        @JvmStatic
        fun create(): Props = Props.create(AkkabaseDb::class.java)
    }

    val log = Logging.getLogger(context.system(), this)

    val map: MutableMap<String, Any> = mutableMapOf()

    init {
        receive(ReceiveBuilder
                .match(SetRequest::class.java, { this.receiveSetMessage(it) })
                .match(GetRequest::class.java, { this.receiveGetMessage(it) })
                .match(SetIfNotExistsRequest::class.java, { this.receiveSetIfNotExistsMessage(it) })
                .match(DeleteRequest::class.java, { this.receiveDeleteMessage(it) })
                .matchAny { o -> log.info("received unknown message {}", o) }
                .build())
    }

    fun receiveSetMessage(request: SetRequest) {
        log.info("received set request: {}", request)
        map.put(request.key, request.value)
        sender().tell(Status.Success(request.key), ActorRef.noSender());
    }

    fun receiveGetMessage(request: GetRequest) {
        log.info("received get request: {}", request)
        if (map.containsKey(request.key)) {
            sender().tell(map.get(request.key), ActorRef.noSender())
        } else {
            sender().tell(Status.Failure(KeyNotFoundException(request.key)), ActorRef.noSender());
        }
    }

    fun receiveSetIfNotExistsMessage(request: SetIfNotExistsRequest) {
        log.info("received set-if-not-exists request: {}", request)
        if (!map.containsKey(request.key)) {
            map.put(request.key, request.value)
        }
        sender().tell(Status.Success(map.get(request.key)), ActorRef.noSender());
    }

    fun receiveDeleteMessage(request: DeleteRequest) {
        log.info("received delete request: {}", request)
        if (map.containsKey(request.key)) {
            map.remove(request.key)
            sender().tell(Status.Success(true), ActorRef.noSender());
        } else {
            sender().tell(Status.Success(false), ActorRef.noSender());
        }
    }
}