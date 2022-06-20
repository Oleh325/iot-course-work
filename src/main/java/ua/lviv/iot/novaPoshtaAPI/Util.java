package ua.lviv.iot.novaPoshtaAPI;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public final class Util {

    private Util() { }

    public static String getDateNow() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        return formatter.format(date);
    }

    public static File validateFile(String directoryPath, String objectPrefix) throws ParseException {
        File file = generateFile(directoryPath, objectPrefix);

        File directory = new File(directoryPath);
        if (directory.length() != 0) {
            file = findFile(directoryPath, objectPrefix);
        }

        return file;
    }

    public static File findFile(String directoryPath, String objectPrefix) throws ParseException {
        File file = new File("");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        for (LocalDate date = LocalDate.now(); date.isAfter(formatter.parse("1970-01-01").toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate()); date = date.minusDays(1)) {
            DateTimeFormatter pathFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String datePath = date.format(pathFormatter);

            if (Files.exists(Paths.get(directoryPath + objectPrefix + "-" + datePath + ".csv"))) {
                file = new File(directoryPath + objectPrefix + "-" + datePath + ".csv");
                break;
            }
        }

        return file;
    }

    public static File generateFile(String directoryPath, String objectPrefix) {
        String date = Util.getDateNow();

        return new File(directoryPath + objectPrefix + "-" + date + ".csv");
    }

    public static void generateDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    public static List<String> processRawValues(List<String> rawValues) {
        List<String> values = new LinkedList<>();
        boolean isList = false;
        String listValue = "";
        for (String value : rawValues) {
            if (value.equals("null")) {
                values.add("");
            } else if (value.contains("[") && value.contains("]")) {
                if (value.equals("[]")) {
                    values.add("");
                } else {
                    int i = 1;
                    while (value.charAt(i) != ']') {
                        listValue += value.charAt(i);
                        i++;
                    }
                    values.add(listValue);
                    isList = false;
                }
            } else if (value.contains("[")) {
                isList = true;
                for (int i = 1; i < value.length(); i++) {
                    listValue += value.charAt(i);
                }
                listValue += ", ";
            } else if (isList) {
                if (value.contains("]")) {
                    int i = 0;
                    while (value.charAt(i) != ']') {
                        listValue += value.charAt(i);
                        i++;
                    }
                    values.add(listValue);
                    isList = false;
                } else {
                    listValue += value + ", ";
                }
            } else {
                values.add(value);
            }
        }
        listValue = "";
        return values;
    }

}
