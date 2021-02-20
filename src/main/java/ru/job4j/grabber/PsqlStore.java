package ru.job4j.grabber;

import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.model.Store;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private final Connection connection;

    public PsqlStore(Connection connection) {
        this.connection = connection;
    }

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
                             + "(name, text, link, created_date) values (?, ?, ?, ?)")) {
            statement.setString(1, post.name.substring(0, Math.min(post.name.length(), 200)));
            statement.setString(2, post.body.substring(0, Math.min(post.body.length(), 10000)));
            statement.setString(3, post.url.substring(0, Math.min(post.url.length(), 255)));
            statement.setTimestamp(4, new java.sql.Timestamp(post.date.getTime()));
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("select * from post")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    posts.add(new Post(resultSet.getString("link"),
                                    resultSet.getString("name"),
                                    resultSet.getDate("created_date"),
                                    resultSet.getString("text")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post findById(String id) throws SQLException {
        Post post = null;
        try (PreparedStatement statement =
                     connection.prepareStatement("select * from post where link=?;")) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    post = new Post(resultSet.getString("link"),
                            resultSet.getString("name"),
                            resultSet.getDate("created_date"),
                            resultSet.getString("text")
                    );
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return post;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}