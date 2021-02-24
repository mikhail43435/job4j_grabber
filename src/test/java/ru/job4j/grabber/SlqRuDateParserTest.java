package ru.job4j.grabber;

import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.text.ParseException;
import java.time.*;

public class SlqRuDateParserTest {

    @Test
    public void testSimpleDate() throws ParseException {
        String date = "17 окт 07, 01:49";
        assertThat(SlqRuDateParser.parseDate(date),
                is(LocalDate.of(2007, Month.OCTOBER, 17)));
    }

    @Test
    public void testToday() throws ParseException {
        String date = "сегодня, 08:38";
        assertThat(SlqRuDateParser.parseDate(date),
                is(ZonedDateTime.now(ZoneId.of("Europe/Moscow")).toLocalDate()));
    }

    @Test
    public void testYesterday() throws ParseException {
        String date = "вчера, 08:38";
        assertThat(SlqRuDateParser.parseDate(date),
                is(ZonedDateTime.now(ZoneId.of("Europe/Moscow")).minusDays(1).toLocalDate()));
    }

    @Test(expected = ParseException.class)
    public void testExceptionVar1() throws ParseException {
        String date = "вчера 08:38";
        SlqRuDateParser.parseDate(date);
    }

    @Test(expected = ParseException.class)
    public void testExceptionVar2() throws ParseException {
        String date = "17 фев, 14:53";
        System.out.println(SlqRuDateParser.parseDate(date));
    }

    @Test(expected = ParseException.class)
    public void testExceptionVar3() throws ParseException {
        String date = "17 rrr 21, 14:53";
        System.out.println(SlqRuDateParser.parseDate(date));
    }

    @Test(expected = ParseException.class)
    public void testExceptionVar4() throws ParseException {
        String date = "127 дек 21, 14:53";
        System.out.println(SlqRuDateParser.parseDate(date));
    }

    @Test(expected = ParseException.class)
    public void testExceptionVar5() throws ParseException {
        String date = "17 декк 231, 14:53";
        System.out.println(SlqRuDateParser.parseDate(date));
    }
}