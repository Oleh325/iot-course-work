package ua.lviv.iot.novaPoshtaAPI.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuppressFBWarnings
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
        return "parcelId, weightInKgs, heightInCm, "
                + "widthInCm, lengthInCm, origin, "
                + "destination, location, dateSent";
    }

    public String toCSV() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(dateSent);
        return parcelId + ", " + weightInKgs + ", " + heightInCm + ", "
                + widthInCm + ", " + lengthInCm + ", " + origin + ", "
                + destination + ", " + location + ", " + date;
    }
}
