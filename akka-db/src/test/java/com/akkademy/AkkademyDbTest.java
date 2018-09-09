package com.akkademy;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.testkit.TestActorRef;
import com.akkademy.messages.SetRequest;
import com.sun.xml.internal.ws.util.CompletedFuture;
import org.junit.Assert;
import org.junit.Test;
import scala.concurrent.Future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static scala.compat.java8.FutureConverters.toJava;

/**
 * @author: Elvis Wan
 * @version: 1.0, 18/09/05
 */
public class AkkademyDbTest {

    ActorSystem actorSystem = ActorSystem.create();


    @Test
    public void itShouldPlaceKeyValueFromSetMessageIntoMap() {
        TestActorRef<AkkademyDb> testActorRef = TestActorRef.create(actorSystem, Props.create(AkkademyDb.class));
        TestActorRef<AkkademyDb> testActorRef1 = TestActorRef.create(actorSystem, Props.create(JavaPongActor.class));

        testActorRef.tell(new SetRequest("key1", "value1"), ActorRef.noSender());
        testActorRef.tell(new String("key1value1"), ActorRef.noSender());

        testActorRef1.tell("ping", ActorRef.noSender());

        AkkademyDb akkademyDb = testActorRef.underlyingActor();
        System.out.println(testActorRef.path());
        System.out.println(testActorRef1.path());
        assertEquals(akkademyDb.map.get("key1"), "value1");

        ActorSelection selection = actorSystem.actorSelection("akka.tcp://actorSystem@host.jason-goodwin" +
                ".com:5678/user/KeanuReeves");
    }

    @Test
    public void testFuture() {
        TestActorRef<AkkademyDb> testActorRef1 = TestActorRef.create(actorSystem, Props.create(JavaPongActor.class));
        Future<Object> future = Patterns.ask(testActorRef1, "Ping", 1000);
        final CompletionStage<Object> cs = toJava(future);
        System.out.println("111");
        System.out.println(Thread.currentThread().getName());

        final CompletableFuture<Object> jFuture = (CompletableFuture<Object>) cs;
        jFuture
//                .thenRun(() -> {
//            System.out.println("ttt");
//            System.out.println(Thread.currentThread().getName());
//        })
                .thenAccept(x -> System.out.println(x));
        System.out.println("fisrt");
    }

    public static String getUserNameById(String id) {
        try {
            Thread.sleep(1000);
            System.out.println(1);
            Thread.sleep(1000);
            System.out.println(2);
            System.out.println(Thread.currentThread().getName());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "username" + id;
    }


    @Test
    public void testReverse() throws ExecutionException, InterruptedException {

        TestActorRef<StringsConverter> ref = TestActorRef.create(actorSystem, Props.create(StringsConverter.class));

        ref.tell("Hell, akka!", ActorRef.noSender());

        Future<Object> future = Patterns.ask(ref, "Hell, akka!", 1000);
        final CompletionStage<Object> cs = toJava(future);
//        cs.thenAccept(c -> {
//            assert ("!akka ,lleH" == c);
//        });


        String str =  (String)((CompletableFuture) cs).get();

        assertEquals("!akka ,lleH", str);
    }
}
