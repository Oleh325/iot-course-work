package ua.lviv.iot.novaPoshtaAPI.datastorage.dal;

import ua.lviv.iot.novaPoshtaAPI.datastorage.dal.util.FileStoreHelper;
import ua.lviv.iot.novaPoshtaAPI.model.Item;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

public abstract class FileStore<T extends Item> {

    public void save(final HashMap<Long, T> items, String directoryPath) throws IOException {
        FileStoreHelper.generateDirectory(directoryPath);
        File file = FileStoreHelper.generateFile(directoryPath, getObjectPrefix());

        StringBuilder content = new StringBuilder(items.values().stream().toList().get(0).getHeaders() + "\n");
        for (T item: items.values()) {
            content.append(item.toCSV()).append("\n");
        }

        FileStoreHelper.writeContentToFile(file, content.toString());
    }

    public HashMap<Long, T> load(String directoryPath) throws IOException, ParseException {
        FileStoreHelper.generateDirectory(directoryPath);

        return new HashMap<>(scan(FileStoreHelper.validateFile(directoryPath, getObjectPrefix())));
    }

    private HashMap<Long, T> scan(File file) throws IOException, ParseException {
        HashMap<Long, T> resultCouriers = new HashMap<>();
        if (file.exists()) {
            Scanner scanner = new Scanner(file, StandardCharsets.UTF_8);
            boolean isFirst = true;
            while (scanner.hasNextLine()) {
                if (isFirst) {
                    scanner.nextLine();
                    isFirst = false;
                } else {
                    List<String> rawValues = Arrays.stream(scanner.nextLine().split(", ")).toList();
                    List<String> values = FileStoreHelper.processRawValues(rawValues);
                    T item = fill(values);
                    resultCouriers.put(getId(item), item);
                }
            }
            scanner.close();
        }
        return resultCouriers;
    }

    protected abstract Long getId(T item);

    protected abstract T fill(List<String> values) throws ParseException;

    protected abstract String getObjectPrefix();

}
