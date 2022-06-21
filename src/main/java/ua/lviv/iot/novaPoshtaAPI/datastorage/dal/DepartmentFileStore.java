package ua.lviv.iot.novaPoshtaAPI.datastorage.dal;

import org.springframework.stereotype.Component;
import ua.lviv.iot.novaPoshtaAPI.model.Department;
import ua.lviv.iot.novaPoshtaAPI.Util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.Objects;
import java.util.HashMap;

@Component
public class DepartmentFileStore {

    public HashMap<Long, Department> loadDepartments(String directoryPath) throws IOException, ParseException {
        Util.generateDirectory(directoryPath);

        return new HashMap<>(scanDepartment(Util.validateFile(directoryPath, "department")));
    }

    private HashMap<Long, Department> scanDepartment(File file) throws IOException {
        HashMap<Long, Department> resultDepartments = new HashMap<>();
        if (file.exists()) {
            Scanner scanner = new Scanner(file, StandardCharsets.UTF_8);
            boolean isFirst = true;
            while (scanner.hasNextLine()) {
                if (!isFirst) {
                    List<String> rawValues = Arrays.stream(scanner.nextLine().split(", ")).toList();
                    List<String> values = Util.processRawValues(rawValues);
                    Department department = fillDepartment(values);
                    resultDepartments.put(department.getDepartmentId(), department);
                } else {
                    scanner.nextLine();
                    isFirst = false;
                }
            }
            scanner.close();
        }
        return resultDepartments;
    }

    private Department fillDepartment(List<String> values) {
        Department department = new Department();
        int index = 0;
        for (String value : values) {
            switch (index) {
                case 0 -> department.setDepartmentId(Long.parseLong(value));
                case 1 -> department.setLocation(value);
                case 2 -> department.setWorkingHours(value);
                case 3 -> {
                    if (!Objects.equals(value, "")) {
                        department.setParcelIds(Arrays.stream(value.split(", "))
                                .map(s -> Long.parseLong(s.trim())).collect(Collectors.toList()));
                    } else {
                        List<Long> ids = new LinkedList<>();
                        department.setParcelIds(ids);
                    }
                }
                default -> { }
            }
            index++;
        }
        return department;
    }

    public void saveDepartments(final HashMap<Long, Department> departments, String directoryPath) throws IOException {
        Util.generateDirectory(directoryPath);
        File file = Util.generateFile(directoryPath, "department");

        String content = departments.values().stream().toList().get(0).getHeaders() + "\n";
        for (Department department: departments.values()) {
            content += department.toCSV() + "\n";
        }

        Util.writeContentToFile(file, content);

    }

}
