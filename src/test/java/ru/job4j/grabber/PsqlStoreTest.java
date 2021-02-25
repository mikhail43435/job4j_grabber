package ru.job4j.grabber;

import org.junit.Test;
import ru.job4j.grabber.model.Post;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.time.LocalDate;
import java.time.Month;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;

public class PsqlStoreTest {

    public Connection init() {
        try {
            Properties config = PropLoader.load("rabbit.properties");
            Class.forName(config.getProperty("driver-class-name"));
            return DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void thenFindAll() throws Exception {
        try (PsqlStore store = new PsqlStore(ConnectionRollback.create(this.init()))) {
            String url = "www.test_001";
            String name = "test name 1234567890"
                    + "12345678901234567890123456789012345678901234567890123456789012345678901234567890"
                    + "12345678901234567890123456789012345678901234567890123456789012345678901234567890"
                    + "12345678901234567890123456789012345678901234567890123456789012345678901234567890"
                    + "12345678901234567890123456789012345678901234567890123456789012345678901234567890";
            Post post = new Post(url,
                    name,
                    LocalDate.of(2007, Month.DECEMBER, 17),
                    "test body");
            store.save(post);
            assertThat(store.getAll().size(), is(1));
        }
    }

    @Test
    public void thenFindById() throws Exception {
        try (PsqlStore store = new PsqlStore(ConnectionRollback.create(this.init()))) {
            String url = "www.test_001";
            String name = "test name";
            Post post = new Post(url,
                    name,
                    LocalDate.of(2007, 12, 17),
                    "test body");
            store.save(post);
            assertThat(store.findById(String.valueOf(post.id)).id, is(post.id));
        }
    }
}