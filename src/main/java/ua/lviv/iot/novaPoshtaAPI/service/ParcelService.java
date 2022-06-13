package ua.lviv.iot.novaPoshtaAPI.service;

import org.springframework.stereotype.Service;
import ua.lviv.iot.novaPoshtaAPI.model.Parcel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Service
public class ParcelService {

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

    public void updateParcel(Parcel parcel) {
        Parcel oldParcel = this.parcels.get(parcel.getParcelId());
        Parcel newParcel = new Parcel();

        this.parcels.remove(parcel.getParcelId());

        newParcel.setParcelId(oldParcel.getParcelId());
        newParcel.setOrigin(oldParcel.getOrigin());
        newParcel.setDateSent(oldParcel.getDateSent());

        if (parcel.getWeightInKgs() != 0.0) {
            newParcel.setWeightInKgs(parcel.getWeightInKgs());
        } else {
            newParcel.setWeightInKgs(oldParcel.getWeightInKgs());
        }
        if (parcel.getHeightInCm() != 0.0) {
            newParcel.setHeightInCm(parcel.getHeightInCm());
        } else {
            newParcel.setHeightInCm(oldParcel.getHeightInCm());
        }
        if (parcel.getWidthInCm() != 0.0) {
            newParcel.setWidthInCm(parcel.getWidthInCm());
        } else {
            newParcel.setWidthInCm(oldParcel.getWidthInCm());
        }
        if (parcel.getLengthInCm() != 0.0) {
            newParcel.setLengthInCm(parcel.getLengthInCm());
        } else {
            newParcel.setLengthInCm(oldParcel.getLengthInCm());
        }
        if (parcel.getDestination() != null) {
            newParcel.setDestination(parcel.getDestination());
        } else {
            newParcel.setDestination(oldParcel.getDestination());
        }
        if (parcel.getLocation() != null) {
            newParcel.setLocation(parcel.getLocation());
        } else {
            newParcel.setLocation(oldParcel.getLocation());
        }

        this.parcels.put(newParcel.getParcelId(), newParcel);
    }

    public void deleteParcel(Long parcelId) {
        parcels.remove(parcelId);
    }

}
