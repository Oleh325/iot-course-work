package ua.lviv.iot.novaPoshtaAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.lviv.iot.novaPoshtaAPI.model.Courier;
import ua.lviv.iot.novaPoshtaAPI.service.CourierService;

import java.util.List;

@RestController
@RequestMapping("/couriers")
public class CourierController {

    @Autowired
    private CourierService courierService;

    @GetMapping
    public List<Courier> getAllCouriers() {
        return courierService.getAllCouriers();
    }

    @GetMapping("/{courierId}")
    public Courier getCourierById(@PathVariable Long courierId) {
        return courierService.getCourierById(courierId);
    }

    @RequestMapping("/{courierId}/deliver/{parcelId}")
    public void deliverParcel(@PathVariable Long courierId, @PathVariable Long parcelId) {
        courierService.deliverParcel(courierId, parcelId);
    }


}
