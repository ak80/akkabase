package org.ak80.akkabase

import java.io.Serializable

/**
 * Message for Akka Actors
 */

// SetRequest for insert and update of a key-value-pair
class SetRequest(val key: String, val value: Any) : Serializable {

    override fun toString() = "${this.javaClass.simpleName} $key=$value"

}

// GetRequest for insert and update of a key-value-pair
class GetRequest(val key: String) : Serializable {

    override fun toString() = "${this.javaClass.simpleName} $key"

}

class KeyNotFoundException(val key: String) : Exception(), Serializable