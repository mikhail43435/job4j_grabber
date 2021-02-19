package ru.job4j.grabber;

import org.junit.Test;
import ru.job4j.grabber.model.Post;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class SqlRuParseTest {

    @Test
    public void thenPostStandartDate() throws IOException, ParseException {
        SqlRuParse parser = new SqlRuParse();
        String url = "https://www.sql.ru/forum/1328546-1/administrator-baz-dannyh-oracle-200-000-250-000-rub-moskva";
        Post post = parser.detail(url);
        assertThat(post.url, is(url));
        assertThat(post.name, is("Администратор баз данных Oracle (200 000 - 250 000 руб.), Москва [new]"));
        assertThat(post.date.toString(), is("Tue Aug 25 00:00:00 MSK 2020"));
        assertThat(post.body.length(), is(1390));
    }

    @Test
    public void thenPostYesterdayDate() throws IOException, ParseException {
        SqlRuParse parser = new SqlRuParse();
        String url = "https://www.sql.ru/forum/1333583/beeline-starshiy-ekspert-analitik-po-planirovaniu-i-budzhetirovaniu";
        Post post = parser.detail(url);
        assertThat(post.url, is(url));
        assertThat(post.name, is("Beeline Старший эксперт-аналитик по планированию и бюджетированию [new]"));
        assertThat(post.date.toString(), is("Thu Feb 18 00:00:00 MSK 2021"));
        assertThat(post.body.length(), is(754));
    }

    @Test(expected = ParseException.class)
    public void thenPostParseException() throws IOException, ParseException {
        SqlRuParse parser = new SqlRuParse();
        String url = null;
        Post post = parser.detail("https://www.sql.ru/forum/job-offers");
    }

    @Test
    public void thenList() throws IOException, ParseException {
        SqlRuParse parser = new SqlRuParse();
        String url = "https://www.sql.ru/forum/job-offers/1";
        List<Post> list = parser.list(url);
        assertThat(list.size(), is(53));
        assertThat(list.get(0).name, is("Сообщения от модераторов (здесь Вы можете узнать причины удаления топиков) [new]"));
    }
}