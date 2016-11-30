package org.ak80.akkabase

import java.io.Serializable

/**
 * Message for Akka Actors
 */

fun getDbActor(remoteAddress: String) = "akka.tcp://$serverSystem@$remoteAddress/user/$dbActor"

val serverSystem = "akkabase"

val dbActor = "akkabase-db"


// SetRequest for insert and update of a key-value-pair
class SetRequest(val key: String, val value: Any) : Serializable {

    override fun toString() = "${this.javaClass.simpleName} $key=$value"

}

// GetRequest for insert and update of a key-value-pair
class GetRequest(val key: String) : Serializable {

    override fun toString() = "${this.javaClass.simpleName} $key"

}

// SetIfNotExists for insert if no pair exists for key
class SetIfNotExistsRequest(val key: String, val value: Any) : Serializable {

    override fun toString() = "${this.javaClass.simpleName} $key=$value"

}


// DeleteRequest delete of a key-value-pair
class DeleteRequest(val key: String) : Serializable {

    override fun toString() = "${this.javaClass.simpleName} $key"

}

// for key not found
class KeyNotFoundException(val key: String) : Exception(), Serializable