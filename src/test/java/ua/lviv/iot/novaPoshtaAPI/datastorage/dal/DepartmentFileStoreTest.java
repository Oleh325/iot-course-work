package ua.lviv.iot.novaPoshtaAPI.datastorage.dal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.lviv.iot.novaPoshtaAPI.model.Department;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentFileStoreTest {

    HashMap<Long, Department> map = new HashMap<>();
    DepartmentFileStore departmentFileStore = new DepartmentFileStore();

    @BeforeEach
    void setUp() {
        List<Long> parcelIds1 = new LinkedList<>();
        parcelIds1.add(1L);
        parcelIds1.add(2L);
        parcelIds1.add(3L);
        Department department1 = new Department(1L, "Nyzynna street 5 (Lviv)",
                "08:00-19:00", parcelIds1);
        List<Long> parcelIds2 = new LinkedList<>();
        parcelIds2.add(4L);
        parcelIds2.add(5L);
        Department department2 = new Department(2L, "Short street 7 (Lviv)",
                "09:00-19:00", parcelIds2);
        List<Long> parcelIds3 = new LinkedList<>();
        parcelIds3.add(6L);
        Department department3 = new Department(3L, "Horodots'ka street 69 (Lviv)",
                "06:00-21:00", parcelIds3);
        List<Long> parcelIds4 = new LinkedList<>();
        parcelIds4.add(7L);
        parcelIds4.add(8L);
        parcelIds4.add(9L);
        Department department4 = new Department(4L, "Svobody avenue 8 (Rivne)",
                "07:00-21:00", parcelIds4);
        List<Long> parcelIds5 = new LinkedList<>();
        parcelIds5.add(10L);
        parcelIds5.add(11L);
        Department department5 = new Department(5L, "Lvivs'ka street 12 (Kryvyi Rih)",
                "10:00-18:00", parcelIds5);


        map.put(department1.getDepartmentId(), department1);
        map.put(department2.getDepartmentId(), department2);
        map.put(department3.getDepartmentId(), department3);
        map.put(department4.getDepartmentId(), department4);
        map.put(department5.getDepartmentId(), department5);
    }

    @Test
    void SaveAndLoadDepartments() throws IOException {
        departmentFileStore.saveDepartments(map, "res\\test\\");
        HashMap<Long, Department> resultMap = departmentFileStore.loadDepartments("res\\test\\");
        String expected = "[Department(departmentId=1, location=Nyzynna street 5 (Lviv), " +
                "workingHours=08:00-19:00, parcelIds=[1, 2, 3]), " +
                "Department(departmentId=2, location=Short street 7 (Lviv), " +
                "workingHours=09:00-19:00, parcelIds=[4, 5]), " +
                "Department(departmentId=3, location=Horodots'ka street 69 (Lviv), " +
                "workingHours=06:00-21:00, parcelIds=[6]), " +
                "Department(departmentId=4, location=Svobody avenue 8 (Rivne), " +
                "workingHours=07:00-21:00, parcelIds=[7, 8, 9]), " +
                "Department(departmentId=5, location=Lvivs'ka street 12 (Kryvyi Rih), " +
                "workingHours=10:00-18:00, parcelIds=[10, 11])]";
        assertEquals(expected, resultMap.values().stream().toList().toString());
    }
}