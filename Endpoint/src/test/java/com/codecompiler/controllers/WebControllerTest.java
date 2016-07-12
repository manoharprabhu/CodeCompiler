package com.codecompiler.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

/**
 * Created by manoharprabhu on 14/6/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class WebControllerTest {
    @Test
    public void validateClasspathResources() {
        final String[] CLASSPATH_RESOURCE_LOCATIONS = {
                "classpath:/META-INF/resources/", "classpath:/resources/",
                "classpath:/static/", "classpath:/public/" };

        ResourceHandlerRegistry registry = Mockito.mock(ResourceHandlerRegistry.class);
        ResourceHandlerRegistration resourceHandlerRegistration = Mockito.mock(ResourceHandlerRegistration.class);
        Mockito.when(registry.addResourceHandler("/**")).thenReturn(resourceHandlerRegistration);

        WebController controller = new WebController();
        controller.addResourceHandlers(registry);
        Mockito.verify(resourceHandlerRegistration).addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }
}
