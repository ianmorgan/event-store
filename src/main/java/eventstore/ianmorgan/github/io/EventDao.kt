package eventstore.ianmorgan.github.io

class EventDao {

    val events = ArrayList<Event>()

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