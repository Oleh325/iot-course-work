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
    private ParcelService parcelService;
    @Autowired
    private CourierService courierService;
    @Autowired
    private DepartmentFileStore departmentFileStore;

    private HashMap<Long, Department> departments = new HashMap<>();

    public List<Department> getAllDepartments() {
        return new LinkedList<>(this.departments.values());
    }

    public Department getDepartmentById(Long departmentId) {
        return this.departments.get(departmentId);
    }

    public void addDepartment(Department department) throws IOException {
        List<Long> parcelIds = new LinkedList<>();
        department.setParcelIds(parcelIds);
        this.departments.put(department.getDepartmentId(), department);
        saveDepartments();
    }

    public void updateDepartment(Department department, Long departmentId) throws IOException {
        if (this.departments.get(departmentId) != null) {
            if (department.getDepartmentId() == null) {
                department.setDepartmentId(departmentId);
            }
            if (department.getLocation() == null) {
                department.setLocation(this.departments.get(departmentId).getLocation());
            }
            if (department.getWorkingHours() == null) {
                department.setWorkingHours(this.departments.get(departmentId).getWorkingHours());
            }
            if (department.getParcelIds() == null) {
                department.setParcelIds(this.departments.get(departmentId).getParcelIds());
            }
            this.departments.put(departmentId, department);
            saveDepartments();
        }
    }

    public void deleteDepartment(Long departmentId) throws IOException {
        for (Long parcelId: this.departments.get(departmentId).getParcelIds()) {
            parcelService.deleteParcel(parcelId);
        }
        for (Courier courier: courierService.getAllCouriers()) {
            if (Objects.equals(courier.getDepartmentId(), departmentId)) {
                courierService.deleteCourier(courier.getCourierId());
            }
        }
        this.departments.remove(departmentId);
        saveDepartments();
    }

    public void giveParcelToCourier(Long departmentId, Long courierId, Long parcelId) throws IOException {
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
            parcelService.saveParcels();
            courierService.saveCouriers();
            saveDepartments();
        }
    }

    public void addParcel(Long departmentId, Parcel parcel) throws IOException {
        List<Long> newIds = this.departments.get(departmentId).getParcelIds();
        newIds.add(parcel.getParcelId());
        this.departments.get(departmentId).setParcelIds(newIds);
        parcel.setLocation(this.departments.get(departmentId).getLocation());
        parcelService.addParcel(parcel);
        parcelService.saveParcels();
    }

    public void updateParcel(Long departmentId, Parcel parcel, Long parcelId) throws IOException {
        if (this.departments.get(departmentId).getParcelIds().contains(parcel.getParcelId())) {
            parcelService.updateParcel(parcel, parcelId);
            parcelService.saveParcels();
        }
    }

    public void deleteParcel(Long departmentId, Long parcelId) throws IOException {
        if (this.departments.get(departmentId).getParcelIds().contains(parcelId)) {
            parcelService.deleteParcel(parcelId);
            parcelService.saveParcels();
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

    public void deliverParcel(Long departmentIdFrom, Long departmentIdTo, Long parcelId) throws IOException {
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
            parcelService.saveParcels();
            saveDepartments();
        }
    }

    public void addCourier(Long departmentId, Courier courier) throws IOException {
        if (this.departments.get(departmentId) != null) {
            courier.setDepartmentId(departmentId);
            courierService.addCourier(courier);
            courierService.saveCouriers();
        }
    }

    public void updateCourier(Long departmentId, Courier courier, Long courierId) throws IOException {
        if (this.departments.get(departmentId) != null) {
            courier.setDepartmentId(departmentId);
            courierService.updateCourier(courier, courierId);
            courierService.saveCouriers();
        }
    }

    public void deleteCourier(Long departmentId, Long courierId) throws IOException {
        if (this.departments.get(departmentId) != null) {
            courierService.deleteCourier(courierId);
            courierService.saveCouriers();
        }
    }

    public List<Courier> getAllCouriers(Long departmentId) {
        List<Courier> result = new LinkedList<>();
        for (Courier courier: courierService.getAllCouriers()) {
            if (Objects.equals(courier.getDepartmentId(), departmentId)) {
                result.add(courier);
            }
        }

        return result;
    }

    public Courier getCourierById(Long departmentId, Long courierId) {
        Courier result = new Courier();
        for (Courier courier: courierService.getAllCouriers()) {
            if (Objects.equals(courier.getDepartmentId(), departmentId)) {
                if (Objects.equals(courier.getCourierId(), courierId)) {
                    result = courier;
                }
            }
        }

        return result;
    }

    @PreDestroy
    public void saveDepartments() throws IOException {
        departmentFileStore.save(this.departments, "res\\");
    }

    @PostConstruct
    public void loadDepartments() throws IOException, ParseException {
        if (departmentFileStore.load("res\\") != null) {
            this.departments = departmentFileStore.load("res\\");
        }
    }

}
