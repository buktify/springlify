package org.buktify.springlify.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j2
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ListenableServiceInitializer implements DestructionAwareBeanPostProcessor {

    JavaPlugin plugin;

    @Nullable
    private Listener getListener(@NotNull Object bean) {
        if (bean instanceof Listener listener) return listener;
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
        Listener listener = getListener(bean);
        if (listener != null) {
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
            log.warn("Registered " + bean.getClass().getSimpleName() + " as event listener.");
        }
        return bean;
    }

    @Override
    public void postProcessBeforeDestruction(@NotNull Object bean, @NotNull String beanName) throws BeansException {
        Listener listener = getListener(bean);
        if (listener != null) HandlerList.unregisterAll(listener);
    }
}
