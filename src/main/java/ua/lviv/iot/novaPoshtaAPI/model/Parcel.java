package ua.lviv.iot.novaPoshtaAPI.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuppressFBWarnings
public class Parcel extends Item {
    @NotNull(message = "Parcel ID shouldn't be null!")
    private Long parcelId;
    @NotNull(message = "Parcel's weight shouldn't be null!")
    private float weightInKgs;
    private float heightInCm;
    private float widthInCm;
    private float lengthInCm;
    private String origin;
    @NotNull(message = "Parcel's destination shouldn't be null!")
    private String destination;
    @NotNull(message = "Parcel's location shouldn't be null!")
    private String location;
    @NotNull(message = "Parcel's date sent shouldn't be null!")
    private Date dateSent;

    @Override
    public String getHeaders() {
        return "parcelId, weightInKgs, heightInCm, "
                + "widthInCm, lengthInCm, origin, "
                + "destination, location, dateSent";
    }

    @Override
    public String toCSV() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(dateSent);
        return parcelId + ", " + weightInKgs + ", " + heightInCm + ", "
                + widthInCm + ", " + lengthInCm + ", " + origin + ", "
                + destination + ", " + location + ", " + date;
    }
}
