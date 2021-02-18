package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import static java.util.Objects.nonNull;

public class SqlRuParse {

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
        return el.get(1).text();
    }
}