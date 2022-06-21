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
public class Courier {
    private Long courierId;
    private Long departmentId;
    private String fullName;
    private boolean isWorking;
    private List<Long> parcelIds;

    public String getHeaders() {
        return "courierId, departmentId, fullName, "
                + "isWorking, parcelIds";
    }

    public String toCSV() {
        return courierId + ", " + departmentId + ", " + fullName + ", "
                + isWorking + ", " + parcelIds;
    }
}
