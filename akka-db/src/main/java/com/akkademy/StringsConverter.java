package com.akkademy;

import akka.actor.AbstractActor;
import akka.actor.Status;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class StringsConverter extends AbstractActor {
    protected LoggingAdapter log = Logging.getLogger(context().system(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, str -> {
                    StringBuilder stringBuilder = new StringBuilder(str);
                    log.info("the message is : {}", str);
                    sender().tell(new Status.Success(stringBuilder.reverse().toString()), self());
                })
                .matchAny(obj -> {
                    log.info("cant't convert this object : {}", obj);
                    sender().tell(new Status.Failure(new Exception()), self());
                })
                .build();
    }
}
