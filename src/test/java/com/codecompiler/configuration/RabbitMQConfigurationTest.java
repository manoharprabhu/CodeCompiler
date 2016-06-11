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
        Assert.assertTrue(rabbitMQConfiguration.queue().getName().equals("queue"));
        Assert.assertTrue(rabbitMQConfiguration.exchange().getName().equals("exchange"));
        Assert.assertTrue(rabbitMQConfiguration.METHOD_NAME.equals("receiveMessage"));
        Assert.assertNotNull(rabbitMQConfiguration.binding(rabbitMQConfiguration.queue(), rabbitMQConfiguration.exchange()));
        Assert.assertNotNull(rabbitMQConfiguration.connectionFactory());
    }
}
