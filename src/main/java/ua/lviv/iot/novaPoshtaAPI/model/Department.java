package ua.lviv.iot.novaPoshtaAPI.model;

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
public class Department {
    private Long departmentId;
    private String location;
    private String workingHours;
    private List<Long> parcelIds;

    public String getHeaders() {
        return "departmentId, location, workingHours, parcelIds";
    }

    public String toCSV() {
        return departmentId + ", " + location + ", " + workingHours + ", " + parcelIds;
    }
}
