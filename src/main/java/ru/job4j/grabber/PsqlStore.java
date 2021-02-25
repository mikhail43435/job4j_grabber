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
        normalizePost(post);
        try (PreparedStatement statement =
                     connection.prepareStatement("INSERT INTO POST"
                             + "(name, text, link, created_date) VALUES (?, ?, ?, ?)" +
                             "ON CONFLICT DO NOTHING", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, post.name);
            statement.setString(2, post.body);
            statement.setString(3, post.url);
            statement.setTimestamp(4, Timestamp.valueOf(post.date.atStartOfDay()));
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                post.id = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Post normalizePost(Post post) {
        post.name = post.name.substring(0, Math.min(post.name.length(), 200));
        post.body = post.body.substring(0, Math.min(post.body.length(), 10000));
        post.url = post.url.substring(0, Math.min(post.url.length(), 255));
        return post;
    }

    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("select * from post")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    posts.add(new Post(resultSet.getString("link"),
                                    resultSet.getString("name"),
                                    resultSet.getDate("created_date").toLocalDate(),
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
                     connection.prepareStatement("select * from post where id=?;")) {
            statement.setInt(1, Integer.parseInt(id));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    post = new Post(resultSet.getInt("id"),
                            resultSet.getString("link"),
                            resultSet.getString("name"),
                            resultSet.getDate("created_date").toLocalDate(),
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