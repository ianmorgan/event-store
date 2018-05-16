package eventstore.ianmorgan.github.io

import org.json.JSONObject
import java.io.File

class EventDao {

    val events = ArrayList<Event>()

    /**
     * Used to load a set of events from JSON files stored on disk
     */
    fun load (directory : String){

        // using extension function walk
        File(directory).walk().forEach {
            //println(it)

            if (it.name.endsWith(".json")){
                val content = it.readText()
                //println(content)
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
     * Retrieves all events without any filtering.
     */
    fun retrieve(): List<Event> {
        return this.events;
    }
}