package ua.lviv.iot.novaPoshtaAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lviv.iot.novaPoshtaAPI.datastorage.dal.CourierFileStore;
import ua.lviv.iot.novaPoshtaAPI.model.Courier;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Service
public class CourierService {

    @Autowired
    private ParcelService parcelService;
    @Autowired
    private CourierFileStore courierFileStore;

    HashMap<Long, Courier> couriers = new HashMap<>();

    public List<Courier> getAllCouriers() {
        return new LinkedList<>(this.couriers.values());
    }

    public Courier getCourierById(Long courierId) {
        return couriers.get(courierId);
    }

    public void deliverParcel(Long courierId, Long parcelId) {
        if (this.couriers.get(courierId).getParcelIds().contains(parcelId)) {
            parcelService.deleteParcel(parcelId);
            List<Long> newIds = this.couriers.get(courierId).getParcelIds();
            newIds.remove(parcelId);
            this.couriers.get(courierId).setParcelIds(newIds);
        }
    }

    public void addCourier(Courier courier) {
        List<Long> parcelIds = new LinkedList<>();
        courier.setParcelIds(parcelIds);
        couriers.put(courier.getCourierId(), courier);
    }

    public void updateCourier(Courier courier, Long courierId) {
        this.couriers.put(courierId, courier);
    }

    public void deleteCourier(Long courierId) {
        for (Long parcelId: this.couriers.get(courierId).getParcelIds()) {
            parcelService.deleteParcel(parcelId);
        }
        this.couriers.remove(courierId);
    }

    @PreDestroy
    private void saveCouriers() throws IOException {
        courierFileStore.saveCouriers(this.couriers, "res\\");
    }

    @PostConstruct
    private void loadCouriers() throws IOException {
        if (courierFileStore.loadCouriers("res\\") != null) {
            this.couriers = courierFileStore.loadCouriers("res\\");
        }
    }

}
