package com.dockerconsumercompiler.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dockerconsumercompiler.services.MessageReceiverService;
import com.dockerconsumercompiler.services.ProgramRepository;

/**
 * Created by Manohar Prabhu on 6/1/2016.
 */
@Configuration
public class RabbitMQConfiguration {

    public static final String QUEUE_NAME = "queue";
    public static final String EXCHANGE_NAME = "exchange";
    public static final String METHOD_NAME = "receiveMessage";
    public static String MQ_HOST = "localhost";

    @Autowired
    private ProgramRepository programRepository;

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, false);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(QUEUE_NAME);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory(MQ_HOST);
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public MessageReceiverService receiver() {
        return new MessageReceiverService(this.programRepository);
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(MessageReceiverService receiver) {
        return new MessageListenerAdapter(receiver, METHOD_NAME);
    }
    

    public void setProgramRepository(ProgramRepository programRepository) {
		this.programRepository = programRepository;
	}

}
