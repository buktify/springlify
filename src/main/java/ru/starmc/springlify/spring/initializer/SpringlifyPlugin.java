package ru.starmc.springlify.spring.initializer;

import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.context.ConfigurableApplicationContext;

@RequiredArgsConstructor
public abstract class SpringlifyPlugin extends JavaPlugin {
    private ConfigurableApplicationContext context;

    @Override
    public void onEnable() {
        this.context = SpringlifyBootstrapper.initialize(this, getAppClass());
    }

    @Override
    public void onDisable() {
        if (context != null) {
            context.close();
            context = null;
        }
    }

    public <T> T getService(Class<T> serviceClass) {
        if (allowExternalAccess(serviceClass))
            return context.getBean(serviceClass);
        else
            return null;
    }

    protected boolean allowExternalAccess(Class<?> needClass) {
        return true;
    }

    protected abstract Class<?> getAppClass();
}
