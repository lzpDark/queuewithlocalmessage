package com.github.lzpdark.queuewithlocalmessage.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lzpdark.queuewithlocalmessage.mapper.LocalMessageMapper;
import com.github.lzpdark.queuewithlocalmessage.model.Incident;
import com.github.lzpdark.queuewithlocalmessage.model.LocalMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * TODO: 根据本地消息表补偿发消息
 *  这里要考虑多个服务实例的情况，可能多个服务同时处理一个消息：
 *   - 应该把这部分逻辑单独放到一个服务里
 *   - 考虑多个服务之间怎么分配消息
 *   - 怎么扩容，方便加快消息处理速度
 *
 * @author lzp
 */
@Component
@Slf4j
public class PendingMessageReSender {

    @Autowired
    IncidentPublisher incidentPublisher;
    @Autowired
    LocalMessageMapper localMessageMapper;
    @Autowired
    ObjectMapper objectMapper;
    private static final int maxRetryCount = 3;

    @Scheduled(fixedRate = 10_000)
    public void resend() {
        List<LocalMessage> localMessages = localMessageMapper.selectToSendMessages();
        if (localMessages == null) {
            return;
        }
        for (LocalMessage localMessage : localMessages) {
            // check max retry count
            if(localMessage.getErrorCount() >= maxRetryCount) {
                failed(localMessage);
                continue;
            }
            // parse message
            Incident incident;
            try {
                incident = objectMapper.readValue(localMessage.getMessage(), Incident.class);
            } catch (JsonProcessingException e) {
                log.error("parse message failed: {}", localMessage.getMessage());
                failed(localMessage);
                continue;
            }
            // send and count
            incidentPublisher.sendIncident(incident);
            incrementErrorCount(localMessage);
        }
    }

    private void failed(LocalMessage localMessage) {
        if(localMessageMapper.failed(localMessage.getKeyId()) < 1) {
            log.error("local message-failed ops failed with keyId:{}",
                    localMessage.getKeyId());
        }
    }

    private void incrementErrorCount(LocalMessage localMessage) {
        if(localMessageMapper.incrementErrorCount(localMessage.getKeyId()) < 1) {
            log.error("local message-increment-error-count failed with keyId:{}",
                    localMessage.getKeyId());
        }
    }
}
