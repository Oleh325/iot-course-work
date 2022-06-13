package ua.lviv.iot.novaPoshtaAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/{departmentId}/couriers/{courierId}/parcels/{parcelId}")
    public void giveParcelToCourier(@PathVariable Long departmentId, @PathVariable Long courierId, @PathVariable Long parcelId) {
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

    @PutMapping("/{departmentId}/parcels")
    public void updateParcel(@PathVariable Long departmentId, @RequestBody Parcel parcel) {
        departmentService.updateParcel(departmentId, parcel);
    }

    @DeleteMapping("/{departmentId}/parcels/{parcelId}")
    public void deleteParcel(@PathVariable Long departmentId, @PathVariable Long parcelId) {
        departmentService.deleteParcel(departmentId, parcelId);
    }

}
