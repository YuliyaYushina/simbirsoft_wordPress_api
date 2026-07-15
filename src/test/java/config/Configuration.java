package config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config.properties")
public interface Configuration extends Config {
    String basePath();

    String baseUrl();

    String user();

    String password();
}
