package com.github.lzpdark.queuewithlocalmessage.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author lzp
 */
@Component
@Slf4j
public class IncidentDeadListener {

    @RabbitListener(queues = "queue.incident.dead", ackMode = "MANUAL", concurrency = "2")
    public void handleDeadMessage(String message) {
        log.info("dead message: {}", message);
        // TODO: record dead message
    }
}
