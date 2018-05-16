package eventstore.ianmorgan.github.io

import org.json.JSONObject
import java.io.File
import java.util.*


class EventDao {

    val events = ArrayList<Event>()

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
     * Retrieves events with filtering applied
     */
    fun retrieve(filter : Filter) : List<Event> {
        return this.events.filter { it -> matchesFilter(it,filter) };
    }


    private fun matchesFilter (ev : Event, filter : Filter) : Boolean {
        var matched : Boolean
        matched =  ((filter.type != null) && (ev.type == (filter.type) ))

        return matched;
    }
}


data class Filter(
    val type: String? = null,  // Comma separated list of event types (the 'type' key) to filter on
    val pageSize: Integer? = null,
    val lastId: UUID? = null,  // Typically combined with the 'pageSize' to retrieve from a position within the event stream. Note that this exclusive, i.e. the query will return the matching events directly after this event id
    val aggregateId: String? = null,
    val sessionId: String? = null
)