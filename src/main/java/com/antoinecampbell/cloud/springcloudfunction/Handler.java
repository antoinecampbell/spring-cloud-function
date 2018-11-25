package com.antoinecampbell.cloud.springcloudfunction;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.function.adapter.aws.SpringBootApiGatewayRequestHandler;
import org.springframework.util.StringUtils;


@SuppressWarnings("unused")
@Slf4j
public class Handler extends SpringBootApiGatewayRequestHandler {

    @Override
    protected Object convertEvent(APIGatewayProxyRequestEvent event) {
        log.info(event.toString());

        if (StringUtils.isEmpty(event.getBody())) {
            return event;
        } else {
            return super.convertEvent(event);
        }
    }
}