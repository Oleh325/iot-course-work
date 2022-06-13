package ua.lviv.iot.novaPoshtaAPI.datastorage.dal;

import org.springframework.stereotype.Component;
import ua.lviv.iot.novaPoshtaAPI.model.Department;
import ua.lviv.iot.novaPoshtaAPI.Util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class DepartmentFileStore {

    public List<Department> loadDepartments() throws IOException, ParseException {
        List<Department> resultList = new LinkedList<>();

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
                if (Files.exists(Paths.get("D:\\My Projects\\novaPoshtaAPI\\res\\department-" + year + "-" + month + "-0" + i + ".csv"))) {
                    file = new File("D:\\My Projects\\novaPoshtaAPI\\res\\department-" + year + "-" + month + "-0" + i + ".csv");
                    resultList.addAll(ScanDepartment(file));
                }
            } else {
                if (Files.exists(Paths.get("D:\\My Projects\\novaPoshtaAPI\\res\\department-" + year + "-" + month + "-" + i + ".csv"))) {
                    file = new File("D:\\My Projects\\novaPoshtaAPI\\res\\department-" + year + "-" + month + "-" + i + ".csv");
                    resultList.addAll(ScanDepartment(file));
                }
            }
        }

        return resultList;
    }

    private List<Department> ScanDepartment(File file) throws ParseException, IOException {
        List<Department> resultDepartments = new LinkedList<>();
        Scanner scanner = new Scanner(file, StandardCharsets.UTF_8);
        boolean isFirst = true;
        while (scanner.hasNextLine()) {
            if (!isFirst) {
                List<String> rawValues = Arrays.stream(scanner.nextLine().split(", ")).toList();
                List<String> values = new LinkedList<>();
                boolean isList = false;
                String listValue = "";
                for (String value: rawValues) {
                    if (value.contains("[")) {
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
                resultDepartments.add(FillDepartment(values));
            } else {
                scanner.nextLine();
                isFirst = false;
            }
        }
        return resultDepartments;
    }

    private Department FillDepartment(List<String> values) throws ParseException {
        Department department = new Department();
        int index = 0;
        for (String value: values) {
            switch (index) {
                case 0 -> department.setDepartmentId(Long.parseLong(value));
                case 1 -> department.setLocation(value);
                case 2 -> department.setWorkingHours(value);
                case 3 -> department.setParcelIds(Arrays.stream(value.split(", ")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList()));
            }
            index++;
        }
        return department;
    }

    public void saveDepartments(final List<Department> departments) {
        String date = Util.getTimeNow();

        File file = new File("D:\\My Projects\\novaPoshtaAPI\\res\\department-" + date + ".csv");
        try (FileWriter writer = new FileWriter(file);) {
            writer.write(departments.get(0).getHeaders() + "\n");
            for (Department department: departments) {
                writer.write(department.toCSV() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
