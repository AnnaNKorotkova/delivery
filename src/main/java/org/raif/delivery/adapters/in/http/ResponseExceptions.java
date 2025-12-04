package org.raif.delivery.adapters.in.http;

import lombok.Getter;

@Getter
public class ResponseExceptions extends RuntimeException {
    private final int code;
    public ResponseExceptions(int code, String message) {
        super(message);
        this.code = code;
    }
}
