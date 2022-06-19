package ua.lviv.iot.novaPoshtaAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lviv.iot.novaPoshtaAPI.datastorage.dal.ParcelFileStore;
import ua.lviv.iot.novaPoshtaAPI.model.Parcel;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Service
public class ParcelService {

    @Autowired
    private ParcelFileStore parcelFileStore;

    private HashMap<Long, Parcel> parcels = new HashMap<>();

    public List<Parcel> getAllParcels() {
        return new LinkedList<>(this.parcels.values());
    }

    public Parcel getParcelById(Long parcelId) {
        return this.parcels.get(parcelId);
    }

    public void addParcel(Parcel parcel) {
        parcels.put(parcel.getParcelId(), parcel);
    }

    public void updateParcel(Parcel parcel, Long parcelId) {
        this.parcels.put(parcelId, parcel);
    }

    public void deleteParcel(Long parcelId) {
        parcels.remove(parcelId);
    }

    @PreDestroy
    private void saveParcels() throws IOException {
        parcelFileStore.saveParcels(this.parcels, "res\\");
    }

    @PostConstruct
    private void loadParcels() throws IOException, ParseException {
        if (parcelFileStore.loadParcels("res\\") != null) {
            this.parcels = parcelFileStore.loadParcels("res\\");
        }
    }

}
