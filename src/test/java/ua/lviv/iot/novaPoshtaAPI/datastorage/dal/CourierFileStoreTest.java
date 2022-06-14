package ua.lviv.iot.novaPoshtaAPI.datastorage.dal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.lviv.iot.novaPoshtaAPI.model.Courier;

import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourierFileStoreTest {

    List<Courier> list = new LinkedList<>();
    CourierFileStore courierFileStore = new CourierFileStore();

    @BeforeEach
    void setUp() throws ParseException {
        List<Long> parcelIds1 = new LinkedList<>();
        parcelIds1.add(1L);
        parcelIds1.add(2L);
        parcelIds1.add(3L);
        Courier courier1 = new Courier(1L, 1L, "Pavelchak Andrii",
                false, parcelIds1);
        List<Long> parcelIds2 = new LinkedList<>();
        parcelIds2.add(4L);
        parcelIds2.add(5L);
        Courier courier2 = new Courier(2L, 1L, "Veres Zenovii", false, parcelIds2);
        List<Long> parcelIds3 = new LinkedList<>();
        parcelIds3.add(6L);
        Courier courier3 = new Courier(3L, 1L, "Pavlyk Vadym",
                false, parcelIds3);
        List<Long> parcelIds4 = new LinkedList<>();
        parcelIds4.add(7L);
        parcelIds4.add(8L);
        parcelIds4.add(9L);
        Courier courier4 = new Courier(4L, 2L, "Shvets' Yulia",
                true, parcelIds4);
        List<Long> parcelIds5 = new LinkedList<>();
        parcelIds5.add(10L);
        parcelIds5.add(11L);
        Courier courier5 = new Courier(5L, 2L, "Mel'nyk Roman",
                true, parcelIds5);


        list.add(courier1);
        list.add(courier2);
        list.add(courier3);
        list.add(courier4);
        list.add(courier5);
    }

    @Test
    void SaveAndLoadCouriers() throws IOException, ParseException {
        courierFileStore.saveCouriers(list, true);
        List<Courier> resultList = courierFileStore.loadCouriers(true);
        String expected = "[Courier(courierId=1, departmentId=1, fullName=Pavelchak Andrii, " +
                "isWorking=false, parcelIds=[1, 2, 3]), " +
                "Courier(courierId=2, departmentId=1, fullName=Veres Zenovii, " +
                "isWorking=false, parcelIds=[4, 5]), " +
                "Courier(courierId=3, departmentId=1, fullName=Pavlyk Vadym, " +
                "isWorking=false, parcelIds=null), " +
                "Courier(courierId=4, departmentId=2, fullName=Shvets' Yulia, " +
                "isWorking=true, parcelIds=[7, 8, 9]), " +
                "Courier(courierId=5, departmentId=2, fullName=Mel'nyk Roman, " +
                "isWorking=true, parcelIds=[10, 11])]";
        assertEquals(expected, resultList.toString());
    }
}