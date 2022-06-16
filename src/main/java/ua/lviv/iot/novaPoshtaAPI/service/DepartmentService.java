package ua.lviv.iot.novaPoshtaAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lviv.iot.novaPoshtaAPI.datastorage.dal.DepartmentFileStore;
import ua.lviv.iot.novaPoshtaAPI.model.Courier;
import ua.lviv.iot.novaPoshtaAPI.model.Department;
import ua.lviv.iot.novaPoshtaAPI.model.Parcel;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.text.ParseException;
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
    @Autowired
    DepartmentFileStore departmentFileStore;

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
        if (departments.get(departmentId).getParcelIds().contains(parcelId)
                && Objects.equals(courierService.getCourierById(courierId).getDepartmentId(), departmentId)) {
            List<Long> newIdsDpt = departments.get(departmentId).getParcelIds();
            newIdsDpt.remove(parcelId);
            departments.get(departmentId).setParcelIds(newIdsDpt);
            List<Long> newIdsCourier = courierService.getCourierById(courierId).getParcelIds();
            newIdsCourier.add(parcelId);
            Courier courier = courierService.getCourierById(courierId);
            courier.setParcelIds(newIdsCourier);
            courierService.updateCourier(courier, courierId);
            Parcel newParcel = parcelService.getParcelById(parcelId);
            newParcel.setLocation("Courier is delivering the parcel");
            parcelService.deleteParcel(parcelId);
            parcelService.addParcel(newParcel);
        }
    }

    public void addParcel(Long departmentId, Parcel parcel) {
        List<Long> newIds = this.departments.get(departmentId).getParcelIds();
        newIds.add(parcel.getParcelId());
        this.departments.get(departmentId).setParcelIds(newIds);
        parcel.setLocation(this.departments.get(departmentId).getLocation());
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
                if (Objects.equals(parcel.getParcelId(), parcelId)) {
                    result = parcel;
                }
            }
        }

        return result;
    }

    public void deliverParcel(Long departmentIdFrom, Long departmentIdTo, Long parcelId) {
        if (departments.get(departmentIdFrom).getParcelIds().contains(parcelId)) {
            List<Long> newIdsFrom = departments.get(departmentIdFrom).getParcelIds();
            newIdsFrom.remove(parcelId);
            departments.get(departmentIdFrom).setParcelIds(newIdsFrom);
            List<Long> newIdsTo = departments.get(departmentIdTo).getParcelIds();
            newIdsTo.add(parcelId);
            departments.get(departmentIdTo).setParcelIds(newIdsTo);
            Parcel newParcel = parcelService.getParcelById(parcelId);
            newParcel.setLocation(departments.get(departmentIdTo).getLocation());
            parcelService.deleteParcel(parcelId);
            parcelService.addParcel(newParcel);
        }
    }

    @PreDestroy
    private void saveDepartments() {
        List<Department> list = this.departments.values().stream().toList();
        departmentFileStore.saveDepartments(list, false);
    }

    @PostConstruct
    private void loadDepartments() throws IOException, ParseException {
        if (departmentFileStore.loadDepartments(false) != null) {
            List<Department> list = departmentFileStore.loadDepartments(false);
            for (Department department: list) {
                this.departments.put(department.getDepartmentId(), department);
            }
        }
    }

}
