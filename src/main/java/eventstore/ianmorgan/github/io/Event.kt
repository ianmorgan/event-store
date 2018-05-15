package eventstore.ianmorgan.github.io

import java.util.*
import kotlin.collections.HashMap

/**
 * Data class to define an Event.
 */
data class Event(
    val id: UUID = UUID.randomUUID(),
    val type: String,
    val timestamp: Long = System.currentTimeMillis(),
    val creator: String = "???",
    val aggregateId: String? = null,
    val sessionId : String? = null,
    val payload: Map<String,Any>? = null
) {

    /**
     *
     */
    object ModelMapper {
        fun asMap(ev: Event): Map<String, Any> {
            val map = HashMap<String, Any>()
            map["id"] = ev.id
            map["type"] = ev.type
            map["creator"] = ev.creator
            map["timestamp"] = ev.timestamp

            if (ev.aggregateId != null) map["aggregateId"] = ev.aggregateId
            if (ev.sessionId != null) map["sessionId"] = ev.sessionId
            if (ev.payload != null) map["payload"] = ev.payload
            return map
        }
    }

}




