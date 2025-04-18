package com.github.lzpdark.queuewithlocalmessage.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lzpdark.queuewithlocalmessage.mapper.IncidentMapper;
import com.github.lzpdark.queuewithlocalmessage.mapper.LocalMessageMapper;
import com.github.lzpdark.queuewithlocalmessage.model.Incident;
import com.github.lzpdark.queuewithlocalmessage.model.LocalMessage;
import com.github.lzpdark.queuewithlocalmessage.publisher.IncidentPublisher;
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
    IncidentMapper incidentMapper;
    @Autowired
    LocalMessageMapper localMessageMapper;
    @Autowired
    IncidentPublisher incidentPublisher;

    @GetMapping("")
    @Transactional
    public Object test() {

        long millis = System.currentTimeMillis();
        // incident record
        Incident incident = new Incident("title-" + millis, "content-" + millis, "zzz", "aaa");
        incidentMapper.addIncident(incident);

        // local message record
        String json;
        try {
            json = new ObjectMapper().writeValueAsString(incident);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("incident to json error", e);
        }
        Integer incidentId = incident.getId();
        LocalMessage localMessage = new LocalMessage("" + incidentId, json);
        localMessageMapper.addLocalMessage(localMessage);

        // send message
        incidentPublisher.sendIncident(incident);
        return "done";
    }
}
