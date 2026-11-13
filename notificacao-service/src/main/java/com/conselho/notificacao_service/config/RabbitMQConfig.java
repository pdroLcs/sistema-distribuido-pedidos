package com.conselho.notificacao_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "pedido.exchange";
    public static final String QUEUE_NAME = "pedido.queue";
    public static final String ROUTING_KEY = "pedido.criado";

    @Bean
    public DirectExchange pedidoExchange() {
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue pedidoQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Binding pedidoBinding(Queue pedidoQueue, DirectExchange pedidoExchange) {
        return BindingBuilder.bind(pedidoQueue).to(pedidoExchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}