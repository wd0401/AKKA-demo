package com.lightbend.akka.sample;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * printer-messages
 *
 * @author: Elvis Wan
 * @version: 1.0, 18/09/05
 */
public class Printer extends AbstractActor {
    /**
     * #printer-messages
     *
     * @return
     */
    static public Props props() {
        return Props.create(Printer.class, () -> new Printer());
    }

    /**
     * #printer-messages
     */
    static public class Greeting {
        public final String message;

        public Greeting(String message) {
            this.message = message;
        }
    }

    /**
     * #printer-messages
     */
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public Printer() {
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Greeting.class, greeting -> {
                    log.info(greeting.message);
                })
                .build();
    }
//#printer-messages
}
//#printer-messages
