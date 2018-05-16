## About

This is a basic event store that should be sufficient for many event sourcing scenarios. Events are stored and retrieved 
using a simple REST style API. It will also support a simple subscription style push model.
 when I get around to it.
 
## Event Types 
 
### SimpleEvent  
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
* __timestamp__ - Unix style timestamp indicating the time the event was created. This is roughly analogous to the common use of update/createdTimestamp columns on database tables. _Don't rely upon this for ordering, as it set by the application at the time of creating the event and therefore will not necessarily be well ordered_.   
* __creator__ - The name / indentifier of the user or system that created the event, e.g. 'john.smith@example.com'. Limited to 255 chars 

Normally additional fields are added. The following can be combined as necessary.

### PayloadEvent 

This include the 'payload' key which holds any additional data captured with the event. 

```json
{
  "id" : "4778a3ef-a920-4323-bc34-b87aa0bffb41",
  "type" : "PayloadEvent",
  "timestamp": 1509618174218,
  "creator": "test",
  "payload" : {
    "some": "data",
    "array": [
      1,
      2,
      3
    ],
    "can be nested" : { "more" : "data"},
    "message": "payload should be no more less than 10K minified JSON"
  }
}
```

The content of the 'payload' is entirely application specific but it should not exceed 10k (this is actually a 
soft limit, the service will start to log warnings if exceeded). The hard limit is around 20K, and is determined by column size 
limitations in MySQL.

### AggregateEvent 

Often it is necessary to easily find the events for a particular entity or aggregate (using [DDD](https://martinfowler.com/bliki/DDD_Aggregate.html) 
terminology). This is done by adding an 'aggregateId' key.

```
{
  "id" : "4778a3ef-a920-4323-bc34-b87aa0bffb41",
  "type" : "AggregateEvent",
  "timestamp": 1509618174218,
  "creator": "test",
  "aggregateId" : "123"
}
``` 

The 'aggregateId' can be any string up to 255 characters 

### SessionEvent 

This allows a set of events to be related some underlying session or transaction, which is typically useful 
 when investigating problems or attempting to recreate the original user input. Note that this is NOT the same as 
batching saves (see below).

```json
{
  "id" : "4778a3ef-a920-4323-bc34-b87aa0bffb41",
  "type" : "SessionEvent",
  "timestamp": 1509618174218,
  "creator": "test",
  "sessionId" : "session#564ghsdgd5bncfz"
}
```

The 'sessionId' can be any string up to 255 characters.


## Storing and Retrieving Events 

### Saving Events 

Simple POST a JSON array containing one or more events.

```bash
curl -H "Content-Type: application/json" -X POST -d '[{ "id" : "4778a3ef-a920-4323-bc34-b87aa0bffb41", "type" : "SimpleEvent", "timestamp": 1509618174218,"creator": "test"}]' http://localhost:7001/events
```

