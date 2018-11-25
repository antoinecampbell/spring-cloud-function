package com.antoinecampbell.cloud.springcloudfunction.item;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.antoinecampbell.cloud.springcloudfunction.Page;
import com.antoinecampbell.cloud.springcloudfunction.message.ErrorMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Component
public class ItemFunctions {

    private final ItemRepository itemRepository;
    private final Validator validator;

    public ItemFunctions(ItemRepository itemRepository, Validator validator) {
        this.itemRepository = itemRepository;
        this.validator = validator;
    }

    @Bean
    public Function<Message<Item>, Message> createItem() {
        return itemMessage -> {
            Item item = itemMessage.getPayload();
            Set<ConstraintViolation<Item>> errors = validator.validate(item);
            Message outputMessage;
            if (errors.isEmpty()) {
                long id = itemRepository.insertItem(item);
                item.setId(id);
                outputMessage = new GenericMessage<>(item);
            } else {
                outputMessage = createErrorMessage(errors);
            }

            return outputMessage;
        };
    }

    @Bean
    public Function<APIGatewayProxyRequestEvent, Message> getItems() {
        return apiGatewayProxyRequestEvent -> {
            Message outputMessage;
            try {
                Map<String, String> params = apiGatewayProxyRequestEvent.getQueryStringParameters();
                if (params == null) {
                    params = new HashMap<>();
                }
                int page = Integer.parseInt(params.getOrDefault("page", "1"));
                int size = Integer.parseInt(params.getOrDefault("size", "25"));
                outputMessage = new GenericMessage<>(itemRepository.getAllItems(new Page.Params(page, size)));
            } catch (Exception e) {
                ErrorMessage errorMessage = new ErrorMessage<>("Error getting page");
                Map<String, Object> headers = new HashMap<>();
                headers.put("statuscode", 500);
                outputMessage = new GenericMessage<>(errorMessage, headers);
            }

            return outputMessage;
        };
    }

    @Bean
    public Function<APIGatewayProxyRequestEvent, Message> getItem() {
        return apiGatewayProxyRequestEvent -> {
            Message outputMessage;
            try {
                long id = Long.parseLong(apiGatewayProxyRequestEvent.getPathParameters().get("id"));
                Item item = itemRepository.getItem(id);
                outputMessage = new GenericMessage<>(item);
            } catch (EmptyResultDataAccessException e) {
                Map<String, Object> headers = new HashMap<>();
                headers.put("statuscode", 404);
                outputMessage = new GenericMessage<>(new ErrorMessage<>("Item not found"), headers);
            } catch (Exception e) {
                Map<String, Object> headers = new HashMap<>();
                headers.put("statuscode", 500);
                outputMessage = new GenericMessage<>(new ErrorMessage<>("Error requesting item"), headers);
            }

            return outputMessage;
        };
    }

    @Bean
    public Function<Message<Item>, Message> updateItem() {
        return itemMessage -> {
            Message outputMessage;
            try {
                Item item = itemMessage.getPayload();
                Set<ConstraintViolation<Item>> errors = validator.validate(item);
                APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent =
                        itemMessage.getHeaders().get("request", APIGatewayProxyRequestEvent.class);
                Map<String, String> pathParams = apiGatewayProxyRequestEvent.getPathParameters();
                long id = Long.parseLong(pathParams.get("id"));
                if (errors.isEmpty()) {
                    itemRepository.updateItem(id, item);
                    outputMessage = new GenericMessage<>(itemRepository.getItem(id));
                } else {
                    outputMessage = createErrorMessage(errors);
                }
            } catch (Exception e) {
                Map<String, Object> headers = new HashMap<>();
                headers.put("statuscode", 500);
                outputMessage = new GenericMessage<>(new ErrorMessage<>("Error requesting item"), headers);
            }

            return outputMessage;
        };
    }

    private Message createErrorMessage(Set<ConstraintViolation<Item>> errors) {
        Message message;
        ErrorMessage errorMessage = new ErrorMessage<>("Invalid item", errors);
        Map<String, Object> headers = new HashMap<>();
        headers.put("statuscode", 400);
        message = new GenericMessage<>(errorMessage, headers);

        return message;
    }
}
