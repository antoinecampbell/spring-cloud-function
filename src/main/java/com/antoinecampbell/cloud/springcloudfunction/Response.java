package com.antoinecampbell.cloud.springcloudfunction;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Response {
    private Object body;
    private int statusCode = 200;

    public Response(Object body) {
        this.body = body;
    }
}
