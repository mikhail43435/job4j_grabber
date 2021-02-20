package ru.job4j.grabber;

import org.junit.Test;
import ru.job4j.grabber.model.Post;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Properties;


public class PsqlStoreTest {

/*    @Test
    public void thenPostStandartDate() throws IOException, ParseException {
        Properties config = LoadProperties.load("rabbit.properties");
        PsqlStore store = new PsqlStore(config);
        String url = "https://www.sql.ru/forum/1328546-1/administrator-baz-dannyh-oracle-200-000-250-000-rub-moskva";
        SqlRuParse parser = new SqlRuParse();
        Post post = parser.detail(url);
        store.save(post);

        //assertThat(post.url, is(url));
    }*/
}