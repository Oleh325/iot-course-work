package ua.lviv.iot.novaPoshtaAPI.datastorage.dal;

import org.springframework.stereotype.Component;
import ua.lviv.iot.novaPoshtaAPI.model.Courier;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Objects;

@Component
public class CourierFileStore extends FileStore<Courier> {

    @Override
    protected Long getId(Courier courier) {
        return courier.getCourierId();
    }

    @Override
    protected Courier fill(List<String> values) {
        List<Long> ids = new LinkedList<>();
        if (!Objects.equals(values.get(4), "")) {
            ids = Arrays.stream(values.get(4).split(", "))
                    .map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        }
        return new Courier(Long.parseLong(values.get(0)), Long.parseLong(values.get(1)),
                values.get(2), Boolean.parseBoolean(values.get(3)), ids);
    }

    @Override
    protected String getObjectPrefix() {
        return "courier";
    }

}
