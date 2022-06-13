package ua.lviv.iot.novaPoshtaAPI.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Parcel {
    private Long parcelId;
    private float weightInKgs;
    private float heightInCm;
    private float widthInCm;
    private float lengthInCm;
    private String origin;
    private String destination;
    private String location;
    private Date dateSent;

    public String getHeaders() {
        return "parcelId, weightInKgs, heightInCm, " +
                "widthInCm, lengthInCm, origin, " +
                "destination, location, dateSent";
    }

    public String toCSV() {
        return parcelId + ", " + weightInKgs + ", " + heightInCm + ", "
                + widthInCm + ", " + lengthInCm + ", " + origin + ", "
                + destination + ", " + location + ", " + dateSent;
    }
}
