package ua.lviv.iot.novaPoshtaAPI.datastorage.dal;

import org.springframework.stereotype.Component;
import ua.lviv.iot.novaPoshtaAPI.model.Department;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

@Component
public class DepartmentFileStore {

    public void saveDepartment(final Department department) throws IOException {
        String year = Integer.toString(LocalDate.now().getYear());
        String month = Integer.toString(LocalDate.now().getMonthValue());
        String day = Integer.toString(LocalDate.now().getDayOfMonth());
        if (Files.exists(Paths.get("department-" + year + month + day + ".csv"))) {
            final File file = new File("department-" + year + month + day + ".csv");
            try (FileWriter writer = new FileWriter(file);) {
                writer.write(department.toCSV() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            final File file = new File("department-" + year + month + day + ".csv");
            try (FileWriter writer = new FileWriter(file);) {
                writer.write(department.getHeaders() + "\n");
                writer.write(department.toCSV() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
