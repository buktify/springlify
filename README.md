# Springlify

Данная библиотека позволяет использовать Spring для написания Spigot плагинов.

Чтобы иметь возможность использовать эту библиотеку, нужно просто подключить её как implementation.
Так-же, не забудьте подключить сам стартер Spring'a (куда-же без него)

```groovy
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.0.4'
    id 'io.spring.dependency-management' version '1.1.0'
}

repositories {
    maven {
        name = 'buktify-repo'
        url = 'https://repo.crazylegend.space/public'
    }
}

dependencies {
    compileOnly 'org.buktify:springlify:1.0.3'
    compileOnly 'org.springframework.boot:spring-boot-starter'
}
```

Дальше, нужно создать класс Spring приложения.

```java

@SpringBootApplication(scanBasePackages = "org.buktify.test")
public class YourPluginApplication {

}
```

Затем - главный класс вашего плагина.
Главный класс Spigot плагина должен быть отдельно от главного класса Spring приложения.

Метод getApplicationClass() возвращает главный класс Spigot приложения.

```java
public class YourPlugin extends SpringlifyPlugin {

    @Override
    protected Class<?> getApplicationClass() {
        return YourPluginApplication.class;
    }
}
```

Готово, можете реализовывать основной функционал вашего плагина.
В библиотеке есть небольшие дополнения к Spring'y, для облегчения работы с API Spigot'a.

### Слушатели событий

Вам не нужно регистрировать слушатели событий в PluginManager'е, просто имплементируете Listener.
При инициализации бинов Spring сам их зарегистрирует.

```java

@Service
public class SomeService implements Listener {
    // Реализация
}
```

### Команды

При инициализации бинов Spring сам зарегистрирует обработчики команд.
Не забудьте добавить их в plugin.yml

```java

@SpigotCommand(command = "test")
public class TestCommand implements CommandExecutor {
    // Реализация
}
```

### Конфигурации плагина

Настоятельно не рекомендую использовать Spigot конфиги, и application.yml для реализации конфигураций плагина.
Как вариант, используйте одну из наших библиотек - [Configurate](https://github.com/buktify/configurate), она довольно
неплохо работает со Spring'ом.

Пример объявления бинов конфигураций.
Не забудьте ознакомиться с документацией самого Configurate, если будете использовать.

```java
    @Bean
@SneakyThrows
public DatabaseSettings databaseSettings(@NotNull ConfigurationService configurationService){
        return configurationService.getConfigurationPool().get(DatabaseSettings.class);
}

@Bean
public ConfigurationService configurationService(@NotNull JavaPlugin javaPlugin){
        ConfigurationService configurationService=new ConfigurationService()
        .rootDirectory(javaPlugin.getDataFolder())
        .registerConfigurations(DatabaseSettings.class);
        configurationService.apply();
        return configurationService;
}
```