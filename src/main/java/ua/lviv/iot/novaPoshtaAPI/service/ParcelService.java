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
        if (this.parcels.get(parcelId) != null) {
            if (parcel.getParcelId() == null) {
                parcel.setParcelId(parcelId);
            }
            if (parcel.getWeightInKgs() == 0.0f) {
                parcel.setWeightInKgs(this.parcels.get(parcelId).getWeightInKgs());
            }
            if (parcel.getHeightInCm() == 0.0f) {
                parcel.setHeightInCm(this.parcels.get(parcelId).getHeightInCm());
            }
            if (parcel.getWidthInCm() == 0.0f) {
                parcel.setWidthInCm(this.parcels.get(parcelId).getWidthInCm());
            }
            if (parcel.getLengthInCm() == 0.0f) {
                parcel.setLengthInCm(this.parcels.get(parcelId).getLengthInCm());
            }
            if (parcel.getOrigin() == null) {
                parcel.setOrigin(this.parcels.get(parcelId).getOrigin());
            }
            if (parcel.getDestination() == null) {
                parcel.setDestination(this.parcels.get(parcelId).getDestination());
            }
            if (parcel.getLocation() == null) {
                parcel.setLocation(this.parcels.get(parcelId).getLocation());
            }
            if (parcel.getDateSent() == null) {
                parcel.setDateSent(this.parcels.get(parcelId).getDateSent());
            }
            this.parcels.put(parcelId, parcel);
        }
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
