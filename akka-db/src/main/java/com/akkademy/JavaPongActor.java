package com.akkademy;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Status;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class JavaPongActor extends AbstractActor {

    protected final LoggingAdapter log = Logging.getLogger(context().system(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("Ping", s -> {
                            sender().tell("Pong", ActorRef.noSender());
                            log.info("Ping: {}", s);
                        }
                )
                .matchAny(x -> {
                            log.info("other : {}", x);
                            sender().tell(
                                    new Status.Failure(new Exception("unknown message")), self()
                            );
                        }
                )
                .build();
    }

    public static Props props(String msg) {
        return Props.create(JavaPongActor.class, msg);
    }
}
