package com.github.lzpdark.queuewithlocalmessage.consumer;

import com.github.lzpdark.queuewithlocalmessage.mapper.IncidentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lzp
 */
@Component
@Slf4j
public class IncidentConsumer {

    @Autowired
    IncidentMapper incidentMapper;

    @RabbitListener(queues = "queue.incident", ackMode = "AUTO", concurrency = "7")
    public void handleIncident(String message) {
        // TODO: 幂等处理

        // TODO: 处理业务
        log.info("received message: {}", message);
    }
}
