package com.github.lzpdark.queuewithlocalmessage.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lzpdark.queuewithlocalmessage.mapper.LocalMessageMapper;
import com.github.lzpdark.queuewithlocalmessage.model.Incident;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lzp
 */
@Component
public class IncidentPublisher {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    LocalMessageMapper localMessageMapper;

    public void sendIncident(Incident incident) {

        String json;
        try {
            json = new ObjectMapper().writeValueAsString(incident);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("incident to json error", e);
        }

        // send message
        CorrelationData correlationData = new CorrelationData();
        String incidentId = "" + incident.getId();
        correlationData.setId("" + incidentId);
        rabbitTemplate.convertAndSend("exchange.incident", "route.incident",
                json,
                message -> {
                    message.getMessageProperties().setCorrelationId("" + incidentId);
                    message.getMessageProperties().setExpiration("30000");
                    message.getMessageProperties().setHeader("unique_key", "" + incidentId);
                    return message;
                },
                correlationData);
    }
}