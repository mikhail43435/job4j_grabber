package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static java.util.Objects.nonNull;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements el = doc.getElementsByTag("tr");
        for (Element elementTrTag : el) {
            Elements element1 = elementTrTag.getElementsByClass("postslisttopic");
            if (nonNull(element1.first()) && nonNull(elementTrTag.child(5))) {
                System.out.println("Topic label: " + element1.first().text());
                System.out.println("Post date: " + elementTrTag.child(5).text());
                System.out.println("-----------------");
            }
       }
    }
}