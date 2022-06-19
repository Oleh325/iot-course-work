package ua.lviv.iot.novaPoshtaAPI.datastorage.dal;

import org.springframework.stereotype.Component;
import ua.lviv.iot.novaPoshtaAPI.model.Department;
import ua.lviv.iot.novaPoshtaAPI.Util;

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
public class DepartmentFileStore {

    public List<Department> loadDepartments(boolean isTest) throws IOException {
        List<Department> resultList = new LinkedList<>();

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
                if (Files.exists(Paths.get("res\\" + testPath + "department-" + year + "-"
                        + month + "-0" + i + ".csv"))) {
                    file = new File("res\\" + testPath + "department-" + year + "-"
                            + month + "-0" + i + ".csv");
                    resultList.addAll(scanDepartment(file));
                }
            } else {
                if (Files.exists(Paths.get("res\\" + testPath + "department-" + year + "-"
                        + month + "-" + i + ".csv"))) {
                    file = new File("res\\" + testPath + "department-" + year + "-"
                            + month + "-" + i + ".csv");
                    resultList.addAll(scanDepartment(file));
                }
            }
        }

        return resultList;
    }

    private List<Department> scanDepartment(File file) throws IOException {
        List<Department> resultDepartments = new LinkedList<>();
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
                resultDepartments.add(fillDepartment(values));
                listValue = "";
            } else {
                scanner.nextLine();
                isFirst = false;
            }
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

    public void saveDepartments(final List<Department> departments, boolean isTest) {
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

        File file = new File("res\\" + testPath + "department-" + date + ".csv");
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);) {
            writer.write(departments.get(0).getHeaders() + "\n");
            for (Department department : departments) {
                writer.write(department.toCSV() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
