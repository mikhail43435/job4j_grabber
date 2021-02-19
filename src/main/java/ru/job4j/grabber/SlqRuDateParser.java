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
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
    private static final Instant TODAY_INSTANT, YESTERDAY_INSTANT;
    private static final String TODAY, YESTERDAY;

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
        TODAY_INSTANT = Instant.now();
        YESTERDAY_INSTANT = TODAY_INSTANT.minus(1, ChronoUnit.DAYS);
        TODAY = DATE_TIME_FORMATTER.format(TODAY_INSTANT);
        YESTERDAY = DATE_TIME_FORMATTER.format(YESTERDAY_INSTANT);
    }

    private SlqRuDateParser() {
        //not called
    }

    public static Date parseDate(String textDate) throws ParseException {

        String[] stringArray = textDate.split(",");
        if (stringArray.length < 2) {
            return null;
        }
        String[] stringArray2 = stringArray[0].split(" ");
        return stringArray2.length == 3
                ? formatter.parse(getDateLongDate(stringArray2)) : formatter.parse(getDateShortDate(stringArray));
    }

    private static String getDateShortDate(String... stringArray) {
        String date = "";
        if (stringArray[0].contains("сегодня")) {
            return TODAY;
        } else if (stringArray[0].contains("вчера")) {
            return YESTERDAY;
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