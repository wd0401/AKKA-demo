package com.akkdemy;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.util.Timeout;

import static akka.pattern.Patterns.ask;

public class AskDemoArticleParser extends AbstractActor {
    private final ActorSelection cacheActor;
    private final ActorSelection httpClientActor;
    private final ActorSelection articleParseActor;
    private final Timeout timeout;

    public AskDemoArticleParser(String cacheActorPath, String httpClientActorPath, String artcileParseActor,
                                Timeout timeout) {
        this.cacheActor = context().actorSelection(cacheActorPath);
        this.httpClientActor =
                context().actorSelection(httpClientActorPath);
        this.articleParseActor =
                context().actorSelection(artcileParseActor);
        this.timeout = timeout;
    }

    @Override
    public Receive createReceive() {


        return null;
    }
}