package ru.starmc.springlify.spring.configuration;

import org.bukkit.configuration.Configuration;
import org.springframework.core.env.PropertySource;

public class ConfigurationPropertySource extends PropertySource<Configuration> {

    public ConfigurationPropertySource(String name, Configuration source) {
        super("springlify_" + name, source);
    }

    @Override
    public Object getProperty(String s) {
        return source.get(s);
    }
}
