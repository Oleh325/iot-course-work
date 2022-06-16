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
import ua.lviv.iot.novaPoshtaAPI.model.Department;
import ua.lviv.iot.novaPoshtaAPI.model.Parcel;
import ua.lviv.iot.novaPoshtaAPI.service.DepartmentService;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @GetMapping
    private List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{departmentId}")
    public Department getDepartmentById(@PathVariable Long departmentId) {
        return departmentService.getDepartmentById(departmentId);
    }

    @PostMapping
    public void addDepartment(@RequestBody Department department) {
        departmentService.addDepartment(department);
    }

    @PutMapping("/{departmentId}")
    public void updateDepartment(@RequestBody Department department, @PathVariable Long departmentId) {
        departmentService.updateDepartment(department, departmentId);
    }

    @DeleteMapping("/{departmentId}")
    public void deleteDepartment(@PathVariable Long departmentId) {
        departmentService.deleteDepartment(departmentId);
    }

    @RequestMapping("/{departmentId}/couriers/{courierId}/parcels/{parcelId}")
    public void giveParcelToCourier(@PathVariable Long departmentId, @PathVariable Long courierId,
                                    @PathVariable Long parcelId) {
        departmentService.giveParcelToCourier(departmentId, courierId, parcelId);
    }

    @GetMapping("/{departmentId}/parcels")
    public List<Parcel> getAllParcels(@PathVariable Long departmentId) {
        return departmentService.getAllParcels(departmentId);
    }

    @GetMapping("/{departmentId}/parcels/{parcelId}")
    public Parcel getParcelById(@PathVariable Long departmentId, @PathVariable Long parcelId) {
        return departmentService.getParcelById(departmentId, parcelId);
    }

    @PostMapping("/{departmentId}/parcels")
    public void addParcel(@PathVariable Long departmentId, @RequestBody Parcel parcel) {
        departmentService.addParcel(departmentId, parcel);
    }

    @PutMapping("/{departmentId}/parcels/{parcelId}")
    public void updateParcel(@PathVariable Long departmentId, @PathVariable Long parcelId, @RequestBody Parcel parcel) {
        departmentService.updateParcel(departmentId, parcel, parcelId);
    }

    @DeleteMapping("/{departmentId}/parcels/{parcelId}")
    public void deleteParcel(@PathVariable Long departmentId, @PathVariable Long parcelId) {
        departmentService.deleteParcel(departmentId, parcelId);
    }

    @RequestMapping("/{departmentIdFrom}/deliver/{departmentIdTo}/parcels/{parcelId}")
    public void deliverParcel(@PathVariable Long departmentIdFrom, @PathVariable Long departmentIdTo,
                              @PathVariable Long parcelId) {
        departmentService.deliverParcel(departmentIdFrom, departmentIdTo, parcelId);
    }

    @PostMapping("/couriers")
    public void addCourier(@RequestBody Courier courier) {
        departmentService.addCourier(courier);
    }

    @PutMapping("/couriers/{courierId}")
    public void updateCourier(@RequestBody Courier courier, @PathVariable Long courierId) {
        departmentService.updateCourier(courier, courierId);
    }

    @DeleteMapping("/couriers/{courierId}")
    public void deleteCourier(@PathVariable Long courierId) {
        departmentService.deleteCourier(courierId);
    }

}
