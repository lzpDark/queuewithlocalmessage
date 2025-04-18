package com.github.lzpdark.queuewithlocalmessage.consumer;

import com.github.lzpdark.queuewithlocalmessage.mapper.IdempotentMapper;
import com.github.lzpdark.queuewithlocalmessage.mapper.IncidentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @author lzp
 */
@Component
@Slf4j
public class IncidentConsumer {

    @Autowired
    IncidentMapper incidentMapper;
    @Autowired
    IdempotentMapper idempotentMapper;

    @RabbitListener(queues = "queue.incident", ackMode = "AUTO", concurrency = "7")
    @Transactional
    public void handleIncident(String message,
                               @Header("unique_key") String uniqueKey) {
        if (!StringUtils.hasLength(uniqueKey)) {
            log.error("empty uniqueKey, message:{}", message);
            throw new RuntimeException("empty uniqueKey");
        }
        // 幂等处理
        if(idempotentMapper.addRecord(uniqueKey) != 1) { // 已经有这么一条记录了，说明处理过了
            log.info("already processed key: {}", uniqueKey);
            return;
        }
        // TODO: 处理业务
        //  - 如果业务只有db操作，这里实现没问题
        //  - 如果业务是异步调用第三方操作，幂等处理需要改成查询操作，异步调用成功后再插入幂等操作的记录
        log.info("received message: {}", message);
    }
}
