package com.akkademy.messages;

import java.io.Serializable;

public class GetRequest implements Serializable {

    private final String key;

    public GetRequest(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
