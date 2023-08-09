package org.buktify.springlify.commands;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.buktify.springlify.exception.ServiceException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SpigotTabCompleterInitializer implements BeanPostProcessor {

    JavaPlugin plugin;

    @Nullable
    private TabCompleter getTabCompleter(@NotNull Object bean) throws ServiceException {
        if (bean.getClass().isAnnotationPresent(SpigotCommand.class)) {
            if (bean instanceof TabCompleter commandExecutor) {
                return commandExecutor;
            }
        }
        if (bean instanceof TabCompleter && !bean.getClass().isAnnotationPresent(SpigotCommand.class)) {
            log.warn("Service " + bean.getClass().getSimpleName() + " implements TabCompleter but not annotated with @SpigotCommand, so it wouldn't be registered");
        }
        return null;
    }


    @Override
    @SneakyThrows(ServiceException.class)
    public Object postProcessAfterInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
        TabCompleter tabCompleter = getTabCompleter(bean);
        if (tabCompleter == null) return null;
        SpigotCommand commandAnnotation = tabCompleter.getClass().getAnnotation(SpigotCommand.class);
        PluginCommand command = plugin.getCommand(commandAnnotation.command());
        if (command != null) {
            log.warn("Registered " + bean.getClass().getSimpleName() + " as tab completer.");
            command.setTabCompleter(tabCompleter);
        } else log.warn("You forgot add command '" + commandAnnotation.command() + "' to plugin.yml");
        return bean;
    }
}
