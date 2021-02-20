package ru.job4j.grabber;

import org.junit.Test;
import ru.job4j.grabber.model.Post;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

/*    @Test
    public void thenPostStandartDate() throws IOException, ParseException, SQLException {
        PsqlStore store = new PsqlStore(this.init());
        String url = "https://www.sql.ru/forum/1328546-1/administrator-baz-dannyh-oracle-200-000-250-000-rub-moskva";
        SqlRuParse parser = new SqlRuParse();
        Post post = parser.detail(url);
        store.save(post);
        System.out.println(store.findById(url).url);

    }*/

    @Test
    public void thenFindAll() {
        PsqlStore store = new PsqlStore(this.init());
        assertThat(store.getAll().size(), is(1));
    }

    @Test
    public void thenFindByIdAndSave() throws Exception {
        try (PsqlStore store = new PsqlStore(ConnectionRollback.create(this.init()))) {
            String url = "www.test_001";
            String name = "test name";

            Post post = new Post(url,
                    name,
                    new SimpleDateFormat("yyyy-MM-dd").parse("2007-10-17 00:00"),
                    "test body");
            store.save(post);
            assertThat(store.findById(url).name, is(name));
        }
    }
}