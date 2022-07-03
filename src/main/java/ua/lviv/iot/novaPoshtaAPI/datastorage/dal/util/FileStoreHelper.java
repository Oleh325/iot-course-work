package ua.lviv.iot.novaPoshtaAPI.datastorage.dal.util;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
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

public final class FileStoreHelper {

    private FileStoreHelper() {
        throw new AssertionError("Instantiating utility class.");
    }

    public static String getDateNow() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        return formatter.format(date);
    }

    public static void writeContentToFile(File file, String content) throws IOException {
        Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        writer.write(content);
        writer.close();
    }

    public static File validateFile(String directoryPath, String objectPrefix) throws ParseException {
        File file = generateFile(directoryPath, objectPrefix);

        File directory = new File(directoryPath);
        if (FileUtils.sizeOfDirectory(directory) != 0) {
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
        String date = FileStoreHelper.getDateNow();

        return new File(directoryPath + objectPrefix + "-" + date + ".csv");
    }

    @SuppressFBWarnings
    public static void generateDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
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
        return values;
    }

}
