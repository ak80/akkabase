package org.ak80.akkabase

/**
 * Message for Akka Actors
 */

// SetMessage for insert and update of a key-value-pair
class SetMessage(val key: String, val value: Object) {

    override fun toString() = "${this.javaClass.simpleName} $key=$value"

}