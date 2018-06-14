#!/bin/bash


exec java -Xmx64m -XX:+HeapDumpOnOutOfMemoryError \
	-XX:+PrintGC \
    -cp /home/app/event-store.jar \
    eventstore.ianmorgan.github.io.AppKt