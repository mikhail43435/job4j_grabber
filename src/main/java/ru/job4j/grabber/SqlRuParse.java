package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.model.Parse;
import ru.job4j.grabber.model.Post;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class SqlRuParse implements Parse {

    public static void main(String[] args) throws Exception {
        for (int i = 1; i < 2; i++) {
            System.out.println("Printing page " + i + "...");
            System.out.println("-----------------");
            Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers/" + i).get();
            Elements el = doc.getElementsByTag("tr");
            for (Element elementTrTag : el) {
                Elements element1 = elementTrTag.getElementsByClass("postslisttopic");
                if (nonNull(element1.first()) && nonNull(elementTrTag.child(5))) {
                    String link = elementTrTag.child(1).getElementsByTag("a").attr("href");
                    System.out.println("Topic label: " + element1.first().text());
                    System.out.println("Date: " + SlqRuDateParser.parseDate(elementTrTag.child(5).text()));
                    System.out.println("URL: " + link);
                    System.out.println(getBody(link));
                    System.out.println("-----------------------------------------------------");
                }
            }
        }
    }

    private static String getBody(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        Elements el = doc.getElementsByClass("msgBody");
        return nonNull(el.get(1)) ? el.get(1).text() : "";
    }

    @Override
    public List<Post> list(String link) throws IOException, ParseException {
        List<Post> list = new ArrayList<>();
        Document doc = Jsoup.connect(link).get();
        Elements el = doc.getElementsByTag("tr");
        for (Element elementTrTag : el) {
            Elements element1;
                element1 = elementTrTag.getElementsByClass("postslisttopic");
            if (nonNull(element1.first()) && nonNull(elementTrTag.child(5))) {
                String postLink = elementTrTag.child(1).getElementsByTag("a").attr("href");
                list.add(detail(postLink));
            }
        }
        return list;
    }

    @Override
    public Post detail(String link) throws IOException, ParseException {
        Document doc = Jsoup.connect(link).get();
        Elements elBody = doc.getElementsByClass("msgBody");
        Elements elHeader = doc.getElementsByClass("messageHeader");
        Elements elFooter = doc.getElementsByClass("msgFooter");
        if (elHeader.isEmpty()
                || elFooter.isEmpty()
                || elBody.isEmpty()
                || isNull(elHeader.get(0))
                || isNull(elFooter.get(0))
                || isNull(elBody.get(1))) {
            throw new ParseException("Ошибка парсинга страницы: " + link, 0);
        }
        return new Post(link,
                elHeader.get(0).text(),
                SlqRuDateParser.
                        parseDate(elFooter.get(0).text().substring(0, elFooter.get(0).text().indexOf(" ["))),
                elBody.get(1).text());
    }
}