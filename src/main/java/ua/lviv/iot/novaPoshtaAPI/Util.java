package ua.lviv.iot.novaPoshtaAPI;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public final class Util {

    private Util() { }

    public static String getDateNow() {
        String year = Integer.toString(LocalDate.now().getYear());
        String month;
        String day;

        if (LocalDate.now().getMonthValue() < 10) {
            month = "0" + LocalDate.now().getMonthValue();
        } else {
            month = Integer.toString(LocalDate.now().getMonthValue());
        }
        if (LocalDate.now().getDayOfMonth() < 10) {
            day = "0" + LocalDate.now().getDayOfMonth();
        } else {
            day = Integer.toString(LocalDate.now().getDayOfMonth());
        }

        return year + "-" + month + "-" + day;
    }

    public static File validateFile(String directoryPath, String objectPrefix) {
        File file = generateFile(directoryPath, objectPrefix);

        File directory = new File(directoryPath);
        if (directory.length() != 0) {
            for (int year = LocalDate.now().getYear(); year > 1970; year--) {
                for (int month = 12; month > 0; month--) {
                    file = findFile(directoryPath, objectPrefix, year, month);
                    if (file.length() != 0) {
                        break;
                    }
                }
                if (file.length() != 0) {
                    break;
                }
            }
        }

        return file;
    }

    public static File findFile(String directoryPath, String objectPrefix, int yearInt, int monthInt) {
        File file = new File("");
        String year = Integer.toString(yearInt);
        String month;
        if (monthInt < 10) {
            month = "0" + monthInt;
        } else {
            month = Integer.toString(monthInt);
        }

        for (int day = LocalDate.now().getDayOfMonth(); day > 0; day--) {
            if (day < 10) {
                if (Files.exists(Paths.get(directoryPath + objectPrefix + "-" + year + "-"
                        + month + "-0" + day + ".csv"))) {
                    file = new File(directoryPath + objectPrefix + "-" + year + "-"
                            + month + "-0" + day + ".csv");
                    break;
                }
            } else {
                if (Files.exists(Paths.get(directoryPath + objectPrefix + "-" + year + "-"
                        + month + "-" + day + ".csv"))) {
                    file = new File(directoryPath + objectPrefix + "-" + year + "-"
                            + month + "-" + day + ".csv");
                    break;
                }
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
