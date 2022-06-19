package ua.lviv.iot.novaPoshtaAPI.datastorage.dal;

import org.springframework.stereotype.Component;
import ua.lviv.iot.novaPoshtaAPI.model.Parcel;
import ua.lviv.iot.novaPoshtaAPI.Util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.HashMap;

@Component
public class ParcelFileStore {

    public HashMap<Long, Parcel> loadParcels(String directoryPath) throws IOException, ParseException {
        HashMap<Long, Parcel> resultMap = new HashMap<>();

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdir();
        }

        for (File file: Util.validateFile(directoryPath, "parcel")) {
            resultMap.putAll(scanParcel(file));
        }

        return resultMap;
    }

    private HashMap<Long, Parcel> scanParcel(File file) throws ParseException, IOException {
        HashMap<Long, Parcel> resultParcels = new HashMap<>();
        Scanner scanner = new Scanner(file, StandardCharsets.UTF_8);
        boolean isFirst = true;
        while (scanner.hasNextLine()) {
            if (!isFirst) {
                List<String> values = Arrays.stream(scanner.nextLine().split(", ")).toList();
                Parcel parcel = fillParcel(values);
                resultParcels.put(parcel.getParcelId(), parcel);
            } else {
                scanner.nextLine();
                isFirst = false;
            }
        }
        return resultParcels;
    }

    private Parcel fillParcel(List<String> values) throws ParseException {
        Parcel parcel = new Parcel();
        int index = 0;
        for (String value : values) {
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

    public void saveParcels(final HashMap<Long, Parcel> parcels, String directoryPath) throws IOException {
        String date = Util.getDateNow();

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdir();
        }


        File file = new File(directoryPath + "parcel-" + date + ".csv");
        Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        writer.write(parcels.values().stream().toList().get(0).getHeaders() + "\n");
        for (Parcel parcel : parcels.values()) {
            writer.write(parcel.toCSV() + "\n");
        }
        writer.close();

    }

}
