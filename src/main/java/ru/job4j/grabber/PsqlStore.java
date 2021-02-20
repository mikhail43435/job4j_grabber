package ru.job4j.grabber;

import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.model.Store;

import java.sql.*;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private Connection connection;

    public PsqlStore(Properties config) {
        try {
            connection = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement statement =
                     connection.prepareStatement("insert into post"
                             + "(name, text, link, created_date) values (?, ?, ?, ?)");) {
            statement.setString(1, post.name);
            statement.setString(2, post.body);
            statement.setString(3, post.url);
            statement.setTimestamp(4, new java.sql.Timestamp(post.date.getTime()));
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Post> getAll() {
        return null;
    }

    @Override
    public Post findById(String id) {
        return null;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}