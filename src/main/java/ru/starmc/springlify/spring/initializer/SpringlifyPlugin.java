package ru.starmc.springlify.spring.initializer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.context.ConfigurableApplicationContext;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class SpringlifyPlugin extends JavaPlugin {
    ConfigurableApplicationContext context;

    @Override
    public void onEnable() {
        this.context = SpringlifyBootstrap.initialize(this, getApplicationClass());
    }

    @Override
    public void onDisable() {
        if (context != null) {
            context.close();
            context = null;
        }
    }

    public <T> T getService(Class<T> serviceClass) {
        return context.getBean(serviceClass);
    }

    protected abstract Class<?> getApplicationClass();
}
