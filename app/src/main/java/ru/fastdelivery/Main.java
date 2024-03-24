package ru.fastdelivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import ru.fastdelivery.config.Beans;

/**
 * Класс запускающий приложение
 */
@SpringBootApplication(scanBasePackages = { "ru.fastdelivery" })
@ConfigurationPropertiesScan(value = { "ru.fastdelivery.properties" })
@EnableConfigurationProperties
@Import(Beans.class)
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}