package ru.job4j.grabber;

import java.io.InputStream;
import java.util.Properties;

public class LoadProperties {

    /**
     * Метод загружает и передает файл свойств
     * по полученному имени
     * @param propName имя файла свойст
     * @return объект типа Properties
     */

    public static Properties load(String propName) {
        Properties config = new Properties();
        try (InputStream inputStream =
                     AlertRabbit.class.getClassLoader().getResourceAsStream(propName)) {
            config.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }
}
