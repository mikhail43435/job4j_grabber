package ru.job4j.grabber;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;

public final class SlqRuDateParser {

    private static final Map<String, String> mapMonths = new HashMap<>();
    private static final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneId.systemDefault());

    static {
        mapMonths.put("янв", "01");
        mapMonths.put("фев", "02");
        mapMonths.put("мар", "03");
        mapMonths.put("апр", "04");
        mapMonths.put("май", "05");
        mapMonths.put("июн", "06");
        mapMonths.put("июл", "07");
        mapMonths.put("авг", "08");
        mapMonths.put("сен", "09");
        mapMonths.put("окт", "10");
        mapMonths.put("ноя", "11");
        mapMonths.put("дек", "12");
    }

    private SlqRuDateParser() {
        //not called
    }

    public static String parseDate(String textDate) {
        String[] stringArray = textDate.split(",");
        if (stringArray.length < 2) {
            return "";
        }
        String[] stringArray2 = stringArray[0].split(" ");
        return stringArray2.length == 3 ? getDateLongDate(stringArray2) : getDateShortDate(stringArray);
    }

    private static String getDateShortDate(String... stringArray) {
        String date = "";
        if (stringArray[0].contains("сегодня")) {
            Instant instantDateTime = Instant.now();
            date = dateTimeFormatter.format(instantDateTime);

        } else if (stringArray[0].contains("вчера")) {
            Instant instantDateTime = Instant.now().minus(1, ChronoUnit.DAYS);
            date = dateTimeFormatter.format(instantDateTime);
        }
        return date;
    }

    private static String getDateLongDate(String... stringArray) {
        String date = "";
        String month = mapMonths.get(stringArray[1]);
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