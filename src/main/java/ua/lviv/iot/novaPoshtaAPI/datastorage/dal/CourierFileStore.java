package ua.lviv.iot.novaPoshtaAPI.datastorage.dal;

import org.springframework.stereotype.Component;
import ua.lviv.iot.novaPoshtaAPI.Util;
import ua.lviv.iot.novaPoshtaAPI.model.Courier;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.Objects;
import java.util.HashMap;

@Component
public class CourierFileStore {

    public HashMap<Long, Courier> loadCouriers(String directoryPath) throws IOException {
        HashMap<Long, Courier> resultMap = new HashMap<>();

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdir();
        }

        for (File file: Util.validateFile(directoryPath, "courier")) {
            resultMap.putAll(scanCourier(file));
        }

        return resultMap;
    }

    private HashMap<Long, Courier> scanCourier(File file) throws IOException {
        HashMap<Long, Courier> resultCouriers = new HashMap<>();
        Scanner scanner = new Scanner(file, StandardCharsets.UTF_8);
        boolean isFirst = true;
        while (scanner.hasNextLine()) {
            if (!isFirst) {
                List<String> rawValues = Arrays.stream(scanner.nextLine().split(", ")).toList();
                List<String> values = processRawValues(rawValues);
                Courier courier = fillCourier(values);
                resultCouriers.put(courier.getCourierId(), courier);
            } else {
                scanner.nextLine();
                isFirst = false;
            }
        }
        return resultCouriers;
    }

    private List<String> processRawValues(List<String> rawValues) {
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

    public void saveCouriers(final HashMap<Long, Courier> couriers, String directoryPath) throws IOException {
        String date = Util.getDateNow();

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdir();
        }

        File file = new File(directoryPath + "courier-" + date + ".csv");
        Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        writer.write(couriers.values().stream().toList().get(0).getHeaders() + "\n");
        for (Courier courier: couriers.values()) {
            writer.write(courier.toCSV() + "\n");
        }
        writer.close();

    }

}
