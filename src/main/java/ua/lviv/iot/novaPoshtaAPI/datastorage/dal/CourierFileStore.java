package ua.lviv.iot.novaPoshtaAPI.datastorage.dal;

import org.springframework.stereotype.Component;
import ua.lviv.iot.novaPoshtaAPI.Util;
import ua.lviv.iot.novaPoshtaAPI.model.Courier;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.Writer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CourierFileStore {

    public List<Courier> loadCouriers(boolean isTest) throws IOException {
        List<Courier> resultList = new LinkedList<>();

        File directory = new File("res");
        if (!directory.exists()) {
            directory.mkdir();
        }

        String testPath = "";
        if (isTest) {
            testPath = "test\\";
        }
        directory = new File("res\\test");
        if (!directory.exists()) {
            directory.mkdir();
        }

        File file;

        String year = Integer.toString(LocalDate.now().getYear());
        String month;

        if (LocalDate.now().getMonthValue() < 10) {
            month = "0" + LocalDate.now().getMonthValue();
        } else {
            month = Integer.toString(LocalDate.now().getMonthValue());
        }

        for (int i = 1; i <= LocalDate.now().getDayOfMonth(); i++) {
            if (i < 10) {
                if (Files.exists(Paths.get("res\\" + testPath + "courier-" + year + "-"
                        + month + "-0" + i + ".csv"))) {
                    file = new File("res\\" + testPath + "courier-" + year + "-"
                            + month + "-0" + i + ".csv");
                    resultList.addAll(scanCourier(file));
                }
            } else {
                if (Files.exists(Paths.get("res\\" + testPath + "courier-" + year + "-"
                        + month + "-" + i + ".csv"))) {
                    file = new File("res\\" + testPath + "courier-" + year + "-"
                            + month + "-" + i + ".csv");
                    resultList.addAll(scanCourier(file));
                }
            }
        }

        return resultList;
    }

    private List<Courier> scanCourier(File file) throws IOException {
        List<Courier> resultCouriers = new LinkedList<>();
        Scanner scanner = new Scanner(file, StandardCharsets.UTF_8);
        boolean isFirst = true;
        while (scanner.hasNextLine()) {
            if (!isFirst) {
                List<String> rawValues = Arrays.stream(scanner.nextLine().split(", ")).toList();
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
                resultCouriers.add(fillCourier(values));
                listValue = "";
            } else {
                scanner.nextLine();
                isFirst = false;
            }
        }
        return resultCouriers;
    }

    private Courier fillCourier(List<String> values) {
        Courier courier = new Courier();
        int index = 0;
        for (String value : values) {
            switch (index) {
                case 0 -> courier.setCourierId(Long.parseLong(value));
                case 1 -> courier.setDepartmentId(Long.parseLong(value));
                case 2 -> courier.setFullName(value);
                case 3 -> courier.setWorking(Boolean.parseBoolean(value));
                case 4 -> {
                    if (!Objects.equals(value, "")) {
                        courier.setParcelIds(Arrays.stream(value.split(", "))
                                .map(s -> Long.parseLong(s.trim())).collect(Collectors.toList()));
                    } else {
                        List<Long> ids = new LinkedList<>();
                        courier.setParcelIds(ids);
                    }
                }
            }
            index++;
        }
        return courier;
    }

    public void saveCouriers(final List<Courier> couriers, boolean isTest) {
        String date = Util.getDateNow();

        File directory = new File("res");
        if (!directory.exists()) {
            directory.mkdir();
        }

        String testPath = "";
        if (isTest) {
            testPath = "test\\";
        }
        directory = new File("res\\test");
        if (!directory.exists()) {
            directory.mkdir();
        }

        File file = new File("res\\" + testPath + "courier-" + date + ".csv");
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);) {
            writer.write(couriers.get(0).getHeaders() + "\n");
            for (Courier courier : couriers) {
                writer.write(courier.toCSV() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
