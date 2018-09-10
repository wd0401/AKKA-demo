package com.akkademy;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Status;
import akka.testkit.TestProbe;
import akka.util.Timeout;
import com.akkademy.messages.GetRequest;
import com.akkdemy.HttpResponse;
import com.akkdemy.ParseArticle;
import com.akkdemy.ParsingActor;
import com.akkdemy.TellDemoArticleParser;
import org.junit.Test;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;

public class TellTest {
    ActorSystem system = ActorSystem.create("testSystem");
    Timeout timeout = Timeout.durationToTimeout(Duration.create(5,TimeUnit.SECONDS));


    TestProbe cacheProbe = new TestProbe(system);
    TestProbe httpClientProbe = new TestProbe(system);
    ActorRef articleParseActor = system.actorOf(Props.create(ParsingActor.class));

    ActorRef tellDemoActor = system.actorOf(
            Props.create(TellDemoArticleParser.class,
                    cacheProbe.ref().path().toString(),
                    httpClientProbe.ref().path().toString(),
                    articleParseActor.path().toString(),
                    timeout)
    );

    @Test
    public void itShouldParseArticleTest() throws Exception{
        Future f = ask(tellDemoActor, new ParseArticle(("http://www.google.com")), timeout);
        cacheProbe.expectMsgClass(GetRequest.class);
        cacheProbe.reply(new Status.Failure(new Exception("no cache")));

        httpClientProbe.expectMsgClass(String.class);
        httpClientProbe.reply(new HttpResponse(Articles.article1));

        String result = (String) Await.result(f, timeout.duration());
        assert(result.contains("I’ve been writing a lot in emacs lately"));
        assert(!result.contains("<body>"));
    }


}
