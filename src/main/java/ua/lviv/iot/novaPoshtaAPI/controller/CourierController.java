package ua.lviv.iot.novaPoshtaAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ua.lviv.iot.novaPoshtaAPI.model.Courier;
import ua.lviv.iot.novaPoshtaAPI.service.CourierService;
import ua.lviv.iot.novaPoshtaAPI.service.DepartmentService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping
public class CourierController {

    @Autowired
    private CourierService courierService;
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/couriers")
    public List<Courier> getAllCouriers() {
        return courierService.getAllCouriers();
    }

    @GetMapping("/couriers/{courierId}")
    public Courier getCourierById(@PathVariable Long courierId) {
        return courierService.getCourierById(courierId);
    }

    @PostMapping("/departments/{departmentId}/couriers")
    public void addCourier(@PathVariable Long departmentId,
                           @RequestBody Courier courier) throws IOException {
        departmentService.addCourier(departmentId, courier);
    }

    @PutMapping("/departments/{departmentId}/couriers/{courierId}")
    public void updateCourier(@PathVariable Long departmentId, @RequestBody Courier courier,
                              @PathVariable Long courierId) throws IOException {
        departmentService.updateCourier(departmentId, courier, courierId);
    }

    @DeleteMapping("/departments/{departmentId}/couriers/{courierId}")
    public void deleteCourier(@PathVariable Long departmentId,
                              @PathVariable Long courierId) throws IOException {
        departmentService.deleteCourier(departmentId, courierId);
    }

    @GetMapping("/departments/{departmentId}/couriers")
    public List<Courier> getAllCouriersDepartment(@PathVariable Long departmentId) {
        return departmentService.getAllCouriers(departmentId);
    }

    @GetMapping("/departments/{departmentId}/couriers/{courierId}")
    public Courier getCourierByIdDepartment(@PathVariable Long departmentId, @PathVariable Long courierId) {
        return departmentService.getCourierById(departmentId, courierId);
    }


}
