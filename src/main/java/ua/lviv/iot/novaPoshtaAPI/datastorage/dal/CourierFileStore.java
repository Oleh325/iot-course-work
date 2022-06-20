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
        Util.generateDirectory(directoryPath);

        return new HashMap<>(scanCourier(Util.validateFile(directoryPath, "courier")));
    }

    private HashMap<Long, Courier> scanCourier(File file) throws IOException {
        HashMap<Long, Courier> resultCouriers = new HashMap<>();
        Scanner scanner = new Scanner(file, StandardCharsets.UTF_8);
        boolean isFirst = true;
        while (scanner.hasNextLine()) {
            if (!isFirst) {
                List<String> rawValues = Arrays.stream(scanner.nextLine().split(", ")).toList();
                List<String> values = Util.processRawValues(rawValues);
                Courier courier = fillCourier(values);
                resultCouriers.put(courier.getCourierId(), courier);
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

    public void saveCouriers(final HashMap<Long, Courier> couriers, String directoryPath) throws IOException {
        Util.generateDirectory(directoryPath);
        File file = Util.generateFile(directoryPath, "courier");

        Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        writer.write(couriers.values().stream().toList().get(0).getHeaders() + "\n");
        for (Courier courier: couriers.values()) {
            writer.write(courier.toCSV() + "\n");
        }
        writer.close();

    }



}
