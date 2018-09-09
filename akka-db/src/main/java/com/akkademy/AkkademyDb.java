package com.akkademy;

import akka.actor.AbstractActor;
import akka.actor.Status;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.akkademy.messages.GetRequest;
import com.akkademy.messages.KeyNotFoundException;
import com.akkademy.messages.SetRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author: Elvis Wan
 * @version: 1.0, 18/09/05
 */
public class AkkademyDb extends AbstractActor {
    protected final LoggingAdapter log = Logging.getLogger(context().system(), this);
    protected final Map<String, Object> map = new HashMap<>();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SetRequest.class, message -> {
                    log.info("Received Set request: {}--{}", message.getKey(),message.getValue());
                    map.put(message.getKey(), message.getValue());
                    sender().tell(new Status.Success(message.getKey()),self());
                })
                .match(GetRequest.class, message -> {
                    log.info("Received Get request key: {}", message.getKey());
                    Object value = map.get(message.getKey());
                    Object response = Objects.isNull(value) ? new Status.Failure(new KeyNotFoundException(message.getKey()))
                            : value;
                    sender().tell(response,self());
                })
                .matchAny(o ->{
                    log.info("received unknown message: {}", o);
                    sender().tell(new Status.Failure(new ClassNotFoundException()),self());
                })
                .build();
    }
}
