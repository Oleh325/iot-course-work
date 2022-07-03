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
import ua.lviv.iot.novaPoshtaAPI.model.Parcel;
import ua.lviv.iot.novaPoshtaAPI.service.CourierService;
import ua.lviv.iot.novaPoshtaAPI.service.DepartmentService;
import ua.lviv.iot.novaPoshtaAPI.service.ParcelService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping
public class ParcelController {

    @Autowired
    private ParcelService parcelService;
    @Autowired
    private CourierService courierService;
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/parcels")
    public List<Parcel> getAllParcels() {
        return parcelService.getAllParcels();
    }

    @GetMapping("/parcels/{parcelId}")
    public Parcel getParcelById(@PathVariable Long parcelId) {
        return parcelService.getParcelById(parcelId);
    }

    @GetMapping("/departments/{departmentId}/parcels")
    public List<Parcel> getAllParcelsDepartment(@PathVariable Long departmentId) {
        return departmentService.getAllParcels(departmentId);
    }

    @GetMapping("/departments/{departmentId}/parcels/{parcelId}")
    public Parcel getParcelByIdDepartment(@PathVariable Long departmentId, @PathVariable Long parcelId) {
        return departmentService.getParcelById(departmentId, parcelId);
    }

    @PostMapping("/departments/{departmentId}/parcels")
    public void addParcel(@PathVariable Long departmentId,
                          @RequestBody @Valid Parcel parcel) throws IOException {
        departmentService.addParcel(departmentId, parcel);
    }

    @PutMapping("/departments/{departmentId}/parcels/{parcelId}")
    public void updateParcel(@PathVariable Long departmentId, @PathVariable Long parcelId,
                             @RequestBody @Valid Parcel parcel) throws IOException {
        departmentService.updateParcel(departmentId, parcel, parcelId);
    }

    @DeleteMapping("/departments/{departmentId}/parcels/{parcelId}")
    public void deleteParcel(@PathVariable Long departmentId,
                             @PathVariable Long parcelId) throws IOException {
        departmentService.deleteParcel(departmentId, parcelId);
    }

    @RequestMapping("/departments/{departmentId}/couriers/{courierId}/parcels/{parcelId}")
    public void giveParcelToCourier(@PathVariable Long departmentId, @PathVariable Long courierId,
                                    @PathVariable Long parcelId) throws IOException {
        departmentService.giveParcelToCourier(departmentId, courierId, parcelId);
    }

    @RequestMapping("/departments/{departmentIdFrom}/deliver/{departmentIdTo}/parcels/{parcelId}")
    public void deliverParcelDepartment(@PathVariable Long departmentIdFrom, @PathVariable Long departmentIdTo,
                              @PathVariable Long parcelId) throws IOException {
        departmentService.deliverParcel(departmentIdFrom, departmentIdTo, parcelId);
    }

    @RequestMapping("/couriers/{courierId}/deliver/{parcelId}")
    public void deliverParcelCourier(@PathVariable Long courierId,
                                     @PathVariable Long parcelId) throws IOException {
        courierService.deliverParcel(courierId, parcelId);
    }

}
