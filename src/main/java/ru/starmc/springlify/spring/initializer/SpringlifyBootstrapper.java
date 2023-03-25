package ru.starmc.springlify.spring.initializer;

import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import ru.starmc.springlify.DummySpringlifyPlugin;

import java.util.ArrayList;
import java.util.List;

@EnableAutoConfiguration()
public final class SpringlifyBootstrapper {

    private SpringlifyBootstrapper() {
    }

    public static ConfigurableApplicationContext initialize(JavaPlugin plugin, Class<?> appClass) {
        if (plugin.getClass().equals(appClass))
            throw new RuntimeException("Plugin cant be app class");

        List<ClassLoader> loaders = new ArrayList<>(4);

        loaders.add(plugin.getClass().getClassLoader());
        loaders.add(DummySpringlifyPlugin.class.getClassLoader());
        loaders.add(Thread.currentThread().getContextClassLoader());

        CompoundClassLoader classLoader = new CompoundClassLoader(loaders);

        return initialize(plugin, classLoader, appClass);
    }

    public static ConfigurableApplicationContext initialize(JavaPlugin plugin, ClassLoader classLoader, Class<?> appClass) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(appClass);
        builder.application().setMainApplicationClass(appClass);
        return builder
                .resourceLoader(new DefaultResourceLoader(classLoader))
                .initializers(new SpringlifyInitializer(plugin))
                .bannerMode(Banner.Mode.OFF)
                .run();
    }
}
