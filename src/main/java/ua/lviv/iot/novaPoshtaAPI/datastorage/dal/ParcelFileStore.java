package ua.lviv.iot.novaPoshtaAPI.datastorage.dal;

import org.springframework.stereotype.Component;
import ua.lviv.iot.novaPoshtaAPI.model.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class ParcelFileStore extends FileStore<Parcel> {

    @Override
    protected Long getId(Parcel parcel) {
        return parcel.getParcelId();
    }

    @Override
    protected Parcel fill(List<String> values) throws ParseException {
        return new Parcel(Long.parseLong(values.get(0)), Float.parseFloat(values.get(1)),
                Float.parseFloat(values.get(2)), Float.parseFloat(values.get(3)),
                Float.parseFloat(values.get(4)), values.get(5), values.get(6), values.get(7),
                new SimpleDateFormat("yyyy-MM-dd").parse(values.get(8)));
    }

    @Override
    protected String getObjectPrefix() {
        return "parcel";
    }

}
