package ua.lviv.iot.novaPoshtaAPI;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Util {

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

    public static List<File> validateFile(String directoryPath, String objectPrefix) {
        File file;
        List<File> files = new LinkedList<>();

        String year = Integer.toString(LocalDate.now().getYear());
        String month;

        if (LocalDate.now().getMonthValue() < 10) {
            month = "0" + LocalDate.now().getMonthValue();
        } else {
            month = Integer.toString(LocalDate.now().getMonthValue());
        }

        for (int i = 1; i <= LocalDate.now().getDayOfMonth(); i++) {
            if (i < 10) {
                if (Files.exists(Paths.get(directoryPath + objectPrefix + "-" + year + "-"
                        + month + "-0" + i + ".csv"))) {
                    file = new File(directoryPath + objectPrefix + "-" + year + "-"
                            + month + "-0" + i + ".csv");
                    files.add(file);
                }
            } else {
                if (Files.exists(Paths.get(directoryPath + objectPrefix + "-" + year + "-"
                        + month + "-" + i + ".csv"))) {
                    file = new File(directoryPath + objectPrefix + "-" + year + "-"
                            + month + "-" + i + ".csv");
                    files.add(file);
                }
            }
        }
        return files;
    }
}
