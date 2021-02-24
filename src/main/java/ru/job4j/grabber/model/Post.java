package ru.job4j.grabber.model;

import java.time.LocalDate;

public class Post {
    public String url;
    public String name;
    public LocalDate date;
    public String body;

    @Override
    public String toString() {
        return "Post{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", body='" + body + '\'' +
                '}';
    }

    public Post(String url, String name, LocalDate date, String body) {
        this.url = url;
        this.name = name;
        this.date = date;
        this.body = body;
    }
}
