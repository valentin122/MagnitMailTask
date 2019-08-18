package ru.vzhigalov.servise;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private final Properties values = new Properties();

    public void init() {
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream("app.properties")) {
            values.load(in);
        } catch (IOException e) {
            System.out.println("Can't read file properties. Please check existence of the correct file!");
        }
    }

    public String get(String key) {
        return this.values.getProperty(key);
    }
}
