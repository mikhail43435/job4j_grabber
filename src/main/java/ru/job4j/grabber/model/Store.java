package ru.job4j.grabber.model;

import java.sql.SQLException;
import java.util.List;

public interface Store {

    void save(Post post);

    List<Post> getAll();

    Post findById(String id) throws SQLException;
}