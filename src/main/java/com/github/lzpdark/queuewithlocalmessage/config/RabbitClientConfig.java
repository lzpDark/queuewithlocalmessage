package com.github.lzpdark.queuewithlocalmessage.config;

import com.github.lzpdark.queuewithlocalmessage.mapper.LocalMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lzp
 */
@Configuration
@Slf4j
public class RabbitClientConfig {

    @Autowired
    LocalMessageMapper localMessageMapper;

    @Bean
    public RabbitTemplate rabbitTemplate(RabbitTemplateConfigurer configurer, ConnectionFactory connectionFactory,
                                         ObjectProvider<RabbitTemplateCustomizer> customizers) {
        RabbitTemplate template = new RabbitTemplate();
        configurer.configure(template, connectionFactory);
        customizers.orderedStream().forEach((customizer) -> customizer.customize(template));
        template.setConfirmCallback((correlationData, ack, cause) -> {
            if(correlationData == null) {
                log.warn("empty correlationData, ack:{}, cause:{}", ack, cause);
                return;
            }
            String keyId = correlationData.getId();
            if(ack) {
                localMessageMapper.delete(keyId);
            } else {
                log.error("confirm failed: keyId:{}, cause:{}", keyId, cause);
            }
        });
        template.setReturnsCallback(returned -> {
            log.error("publish returned with correlationId: {}",
                    returned.getMessage().getMessageProperties().getCorrelationId());
        });
        return template;
    }
}
