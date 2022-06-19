package ua.lviv.iot.novaPoshtaAPI.datastorage.dal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.lviv.iot.novaPoshtaAPI.model.Parcel;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;


class ParcelFileStoreTest {

    HashMap<Long, Parcel> map = new HashMap<>();
    ParcelFileStore parcelFileStore = new ParcelFileStore();

    @BeforeEach
    void setUp() throws ParseException {
        Parcel parcel1 = new Parcel(1L, 10.0f, 15.0f, 20.0f, 30.0f, "Lviv",
                "Kharkiv", "Kharkiv", new SimpleDateFormat("yyyy-MM-dd").parse("2022-06-14"));
        Parcel parcel2 = new Parcel(2L, 5.0f, 12.0f, 10.0f, 20.0f, "Lviv",
                "Kyiv", "Lviv", new SimpleDateFormat("yyyy-MM-dd").parse("2022-06-12"));
        Parcel parcel3 = new Parcel(3L, 14.0f, 18.0f, 14.0f, 25.0f, "Kyiv",
                "Lviv", "Kyiv", new SimpleDateFormat("yyyy-MM-dd").parse("2022-05-24"));
        Parcel parcel4 = new Parcel(4L, 11.0f, 15.0f, 20.0f, 15.0f, "Lviv",
                "Ternopil", "Lviv", new SimpleDateFormat("yyyy-MM-dd").parse("2022-06-13"));
        Parcel parcel5 = new Parcel(5L, 3.0f, 25.0f, 30.0f, 31.0f, "Kryvyi Rih",
                "Kyiv", "Kyiv", new SimpleDateFormat("yyyy-MM-dd").parse("2022-06-02"));

        map.put(parcel1.getParcelId(), parcel1);
        map.put(parcel2.getParcelId(), parcel2);
        map.put(parcel3.getParcelId(), parcel3);
        map.put(parcel4.getParcelId(), parcel4);
        map.put(parcel5.getParcelId(), parcel5);
    }

    @Test
    void SaveAndLoadParcels() throws IOException, ParseException {
        parcelFileStore.saveParcels(map, "res\\test\\");
        HashMap<Long, Parcel> resultMap = parcelFileStore.loadParcels("res\\test\\");
        String expected = "[Parcel(parcelId=1, weightInKgs=10.0, heightInCm=15.0, widthInCm=20.0, lengthInCm=30.0, " +
                "origin=Lviv, destination=Kharkiv, location=Kharkiv, dateSent=Tue Jun 14 00:00:00 EEST 2022), " +
                "Parcel(parcelId=2, weightInKgs=5.0, heightInCm=12.0, widthInCm=10.0, lengthInCm=20.0, " +
                "origin=Lviv, destination=Kyiv, location=Lviv, dateSent=Sun Jun 12 00:00:00 EEST 2022), " +
                "Parcel(parcelId=3, weightInKgs=14.0, heightInCm=18.0, widthInCm=14.0, lengthInCm=25.0, " +
                "origin=Kyiv, destination=Lviv, location=Kyiv, dateSent=Tue May 24 00:00:00 EEST 2022), " +
                "Parcel(parcelId=4, weightInKgs=11.0, heightInCm=15.0, widthInCm=20.0, lengthInCm=15.0, " +
                "origin=Lviv, destination=Ternopil, location=Lviv, dateSent=Mon Jun 13 00:00:00 EEST 2022), " +
                "Parcel(parcelId=5, weightInKgs=3.0, heightInCm=25.0, widthInCm=30.0, lengthInCm=31.0, " +
                "origin=Kryvyi Rih, destination=Kyiv, location=Kyiv, dateSent=Thu Jun 02 00:00:00 EEST 2022)]";
        Assertions.assertEquals(expected, resultMap.values().stream().toList().toString());
    }

}