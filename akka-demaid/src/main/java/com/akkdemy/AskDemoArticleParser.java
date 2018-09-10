package com.akkdemy;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.util.Timeout;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static akka.pattern.Patterns.ask;
import static scala.compat.java8.FutureConverters.toJava;

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
        return receiveBuilder()
                .match(ParseArticle.class, msg -> {
                    final CompletionStage cacheResult = toJava(ask(cacheActor, msg, timeout));
                    final CompletionStage result = cacheResult
                            .handle(
                                    (x, t) -> (x != null)
                                            ? CompletableFuture.completedFuture(x)
                                            : toJava(ask(httpClientActor, msg.url,
                                            timeout))
                                            .thenCompose(
                                                    rawArticle -> toJava(
                                                            ask(articleParseActor, new ParseHtmlArticle(msg.url, ((HttpResponse)
                                                                            rawArticle).body),
                                                                    timeout)
                                                    )
                                            )
                            ).thenCompose(x -> x);
                    final ActorRef senderRef = sender();

                    result.handle((x, t) -> {
                        if (x != null) {
                            if (x instanceof ArticleBody) {
                                String body = ((ArticleBody) x).body;
                                cacheActor.tell(body, self());
                                senderRef.tell(body, self());
                            } else if (x instanceof String) {
                                senderRef.tell(x, self());
                            }
                        } else if (x == null) {
                            senderRef.tell(
                                    new akka.actor.Status.Failure(
                                            (Throwable) t),
                                    self());
                        }
                        return null;
                    });
                }).build();
    }
}

