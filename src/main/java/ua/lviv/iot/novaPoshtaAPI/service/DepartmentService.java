package ua.lviv.iot.novaPoshtaAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ua.lviv.iot.novaPoshtaAPI.model.Courier;
import ua.lviv.iot.novaPoshtaAPI.model.Department;
import ua.lviv.iot.novaPoshtaAPI.model.Parcel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
public class DepartmentService {

    @Autowired
    ParcelService parcelService;
    @Autowired
    CourierService courierService;

    private HashMap<Long, Department> departments = new HashMap<>();

    public List<Department> getAllDepartments() {
        return new LinkedList<>(this.departments.values());
    }

    public Department getDepartmentById(Long departmentId) {
        return this.departments.get(departmentId);
    }

    public void addDepartment(Department department) {
        this.departments.put(department.getDepartmentId(), department);
    }

    public void updateDepartment(Department department, Long departmentId) {
        Department oldDpt = this.departments.get(departmentId);
        Department newDpt = new Department();

        this.departments.remove(departmentId);

        newDpt.setDepartmentId(oldDpt.getDepartmentId());

        if (department.getLocation() != null) {
            newDpt.setLocation(department.getLocation());
        } else {
            newDpt.setLocation(oldDpt.getLocation());
        }
        if (department.getWorkingHours() != null) {
            newDpt.setWorkingHours(department.getWorkingHours());
        } else {
            newDpt.setWorkingHours(oldDpt.getWorkingHours());
        }
        if (department.getParcelIds() != null) {
            newDpt.setParcelIds(department.getParcelIds());
        } else {
            newDpt.setParcelIds(oldDpt.getParcelIds());
        }

        this.departments.put(newDpt.getDepartmentId(), newDpt);
    }

    public void deleteDepartment(Long departmentId) {
        this.departments.remove(departmentId);
    }

    public void giveParcelToCourier(Long departmentId, Long courierId, Long parcelId) {
        if (departments.get(departmentId).getParcelIds().contains(parcelId) &&
                Objects.equals(courierService.getCourierById(courierId).getDepartmentId(), departmentId)) {
            List<Long> newIdsDpt = departments.get(departmentId).getParcelIds();
            newIdsDpt.remove(parcelId);
            departments.get(departmentId).setParcelIds(newIdsDpt);
            List<Long> newIdsCourier = courierService.getCourierById(courierId).getParcelIds();
            newIdsCourier.add(parcelId);
            Courier courier = courierService.getCourierById(courierId);
            courier.setParcelIds(newIdsCourier);
            courierService.updateCourier(courier, courierId);
        }
    }

    public void addParcel(Long departmentId, Parcel parcel) {
        List<Long> newIds = this.departments.get(departmentId).getParcelIds();
        newIds.add(parcel.getParcelId());
        this.departments.get(departmentId).setParcelIds(newIds);
        parcelService.addParcel(parcel);
    }

    public void updateParcel(Long departmentId, Parcel parcel) {
        if (this.departments.get(departmentId).getParcelIds().contains(parcel.getParcelId())) {
            parcelService.updateParcel(parcel);
        }
    }

    public void deleteParcel(Long departmentId, Long parcelId) {
        if (this.departments.get(departmentId).getParcelIds().contains(parcelId)) {
            parcelService.deleteParcel(parcelId);
        }
    }

    public List<Parcel> getAllParcels(Long departmentId) {
        List<Parcel> result = new LinkedList<>();
        for (Parcel parcel: parcelService.getAllParcels()) {
            if (departments.get(departmentId).getParcelIds().contains(parcel.getParcelId())) {
                result.add(parcel);
            }
        }

        return result;
    }

    public Parcel getParcelById(Long departmentId, Long parcelId) {
        Parcel result = new Parcel();
        for (Parcel parcel: parcelService.getAllParcels()) {
            if (departments.get(departmentId).getParcelIds().contains(parcel.getParcelId())) {
                if (parcel.getParcelId() == parcelId) {
                    result = parcel;
                }
            }
        }

        return result;
    }

}
