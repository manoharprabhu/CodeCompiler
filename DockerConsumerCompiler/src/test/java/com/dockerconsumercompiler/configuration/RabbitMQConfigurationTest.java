package com.dockerconsumercompiler.configuration;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.dockerconsumercompiler.services.MessageReceiverService;
import com.dockerconsumercompiler.services.ProgramRepository;

/**
 * Created by Manohar Prabhu on 7/20/2016.
 */
public class RabbitMQConfigurationTest {
    @Test
    public void testRabbitMQConfiguration() {
    	ProgramRepository programRepository = Mockito.mock(ProgramRepository.class);
        RabbitMQConfiguration rabbitMQConfiguration = new RabbitMQConfiguration();
        rabbitMQConfiguration.setProgramRepository(programRepository);
        MessageReceiverService messageReceiverService = Mockito.mock(MessageReceiverService.class);
        Assert.assertEquals("queue", rabbitMQConfiguration.queue().getName());
        Assert.assertEquals("exchange", rabbitMQConfiguration.exchange().getName());
        Assert.assertEquals("receiveMessage", RabbitMQConfiguration.METHOD_NAME);
        Assert.assertEquals("exchange", rabbitMQConfiguration.binding(rabbitMQConfiguration.queue(), rabbitMQConfiguration.exchange()).getExchange()); 
        Assert.assertNotNull(rabbitMQConfiguration.binding(rabbitMQConfiguration.queue(), rabbitMQConfiguration.exchange()));
        Assert.assertNotNull(rabbitMQConfiguration.connectionFactory());
        Assert.assertNotNull(rabbitMQConfiguration.container(rabbitMQConfiguration.connectionFactory(), rabbitMQConfiguration.listenerAdapter(messageReceiverService)));
        Assert.assertEquals(MessageReceiverService.class, rabbitMQConfiguration.receiver().getClass());
        Assert.assertNotNull(rabbitMQConfiguration.listenerAdapter(messageReceiverService));
    }
}
