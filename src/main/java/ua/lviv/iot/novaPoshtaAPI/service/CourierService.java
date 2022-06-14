package ua.lviv.iot.novaPoshtaAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lviv.iot.novaPoshtaAPI.datastorage.dal.CourierFileStore;
import ua.lviv.iot.novaPoshtaAPI.model.Courier;
import ua.lviv.iot.novaPoshtaAPI.model.Department;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Service
public class CourierService {

    @Autowired
    ParcelService parcelService;
    @Autowired
    CourierFileStore courierFileStore;

    HashMap<Long, Courier> couriers = new HashMap<>();

    public List<Courier> getAllCouriers() {
        return new LinkedList<>(this.couriers.values());
    }

    public Courier getCourierById(Long courierId) {
        return couriers.get(courierId);
    }

    public void addCourier(Courier courier) {
        couriers.put(courier.getCourierId(), courier);
    }

    public void updateCourier(Courier courier, Long courierId) {
        Courier oldCourier = this.couriers.get(courierId);
        Courier newCourier = new Courier();

        this.couriers.remove(courierId);

        newCourier.setCourierId(oldCourier.getCourierId());

        if (courier.getDepartmentId() != null) {
            newCourier.setDepartmentId(courier.getDepartmentId());
        } else {
            newCourier.setDepartmentId(oldCourier.getDepartmentId());
        }
        if (courier.getFullName() != null) {
            newCourier.setFullName(courier.getFullName());
        } else {
            newCourier.setFullName(oldCourier.getFullName());
        }
        if (courier.getParcelIds() != null) {
            newCourier.setParcelIds(courier.getParcelIds());
        } else {
            newCourier.setParcelIds(oldCourier.getParcelIds());
        }

        newCourier.setWorking(newCourier.getParcelIds() != null);

        this.couriers.put(newCourier.getCourierId(), newCourier);
    }

    public void deleteCourier(Long courierId) {
        this.couriers.remove(courierId);
    }

    public void deliverParcel(Long courierId, Long parcelId) {
        if (this.couriers.get(courierId).getParcelIds().contains(parcelId)) {
            parcelService.deleteParcel(parcelId);
            List<Long> newIds = this.couriers.get(courierId).getParcelIds();
            newIds.remove(parcelId);
            this.couriers.get(courierId).setParcelIds(newIds);
        }
    }

    @PreDestroy
    private void saveCouriers() {
        List<Courier> list = this.couriers.values().stream().toList();
        courierFileStore.saveCouriers(list);
    }

    @PostConstruct
    private void loadCouriers() throws IOException, ParseException {
        if (courierFileStore.loadCouriers() != null) {
            List<Courier> list = courierFileStore.loadCouriers();
            for (Courier courier: list) {
                this.couriers.put(courier.getDepartmentId(), courier);
            }
        }
    }

}