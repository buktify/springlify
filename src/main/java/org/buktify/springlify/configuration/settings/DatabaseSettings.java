package org.buktify.springlify.configuration.settings;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.buktify.configurate.annotation.Configuration;
import org.buktify.configurate.annotation.Variable;

import java.util.List;

@Configuration(
        fileName = "database.yml",
        filePath = "%plugin_root%/%filename%"
)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@SuppressWarnings("FieldMayBeFinal")
public class DatabaseSettings {

    @Variable("hibernate.driver")
    String driver = "com.mysql.cj.jdbc.Driver";
    @Variable("hibernate.url")
    String url = "jdbc:mysql://localhost:3306/jedicraft-test";
    @Variable("hibernate.username")
    String username = "coolpex";
    @Variable("hibernate.password")
    String password = "uQwkDYAkHNXb";
    @Variable("hibernate.additional-properties")
    List<String> additionalProperties = List.of(
            "hibernate.hbm2ddl.auto:update",
            "hibernate.show_sql:true"
    );
}
