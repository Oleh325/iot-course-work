package ua.lviv.iot.novaPoshtaAPI.datastorage.dal;

import org.springframework.stereotype.Component;
import ua.lviv.iot.novaPoshtaAPI.model.Parcel;
import ua.lviv.iot.novaPoshtaAPI.Util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Component
public class ParcelFileStore {

    public List<Parcel> loadParcels() throws IOException, ParseException {
        List<Parcel> resultList = new LinkedList<>();

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
                if (Files.exists(Paths.get("res\\parcel-" + year + "-" + month + "-0" + i + ".csv"))) {
                    file = new File("res\\parcel-" + year + "-" + month + "-0" + i + ".csv");
                    resultList.addAll(ScanParcel(file));
                }
            } else {
                if (Files.exists(Paths.get("res\\parcel-" + year + "-" + month + "-" + i + ".csv"))) {
                    file = new File("res\\parcel-" + year + "-" + month + "-" + i + ".csv");
                    resultList.addAll(ScanParcel(file));
                }
            }
        }

        return resultList;
    }

    private List<Parcel> ScanParcel(File file) throws ParseException, IOException {
        List<Parcel> resultParcels = new LinkedList<>();
        Scanner scanner = new Scanner(file, StandardCharsets.UTF_8);
        boolean isFirst = true;
        while (scanner.hasNextLine()) {
            if (!isFirst) {
                List<String> values = Arrays.stream(scanner.nextLine().split(", ")).toList();
                resultParcels.add(FillParcel(values));
            } else {
                scanner.nextLine();
                isFirst = false;
            }
        }
        return resultParcels;
    }

    private Parcel FillParcel(List<String> values) throws ParseException {
        Parcel parcel = new Parcel();
        int index = 0;
        for (String value: values) {
            switch (index) {
                case 0 -> parcel.setParcelId(Long.parseLong(value));
                case 1 -> parcel.setWeightInKgs(Float.parseFloat(value));
                case 2 -> parcel.setHeightInCm(Float.parseFloat(value));
                case 3 -> parcel.setWidthInCm(Float.parseFloat(value));
                case 4 -> parcel.setLengthInCm(Float.parseFloat(value));
                case 5 -> parcel.setOrigin(value);
                case 6 -> parcel.setDestination(value);
                case 7 -> parcel.setLocation(value);
                case 8 -> parcel.setDateSent(new SimpleDateFormat("yyyy-MM-dd").parse(value));
            }
            index++;
        }
        return parcel;
    }

    public void saveParcels(final List<Parcel> parcels) {
        String date = Util.getTimeNow();

        File file = new File("res\\parcel-" + date + ".csv");
        try (FileWriter writer = new FileWriter(file);) {
            writer.write(parcels.get(0).getHeaders() + "\n");
            for (Parcel parcel: parcels) {
                writer.write(parcel.toCSV() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
