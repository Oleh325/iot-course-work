package ua.lviv.iot.novaPoshtaAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import ua.lviv.iot.novaPoshtaAPI.model.Parcel;
import ua.lviv.iot.novaPoshtaAPI.service.ParcelService;

import java.util.List;

@RestController
@RequestMapping("/parcels")
public class ParcelController {

    @Autowired
    private ParcelService parcelService;

    @GetMapping
    public List<Parcel> getAllParcels() {
        return parcelService.getAllParcels();
    }

    @GetMapping("/{parcelId}")
    public Parcel getParcelById(@PathVariable Long parcelId) {
        return parcelService.getParcelById(parcelId);
    }

}
