package com.github.lzpdark.queuewithlocalmessage.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lzpdark.queuewithlocalmessage.mapper.IncidentMapper;
import com.github.lzpdark.queuewithlocalmessage.mapper.LocalMessageMapper;
import com.github.lzpdark.queuewithlocalmessage.model.Incident;
import com.github.lzpdark.queuewithlocalmessage.model.LocalMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lzp
 */
@RestController
@RequestMapping("/publish")
public class PublishController {

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    IncidentMapper incidentMapper;
    @Autowired
    LocalMessageMapper localMessageMapper;

    @GetMapping("")
    @Transactional
    public Object test() throws JsonProcessingException {

        long millis = System.currentTimeMillis();
        Incident incident = new Incident("title-" + millis, "content-" + millis, "zzz", "aaa");
        incidentMapper.addIncident(incident);
        Integer incidentId = incident.getId();
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId("" + incidentId);
        LocalMessage localMessage = new LocalMessage("" + incidentId, incident.getContent());
        localMessageMapper.addLocalMessage(localMessage);

        rabbitTemplate.convertAndSend("exchange.incident", "route.incident",
                new ObjectMapper().writeValueAsString(incident),
                message -> {
                    message.getMessageProperties().setCorrelationId("" + incidentId);
                    message.getMessageProperties().setExpiration("30000");
                    return message;
                },
                correlationData);
        return "done";
    }
}
