package ua.lviv.iot.novaPoshtaAPI.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuppressFBWarnings
public class Department extends Item {
    private Long departmentId;
    private String location;
    private String workingHours;
    private List<Long> parcelIds;

    @Override
    public String getHeaders() {
        return "departmentId, location, workingHours, parcelIds";
    }

    @Override
    public String toCSV() {
        return departmentId + ", " + location + ", " + workingHours + ", " + parcelIds;
    }
}
