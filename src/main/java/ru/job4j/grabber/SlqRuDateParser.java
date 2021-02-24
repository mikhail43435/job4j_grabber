package ru.job4j.grabber;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

public final class SlqRuDateParser {

    private static final Map<String, String> MAP_MONTHS = new HashMap<>();

    static {
        MAP_MONTHS.put("янв", "01");
        MAP_MONTHS.put("фев", "02");
        MAP_MONTHS.put("мар", "03");
        MAP_MONTHS.put("апр", "04");
        MAP_MONTHS.put("май", "05");
        MAP_MONTHS.put("июн", "06");
        MAP_MONTHS.put("июл", "07");
        MAP_MONTHS.put("авг", "08");
        MAP_MONTHS.put("сен", "09");
        MAP_MONTHS.put("окт", "10");
        MAP_MONTHS.put("ноя", "11");
        MAP_MONTHS.put("дек", "12");
    }

    private SlqRuDateParser() {
        //not called
    }

    public static LocalDate parseDate(String textDate) throws ParseException {
        String patternStandardDate = "\\d?\\d\\s[а-я]{3}+\\s\\d\\d,\\s\\d\\d:\\d\\d";
        String patternToday = "сегодня,\\s\\d\\d:\\d\\d";
        String patternYesterday = "вчера,\\s\\d\\d:\\d\\d";
        if (Pattern.matches(patternStandardDate, textDate)) {
            return getLongDate(textDate);
        } else if (Pattern.matches(patternToday, textDate)) {
            return ZonedDateTime.now(ZoneId.of("Europe/Moscow")).toLocalDate();
        } else if (Pattern.matches(patternYesterday, textDate)) {
            return ZonedDateTime.now(ZoneId.of("Europe/Moscow")).minusDays(1).toLocalDate();
        }
        throw new ParseException("Ошибка парсинга даты >" + textDate + "<", 0);
    }

    private static LocalDate getLongDate(String textDate) throws ParseException {
        String date;
        String[] stringArray = textDate.replace(",", "").split("\\s");
        String month = MAP_MONTHS.get(stringArray[1]);
        if (isNull(month)) {
            throw new ParseException("Ошибка парсинга даты >" + textDate + "< . Не распознан месяц :" + month, 0);
        }
        date = (stringArray[0].length() == 1 ? "0" : "")
                + stringArray[0]
                + "."
                + month
                + ".20"
                + stringArray[2];
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}