package ru.job4j.grabber;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;

public final class SlqRuDateParser {

    private static final Map<String, String> MAP_MONTHS = new HashMap<>();
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy").withZone(ZoneId.systemDefault());
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd.MM.yyyy");

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

    public static Date parseDate(String textDate) throws ParseException {
        String[] stringArray = textDate.split(",");
        String[] stringArray2 = stringArray[0].split(" ");
        if (stringArray.length != 2
                || (stringArray2.length != 1 && stringArray2.length != 3)
                || ((stringArray2.length == 3) && (stringArray2[0].length() != 2
                        || stringArray2[1].length() != 3
                        || stringArray2[2].length() != 2))) {
            throw new ParseException("Ошибка парсинга даты: " + textDate, 0);
        }
        return stringArray2.length == 3
                ? FORMATTER.parse(getDateLongDate(stringArray2)) : FORMATTER.parse(getDateShortDate(stringArray));
    }

    private static String getDateShortDate(String... stringArray) {
        String date = "";
        if (stringArray[0].contains("сегодня")) {
            return DATE_TIME_FORMATTER.format(Instant.now());
        } else if (stringArray[0].contains("вчера")) {
            return DATE_TIME_FORMATTER.format(Instant.now().minus(1, ChronoUnit.DAYS));
        }
        return date;
    }

    private static String getDateLongDate(String... stringArray) {
        String date = "";
        String month = MAP_MONTHS.get(stringArray[1]);
        if (nonNull(month)) {
            date = (stringArray[0].length() == 1 ? "0" : "")
                    + stringArray[0]
                    + "."
                    + month
                    + ".20"
                    + stringArray[2];
        }
        return date;
    }
}