package ru.starmc.springlify.spring.initializer;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import ru.starmc.springlify.DummySpringlifyPlugin;
import ru.starmc.springlify.spring.configuration.ConfigurationPropertySource;
import ru.starmc.springlify.spring.configuration.SpringlifyConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Initializer that set core properties and adds config yml source
 */
public class SpringlifyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private final Plugin plugin;

    public SpringlifyInitializer(Plugin plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("ConstantConditions")
    public void initialize(ConfigurableApplicationContext context) {
        MutablePropertySources propertySources = context.getEnvironment().getPropertySources();
        ConfigurationPropertySource builtInConfiguration = new ConfigurationPropertySource(
                "SpringlifyBuiltIn",
                YamlConfiguration.loadConfiguration(
                        new InputStreamReader(
                                JavaPlugin
                                        .getPlugin(DummySpringlifyPlugin.class)
                                        .getResource("application.yml")
                        ))
        );

        InputStream applicationYamlInput = plugin.getResource("application.yml");

        if (applicationYamlInput != null) {
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(new InputStreamReader(applicationYamlInput));
            ConfigurationPropertySource innerPluginConfig = new ConfigurationPropertySource("application", configuration);

            propertySources.addFirst(innerPluginConfig);

            String springConfig = configuration.getString("springlify.config");

            if (springConfig != null) {
                plugin.saveResource(springConfig, false);

                File springPluginConfig = new File(plugin.getDataFolder(), springConfig);
                if (springPluginConfig.isFile()) {

                    YamlConfiguration pluginSpringConfiguration = YamlConfiguration.loadConfiguration(springPluginConfig);
                    propertySources.addFirst(new ConfigurationPropertySource("config", pluginSpringConfiguration));
                }
            }
        }
        propertySources.addLast(builtInConfiguration);
        context.getBeanFactory().registerSingleton(plugin.getClass().getCanonicalName(), plugin);

        if (context instanceof AnnotationConfigApplicationContext) {
            ((AnnotationConfigApplicationContext) context).register(SpringlifyConfiguration.class);
        }
    }

}