package ua.lviv.iot.novaPoshtaAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import ua.lviv.iot.novaPoshtaAPI.model.Courier;
import ua.lviv.iot.novaPoshtaAPI.service.CourierService;

import java.util.List;

@RestController
@RequestMapping("/couriers")
public class CourierController {

    @Autowired
    CourierService courierService;

    @GetMapping
    public List<Courier> getAllCouriers() {
        return courierService.getAllCouriers();
    }

    @GetMapping("/{courierId}")
    public Courier getCourierById(@PathVariable Long courierId) {
        return courierService.getCourierById(courierId);
    }

    @PostMapping
    public void addCourier(@RequestBody Courier courier) {
        courierService.addCourier(courier);
    }

    @PutMapping("/{courierId}")
    public void updateCourier(@RequestBody Courier courier, @PathVariable Long courierId) {
        courierService.updateCourier(courier, courierId);
    }

    @DeleteMapping("/{courierId}")
    public void deleteCourier(@PathVariable Long courierId) {
        courierService.deleteCourier(courierId);
    }

    @RequestMapping("/{courierId}/deliver/{parcelId}")
    public void deliverParcel(@PathVariable Long courierId, @PathVariable Long parcelId) {
        courierService.deliverParcel(courierId, parcelId);
    }


}
