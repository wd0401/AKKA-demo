package com.akkademy.messages;

import java.io.Serializable;

/**
 *
 * @author: Elvis Wan
 * @version: 1.0, 18/09/05
 */
public class SetRequest implements Serializable {

    private final String key;
    private final Object value;

    public SetRequest(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
