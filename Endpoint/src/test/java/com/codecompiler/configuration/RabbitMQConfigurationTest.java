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
        Assert.assertTrue("queue".equals(rabbitMQConfiguration.queue().getName()));
        Assert.assertTrue("exchange".equals(rabbitMQConfiguration.exchange().getName()));
        Assert.assertTrue("receiveMessage".equals(rabbitMQConfiguration.METHOD_NAME));
        Assert.assertNotNull(rabbitMQConfiguration.binding(rabbitMQConfiguration.queue(), rabbitMQConfiguration.exchange()));
        Assert.assertNotNull(rabbitMQConfiguration.connectionFactory());
    }
}
