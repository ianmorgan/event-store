## About

This is a basic event store that should be sufficient for many event sourcing scenarios. Events are stored and retrieved 
using a simple REST style API. It will also support a simple subscription style push model.
 when I get around to it.
 
The most basic event is as follows:


```json 
{
  "id" : "4778a3ef-a920-4323-bc34-b87aa0bffb41",
  "type" : "SimpleEvent",
  "timestamp": 1509618174218,
  "creator": "test"
}
```

* __id__ - A random (type 3) UUID
* __type__ - A unique name for the type of event. By convention in Java class name format. Limited to 255 chars
* __timestamp__ - Unix style timestamp indicating the time the event was created. This is roughly analogous to the common use of update/createdTimestamp columns on database tables.
* __creator__ - The name / indentifier of the user or system that created the event, e.g. 'john.smith@example.com' 
