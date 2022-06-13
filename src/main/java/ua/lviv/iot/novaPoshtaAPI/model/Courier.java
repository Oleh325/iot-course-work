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
public class Courier {
    private Long courierId;
    private Long departmentId;
    private String fullName;
    private boolean isWorking;
    private List<Long> parcelIds;

    public String getHeaders() {
        return "courierId, departmentId, fullName, " +
                "isWorking, parcelIds";
    }

    public String toCSV() {
        return courierId + ", " + departmentId + ", " + fullName + ", "
                + isWorking + ", " + parcelIds;
    }
}
