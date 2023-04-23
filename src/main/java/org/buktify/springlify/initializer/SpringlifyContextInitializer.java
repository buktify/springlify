package org.buktify.springlify.initializer;

import org.buktify.springlify.configuration.DatabaseConfiguration;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringlifyContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext context) {
        if (context instanceof AnnotationConfigApplicationContext annotationConfigApplicationContext) {
            annotationConfigApplicationContext.register(DatabaseConfiguration.class);
        }
    }
}
