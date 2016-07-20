package com.codecompiler.configuration;

import org.junit.Assert;
import org.junit.Test;


/**
 * Created by Manohar Prabhu on 6/1/2016.
 */
public class RabbitMQConfigurationTest {
    @Test
    public void testRabbitMQConfiguration() {
        RabbitMQConfiguration rabbitMQConfiguration = new RabbitMQConfiguration();
        Assert.assertEquals("queue", rabbitMQConfiguration.queue().getName());
        Assert.assertEquals("exchange", rabbitMQConfiguration.exchange().getName());
        Assert.assertEquals("receiveMessage", RabbitMQConfiguration.METHOD_NAME);
        Assert.assertEquals("exchange", rabbitMQConfiguration.binding(rabbitMQConfiguration.queue(), rabbitMQConfiguration.exchange()).getExchange()); 
        Assert.assertNotNull(rabbitMQConfiguration.binding(rabbitMQConfiguration.queue(), rabbitMQConfiguration.exchange()));
        Assert.assertNotNull(rabbitMQConfiguration.connectionFactory());
    }
}
