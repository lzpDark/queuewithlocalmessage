package com.github.lzpdark.queuewithlocalmessage.config;

import com.sun.jdi.connect.Connector;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lzp
 */
@Configuration
public class QueueConfig {

    @Bean
    public DirectExchange incidentExchange() {
        return ExchangeBuilder.directExchange("exchange.incident").build();
    }

    @Bean
    public DirectExchange incidentDeadExchange() {
        return ExchangeBuilder.directExchange("exchange.incident.dead").build();
    }

    @Bean
    public Queue incidentQueue() {
        return QueueBuilder.durable("queue.incident")
                .withArgument("x-message-ttl", 600_000)
                .withArgument("x-dead-letter-exchange", "exchange.incident.dead")
                .withArgument("x-dead-letter-routing-key", "route.incident.dead")
                .build();
    }

    @Bean
    public Queue incidentDeadQueue() {
        return new Queue("queue.incident.dead");
    }

    @Bean
    public Binding binding(Queue incidentQueue, DirectExchange incidentExchange) {
        return BindingBuilder.bind(incidentQueue)
                .to(incidentExchange)
                .with("route.incident");
    }

    @Bean
    public Binding bindingDead(Queue incidentDeadQueue, DirectExchange incidentDeadExchange) {
        return BindingBuilder.bind(incidentDeadQueue)
                .to(incidentDeadExchange)
                .with("route.incident.dead");
    }

}
