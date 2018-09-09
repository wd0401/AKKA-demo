package com.akkademy;

import com.akkademy.messages.KeyNotFoundException;
import com.akkdemy.JClient;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class JClientIntegrationTest {
    JClient client = new JClient("127.0.0.1:2552");

    @Test
    public void itShouldSetRecord() throws Exception {
        client.set("123", 123);
        Integer result = (Integer) ((CompletableFuture) client.
                get("123")).get();

        assert (result == 123);
    }

    @Test
    public void itShouldSetRecord1() throws Exception {
        Integer result = (Integer) ((CompletableFuture) client.
                get("123")).get();
        System.out.println(result);
        assert (result == 123);
    }

    @Test
    public void itShouldSetRecord2() throws Exception {
        client.get("00").handle((x, t) -> {
            if (t != null) {
                System.out.println("error...");
                assert t instanceof KeyNotFoundException;
            }
            return false;
        });
//        CompletableFuture f = ((CompletableFuture) client.
//                get("234"));
//        f.exceptionally(fn -> {
//            return new Exception();
//        });


    }
}
