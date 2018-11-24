package com.antoinecampbell.cloud.springcloudfunction;

import com.antoinecampbell.cloud.springcloudfunction.item.Item;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

import java.util.function.Function;
import java.util.function.Supplier;

@SpringBootApplication
public class SpringCloudFunctionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudFunctionApplication.class, args);
    }

    @Bean
    public Function<String, String> test() {
        return s -> s;
    }

    @Bean
    public Function<Message<Item>, Response> postMessage() {
        return Response::new;
    }

    @Bean
    public Supplier<Response> getMessage() {
        return Response::new;
    }
}
