package org.buktify.springlify;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.bukkit.plugin.java.JavaPlugin;
import org.buktify.springlify.initializer.SpringlifyInitializer;
import org.springframework.context.ConfigurableApplicationContext;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@SuppressWarnings("unused")
public abstract class SpringlifyPlugin extends JavaPlugin {

    @NonFinal
    ConfigurableApplicationContext context;

    @Override
    public void onEnable() {
        context = SpringlifyInitializer.initialize(this, getApplicationClass());
    }

    @Override
    public void onDisable() {
        if (context == null) return;
        context.close();
        context = null;
    }

    public abstract Class<?> getApplicationClass();
}
