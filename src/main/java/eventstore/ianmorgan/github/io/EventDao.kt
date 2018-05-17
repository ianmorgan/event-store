package eventstore.ianmorgan.github.io

import org.json.JSONObject
import java.io.File
import java.util.*


class EventDao {

    private val events = ArrayList<Event>()

    /**
     * Used to load a set of events from JSON files stored on disk
     */
    fun load(directory: String) {

        // using extension function walk
        File(directory).walk().forEach {
            if (it.name.endsWith(".json")) {
                val content = it.readText()
                val json = JSONObject(content)
                events.add(Event.ModelMapper.fromJSON(json))
            }
        }
    }

    /**
     * Stores (saves) the provided events.
     */
    fun storeEvents(events: List<Event>) {
        this.events.addAll(events)
    }

    /**
     * Retrieves all events, without any filtering.
     */
    fun retrieve(): List<Event> {
        return this.events;
    }

    /**
     * Drop all events (similar to TRUNCATE TABLE in the SQL world)
     *
     */
    fun truncate() {
        events.clear()
    }

    /**
     * Retrieves events with filtering applied
     */
    fun retrieve(filter: Filter): List<Event> {
        if (filter.lastId != null) {
            for (i in events.indices) {
                if (events[i].id == filter.lastId) {// not the last event
                    val filtered = this.events
                        .subList(i + 1, events.size)
                        .filter { it -> matchesFilter(it, filter) }

                    if (filter.pageSize != null){
                        return filtered.take(filter.pageSize)
                    }
                    else {
                        return filtered
                    }
                }
            }
            return Collections.emptyList()
        } else {
            val filtered = this.events.filter { it -> matchesFilter(it, filter) }
            if (filter.pageSize != null){
                return filtered.take(filter.pageSize)
            }
            else {
                return filtered
            }
        }
    }


    private fun matchesFilter(ev: Event, filter: Filter): Boolean {
        val matchedType = if (filter.type != null) (ev.type == filter.type) else true
        val matchedAggregate = if (filter.aggregateId != null) (ev.aggregateId == filter.aggregateId) else true
        val matchedSession = if (filter.sessionId != null) (ev.sessionId == filter.sessionId) else true

        return matchedType && matchedAggregate && matchedSession
    }
}


data class Filter(
    val type: String? = null,  // Comma separated list of event types (the 'type' key) to filter on
    val pageSize: Int? = null,
    val lastId: UUID? = null,  // Typically combined with the 'pageSize' to retrieve from a position within the event stream. Note that this exclusive, i.e. the query will return the matching events directly after this event id
    val aggregateId: String? = null,
    val sessionId: String? = null


) {
    object ModelMapper {

        /**
         * Unpack the map of http query params and build a filter
         */
        fun fromQueryMap(map: Map<String, Array<String>>): Filter {
            val type = safeUnpack(map.get("type"))
            val pageSize = safeToInteger(safeUnpack(map.get("pageSize")))
            val lastId = safeToUUID(safeUnpack(map.get("lastId")))
            val aggregateId = safeUnpack(map.get("aggregateId"))
            val sessionId = safeUnpack(map.get("sessionId"))

            return Filter(
                type = type,
                pageSize = pageSize,
                lastId = lastId,
                aggregateId = aggregateId,
                sessionId = sessionId
            )
        }

        private fun safeToInteger(value: String?): Int? {
            return if (value != null) Integer.parseInt(value) else null
        }

        private fun safeToUUID(value: String?): UUID? {
            return if (value != null) UUID.fromString(value) else null
        }

        private fun safeUnpack(array: Array<String>?): String? {
            if (array != null && array.isNotEmpty()) {
                return array[0]
            }
            return null
        }
    }
}