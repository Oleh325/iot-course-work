package ua.lviv.iot.novaPoshtaAPI.datastorage.dal;

import org.springframework.stereotype.Component;
import ua.lviv.iot.novaPoshtaAPI.model.Parcel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

@Component
public class ParcelFileStore {

    public void saveParcel(final Parcel parcel) throws IOException {
        String year = Integer.toString(LocalDate.now().getYear());
        String month = Integer.toString(LocalDate.now().getMonthValue());
        String day = Integer.toString(LocalDate.now().getDayOfMonth());
        if (Files.exists(Paths.get("parcel-" + year + month + day + ".csv"))) {
            final File file = new File("parcel-" + year + month + day + ".csv");
            try (FileWriter writer = new FileWriter(file);) {
                writer.write(parcel.toCSV() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            final File file = new File("parcel-" + year + month + day + ".csv");
            try (FileWriter writer = new FileWriter(file);) {
                writer.write(parcel.getHeaders() + "\n");
                writer.write(parcel.toCSV() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
