package ua.lviv.iot.novaPoshtaAPI.datastorage.dal;

import org.springframework.stereotype.Component;
import ua.lviv.iot.novaPoshtaAPI.model.Department;
import ua.lviv.iot.novaPoshtaAPI.Util;

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
public class DepartmentFileStore {

    public HashMap<Long, Department> loadDepartments(String directoryPath) throws IOException {
        HashMap<Long, Department> resultMap = new HashMap<>();

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdir();
        }

        for (File file: Util.validateFile(directoryPath, "department")) {
            resultMap.putAll(scanDepartment(file));
        }

        return resultMap;
    }

    private HashMap<Long, Department> scanDepartment(File file) throws IOException {
        HashMap<Long, Department> resultDepartments = new HashMap<>();
        Scanner scanner = new Scanner(file, StandardCharsets.UTF_8);
        boolean isFirst = true;
        while (scanner.hasNextLine()) {
            if (!isFirst) {
                List<String> rawValues = Arrays.stream(scanner.nextLine().split(", ")).toList();
                List<String> values = processRawValues(rawValues);
                Department department = fillDepartment(values);
                resultDepartments.put(department.getDepartmentId(), department);
            } else {
                scanner.nextLine();
                isFirst = false;
            }
        }
        return resultDepartments;
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

    private Department fillDepartment(List<String> values) {
        Department department = new Department();
        int index = 0;
        for (String value : values) {
            switch (index) {
                case 0 -> department.setDepartmentId(Long.parseLong(value));
                case 1 -> department.setLocation(value);
                case 2 -> department.setWorkingHours(value);
                case 3 ->{
                    if (!Objects.equals(value, "")) {
                        department.setParcelIds(Arrays.stream(value.split(", "))
                                .map(s -> Long.parseLong(s.trim())).collect(Collectors.toList()));
                    } else {
                        List<Long> ids = new LinkedList<>();
                        department.setParcelIds(ids);
                    }
                }
            }
            index++;
        }
        return department;
    }

    public void saveDepartments(final HashMap<Long, Department> departments, String directoryPath) throws IOException {
        String date = Util.getDateNow();

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdir();
        }

        File file = new File(directoryPath + "department-" + date + ".csv");
        Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        writer.write(departments.values().stream().toList().get(0).getHeaders() + "\n");
        for (Department department : departments.values()) {
            writer.write(department.toCSV() + "\n");
        }
        writer.close();

    }

}
