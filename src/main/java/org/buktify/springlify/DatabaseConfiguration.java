package org.buktify.springlify;

import dev.temez.jedicraft.configurate.ConfigurationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.bukkit.plugin.java.JavaPlugin;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DatabaseConfiguration {

    @Bean
    public DatabaseSettings databaseSettings(@NotNull JavaPlugin javaPlugin){
        DatabaseSettings databaseSettings = new DatabaseSettings();
        new ConfigurationService()
                .rootDirectory(javaPlugin.getDataFolder())
                .registerConfiguration(databaseSettings)
                .apply();
        return databaseSettings;
    }


    @Bean
    public HashMap<Object, Object> hibernateProperties(@NotNull DatabaseSettings databaseSettings) {
        return new HashMap<>() {
            {
                put("hibernate.connection.driver", databaseSettings.getDriver());
                put("hibernate.connection.url", databaseSettings.getUrl());
                put("hibernate.connection.username", databaseSettings.getUsername());
                put("hibernate.connection.password", databaseSettings.getPassword());

                for (String option : databaseSettings.getAdditionalProperties()){
                    String[] optionsArray = option.split(":");
                    put(optionsArray[0], optionsArray[1]);
                }
            }
        };
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(@NotNull Map<Object, Object> hibernateProperties) {
        return new HibernatePersistenceProvider().createEntityManagerFactory("Dummy", hibernateProperties);
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }
}
