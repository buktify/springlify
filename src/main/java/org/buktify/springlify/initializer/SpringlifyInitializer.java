package org.buktify.springlify.initializer;

import lombok.experimental.UtilityClass;
import org.bukkit.plugin.java.JavaPlugin;
import org.buktify.springlify.commands.SpigotCommandInitializer;
import org.buktify.springlify.listener.ListenableServiceInitializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.Banner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class SpringlifyInitializer {

    public static ConfigurableApplicationContext initialize(@NotNull JavaPlugin plugin, @NotNull Class<?> applicationClass) {
        List<ClassLoader> loaders = new ArrayList<>(4);
        loaders.add(plugin.getClass().getClassLoader());
        loaders.add(Thread.currentThread().getContextClassLoader());
        CompoundClassLoader classLoader = new CompoundClassLoader(loaders);
        return new SpringApplicationBuilder(applicationClass)
                .initializers((ApplicationContextInitializer<GenericApplicationContext>) applicationContext -> {
                    applicationContext.registerBean(SpigotCommandInitializer.class, () -> new SpigotCommandInitializer(plugin));
                    applicationContext.registerBean(ListenableServiceInitializer.class, () -> new ListenableServiceInitializer(plugin));
                })
                .resourceLoader(new DefaultResourceLoader(classLoader))
                .bannerMode(Banner.Mode.OFF)
                .run();
    }
}
