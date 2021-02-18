package ru.job4j.grabber;

public class Post {
    public String url;
    public String label;
    public String date;
    public String body;

    public Post(String url, String label, String date, String body) {
        this.url = url;
        this.label = label;
        this.date = date;
        this.body = body;
    }
}
